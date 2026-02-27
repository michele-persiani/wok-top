package it.unibo.msrehab.model.weekendInRome;

import java.util.Collections;

/**
 * This class represents the PDDL actions sleep and sleepShort
 * @author Margherita Donnici
 *
 */

public class Sleep extends Action{
	
/*	PDDL string:   	(sleep ?p ?hour1 ?hour2 ?hour3 ?hour4 ?hour5 ?hour6 ?hour7 ?hour8 ?hour9 ) */
	
	String place;
	String startTime;
	String endTime;
	int startTimeInt;
	int endTimeInt;

	public Sleep(String place, String startTime, String endTime) {
		super();
		this.place = place;
		// I only need start and end time, middle hours can be obtained programmatically
		this.startTime = startTime;
		this.endTime = endTime;
		startTimeInt = Integer.parseInt(startTime.split("r")[1]); // startTime will be in format "hourX" where X is the int
		endTimeInt = Integer.parseInt(endTime.split("r")[1]);
	}

	@Override
	void generateEffects() {
		/* PDDL effects:
	      (not (at ?p ?hour1))
          (not (future ?hour2))
          (not (future ?hour3))
          (not (future ?hour4))
          (not (future ?hour5))
          (not (future ?hour6))
          (not (future ?hour7))
          (not (future ?hour8))
          (not (future ?hour9))
          (at ?p ?hour9)
          (slept) */
		
		String pddlEffect1 = "(not (at " + place + " " + startTime + "))";
		String pddlEffect2 = "(at " + place + " " + endTime + ")";
		String pddlEffect3 = "(slept)";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3);
		for (int i=startTimeInt+1; i<=endTimeInt; i++) {
			String effectString = "(not (future hour" + String.valueOf(i) + "))";
			this.effects.add(effectString);
		}
	}

	@Override
	String getHintString() {
		int numberOfHours = endTimeInt - startTimeInt;
		String hint = "Prova a dormire " + String.valueOf(numberOfHours) + " ore";
		return hint;
	}

}