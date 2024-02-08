
DROP DATABASE IF EXISTS azurhyan;

create database azurhyan;
use azurhyan;

create table personnages(
 PERSOID integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 NOM varchar(255) NOT NULL,
 PARTIE varchar(255) NOT NULL,
 JOUEUR varchar(255),
 VISIBLE boolean NOT NULL DEFAULT TRUE,
 CON integer unsigned NOT NULL DEFAULT 10,
 PDCCOMBAT integer unsigned NOT NULL DEFAULT 3,
 HFATIGUE integer unsigned NOT NULL DEFAULT 0,
 CCINFATIGABLE integer unsigned NOT NULL DEFAULT 0,
 HMOBILITE integer unsigned NOT NULL DEFAULT 0,
 CCINCOERCIBLE integer unsigned NOT NULL DEFAULT 0,
 HSENS integer unsigned NOT NULL DEFAULT 0,
 CCTOUJOURSPRET integer unsigned NOT NULL DEFAULT 0,
 CCCOMBATPLUSIEURS integer unsigned NOT NULL DEFAULT 0
);

create table blessures(
 BLESSID integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 REFACTION integer DEFAULT 0,
 PERSONNAGE integer,
 DEMINIVEAU integer unsigned,
 PTDECHOC integer unsigned,
 PARTIETOUCHEE varchar(255),
 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (PERSOID) ON DELETE CASCADE ON UPDATE CASCADE
);

create table combos(
 COMBOID integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 PERSONNAGE integer,
 NOM varchar(255),
 INIT integer NOT NULL DEFAULT 15,
 TYPEDGTS enum('NOR','CTD','PRF') DEFAULT 'NOR',
 GLOBAUX boolean DEFAULT FALSE,
 ELEMENT enum('NORMAL','FEU','ACIDE','FROID','ELECTRICITE','NECROTIQUE') NOT NULL DEFAULT 'NORMAL',
 CAC boolean DEFAULT TRUE,
 TOUCHER integer NOT NULL DEFAULT 15,
 PRDENNEMIE integer NOT NULL DEFAULT 0,
 FORCEATTAQUE integer NOT NULL DEFAULT 15,
 IBATT integer NOT NULL DEFAULT 15,
 DEFENSE integer NOT NULL DEFAULT 15,
 ESQUIVE integer NOT NULL DEFAULT 15,
 PARADE integer NOT NULL DEFAULT 0,
 BOUCLIER enum('Pas_de_bouclier','Dague','Targe','Bouclier','Grand_bouclier','Bouclier_tour') NOT NULL DEFAULT 'NORMAL',
 ENDBOUCLIER integer NOT NULL DEFAULT 15,
 ENDPERSO integer NOT NULL DEFAULT 15,
 IBDEF integer NOT NULL DEFAULT 15,
 ACTIF boolean NOT NULL DEFAULT true,
 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (PERSOID) ON DELETE CASCADE ON UPDATE CASCADE
);

create table actions(
 ACTIOID integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 REFATTAQUE integer default 0,
 PARTIE varchar(255) NOT NULL,
 ACTEURID integer,
 ACTEURNOM varchar(255),
 ACTEURDE integer,
 CIBLEID integer default 0,
 CIBLENOM varchar(255),
 CIBLEDE integer default 0,
 ACTIONTIME datetime NOT NULL,
 DESCRIPTION varchar(1023),
 FOREIGN KEY (ACTEURID) REFERENCES personnages (PERSOID) ON DELETE CASCADE ON UPDATE CASCADE
);

commit;


