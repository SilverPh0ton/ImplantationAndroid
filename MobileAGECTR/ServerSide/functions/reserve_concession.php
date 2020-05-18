<?php
require_once "config_bd.php";
include '../entity/ServerResponse.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "RESERVE_CONCESSION : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if (isset($_POST['idConcession'])
        && isset($_POST['id_user'])) {

            $nbSql = "SELECT count(*) AS nbReservation FROM concession WHERE reservedBy = :id_user ";
            $stmt = $pdo->prepare($nbSql);
            $stmt->bindParam(":id_user", $_POST['id_user']);
            $stmt->execute();
            $row = $stmt->fetch();

            if($row['nbReservation']>9)
            {
                $log .= "More than 10 reservations";

                $response->setSucces(false);
                $response->setMessage("MAX");
            }
            else
            {
                $sql = "UPDATE concession SET reservedBy = :id_user, reservedDate = :reservedDate, reservedExpireDate = :reservedExpireDate WHERE id = :idConcession";

                if ($stmt = $pdo->prepare($sql)) {
                    $reservedDate = date('Y-m-d H-i-s');

                    //Calcul date d'expiration
                    $verificationDate =  date('D', strtotime('+36 hours'));

                    if($verificationDate == 'Sat' || $verificationDate == 'Sun') {
                        $reservedExpireDate = date('Y-m-d H-i-s', strtotime('next monday, 12:10'));
                    }
                    else
                    {
                        $reservedExpireDate = date('Y-m-d H-i-s', strtotime('+36 hours'));
                    }

                    // Bind variables to the prepared statement as parameters
                    $stmt->bindParam(":idConcession", $_POST['idConcession']);
                    $stmt->bindParam(":id_user", $_POST['id_user']);
                    $stmt->bindParam(":reservedDate", $reservedDate);
                    $stmt->bindParam(":reservedExpireDate", $reservedExpireDate);


// Attempt to execute the prepared statement
                    if ($stmt->execute()) {
                        $log .= "succes";
// Records created successfully. Redirect to landing page
                        $response->setSucces(true);
                        $response->setMessage("Concession reserved");
                    } else {
                        $response->setSucces(false);
                        $response->setMessage("Error while reserving");
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

    file_put_contents('log.txt', "erreur_reserve_concession : " . $e . "\n", FILE_APPEND);
}