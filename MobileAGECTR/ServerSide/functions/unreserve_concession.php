<?php
require_once "config_bd.php";
include '../entity/ServerResponse.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "UNRESERVE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if (isset($_POST['idConcession'])) {


        $sql = "UPDATE concession SET reservedBy = :reservedBy, reservedDate = :reservedDate, reservedExpireDate = :reservedExpireDate WHERE id = :idConcession";

        if ($stmt = $pdo->prepare($sql)) {
            $reservedBy = null;
            $reservedDate = null;
            $reservedExpireDate = null;

            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idConcession", $_POST['idConcession']);
            $stmt->bindParam(":reservedBy", $reservedBy);
            $stmt->bindParam(":reservedDate", $reservedDate);
            $stmt->bindParam(":reservedExpireDate", $reservedExpireDate);


// Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
// Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("Concession unreserved");
            } else {
                $response->setSucces(false);
                $response->setMessage("Error while reserving");
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

    file_put_contents('log.txt', "erreur_unreserve_concession : " . $e . "\n", FILE_APPEND);
}