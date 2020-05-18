<?php

require_once "config_bd.php";
include '../entity/Concession.php';
include '../entity/Book.php';

try {
    $book = new Book();

    if (isset($_POST['barcode'])) {

        $sql = "SELECT *  FROM book 
                WHERE barcode = :barcode ORDER BY updatedDate DESC LIMIT 1";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":barcode", $_POST['barcode'], PDO::PARAM_STR);

            // Attempt to execute the prepared statement
            if ($stmt->execute()){
                if($stmt->rowCount() == 1)
                {
                        $row = $stmt->fetch();

                        $book->setIdBook($row['id']);
                        $book->setTitle($row['title']);
                        $book->setAuthor($row['author']);
                        $book->setEdition($row['edition']);
                        $book->setPublisher($row['publisher']);
                        $book->setBarcode($row['barcode']);
                        $book->setUrlPhoto($row['urlPhoto']);

                        echo json_encode($book);
                }
                else
                {
                    echo json_encode(null);
                }
            }

            } else {

                echo json_encode(null);
            }

            // Close statement
            unset($stmt);

    } else {
        echo json_encode(null);
    }

} catch (Exception $e) {
    echo json_encode(null);
    file_put_contents('log.txt', "erreur_get_detailsearch : " . $e . "\n", FILE_APPEND);
}