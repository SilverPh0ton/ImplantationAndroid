<?php
include_once 'NewConfigDB.php';

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
            $sql = "INSERT INTO book (id,title,author,publisher,barcode,createdBy)
                                VALUES(:id,:title,:author,:publisher,:barcode,:createdBy)";
            if($stmt = $this->conn->prepare($sql))
            {
                $createdBy = CONST_CREATEDBY_AUTOIMPORT;

                $stmt->bindParam(":id", $book->getIdBook(), PDO::PARAM_INT);
                $stmt->bindParam(":title", $book->getTitle(), PDO::PARAM_STR);
                $stmt->bindParam(":author", $book->getAuthor(), PDO::PARAM_STR);
                $stmt->bindParam(":publisher", $book->getPublisher(), PDO::PARAM_STR);
                $stmt->bindParam(":barcode", $book->getBarcode(), PDO::PARAM_STR);
                //$stmt->bindParam(":urlPhoto", $book->getUrlPhoto(), PDO::PARAM_INT); --> MUST BE AN INTEGER <---
                $stmt->bindParam(":createdBy", $createdBy, PDO::PARAM_STR);

                if($stmt->execute())
                {
                    return true;
                }
            }
        }
        return false;
    }
}