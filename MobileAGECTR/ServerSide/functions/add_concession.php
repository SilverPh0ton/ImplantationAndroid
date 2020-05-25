<?php
require_once "config_bd.php";
include '../entity/Concession.php';
include '../entity/Book.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {

    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "ADD_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);

    $createdBy = CONST_CREATEDBY;
    $state = CONST_CONCESSION_DEFAULT_STATE;
    $addConcession = new Concession();
    $addBook = new Book();

    try{
        $sqlFee = "SELECT 
                        COLUMN_DEFAULT
                    FROM
                        INFORMATION_SCHEMA.COLUMNS
                    WHERE
                      TABLE_NAME = 'concession'
                      AND COLUMN_NAME = 'feesPercentage';";
        $stmt = $pdo->prepare($sqlFee);
        $stmt->execute();
        $row = $stmt->fetch();
    }
    catch (Exception $e)
    {
        echo json_encode(null);
        file_put_contents('log.txt', "erreur_fetch_default_fee : " . $e . "\n", FILE_APPEND);
        die();
    }

    $feesPercentage = $row['COLUMN_DEFAULT'];
    $feesPercentage = $feesPercentage/100;

    $sellingPrice = round($arr['customerPrice'] + $feesPercentage*$arr['customerPrice'] , 2);


    if (isset($arr['customerPrice'])) {
        if($arr['book']['idBook'] != 0)
        {
            $idBook = $arr['book']['idBook'];
            $addBook->setIdBook($idBook);
        }
        else
        {
            try{
                $sql = "INSERT INTO book(title, author, publisher, edition, barcode, urlPhoto, section, newBook, createdBy) VALUES (:title, :author, :publisher, :edition, :barcode, :urlPhoto, :section, :newBook, :createdBy)";

                if ($stmt = $pdo->prepare($sql)) {
                    $newBook = 1;
                    $section = CONST_FAKE_SECTION;
                    // Bind variables to the prepared statement as parameters
                    $stmt->bindParam(":title", $arr['book']['title']);
                    $stmt->bindParam(":author", $arr['book']['author']);
                    $stmt->bindParam(":publisher", $arr['book']['publisher']);
                    $stmt->bindParam(":edition", $arr['book']['edition']);
                    $stmt->bindParam(":barcode", $arr['book']['barcode']);
                    $stmt->bindParam(":urlPhoto", $arr['book']['urlPhoto']);
                    $stmt->bindParam(":section", $section);
                    $stmt->bindParam(":newBook", $newBook, PDO::PARAM_INT);
                    $stmt->bindParam(":createdBy", $createdBy);

                // Attempt to execute the prepared statement
                    if ($stmt->execute()) {
                        $log .= "succes";
                        $idBook = $pdo->lastInsertId();

                        $addBook->setIdBook($idBook);
                        $addBook->setUrlPhoto($arr['book']['urlPhoto']);
                        $addBook->setBarcode($arr['book']['barcode']);
                        $addBook->setPublisher($arr['book']['publisher']);
                        $addBook->setEdition($arr['book']['edition']);
                        $addBook->setAuthor($arr['book']['author']);
                        $addBook->setTitle($arr['book']['title']);

                    } else {
                        echo json_encode(null);
                        die();
                    }
                }
            }
            catch (Exception $e){
                file_put_contents('log.txt', "erreur_add_book : " . $e . "\n", FILE_APPEND);
                echo json_encode(null);
                die();
            }
        }

        $sql = "INSERT INTO reception (idCustomer, idBook, customerPrice, urlPhoto, state, isAnnotated) VALUES(:idCustomer, :idBook, :customerPrice, :urlPhoto, :state, :isAnnotated)";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idCustomer", $arr['idCustomer']);
            $stmt->bindParam(":idBook", $idBook);
            $stmt->bindParam(":customerPrice", $arr['customerPrice']);
            $stmt->bindParam(":urlPhoto", $arr['urlPhoto']);
            $stmt->bindParam(":state", $state);
            if ($arr['isAnnotated'] == true){
                $isAnnotated = 1;
            }
            else{
                $isAnnotated = 0;
            }
            $stmt->bindParam(":isAnnotated",$isAnnotated);

// Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                $addConcession->setIdConcession($pdo->lastInsertId());
                $addConcession->setIdCustomer($arr['idCustomer']);
                $addConcession->setBook($addBook);
                $addConcession->setCustomerPrice($arr['customerPrice']);
                $addConcession->setUrlPhoto($arr['urlPhoto']);
                $addConcession->setState($state);
                $addConcession->setIsAnnotated($arr['isAnnotated']);
                echo json_encode($addConcession);

            } else {
                echo json_encode(null);
            }
            unset($stmt);
        }
    } else {
        $log .= "Missing info";

        echo json_encode(null);
    }

} catch (Exception $e) {

    file_put_contents('log.txt', "erreur_add_concession : " . $e . "\n", FILE_APPEND);
    echo json_encode(null);
}