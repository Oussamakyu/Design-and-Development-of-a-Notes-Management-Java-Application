package com.ensah.core.bo;


/**
 * Represente un cadre qui travaille à l'administration
 * 
 * @author T. BOUDAA
 *
 */


public class CadreAdministrateur extends Utilisateur {

	private String grade;

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}