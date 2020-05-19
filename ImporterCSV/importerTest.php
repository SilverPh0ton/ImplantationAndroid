<?php
//Les includes.
include_once("Entities/BookIdentifier.php");
include_once("BL/BookImporter.php");

//Déclaration des variables.
$bookImporter = new BookImporter();
$booksIdentifiers = array();
$books = array();

$book1 = new BookIdentifier(20, "9788437624871");
$book2 = new BookIdentifier(35, "9782253260141");

array_push($booksIdentifiers, $book1);
array_push($booksIdentifiers, $book2);

$books = $bookImporter->importBooks($booksIdentifiers);

print_r($books);