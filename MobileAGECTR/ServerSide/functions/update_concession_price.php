<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include 'const.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "UPDATE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    $expireDate =  date('Y-m-d H-i-s', strtotime('+370 day'));
    $updatedBy = CONST_CREATEDBY;
    $state = CONST_UPDATE_STATE;
       /* $sqlFee = "SELECT
                        COLUMN_DEFAULT
                    FROM
                        INFORMATION_SCHEMA.COLUMNS
                    WHERE
                      TABLE_NAME = 'concession'
                      AND COLUMN_NAME = 'feesPercentage';";
        $stmt = $pdo->prepare($sqlFee);
        $stmt->execute();
        $row = $stmt->fetch();*/

    if (isset($_POST['idConcession']) && isset($_POST['customerPrice']))
    {
        $sqlCheckState = "SELECT state FROM concession WHERE id = :idConcession";
        $stmt = $pdo->prepare($sqlCheckState);
        $stmt->bindParam(":idConcession", $_POST['idConcession']);
        if(!$stmt->execute()) {
            $log .= "ERREUR GETTING STATE";
            // Records created successfully. Redirect to landing page
            $response->setSucces(false);
            $response->setMessage("State check failed");
        }
        $row = $stmt->fetch();
        if($row['state'] == CONST_CONCESSION_DEFAULT_STATE)
        {
            $state = CONST_CONCESSION_DEFAULT_STATE;
        }

        $sql = "UPDATE concession SET 
                customerPrice = :customerPrice,
                state = :state,
                updatedBy = :updatedBy,
                expireDate = :expireDate
                WHERE id = :idConcession ";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":customerPrice", $_POST['customerPrice']);
            $stmt->bindParam(":state", $state);
            $stmt->bindParam(":updatedBy", $updatedBy);
            $stmt->bindParam(":expireDate", $expireDate);
            $stmt->bindParam(":idConcession", $_POST['idConcession']);


            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                // Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("Concession updated");
            } else {
                $response->setSucces(false);
                $response->setMessage("Error while updating");
            }
            unset($stmt);
        }
    } else {
        $log .= "Missing info";

        $response->setSucces(false);
        $response->setMessage("All mandatory fields must be complete");
    }

    //file_put_contents('log.txt', $log . "\n", FILE_APPEND);
    echo json_encode($response);
}catch (Exception $e) {

    file_put_contents('log.txt', "erreur_update_concession : " . $e . "\n", FILE_APPEND);
}
