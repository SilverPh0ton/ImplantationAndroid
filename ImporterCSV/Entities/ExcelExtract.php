<?php


class ExcelExtract
{
    //TODO Verify if its an idConcession or a reference code.
    private $idConcession;
    private $idBook;
    private $idUser;

    /**
     * ExcelExtract constructor.
     * @param $idConcession
     * @param $idBook
     * @param $idUser
     */
    public function __construct($idConcession, $idBook, $idUser)
    {
        $this->idConcession = $idConcession;
        $this->idBook = $idBook;
        $this->idUser = $idUser;
    }

    /**
     * @return mixed
     */
    public function getIdConcession()
    {
        return $this->idConcession;
    }

    /**
     * @return mixed
     */
    public function getIdBook()
    {
        return $this->idBook;
    }

    /**
     * @param mixed $idBook
     */
    public function setIdBook($idBook)
    {
        $this->idBook = $idBook;
    }



    /**
     * @return mixed
     */
    public function getIdUser()
    {
        return $this->idUser;
    }

    /**
     * @throws Exception
     */
    private function validate()
    {
        $this->validateIdConcession();
        $this->validateIdBook();
        $this->validateIdUser();
    }

    /**
     * @throws Exception
     */
    private function validateIdConcession()
    {
        if(!isset($this->idConcession))
        {
            throw new Exception("Une des concessions n'a pas tout les informations requises. (idConcession)");
        }
    }

    /**
     * @throws Exception
     */
    private function validateIdBook()
    {
        if(!isset($this->idBook))
        {
            throw new Exception("Une des concessions n'a pas tout les informations requises. (idBook)");
        }
    }

    /**
     * @throws Exception
     */
    private function validateIdUser()
    {
        if(!isset($this->idUser))
        {
            throw new Exception("Une des concessions n'a pas tout les informations requises. (idUser)");
        }
    }
}