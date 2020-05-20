<?php
include_once 'NewConfigDB.php';
include_once '../GlobalAGECTR/SharedConstant.php';

class NewDB extends NewConfigDB
{
    /**
     * NewDB constructor.
     */
    public function __construct()
    {
        parent::__construct();
    }

    public function __destruct()
    {
        parent::__destruct();
    }

    /**
     * @param $books
     * @throws Exception
     */
    public function createBooks($books)
    {
        foreach($books as $book)
        {
            if(!$this->createBook($book))
            {
                throw new Exception("Error while inserting books");
            }
        }
    }

    private function createBook($book)
    {
        if(isset($book))
        {
            //TODO Add the urlPhoto when we have the id (when we manage to download the picture from the url in our repo)
            $sql = "INSERT INTO book (id,title,author,publisher,barcode, section, createdBy, edition)
                                VALUES(:id,:title,:author,:publisher,:barcode, :section, :createdBy, :edition)";
            if($stmt = $this->conn->prepare($sql))
            {
                $createdBy = CONST_CREATEDBY_AUTOIMPORT;
                $section = CONST_FAKE_SECTION;

                $idBook = $book->getIdBook();
                $title = $book->getTitle();
                $author = $book->getAuthor();
                $publisher = $book->getPublisher();
                $barcode = $book->getBarcode();
                $urlPhoto = $book->getUrlPhoto();
                $edition = $book->getEdition();

                $stmt->bindParam(":id",$idBook , PDO::PARAM_INT);
                $stmt->bindParam(":title", $title, PDO::PARAM_STR);
                $stmt->bindParam(":author", $author, PDO::PARAM_STR);
                $stmt->bindParam(":publisher", $publisher, PDO::PARAM_STR);
                $stmt->bindParam(":barcode", $barcode, PDO::PARAM_STR);
                //$stmt->bindParam(":urlPhoto", $urlPhoto, PDO::PARAM_INT); --> MUST BE AN INTEGER <---
                $stmt->bindParam(":section", $section);
                $stmt->bindParam(":createdBy", $createdBy, PDO::PARAM_STR);
                $stmt->bindParam(":edition", $edition, PDO::PARAM_STR);

                if($stmt->execute())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param $users
     * @throws Exception
     */
    public function createUsers($users)
    {
        foreach ($users as $user)
        {
            if(!$this->createUser($user))
            {
                throw new Exception("Inserting users failed");
            }
        }
    }

    private function createUser($user)
    {
        if(isset($user))
        {
            $sql = "INSERT INTO customer (id,firstName,lastName,phoneNumber,email, password, createdBy)
                                    VALUES(:id, :firstName, :lastName, :phoneNumber, :email, :password, :createdBy)";
            if($stmt = $this->conn->prepare($sql))
            {
                $createdBy = CONST_CREATEDBY_AUTOIMPORT;

                $id = $user->getId();
                $firstName = $user->getFirstName();
                $lastName = $user->getLastName();
                $phoneNumber = $user->getPhoneNumber();
                $email = $user->getEmail();
                $password = '123'; //$generatedPassword ???

                $stmt->bindParam(":id", $id, PDO::PARAM_INT);
                $stmt->bindParam(":firstName", $firstName, PDO::PARAM_STR);
                $stmt->bindParam(":lastName", $lastName, PDO::PARAM_STR);
                $stmt->bindParam(":phoneNumber", $phoneNumber, PDO::PARAM_STR);
                $stmt->bindParam(":email", $email, PDO::PARAM_STR);
                $stmt->bindParam(":password", $password, PDO::PARAM_STR);
                $stmt->bindParam(":createdBy", $createdBy, PDO::PARAM_STR);
                if($stmt->execute())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param $concessions
     * @throws Exception
     */
    public function createConcessions($concessions)
    {
        foreach ($concessions as $concession)
        {
            if(!$this->createConcession($concession))
            {
                throw new Exception("Inserting concessions failed");
            }
        }
    }

    private function createConcession($concession)
    {
        if(isset($concession))
        {
            $sql = "INSERT INTO concession (id,idCustomer,idBook,customerPrice,feesPercentage,sellingPrice,isAnnotated,createdBy)
                                    VALUES(:id, :idCustomer, :idBook, :customerPrice, :feesPercentage, :sellingPrice, :isAnnotated, :createdBy)";
            if($stmt = $this->conn->prepare($sql))
            {
                $createdBy = CONST_CREATEDBY_AUTOIMPORT;
                $isAnnotated = CONST_FAKE_ANNOTATED;

                $idConcession = $concession->getIdConcession();
                $idCustomer = $concession->getIdCustomer();
                $idBook = $concession->getIdBook();
                $customerPrice = $concession->getCustomerPrice();
                $feesPercentage = $concession->getFeesPercentage();
                $sellingPrice = $concession->getSellingPrice();

                $stmt->bindParam(":id", $idConcession, PDO::PARAM_INT);
                $stmt->bindParam(":idCustomer", $idCustomer, PDO::PARAM_INT);
                $stmt->bindParam(":idBook", $idBook, PDO::PARAM_INT);
                $stmt->bindParam(":customerPrice", $customerPrice);
                $stmt->bindParam(":feesPercentage", $feesPercentage);
                $stmt->bindParam(":sellingPrice", $sellingPrice);
                $stmt->bindParam(":isAnnotated", $isAnnotated);
                $stmt->bindParam(":createdBy", $createdBy, PDO::PARAM_STR);
                if($stmt->execute())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public function deleteAll() {
        $this->deleteAllConcessions();
        $this->deleteAllUsers();
        $this->deleteAllBooks();
    }

    private function deleteAllBooks()
    {
        $sql = "DELETE FROM book";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->execute();
        }
    }

    private function deleteAllConcessions()
    {
        $sql = "DELETE FROM concession";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->execute();
        }
    }

    private function deleteAllUsers()
    {
        $sql = "DELETE FROM customer";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->execute();
        }
    }
}