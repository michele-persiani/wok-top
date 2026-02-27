package it.unibo.msrehab.model.weekendInRome;

import java.util.Collections;

/**
 * This class represents the PDDL action do-activity
 * @author Margherita Donnici
 *
 */

public class DoActivity extends Action{
	
/*	PDDL string:   	(do-activity ?p ?hour1 ?hour2 ?hour3 ?opening-time ?closing-time) */
	
	String place;
	String startTime;
	String middleHour;
	String endTime;

	
	public DoActivity(String place, String startTime, String middleHour, String endTime) {
		super();
		this.place = place;
		this.startTime = startTime;
		this.middleHour = middleHour;
		this.endTime = endTime;
	}



	@Override
	void generateEffects() {
		/* PDDL effects:
          (at ?p ?hour3)
          (not (at ?p ?hour1))
          (done-activity-timed ?p ?hour1)
          (done-activity ?p)
          (not (future ?hour1))
          (not (future ?hour2))
          (not (future ?hour3)) */
		
		String pddlEffect1 = "(not (at " + place + " " + startTime + "))";
		String pddlEffect2 = "(at " + place + " " + endTime + ")";
		String pddlEffect3 = "(done-activity " + place + ")";
		String pddlEffect4 = "(not (future " + startTime +"))";
		String pddlEffect5 = "(not (future " + middleHour +"))";
		String pddlEffect6 = "(not (future " + endTime +"))";
		String pddlEffect7 = "(done-activity-timed " + place + " " + startTime + ")";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3, pddlEffect4, pddlEffect5, pddlEffect6, pddlEffect7);
	}



	@Override
	String getHintString() {
		String hint = "Prova a fare l'attività disponibile nel luogo dove sei";
		return hint;
	}

}