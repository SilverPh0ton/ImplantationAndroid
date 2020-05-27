<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "REMOVE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    $removed_state = CONST_TO_REMOVE_STATE;

    if (isset($_POST['idConcession'])) {
        $idConcession = $_POST['idConcession'];
        $modifiedState = CONST_UPDATE_STATE;
        $sqlRevertPriceModif = "UPDATE concession SET customerPrice = sellingPrice/((100+feesPercentage)/100) WHERE id =  :idConcession AND state = :state";

        if($stmt = $pdo->prepare($sqlRevertPriceModif)) {
            $stmt->bindParam(":idConcession", $idConcession, PDO::PARAM_INT);
            $stmt->bindParam(":state", $modifiedState, PDO::PARAM_STR);

            if ($stmt->execute()) {

                $sql = "UPDATE concession SET state = :state WHERE id = :idConcession";

                if ($stmt = $pdo->prepare($sql)) {
                    // Bind variables to the prepared statement as parameters
                    $stmt->bindParam(":state", $removed_state);
                    $stmt->bindParam(":idConcession", $_POST['idConcession']);


                    // Attempt to execute the prepared statement
                    if ($stmt->execute()) {
                        $log .= "succes";
                        // Records created successfully. Redirect to landing page
                        $response->setSucces(true);
                        $response->setMessage("Concession removed");
                    } else {
                        $response->setSucces(false);
                        $response->setMessage("Error while removing");
                    }
                    unset($stmt);
                }
                else {
                    $response->setSucces(false);
                    $response->setMessage("Error while removing");
                }
            }
            else {
                $response->setSucces(false);
                $response->setMessage("Error while removing");
            }
        }
        else {
            $response->setSucces(false);
            $response->setMessage("Error while removing");
        }
    } else {
        $log .= "Missing info";

        $response->setSucces(false);
        $response->setMessage("All mandatory fields must be complete");
    }

    //file_put_contents('log.txt', $log . "\n", FILE_APPEND);
    echo json_encode($response);

} catch (Exception $e) {

    file_put_contents('log.txt', "erreur_removing_concession : " . $e . "\n", FILE_APPEND);
}
