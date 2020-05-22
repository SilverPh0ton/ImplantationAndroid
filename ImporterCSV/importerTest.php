<?php
//Les includes.
include_once 'BL/ExcelImporter.php';
include_once("Entities/BookIdentifier.php");
include_once("BL/BookImporter.php");

//Déclaration des variables.
$url = "https://www.googleapis.com/books/v1/volumes?q=isbn:9780553804577&key=AIzaSyAX_Nwfqm3p7z_UUD4i-E2cbS2plhECubM" ;


$page = file_get_contents($url);

$data = json_decode($page, true);

var_dump($data['items'][0]['volumeInfo']['title']);

/*
$headers = array(
    "Content-Type: application/json"
);
curl_setopt_array($rest,
    array(
        CURLOPT_URL => $url,
        CURLOPT_HEADER => $headers,
        CURLOPT_RETURNTRANSFER => 1,
        CURLOPT_HTTPGET => 1,
        CURLOPT_SSL_VERIFYHOST, 0,
        CURLOPT_SSL_VERIFYPEER, 0
    )
$response = curl_exec($rest);
curl_close($rest);
);*/