package it.unibo.msrehab.model.weekendInRome;

import java.util.ArrayList;

//This abstract class represents a PDDL Action for the Weekend in Rome exercise
//Creator: Margherita Donnici

abstract class Action {

	String actionHead;
	ArrayList<String> effects;

	public Action() {
		super();
		this.effects = new ArrayList<String>();
	}

	abstract void generateEffects();

	public ArrayList<String> getEffects() {
		return effects;
	}

	abstract String getHintString();

}
