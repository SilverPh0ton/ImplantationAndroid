<?php
require_once "config_bd.php";
include '../entity/ServerResponse.php';

try {
    $response = new ServerResponse();
    $file_path_concession = "../../../GlobalAGECTR/upload_photo_concession/";
    $file_path_book = "../../../GlobalAGECTR/upload_photo_book/";

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

    $files = glob($file_path_concession . $idConcession . '-*.jpg');
    foreach ($files as $file) {
        unlink($file);
    }

    /*
            CONCESSION
    */
    $sql = "insert into reception_image values (0,:nom,:taille,:extention)";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(":nom",$_FILES["file"]["name"]);
    $stmt->bindParam(":taille",$_FILES["file"]["size"]);
    $stmt->bindParam(":extention",$ext);
    $stmt->execute();
  
    $sql = "Select max(id) from concession_image";
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
    /*
    BOOK
    */
        $sqlConcessionHasBook = "SELECT b.id, b.urlPhoto FROM concession c
                            INNER JOIN book b on b.id = c.idBook
                            WHERE c.id = :idConcession ;";

        if($stmt = $pdo->prepare($sqlConcessionHasBook)) {

            $stmt->bindParam(":idConcession", $idConcession);
            if ($stmt->execute()) {
                $row = $stmt->fetch();

                if ($row['urlPhoto'] == null) {
                    $idBook = $row['id'];

                    $sqlBook = "UPDATE book SET urlPhoto =:path_img WHERE id=:idBook";
                    $bookUid = sprintf('%04X-%04X-%04X', mt_rand(0, 65535), mt_rand(0, 65535), mt_rand(0, 65535));


                    if ($stmt = $pdo->prepare($sqlBook)) {
                        $bookphotoName = $idBook . '-' . $bookUid . '.jpg';

                        $stmt->bindParam(":path_img", $bookphotoName);
                        $stmt->bindParam(":idBook", $idBook);
                        if ($stmt->execute()) {

                            $file_path_book = $file_path_book . $bookphotoName;
                            file_put_contents('log.txt', "FILE PATH BOOK ::  " . $file_path_book . "\n", FILE_APPEND);
                            file_put_contents('log.txt', "FILE_TMP_NAME_AT_BOOK :: ".$_FILES['file']['tmp_name']."\n", FILE_APPEND);
                            if (copy($_FILES['file']['tmp_name'], $file_path_book)) {
                                $response->setMessage("Objet book ajouté");
                                $response->setSucces(true);
                            } else {
                                $response->setMessage('erreur lors du file move book');
                                $response->setSucces(false);
                            }

                        } else {
                            $response->setMessage('erreur lors de execute lupdate book');
                            $response->setSucces(false);
                        }
                    } else {
                        $response->setMessage('erreur lors de prepare update book');
                        $response->setSucces(false);
                    }
                } else {
                    $response->setMessage('Concession updated UrlPhoto is not null in book');
                    $response->setSucces(true);
                }
            } else {
                $response->setMessage('Book url exist execute statement failed ');
                $response->setSucces(false);
            }
        }
        else
        {
            $response->setMessage('Book url prepare statement failed');
            $response->setSucces(false);
        }


    unset($stmt);

    echo json_encode($response);


} catch (Exception $e) {
    file_put_contents('log.txt', "erreur_photo_upload: " . $e . "\n", FILE_APPEND);
}