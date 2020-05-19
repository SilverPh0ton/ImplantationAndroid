<?php
//Les includes.
include_once 'Entities/Book.php';
include_once 'Entities/BookImporterResponses.php';

class BookImporter
{
    //Déclaration des variables.
    private $restKey = '44069_1e599bc965ce65638d7bb55bba332462'; //44069_1e599bc965ce65638d7bb55bba332462
    private $rest;

    //Constructeur
    public function __construct()
    {
        $this->rest = curl_init();
    }

    //Destructeur
    public function __destruct()
    {
        curl_close($this->rest);
    }

    //Fonction récupérant les informations d'un livre.
    public function importBooks ($booksIdentifiers) {
        $bookImporterResponses = new BookImporterResponses();

        foreach ($booksIdentifiers as $bookIdentifier) {
            $url = "https://api2.isbndb.com/book/" . $bookIdentifier->getIsbn();

            $headers = array(
                "Content-Type: application/json",
                "Authorization: " . $this->restKey
            );

            curl_setopt($this->rest,CURLOPT_URL,$url);
            curl_setopt($this->rest,CURLOPT_HTTPHEADER,$headers);
            curl_setopt($this->rest,CURLOPT_RETURNTRANSFER, true);

            $this->JSONconverter(curl_exec($this->rest), $bookIdentifier->getIdBook(), $bookImporterResponses);
        }

        return $bookImporterResponses;
    }

    public function sanitizeDuplicates($mappedIdentifiers, $extracts)
    {
        $sanitizedBookIds = array();
        foreach($mappedIdentifiers as $identifiers)
        {
            if(sizeof($identifiers) > 1)
            {
                $idKept = max($identifiers);
                foreach($identifiers as $identifier)
                {
                    if($identifier != $idKept)
                    {
                        $this->replaceInExtract($extracts, $identifier, $idKept);
                    }
                }
                array_push($sanitizedBookIds, $idKept);
            }
            else
            {
                array_push($sanitizedBookIds, $identifiers);
            }
        }
        return $sanitizedBookIds;
    }

    private function replaceInExtract($extracts, $identifier, $idKept)
    {
        foreach($extracts as $extract)
        {
            if($extract->getIdBook() == $identifier)
            {
                $extract->setIdBook($idKept);
            }
        }
    }

    //Fonction convertisant le retour de l'API en JSON, en object de type livre.
    private function JSONconverter($response, $idBook, $bookImporterResponses) {

        $responseBook = json_decode($response, true);

        if (isset($responseBook["errorMessage"])) {
            $bookImporterResponses->addUnfoundId($idBook);
        } else {
            $book = new Book(
                $idBook,
                $this->concatAuthors($responseBook["book"]["authors"]),
                $responseBook["book"]["isbn13"],
                null,
                $responseBook["book"]["publisher"],
                $responseBook["book"]["title"],
                $responseBook["book"]["image"]
            );

            $bookImporterResponses->addBook($book);
        }
    }

    //Fonction servant à concaténer les auteurs en un string.
    private function concatAuthors($authors) {
        if (!is_null($authors)) {
            if (sizeof($authors) > 1) {
                $concatAuthors = "";
                $ctr = 0;

                foreach ($authors as $author) {
                    if ($ctr > 0) {
                        $concatAuthors .= ", " . $author;
                    } else {
                        $concatAuthors .= $author;
                    }

                    $ctr++;
                }

                return $concatAuthors;
            }
        }

        return $authors[0];
    }




}