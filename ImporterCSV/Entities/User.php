<?php


class User
{
    public $id;
    public $firstName;
    public $lastName;
    public $phoneNumber;
    public $email;

    /**
     * User constructor.
     * @param $id
     * @param $firstName
     * @param $lastName
     * @param $phoneNumber
     * @param $email
     */
    public function __construct($id, $firstName, $lastName, $phoneNumber, $email)
    {
        $this->id = $id;
        $this->firstName = $firstName;
        $this->lastName = $lastName;
        $this->phoneNumber = $phoneNumber;
        $this->email = $email;
        $this->sanitizePhoneNumber();
    }

    private function sanitizePhoneNumber()
    {
        $this->phoneNumber = str_replace('(', '', $this->phoneNumber);
        $this->phoneNumber = str_replace(')', '', $this->phoneNumber);
        $this->phoneNumber = str_replace('-', '', $this->phoneNumber);
        $this->phoneNumber = str_replace(' ', '', $this->phoneNumber);

        if(strlen($this->phoneNumber) != 10)
        {
          $this->phoneNumber = null;
        }
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @return mixed
     */
    public function getFirstName()
    {
        return $this->firstName;
    }

    /**
     * @return mixed
     */
    public function getLastName()
    {
        return $this->lastName;
    }

    /**
     * @return mixed
     */
    public function getPhoneNumber()
    {
        return $this->phoneNumber;
    }

    /**
     * @return mixed
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @param mixed $email
     */
    public function setEmail($email)
    {
        $this->email = $email;
    }






}