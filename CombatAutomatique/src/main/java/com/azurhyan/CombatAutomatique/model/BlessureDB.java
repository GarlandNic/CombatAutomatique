package com.azurhyan.CombatAutomatique.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "blessures")
public class BlessureDB {
	
//	create table blessures(
//			 ID integer PRIMARY KEY,
//			 PERSONNAGE integer,
//			 DEMINIVEAU integer unsigned,
//			 PTDECHOC integer unsigned,
//			 PARTIETOUCHEE varchar(255),
//			 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
//			);

}
