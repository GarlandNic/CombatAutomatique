package com.azurhyan.CombatAutomatique.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "combos")
public class ComboDB {
//	create table combos(
//	 ID integer PRIMARY KEY,
//	 PERSONNAGE integer,
//	 NOM varchar(255),
//	 INIT integer unsigned NOT NULL DEFAULT 15,
//	 CAC boolean NOT NULL DEFAULT TRUE,
//	 TOUCHER integer unsigned NOT NULL DEFAULT 15,
//	 PRDENNEMIE integer NOT NULL DEFAULT 0,
//	 FORCE integer unsigned NOT NULL DEFAULT 15,
//	 IBATT integer unsigned NOT NULL DEFAULT 15,
//	 DEFENSE integer unsigned NOT NULL DEFAULT 15,
//	 ESQUIVE integer unsigned NOT NULL DEFAULT 15,
//	 PARADE integer NOT NULL DEFAULT 0,
//	 ENDBOUCLIER integer unsigned NOT NULL DEFAULT 15,
//	 ENDURANCE integer unsigned NOT NULL DEFAULT 15,
//	 IBDEF integer unsigned NOT NULL DEFAULT 15,
//	 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
//	);
}
