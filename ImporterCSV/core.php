<?php
include_once 'BL/ExcelImporter.php';
include_once 'BL/BookImporter.php';
include_once 'BL/UserImporter.php';
include_once 'BL/ConcessionImporter.php';
include_once 'DBObject/OldDB.php';
include_once 'DBObject/NewDB.php';

if (isset($_POST["submit"])) {
    try {
        $useAPI = false;
        $apiKey = "";
        if(!empty($_POST['apiKey']))
        {
            $useAPI = $_POST['field'];
            if($useAPI == 'option')
            {
                $useAPI = true;
            }

            $apiKey = $_POST['apiKey'];
        }
        //Objects init
        $excelImporter = new ExcelImporter();
        $bookImporter = new BookImporter();
        $userImporter = new UserImporter();
        $concessionImporter = new ConcessionImporter();
        $oldDB = new OldDB();
        $newDB = new NewDB();

        //Remove Data from new DB
        $newDB->deleteAll();

        //Get ExcelExtracts Entities From Xlsx (idConcession aka ReferenceCode, idBook, idUser)
        $extracts = $excelImporter->import($_FILES['file']['name']);
        $sanitizedExtract = $excelImporter->sanitized($extracts);

        $unfoundBooks = importBooks($excelImporter, $sanitizedExtract, $oldDB, $bookImporter, $newDB, $useAPI, $apiKey);
        print_r($sanitizedExtract);

        importUsers($excelImporter, $sanitizedExtract, $oldDB, $userImporter, $newDB);
        importConcessions($excelImporter, $sanitizedExtract, $oldDB, $concessionImporter, $newDB);

        if ($unfoundBooks > 0) {
            ?>
            <h3>Livres importés utilisant les vieilles données</h3>
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

                    if (!is_null($unfoundBooks[$i])) {
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
function importBooks(ExcelImporter $excelImporter, $extracts, OldDB $oldDB, BookImporter $bookImporter, NewDB $newDB, $useAPI, $apiKey)
{
    //List the bookIds from Extracts
    $bookIds = $excelImporter->extractBookIdsFromImport($extracts);
    array_pop($bookIds);

    //Map the duplicate ISBN id together (ex. ['9781923829' => [1,456,1235]])
    $mappedIdentifiers = $oldDB->getMappedIdentifiers($bookIds);

    //Replace duplicate values with only 1 instance in Extracts
    $sanitizedBookIds = $bookImporter->sanitizeDuplicates($mappedIdentifiers, $extracts);


    if($useAPI)
    {
        //Get the ISBN of the bookIds (BookIdentifier Entity)
        $booksIdentifiers = $oldDB->getBooksIdentifiers($sanitizedBookIds);

        //Import From Api the info of the books
        $bookImporterResponses = $bookImporter->importBooks($booksIdentifiers, $apiKey);

        //Get the old values of book If not found on the ISBN API
        $unfoundBooks = $oldDB->getBooksFromIds($bookImporterResponses->getUnfoundIds());
        
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

/**
 * @param ExcelImporter $excelImporter
 * @param $extracts
 * @param OldDB $oldDB
 * @param UserImporter $userImporter
 * @param NewDB $newDB
 * @return array
 * @throws Exception
 */
function importUsers(ExcelImporter $excelImporter, $extracts, OldDB $oldDB, UserImporter $userImporter, NewDB $newDB)
{
    //Get unique userIds from extracts
    $userIds = $excelImporter->extractUserIdsFromImport($extracts);
    array_pop($userIds);

    //Get Users Object from oldDB
    $users = $oldDB->getUsersFromIds($userIds);

    //Map of an email with each user associated with it
    $mappedEmails = $oldDB->getMappedEmails($users);

    //For emails with more than 1 user associated, generate temporary email
    $userImporter->sanitizeEmails($mappedEmails, $users);

    $newDB->createUsers($users);
}

/**
 * @param ExcelImporter $excelImporter
 * @param $extracts
 * @param OldDB $oldDB
 * @param ConcessionImporter $concessionImporter
 * @param NewDB $newDB
 * @throws Exception
 */
function importConcessions(ExcelImporter $excelImporter, $extracts, OldDB $oldDB, ConcessionImporter $concessionImporter, NewDB $newDB)
{
    $concessionIds = $excelImporter->extractConcessionIdsFromImport($extracts);
    array_pop($concessionIds);

    $concessions = $oldDB->getConcessionByIds($concessionIds);
    $concessionImporter->replaceConcessionBookIdsWithExtractBookIds($concessions, $extracts);
    $newDB->createConcessions($concessions);
}

