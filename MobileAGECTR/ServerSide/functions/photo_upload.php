<?php
require_once "config_bd.php";
include '../entity/ServerResponse.php';

try {
    $response = new ServerResponse();
    $file_path_concession = "../../../GlobalAGECTR/upload_photo_reception/";

    $idConcession = $_POST['idConcession'];

    file_put_contents('log.txt', "FICHIER_UPLOAD  :::: > " . "\n", FILE_APPEND);
    file_put_contents('log.txt', "FICHIER_tmp_name ::: " . $_FILES["file"]["tmp_name"] . "\n", FILE_APPEND);
    file_put_contents('log.txt', "FICHIER_name ::: : " . $_FILES["file"]["name"] . "\n", FILE_APPEND);
    file_put_contents('log.txt', "FICHIER_Type ::: : " . $_FILES["file"]["type"] . "\n", FILE_APPEND);
    file_put_contents('log.txt', "FICHIER_size ::: : " . $_FILES["file"]["size"] . "\n", FILE_APPEND);
    file_put_contents('log.txt', "FICHIER_pathinfo_type ::: : " . pathinfo($_FILES["file"]["name"], PATHINFO_EXTENSION) . "\n", FILE_APPEND);
    file_put_contents('log.txt', "Path IMG  :::  " . $idConcession . '-' . basename($_FILES['file']['name']) . "\n", FILE_APPEND);
    file_put_contents('log.txt', "iTEMiD  :::  " . $idConcession . "\n", FILE_APPEND);
    $ext = pathinfo($_FILES["file"]["name"], PATHINFO_EXTENSION);

    /*
            CONCESSION
    */
    $sql = "INSERT into reception_image values (0,:nom,:taille,:extention)";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(":nom",$_FILES["file"]["name"]);
    $stmt->bindParam(":taille",$_FILES["file"]["size"]);
    $stmt->bindParam(":extention",$ext);
    $stmt->execute();
  
    $sql = "SELECT max(id) from reception_image";
    $stmt=$pdo->prepare($sql);
    $stmt->execute();
    $idImg=$stmt->fetch();

    $sql = "UPDATE reception SET urlPhoto =:path_img WHERE id=:itemId";

    if ($stmt = $pdo->prepare($sql)) {
        $concessionphotoName = $idImg[0] . '.png';

        $stmt->bindParam(":path_img", $idImg[0]);
        $stmt->bindParam(":itemId", $idConcession);
        // Attempt to execute the prepared statement
        if ($stmt->execute()) {

            $file_path_concession = $file_path_concession . $concessionphotoName;
            file_put_contents('log.txt', "FILE PATH CONCESSION ::  " . $file_path_concession . "\n", FILE_APPEND);

            if (copy($_FILES['file']['tmp_name'], $file_path_concession)) {
                $response->setMessage("Objet concession ajouté");
                $response->setSucces(true);
            } else {
                $response->setMessage('erreur lors du file move concession');
                $response->setSucces(false);
            }

        } else {

            $response->setMessage("Erreur lors de lupdate concession");
            $response->setSucces(false);
        }
    }

    unset($stmt);
    echo json_encode($response);

} catch (Exception $e) {
    file_put_contents('log.txt', "erreur_photo_upload: " . $e . "\n", FILE_APPEND);
}