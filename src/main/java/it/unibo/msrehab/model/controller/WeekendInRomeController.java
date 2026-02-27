package it.unibo.msrehab.model.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.heuristics.relaxation.Heuristic;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.hsp.HSP;
import fr.uga.pddl4j.util.Plan;
import it.unibo.msrehab.model.weekendInRome.Goal;
import it.unibo.msrehab.model.weekendInRome.Problem;
import it.unibo.msrehab.model.weekendInRome.TrainTrip;

/*
*	This class is responsible for generating PDDL problems for the Weekend in Rome exercise
* 	Margherita Donnici
*/

public class WeekendInRomeController
{
	
	private static final String DOMAIN_FILE = "(define (domain weekend-in-rome)\n " + "\n " + "  (:requirements \n "
			+ "    :typing\n " + "    :strips\n " + "    :adl\n " + "    :equality\n " + "  )\n " + "\n "
			+ "  (:types\n " + "    place \n " + "    time\n " + "  )\n " + "\n " + "  (:predicates\n "
			+ "    ;Predicates for train and hotel booking\n "
			+ "    (at-bologna) ; true if I currently am in bologna\n " + "    ;;Available train choices\n "
			+ "    (round-trip ?station - place ?outward-departure ?return-departure - time)"
			+ "    (outward-train ?station - place ?outward-departure ?outward-arrival - time)\n "
			+ "    (return-train ?station - place ?return-departure ?return-arrival)\n " + "\n "
			+ "    (train-station ?dst - place) ;the place is a train station\n "
			+ "    (booked-return-train ?departure-place ?return-departure ?return-arrival) ;used to fix the return train\n "
			+ "\n "
			+ "    ;;Predicates to force the order of the booking actions (Book trains -> Book hotel / Book outward -> Book return -> Book hotel)\n "
			+ "    (done-outward-train-booking)\n "
			+ "    (done-train-booking); true if trains have already been booked\n "
			+ "    (done-hotel-booking); true if hotel has been booked\n " + "    (booked-hotel ?p)\n " + "\n "
			+ "    ;Predicates to model time\n " + "    (future ?time - time)\n "
			+ "    (consecutive ?hour1 ?hour2 - time)\n " + "    (at ?p - place ?time - time)\n " + "\n "
			+ "    ;Predicates to create the map\n " + "    (foot-path ?p1 ?p2 - place)\n "
			+ "    (bus-path ?p1 ?p2 - place)\n " + "    (can-sleep ?p - place)\n "
			+ "    (activity-available ?p - place ?hour - time)\n "
			+ "    (opening-hours ?p - place ?opening-time ?closing-time - time)\n "
			+ "    (breakfast ?start ?end - time)\n " + "\n " + "    ;Buses\n "
			+ "    (bus-scheduled ?src ?dst - place ?time - time) ;theres is a bus for dst passing at this time in this bus stop\n "
			+ "\n " + "    ;Predicates to keep track of goals\n " + "    (slept)\n " + "    (had-breakfast)\n "
			+ "    (done-activity-timed ?p - place ?hour1  - time)\n " + "    (done-activity ?p - place)\n "
			+ "    (visited ?p - place)\n " + "    (sleep-time ?time - time) ;hour at I which must go to sleep\n "
			+ "    (gone-running)\n " + "  )\n " + "\n " + "  ; ACTIONS\n " + "\n " + "  ; Book train round trip\n "
			+ "  ; Trip lasts 2hours\n " + "  (:action book-train-round-trip\n "
			+ "  :parameters (?p - place ?outward-departure ?outward-middle-hour ?outward-arrival ?return-departure ?return-middle-hour ?return-arrival - time)\n "
			+ "  :precondition (and\n " + "                  (at-bologna)\n "
			+ "                  (consecutive ?outward-departure ?outward-middle-hour)\n "
			+ "                  (consecutive ?outward-middle-hour ?outward-arrival)\n "
			+ "                  (consecutive ?return-departure ?return-middle-hour)\n "
			+ "                  (consecutive ?return-middle-hour ?return-arrival)\n "
			+ "                  (future ?outward-departure)\n " + "                  (future ?outward-middle-hour)\n "
			+ "                  (future ?outward-arrival)\n " + "                  (future ?return-departure)\n "
			+ "                  (future ?return-middle-hour)\n " + "                  (future ?return-arrival)\n "
			+ "                  (round-trip ?p ?outward-departure ?return-departure)\n "
			+ "                  (train-station ?p)\n " + "                  (not (done-train-booking))\n "
			+ "                )\n " + "  :effect (and\n " + "            (not (at-bologna))\n "
			+ "            (not(future ?outward-departure))\n " + "            (not(future ?outward-middle-hour))\n "
			+ "            (not(future ?outward-arrival))\n " + "            (at ?p ?outward-arrival)\n "
			+ "            (visited ?p)\n " + "            ;Predicate to fix return\n "
			+ "            (booked-return-train ?p ?return-departure ?return-arrival)\n "
			+ "            (done-train-booking)\n " + "          )\n " + "  )\n " + "\n "
			+ "  ; Book train outward journey\n " + "  ; Trip lasts 2hours\n " + "  (:action book-train-outward\n "
			+ "  :parameters (?p - place ?outward-departure ?outward-middle-hour ?outward-arrival - time)\n "
			+ "  :precondition (and\n " + "                  (at-bologna)\n "
			+ "                  (not (done-train-booking))\n "
			+ "                  (consecutive ?outward-departure ?outward-middle-hour)\n "
			+ "                  (consecutive ?outward-middle-hour ?outward-arrival)\n "
			+ "                  (future ?outward-departure)\n " + "                  (future ?outward-middle-hour)\n "
			+ "                  (future ?outward-arrival)\n "
			+ "                  (outward-train ?p ?outward-departure ?outward-arrival)\n "
			+ "                  (train-station ?p)\n " + "                  (not(done-outward-train-booking))\n "
			+ "                )\n " + "  :effect (and\n " + "            (not (at-bologna))\n "
			+ "            (not(future ?outward-departure))\n " + "            (not(future ?outward-middle-hour))\n "
			+ "            (not(future ?outward-arrival))\n " + "            (at ?p ?outward-arrival)\n "
			+ "            (visited ?p)\n " + "            (done-outward-train-booking)\n " + "          )\n "
			+ "  )\n " + "\n " + "  ; Book train return journey\n " + "  ; Trip lasts 2hours\n "
			+ "  (:action book-train-return\n "
			+ "  :parameters (?p - place ?return-departure ?return-middle-hour ?return-arrival - time)\n "
			+ "  :precondition (and\n " + "                  (not (done-train-booking))\n "
			+ "                  (consecutive ?return-departure ?return-middle-hour)\n "
			+ "                  (consecutive ?return-middle-hour ?return-arrival)\n "
			+ "                  (future ?return-departure)\n " + "                  (future ?return-middle-hour)\n "
			+ "                  (future ?return-arrival)\n "
			+ "                  (return-train ?p ?return-departure ?return-arrival)\n "
			+ "                  (train-station ?p)\n " + "                )\n " + "  :effect (and\n "
			+ "            (booked-return-train ?p ?return-departure ?return-arrival)\n "
			+ "            (done-train-booking)\n " + "          )\n " + "  )\n " + "\n " + "  ; Return home\n "
			+ "  ; This action is the last action to be done\n " + "  ; Depends on the booked train\n "
			+ "  ; Trip lasts 2hours\n " + "  (:action return-home\n "
			+ "  :parameters (?src - place ?return-departure ?return-middle-hour ?return-arrival - time)\n "
			+ "  :precondition (and\n " + "                  (done-hotel-booking)\n "
			+ "                  (train-station ?src)\n " + "                  (at ?src ?return-departure)\n "
			+ "                  (consecutive ?return-departure ?return-middle-hour)\n "
			+ "                  (consecutive ?return-middle-hour ?return-arrival)\n "
			+ "                  (future ?return-middle-hour)\n " + "                  (future ?return-arrival)\n "
			+ "                  (booked-return-train ?src ?return-departure ?return-arrival)\n "
			+ "                )\n " + "  :effect (and\n " + "            (not(future ?return-departure))\n "
			+ "            (not(future ?return-middle-hour))\n " + "            (not(future ?return-arrival))\n "
			+ "            (at-bologna)\n " + "(not (at ?src ?return-departure)))\n " + "  )\n " + "\n " + "  ; Book hotel\n "
			+ "  (:action book-hotel\n " + "  :parameters (?p - place)\n " + "  :precondition (and\n "
			+ "                  (done-train-booking)\n " + "                  (not (done-hotel-booking))\n "
			+ "                  (can-sleep ?p)\n " + "                )\n " + "  :effect (and\n "
			+ "            (done-hotel-booking)\n " + "            (booked-hotel ?p)\n " + "          )\n " + "  )\n "
			+ "\n " + "  ; Traveling by foot takes 1 time unit\n " + "  ; The destination is marked as visited\n "
			+ "  (:action travel-by-foot\n " + "  :parameters (?src ?dst - place ?start-time ?end-time - time)\n "
			+ "  :precondition (and\n " + "                  (done-hotel-booking)\n "
			+ "                  (at ?src ?start-time)\n " + "                  (foot-path ?src ?dst)\n "
			+ "                  (consecutive ?start-time ?end-time)\n " + "                  (future ?end-time)\n "
			+ "                )\n " + "  :effect (and\n " + "              (not (future ?start-time))\n "
			+ "              (not (future ?end-time))\n " + "              (not (at ?src ?start-time))\n "
			+ "              (at ?dst ?end-time)\n " + "              (visited ?dst)\n " + "          )\n " + "  )\n "
			+ "\n " + "  ; Traveling by bus takes 1 time unit\n " + "  ; The destination is marked as visited\n "
			+ "  (:action travel-by-bus\n "
			+ "  :parameters (?src ?dst - place ?departure-time ?arrival-time - time)\n " + "  :precondition (and\n "
			+ "                  (done-hotel-booking)\n " + "                  (at ?src ?departure-time)\n "
			+ "                  (consecutive ?departure-time ?arrival-time)\n "
			+ "                  (bus-path ?src ?dst)\n " + "                  (future ?arrival-time)\n "
			+ "                  (bus-scheduled ?src ?dst ?departure-time)\n " + "                )\n "
			+ "  :effect (and\n " + "              (not (future ?departure-time))\n "
			+ "              (not (future ?arrival-time))\n " + "              (not (at ?src ?departure-time))\n "
			+ "              (at ?dst ?arrival-time)\n " + "              (visited ?dst)\n " + "          )\n "
			+ "  )\n " + "\n " + "  ; Waiting lasts 1 time unit\n " + "  (:action wait\n "
			+ "  :parameters (?p - place ?start-time ?end-time - time)\n " + "  :precondition (and\n "
			+ "                  (done-hotel-booking)\n " + "                  (at ?p ?start-time)\n "
			+ "                  (future ?end-time)\n " + "                  (consecutive ?start-time ?end-time)\n "
			+ "                )\n " + "  :effect (and\n " + "              (at ?p ?end-time)\n "
			+ "              (not (at ?p ?start-time))\n " + "              (not (future ?end-time))\n "
			+ "          )\n " + "  )\n " + "\n " + "  ; Sleeping is essentialy a wait action for several hours\n "
			+ "  ; In this case, 8h\n " + "  ; Location must be the booked hotel\n " + "  (:action sleep\n "
			+ "  :parameters (?p - place ?hour1 ?hour2 ?hour3 ?hour4 ?hour5 ?hour6 ?hour7 ?hour8 ?hour9 - time)\n "
			+ "  :precondition (and\n " + "                  (done-hotel-booking)\n "
			+ "                  (sleep-time ?hour1)\n " + "                  (booked-hotel ?p)\n "
			+ "                  (at ?p ?hour1)\n " + "                  (consecutive ?hour1 ?hour2)\n "
			+ "                  (consecutive ?hour2 ?hour3)\n " + "                  (consecutive ?hour3 ?hour4)\n "
			+ "                  (consecutive ?hour4 ?hour5)\n " + "                  (consecutive ?hour5 ?hour6)\n "
			+ "                  (consecutive ?hour6 ?hour7)\n " + "                  (consecutive ?hour7 ?hour8)\n "
			+ "                  (consecutive ?hour8 ?hour9)\n " + "                  (future ?hour2)\n "
			+ "                  (future ?hour3)\n " + "                  (future ?hour4)\n "
			+ "                  (future ?hour5)\n " + "                  (future ?hour6)\n "
			+ "                  (future ?hour7)\n " + "                  (future ?hour8)\n "
			+ "                  (future ?hour9)\n " + "\n " + "                )\n " + "  :effect (and\n "
			+ "              (not (at ?p ?hour1))\n " + "              (not (future ?hour2))\n "
			+ "              (not (future ?hour3))\n " + "              (not (future ?hour4))\n "
			+ "              (not (future ?hour5))\n " + "              (not (future ?hour6))\n "
			+ "              (not (future ?hour7))\n " + "              (not (future ?hour8))\n "
			+ "              (not (future ?hour9))\n " + "              (at ?p ?hour9)\n " + "              (slept)\n "
			+ "          )\n " + "  )\n " + "  ; Sleeping is essentially a wait action for several hours\n "
			+ "  ; In this case, 4h\n " + "  ; Location must be the booked hotel\n " + "  (:action sleep-short\n "
			+ "  :parameters (?p - place ?hour1 ?hour2 ?hour3 ?hour4 ?hour5 - time)\n " + "  :precondition (and\n "
			+ "                  (done-hotel-booking)\n " + "                  (sleep-time ?hour1)\n "
			+ "                  (booked-hotel ?p)\n " + "                  (at ?p ?hour1)\n "
			+ "                  (consecutive ?hour1 ?hour2)\n " + "                  (consecutive ?hour2 ?hour3)\n "
			+ "                  (consecutive ?hour3 ?hour4)\n " + "                  (consecutive ?hour4 ?hour5)\n "
			+ "                  (future ?hour2)\n " + "                  (future ?hour3)\n "
			+ "                  (future ?hour4)\n " + "                  (future ?hour5)\n " + "\n "
			+ "                )\n " + "  :effect (and\n " + "              (not (at ?p ?hour1))\n "
			+ "              (not (future ?hour2))\n " + "              (not (future ?hour3))\n "
			+ "              (not (future ?hour4))\n " + "              (not (future ?hour5))\n "
			+ "              (at ?p ?hour5)\n " + "              (slept)\n " + "          )\n " + "  )\n " + "\n "
			+ "  ; Have breakfast\n " + "  ; Must be during breakfast hours\n "
			+ "  ; hour1 and hour2 is the time in which I am having breakfast\n "
			+ "  ; start-breakfast and end-breakfast is the time range in which it is possible to have breakfast\n "
			+ "  ; Lasts 1h \n " 
                        + "  (:action have-breakfast\n "
			+ "  :parameters (?p - place ?hour1 ?hour2 ?start-breakfast ?end-breakfast - time)\n "
			+ "  :precondition (and\n " + "                  (done-hotel-booking)\n "
			+ "                  (at ?p ?hour1)\n " + "                  (booked-hotel ?p)\n "
			+ "                  (consecutive ?hour1 ?hour2)\n " + "                  (future ?hour2)\n "
			+ "                  (breakfast ?start-breakfast ?end-breakfast)\n "
			+ "                  (not (future ?start-breakfast))\n " 
                        + "                  (future ?end-breakfast)\n "
                        + "                  (not(had-breakfast))\n "
			+ "                )\n " 
                        + "  :effect (and\n " 
                        + "              (not (at ?p ?hour1))\n "
			+ "              (not (future ?hour2))\n " 
                        + "              (at ?p ?hour2)\n "
			+ "              (had-breakfast)\n " + "          )\n " + "  )\n " + "\n " 
                        + "  ;Activities\n "
			+ "  ;Activities last 2h\n " + "\n " 
                        + "  (:action do-activity\n "
			+ "  :parameters (?p - place ?hour1 ?hour2 ?hour3 ?opening-time ?closing-time - time)\n "
			+ "  :precondition (and\n " + "                  (done-hotel-booking)\n "
			+ "                  (at ?p ?hour1)\n " + "                  (activity-available ?p ?hour1)\n "
			+ "                  (consecutive ?hour1 ?hour2)\n " + "                  (consecutive ?hour2 ?hour3)\n "
			+ "                  (future ?hour2)\n " + "                  (future ?hour3)\n "
			+ "                  (opening-hours ?p ?opening-time ?closing-time)\n "
			+ "                  (not (future ?opening-time))\n " + "                  (future ?closing-time)\n "
			+ "                  (not (= ?hour2 ?closing-time))\n " + "(not (done-activity ?p)))\n " 
                        + "  :effect (and\n "
			+ "              (at ?p ?hour3)\n " + "              (not (at ?p ?hour1))\n "
			+ "              (done-activity-timed ?p ?hour1)\n " + "              (done-activity ?p)\n "
			+ "              (not (future ?hour1))\n " + "              (not (future ?hour2))\n "
			+ "              (not (future ?hour3))\n " + "          )\n " + "  )\n " + "\n " 
                        + "  ; Go running\n "
			+ "  ; Must be before breakfast\n " + "  ; hour1 and hour2 is the time in which I am going running\n "
			+ "  ; start-breakfast and end-breakfast is the time range in which it is possible to have breakfast\n "
			+ "  ; Lasts 1h \n " 
                        + "  (:action go-running\n "
			+ "  :parameters (?p - place ?hour1 ?hour2 ?start-breakfast ?end-breakfast - time)\n "
			+ "  :precondition (and\n " 
                        + "                  (done-hotel-booking)\n "
			+ "                  (at ?p ?hour1)\n " 
                        + "                  (booked-hotel ?p)\n "
			+ "                  (consecutive ?hour1 ?hour2)\n " 
                        + "                  (future ?hour2)\n "
			+ "                  (breakfast ?start-breakfast ?end-breakfast)\n "
			+ "                  (future ?end-breakfast)\n " 
                        + "                  (not (had-breakfast))\n "
                        + "                  (not (gone-running))\n "
			+ "                  (slept)\n " 
                        + "                )\n " 
                        + "  :effect (and\n "
			+ "              (not (at ?p ?hour1))\n " 
                        + "              (not (future ?hour2))\n "
			+ "              (at ?p ?hour2)\n " 
                        + "              (gone-running)\n " 
                        + "          )\n " + "  )\n "
			+ "\n " + ")";



	public Problem generateProblem(int difficulty) {
		//const locationArray=[colosseo, treviFountain, pantheon, auditorium, araPacis, stadio, sanPietro, hotel1, hotel2, hotel3, hotel4, trastevere, termini, tiburtina]
		//const locationGoalIds = [3,4,7,0,6,0,5,0,0,0,0,8,0,0]
                final List<Integer> visitedGoals = Arrays.asList(3,4,5,6,7,8);
		final List<Integer> doActivityGoals = Arrays.asList(9,10,11,12,13);
		final List<Integer> doActivityTimedTrastevere = Arrays.asList(14,15,16,17,18,19,20,21,22,23);
		final List<Integer> doActivityTimedStadio = Arrays.asList(24,25,26,27);
		final List<Integer> doActivityTimedAuditorium =Arrays.asList(28,29,30,31);
		Problem problem;
		// Generate problems until you find a solvable one
		boolean solvable = false;
		do {
			problem = new Problem(difficulty);
			// TODO: GENERATE PROBLEM!
			// things to generate: goals, sleep-hour, trains
			// List of the goal ids
			ArrayList<Integer> goalIdList = new ArrayList<Integer>();
			goalIdList.addAll(Arrays.asList(1, 2)); // fixed goals: slept and at-bologna
			ArrayList<Integer> newGoals = new ArrayList<Integer>(); // temp list for goals
			int category; // used to choose between the 3 categories of timed activities goals (stadio, auditorium, trastevere)
			// To generate goals randomly, shuffle the list for each type of goal, then get the desired number of elements
			switch (difficulty) {
				case 1:
					// EASY, lvl 1
					// 2 visited goals
					Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
					newGoals = new ArrayList<Integer>(visitedGoals.subList(0, 2)); // fromIndex inclusive, toIndex exclusive
					break;
				case 2: 
					// EASY, lvl 2
					// 2 visited goals
					// 1 do-activity
					Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
					newGoals = new ArrayList<Integer>(visitedGoals.subList(0, 3));
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals.add(doActivityGoals.get(0));
					break;
				
                                    case 3:
					// EASY, lvl 3
					// 2 visited goals
					// 1 do-activity
					// 1 do-activity timed
					Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
					newGoals = new ArrayList<Integer>(visitedGoals.subList(0, 2));
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals.add(doActivityGoals.get(0));
					// Choose randomly 1 of the 3 categories of do-activity-timed goals
					
                                    /*
					Collections.shuffle(visitedGoals);
					newGoals.add(visitedGoals.get(0));
					Collections.shuffle(doActivityGoals);
					category = randomNumberInRange(1, 3);
					if (category == 1) {
						// Category 1: Stadio
						Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (category == 2) {
						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}*/
					newGoals.add(33); // id for have-breakfast goal
					break;
                                   
                                case 4:
					// MEDIUM, lvl 4
					// 1 visited goal
					// 1 do-activity
					// have-breakfast
                                    
                                        Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
					newGoals = new ArrayList<Integer>(visitedGoals.subList(0, 1));
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals.add(doActivityGoals.get(0));
                                        newGoals.add(33);
                                       
                                        break;
                                case 5:   
                                        Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
					newGoals=new ArrayList<Integer>(visitedGoals.subList(0, 2));
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals.add(doActivityGoals.get(0));
					//newGoals.add(33); // id for have-breakfast goal
                                         category = randomNumberInRange(1, 3);
					if (category == 1) {
						// Category 1: Stadio
						Collections.shuffle(doActivityTimedStadio);
                                                Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (category == 2) {
						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
                                                Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
                                                Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}
                                        newGoals.add(32);
					break;
				case 6:
                                        Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
					newGoals=new ArrayList<Integer>(visitedGoals.subList(0, 1));
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals.add(doActivityGoals.get(0));
                                        newGoals.add(doActivityGoals.get(1));
                                        category = randomNumberInRange(1, 3);
					if (category == 1) {
						// Category 1: Stadio
						Collections.shuffle(doActivityTimedStadio);
                                                Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (category == 2) {
						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
                                                Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
                                                Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}
					newGoals.add(33); // id for have-breakfast goal
					break;
                                    
                                case 7:
					// HARD, lvl 7
					// 1 do-activity
					// 1 do-activity timed
					// have-breakfast + go running
					
                                         Collections.shuffle(visitedGoals);
                                        Collections.shuffle(visitedGoals);
                                        Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals=new ArrayList<Integer>(visitedGoals.subList(0, 1));
					newGoals.add(doActivityGoals.get(0));
					newGoals.add(33); // id for have-breakfast goal
					newGoals.add(32); // id for go running goal
					// Choose randomly 1 of the 3 categories of do-activity-timed goals
					category = randomNumberInRange(1, 3);
					if (category == 1) {
						// Category 1: Stadio
						Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (category == 2) {
						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}
					break;
				case 8:
					// HARD, lvl 8
					// 2 visited goal
					// 1 do-activity
					// 1 do-activity timed
					// have-breakfast + go running
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
                                        newGoals=new ArrayList<Integer>(doActivityGoals.subList(0,2));
                                        Collections.shuffle(visitedGoals);
					Collections.shuffle(visitedGoals);
					newGoals.add(visitedGoals.get(0));
                                         
					category=randomNumberInRange(1,2);
                                        if(category==1){
                                        newGoals.add(33); // id for have-breakfast goal
                                        }else{
                                          newGoals.add(visitedGoals.get(1));
                                        }
                                        newGoals.add(32); // id for go running goal
					// Choose randomly 1 of the 3 categories of do-activity-timed goals
					category = randomNumberInRange(1, 3);
					if (category == 1) {
						// Category 1: Stadio
						Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (category == 2) {
						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}
					break;
				
				case 9:
                                        // HARD, lvl 9
					// 2 do-activity
					// 2 do-activity timed
					// have-breakfast + go running
					Collections.shuffle(doActivityGoals);
                                        Collections.shuffle(doActivityGoals);
					newGoals = new ArrayList<Integer>(doActivityGoals.subList(0, 2));
                                        Collections.shuffle(visitedGoals);
					newGoals.add(visitedGoals.get(0));
                                        newGoals.add(visitedGoals.get(1));
					// Choose randomly 1 of the 3 categories of do-activity-timed goals
					category = randomNumberInRange(1, 3);					
                                        
                                        if (category == 1) {
						// Category 1: Stadio
						Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (category == 2) {
//						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}
					int secondCategory=(category+1)%3+1;
                                        
					if (secondCategory == 1) {
						// Category 1: Stadio
                                                Collections.shuffle(doActivityTimedStadio);
						newGoals.add(doActivityTimedStadio.get(0));
					} else if (secondCategory == 2) {
						// Category 2: Auditorium
						Collections.shuffle(doActivityTimedAuditorium);
						newGoals.add(doActivityTimedAuditorium.get(0));						
					} else {
						// Category 3: Trastevere
						Collections.shuffle(doActivityTimedTrastevere);
						newGoals.add(doActivityTimedTrastevere.get(0));
					}
					newGoals.add(33); // id for have-breakfast goal
					newGoals.add(32); // id for go running goal
					break;
                                default:
                                    
                                    break;

			}
                        if (difficulty >9)
                        {
                        return null;
                        }
                        
			goalIdList.addAll(newGoals);
			System.out.println("Difficulty: " + difficulty + ", Generated goals: " + goalIdList.toString());
			// SLEEP-HOUR
                        //return in hotel old sleep hour
                        int ora= (int)(Math.random()*3+22);
			problem.setSleepHour(ora);
		/*	if (goalIdList.contains(18) || goalIdList.contains(18)) {
				problem.setSleepHour(25);
			}
			if (goalIdList.contains(20)) {
				problem.setSleepHour(26);
			}*/
			if (goalIdList.contains(18)||goalIdList.contains(28)||goalIdList.contains(19)) 
			{
			problem.setSleepHour(24);
			}			
			//GOALS			
			for (Integer goal : goalIdList) {
				Goal goal1 = new Goal(goal);
				problem.getGoals().add(goal1);
			}
			
			// TRAIN TRIPS
                        if(difficulty >6){
			TrainTrip trip = new TrainTrip("Termini", 6, 46);
			trip.generatePddlString();
                        problem.getTrainTrips().add(trip);}
                        else if(difficulty<=6&&difficulty>=3){
			TrainTrip trip = new TrainTrip("Termini", 8, 42);
			trip.generatePddlString();
                        problem.getTrainTrips().add(trip);}
                         else if(difficulty<3){
			TrainTrip trip = new TrainTrip("Termini", 13, 40);
			trip.generatePddlString();
                        problem.getTrainTrips().add(trip);}
			TrainTrip trip2 = new TrainTrip("Termini", 13, 44);
			trip2.generatePddlString();
			problem.getTrainTrips().add(trip2);
			TrainTrip trip3 = new TrainTrip("Termini", 10, 42);
			trip3.generatePddlString();
			problem.getTrainTrips().add(trip3);
			
			problem.generateProblemString();
			
			solvable = isSolvable(problem);
		} while (!solvable);

		return problem;
	}
	
	public boolean isSolvable(Problem problem) {
		boolean solvable;
		final ProblemFactory factory = new ProblemFactory();
		// Parse the domain and the problem
		ErrorManager errorManager;
		try {
			errorManager = factory.parseFromString(DOMAIN_FILE, problem.getProblemString());
			if (!errorManager.isEmpty()) {
				errorManager.printAll();
				System.exit(0);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Encode and simplify the planning problem in a compact representation
		final CodedProblem pb = factory.encode();
		if (!pb.isSolvable()) {
			System.out.println("\n Goal can be simplified to FALSE. No search will solve it");
			solvable = false;
		} else {
			// Create the planner
			final HSP planner = new HSP();
                        //FAST_FORWARD
			planner.setHeuristicType(Heuristic.Type.AJUSTED_SUM2);
			planner.setSaveState(false);
			planner.setTimeOut(1);
			// Search for a solution plan
			final Plan plan = planner.search(pb);
			if (plan != null) {
				System.out.println(String.format("\n\n Found plan as follows:\n"));
				System.out.println(pb.toString(plan));
				solvable = true;
			} else {
				System.out.append(String.format("\n No plan found unsolvable!"));
				solvable = false;
			}
		}
		return solvable;	
	}
	
	public String getNextAction(Problem problem) {
		System.out.println("Getting next action...");
		String firstAction = "";
		final ProblemFactory factory = new ProblemFactory();
		// Parse the domain and the problem
		ErrorManager errorManager;
		try {
			errorManager = factory.parseFromString(DOMAIN_FILE, problem.getProblemString());
			if (!errorManager.isEmpty()) {
				errorManager.printAll();
				System.out.println("ERROR");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Encode and simplify the planning problem in a compact representation
		final CodedProblem pb = factory.encode();
		if (!pb.isSolvable()) {
			System.out.println("\n Goal can be simplified to FALSE. No search will solve it");
		} else {
			// Create the planner
			final HSP planner = new HSP();
			planner.setHeuristicType(Heuristic.Type.AJUSTED_SUM2);
			planner.setSaveState(false);
			planner.setTimeOut(1);
			// Search for a solution plan
			final Plan plan = planner.search(pb);
			if (plan != null) {
				System.out.println(String.format("\n\n Found plan as follows:\n"));
				System.out.println(pb.toString(plan));
				String planString = pb.toString(plan);
				// Get the first action of the plan
				// Get first line, format: 00: (  book-train-round-trip tiburtina hour13 hour14 hour15 hour44 hour45 hour46) [1]
				String firstLine = planString.split(":")[1];
				// Get that is between parentheses
				firstAction = firstLine.substring(firstLine.indexOf("(")+1,firstLine.indexOf(")")).trim();
			} else {
				System.out.append(String.format("\n No plan found unsolvable!"));
			}
		}
		return firstAction;	
	}
	
    private static int randomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();
    }

}
