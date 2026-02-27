/**
 * 
 */
package it.unibo.msrehab.model.weekendInRome;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the action book-train-round-trip
 * @author Margherita Donnici
 *
 */
public class BookTrainRoundTrip extends Action {

/*	PDDL String:
	(book-train-round-trip ?station outward-departure ?outward-middle-hour ?outward-arrival ?return-departure ?return-middle-hour ?return-arrival)*/
	
	String station;
	
	String outwardDeparture;
	String outwardMiddleHour;
	String outwardArrival;
	
	String returnDeparture;
	String returnMiddleHour;
	String returnArrival;
	
	public BookTrainRoundTrip(	String station, String outwardDeparture, String outwardMiddleHour, String outwardArrival, 
			String returnDeparture, String returnMiddleHour, String returnArrival) {
		super();
		this.station = station;
		this.outwardDeparture = outwardDeparture;
		this.outwardMiddleHour = outwardMiddleHour;
		this.outwardArrival = outwardArrival;
		this.returnDeparture = returnDeparture;
		this.returnMiddleHour = returnMiddleHour;
		this.returnArrival = returnArrival;
	}
	
	@Override
	void generateEffects() {
		
		/* PDDL effects:
        (not (at-bologna))
        (not(future ?outward-departure))
        (not(future ?outward-middle-hour))
        (not(future ?outward-arrival))
        (at ?station ?outward-arrival)
        (visited ?station)
        (booked-return-train ?station ?return-departure ?return-arrival)
        (done-train-booking) */
		
		String pddlEffect1 = "(not (at-bologna))";
		String pddlEffect2 = "(not (future " + outwardDeparture + "))";
		String pddlEffect3 = "(not (future " + outwardMiddleHour + "))";
		String pddlEffect4 = "(not (future " + outwardArrival + "))";
		String pddlEffect5 = "(at " + station + " " + outwardArrival + ")";
		String pddlEffect6 = "(visited " + station + ")";
		String pddlEffect7 = "(booked-return-train " + station + " " + returnDeparture + " " + returnArrival + ")";
		String pddlEffect8 = "(done-train-booking)";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2, pddlEffect3, pddlEffect4, pddlEffect5, pddlEffect6, pddlEffect7, pddlEffect8);
		
	}

	@Override
	public String toString() {
		return "BookTrainRoundTrip [station=" + station + ", outwardDeparture=" + outwardDeparture
				+ ", outwardMiddleHour=" + outwardMiddleHour + ", outwardArrival=" + outwardArrival
				+ ", returnDeparture=" + returnDeparture + ", returnMiddleHour=" + returnMiddleHour + ", returnArrival="
				+ returnArrival + "]";
	}

	@Override
	String getHintString() {
		String formattedStation = station.substring(0,1).toUpperCase() + station.substring(1).toLowerCase();
		String hint = "Prova a prenotare il treno  di andata che parte da " + formattedStation + " alle " + outwardDeparture.split("r")[1];
		return hint;
	}
	
	
	
	

}
