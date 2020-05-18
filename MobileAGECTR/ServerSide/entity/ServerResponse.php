<?php


class ServerResponse
{
    public $succes;
    public $message;

    /**
     * @return mixed
     */
    public function getSucces()
    {
        return $this->succes;
    }

    /**
     * @param mixed $succes
     */
    public function setSucces($succes)
    {
        $this->succes = $succes;
    }

    /**
     * @return mixed
     */
    public function getMessage()
    {
        return $this->message;
    }

    /**
     * @param mixed $message
     */
    public function setMessage($message)
    {
        $this->message = $message;
    }

}