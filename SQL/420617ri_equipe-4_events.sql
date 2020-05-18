CREATE DEFINER=`root`@`localhost` EVENT `reservationExpired` ON SCHEDULE EVERY 1 HOUR STARTS '2020-05-04 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE concession 
SET reservedBy = NULL,
reservedDate = NULL,
reservedExpireDate = NULL
WHERE reservedExpireDate < (SELECT NOW());

CREATE DEFINER=`1753739`@`%` EVENT `concessionExpired` ON SCHEDULE EVERY 1 DAY STARTS '2020-03-02 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE concession SET 
state = 'disponible', manageByAGECTR = 1
WHERE state = 'renouveler' AND expireDate < (SELECT NOW());
