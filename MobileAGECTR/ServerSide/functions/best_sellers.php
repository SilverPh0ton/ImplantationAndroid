<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
// include database and object files
include_once 'config_bd.php';
include '../entity/Concession.php';
include '../entity/Book.php';

// prepare client object
$concession = new Concession();
$book = new Book();

$book_arr = array();
$book_arr["book"] = array();

$query = "SELECT history.idBook, count(history.id) as nbr,
(SELECT count(idBook) from concession where concession.idBook = history.idBook and (state = 'disponible' or state = 'renouveler')) as dispo,
(SELECT urlPhoto from book where id = history.idBook) as image FROM `history` WHERE history.idCustomer <> 0 AND (history.state = 'paye' or history.state = 'a_paye') AND
(SELECT count(idBook) from concession where concession.idBook = history.idBook and (state = 'disponible' or state = 'renouveler')) > 0
GROUP BY history.idBook ORDER BY nbr DESC, MAX(history.soldDate) DESC LIMIT 9";
$stmt = $pdo->prepare($query);
$stmt->execute();
$num = $stmt->rowCount();

if($num > 0) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $query = "SELECT * FROM `book` WHERE id= '".$idBook."'";
        $stmt2 = $pdo->prepare($query);
        $stmt2->execute();

        $num2 = $stmt2->rowCount();
        if ($num2 > 0) {
            while ($row2 = $stmt2->fetch(PDO::FETCH_ASSOC)) {
                extract($row2);

                $book_item = array(
                    "id" => $id,
                    "title" => $title,
                    "author" => $author,
                    "publisher" => $publisher,
                    "edition" => $edition,
                    "barcode" => $barcode,
                    "amount" => $dispo,
                    "image" => $image
                );
                array_push($book_arr["book"], $book_item);
            }
        }
    }
    print_r(json_encode($book_arr["book"]));
}
else {
    print_r(json_encode($book_arr["book"]));
}
?>