/* ordine:7*/
/*associazione tra MENU_GIORNALIERO e PIATTO con PORTATA='C' */
START TRANSACTION;

DROP TABLE IF EXISTS `menu_giornaliero_contorno`;

CREATE TABLE IF NOT EXISTS `menu_giornaliero_contorno` (
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


/*trigger MENU_GIORNALIERO_CONTORNO */
/*il piatto deve essere un contorno*/
/*
vincolo comune
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Piatto_Contorno//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Piatto_Contorno(IN ID_PIATTO INTEGER)
READS SQL DATA
BEGIN
         IF (select p.PORTATA from piatto p where p.ID_PIATTO=ID_PIATTO)!='C' THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuGiornalieroContorno: I PIATTI DEVONO AVERE PORTATA=C';
        END IF;  
    

END//
DELIMITER ;

/*applico vincolo a ID_PIATTO per update */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Contorno_update//
create trigger Chk_Piatto_Contorno_update BEFORE UPDATE  ON menu_giornaliero_contorno
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Contorno(NEW.ID_PIATTO);

END;//
delimiter ;


/*applico vincolo a ID_PIATTO per insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Contorno_insert//
create trigger Chk_Piatto_Contorno_insert BEFORE INSERT  ON menu_giornaliero_contorno
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Contorno(NEW.ID_PIATTO);

END;//
delimiter ;


/*insert MENU_GIORNALIERO_SECONDO */
/*menu settimanale 1 giorno 1*/
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,19);
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,20);

/*menu settimanale 1 giorno 2*/
INSERT INTO menu_giornaliero_contorno(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,21);
INSERT INTO menu_giornaliero_contorno(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,22);

/*menu settimanale 1 giorno 3*/
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,19);
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,20);


/*menu settimanale 1 giorno 4*/
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,21);
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,22);


/*menu settimanale 1 giorno 5*/
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,19);
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,20);


/*menu settimanale 1 giorno 6*/
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,21);
INSERT INTO menu_giornaliero_contorno (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,22);

COMMIT;