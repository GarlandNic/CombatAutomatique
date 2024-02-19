
TODO list

  o bousculade et attaques spéciales (coup dans le bouclier, etc.)
  	- html : fieldset lors de l'attaque
  	- objet pour regrouper les flag d'attaque spéciale ? dans attaque Dto
  	- 

 

DONE :

 o afficher niveau de blessure dans les évènements
 o point de règle : dégâts perforants sur bouclier
 o Blessures sur armure
 o nom des combo en gras à dé-grassé (handicap aussi) 
 o page visibilité : rajouter voir combo
 o mémoriser les défenses & s'en reservir
 o afficher les défenses
 o reset les défenses
 o Effacer les évenements
 o Nouveau perso non-visible par défaut
 o bousculade automatique avec dgts ctd
 o bugs à corriger : quand on modifie déf et sauvegarde -> plante ; quand on sauvegarde un perso -> annule son état
 o bug à l'attaque duplicate entry PERSONNAGE
 o bug d'affichage : quand on donne un dédéf à un perso (on le passe de 0 à quelque chose), il faut raffraichir une fois de plus pour que ça s'affiche
 o tester attaque de groupe
 	o bug : parfois la défense (la même pour toutes les attaques ne se sauvegarde pas)
 	o bug de règle : les handicaps ne devrait pas compter pour les attaques du même groupe
 o sauvegarder avant d'attaquer
 o bug : je n'arrive pas à accéder à la fiche du 1er perso depuis visibilité. à cause des form imbriquées ?
 o bug attaque : à investiguer (sipahi attaque multiple, lepluspossible)
 o affichage bousculade
 o bouger des boutons : enregistrer combo : plus haut + enregistrer et retourner
 o numéro pour dupliquer -> A tester
 o blessures avec les perso (page principale : on veut voir comment ils sont blessés)
 o incapacité
 o test incapacité
 o combo a switcher
  o bug : dans visibilité, modif incapacité
  o ligne combo noirifiée automatiquement
  o tester modifs affichage bouclier & types de dégâts
  o ajouter bl/# au-dessus des carrées
  o modifier calcul combat : 
  	- dégâts globaux (+1 niv, répartit entre 6 parties du corps)
  o PP
  	X modif table action !!!!!!! temp.sql
  	X modif objet java actionDB
  	X nouvelle route : azurhyan/partie/annulerAttaque avec lastAttaque=refAttaque
  	X nouvelle route : azurhyan/partie/modifierAttaque avec lastAttaque=refAttaque
  	X ActionService : annulerAction :
  		annuler chaque blessure
  		o modifier H-
  		o supprimer blessure
  		o supprimer action
  	X modifier lancerAttaque pour que actionDB enregistre les bonnes infos (dé et cible et refattaque)
  		et blessureDB (refAction)
  o type de dégâts
  o séparer dégâts armure et bouclier
  o nouveau round => séparateur dans la liste des évènements
  o afficher marge Tch / marge bl (modif Degat.java pour les enregistrer)
  o couleur des évènements
  o supprimer perso / archive :
	- bouton Supprimer à droite de Dupliquer ssi joueur == PNJ
  	- à gauche : flèches pour archiver
  	- en bas : Archive, en dessous des boutons
  	- confirmation pour la suppression
  o page d'attaque : plus d'info sur les persos
  

  


  
