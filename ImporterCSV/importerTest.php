<?php
//Les includes.
include_once("Entities/BookIdentifier.php");
include_once("BL/BookImporter.php");

//Déclaration des variables.
$bookImporter = new BookImporter();
$booksIdentifiers = array();

$book1 = new BookIdentifier(20, "9788437624871"); //vrai donnée 9788437624877
$book2 = new BookIdentifier(35, "9782253260141");

array_push($booksIdentifiers, $book1);
array_push($booksIdentifiers, $book2);

$bookImporterResponses = $bookImporter->importBooks($booksIdentifiers);

print_r($bookImporterResponses);
