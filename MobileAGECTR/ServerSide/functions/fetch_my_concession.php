<?php

require_once "config_bd.php";
include '../entity/Concession.php';
include '../entity/Book.php';

try {
    $concessions = array();

    if (isset($_POST['id_user'])) {

        $sql = "SELECT * FROM concession WHERE idCustomer = :idCustomer";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idCustomer", $_POST['id_user'], PDO::PARAM_INT);

            // Attempt to execute the prepared statement
            if ($stmt->execute())
                $concessionBD = $stmt->fetchAll(PDO::FETCH_ASSOC);

            foreach ($concessionBD as $row) {

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

                array_push($concessions, $concession);
            }
        }

        $sqlReception = "SELECT * FROM reception WHERE idCustomer = :idCustomer";
        if($stmt = $pdo->prepare($sqlReception))
        {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idCustomer", $_POST['id_user'], PDO::PARAM_INT);

            // Attempt to execute the prepared statement
            if ($stmt->execute())
                $receptionBD = $stmt->fetchAll(PDO::FETCH_ASSOC);

            foreach ($receptionBD as $row) {

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
                $concession->setState($row['state']);
                $concession->setUrlPhoto($row['urlPhoto']);
                $concession->setRefuseReason($row['refuseReason']);

                array_push($concessions, $concession);
            }
        }
        echo json_encode($concessions);
        // Close statement
        unset($stmt);

    } else {
        echo json_encode(null);
    }

} catch (Exception $e) {
    echo json_encode(null);
    file_put_contents('log.txt', "erreur_fetch_my_concession : " . $e . "\n", FILE_APPEND);
}