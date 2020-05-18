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
            $sql = "INSERT INTO book"; //Continuer query
        }
        return false;
    }
}