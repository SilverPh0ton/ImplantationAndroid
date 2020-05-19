<?php
include_once 'BL/ExcelImporter.php';
include_once 'BL/BookImporter.php';
include_once 'DBObject/OldDB.php';
include_once 'DBObject/NewDB.php';

if (isset($_POST["submit"])) {
    try {

        $useAPI = false;
        //Objects init
        $excelImporter = new ExcelImporter();
        $bookImporter = new BookImporter();
        $oldDB = new OldDB();
        $newDB = new NewDB();

        //Remove Data from new DB
        $newDB->deleteAll();

        //Get ExcelExtracts Entities From Xlsx (idConcession aka ReferenceCode, idBook, idUser)
        $extracts = $excelImporter->import($_FILES['file']['name']);

        $unfoundBooks = importBooks($excelImporter, $extracts, $oldDB, $bookImporter, $newDB, $useAPI);

        //GET USER INFO FROM OLD BD
        $userIds = $excelImporter->extractUserIdsFromImport($extracts);

        //VALIDATE INFO IS CORRECT
        //SAVE USERS IN NEW BD

        //SAVE CONCESSION WITH OLD INFO + ID'S
        if ($unfoundBooks > 0) {
            ?>
            <h3>Livres non trouvés sur l'API et utilisant les vieilles données</h3>
            <table id="books">
                <tr>
                    <td>idBook</td>
                    <td>title</td>
                    <td>author</td>
                    <td>publisher</td>
                    <td>edition</td>
                    <td>barcode</td>
                    <td>urlPhoto</td>
                </tr>

                <?php
                for ($i = 0; $i < count($unfoundBooks); $i++) {
                    ?>
                    <tr>
                        <td><?= $unfoundBooks[$i]->getIdBook() ?></td>
                        <td><?= $unfoundBooks[$i]->getTitle() ?></td>
                        <td><?= $unfoundBooks[$i]->getAuthor() ?></td>
                        <td><?= $unfoundBooks[$i]->getPublisher() ?></td>
                        <td><?= $unfoundBooks[$i]->getEdition() ?></td>
                        <td><?= $unfoundBooks[$i]->getBarcode() ?></td>
                        <td><?= $unfoundBooks[$i]->getUrlPhoto() ?></td>
                    </tr>
                    <?php

                }
                ?>
            </table>
            <?php
        }
    } catch
    (Exception $e) {
        die($e);
    }
}

/**
 * @param ExcelImporter $excelImporter
 * @param $extracts
 * @param OldDB $oldDB
 * @param BookImporter $bookImporter
 * @param NewDB $newDB
 * @return array
 * @throws Exception
 */
function importBooks(ExcelImporter $excelImporter, $extracts, OldDB $oldDB, BookImporter $bookImporter, NewDB $newDB, $useAPI)
{
    //List the bookIds from Extracts
    $bookIds = $excelImporter->extractBookIdsFromImport($extracts);

    //Map the duplicate ISBN id together (ex. ['9781923829' => [1,456,1235]])
    $mappedIdentifiers = $oldDB->getMappedIdentifiers($bookIds);

    //Replace duplicate values with only 1 instance in Extracts
    $sanitizedBookIds = $bookImporter->sanitizeDuplicates($mappedIdentifiers, $extracts);


    if($useAPI)
    {
        //Get the ISBN of the bookIds (BookIdentifier Entity)
        $booksIdentifiers = $oldDB->getBooksIdentifiers($sanitizedBookIds);

        //Import From Api the info of the books
        //bookImporter has a key(restKey) that must be updated if used in the future
        $bookImporterResponses = $bookImporter->importBooks($booksIdentifiers);

        //Get the old values of book If not found on the ISBN API
        $unfoundBooks = $oldDB->getBooksFromIds($bookImporterResponses->getUnfoundIds());

        //TODO Manage the url given to download it in our repository and database (with id)
        //Save books in new Database
        $newDB->createBooks($bookImporterResponses->getBooks());
        $newDB->createBooks($unfoundBooks);
        return $unfoundBooks;
    }

    $bookIds = array();
    foreach ($sanitizedBookIds as $id)
    {
        array_push($bookIds, $id[0]);
    }
    $books = $oldDB->getBooksFromIds($bookIds);
    $newDB->createBooks($books);
    return $books;
}

