package com.azurhyan.CombatAutomatique.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Competence {
	
	Static List<String> compNom = [];
	
	String id;
	
	int AGI=9;
	int CHA=9;
	int CON=9;
	int ESP=9;
	int HAB=9;
	int INT=9;
	int PER=9;
		
		N°	Compétence	PdC	Sous-compétence
1	Adresse		Escamotage	Furtivité	Souplesse
			
2	Athlétisme		Course 	Equilibre 	Saut
			
3	Charme		Renseignement	Séduction	Sympathie
			
4	Chasse		Dressage	Pister	Trappe
			
5	Combat		Défense	Toucher	Réflexes
			
6	Eloquence		Langues	Persuasion	Récit
			
7	Exploration		Nourriture	Orientation	Survie
			
Spé :
8	Magie		Attaque	Soutien	Divers
			
9	Mental		Constance	Intimidation	Volonté
			
10	Métier		Art 	Artisanat	Manœuvre
			
Spé :
11	Recherche		Détection	Repérer	Sens 
			
12	Robustesse		Vigueur	Mouvement	Résistance
			
13	Sagacité		Apprentissage 	Finesse 	Stratégie 
			
14	Savoir		Lettres 	Sciences 	Sociétés
			
Spé :
15	Spectacle		Arts du cirque	Arts musicaux	Arts scéniques
			
Spé :
}
