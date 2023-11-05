package com.azurhyan.CombatAutomatique.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "blessures")
public class BlessureDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	int blessureId;
	
	@Column(name="PERSONNAGE")
	int persoId;
	
	int deminiveau;
	
	int ptDeChoc;
	
	String partieTouchee;
	
//	create table blessures(
//			 ID integer PRIMARY KEY,
//			 PERSONNAGE integer,
//			 DEMINIVEAU integer unsigned,
//			 PTDECHOC integer unsigned,
//			 PARTIETOUCHEE varchar(255),
//			 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
//			);

}
