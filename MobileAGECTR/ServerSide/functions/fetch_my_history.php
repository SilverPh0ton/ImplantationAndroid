<?php


require_once "config_bd.php";
include '../entity/History.php';

try {
    $historys = array();

    if (isset($_POST['id_user'])) {

        $sql = "SELECT state, customerPrice, idBook FROM history WHERE idCustomer = :idCustomer";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $stmt->bindParam(":idCustomer", $_POST['id_user'], PDO::PARAM_INT);

            // Attempt to execute the prepared statement
            if ($stmt->execute())
                $concessionBD = $stmt->fetchAll(PDO::FETCH_ASSOC);

            foreach ($concessionBD as $row) {
                try {
                    $sqlBook = "SELECT title FROM book WHERE id = :idBook";
                    $stmt = $pdo->prepare($sqlBook);
                    $stmt->bindParam(":idBook", $row['idBook'], PDO::PARAM_INT);
                    $stmt->execute();
                    $rowBook = $stmt->fetch();
                } catch (Exception $e) {
                    echo json_encode(null);
                }

                $history = new History();
                $history->setTitle($rowBook['title']);
                $history->setState($row['state']);
                $history->setPrice($row['customerPrice']);

                array_push($historys, $history);
            }
            echo json_encode($historys);
        } else {

            echo json_encode(null);
        }

        // Close statement
        unset($stmt);

    } else {
        echo json_encode(null);
    }

} catch (Exception $e) {
    echo json_encode(null);
    file_put_contents('log.txt', "erreur_fetch_my_history : " . $e . "\n", FILE_APPEND);
}