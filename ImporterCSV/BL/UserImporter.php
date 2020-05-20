<?php


class UserImporter
{


    public function __construct()
    {

    }

    public function  __destruct()
    {

    }

    public function sanitizeEmails ($mappedEmails, $users) {

        $sanitizeEmails = array();
        if (isset($mappedEmails)) {

            foreach ($mappedEmails as $userIds) {

                if (sizeof($userIds) > 1) {

                    foreach ($userIds as $userId) {




                    }
                }
            }
        }

    }
}