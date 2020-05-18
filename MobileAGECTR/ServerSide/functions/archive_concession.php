<?php
require_once "config_bd.php";
include '../entity/ServerResponse.php';
include 'const.php';

try{
    $arr = json_decode($post, true);
    $log = "ARCHIVE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if (isset($_POST['idConcession'])) {

        $sql = "SELECT * FROM concession WHERE id = :idConcession";
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam("idConcession", $_POST['idConcession']);
        if($stmt->execute())
        {
            $concessionInfoBD = $stmt->fetch();
            if(isset($concessionInfoBD['id'])) {

                $sqlInsertHistory = "INSERT INTO history (customerPrice,feesPercentage, sellingPrice,  state, createdDate, idCustomer, idBook)
                                 VALUES(:customerPrice, :feesPercentage, :sellingPrice, :state, :createdDate, :expiredDate, :idCustomer, :idBook)";
                $stmt = $pdo->prepare($sqlInsertHistory);
                $stmt->bindParam(":customerPrice", $concessionInfoBD['customerPrice']);
                $stmt->bindParam(":feesPercentage", $concessionInfoBD['feesPercentage']);
                $stmt->bindParam(":sellingPrice", $concessionInfoBD['sellingPrice']);
                $stmt->bindParam(":state", $concessionInfoBD['state']);
                $stmt->bindParam(":createdDate", $concessionInfoBD['createdDate']);
                $stmt->bindParam(":idCustomer", $concessionInfoBD['idCustomer']);
                $stmt->bindParam(":idBook", $concessionInfoBD['idBook']);
                if ($stmt->execute()) {
                    $sql = "DELETE FROM concession WHERE id = :idConcession";
                    $stmt = $pdo->prepare($sql);
                    $stmt->bindParam(":idConcession", $_POST['idConcession']);
                    if ($stmt->execute()) {
                        $log .= "Archived concession";
                        $response->setSucces(true);
                        $response->setMessage("Success");
                    } else {
                        $log .= "No execute deleteConcession";
                        $response->setSucces(false);
                        $response->setMessage("Execute:DeleteAfterInsertHistory");
                    }
                } else {
                    $log .= "No execute insertHistory";
                    $response->setSucces(false);
                    $response->setMessage("Execute:InsertConcessionIntoHistory");
                }
            }
            else
            {
                $log .= "Concession doesnt exist";
                $response->setSucces(false);
                $response->setMessage("Concession doesnt exist");
            }
        }
        else
        {
            $log .= "No execute fetchconcession";
            $response->setSucces(false);
            $response->setMessage("Execute:GetConcession");
        }
    } else {
        $log .= "Missing info";
        $response->setSucces(false);
        $response->setMessage("All mandatory fields must be complete");
    }

    file_put_contents('log.txt', $log . "\n", FILE_APPEND);
    echo json_encode($response);
} catch (Exception $e) {
    $response = new ServerResponse();
    $response->setSucces(false);
    $response->setMessage("Error archive_concession : ".$e);
    echo json_encode($response);
    file_put_contents('log.txt', "erreur_archiving_concession : " . $e . "\n", FILE_APPEND);
}
