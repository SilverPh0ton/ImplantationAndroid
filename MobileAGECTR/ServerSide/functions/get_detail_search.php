<?php

require_once "config_bd.php";
include '../entity/Concession.php';
include '../entity/Book.php';

try {
    $log = "GET_DETAILSEARCH:";
    file_put_contents('log.txt', $log, FILE_APPEND);
    $post = file_get_contents('php://input');
    file_put_contents('log.txt', $post, FILE_APPEND);
    $concessions = array();
    $book = new Book();
    if (isset($_POST['idBook'])) {

        try {
            $sqlBook = "SELECT * FROM book WHERE id = :idBook";
            $stmt = $pdo->prepare($sqlBook);
            $stmt->bindParam(":idBook", $_POST['idBook'], PDO::PARAM_INT);
            $stmt->execute();
            $row = $stmt->fetch();


            $book->setIdBook($row['id']);
            $book->setTitle($row['title']);
            $book->setAuthor($row['author']);
            $book->setEdition($row['edition']);
            $book->setPublisher($row['publisher']);
            $book->setBarcode($row['barcode']);
            $book->setUrlPhoto($row['urlPhoto']);

        }
        catch (Exception $e)
        {
            echo json_encode(null);
        }


        $sql = "SELECT *  FROM concession 
                WHERE idBook = :idBook AND reservedBy IS NULL AND (state = 'disponible' OR state = 'renouveler')
                ORDER BY sellingPrice";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idBook", $_POST['idBook'], PDO::PARAM_INT);

            // Attempt to execute the prepared statement
            if ($stmt->execute())
                $concessionsBD = $stmt->fetchAll(PDO::FETCH_ASSOC);

            foreach ($concessionsBD as $row) {
                $concession = new Concession();

                $concession->setIdConcession($row['id']);
                $concession->setIdCustomer($row['idCustomer']);
                $concession->setIsAnnotated($row['isAnnotated']);
                $concession->setBook($book);
                $concession->setCustomerPrice($row['customerPrice']);
                $concession->setSellingPrice($row['sellingPrice']);
                $concession->setState($row['state']);
                $concession->setUrlPhoto($row['urlPhoto']);

                array_push($concessions, $concession);
            }
            echo json_encode($concessions);
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