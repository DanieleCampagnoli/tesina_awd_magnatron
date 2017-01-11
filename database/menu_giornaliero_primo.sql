/* ordine:5*/
/*associazione tra MENU_GIORNALIERO e PIATTO con PORTATA='P' */
START TRANSACTION;

DROP TABLE IF EXISTS `menu_giornaliero_primo`;

CREATE TABLE IF NOT EXISTS `menu_giornaliero_primo` (
  `ID_MENU_SETTIMANALE` INTEGER NOT NULL,
  `GIORNO` INTEGER NOT NULL,
  `ID_PIATTO` INTEGER NOT NULL,

   FOREIGN KEY fk_menu_giornaliero(`ID_MENU_SETTIMANALE`,`GIORNO`) 
   REFERENCES menu_giornaliero(`ID_MENU_SETTIMANALE`,`GIORNO`)
   ON DELETE CASCADE ON UPDATE CASCADE,
   
   FOREIGN KEY fk_piatto(`ID_PIATTO`) 
   REFERENCES piatto(`ID_PIATTO`)  
   ON DELETE CASCADE ON UPDATE CASCADE
);


/*trigger MENU_GIORNALIERO_PRIMO */
/*il piatto deve essere un primo*/
/*
vincolo comune
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Piatto_Primo//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Piatto_Primo(IN ID_PIATTO INTEGER)
READS SQL DATA
BEGIN
         IF (select p.PORTATA from piatto p where p.ID_PIATTO=ID_PIATTO)!='P' THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuGiornalieroPrimo: I PIATTI DEVONO AVERE PORTATA=P ';
        END IF;  
    

END//
DELIMITER ;

/*applico vincolo a ID_PIATTO per update */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Primo_update//
create trigger Chk_Piatto_Primo_update BEFORE UPDATE  ON menu_giornaliero_primo
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Primo(NEW.ID_PIATTO);

END;//
delimiter ;


/*applico vincolo a ID_PIATTO per insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Primo_insert//
create trigger Chk_Piatto_Primo_insert BEFORE INSERT  ON menu_giornaliero_primo
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Primo(NEW.ID_PIATTO);

END;//
delimiter ;


/*insert MENU_GIORNALIERO_PRIMO */
/*menu settimanale 1 giorno 1*/
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,1);
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,2);

/*menu settimanale 1 giorno 2*/
INSERT INTO menu_giornaliero_primo(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,3);
INSERT INTO menu_giornaliero_primo(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,4);

/*menu settimanale 1 giorno 3*/
INSERT INTO menu_giornaliero_primo(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,1);
INSERT INTO menu_giornaliero_primo(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,4);


/*menu settimanale 1 giorno 4*/
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,3);
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,2);


/*menu settimanale 1 giorno 5*/
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,3);
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,2);


/*menu settimanale 1 giorno 6*/
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,3);
INSERT INTO menu_giornaliero_primo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,2);

COMMIT;




