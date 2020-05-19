<?php
include_once 'BL/ExcelImporter.php';
include_once 'BL/BookImporter.php';
include_once 'DBObject/OldDB.php';
include_once 'DBObject/NewDB.php';

if (isset($_POST["submit"])) {
    try {


        //Objects init
        $excelImporter = new ExcelImporter();
        $bookImporter = new BookImporter();
        $oldDB = new OldDB();
        $newDB = new NewDB();

        //Calls
        $newDB->deleteAll();
        $extracts = $excelImporter->import($_FILES['file']['name']);

        $bookIds = $excelImporter->extractBookIdsFromImport($extracts);
        $mappedIdentifiers = $oldDB->getMappedIdentifiers($bookIds);
        $sanitizedBookIds = $bookImporter->sanitizeDuplicates($mappedIdentifiers, $extracts);
        $booksIdentifiers = $oldDB->getBooksIdentifiers($sanitizedBookIds);
        
        //bookImporter has a key(restKey) that must be updated if used in the future
        $bookImporterResponses = $bookImporter->importBooks($booksIdentifiers);

        //Get the old values of book If not found on the ISBN API
        $unfoundBooks = $oldDB->getBooksFromIds($bookImporterResponses->getUnfoundIds());

        //TODO Manage the url given to download it in our repository and database (with id)
        $newDB->createBooks($bookImporterResponses->getBooks());
        $newDB->createBooks($unfoundBooks);

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
        }*/
    } catch
    (Exception $e) {
        die($e);
    }
}

