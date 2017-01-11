/* ordine:8*/
/*associazione tra MENU_GIORNALIERO e PIATTO con PORTATA='D' */
START TRANSACTION;

DROP TABLE IF EXISTS `menu_giornaliero_dessert`;

CREATE TABLE IF NOT EXISTS `menu_giornaliero_dessert` (
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


/*trigger MENU_GIORNALIERO_DESSERT */
/*il piatto deve essere un dessert*/
/*
vincolo comune
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Piatto_Dessert//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Piatto_Dessert(IN ID_PIATTO INTEGER)
READS SQL DATA
BEGIN
         IF (select p.PORTATA from piatto p where p.ID_PIATTO=ID_PIATTO)!='D' THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuGiornalieroDessert: I PIATTI DEVONO AVERE PORTATA=D';
        END IF;  
    

END//
DELIMITER ;

/*applico vincolo a ID_PIATTO per update */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Dessert_update//
create trigger Chk_Piatto_Dessert_update BEFORE UPDATE  ON menu_giornaliero_dessert
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Dessert(NEW.ID_PIATTO);

END;//
delimiter ;


/*applico vincolo a ID_PIATTO per insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Dessert_insert//
create trigger Chk_Piatto_Dessert_insert BEFORE INSERT  ON menu_giornaliero_dessert
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Dessert(NEW.ID_PIATTO);

END;//
delimiter ;


/*insert MENU_GIORNALIERO_SECONDO */
/*menu settimanale 1 giorno 1*/
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,23);
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,1,24);

/*menu settimanale 1 giorno 2*/
INSERT INTO menu_giornaliero_dessert(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,25);
INSERT INTO menu_giornaliero_dessert(ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,2,26);

/*menu settimanale 1 giorno 3*/
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,23);
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,3,24);


/*menu settimanale 1 giorno 4*/
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,25);
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,4,26);


/*menu settimanale 1 giorno 5*/
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,23);
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,5,24);


/*menu settimanale 1 giorno 6*/
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,25);
INSERT INTO menu_giornaliero_dessert (ID_MENU_SETTIMANALE,GIORNO,ID_PIATTO) 
              VALUES(1,6,26);

COMMIT;