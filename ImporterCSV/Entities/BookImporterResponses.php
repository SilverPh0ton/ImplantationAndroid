<?php


class BookImporterResponses
{

    private $books;
    private $unfoundIds;



    /**
     * BookImporterResponses constructor.
     */
    public function __construct()
    {
        $this->books = array();
        $this->unfoundIds = array();
    }

    /**
     * @return mixed
     */
    public function getBooks()
    {
        return $this->books;
    }

    /**
     * @return mixed
     */
    public function getUnfoundIds()
    {
        return $this->unfoundIds;
    }

    public function addBook($book) {
        array_push($this->books, $book);
    }

    public function addUnfoundId($unfoundId) {
        array_push($this->unfoundIds, $unfoundId);
    }

}