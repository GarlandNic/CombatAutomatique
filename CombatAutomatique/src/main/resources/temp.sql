faire une modif dans la base de donnée
ouvrir une invite de commande et taper "mysql -u root -p"
mettre le mot de passe ("root")
puis taper "use azurhyan;"
puis "ALTER TABLE combos MODIFY COLUMN ELEMENT enum('NORMAL','FEU','ACIDE','FROID','ELECTRICITE','NECROTIQUE','BLANCHE') NOT NULL DEFAULT 'NORMAL';"
"exit"
"exit"

mysql -u root -p

use azurhyan;

ALTER TABLE combos
MODIFY COLUMN ELEMENT enum('NORMAL','FEU','ACIDE','FROID','ELECTRICITE','NECROTIQUE','BLANCHE') NOT NULL DEFAULT 'NORMAL';

commit;
