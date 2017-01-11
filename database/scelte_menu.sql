/* ordine:9*/
START TRANSACTION;
/*
tabella che contiene i piatti scelti dai dipendenti 
*/

DROP TABLE IF EXISTS `scelte_menu`;

CREATE TABLE IF NOT EXISTS `scelte_menu` (
  `ID_MENU_SETTIMANALE` int NOT NULL,
  `GIORNO` INT NOT NULL,
  `ID_PIATTO` INT NOT NULL,
  `MATRICOLA` INT NOT NULL,

   FOREIGN KEY fk_menu_giornaliero(`ID_MENU_SETTIMANALE`,`GIORNO`) 
   REFERENCES menu_giornaliero(`ID_MENU_SETTIMANALE`,`GIORNO`)
   ON DELETE CASCADE ON UPDATE CASCADE,
   
   FOREIGN KEY fk_persona(`MATRICOLA`) 
   REFERENCES persona(`MATRICOLA`)
   ON DELETE CASCADE ON UPDATE CASCADE,

   FOREIGN KEY fk_piatto(`ID_PIATTO`) 
   REFERENCES piatto(`ID_PIATTO`)
   ON DELETE CASCADE ON UPDATE CASCADE,

   PRIMARY KEY (`ID_MENU_SETTIMANALE`,`GIORNO`,`ID_PIATTO`,`MATRICOLA`)
   
);

/*trigger SCELTE_MENU */
/* il menu settimanale deve essere attivo*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_ScelteMenu_MenuSettimanale_attivo//

CREATE DEFINER=magna_tron_user@localhost 
PROCEDURE procedure_Chk_ScelteMenu_MenuSettimanale_attivo(IN ID_MENU_SETTIMANALE INTEGER)
READS SQL DATA
BEGIN
         IF (select m.ATTIVO 
             from menu_settimanale m 
             where m.ID_MENU_SETTIMANALE=ID_MENU_SETTIMANALE)!=TRUE THEN 
            signal sqlstate '45000' set message_text = 'ErrorScelteMenu: ID_MENU_SETTIMANALE DEVE ESSERE ATTIVO';
        END IF;  
END//
DELIMITER ;





COMMIT;


