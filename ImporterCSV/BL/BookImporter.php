<?php
//Les includes.
include_once 'Entities/Book.php';
include_once 'Entities/BookImporterResponses.php';

class BookImporter
{
    //Déclaration des variables.
    //44069_1e599bc965ce65638d7bb55bba332462
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
    public function importBooks ($booksIdentifiers, $apiKey) {
        $bookImporterResponses = new BookImporterResponses();

        foreach ($booksIdentifiers as $bookIdentifier) {
            $url = "https://www.googleapis.com/books/v1/volumes?q=isbn:".$bookIdentifier->getIsbn()."&key=".$apiKey."&orderBy=newest" ;

            $page = file_get_contents($url);

            $response = json_decode($page, true);

            $this->convertApiResponseToBooks($response, $bookIdentifier, $bookImporterResponses);
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
                array_push($sanitizedBookIds, $identifiers[0]);
            }
        }
        return$sanitizedBookIds;
    }

    private function replaceInExtract($sanitizedExtracts, $identifier, $idKept)
    {
        foreach($sanitizedExtracts as $extract)
        {
            if($extract->getIdBook() == $identifier)
            {
                $extract->setIdBook($idKept);
            }
        }
    }


    //Fonction servant à concaténer les auteurs en un string.
    private function concatInfos($infos) {
        if (!is_null($infos)) {
            if (sizeof($infos) > 1) {
                $concatInfo = "";
                $ctr = 0;

                foreach ($infos as $info) {
                    if ($ctr > 0) {
                        $concatInfo .= ", " . utf8_decode($info);
                    } else {
                        $concatInfo .= utf8_decode($info);
                    }

                    $ctr++;
                }

                return $concatInfo;
            }
        }

        return null;
    }

    private function convertApiResponseToBooks($response, $bookIdentifier, $bookImporterResponses)
    {
        if($response['totalItems'] != 0)
        {
            $volumeInfos = $response['items'][0]['volumeInfo'];
            if(isset($volumeInfos))
            {
                $title = utf8_decode($volumeInfos['title']);
                $authors = null;
                $publisher = null;
                $urlPhoto = null;


                if(isset($volumeInfos['authors']))
                {
                    $authors = $volumeInfos['authors'];
                }

                if(isset($volumeInfos['publisher']))
                {
                    $publisher = utf8_decode($volumeInfos['publisher']);
                }

                if(isset($volumeInfos['imageLinks']))
                {
                    if(isset($volumeInfos['imageLinks']['small']))
                    {
                        $urlPhoto = $volumeInfos['imageLinks']['small'];
                    }
                    else if(isset($volumeInfos['imageLinks']['thumbnail']))
                    {
                        $urlPhoto = $volumeInfos['imageLinks']['thumbnail'];
                    }
                    else if(isset($volumeInfos['imageLinks']['smallThumbnail']))
                    {
                        $urlPhoto = $volumeInfos['imageLinks']['smallThumbnail'];
                    }
                    else if(isset($volumeInfos['imageLinks']['medium']))
                    {
                        $urlPhoto = $volumeInfos['imageLinks']['medium'];
                    }
                    else if(isset($volumeInfos['imageLinks']['large']))
                    {
                        $urlPhoto = $volumeInfos['imageLinks']['large'];
                    }
                    else if(isset($volumeInfos['imageLinks']['extraLarge']))
                    {
                        $urlPhoto = $volumeInfos['imageLinks']['extraLarge'];
                    }
                }


                $book = new Book(
                    $bookIdentifier->getIdBook(),
                    $title,
                    $this->concatInfos($authors),
                    $publisher,
                    null,
                    $bookIdentifier->getIsbn(),
                    $urlPhoto
                );

                $bookImporterResponses->addBook($book);
            }
            else
            {
                $bookImporterResponses->addUnfoundId($bookIdentifier->getIdBook());
            }
        }
        else
        {
            $bookImporterResponses->addUnfoundId($bookIdentifier->getIdBook());
        }
    }


}