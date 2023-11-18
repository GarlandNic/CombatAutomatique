
TODO list

personnage.html > (Blessure) boutons pour enlever/ajouter des lignes
personnage.html > lignes de blessures
personnage.html > section combo
utiliser les redirect:/ ??









o objects de la base de donnée
- o class repository
- - o service

o partie.html
	o GET pour affichage
		o liste des actions à récupérer (ActionsDto.actionList : List<ActionDB>)
			o actionService
				o actionRepository
		o liste des perso visibles à récupérer (PersosVisiblesDto.persoList : List<PersoVisible>)
			o persoService
				o persoRepository
x	o (Voir combo) -> GET personnage.html
x	o (Cacher/Révéler personnage) -> GET visibilite.html
x	o (Créer un vouveau personnage) -> GET personnage.html
o visibilite.html
	o GET pour affichage
		o liste des persos à récupérer (PersosDto.persoList : List<PersonnageDB>)
X			o persoService
X				o persoRepository
	o (Enregistrer les modifications) -> POST/PUT (PersosDto.persoList : List<PersonnageDB>)
X		o persoService
X			o persoRepository
		o et retour à partie.html
	o (Retourner sans enregistrer) -> GET partie.html (sans enregistrer)
Xo personnage.html
X	o GET pour affichage
X		o touts les stats du perso (PersonnageDB)
X	o (Mise à jour) -> POST/PUT (PersonnageDB)
X		o et on recharge la page
X	o (Lancer une attaque) -> GET attaque.html
X	o (Retour) -> GET partie.html
X	o dupliquer un PNJ -> pour plus tard
Xo attaque.html
X	o GET pour affichage
X	o
X	o
	
	
