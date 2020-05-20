<?php

include_once '../GlobalAGECTR/SharedConstant.php';

class UserImporter
{


    public function __construct()
    {

    }

    public function  __destruct()
    {

    }

    public function sanitizeEmails($mappedEmails, $users) {

        if (isset($mappedEmails)) {

            foreach ($mappedEmails as $userIds) {

                if (sizeof($userIds) > 1) {

                    foreach ($userIds as $userId) {
                        $this->replaceEmailInUsers($userId, $users);
                    }
                }
            }
        }

    }

    private function replaceEmailInUsers($userId, $users)
    {
        foreach ($users as $user)
        {
            if($user->getId() == $userId)
            {
                $user->setEmail($user->getId().CONST_FAKE_EMAIL);
            }
        }
    }
}