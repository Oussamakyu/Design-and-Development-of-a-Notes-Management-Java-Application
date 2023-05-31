package com.ensah.bo;


import java.sql.Date;
import java.util.List;


/**
 * Represente un Etudiant.
 * 
 * Un enseignant est un cas spéciale de l'Etudiant
 * 
 * @author T. BOUDAA
 *
 */


public class Etudiant extends Utilisateur {

	private String cne;

	private Date dateNaissance;

	private List<InscriptionAnnuelle> inscriptions;

	public Etudiant() {
		super();
	}


	public Etudiant(Long idUtilisateur, String cin, String email, String nom, String nomArabe, String photo, String prenom, String prenomArabe, String telephone) {
		super(idUtilisateur, cin, email, nom, nomArabe, photo, prenom, prenomArabe, telephone);
	}


	public String getCne() {
		return cne;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public List<InscriptionAnnuelle> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<InscriptionAnnuelle> inscriptions) {
		this.inscriptions = inscriptions;
	}

	@Override
	public String toString() {
		return "Etudiant [cne=" + cne + ", dateNaissance=" + dateNaissance + ", inscriptions=" + inscriptions + "]";
	}


	

}