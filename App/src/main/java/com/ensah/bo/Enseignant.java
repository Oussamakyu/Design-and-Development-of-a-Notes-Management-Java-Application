package com.ensah.bo;


/**
 * Represente un enseignant.
 * 
 * Un enseignant est un cas spéciale de l'Utilisateur
 * 
 * @author T. BOUDAA
 *
 */


public class Enseignant extends Utilisateur {


	
	private String specialite;

	public Enseignant(Long idUtilisateur, String cin, String email, String nom, String nomArabe, String photo, String prenom, String prenomArabe, String telephone) {
		super(idUtilisateur, cin, email, nom, nomArabe, photo, prenom, prenomArabe, telephone);
	}


	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}





}