<?php
include_once 'BL/ExcelImporter.php';
include_once 'DBObject/OldDB.php';
if(isset($_POST["submit"]))
{
    //Objects init
    $excelImporter = new ExcelImporter();
    $oldDB = new OldDB();

    //Calls
    $extracts = $excelImporter->import($_FILES['file']['name']);

    $bookIds = $excelImporter->extractBookIdsFromImport($extracts);
    $booksIdentifiers  = $oldDB->getBooksIdentifers($bookIds);
    //GETBOOKIDENTIFIERS FROM OLD BD

    //GETOBJECTS FROM IMPORTER
    //SAVE BOOKS IN NEW BD

    //GET USER INFO FROM OLD BD

    //VALIDATE INFO IS CORRECT
    //SAVE USERS IN NEW BD

    //SAVE CONCESSION WITH OLD INFO + ID'S



}

