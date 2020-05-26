<?php
require_once "config_bd.php";
include '../entity/ServerResponse.php';
include '../../../GlobalAGECTR/SharedConstant.php';

try {
$post = file_get_contents('php://input');

$arr = json_decode($post, true);
$log = "ADD_BOOK : ";
file_put_contents('log.txt', $post, FILE_APPEND);
$response = new ServerResponse();
$createdBy = CONST_CREATEDBY;

if (isset($arr['title'])
&& isset($arr['barcode'])) {


$sql = "INSERT INTO book(title, author, publisher, edition, barcode, urlPhoto, createdBy) VALUES (:title, :author, :publisher, :edition, :barcode, :urlPhoto, :createdBy)";

if ($stmt = $pdo->prepare($sql)) {
    // Bind variables to the prepared statement as parameters
    $stmt->bindParam(":title", $arr['title']);
    $stmt->bindParam(":author", $arr['author']);
    $stmt->bindParam(":publisher", $arr['publisher']);
    $stmt->bindParam(":edition", $arr['edition']);
    $stmt->bindParam(":barcode", $arr['barcode']);
    $stmt->bindParam(":urlPhoto", $arr['urlPhoto']);
    $stmt->bindParam(":createdBy", $createdBy);

// Attempt to execute the prepared statement
if ($stmt->execute()) {
$log .= "succes";
// Records created successfully. Redirect to landing page
$response->setSucces(true);
$response->setMessage("Book inserted");
} else {
$response->setSucces(false);
$response->setMessage("Error while inserting");
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

file_put_contents('log.txt', "erreur_add_book : " . $e . "\n", FILE_APPEND);
}