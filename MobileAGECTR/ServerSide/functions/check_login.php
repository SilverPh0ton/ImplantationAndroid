<?php

require_once "config_bd.php";
include '../entity/User.php';

try {
    $connectedUser = new User();
    if (isset($_POST['connection_info']) && isset($_POST['password'])) {
        $connection_info = trim($_POST['connection_info']);
        $password = trim($_POST["password"]);

        $sql = "SELECT * FROM customer WHERE matricule = :connection_info OR email = :connection_info";
        if($stmt = $pdo->prepare($sql))
        {
            $stmt->bindParam(":connection_info", $connection_info, PDO::PARAM_STR);
            if($stmt->execute())
            {
                if($stmt->rowCount() == 1) {
                    if ($row = $stmt->fetch()) {
                        $customerPassword = $row['password'];
                        if (password_verify($password, $customerPassword)) {
                            $connectedUser->setIdUser($row['id']);
                            $connectedUser->setMatricule($row['matricule']);
                            $connectedUser->setFirstName($row['firstName']);
                            $connectedUser->setLastName($row['lastName']);
                            $connectedUser->setPhoneNumber($row['phoneNumber']);
                            $connectedUser->setEmail($row['email']);
                            $connectedUser->setAvatar($row['avatar']);

                            echo json_encode($connectedUser);
                        } else {
                            echo json_encode(null) . "failed verify";
                        }
                    } else {
                        echo json_encode(null);
                    }
                }
                else {
                    echo json_encode(null);
                }

                }
                else {
                    echo json_encode(null);
                }
            }
            else
            {
                echo json_encode(null);
            }
        }
        else {
            echo json_encode(null);
        }
}catch (Exception $e) {

    file_put_contents('log.txt', "erreur_user_check : " . $e . "\n", FILE_APPEND);
}

