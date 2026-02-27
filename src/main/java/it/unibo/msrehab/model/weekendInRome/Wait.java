/**
 * 
 */
package it.unibo.msrehab.model.weekendInRome;

import java.util.Collections;

/**
 * This class represents the action wait
 * @author Margherita Donnici
 *
 */
public class Wait extends Action {

	/*	PDDL String:
	(wait(?p - place ?start-time ?end-time - time)*/
	
	String place;
	String startTime;
	String endTime;
	
	
	
	public Wait(String place, String startTime, String endTime) {
		super();
		this.place = place;
		this.startTime = startTime;
		this.endTime = endTime;
	}



	@Override
	void generateEffects() {
		/*	PDDL effects:
            (at ?p ?end-time)
            (not (at ?p ?start-time))
            (not (future ?end-time)) */
		
		String pddlEffect1 = "(not (future " + endTime + "))";
		String pddlEffect2 = "(not (at " + place + " " + startTime + "))";
		String pddlEffect3 = "(at " + place + " " + endTime + ")";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3);
	}



	@Override
	String getHintString() {
		String hint = "Prova ad aspettare un'ora";
		return hint;
	}

}