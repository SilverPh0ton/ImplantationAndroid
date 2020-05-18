<?php


class NewConfigDB
{
    private  $DB_SERVER;
    private  $DB_USERNAME;
    private  $DB_PASSWORD;
    private  $DB_NAME;
    protected  $conn;

    /**
     * ConfigDB constructor.
     */
    public function __construct()
    {
        $this->DB_SERVER = 'localhost';
        $this->DB_USERNAME = '1750944';
        $this->DB_PASSWORD = '1750944';
        $this->DB_NAME = '420617ri_equipe-4';

        try{

            /* Attempt to connect to MySQL database */
            $this->conn = new PDO("mysql:host=$this->DB_SERVER;dbname=$this->DB_NAME",$this->DB_USERNAME,$this->DB_PASSWORD);
            // Set the PDO error mode to exception

            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch(PDOException $e){
            die("ERROR: Could not connect. " . $e->getMessage());
        }
    }


    public function __destruct() {
        $this->conn = null;
    }
}