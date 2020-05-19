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
            $sql = "INSERT INTO book (id,title,author,publisher,barcode, section, createdBy)
                                VALUES(:id,:title,:author,:publisher,:barcode, :section, :createdBy)";
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

                $stmt->bindParam(":id",$idBook , PDO::PARAM_INT);
                $stmt->bindParam(":title", $title, PDO::PARAM_STR);
                $stmt->bindParam(":author", $author, PDO::PARAM_STR);
                $stmt->bindParam(":publisher", $publisher, PDO::PARAM_STR);
                $stmt->bindParam(":barcode", $barcode, PDO::PARAM_STR);
                //$stmt->bindParam(":urlPhoto", $urlPhoto, PDO::PARAM_INT); --> MUST BE AN INTEGER <---
                $stmt->bindParam(":section", $section);
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