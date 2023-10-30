
TODO list

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
	o (Voir combo) -> GET personnage.html
	o (Cacher/Révéler personnage) -> GET visibilite.html
	o (Créer un vouveau personnage) -> GET personnage.html
o visibilite.html
	o GET pour affichage
		o liste des persos à récupérer (PersosDto.persoList : List<PersonnageDB>)
			o persoService
				o persoRepository
	o (Enregistrer les modifications) -> POST/PUT (PersosDto.persoList : List<PersonnageDB>)
		o et retour à partie.html
	o (Retourner sans enregistrer) -> GET partie.html (sans enregistrer)
o personnage.html
	o GET pour affichage
		o touts les stats du perso (PersonnageDB)
	o (Mise à jour) -> POST/PUT (PersonnageDB)
		o et on recharge la page
	o (Lancer une attaque) -> GET attaque.html
	o (Retour) -> GET partie.html
o attaque.html
	o GET pour affichage
