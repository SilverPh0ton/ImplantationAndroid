<?php


class Concession
{
    public $idConcession;
    public $state;
    public $customerPrice;
    public $sellingPrice;
    public $idCustomer;
    public $book;
    public $reservedExpireDate;
    public $urlPhoto;
    public $isAnnotated;
    public $expireDate;
    public $refuseReason;

    /**
     * @return mixed
     */
    public function getIdConcession()
    {
        return $this->idConcession;
    }

    /**
     * @param mixed $idConcession
     */
    public function setIdConcession($idConcession)
    {
        $this->idConcession = $idConcession;
    }

    /**
     * @return mixed
     */
    public function getState()
    {
        return $this->state;
    }

    /**
     * @param mixed $state
     */
    public function setState($state)
    {
        $this->state = $state;
    }

    /**
     * @return mixed
     */
    public function getCustomerPrice()
    {
        return $this->customerPrice;
    }

    /**
     * @param mixed $customerPrice
     */
    public function setCustomerPrice($customerPrice)
    {
        $this->customerPrice = $customerPrice;
    }

    /**
     * @return mixed
     */
    public function getSellingPrice()
    {
        return $this->sellingPrice;
    }

    /**
     * @param mixed $sellingPrice
     */
    public function setSellingPrice($sellingPrice)
    {
        $this->sellingPrice = $sellingPrice;
    }

    /**
     * @return mixed
     */
    public function getIdCustomer()
    {
        return $this->idCustomer;
    }

    /**
     * @param mixed $idCustomer
     */
    public function setIdCustomer($idCustomer)
    {
        $this->idCustomer = $idCustomer;
    }

    /**
     * @return mixed
     */
    public function getBook()
    {
        return $this->book;
    }

    /**
     * @param mixed $book
     */
    public function setBook($book)
    {
        $this->book = $book;
    }

    /**
     * @return mixed
     */
    public function getReservedExpireDate()
    {
        return $this->reservedExpireDate;
    }

    /**
     * @param mixed $reservedExpireDate
     */
    public function setReservedExpireDate($reservedExpireDate)
    {
        $this->reservedExpireDate = $reservedExpireDate;
    }

    /**
     * @return mixed
     */
    public function getUrlPhoto()
    {
        return $this->urlPhoto;
    }

    /**
     * @param mixed $urlPhoto
     */
    public function setUrlPhoto($urlPhoto)
    {
        $this->urlPhoto = $urlPhoto;
    }

    /**
     * @return mixed
     */
    public function getIsAnnotated()
    {
        return $this->isAnnotated;
    }

    /**
     * @param mixed $isAnnotated
     */
    public function setIsAnnotated($isAnnotated)
    {
        $this->isAnnotated = $isAnnotated;
    }

    /**
     * @return mixed
     */
    public function getExpireDate()
    {
        return $this->expireDate;
    }

    /**
     * @param mixed $expireDate
     */
    public function setExpireDate($expireDate)
    {
        $this->expireDate = $expireDate;
    }

    /**
     * @return mixed
     */
    public function getRefuseReason()
    {
        return $this->refuseReason;
    }

    /**
     * @param mixed $refuseReason
     */
    public function setRefuseReason($refuseReason)
    {
        $this->refuseReason = $refuseReason;
    }



}