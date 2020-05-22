<?php
namespace Entities;

class Concession
{
    private $idConcession;
    private $idCustomer;
    private $idBook;
    private $customerPrice;
    private $feesPercentage;
    private $sellingPrice;

    /**
     * Concession constructor.
     * @param $idConcession
     * @param $idCustomer
     * @param $idBook
     * @param $customerPrice
     * @param $feesPercentage
     * @param $sellingPrice
     */
    public function __construct($idConcession, $idCustomer, $idBook, $customerPrice, $feesPercentage, $sellingPrice)
    {
        $this->idConcession = $idConcession;
        $this->idCustomer = $idCustomer;
        $this->idBook = $idBook;
        $this->customerPrice = $customerPrice;
        $this->feesPercentage = $feesPercentage;
        $this->sellingPrice = $sellingPrice;
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
    public function getIdCustomer()
    {
        return $this->idCustomer;
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
    public function getCustomerPrice()
    {
        return $this->customerPrice;
    }

    /**
     * @return mixed
     */
    public function getFeesPercentage()
    {
        return $this->feesPercentage;
    }

    /**
     * @return mixed
     */
    public function getSellingPrice()
    {
        return $this->sellingPrice;
    }

    /**
     * @param mixed $idConcession
     */
    public function setIdConcession($idConcession)
    {
        $this->idConcession = $idConcession;
    }

    /**
     * @param mixed $idCustomer
     */
    public function setIdCustomer($idCustomer)
    {
        $this->idCustomer = $idCustomer;
    }

    /**
     * @param mixed $customerPrice
     */
    public function setCustomerPrice($customerPrice)
    {
        $this->customerPrice = $customerPrice;
    }

    /**
     * @param mixed $feesPercentage
     */
    public function setFeesPercentage($feesPercentage)
    {
        $this->feesPercentage = $feesPercentage;
    }

    /**
     * @param mixed $sellingPrice
     */
    public function setSellingPrice($sellingPrice)
    {
        $this->sellingPrice = $sellingPrice;
    }

}