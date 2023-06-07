package com.ensah.bo;

import java.util.List;


public class Module {

	private Long idModule;

	private String titre;

	private String code;


	private List<Element> elements;

	private Niveau niveau;

	public Module(){}

	public Module(Long idModule) {
		this.idModule = idModule;
	}

	public Module(Long idModule, String titre, String code, Niveau niveau) {
		this.idModule = idModule;
		this.titre = titre;
		this.code = code;
		this.niveau = niveau;
	}

	public Long getIdModule() {
		return idModule;
	}

	public void setIdModule(Long idModule) {
		this.idModule = idModule;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public Niveau getNiveau() {
		return niveau;
	}

	public void setNiveau(Niveau niveau) {
		this.niveau = niveau;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public void addElement(Element e){
		elements.add(e);
	}

	@Override
	public String toString() {
		return "Module{" +
				"idModule=" + idModule +
				", titre='" + titre + '\'' +
				", code='" + code + '\'' +
				", elements=" + elements +
				", niveau=" + niveau +
				'}';
	}
}