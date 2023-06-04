package com.ensah.bo;


/**
 * Represente un cadre qui travaille à l'administration
 * 
 * @author T. BOUDAA
 *
 */


public class CadreAdministrateur extends Utilisateur {

	private String grade;

	public CadreAdministrateur(Long idUtilisateur, String cin, String email, String nom, String nomArabe, String photo, String prenom, String prenomArabe, String telephone) {
		super(idUtilisateur, cin, email, nom, nomArabe, photo, prenom, prenomArabe, telephone);
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}