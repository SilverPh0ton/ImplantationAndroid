<?php
include_once 'OldConfigDB.php';
include_once 'Entities/BookIdentifier.php';

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

    public function getBooksIdentifers($bookIds)
    {
        if (isset($bookIds)) {
            $booksIdentifiers = array();
            foreach ($bookIds as $bookId)
            {
                array_push($booksIdentifiers, $this->getBookIdentifiers($bookId));
            }
            return $booksIdentifiers;
        }
        return null;
    }

    private function getBookIdentifiers($bookId)
    {
        if(isset($bookId))
        {
            $sql = "SELECT barcode FROM book WHERE id = :idBook";
            if($stmt = $this->conn->prepare($sql))
            {
                $stmt->bindParam(":idBook", $bookId, PDO::PARAM_INT);
                if($stmt->execute())
                {
                    if($row = $stmt->fetch())
                    {
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
}