package it.unibo.msrehab.model.weekendInRome;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the PDDL actions travel-by-foot and travel-by-bus
 * (They have the same parameters and effects)
 * @author Margherita Donnici
 *
 */

public class Travel extends Action{
	
/*	PDDL strings:   	
	(travel-by-foot ?src ?dst ?start-time ?end-time)
	(travel-by-bus ?src ?dst ?start-time ?end-time) */
	
	String startTime;
	String endTime;
	String source;
	String destination;

	public Travel(String startTime, String endTime, String source, String destination) {
		super();
		this.actionHead = "travel-by-foot";
		this.startTime = startTime;
		this.endTime = endTime;
		this.source = source;
		this.destination = destination;
	}

	@Override
	void generateEffects() {
		/* PDDL effects:
		(not (future ?start-time))
		(not (future ?end-time))
		(not (at ?src ?start-time))
		(at ?dst ?end-time)
		(visited ?dst) */
		
		String pddlEffect1 = "(not (future " + startTime + "))";
		String pddlEffect2 = "(not (future " + endTime + "))";
		String pddlEffect3 = "(not (at " + source + " " + startTime + "))";
		String pddlEffect4 = "(at " + destination + " " + endTime + ")";
		String pddlEffect5 = "(visited " + destination+ ")";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3, pddlEffect4, pddlEffect5);
	}

	@Override
	String getHintString() {
		String formattedDst;
		String[] splitDest = destination.split("-");
		if (splitDest.length > 1) {
			formattedDst = splitDest[0].substring(0,1).toUpperCase() + splitDest[0].substring(1).toLowerCase() + " " + splitDest[1].substring(0,1).toUpperCase() + splitDest[1].substring(1).toLowerCase();
		} else {
			formattedDst = destination.substring(0,1).toUpperCase() + destination.substring(1).toLowerCase();
		}
		String hint = "Prova ad andare qui: " + formattedDst;
		return hint;
	}

}
