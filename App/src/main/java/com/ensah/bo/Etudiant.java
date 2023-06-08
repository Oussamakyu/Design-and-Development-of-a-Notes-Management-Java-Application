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

	public Utilisateur(Long idUtilisateur, String nom, String prenom, String cin, String email, String telephone, String nomArabe, String prenomArabe, String photo) {
		this.idUtilisateur = idUtilisateur;
		this.nom = nom;
		this.prenom = prenom;
		this.cin = cin;
		this.email = email;
		this.telephone = telephone;
		this.nomArabe = nomArabe;
		this.prenomArabe = prenomArabe;
		this.photo = photo;
	}


	public Utilisateur(Long idUtilisateur, String nom, String prenom) {
		this.idUtilisateur = idUtilisateur;
		this.nom = nom;
		this.prenom = prenom;
	}


	public Etudiant(long idUtilisateur, String cne, String nom, String prenom) {
		super(idUtilisateur,nom,prenom);
		this.cne= cne;}




	public Etudiant(Long idUtilisateur, String cin, String email, String nom, String nomArabe, String photo, String prenom, String prenomArabe, String telephone) {
		super(idUtilisateur, cin, email, nom, nomArabe, photo, prenom, prenomArabe, telephone);
	}

    public Etudiant(long idEtudiant) {
		setIdUtilisateur(idEtudiant);
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