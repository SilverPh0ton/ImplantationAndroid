<?php
include_once 'SimpleXLSX.php';
include_once 'Entities/ExcelExtract.php';

class ExcelImporter
{
    /**
     * ExcelImporter constructor.
     */
    public function __construct()
    {
    }

    /**
     * @param $filename
     * @param $
     * @param $
     * @param $
     * @return mixed
     * @throws Exception
     */
    public function import($filename)
    {
        if($filename)
        {
            $xlsx = new SimpleXLSX($filename);
            if ($xlsx->success()) {
                $infos = $xlsx->rows();
                $ctr = 0;
                $extracts = array();
                foreach ($infos as $info) {
                    if ($ctr > 0) {
                        array_push($extracts, new ExcelExtract($info[0], $info[2], $info[4]));
                    }
                    $ctr++;
                }
                return $extracts;
            } else
            {
                throw new Exception('xlsx error: '. $xlsx->error());
            }

        }
        else
        {
            throw new Exception("No filename given to ExcelImporter");
        }
    }

    public function extractUserIdsFromImport($extracts)
    {
        $userIds = array();
        foreach ($extracts as $extract)
        {
            if(!in_array($extract->getIdUser(), $userIds))
            {
                array_push($userIds, $extract->getIdUser());
            }
        }
        return $userIds;
    }

    public function extractBookIdsFromImport($sanitizedExtract)
    {
        $bookIds = array();
        foreach ($sanitizedExtract as $extract)
        {
            if(!in_array($extract->getIdBook(), $bookIds))
            {
                array_push($bookIds, $extract->getIdBook());
            }
        }
        return $bookIds;
    }


    public function extractConcessionIdsFromImport($sanitizedExtract)
    {
        $concessionIds = array();
        foreach ($sanitizedExtract as $extract)
        {
            if(!in_array($extract->getIdConcession(), $concessionIds))
            {
                array_push($concessionIds, $extract->getIdConcession());
            }
        }
        return $concessionIds;
    }


    public function sanitized($extracts) {

        $sanitizedExtracts = array();
        foreach ($extracts as $extract) {
            if (!empty($extract->getIdConcession())) {
                array_push($sanitizedExtracts, $extract);
            }

        }
        return $sanitizedExtracts;

    }



}