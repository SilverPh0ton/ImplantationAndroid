<?php


class ConcessionImporter
{

    /**
     * ConcessionImporter constructor.
     */
    public function __construct()
    {
    }

    public function replaceConcessionBookIdsWithExtractBookIds(array $concessions, $extracts)
    {
        foreach ($concessions as $concession)
        {
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