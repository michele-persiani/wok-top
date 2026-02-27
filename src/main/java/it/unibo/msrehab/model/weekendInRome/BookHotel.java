package it.unibo.msrehab.model.weekendInRome;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the PDDL action book-hotel
 * @author Margherita Donnici
 *
 */

public class BookHotel extends Action {
	
	/*	PDDL string:   	(book-hotel ?p) */
	
	String place;
	
	public BookHotel(String place) {
		super();
		this.place = place;
		toString();
	}


	@Override
	void generateEffects() {
		/* PDDL effects:
		   (done-hotel-booking)
           (booked-hotel ?p)) */
		
		String pddlEffect1 = "(done-hotel-booking)";
		String pddlEffect2 = "(booked-hotel " + place +")";
		Collections.addAll(this.effects, pddlEffect1, pddlEffect2);
	}


	@Override
	public String toString() {
		return "BookHotel [place=" + place + "]";
	}


	@Override
	String getHintString() {
		String formattedPlace = place.substring(0,1).toUpperCase() + place.substring(1).toLowerCase();
		String hint = "Prova a prenotare l'albergo " + formattedPlace;
		return hint;
	}
	

}
