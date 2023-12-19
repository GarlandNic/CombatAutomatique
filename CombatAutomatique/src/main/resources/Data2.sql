
use azurhyan;

drop table etat;

create table etat(
 PERSONNAGE integer NOT NULL PRIMARY KEY,
 DEDEF integer,
 TURNORDER integer NOT NULL,
 INCAPACITE integer DEFAULT 0,
 UNIQUE (PERSONNAGE),
 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (PERSOID) ON DELETE CASCADE ON UPDATE CASCADE
);

commit;


