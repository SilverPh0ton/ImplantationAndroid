<?php
include_once 'BL/ExcelImporter.php';
include_once 'BL/BookImporter.php';
include_once 'DBObject/OldDB.php';
include_once 'DBObject/NewDB.php';

if(isset($_POST["submit"]))
{
    //Objects init
    $excelImporter = new ExcelImporter();
    $bookImporter = new BookImporter();
    $oldDB = new OldDB();
    $newDB = new NewDB();

    //Calls
    $extracts = $excelImporter->import($_FILES['file']['name']);

    $bookIds = $excelImporter->extractBookIdsFromImport($extracts);
    $booksIdentifiers  = $oldDB->getBooksIdentifers($bookIds);  //TODO SETUP OLD BD
    //IMPORTER HAS A KEY(restKey) THAT MUST BE UPDATED IF REUSED IN THE FUTURE
    $books = $bookImporter->importBooks($booksIdentifiers);

    //SAVE BOOKS IN NEW BD

    //GET USER INFO FROM OLD BD
    $userIds = $excelImporter->extractUserIdsFromImport($extracts);

    //VALIDATE INFO IS CORRECT
    //SAVE USERS IN NEW BD

    //SAVE CONCESSION WITH OLD INFO + ID'S



}

