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
            array_push($userIds, $extract->getIdUser());
        }
        return $userIds;
    }

    public function extractBookIdsFromImport($extracts)
    {
        $userIds = array();
        foreach ($extracts as $extract)
        {
            array_push($userIds, $extract->getIdBook());
        }
        return $userIds;
    }



}