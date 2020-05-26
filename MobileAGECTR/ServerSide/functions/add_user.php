<?php

require_once "config_bd.php";
include '../entity/User.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {
    $post = file_get_contents('php://input');

    $arr = json_decode($post, true);
    $log = "ADD_USER : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $addUser = new User();
    $createdBy = CONST_CREATEDBY;

    if (isset($arr['email'])
        && isset($arr['password'])
        && isset($arr['firstName'])) {


        $sql = "INSERT INTO customer(email, firstName, lastName, password, matricule, phoneNumber, avatar, createdBy) VALUES (:email, :firstName, :lastName, :password, :matricule, :phoneNumber, :avatar, :createdBy)";

        if ($stmt = $pdo->prepare($sql)) {
            // Bind variables to the prepared statement as parameters
            $hashPassword = password_hash($arr['password'], PASSWORD_DEFAULT);
            $stmt->bindParam(":email", $arr['email']);
            $stmt->bindParam(":firstName", $arr['firstName']);
            $stmt->bindParam(":lastName", $arr['lastName']);
            $stmt->bindParam(":password", $hashPassword);
            $stmt->bindParam(":matricule", $arr['matricule']);
            $stmt->bindParam(":phoneNumber", $arr['phoneNumber']);
            $stmt->bindParam(":avatar", $arr['avatar']);
            $stmt->bindParam(":createdBy", $createdBy);

            // Attempt to execute the prepared statement
            if ($stmt->execute()) {
                $log .= "succes";
                $addUser->setIdUser($pdo->lastInsertId());
                $addUser->setFirstName($arr['firstName']);
                $addUser->setLastName($arr['lastName']);
                $addUser->setEmail($arr['email']);
                $addUser->setPhoneNumber($arr['phoneNumber']);
                $addUser->setMatricule($arr['matricule']);
                $addUser->setAvatar($arr['avatar']);
                // Records created successfully. Redirect to landing page
                echo json_encode($addUser);
            } else {
                echo json_encode(null);
            }
            unset($stmt);
        }
    } else {
        $log .= "Missing info";

        echo json_encode(null);
    }
    file_put_contents('log.txt', $log . "\n", FILE_APPEND);

} catch (Exception $e) {

    file_put_contents('log.txt', "erreur_add_user : " . $e . "\n", FILE_APPEND);
    echo json_encode(null);
}
