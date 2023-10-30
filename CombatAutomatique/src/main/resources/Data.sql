
DROP DATABASE IF EXISTS azurhyan;

create database azurhyan;
use azurhyan;

create table personnages(
 ID integer PRIMARY KEY,
 NOM varchar(255) NOT NULL,
 PARTIE varchar(255) NOT NULL,
 JOUEUR varchar(255),
 VISIBLE boolean NOT NULL DEFAULT TRUE,
 CON integer unsigned NOT NULL DEFAULT 10,
 HFATIGUE integer unsigned NOT NULL DEFAULT 0,
 CCINFATIGABLE integer unsigned NOT NULL DEFAULT 0,
 HMOBILITE integer unsigned NOT NULL DEFAULT 0,
 CCINCOERCIBLE integer unsigned NOT NULL DEFAULT 0,
 HSENS integer unsigned NOT NULL DEFAULT 0,
 CCTOUJOURSPRET integer unsigned NOT NULL DEFAULT 0,
 CCCOMBATPLUSIEURS integer unsigned NOT NULL DEFAULT 0
);

create table blessures(
 ID integer PRIMARY KEY,
 PERSONNAGE integer,
 DEMINIVEAU integer unsigned,
 PTDECHOC integer unsigned,
 PARTIETOUCHEE varchar(255),
 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

create table combos(
 ID integer PRIMARY KEY,
 PERSONNAGE integer,
 NOM varchar(255),
 INIT integer unsigned NOT NULL DEFAULT 15,
 CAC boolean NOT NULL DEFAULT TRUE,
 TOUCHER integer unsigned NOT NULL DEFAULT 15,
 PRDENNEMIE integer NOT NULL DEFAULT 0,
 FORCE integer unsigned NOT NULL DEFAULT 15,
 IBATT integer unsigned NOT NULL DEFAULT 15,
 DEFENSE integer unsigned NOT NULL DEFAULT 15,
 ESQUIVE integer unsigned NOT NULL DEFAULT 15,
 PARADE integer NOT NULL DEFAULT 0,
 ENDBOUCLIER integer unsigned NOT NULL DEFAULT 15,
 ENDURANCE integer unsigned NOT NULL DEFAULT 15,
 IBDEF integer unsigned NOT NULL DEFAULT 15,
 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

create table actions(
 ID integer PRIMARY KEY,
 PARTIE varchar(255) NOT NULL,
 ACTEURID integer,
 ACTEURNOM varchar(255),
 ACTIONTIME datetime NOT NULL,
 DESCRIPTION varchar(1023)
);

commit;


