<?php

require_once "config_bd.php";
include '../entity/ServerResponse.php';
include_once "../../WebAGECTR/Functions/_functionHelper.php";

try {
    $post = file_get_contents('php://input');
    $log = "RESET_PASSWORD : ";
    file_put_contents('log.txt', $post, FILE_APPEND);
    $response = new ServerResponse();

    if(isset($_POST['mail']) && isset($_POST['password']))
    {
        $sql = "SELECT id FROM customer WHERE email = :email";
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam(':email', $_POST['mail']);
        if($stmt->execute())
        {
            if($stmt->rowCount() == 1)
            {
                $row = $stmt->fetch();
                $sql = "UPDATE customer SET password = :password WHERE id = :idUser";
                $stmt = $pdo->prepare($sql);
                $hashPassword = password_hash($_POST['password'], PASSWORD_DEFAULT);
                $stmt->bindParam(":password",$hashPassword);
                $stmt->bindParam(":idUser", $row['id']);
                if($stmt->execute())
                {
                    $to_email = $_POST['mail'];
                    $subject = RENEW_PASSWORD_HEADER;
                    $message = RENEW_PASSWORD_BODY_SECT1.$_POST['password'];
                    $headers = 'Content-Type: text/plain; charset=utf-8' . "\r\n".MAIL_AGECTR_SOURCEMAIL;
                    mail($to_email,$subject,$message,$headers);

                        $log .= "success";
                        $response->setSucces(true);
                        $response->setMessage("Successfully sent mail and updated password");
                }
                else
                {
                    $log .= "execute update password fails";
                    $response->setSucces(false);
                    $response->setMessage("Error while executing updatePassword querry");
                }
            }
            else {
                $log .= "More than one email";
                $response->setSucces(false);
                $response->setMessage("Le email existe plus qu'une fois");
            }
        }
        else
        {
            $log .= "execute Select get user id fails";
            $response->setSucces(false);
            $response->setMessage("Error while executing getuserid querry");
        }
    }
    else
    {
        $log .= "Missing info";
        $response->setSucces(false);
        $response->setMessage("Missing mandatory fields");
    }
    file_put_contents('log.txt', $log . "\n", FILE_APPEND);
    echo json_encode($response);

}
catch
(Exception $e) {

    file_put_contents('log.txt', "erreur_reset_password : " . $e . "\n", FILE_APPEND);
}
