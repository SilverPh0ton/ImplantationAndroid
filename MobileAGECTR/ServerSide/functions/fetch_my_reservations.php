<?php

require_once "config_bd.php";
include '../entity/Concession.php';
include '../entity/Book.php';

try {
    $reservations = array();

    if (isset($_POST['id_user'])) {

        $sql = "SELECT * FROM concession WHERE reservedBy = :reservedBy";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":reservedBy", $_POST['id_user'], PDO::PARAM_INT);

            // Attempt to execute the prepared statement
            if ($stmt->execute())
                $reservationsBD = $stmt->fetchAll(PDO::FETCH_ASSOC);

            foreach ($reservationsBD as $row) {

                $book = new Book();

                try {
                    $sqlBook = "SELECT * FROM book WHERE id = :idBook";
                    $stmt = $pdo->prepare($sqlBook);
                    $stmt->bindParam(":idBook", $row['idBook'], PDO::PARAM_INT);
                    $stmt->execute();
                    $rowBook = $stmt->fetch();


                    $book->setIdBook($rowBook['id']);
                    $book->setTitle($rowBook['title']);
                    $book->setAuthor($rowBook['author']);
                    $book->setEdition($rowBook['edition']);
                    $book->setPublisher($rowBook['publisher']);
                    $book->setBarcode($rowBook['barcode']);
                    $book->setUrlPhoto($rowBook['urlPhoto']);

                }
                catch (Exception $e)
                {
                    echo json_encode(null);
                }

                $concession = new Concession();

                $concession->setIdConcession($row['id']);
                $concession->setIdCustomer($_POST['id_user']);
                $concession->setIsAnnotated(($row['isAnnotated']==1)?true:false);
                $concession->setBook($book);
                $concession->setCustomerPrice($row['customerPrice']);
                $concession->setSellingPrice($row['sellingPrice']);
                $concession->setState($row['state']);
                $concession->setUrlPhoto($row['urlPhoto']);
                $concession->setReservedExpireDate($row['reservedExpireDate']);
                $concession->setExpireDate($row['expireDate']);

                array_push($reservations, $concession);
            }
            echo json_encode($reservations);
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
    file_put_contents('log.txt', "erreur_fetch_myreservation : " . $e . "\n", FILE_APPEND);
}