/* ordine:2*/
START TRANSACTION;
/*
tabella che contiene i piatti della mensa
il campo portata indica il tipo di portata di cui fa parte il piatto:
-P: primo
-S: secondo
-C: contorno
-D:dessert
*/
DROP TABLE IF EXISTS `piatto`;

CREATE TABLE IF NOT EXISTS `piatto` (
  `ID_PIATTO` int NOT NULL AUTO_INCREMENT,
  `NOME` varchar(25) NOT NULL,
  `DESCRIZIONE` TEXT NOT NULL, 
  `PREZZO` FLOAT NOT NULL,
  `PORTATA` char(1) NOT NULL,
  PRIMARY KEY (`ID_PIATTO`)
); 

/*trigger di sulla tabella PIATTO*/

/*controllo validità campo RUOLO per insert e update*/
/*
vincolo comune
*/
DELIMITER //
DROP PROCEDURE IF EXISTS procedure_Chk_Piatto_Portata//

CREATE DEFINER=magna_tron_user@localhost PROCEDURE procedure_Chk_Piatto_Portata(IN portata CHAR(1))
READS SQL DATA
BEGIN
         declare msg varchar(128);
         IF( portata!='P' AND portata!='S' AND portata!='C' AND portata!='D') THEN 
            set msg = concat('ErrorPiattoPortata: PORTATA NON CORRETTA');
            signal sqlstate '45000' set message_text = msg;
        END IF;  
    

END//
DELIMITER ;

/*applico vincolo a RUOLO per update */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Portata_update//
create trigger Chk_Piatto_Portata_update BEFORE UPDATE  ON piatto
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Portata(NEW.PORTATA);

END;//
delimiter ;


/*applico vincolo a RUOLO per insert */
delimiter //
DROP TRIGGER IF EXISTS Chk_Piatto_Portata__insert//
create trigger Chk_Piatto_Portata_insert BEFORE INSERT  ON piatto
FOR EACH ROW
BEGIN

CALL  procedure_Chk_Piatto_Portata(NEW.PORTATA);

END;//
delimiter ;


/*insert PIATTO*/
/*primi*/
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('pasta e fagioli','pasta con fagioli freschi.',4.30,'P');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('pasta allo scoglio','pasta e vongole.',5.30,'P');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('pasta al pomodoro','pasta con sugo di pomodoro.',3.60,'P');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('pasta al pesto','pasta con pesto genovese.',4.00,'P');

INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('insalata greca','insalata con pomodori, cipolla e formaggio feta',4.00,'P');

INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('tortellini','tortellini di carne tradizionali',4.00,'P');

INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('tortellini di zucca','tortellini con ripieno di zucca',4.00,'P');


/*secondi*/
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('bistecca di pollo','bistecca di pollo al limone.',3.00,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('sogliola','sogliola impanata.',4.90,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('bistecca di maiale','bistecca di maiale ai ferri',3.00,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('tonno','tonno ai ferri',5.00,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('prosciutto crudo','porzione di prosciutto crudo',3.00,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('prosciutto cotto','porzione di prosciutto cotto',3.00,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('insalata di patate','patate con fagiolini, pomodori e salsa',3.00,'S');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('pollo arrosto','pollo arrosto',3.50,'S');

INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('arrosto di maiale','arrosto di maiale',3.50,'S');

INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('arrosto di manzo','arrosto di manzo',3.50,'S');

INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('polpette','polpette',4.00,'S');


/*contorni*/
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('verdure miste','insalata, carote, pomodori e fagioli freschi.',2.00,'C');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('patatine fritte','patatine fritte con olio.',2.00,'C');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('broccoli','broccoli cotti al vapore.',2.50,'C');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('fagiolini','fagiolini cotti al vapore.',2.40,'C');
/*dessert*/
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('budino','budino al cioccolato, crema e neutro.',2.00,'D');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('frutto','frutto a scelta tra arancia, mela e pera.',2.00,'D');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('torta','torta al cioccolato.',2.00,'D');
INSERT INTO piatto (NOME,DESCRIZIONE,PREZZO,PORTATA) 
              VALUES('crostata','crostata di marmellata.',2.00,'D');
COMMIT;
