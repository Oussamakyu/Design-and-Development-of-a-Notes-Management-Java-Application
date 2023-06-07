package com.ensah.bo;

/**
 * Represente un élément d'un module
 * 
 * @author T. BOUDAA
 *
 */

public class Element {

	private Long idMatiere;

	private String nom;

	private String code;

	private double currentCoefficient;

	private Module module;

	public Element(){}

	public Element(Long idMatiere, String nom, String code, double currentCoefficient, Module module) {
		this.idMatiere = idMatiere;
		this.nom = nom;
		this.code = code;
		this.currentCoefficient = currentCoefficient;
		this.module = module;
	}

	public Long getIdMatiere() {
		return idMatiere;
	}

	public void setIdMatiere(Long idMatiere) {
		this.idMatiere = idMatiere;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public double getCurrentCoefficient() {
		return currentCoefficient;
	}

	public void setCurrentCoefficient(double currentCoefficient) {
		this.currentCoefficient = currentCoefficient;
	}

}