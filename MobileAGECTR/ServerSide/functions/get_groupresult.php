<?php

require_once "config_bd.php";
include '../entity/GroupResult.php';
include '../entity/Book.php';

try{
    $groupresults = array();

    if(isset($_POST['search']))
    {
        $sql = "SELECT b.id, b.title, b.author, b.edition, b.publisher, b.barcode, b.urlPhoto, count(*) AS amount FROM concession c 
                INNER JOIN book b on b.id = c.idBook
                WHERE c.reservedBy IS NULL AND(c.state = 'disponible' OR c.state = 'renouveler') AND (
    LOWER(b.title) like LOWER(:search)
                OR LOWER(author) like LOWER (:search)
                OR LOWER(edition) like LOWER (:search)
                OR LOWER(barcode) like lower (:search)
                )GROUP BY b.id
                ORDER BY amount DESC";

        if ($stmt = $pdo->prepare($sql)) {

            // Set parameters
            $search = "%".trim($_POST['search'])."%";

            $unreservedValue = null;
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":search", $search, PDO::PARAM_STR);
            $stmt->bindParam(":unreserved", $unreservedValue, PDO::PARAM_NULL);

            // Attempt to execute the prepared statement
            if ($stmt->execute())
                $groupResultsBD = $stmt->fetchAll(PDO::FETCH_ASSOC);
                        foreach ($groupResultsBD as $row)
                        {
                            $book = new Book();
                            $groupresult = new GroupResult();

                            $book->setIdBook($row['id']);
                            $book->setTitle($row['title']);
                            $book->setAuthor($row['author']);
                            $book->setEdition($row['edition']);
                            $book->setPublisher($row['publisher']);
                            $book->setBarcode($row['barcode']);
                            $book->setUrlPhoto($row['urlPhoto']);

                            $groupresult->setBook($book);
                            $groupresult->setAmount($row['amount']);


                            array_push($groupresults, $groupresult);
                        }

                        echo json_encode($groupresults);
            }
            else
            {
                echo json_encode(null);
            }

            // Close statement
            unset($stmt);

        }
        else
        {
            echo json_encode(null);
        }

}catch (Exception $e)
{
    file_put_contents('log.txt', "erreur_get_groupresult : " . $e . "\n", FILE_APPEND);
}