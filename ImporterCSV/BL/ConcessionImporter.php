<?php


class ConcessionImporter
{

    /**
     * ConcessionImporter constructor.
     */
    public function __construct()
    {
    }

    public function replaceConcessionBookIdsWithExtractBookIds($concessions, $extracts)
    {
        foreach ($concessions as $concession)
        {

            print_r($concession);
            $idConcession = $concession->getIdConcession();
            foreach ($extracts as $extract)
            {
                if($extract->getIdConcession() == $idConcession)
                {
                    $concession->setIdBook($extract->getIdBook());
                }
            }
        }
    }
}