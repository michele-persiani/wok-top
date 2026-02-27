package it.unibo.msrehab.model.weekendInRome;

import java.util.Collections;

/**
 * This class represents the PDDL action return-home
 * @author Margherita Donnici
 *
 */

public class ReturnHome extends Action{
	
/*	PDDL string:   	(return-home ?src ?return-departure ?return-middle-hour ?return-arrival) */
	
	String place;
	String returnDeparture;
	String returnMiddleHour;
	String returnArrival;
	
	public ReturnHome(String place, String returnDeparture, String returnMiddleHour, String returnArrival) {
		super();
		this.place = place;
		this.returnDeparture = returnDeparture;
		this.returnMiddleHour = returnMiddleHour;
		this.returnArrival = returnArrival;
	}

	@Override
	void generateEffects() {
		/* PDDL effects:
		 * (not (at ?src ?return-departure))
            (not(future ?return-departure))
            (not(future ?return-middle-hour))
            (not(future ?return-arrival))
            (at-bologna) */
		
		String pddlEffect1 = "(not (at " + place + " " + returnDeparture + "))";
		String pddlEffect2 = "(not (future " + returnMiddleHour +"))";
		String pddlEffect3 = "(not (future " + returnDeparture +"))";
		String pddlEffect4 = "(not (future " + returnArrival +"))";
		String pddlEffect5 = "(at-bologna)";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3, pddlEffect4, pddlEffect5);
	}

	@Override
	String getHintString() {
		String hint = "Prova a prendere il treno del ritorno ora!";
		return hint;
	}

}