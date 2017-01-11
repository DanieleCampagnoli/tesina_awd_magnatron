/* ordine:1*/
START TRANSACTION;
/*
tabella che contiene i dati di una persona
il campo ruolo indica il ruolo svolto dall persona nel sistema:
-C: cuoco
-I: impiegato
-A: amministratore
*/
DROP TABLE IF EXISTS `persona`;

CREATE TABLE IF NOT EXISTS `persona` (
  `MATRICOLA` int NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(25) NOT NULL,
  `USERNAME` varchar(25) unique NOT NULL, 
  `NOME` varchar(25) NOT NULL,
  `COGNOME` varchar(25) NOT NULL, 
  `DATA_NASCITA` DATE NOT NULL,
  `RUOLO` char(1) NOT NULL,
  PRIMARY KEY (`MATRICOLA`) 
);


/*trigger di sulla tabella persona*/

/*controllo validità campo RUOLO per insert e update*/
/*
vincolo comune
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Persona_Ruolo//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Persona_Ruolo(IN ruolo CHAR(1))
READS SQL DATA
BEGIN
         declare msg varchar(128);
         IF( ruolo!='A' AND ruolo!='C' AND ruolo!='I') THEN 
            set msg = concat('ErrorPersonaRuolo: RUOLO NON CORRETTO');
            signal sqlstate '45000' set message_text = msg;
        END IF;  
    

END//
DELIMITER ;

/*applico vincolo a RUOLO per update */
delimiter //
DROP TRIGGER IF EXISTS Chk_Persona_Ruolo_update//
create trigger Chk_Persona_Ruolo_insert_update BEFORE UPDATE  ON persona
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Persona_Ruolo(NEW.RUOLO);

END;//
delimiter ;


/*applico vincolo a RUOLO per insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_Persona_Ruolo_insert//
create trigger Chk_Persona_Ruolo_insert BEFORE INSERT  ON persona
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Persona_Ruolo(NEW.RUOLO);

END;//
delimiter ;



/*insert PERSONA*/
/*amministratore*/
INSERT INTO persona (PASSWORD,NOME,COGNOME,DATA_NASCITA,RUOLO,USERNAME) 
              VALUES('secure123','mario','rossi','1991-05-22','A','user1');
/*cuoco*/
INSERT INTO persona (PASSWORD,NOME,COGNOME,DATA_NASCITA,RUOLO,USERNAME) 
              VALUES('secure123','joe','bastianich','1987-05-22','C','user2');
/*impiegati*/
INSERT INTO persona (PASSWORD,NOME,COGNOME,DATA_NASCITA,RUOLO,USERNAME) 
              VALUES('secure123','ugo','fantozzi','1975-10-29','I','user3');
INSERT INTO persona (PASSWORD,NOME,COGNOME,DATA_NASCITA,RUOLO,USERNAME) 
              VALUES('secure123','paolo','bitta','1980-10-29','I','user4');
INSERT INTO persona (PASSWORD,NOME,COGNOME,DATA_NASCITA,RUOLO,USERNAME) 
              VALUES('secure123','luca','nervi','1982-03-29','I','user5');
COMMIT;