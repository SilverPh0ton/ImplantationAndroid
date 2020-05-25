<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "GIVE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if (isset($_POST['idConcession'])) {

        $sql = "UPDATE concession SET state = :state, donationDate = :donationDate, manageByAGECTR = :manageByAGECTR, expireDate = :expireDate WHERE id = :idConcession";

        if ($stmt = $pdo->prepare($sql)) {
            $idConcession = $_POST['idConcession'];
            $state = CONST_ACCEPT_STATE;
            $donationDate = date('Y-m-d H-i-s');
            $manageByAGECTR = 1;
            $expireDate = null;

            $stmt->bindParam(":state", $state, PDO::PARAM_STR);
            $stmt->bindParam(":donationDate", $donationDate);
            $stmt->bindParam(":manageByAGECTR", $manageByAGECTR);
            $stmt->bindParam(":expireDate", $expireDate);
            $stmt->bindParam(":idConcession", $idConcession, PDO::PARAM_INT);


            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                // Records created successfully. Redirect to landing page
                $response->setSucces(true);
                $response->setMessage("Concession given");
            } else {
                $response->setSucces(false);
                $response->setMessage("Error while giving");
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

    file_put_contents('log.txt', "erreur_giving_concession : " . $e . "\n", FILE_APPEND);
}
