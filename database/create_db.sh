#!/bin/bash
susername=magna_tron_user
spassword=magna_tron
#delete tables
echo "drop tables \n"
./dropTables.sh $susername $spassword magna_tron_db
#rebuild tables
echo "rebuild tables\n"
echo "rebuild table: persona \n"
mysql -D magna_tron_db -u$susername -p$spassword < persona.sql
echo "rebuild table: piatto \n"
mysql -D magna_tron_db -u$susername -p$spassword < piatto.sql
echo "rebuild table: menu_settimanale \n"
mysql -D magna_tron_db -u$susername -p$spassword < menu_settimanale.sql
echo "rebuild table: menu_giornaliero \n"
mysql -D magna_tron_db -u$susername -p$spassword < menu_giornaliero.sql
echo "rebuild table: menu_giornaliero_primo \n"
mysql -D magna_tron_db -u$susername -p$spassword < menu_giornaliero_primo.sql
echo "rebuild table: menu_giornaliero_secondo \n"
mysql -D magna_tron_db -u$susername -p$spassword < menu_giornaliero_secondo.sql
echo "rebuild table: menu_giornaliero_contorno \n"
mysql -D magna_tron_db -u$susername -p$spassword < menu_giornaliero_contorno.sql
echo "rebuild table: menu_giornaliero_dessert \n"
mysql -D magna_tron_db -u$susername -p$spassword < menu_giornaliero_dessert.sql
echo "rebuild table: scelte_menu \n"
mysql -D magna_tron_db -u$susername -p$spassword < scelte_menu.sql
