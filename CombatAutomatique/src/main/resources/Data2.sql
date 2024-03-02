
use azurhyan;

create table handicaps(
 HANDID integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 REFACTION integer DEFAULT 0,
 PERSONNAGE integer NOT NULL,
 DEMINOMBRE integer unsigned NOT NULL,
 TYPEHAND enum('FATIGUE','MOBILITE','SENS') NOT NULL DEFAULT 'FATIGUE',
 NOMHAND varchar(255),
 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (PERSOID) ON DELETE CASCADE ON UPDATE CASCADE
);

alter table personnages
drop column HFATIGUE,
drop column HMOBILITE,
drop column HSENS;

commit;


