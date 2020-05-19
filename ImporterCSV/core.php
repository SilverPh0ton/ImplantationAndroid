<?php
include_once 'BL/ExcelImporter.php';
include_once 'BL/BookImporter.php';
include_once 'DBObject/OldDB.php';
include_once 'DBObject/NewDB.php';

if(isset($_POST["submit"]))
{
    try{


    //Objects init
    $excelImporter = new ExcelImporter();
    $bookImporter = new BookImporter();
    $oldDB = new OldDB();
    $newDB = new NewDB();

    //Calls
    $newDB->deleteAll();
    $extracts = $excelImporter->import($_FILES['file']['name']);

    $bookIds = $excelImporter->extractBookIdsFromImport($extracts);
    $booksIdentifiers  = $oldDB->getBooksIdentifers($bookIds);
    //TODO Get rid of duplicates here And redirect on the single unit
    //bookImporter has a key(restKey) that must be update if used in the future
    $bookImporterResponses = $bookImporter->importBooks($booksIdentifiers);
    //print_r($books);
    //TODO Manage the url given to download it in our repository and database (with id)
    $newDB->createBooks($bookImporterResponses->getBooks());

    //Get the old values of book If not found on the ISBN API
    $unfoundBooks = $oldDB->getBooksFromIds($bookImporterResponses->getUnfoundIds());

    //GET USER INFO FROM OLD BD
    $userIds = $excelImporter->extractUserIdsFromImport($extracts);

    //VALIDATE INFO IS CORRECT
    //SAVE USERS IN NEW BD

    //SAVE CONCESSION WITH OLD INFO + ID'S

}
catch(Exception $e)
{
    die($e);
}
}

