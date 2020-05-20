<?php
include_once 'OldConfigDB.php';
include_once 'Entities/BookIdentifier.php';
include_once 'Entities/Book.php';
include_once 'Entities/User.php';
include_once 'Entities/Concession.php';

class OldDB extends OldConfigDB
{
    /**
     * OldDB constructor.
     */
    public function __construct()
    {
        parent::__construct();
    }

    public function __destruct()
    {
        parent::__destruct();
    }

    public function getMappedIdentifiers($bookIds)
    {
        if (isset($bookIds)) {
            $mappedIdentifiers = array();
            foreach ($bookIds as $bookId) {
                $bookIdentifier = $this->getBookIdentifiers($bookId);
                $isbn = $bookIdentifier->getIsbn();
                if (!array_key_exists($isbn, $mappedIdentifiers))
                {
                    $mappedIdentifiers[$isbn] = [$bookIdentifier->getIdBook()];
                }
                else
                {
                    array_push(
                        $mappedIdentifiers[$isbn],
                        $bookIdentifier->getIdBook()
                    );
                }
            }
        }
        return $mappedIdentifiers;
    }

    public function getBooksIdentifiers($bookIds)
    {
        $booksIdentifiers = array();
        foreach ($bookIds as $bookId)
        {
            array_push($booksIdentifiers, $this->getBookIdentifiers($bookId[0]));
        }
        return $booksIdentifiers;
    }

    private function getBookIdentifiers($bookId)
    {
        if (isset($bookId)) {
            $sql = "SELECT barcode FROM book WHERE id = :idBook";
            if ($stmt = $this->conn->prepare($sql)) {
                $stmt->bindParam(":idBook", $bookId, PDO::PARAM_INT);
                if ($stmt->execute()) {
                    if ($row = $stmt->fetch()) {
                        $bookIdentifier = new BookIdentifier(
                            $bookId,
                            $row['barcode']
                        );
                        return $bookIdentifier;
                    }
                }
            }
        }
        return null;
    }

    public function getBooksFromIds($bookIds)
    {
        $books = array();
        foreach ($bookIds as $bookId) {
            array_push($books, $this->getBookFromId($bookId));
        }
        return $books;
    }

    private function getBookFromId($idBook)
    {
        if (isset($idBook)) {
            $sql = "SELECT * FROM book WHERE id = :idBook";

            if ($stmt = $this->conn->prepare($sql)) {
                $stmt->bindParam(":idBook", $idBook, PDO::PARAM_INT);

                if ($stmt->execute()) {
                    if ($stmt->rowCount() == 1) {
                        if ($row = $stmt->fetch()) {
                            $urlPhoto = null;
                            $book = new Book(
                                $row['id'],
                                $row['title'],
                                $row['author'],
                                $row['publisher'],
                                $row['edition'],
                                $row['barcode'],
                                $urlPhoto
                            );
                            unset($stmt);

                            return $book;
                        }
                    }
                }
            }
        }
        return null;
    }

    public function getUsersFromIds($userIds) {
        $users = array();
        foreach ($userIds as $userId) {
            array_push($users, $this->getUserFromId($userId));
        }
        return $users;
    }

    private function getUserFromId($userId) {
        if (isset($userId)) {
            $sql = "SELECT * FROM customer WHERE id = :idUser";

            if ($stmt = $this->conn->prepare($sql)) {
                $stmt->bindParam(":idUser", $userId, PDO::PARAM_INT);

                if ($stmt->execute()) {
                    if ($stmt->rowCount() == 1) {
                        if ($row = $stmt->fetch()) {
                            $user = new User(
                                $row['id'],
                                $row['firstName'],
                                $row['lastName'],
                                $row['phoneNumber'],
                                $row['email']
                            );
                            unset($stmt);

                            return $user;
                        }
                    }
                }
            }
        }
        return null;
    }

    public function getMappedEmails($users)
    {
        $mappedEmails = array();
        if (isset($users)) {
            foreach ($users as $user) {
                $email = $user->getEmail();
                if (!array_key_exists($email, $mappedEmails))
                {
                    $mappedEmails[$email] = [$user->getId()];
                }
                else
                {
                    array_push(
                        $mappedEmails[$email],
                        $user->getId()
                    );
                }
            }
        }

        return $mappedEmails;
    }

    public function getConcessionByIds($concessionIds)
    {
        $concessions = array();
        if(isset($concessionIds))
        {
            foreach ($concessionIds as $concessionId)
            {
                array_push($concessions, $this->getConcessionById($concessionId));
            }
        }
        return $concessions;
    }

    private function getConcessionById($concessionId)
    {
        if (isset($concessionId)) {
            $sql = "SELECT * FROM concession WHERE id = :idConcession";

            if ($stmt = $this->conn->prepare($sql)) {
                $stmt->bindParam(":idConcession", $concessionId, PDO::PARAM_INT);

                if ($stmt->execute()) {
                    if ($stmt->rowCount() == 1) {
                        if ($row = $stmt->fetch()) {
                            $user = new Concession(
                                $row['id'],
                                $row['idCustomer'],
                                $row['idBook'],
                                $row['customerPrice'],
                                $row['feesPercentage'],
                                $row['sellingPrice']
                            );
                            unset($stmt);

                            return $user;
                        }
                    }
                }
            }
        }
        return null;
    }

}