
DROP DATABASE IF EXISTS azurhyan;

create database azurhyan;
use azurhyan;

create table personnages(
 ID integer PRIMARY KEY,
 NOM varchar(247) NOT NULL,
 PARTIE varchar(247),
 JOUEUR varchar(247),
 CACHE varchar(247),
 
);

create table actions(
);

commit;


