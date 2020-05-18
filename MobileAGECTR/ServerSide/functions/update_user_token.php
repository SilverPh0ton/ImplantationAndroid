<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';

try {
    $post = file_get_contents('php://input');
    $log = "UPDATE_USER_TOKEN : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if (isset($_POST['id_user'])&& isset($_POST['token'])) {

        $sql = "UPDATE customer SET 
                token = :token WHERE id = :idUser";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":token", $_POST['token']);
            $stmt->bindParam(":idUser", $_POST['id_user']);

            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                // Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("User token updated");
            } else {
                $response->setSucces(false);
                $response->setMessage("Error while updating user token");
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

    file_put_contents('log.txt', "erreur_update_user_token : " . $e . "\n", FILE_APPEND);
}

