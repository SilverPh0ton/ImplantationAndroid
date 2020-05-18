<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include 'const.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "UPDATE_USER : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();
    $updatedBy = CONST_CREATEDBY;

        if (isset($arr['idUser'])
            && isset($arr['email'])
            && isset($arr['firstName'])
            && isset($arr['lastName'])
            && isset($arr['avatar'])) {

            $sql = "UPDATE customer SET 
                email = :email,
                firstName = :firstName,
                lastName = :lastName,
                matricule = :matricule,
                phoneNumber = :phoneNumber,
                avatar = :avatar,
                updatedBy = :updatedBy WHERE id = :idUser";

            if ($stmt = $pdo->prepare($sql)) {
                // Bind variables to the prepared statement as parameters
                $stmt->bindParam(":email", $arr['email']);
                $stmt->bindParam(":firstName", $arr['firstName']);
                $stmt->bindParam(":lastName", $arr['lastName']);
                $stmt->bindParam(":matricule", $arr['matricule']);
                $stmt->bindParam(":phoneNumber", $arr['phoneNumber']);
                $stmt->bindParam(":avatar", $arr['avatar']);
                $stmt->bindParam(":updatedBy", $updatedBy);
                $stmt->bindParam(":idUser", $arr['idUser']);

                // Attempt to execute the prepared statement
                if ($stmt->execute()) {
                    $log .= "succes";
                    // Records created successfully. Redirect to landing page
                    $response->setSucces(true);
                    $response->setMessage("User updated");
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

        file_put_contents('log.txt', $log . "\n", FILE_APPEND);
        echo json_encode($response);
}
catch
    (Exception $e) {

        file_put_contents('log.txt', "erreur_update_user : " . $e . "\n", FILE_APPEND);
    }

