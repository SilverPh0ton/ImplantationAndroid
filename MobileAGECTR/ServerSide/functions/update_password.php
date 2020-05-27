<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {


    $log = "UPDATE_PASSWORD : ";
    $response = new ServerResponse();
    $updatedBy = CONST_CREATEDBY;

    if (isset($_POST['id_user'])
        && isset($_POST['currentPassword'])
        && isset($_POST['newPassword'])) {

        $sqlCurrPass = "SELECT password FROM customer WHERE id = :idUser";
        $stmt = $pdo->prepare($sqlCurrPass);
        $stmt->bindParam(":idUser", $_POST['id_user']);
        $stmt->execute();
        if ($stmt->rowCount() == 1) {
            $row = $stmt->fetch();
            if (password_verify($_POST['currentPassword'],$row['password']))
            {
                $sql = "UPDATE customer SET 
                password = :newPassword,
                updatedBy = :updatedBy WHERE id = :idUser";

                if($stmt = $pdo->prepare($sql)) {
                    // Bind variables to the prepared statement as parameters
                    $hashNewPassword = password_hash($_POST['newPassword'], PASSWORD_DEFAULT);
                    $stmt->bindParam(":newPassword", $hashNewPassword);
                    $stmt->bindParam(":updatedBy", $updatedBy);
                    $stmt->bindParam(":idUser", $_POST['id_user']);

                    // Attempt to execute the prepared statement
                    if ($stmt->execute()) {
                        $log .= "succes";
                        // Records created successfully. Redirect to landing page
                        $response->setSucces(true);
                        $response->setMessage("User password updated");
                    } else {
                        $response->setSucces(false);
                        $response->setMessage("Error while updating password");
                    }
                }
                else
                {
                    $log .= "Update prepare statement failed";

                    $response->setSucces(false);
                    $response->setMessage("Update prepare statement failed");
                }

            } else {
                $log .= "Current password doesn't match";

                $response->setSucces(false);
                $response->setMessage("Current password doesn't match");
            }
        } else {
            $log .= "User doesnt exist";

            $response->setSucces(false);
            $response->setMessage("User given doesn't exist");
        }

        unset($stmt);
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

    file_put_contents('log.txt', "erreur_update_password : " . $e . "\n", FILE_APPEND);
}

