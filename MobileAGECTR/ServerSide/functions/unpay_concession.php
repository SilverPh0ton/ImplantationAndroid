<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "Unpayed : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    $unpay_state = CONST_UNPAYED_STATE;

    if (isset($_POST['idHistory'])) {

        $sql = "UPDATE history SET state = :state WHERE id = :idHistory";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":state", $unpay_state);
            $stmt->bindParam(":idHistory", $_POST['idHistory']);


            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                // Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("History unpayed");
            } else {
                $response->setSucces(false);
                $response->setMessage("Error while unpaying");
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

} catch (Exception $e) {

    file_put_contents('log.txt', "erreur_Unpayed : " . $e . "\n", FILE_APPEND);
}
