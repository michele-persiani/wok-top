package it.unibo.msrehab.model.weekendInRome;

import java.util.Collections;

/**
 * This class represents the PDDL action go-running
 * @author Margherita Donnici
 *
 */

public class GoRunning extends Action{
	
/*	PDDL string:   	(go-running ?p ?hour1 ?hour2 ?start-breakfast ?end-breakfast) */
	
	String place;
	String startTime;
	String endTime;
	
	public GoRunning(String place, String startTime, String endTime) {
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
           (gone-running) */
		
		String pddlEffect1 = "(not (at " + place + " " + startTime + "))";
		String pddlEffect2 = "(at " + place + " " + endTime + ")";
		String pddlEffect3 = "(not (future " + endTime +"))";
		String pddlEffect4 = "(gone-running)";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3, pddlEffect4);
	}

	@Override
	String getHintString() {
		String hint = "Prova ad andare a correre adesso";
		return hint;
	}

}