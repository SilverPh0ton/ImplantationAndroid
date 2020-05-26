<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "UPDATE_BOOK : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();
    $updatedBy = CONST_CREATEDBY;

    if (isset($arr['idBook'])
        && isset($arr['title'])
        && isset($arr['barcode'])) {


        $sql = "UPDATE book SET 
                title = :title,
                author = :author,
                publisher = :publisher,
                edition = :edition,
                barcode = :barcode,
                urlPhoto = :urlPhoto,
                updatedBy = :updatedBy WHERE id = :idBook ";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":title", $arr['title']);
            $stmt->bindParam(":author", $arr['author']);
            $stmt->bindParam(":publisher", $arr['publisher']);
            $stmt->bindParam(":edition", $arr['edition']);
            $stmt->bindParam(":barcode", $arr['barcode']);
            $stmt->bindParam(":urlPhoto", $arr['urlPhoto']);
            $stmt->bindParam(":updatedBy", $updatedBy);
            $stmt->bindParam(":idBook", $arr['idBook']);

            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                // Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("Book updated");
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

} catch (Exception $e) {

    file_put_contents('log.txt', "erreur_update_book : " . $e . "\n", FILE_APPEND);
}
