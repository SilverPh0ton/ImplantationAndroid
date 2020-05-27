<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
// include database and object files
include_once 'config_bd.php';
include '../entity/GroupResult.php';
include '../entity/Book.php';
include '../../../GlobalAGECTR/SharedConstant.php';



$groupResults = array();

$query = "SELECT distinct idBook, count(id) as dispo, (SELECT urlPhoto from book where id = concession.idBook) as image FROM concession WHERE state = '".CONST_ACCEPT_STATE."' or state = '".CONST_TO_RENEW_STATE."' group by idBook order by MAX(createdDate) desc limit 9";
$stmt = $pdo->prepare($query);
$stmt->execute();
$num = $stmt->rowCount();

if($num > 0) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $query = "SELECT * FROM book WHERE id= '".$idBook."'";
        $stmt2 = $pdo->prepare($query);
        $stmt2->execute();

        $num2 = $stmt2->rowCount();
        if ($num2 > 0) {
            while ($row2 = $stmt2->fetch(PDO::FETCH_ASSOC)) {
                extract($row2);

                $book = new Book();
                $book->setIdBook($id);
                $book->setTitle($title);
                $book->setAuthor($author);
                $book->setEdition($edition);
                $book->setPublisher($publisher);
                $book->setBarcode($barcode);
                $book->setUrlPhoto($image);

                $groupResult = new GroupResult();
                $groupResult->setBook($book);
                $groupResult->setAmount($dispo);
                array_push($groupResults, $groupResult);
            }
        }
    }
    echo json_encode($groupResults);
}
else {
    echo json_encode($groupResults);
}
?>
