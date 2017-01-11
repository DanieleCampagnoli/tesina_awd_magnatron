/* ordine:6*/
/*associazione tra MENU_GIORNALIERO e PIATTO con PORTATA='S' */
START TRANSACTION;

DROP TABLE IF EXISTS `menu_giornaliero_secondo`;

CREATE TABLE IF NOT EXISTS `menu_giornaliero_secondo` (
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


/*trigger MENU_GIORNALIERO_SECONDO */
/*il piatto deve essere un secondo*/
/*
vincolo comune
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Piatto_Secondo//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Piatto_Secondo(IN ID_PIATTO INTEGER)
READS SQL DATA
BEGIN
         IF (select p.PORTATA from piatto p where p.ID_PIATTO=ID_PIATTO)!='S' THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuGiornalieroSecondo: I PIATTI DEVONO AVERE PORTATA=S';
        END IF;  
    

END//
DELIMITER ;

/*applico vincolo a ID_PIATTO per update */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Secondo_update//
create trigger Chk_Piatto_Secondo_update BEFORE UPDATE  ON menu_giornaliero_secondo
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Secondo(NEW.ID_PIATTO);

END;//
delimiter ;


/*applico vincolo a ID_PIATTO per insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Secondo_insert//
create trigger Chk_Piatto_Secondo_insert BEFORE INSERT  ON menu_giornaliero_secondo
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Secondo(NEW.ID_PIATTO);

END;//
delimiter ;


/*insert MENU_GIORNALIERO_SECONDO */
/*menu settimanale 1 giorno 1*/
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,8);
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,9);

/*menu settimanale 1 giorno 2*/
INSERT INTO menu_giornaliero_secondo(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,10);
INSERT INTO menu_giornaliero_secondo(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,11);

/*menu settimanale 1 giorno 3*/
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,12);
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,8);


/*menu settimanale 1 giorno 4*/
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,13);
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,14);


/*menu settimanale 1 giorno 5*/
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,16);
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,9);


/*menu settimanale 1 giorno 6*/
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,10);
INSERT INTO menu_giornaliero_secondo (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,18);

COMMIT;
