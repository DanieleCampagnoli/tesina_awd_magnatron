/* ordine:3*/
START TRANSACTION;
/*
tabella che contiene i menu giornalieri settimanali

*/

DROP TABLE IF EXISTS `menu_settimanale`;

CREATE TABLE IF NOT EXISTS `menu_settimanale` (
  `ID_MENU_SETTIMANALE` int NOT NULL AUTO_INCREMENT,
  `DATA_INIZIO` DATE NOT NULL,
  `ATTIVO` BOOLEAN NOT NULL,
  `ID_CUOCO` INT NOT NULL,
   
   FOREIGN KEY fk_cuoco(`ID_CUOCO`) 
   REFERENCES persona(`MATRICOLA`)
   ON DELETE CASCADE ON UPDATE CASCADE,
 
  PRIMARY KEY (`ID_MENU_SETTIMANALE`)
);

/*trigger MENU_SETTIMANALE */
/* ID_CUOCO deve essere un Cuoco*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Persona_Cuoco//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Persona_Cuoco(IN MATRICOLA INTEGER)
READS SQL DATA
BEGIN
         IF (select p.RUOLO from persona p where p.MATRICOLA=MATRICOLA)!='C' THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuSettimanale: ID_CUOCO deve essere un cuoco';
        END IF;  
    

END//
DELIMITER ;

/*
vincolo: un menu settimanale ha ATTIVO=TRUE se e solo se ha 6 menu giornalieri 
e ogni menu giornaliero ha due scelte per ogni portata 
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_MenuSettimanale_activation//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_MenuSettimanale_activation(IN ID_MENU_SETTIMANALE INTEGER)
READS SQL DATA
BEGIN

         /*primo*/ 
         IF (select count(*)
             /*per ogni menu giornaliero conto i piatti assegnati al menu*/ 
             from (
                     select count(mg.ID_PIATTO) as piatti 
                     from menu_giornaliero_primo mg
                     where mg.ID_MENU_SETTIMANALE=ID_MENU_SETTIMANALE
			         group by mg.GIORNO
                   ) as t
              /*prendo i giorni che hanno almeno due scelte*/
			  where piatti>=2
             /*i giorni in cui ci sono due scelte devono essere almeno 6*/
             )<6 THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuSettimanale: MENU_GIORNALIERO_PRIMO NON CONTIENE ABBASTANZA PIATTI';
         END IF;

         /*secondo*/ 
         IF (select count(*)
             /*per ogni menu giornaliero conto i piatti assegnati al menu*/ 
             from (
                     select count(mg.ID_PIATTO) as piatti 
                     from menu_giornaliero_secondo mg
                     where mg.ID_MENU_SETTIMANALE=ID_MENU_SETTIMANALE
			         group by mg.GIORNO
                   ) as t
              /*prendo i giorni che hanno almeno due scelte*/
			  where piatti>=2
             /*i giorni in cui ci sono due scelte devono essere almeno 6*/
             )<6 THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuSettimanale: MENU_GIORNALIERO_SECONDO NON CONTIENE ABBASTANZA PIATTI';
         END IF;

         /*contorno*/ 
         IF (select count(*)
             /*per ogni menu giornaliero conto i piatti assegnati al menu*/ 
             from (
                     select count(mg.ID_PIATTO) as piatti 
                     from menu_giornaliero_contorno mg
                     where mg.ID_MENU_SETTIMANALE=ID_MENU_SETTIMANALE
			         group by mg.GIORNO
                   ) as t
              /*prendo i giorni che hanno almeno due scelte*/
			  where piatti>=2
             /*i giorni in cui ci sono due scelte devono essere almeno 6*/
             )<6 THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuSettimanale: MENU_GIORNALIERO_CONTORNO NON CONTIENE ABBASTANZA PIATTI';
         END IF;




         /*dessert*/ 
         IF (select count(*)
             /*per ogni menu giornaliero conto i piatti assegnati al menu*/ 
             from (
                     select count(mg.ID_PIATTO) as piatti 
                     from menu_giornaliero_dessert mg
                     where mg.ID_MENU_SETTIMANALE=ID_MENU_SETTIMANALE
			         group by mg.GIORNO
                   ) as t
              /*prendo i giorni che hanno almeno due scelte*/
			  where piatti>=2
             /*i giorni in cui ci sono due scelte devono essere almeno 6*/
             )<6 THEN 
            signal sqlstate '45000' set message_text = 'ErrorMenuSettimanale: MENU_GIORNALIERO_DESSERT NON CONTIENE ABBASTANZA PIATTI';
         END IF;


END//
DELIMITER ;
/*prova del vincolo
UPDATE menu_settimanale SET ATTIVO=TRUE
WHERE ID_MENU_SETTIMANALE=1;

*/
/*stored procedure per vincolo in insert e update*/

DELIMITER //
DROP PROCEDURE IF EXISTS procedure_MenuSettimanale_InsertUpdate//

CREATE DEFINER=magna_tron_user@localhost 
PROCEDURE procedure_MenuSettimanale_InsertUpdate(
IN ID_MENU_SETTIMANALE INTEGER,
IN ATTIVO BOOLEAN,													
IN ID_CUOCO INTEGER)
READS SQL DATA
BEGIN
  /*controllo che ID_CUOCO sia un cuoco*/ 
  CALL  procedure_Chk_Persona_Cuoco(ID_CUOCO);
  /*
  controllo che menu settimanale abbia due scelte per ogni portata
  in tutti i giorni prima di attivarlo 
*/ 
  IF (ATTIVO=TRUE)
   THEN 
       CALL  procedure_Chk_MenuSettimanale_activation(ID_MENU_SETTIMANALE);         
   END IF; 
       

END//
DELIMITER ;


/*applico vincoli a update*/
delimiter //
DROP TRIGGER IF EXISTS Chk_MenuSettimanale_update//
create trigger Chk_MenuSettimanale_update BEFORE UPDATE  ON menu_settimanale
FOR EACH ROW
BEGIN

CALL  procedure_MenuSettimanale_InsertUpdate(NEW.ID_MENU_SETTIMANALE,NEW.ATTIVO,NEW.ID_CUOCO);

END;//
delimiter ;


/*applico vincoli a  insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_MenuSettimanale_insert//
create trigger Chk_MenuSettimanale_insert BEFORE INSERT  ON menu_settimanale
FOR EACH ROW
BEGIN

CALL  procedure_MenuSettimanale_InsertUpdate(NEW.ID_MENU_SETTIMANALE,NEW.ATTIVO,NEW.ID_CUOCO);

END;//
delimiter ;





/*insert MENU_SETTIMANALE*/
INSERT INTO menu_settimanale (DATA_INIZIO,ATTIVO,ID_CUOCO) 
              VALUES('2016-11-23',FALSE,2);

INSERT INTO menu_settimanale (DATA_INIZIO,ATTIVO,ID_CUOCO) 
              VALUES('2016-11-10',FALSE,2);




COMMIT;
