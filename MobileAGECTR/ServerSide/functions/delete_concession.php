<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "DELETE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if (isset($_POST['idConcession'])) {

        if($_POST['State']=="validation" || $_POST['State']=='refuser')
         {  
            $sql = "SELECT urlPhoto FROM reception WHERE id = :idConcession";
            $stmt = $pdo->prepare($sql);
            $stmt->bindParam(":idConcession", $_POST['idConcession']);
            $stmt->execute();
            $urlPhoto = $stmt->fetch()[0];
            
            $file_path_concession = "../../../GlobalAGECTR/upload_photo_reception/";
            if (!unlink($file_path_concession . $urlPhoto . '.png')){
                error_log("Image introuvable", 0);
            }
            
            $sql = "DELETE FROM reception WHERE id = :idConcession";

            if ($stmt = $pdo->prepare($sql)) {
                // Bind variables to the prepared statement as parameters
                $stmt->bindParam(":idConcession", $_POST['idConcession']);
    
    
                // Attempt to execute the prepared statement
                if ($stmt->execute()) {
                    $log .= "succes";
                    // Records created successfully. Redirect to landing page
                    $response->setSucces(true);
                    $response->setMessage("Concession deleted");
                } else {
                    $response->setSucces(false);
                    $response->setMessage("Error while deleting");
                }
                unset($stmt);
            }
             
         }else{
        $sql = "DELETE FROM concession WHERE id = :idConcession";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idConcession", $_POST['idConcession']);


            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                // Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("Concession deleted");
            } else {
                $response->setSucces(false);
                $response->setMessage("Error while deleting");
            }
            unset($stmt);
        }
    }
    } else {
        $log .= "Missing info";

        $response->setSucces(false);
        $response->setMessage("All mandatory fields must be complete");
    }

    //file_put_contents('log.txt', $log . "\n", FILE_APPEND);
    echo json_encode($response);

} catch (Exception $e) {
    $response = new ServerResponse();
    $response->setSucces(false);
    $response->setMessage("Error delete_concession : ".$e);
    echo json_encode($response);
    file_put_contents('log.txt', "erreur_deleting_concession : " . $e . "\n", FILE_APPEND);
}
