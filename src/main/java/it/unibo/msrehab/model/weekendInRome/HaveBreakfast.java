package it.unibo.msrehab.model.weekendInRome;

import java.util.Collections;

/**
 * This class represents the PDDL action have breakfast
 * @author Margherita Donnici
 *
 */

public class HaveBreakfast extends Action{
	
/*	PDDL string:   	(have-breakfast ?p ?hour1 ?hour2 ?start-breakfast ?end-breakfast) */
	
	String place;
	String startTime; //hour1
	String endTime; //hour2
	
	public HaveBreakfast(String place, String startTime, String endTime) {
		super();
		this.place = place;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	void generateEffects() {
		/* PDDL effects:
          (not (at ?p ?hour1))
          (not (future ?hour2))
          (at ?p ?hour2)
          (had-breakfast)*/
		
		String pddlEffect1 = "(not (at " + place + " " + startTime + "))";
		String pddlEffect2 = "(at " + place + " " + endTime + ")";
		String pddlEffect3 = "(had-breakfast)";
		String pddlEffect4 = "(not (future " + endTime +")";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3, pddlEffect4);
	}

	@Override
	String getHintString() {
		String hint = "Prova a fare colazione adesso";
		return hint;
	}

}