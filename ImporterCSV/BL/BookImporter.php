<?php
//Les includes.
include_once 'Entities/Book.php';

class BookImporter
{
    //Déclaration des variables.
    private $restKey = '44069_1e599bc965ce65638d7bb55bba332462';
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
        $books = array();

        foreach ($booksIdentifiers as $bookIdentifier) {
            $url = "https://api2.isbndb.com/book/" . $bookIdentifier->getIsbn();

            $headers = array(
                "Content-Type: application/json",
                "Authorization: " . $this->restKey
            );

            curl_setopt($this->rest,CURLOPT_URL,$url);
            curl_setopt($this->rest,CURLOPT_HTTPHEADER,$headers);
            curl_setopt($this->rest,CURLOPT_RETURNTRANSFER, true);

            array_push($books, $this->JSONconverter(curl_exec($this->rest), $bookIdentifier->getIdBook()));
        }

        return $books;
    }

    //Fonction convertisant le retour de l'API en JSON, en object de type livre.
    private function JSONconverter($response, $idBook) {
        $book = new Book();
        $responseBook = json_decode($response, true);

        $book->setIdBook($idBook);
        $book->setAuthor($this->concatAuthors($responseBook["book"]["authors"]));
        $book->setBarcode($responseBook["book"]["isbn13"]);
        $book->setEdition(null);
        $book->setPublisher($responseBook["book"]["publisher"]);
        $book->setTitle($responseBook["book"]["title"]);
        $book->setUrlPhoto($responseBook["book"]["image"]);

        return $book;
    }

    //Fonction servant à concaténer les auteurs en un string.
    private function concatAuthors($authors) {
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

        return $authors[0];
    }




}