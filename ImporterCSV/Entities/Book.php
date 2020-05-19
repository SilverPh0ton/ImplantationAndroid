<?php


class Book
{
    public $idBook;
    public $title;
    public $author;
    public $publisher;
    public $edition;
    public $barcode;
    public $urlPhoto;

    /**
     * Book constructor.
     * @param $idBook
     * @param $title
     * @param $author
     * @param $publisher
     * @param $edition
     * @param $barcode
     * @param $urlPhoto
     */
    public function __construct($idBook, $title, $author, $publisher, $edition, $barcode, $urlPhoto)
    {
        $this->idBook = $idBook;
        $this->title = $title;
        $this->author = $author;
        $this->publisher = $publisher;
        $this->edition = $edition;
        $this->barcode = $barcode;
        $this->urlPhoto = $urlPhoto;
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
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * @return mixed
     */
    public function getAuthor()
    {
        return $this->author;
    }

    /**
     * @return mixed
     */
    public function getPublisher()
    {
        return $this->publisher;
    }

    /**
     * @return mixed
     */
    public function getEdition()
    {
        return $this->edition;
    }

    /**
     * @return mixed
     */
    public function getBarcode()
    {
        return $this->barcode;
    }

    /**
     * @return mixed
     */
    public function getUrlPhoto()
    {
        return $this->urlPhoto;
    }

}