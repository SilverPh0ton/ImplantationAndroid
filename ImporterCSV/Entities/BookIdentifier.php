<?php


class BookIdentifier
{
    private $idBook;
    private $isbn;

    /**
     * BookIdentifier constructor.
     * @param $idBook
     * @param $isbn
     */
    public function __construct($idBook, $isbn)
    {
        $this->idBook = $idBook;
        $this->isbn = $isbn;
    }

    /**
     * @return mixed
     */
    public function getIdBook()
    {
        return $this->idBook;
    }

    /**
     * @return mixed
     */
    public function getIsbn()
    {
        return $this->isbn;
    }

}