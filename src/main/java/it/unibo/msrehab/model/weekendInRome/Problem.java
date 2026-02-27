package it.unibo.msrehab.model.weekendInRome;

import java.util.ArrayList;
import java.util.Arrays;

//This class represents a PDDL Problem in the Weekend for Rome exercise
//Creator: Margherita Donnici


public class Problem {
	
	// Default initial state for problems - these beliefs are static, always present and cannot be changed by actions
	private static String PROBLEM_INIT = "(define (problem p01) \n " + " (:domain weekend-in-rome)\n " + " (:objects\n "
			+ "   hour1 hour2 hour3 hour4 hour5 hour6 hour7 hour8 hour9 hour10 hour11 hour12 hour13 hour14 hour15 hour16 hour17 hour18 hour19 hour20 hour21 hour22 hour23 hour24 hour25 hour26 hour27 hour28 hour29 hour30 hour31 hour32 hour33 hour34 hour35 hour36 hour37 hour38 hour39 hour40 hour41 hour42 hour43 hour44 hour45 hour46 hour47 hour48 - time\n "
			+ "   termini pantheon tiburtina stadio-olimpico trevi-fountain colosseo ara-pacis san-pietro trastevere auditorium belvedere centrale europa aurora - place\n "
			+ " )\n " + " (:init\n " + "   ;time\n " + "   (consecutive hour1 hour2)\n "
			+ "   (consecutive hour2 hour3)\n " + "   (consecutive hour3 hour4)\n " + "   (consecutive hour4 hour5)\n "
			+ "   (consecutive hour5 hour6)\n " + "   (consecutive hour6 hour7)\n " + "   (consecutive hour7 hour8)\n "
			+ "   (consecutive hour8 hour9)\n " + "   (consecutive hour9 hour10)\n "
			+ "   (consecutive hour10 hour11)\n " + "   (consecutive hour11 hour12)\n "
			+ "   (consecutive hour12 hour13)\n " + "   (consecutive hour13 hour14)\n "
			+ "   (consecutive hour14 hour15)\n " + "   (consecutive hour15 hour16)\n "
			+ "   (consecutive hour16 hour17)\n " + "   (consecutive hour17 hour18)\n "
			+ "   (consecutive hour18 hour19)\n " + "   (consecutive hour19 hour20)\n "
			+ "   (consecutive hour20 hour21)\n " + "   (consecutive hour21 hour22)\n "
			+ "   (consecutive hour22 hour23)\n " + "   (consecutive hour23 hour24)\n "
			+ "   (consecutive hour24 hour25)\n " + "   (consecutive hour25 hour26)\n "
			+ "   (consecutive hour26 hour27)\n " + "   (consecutive hour27 hour28)\n "
			+ "   (consecutive hour28 hour29)\n " + "   (consecutive hour29 hour30)\n "
			+ "   (consecutive hour30 hour31)\n " + "   (consecutive hour31 hour32)\n "
			+ "   (consecutive hour32 hour33)\n " + "   (consecutive hour33 hour34)\n "
			+ "   (consecutive hour34 hour35)\n " + "   (consecutive hour35 hour36)\n "
			+ "   (consecutive hour36 hour37)\n " + "   (consecutive hour37 hour38)\n "
			+ "   (consecutive hour38 hour39)\n " + "   (consecutive hour39 hour40)\n "
			+ "   (consecutive hour40 hour41)\n " + "   (consecutive hour41 hour42)\n "
			+ "   (consecutive hour42 hour43)\n " + "   (consecutive hour43 hour44)\n "
			+ "   (consecutive hour44 hour45)\n " + "   (consecutive hour45 hour46)\n "
			+ "   (consecutive hour46 hour47)\n " + "   (consecutive hour47 hour48)\n " + "   ;map\n " + "   (foot-path stadio-olimpico europa)\n "
			+ "   (foot-path europa stadio-olimpico)\n " + "   (foot-path stadio-olimpico auditorium)\n "
			+ "   (foot-path auditorium stadio-olimpico)\n " + "   (foot-path europa san-pietro)\n "
			+ "   (foot-path san-pietro europa)\n " + "   (foot-path san-pietro trastevere)\n "
			+ "   (foot-path trastevere san-pietro)\n " + "   (foot-path ara-pacis pantheon)\n "
			+ "   (foot-path pantheon ara-pacis)\n " + "   (foot-path ara-pacis centrale)\n "
			+ "   (foot-path centrale ara-pacis)\n " + "   (foot-path centrale trastevere)\n "
			+ "   (foot-path trastevere centrale)\n " + "   (foot-path centrale trevi-fountain)\n "
			+ "   (foot-path trevi-fountain centrale)\n " + "   (foot-path pantheon trevi-fountain)\n "
			+ "   (foot-path trevi-fountain pantheon)\n " + "   (foot-path aurora trevi-fountain)\n "
			+ "   (foot-path trevi-fountain aurora)\n " + "   (foot-path aurora colosseo)\n "
			+ "   (foot-path colosseo aurora)\n " + "   (foot-path tiburtina belvedere)\n "
			+ "   (foot-path belvedere tiburtina)\n " + "   (bus-path colosseo tiburtina)\n "
			+ "   (bus-path tiburtina colosseo)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled colosseo tiburtina hour5)\n " + "   (bus-scheduled colosseo tiburtina hour7)\n "
			+ "   (bus-scheduled colosseo tiburtina hour9)\n " + "   (bus-scheduled colosseo tiburtina hour11)\n "
			+ "   (bus-scheduled colosseo tiburtina hour13)\n " + "   (bus-scheduled colosseo tiburtina hour15)\n "
			+ "   (bus-scheduled colosseo tiburtina hour17)\n " + "   (bus-scheduled colosseo tiburtina hour19)\n "
			+ "   (bus-scheduled colosseo tiburtina hour21)\n " + "   (bus-scheduled colosseo tiburtina hour23)\n "
			+ "   (bus-scheduled tiburtina colosseo hour8)\n " + "   (bus-scheduled tiburtina colosseo hour10)\n "
			+ "   (bus-scheduled tiburtina colosseo hour12)\n " + "   (bus-scheduled tiburtina colosseo hour14)\n "
			+ "   (bus-scheduled tiburtina colosseo hour16)\n " + "   (bus-scheduled tiburtina colosseo hour18)\n "
			+ "   (bus-scheduled tiburtina colosseo hour20)\n " + "   (bus-scheduled tiburtina colosseo hour21)\n "
			+ "   (bus-scheduled tiburtina colosseo hour22)\n " + "   (bus-scheduled tiburtina colosseo hour24)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled colosseo tiburtina hour25)\n "
			+ "   (bus-scheduled colosseo tiburtina hour31)\n " + "   (bus-scheduled colosseo tiburtina hour34)\n "
			+ "   (bus-scheduled colosseo tiburtina hour37)\n " + "   (bus-scheduled colosseo tiburtina hour40)\n "
			+ "   (bus-scheduled colosseo tiburtina hour43)\n " + "   (bus-scheduled colosseo tiburtina hour46)\n "
			+ "   (bus-scheduled tiburtina colosseo hour32)\n " + "   (bus-scheduled tiburtina colosseo hour35)\n "
			+ "   (bus-scheduled tiburtina colosseo hour38)\n " + "   (bus-scheduled tiburtina colosseo hour41)\n "
			+ "   (bus-scheduled tiburtina colosseo hour44)\n " + "   (bus-scheduled tiburtina colosseo hour47)\n "
			+ "   (bus-path termini tiburtina)\n " + "   (bus-path tiburtina termini)\n "
			+ "   (bus-scheduled termini tiburtina hour1)\n " + "   (bus-scheduled termini tiburtina hour2)\n "
			+ "   (bus-scheduled termini tiburtina hour3)\n " + "   (bus-scheduled termini tiburtina hour4)\n "
			+ "   (bus-scheduled termini tiburtina hour5)\n " + "   (bus-scheduled termini tiburtina hour6)\n "
			+ "   (bus-scheduled termini tiburtina hour7)\n " + "   (bus-scheduled termini tiburtina hour8)\n "
			+ "   (bus-scheduled termini tiburtina hour9)\n " + "   (bus-scheduled termini tiburtina hour10)\n "
			+ "   (bus-scheduled termini tiburtina hour11)\n " + "   (bus-scheduled termini tiburtina hour12)\n "
			+ "   (bus-scheduled termini tiburtina hour13)\n " + "   (bus-scheduled termini tiburtina hour14)\n "
			+ "   (bus-scheduled termini tiburtina hour15)\n " + "   (bus-scheduled termini tiburtina hour16)\n "
			+ "   (bus-scheduled termini tiburtina hour17)\n " + "   (bus-scheduled termini tiburtina hour18)\n "
			+ "   (bus-scheduled termini tiburtina hour19)\n " + "   (bus-scheduled termini tiburtina hour20)\n "
			+ "   (bus-scheduled termini tiburtina hour21)\n " + "   (bus-scheduled termini tiburtina hour22)\n "
			+ "   (bus-scheduled termini tiburtina hour23)\n " + "   (bus-scheduled termini tiburtina hour24)\n "
			+ "   (bus-scheduled termini tiburtina hour25)\n " + "   (bus-scheduled termini tiburtina hour26)\n "
			+ "   (bus-scheduled termini tiburtina hour27)\n " + "   (bus-scheduled termini tiburtina hour28)\n "
			+ "   (bus-scheduled termini tiburtina hour29)\n " + "   (bus-scheduled termini tiburtina hour30)\n "
			+ "   (bus-scheduled termini tiburtina hour31)\n " + "   (bus-scheduled termini tiburtina hour32)\n "
			+ "   (bus-scheduled termini tiburtina hour33)\n " + "   (bus-scheduled termini tiburtina hour34)\n "
			+ "   (bus-scheduled termini tiburtina hour35)\n " + "   (bus-scheduled termini tiburtina hour36)\n "
			+ "   (bus-scheduled termini tiburtina hour37)\n " + "   (bus-scheduled termini tiburtina hour38)\n "
			+ "   (bus-scheduled termini tiburtina hour39)\n " + "   (bus-scheduled termini tiburtina hour40)\n "
			+ "   (bus-scheduled termini tiburtina hour41)\n " + "   (bus-scheduled termini tiburtina hour42)\n "
			+ "   (bus-scheduled termini tiburtina hour43)\n " + "   (bus-scheduled termini tiburtina hour44)\n "
			+ "   (bus-scheduled termini tiburtina hour45)\n " + "   (bus-scheduled termini tiburtina hour46)\n "
			+ "   (bus-scheduled termini tiburtina hour47)\n " + "   (bus-scheduled tiburtina termini hour1)\n "
			+ "   (bus-scheduled tiburtina termini hour2)\n " + "   (bus-scheduled tiburtina termini hour3)\n "
			+ "   (bus-scheduled tiburtina termini hour4)\n " + "   (bus-scheduled tiburtina termini hour5)\n "
			+ "   (bus-scheduled tiburtina termini hour6)\n " + "   (bus-scheduled tiburtina termini hour7)\n "
			+ "   (bus-scheduled tiburtina termini hour8)\n " + "   (bus-scheduled tiburtina termini hour9)\n "
			+ "   (bus-scheduled tiburtina termini hour10)\n " + "   (bus-scheduled tiburtina termini hour11)\n "
			+ "   (bus-scheduled tiburtina termini hour12)\n " + "   (bus-scheduled tiburtina termini hour13)\n "
			+ "   (bus-scheduled tiburtina termini hour14)\n " + "   (bus-scheduled tiburtina termini hour15)\n "
			+ "   (bus-scheduled tiburtina termini hour16)\n " + "   (bus-scheduled tiburtina termini hour17)\n "
			+ "   (bus-scheduled tiburtina termini hour18)\n " + "   (bus-scheduled tiburtina termini hour19)\n "
			+ "   (bus-scheduled tiburtina termini hour20)\n " + "   (bus-scheduled tiburtina termini hour21)\n "
			+ "   (bus-scheduled tiburtina termini hour22)\n " + "   (bus-scheduled tiburtina termini hour23)\n "
			+ "   (bus-scheduled tiburtina termini hour24)\n " + "   (bus-scheduled tiburtina termini hour25)\n "
			+ "   (bus-scheduled tiburtina termini hour26)\n " + "   (bus-scheduled tiburtina termini hour27)\n "
			+ "   (bus-scheduled tiburtina termini hour28)\n " + "   (bus-scheduled tiburtina termini hour29)\n "
			+ "   (bus-scheduled tiburtina termini hour30)\n " + "   (bus-scheduled tiburtina termini hour31)\n "
			+ "   (bus-scheduled tiburtina termini hour32)\n " + "   (bus-scheduled tiburtina termini hour33)\n "
			+ "   (bus-scheduled tiburtina termini hour34)\n " + "   (bus-scheduled tiburtina termini hour35)\n "
			+ "   (bus-scheduled tiburtina termini hour36)\n " + "   (bus-scheduled tiburtina termini hour37)\n "
			+ "   (bus-scheduled tiburtina termini hour38)\n " + "   (bus-scheduled tiburtina termini hour39)\n "
			+ "   (bus-scheduled tiburtina termini hour40)\n " + "   (bus-scheduled tiburtina termini hour41)\n "
			+ "   (bus-scheduled tiburtina termini hour42)\n " + "   (bus-scheduled tiburtina termini hour43)\n "
			+ "   (bus-scheduled tiburtina termini hour44)\n " + "   (bus-scheduled tiburtina termini hour45)\n "
			+ "   (bus-scheduled tiburtina termini hour46)\n " + "   (bus-scheduled tiburtina termini hour47)\n "
			+ "   (bus-path termini auditorium)\n " + "   (bus-path auditorium termini)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled termini auditorium hour8)\n " + "   (bus-scheduled termini auditorium hour10)\n "
			+ "   (bus-scheduled termini auditorium hour12)\n " + "   (bus-scheduled termini auditorium hour14)\n "
			+ "   (bus-scheduled termini auditorium hour16)\n " + "   (bus-scheduled termini auditorium hour18)\n "
			+ "   (bus-scheduled termini auditorium hour20)\n " + "   (bus-scheduled auditorium termini hour8)\n "
			+ "   (bus-scheduled auditorium termini hour10)\n " + "   (bus-scheduled auditorium termini hour12)\n "
			+ "   (bus-scheduled auditorium termini hour14)\n " + "   (bus-scheduled auditorium termini hour16)\n "
			+ "   (bus-scheduled auditorium termini hour18)\n " + "   (bus-scheduled auditorium termini hour20)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled termini auditorium hour32)\n "
			+ "   (bus-scheduled termini auditorium hour34)\n " + "   (bus-scheduled termini auditorium hour37)\n "
			+ "   (bus-scheduled termini auditorium hour40)\n " + "   (bus-scheduled termini auditorium hour44)\n "
			+ "   (bus-scheduled auditorium termini hour32)\n " + "   (bus-scheduled auditorium termini hour35)\n "
			+ "   (bus-scheduled auditorium termini hour38)\n " + "   (bus-scheduled auditorium termini hour41)\n "
			+ "   (bus-scheduled auditorium termini hour44)\n " 
                        + "   (bus-path termini pantheon)\n "+ "   (bus-path pantheon termini)\n " + "   ;Saturday\n " 
                        + "   (bus-scheduled termini pantheon hour8)\n "
			+ "   (bus-scheduled termini pantheon hour11)\n " + "   (bus-scheduled termini pantheon hour14)\n "
			+ "   (bus-scheduled termini pantheon hour17)\n " + "   (bus-scheduled termini pantheon hour20)\n "
			+ "   (bus-scheduled pantheon termini hour8)\n " + "   (bus-scheduled pantheon termini hour11)\n "
			+ "   (bus-scheduled pantheon termini hour14)\n " + "   (bus-scheduled pantheon termini hour17)\n "
			+ "   (bus-scheduled pantheon termini hour20)\n " + "   ;Sunday\n "
			+ "   (bus-scheduled termini pantheon hour32)\n " + "   (bus-scheduled termini pantheon hour36)\n "
			+ "   (bus-scheduled termini pantheon hour40)\n " + "   (bus-scheduled termini pantheon hour44)\n "
			+ "   (bus-scheduled pantheon termini hour32)\n " + "   (bus-scheduled pantheon termini hour36)\n "
			+ "   (bus-scheduled pantheon termini hour40)\n " + "   (bus-scheduled pantheon termini hour44)\n "
			+ "   (bus-path colosseo trevi-fountain)\n " + "   (bus-path trevi-fountain colosseo)\n "+ "   ;Saturday\n " 
                        + "   (bus-scheduled colosseo trevi-fountain hour8)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour11)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour14)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour17)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour20)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour24)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour8)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour11)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour14)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour17)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour20)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour24)\n " 
                        + "   ;Sunday\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour32)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour36)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour40)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour44)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour32)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour36)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour40)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour44)\n " 
                        + "   (bus-path stadio-olimpico belvedere)\n "
                        + "   (bus-path belvedere stadio-olimpico)\n " 
                        + "   ;Saturday\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour8)\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour12)\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour16)\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour20)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour8)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour12)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour16)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour20)\n " + "   ;Sunday\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour33)\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour38)\n "
			+ "   (bus-scheduled stadio-olimpico belvedere hour43)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour33)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour38)\n "
			+ "   (bus-scheduled belvedere stadio-olimpico hour43)\n " 
                        + "   (bus-path stadio-olimpico tiburtina)\n "+ "   (bus-path tiburtina stadio-olimpico)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour5)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour7)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour9)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour11)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour13)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour15)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour17)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour19)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour21)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour23)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour5)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour7)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour9)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour11)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour13)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour15)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour17)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour19)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour21)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour23)\n " + "   ;Sunday\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour25)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour29)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour32)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour35)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour38)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour41)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour44)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour47)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour25)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour29)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour32)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour35)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour38)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour41)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour44)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour47)\n " 
                        + "   (bus-path san-pietro ara-pacis)\n "
                        + "   (bus-path ara-pacis san-pietro)\n " 
                        + "   ;Saturday\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour7)\n " 
                        + "   (bus-scheduled san-pietro ara-pacis hour10)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour13)\n " 
                        + "   (bus-scheduled san-pietro ara-pacis hour16)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour19)\n " 
                        + "   (bus-scheduled ara-pacis san-pietro hour7)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour10)\n " 
                        + "   (bus-scheduled ara-pacis san-pietro hour13)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour16)\n " 
                        + "   (bus-scheduled ara-pacis san-pietro hour19)\n "
			+ "   ;Sunday\n " 
                        + "   (bus-scheduled san-pietro ara-pacis hour31)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour35)\n " + "   (bus-scheduled san-pietro ara-pacis hour39)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour43)\n " + "   (bus-scheduled ara-pacis san-pietro hour31)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour35)\n " + "   (bus-scheduled ara-pacis san-pietro hour39)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour43)\n " 
                        + "   (bus-path trastevere ara-pacis)\n "+ "   (bus-path ara-pacis trastevere)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled trastevere ara-pacis hour7)\n " + "   (bus-scheduled trastevere ara-pacis hour10)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour13)\n " + "   (bus-scheduled trastevere ara-pacis hour16)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour19)\n " + "   (bus-scheduled trastevere ara-pacis hour22)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour7)\n " + "   (bus-scheduled ara-pacis trastevere hour10)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour13)\n " + "   (bus-scheduled ara-pacis trastevere hour16)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour19)\n " + "   (bus-scheduled ara-pacis trastevere hour22)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled trastevere ara-pacis hour25)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour26)\n " + "   (bus-scheduled trastevere ara-pacis hour27)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour31)\n " + "   (bus-scheduled trastevere ara-pacis hour35)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour39)\n " 
                        + "   (bus-scheduled trastevere ara-pacis hour43)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour25)\n " 
                        + "   (bus-scheduled ara-pacis trastevere hour26)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour27)\n " 
                        + "   (bus-scheduled ara-pacis trastevere hour31)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour35)\n " 
                        + "   (bus-scheduled ara-pacis trastevere hour39)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour43)\n " 
                        + "   (can-sleep aurora)\n "
			+ "   (can-sleep belvedere)\n " 
                        + "   (can-sleep centrale)\n " 
                        + "   (can-sleep europa)\n "
			+ "   (train-station termini)\n "
			+ "   (train-station tiburtina)\n " 
                        + "   (opening-hours colosseo hour9 hour18)\n "
			+ "   (opening-hours colosseo hour38 hour42)\n " 
                        + "   (opening-hours ara-pacis hour14 hour18)\n "
			+ "   (opening-hours ara-pacis hour33 hour37)\n " 
                        + "   (opening-hours san-pietro hour9 hour18)\n "
			+ "   (opening-hours san-pietro hour33 hour42)\n " 
                        + "   (opening-hours pantheon hour9 hour13)\n "
			+ "   (opening-hours pantheon hour38 hour42)\n " 
                        + "   (opening-hours trastevere hour9 hour26)\n "
			+ "   (opening-hours auditorium hour9 hour24)\n " 
                        + "   (opening-hours stadio-olimpico hour10 hour24)\n "
                        + "   (opening-hours trastevere hour33 hour46)\n "
			+ "   (opening-hours auditorium hour33 hour46)\n " 
                        + "   (opening-hours stadio-olimpico hour34 hour46)\n "
			+ "   (breakfast hour31 hour35)\n ";
	
	private static String PROBLEM_INIT_EASY = "(define (problem p01) \n " + " (:domain weekend-in-rome)\n " + " (:objects\n "
			+ "   hour1 hour2 hour3 hour4 hour5 hour6 hour7 hour8 hour9 hour10 hour11 hour12 hour13 hour14 hour15 hour16 hour17 hour18 hour19 hour20 hour21 hour22 hour23 hour24 hour25 hour26 hour27 hour28 hour29 hour30 hour31 hour32 hour33 hour34 hour35 hour36 hour37 hour38 hour39 hour40 hour41 hour42 hour43 hour44 hour45 hour46 hour47 hour48 - time\n "
			+ "   termini pantheon stadio-olimpico trevi-fountain colosseo ara-pacis san-pietro trastevere auditorium centrale europa aurora - place\n "
			+ " )\n " + " (:init\n " + "   ;time\n " + "   (consecutive hour1 hour2)\n "
			+ "   (consecutive hour2 hour3)\n " + "   (consecutive hour3 hour4)\n " + "   (consecutive hour4 hour5)\n "
			+ "   (consecutive hour5 hour6)\n " + "   (consecutive hour6 hour7)\n " + "   (consecutive hour7 hour8)\n "
			+ "   (consecutive hour8 hour9)\n " + "   (consecutive hour9 hour10)\n "
			+ "   (consecutive hour10 hour11)\n " + "   (consecutive hour11 hour12)\n "
			+ "   (consecutive hour12 hour13)\n " + "   (consecutive hour13 hour14)\n "
			+ "   (consecutive hour14 hour15)\n " + "   (consecutive hour15 hour16)\n "
			+ "   (consecutive hour16 hour17)\n " + "   (consecutive hour17 hour18)\n "
			+ "   (consecutive hour18 hour19)\n " + "   (consecutive hour19 hour20)\n "
			+ "   (consecutive hour20 hour21)\n " + "   (consecutive hour21 hour22)\n "
			+ "   (consecutive hour22 hour23)\n " + "   (consecutive hour23 hour24)\n "
			+ "   (consecutive hour24 hour25)\n " + "   (consecutive hour25 hour26)\n "
			+ "   (consecutive hour26 hour27)\n " + "   (consecutive hour27 hour28)\n "
			+ "   (consecutive hour28 hour29)\n " + "   (consecutive hour29 hour30)\n "
			+ "   (consecutive hour30 hour31)\n " + "   (consecutive hour31 hour32)\n "
			+ "   (consecutive hour32 hour33)\n " + "   (consecutive hour33 hour34)\n "
			+ "   (consecutive hour34 hour35)\n " + "   (consecutive hour35 hour36)\n "
			+ "   (consecutive hour36 hour37)\n " + "   (consecutive hour37 hour38)\n "
			+ "   (consecutive hour38 hour39)\n " + "   (consecutive hour39 hour40)\n "
			+ "   (consecutive hour40 hour41)\n " + "   (consecutive hour41 hour42)\n "
			+ "   (consecutive hour42 hour43)\n " + "   (consecutive hour43 hour44)\n "
			+ "   (consecutive hour44 hour45)\n " + "   (consecutive hour45 hour46)\n "
			+ "   (consecutive hour46 hour47)\n " + "   (consecutive hour47 hour48)\n " + "   ;map\n " + "   (foot-path stadio-olimpico europa)\n "
			+ "   (foot-path europa stadio-olimpico)\n " + "   (foot-path stadio-olimpico auditorium)\n "
			+ "   (foot-path auditorium stadio-olimpico)\n " + "   (foot-path europa san-pietro)\n "
			+ "   (foot-path san-pietro europa)\n " + "   (foot-path san-pietro trastevere)\n "
			+ "   (foot-path trastevere san-pietro)\n " + "   (foot-path ara-pacis pantheon)\n "
			+ "   (foot-path pantheon ara-pacis)\n " + "   (foot-path ara-pacis centrale)\n "
			+ "   (foot-path centrale ara-pacis)\n " + "   (foot-path centrale trastevere)\n "
			+ "   (foot-path trastevere centrale)\n " + "   (foot-path centrale trevi-fountain)\n "
			+ "   (foot-path trevi-fountain centrale)\n " + "   (foot-path pantheon trevi-fountain)\n "
			+ "   (foot-path trevi-fountain pantheon)\n " + "   (foot-path aurora trevi-fountain)\n "
			+ "   (foot-path trevi-fountain aurora)\n " + "   (foot-path aurora colosseo)\n "
			+ "   (foot-path colosseo aurora)\n "
			+ "   (bus-path termini auditorium)\n " + "   (bus-path auditorium termini)\n " + "   (bus-path termini pantheon)\n "
			+ "   (bus-path pantheon termini)\n "
			+ "   (bus-path colosseo trevi-fountain)\n " + "   (bus-path trevi-fountain colosseo)\n "
			+ "   (bus-path san-pietro ara-pacis)\n "
			+ "   (bus-path ara-pacis san-pietro)\n " + "   (bus-path trastevere ara-pacis)\n "
			+ "   (bus-path ara-pacis trastevere)\n "+ "   (can-sleep aurora)\n "
			+ "   (can-sleep centrale)\n " + "   (can-sleep europa)\n "
			+ "   (train-station termini)\n "
			+ "   (opening-hours colosseo hour9 hour18)\n "
			+ "   (opening-hours colosseo hour33 hour42)\n " + "   (opening-hours ara-pacis hour9 hour18)\n "
			+ "   (opening-hours ara-pacis hour33 hour42)\n " + "   (opening-hours san-pietro hour9 hour18)\n "
			+ "   (opening-hours san-pietro hour33 hour42)\n " + "   (opening-hours pantheon hour9 hour18)\n "
			+ "   (opening-hours pantheon hour33 hour42)\n " 
                        + "   (opening-hours pantheon hour38 hour42)\n " 
                        + "   (opening-hours trastevere hour9 hour26)\n "
			+ "   (opening-hours auditorium hour9 hour24)\n " 
                        + "   (opening-hours stadio-olimpico hour10 hour24)\n "
                        + "   (opening-hours trastevere hour33 hour46)\n "
			+ "   (opening-hours auditorium hour33 hour46)\n " 
                        + "   (opening-hours stadio-olimpico hour34 hour46)\n "
                        + "   (breakfast hour31 hour35)\n ";
	
	private static String PROBLEM_INIT_MEDIUM = "(define (problem p01) \n " + " (:domain weekend-in-rome)\n " + " (:objects\n "
			+ "   hour1 hour2 hour3 hour4 hour5 hour6 hour7 hour8 hour9 hour10 hour11 hour12 hour13 hour14 hour15 hour16 hour17 hour18 hour19 hour20 hour21 hour22 hour23 hour24 hour25 hour26 hour27 hour28 hour29 hour30 hour31 hour32 hour33 hour34 hour35 hour36 hour37 hour38 hour39 hour40 hour41 hour42 hour43 hour44 hour45 hour46 hour47 hour48 - time\n "
			+ "   termini tiburtina pantheon stadio-olimpico trevi-fountain colosseo ara-pacis san-pietro trastevere auditorium centrale europa aurora - place\n "
			+ " )\n " + " (:init\n " + "   ;time\n " + "   (consecutive hour1 hour2)\n "
			+ "   (consecutive hour2 hour3)\n " + "   (consecutive hour3 hour4)\n " + "   (consecutive hour4 hour5)\n "
			+ "   (consecutive hour5 hour6)\n " + "   (consecutive hour6 hour7)\n " + "   (consecutive hour7 hour8)\n "
			+ "   (consecutive hour8 hour9)\n " + "   (consecutive hour9 hour10)\n "
			+ "   (consecutive hour10 hour11)\n " + "   (consecutive hour11 hour12)\n "
			+ "   (consecutive hour12 hour13)\n " + "   (consecutive hour13 hour14)\n "
			+ "   (consecutive hour14 hour15)\n " + "   (consecutive hour15 hour16)\n "
			+ "   (consecutive hour16 hour17)\n " + "   (consecutive hour17 hour18)\n "
			+ "   (consecutive hour18 hour19)\n " + "   (consecutive hour19 hour20)\n "
			+ "   (consecutive hour20 hour21)\n " + "   (consecutive hour21 hour22)\n "
			+ "   (consecutive hour22 hour23)\n " + "   (consecutive hour23 hour24)\n "
			+ "   (consecutive hour24 hour25)\n " + "   (consecutive hour25 hour26)\n "
			+ "   (consecutive hour26 hour27)\n " + "   (consecutive hour27 hour28)\n "
			+ "   (consecutive hour28 hour29)\n " + "   (consecutive hour29 hour30)\n "
			+ "   (consecutive hour30 hour31)\n " + "   (consecutive hour31 hour32)\n "
			+ "   (consecutive hour32 hour33)\n " + "   (consecutive hour33 hour34)\n "
			+ "   (consecutive hour34 hour35)\n " + "   (consecutive hour35 hour36)\n "
			+ "   (consecutive hour36 hour37)\n " + "   (consecutive hour37 hour38)\n "
			+ "   (consecutive hour38 hour39)\n " + "   (consecutive hour39 hour40)\n "
			+ "   (consecutive hour40 hour41)\n " + "   (consecutive hour41 hour42)\n "
			+ "   (consecutive hour42 hour43)\n " + "   (consecutive hour43 hour44)\n "
			+ "   (consecutive hour44 hour45)\n " + "   (consecutive hour45 hour46)\n "
			+ "   (consecutive hour46 hour47)\n " + "   (consecutive hour47 hour48)\n " + "   ;map\n " + "   (foot-path stadio-olimpico europa)\n "
			+ "   (foot-path europa stadio-olimpico)\n " + "   (foot-path stadio-olimpico auditorium)\n "
			+ "   (foot-path auditorium stadio-olimpico)\n " + "   (foot-path europa san-pietro)\n "
			+ "   (foot-path san-pietro europa)\n " + "   (foot-path san-pietro trastevere)\n "
			+ "   (foot-path trastevere san-pietro)\n " + "   (foot-path ara-pacis pantheon)\n "
			+ "   (foot-path pantheon ara-pacis)\n " + "   (foot-path ara-pacis centrale)\n "
			+ "   (foot-path centrale ara-pacis)\n " + "   (foot-path centrale trastevere)\n "
			+ "   (foot-path trastevere centrale)\n " + "   (foot-path centrale trevi-fountain)\n "
			+ "   (foot-path trevi-fountain centrale)\n " + "   (foot-path pantheon trevi-fountain)\n "
			+ "   (foot-path trevi-fountain pantheon)\n " + "   (foot-path aurora trevi-fountain)\n "
			+ "   (foot-path trevi-fountain aurora)\n " + "   (foot-path aurora colosseo)\n "
			+ "   (foot-path colosseo aurora)\n " + "   (bus-path colosseo tiburtina)\n "
			+ "   (bus-path tiburtina colosseo)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled colosseo tiburtina hour5)\n " + "   (bus-scheduled colosseo tiburtina hour7)\n "
			+ "   (bus-scheduled colosseo tiburtina hour9)\n " + "   (bus-scheduled colosseo tiburtina hour11)\n "
			+ "   (bus-scheduled colosseo tiburtina hour13)\n " + "   (bus-scheduled colosseo tiburtina hour15)\n "
			+ "   (bus-scheduled colosseo tiburtina hour17)\n " + "   (bus-scheduled colosseo tiburtina hour19)\n "
			+ "   (bus-scheduled colosseo tiburtina hour21)\n " + "   (bus-scheduled colosseo tiburtina hour23)\n "
			+ "   (bus-scheduled tiburtina colosseo hour8)\n " + "   (bus-scheduled tiburtina colosseo hour10)\n "
			+ "   (bus-scheduled tiburtina colosseo hour12)\n " + "   (bus-scheduled tiburtina colosseo hour14)\n "
			+ "   (bus-scheduled tiburtina colosseo hour16)\n " + "   (bus-scheduled tiburtina colosseo hour18)\n "
			+ "   (bus-scheduled tiburtina colosseo hour20)\n " + "   (bus-scheduled tiburtina colosseo hour21)\n "
			+ "   (bus-scheduled tiburtina colosseo hour22)\n " + "   (bus-scheduled tiburtina colosseo hour24)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled colosseo tiburtina hour25)\n "
			+ "   (bus-scheduled colosseo tiburtina hour31)\n " + "   (bus-scheduled colosseo tiburtina hour34)\n "
			+ "   (bus-scheduled colosseo tiburtina hour37)\n " + "   (bus-scheduled colosseo tiburtina hour40)\n "
			+ "   (bus-scheduled colosseo tiburtina hour43)\n " + "   (bus-scheduled colosseo tiburtina hour46)\n "
			+ "   (bus-scheduled tiburtina colosseo hour32)\n " + "   (bus-scheduled tiburtina colosseo hour35)\n "
			+ "   (bus-scheduled tiburtina colosseo hour38)\n " + "   (bus-scheduled tiburtina colosseo hour41)\n "
			+ "   (bus-scheduled tiburtina colosseo hour44)\n " + "   (bus-scheduled tiburtina colosseo hour47)\n "
			+ "   (bus-path termini tiburtina)\n " + "   (bus-path tiburtina termini)\n "
			+ "   (bus-scheduled termini tiburtina hour1)\n " + "   (bus-scheduled termini tiburtina hour2)\n "
			+ "   (bus-scheduled termini tiburtina hour3)\n " + "   (bus-scheduled termini tiburtina hour4)\n "
			+ "   (bus-scheduled termini tiburtina hour5)\n " + "   (bus-scheduled termini tiburtina hour6)\n "
			+ "   (bus-scheduled termini tiburtina hour7)\n " + "   (bus-scheduled termini tiburtina hour8)\n "
			+ "   (bus-scheduled termini tiburtina hour9)\n " + "   (bus-scheduled termini tiburtina hour10)\n "
			+ "   (bus-scheduled termini tiburtina hour11)\n " + "   (bus-scheduled termini tiburtina hour12)\n "
			+ "   (bus-scheduled termini tiburtina hour13)\n " + "   (bus-scheduled termini tiburtina hour14)\n "
			+ "   (bus-scheduled termini tiburtina hour15)\n " + "   (bus-scheduled termini tiburtina hour16)\n "
			+ "   (bus-scheduled termini tiburtina hour17)\n " + "   (bus-scheduled termini tiburtina hour18)\n "
			+ "   (bus-scheduled termini tiburtina hour19)\n " + "   (bus-scheduled termini tiburtina hour20)\n "
			+ "   (bus-scheduled termini tiburtina hour21)\n " + "   (bus-scheduled termini tiburtina hour22)\n "
			+ "   (bus-scheduled termini tiburtina hour23)\n " + "   (bus-scheduled termini tiburtina hour24)\n "
			+ "   (bus-scheduled termini tiburtina hour25)\n " + "   (bus-scheduled termini tiburtina hour26)\n "
			+ "   (bus-scheduled termini tiburtina hour27)\n " + "   (bus-scheduled termini tiburtina hour28)\n "
			+ "   (bus-scheduled termini tiburtina hour29)\n " + "   (bus-scheduled termini tiburtina hour30)\n "
			+ "   (bus-scheduled termini tiburtina hour31)\n " + "   (bus-scheduled termini tiburtina hour32)\n "
			+ "   (bus-scheduled termini tiburtina hour33)\n " + "   (bus-scheduled termini tiburtina hour34)\n "
			+ "   (bus-scheduled termini tiburtina hour35)\n " + "   (bus-scheduled termini tiburtina hour36)\n "
			+ "   (bus-scheduled termini tiburtina hour37)\n " + "   (bus-scheduled termini tiburtina hour38)\n "
			+ "   (bus-scheduled termini tiburtina hour39)\n " + "   (bus-scheduled termini tiburtina hour40)\n "
			+ "   (bus-scheduled termini tiburtina hour41)\n " + "   (bus-scheduled termini tiburtina hour42)\n "
			+ "   (bus-scheduled termini tiburtina hour43)\n " + "   (bus-scheduled termini tiburtina hour44)\n "
			+ "   (bus-scheduled termini tiburtina hour45)\n " + "   (bus-scheduled termini tiburtina hour46)\n "
			+ "   (bus-scheduled termini tiburtina hour47)\n " + "   (bus-scheduled tiburtina termini hour1)\n "
			+ "   (bus-scheduled tiburtina termini hour2)\n " + "   (bus-scheduled tiburtina termini hour3)\n "
			+ "   (bus-scheduled tiburtina termini hour4)\n " + "   (bus-scheduled tiburtina termini hour5)\n "
			+ "   (bus-scheduled tiburtina termini hour6)\n " + "   (bus-scheduled tiburtina termini hour7)\n "
			+ "   (bus-scheduled tiburtina termini hour8)\n " + "   (bus-scheduled tiburtina termini hour9)\n "
			+ "   (bus-scheduled tiburtina termini hour10)\n " + "   (bus-scheduled tiburtina termini hour11)\n "
			+ "   (bus-scheduled tiburtina termini hour12)\n " + "   (bus-scheduled tiburtina termini hour13)\n "
			+ "   (bus-scheduled tiburtina termini hour14)\n " + "   (bus-scheduled tiburtina termini hour15)\n "
			+ "   (bus-scheduled tiburtina termini hour16)\n " + "   (bus-scheduled tiburtina termini hour17)\n "
			+ "   (bus-scheduled tiburtina termini hour18)\n " + "   (bus-scheduled tiburtina termini hour19)\n "
			+ "   (bus-scheduled tiburtina termini hour20)\n " + "   (bus-scheduled tiburtina termini hour21)\n "
			+ "   (bus-scheduled tiburtina termini hour22)\n " + "   (bus-scheduled tiburtina termini hour23)\n "
			+ "   (bus-scheduled tiburtina termini hour24)\n " + "   (bus-scheduled tiburtina termini hour25)\n "
			+ "   (bus-scheduled tiburtina termini hour26)\n " + "   (bus-scheduled tiburtina termini hour27)\n "
			+ "   (bus-scheduled tiburtina termini hour28)\n " + "   (bus-scheduled tiburtina termini hour29)\n "
			+ "   (bus-scheduled tiburtina termini hour30)\n " + "   (bus-scheduled tiburtina termini hour31)\n "
			+ "   (bus-scheduled tiburtina termini hour32)\n " + "   (bus-scheduled tiburtina termini hour33)\n "
			+ "   (bus-scheduled tiburtina termini hour34)\n " + "   (bus-scheduled tiburtina termini hour35)\n "
			+ "   (bus-scheduled tiburtina termini hour36)\n " + "   (bus-scheduled tiburtina termini hour37)\n "
			+ "   (bus-scheduled tiburtina termini hour38)\n " + "   (bus-scheduled tiburtina termini hour39)\n "
			+ "   (bus-scheduled tiburtina termini hour40)\n " + "   (bus-scheduled tiburtina termini hour41)\n "
			+ "   (bus-scheduled tiburtina termini hour42)\n " + "   (bus-scheduled tiburtina termini hour43)\n "
			+ "   (bus-scheduled tiburtina termini hour44)\n " + "   (bus-scheduled tiburtina termini hour45)\n "
			+ "   (bus-scheduled tiburtina termini hour46)\n " + "   (bus-scheduled tiburtina termini hour47)\n "
			+ "   (bus-path termini auditorium)\n " + "   (bus-path auditorium termini)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled termini auditorium hour8)\n " + "   (bus-scheduled termini auditorium hour10)\n "
			+ "   (bus-scheduled termini auditorium hour12)\n " + "   (bus-scheduled termini auditorium hour14)\n "
			+ "   (bus-scheduled termini auditorium hour16)\n " + "   (bus-scheduled termini auditorium hour18)\n "
			+ "   (bus-scheduled termini auditorium hour20)\n " + "   (bus-scheduled auditorium termini hour8)\n "
			+ "   (bus-scheduled auditorium termini hour10)\n " + "   (bus-scheduled auditorium termini hour12)\n "
			+ "   (bus-scheduled auditorium termini hour14)\n " + "   (bus-scheduled auditorium termini hour16)\n "
			+ "   (bus-scheduled auditorium termini hour18)\n " + "   (bus-scheduled auditorium termini hour20)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled termini auditorium hour32)\n "
			+ "   (bus-scheduled termini auditorium hour34)\n " + "   (bus-scheduled termini auditorium hour37)\n "
			+ "   (bus-scheduled termini auditorium hour40)\n " + "   (bus-scheduled termini auditorium hour44)\n "
			+ "   (bus-scheduled auditorium termini hour32)\n " + "   (bus-scheduled auditorium termini hour35)\n "
			+ "   (bus-scheduled auditorium termini hour38)\n " + "   (bus-scheduled auditorium termini hour41)\n "
			+ "   (bus-scheduled auditorium termini hour44)\n " + "   (bus-path termini pantheon)\n "
			+ "   (bus-path pantheon termini)\n " + "   ;Saturday\n " + "   (bus-scheduled termini pantheon hour8)\n "
			+ "   (bus-scheduled termini pantheon hour11)\n " + "   (bus-scheduled termini pantheon hour14)\n "
			+ "   (bus-scheduled termini pantheon hour17)\n " + "   (bus-scheduled termini pantheon hour20)\n "
			+ "   (bus-scheduled pantheon termini hour8)\n " + "   (bus-scheduled pantheon termini hour11)\n "
			+ "   (bus-scheduled pantheon termini hour14)\n " + "   (bus-scheduled pantheon termini hour17)\n "
			+ "   (bus-scheduled pantheon termini hour20)\n " + "   ;Sunday\n "
			+ "   (bus-scheduled termini pantheon hour32)\n " + "   (bus-scheduled termini pantheon hour36)\n "
			+ "   (bus-scheduled termini pantheon hour40)\n " + "   (bus-scheduled termini pantheon hour44)\n "
			+ "   (bus-scheduled pantheon termini hour32)\n " + "   (bus-scheduled pantheon termini hour36)\n "
			+ "   (bus-scheduled pantheon termini hour40)\n " + "   (bus-scheduled pantheon termini hour44)\n "
			+ "   (bus-path colosseo trevi-fountain)\n " + "   (bus-path trevi-fountain colosseo)\n "
			+ "   ;Saturday\n " + "   (bus-scheduled colosseo trevi-fountain hour8)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour11)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour14)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour17)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour20)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour24)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour8)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour11)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour14)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour17)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour20)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour24)\n " + "   ;Sunday\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour32)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour36)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour40)\n "
			+ "   (bus-scheduled colosseo trevi-fountain hour44)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour32)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour36)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour40)\n "
			+ "   (bus-scheduled trevi-fountain colosseo hour44)\n " + "   (bus-path stadio-olimpico tiburtina)\n "
			+ "   (bus-path tiburtina stadio-olimpico)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour5)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour7)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour9)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour11)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour13)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour15)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour17)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour19)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour21)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour23)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour5)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour7)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour9)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour11)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour13)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour15)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour17)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour19)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour21)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour23)\n " + "   ;Sunday\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour25)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour29)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour32)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour35)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour38)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour41)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour44)\n "
			+ "   (bus-scheduled stadio-olimpico tiburtina hour47)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour25)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour29)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour32)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour35)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour38)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour41)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour44)\n "
			+ "   (bus-scheduled tiburtina stadio-olimpico hour47)\n " + "   (bus-path san-pietro ara-pacis)\n "
			+ "   (bus-path ara-pacis san-pietro)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour7)\n " + "   (bus-scheduled san-pietro ara-pacis hour10)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour13)\n " + "   (bus-scheduled san-pietro ara-pacis hour16)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour19)\n " + "   (bus-scheduled ara-pacis san-pietro hour7)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour10)\n " + "   (bus-scheduled ara-pacis san-pietro hour13)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour16)\n " + "   (bus-scheduled ara-pacis san-pietro hour19)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled san-pietro ara-pacis hour31)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour35)\n " + "   (bus-scheduled san-pietro ara-pacis hour39)\n "
			+ "   (bus-scheduled san-pietro ara-pacis hour43)\n " + "   (bus-scheduled ara-pacis san-pietro hour31)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour35)\n " + "   (bus-scheduled ara-pacis san-pietro hour39)\n "
			+ "   (bus-scheduled ara-pacis san-pietro hour43)\n " + "   (bus-path trastevere ara-pacis)\n "
			+ "   (bus-path ara-pacis trastevere)\n " + "   ;Saturday\n "
			+ "   (bus-scheduled trastevere ara-pacis hour7)\n " + "   (bus-scheduled trastevere ara-pacis hour10)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour13)\n " + "   (bus-scheduled trastevere ara-pacis hour16)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour19)\n " + "   (bus-scheduled trastevere ara-pacis hour22)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour7)\n " + "   (bus-scheduled ara-pacis trastevere hour10)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour13)\n " + "   (bus-scheduled ara-pacis trastevere hour16)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour19)\n " + "   (bus-scheduled ara-pacis trastevere hour22)\n "
			+ "   ;Sunday\n " + "   (bus-scheduled trastevere ara-pacis hour25)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour26)\n " + "   (bus-scheduled trastevere ara-pacis hour27)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour31)\n " + "   (bus-scheduled trastevere ara-pacis hour35)\n "
			+ "   (bus-scheduled trastevere ara-pacis hour39)\n " + "   (bus-scheduled trastevere ara-pacis hour43)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour25)\n " + "   (bus-scheduled ara-pacis trastevere hour26)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour27)\n " + "   (bus-scheduled ara-pacis trastevere hour31)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour35)\n " + "   (bus-scheduled ara-pacis trastevere hour39)\n "
			+ "   (bus-scheduled ara-pacis trastevere hour43)\n " + "   (can-sleep aurora)\n "
			+ "   (can-sleep centrale)\n " + "   (can-sleep europa)\n "
			+ "   (train-station termini)\n "
			+ "   (train-station tiburtina)\n " + "   (opening-hours colosseo hour9 hour18)\n "
			+ "   (opening-hours colosseo hour38 hour42)\n " + "   (opening-hours ara-pacis hour14 hour18)\n "
			+ "   (opening-hours ara-pacis hour33 hour37)\n " + "   (opening-hours san-pietro hour9 hour18)\n "
			+ "   (opening-hours san-pietro hour33 hour42)\n " + "   (opening-hours pantheon hour9 hour13)\n "
			+ "   (opening-hours pantheon hour38 hour42)\n " 
                        + "   (opening-hours pantheon hour38 hour42)\n " 
                        + "   (opening-hours trastevere hour9 hour26)\n "
			+ "   (opening-hours auditorium hour9 hour24)\n " 
                        + "   (opening-hours stadio-olimpico hour10 hour24)\n "
                        + "   (opening-hours trastevere hour33 hour46)\n "
			+ "   (opening-hours auditorium hour33 hour46)\n " 
                        + "   (opening-hours stadio-olimpico hour34 hour46)\n "
                        + "   (breakfast hour31 hour35)\n ";
	
	String problemString;
	ArrayList<Goal> goals;
	ArrayList<String> beliefs;
	ArrayList<TrainTrip> trainTrips;
	int sleepHour;
	int level;
	
	public Problem(int level) {
		this.goals = new ArrayList<Goal>();
		this.beliefs = new ArrayList<String>();
		this.level = level;
		// Initialize beliefs
		// These are the beliefs which will always be present at the beginning of every problem, but can be modified by actions
		beliefs.add("(at-bologna)");
		// Set all hours as "future"
		// Beginning from hour7 (tweak to make opening hours work)
		// Also generate activity-available at all hours for: colosseo, pantheon, san-pietro, ara-pacis
		// if difficulty of problem is easy, generate scheduled buses for every hour
		for (int i=1; i <=48; i++) {
			beliefs.add("(future hour" + String.valueOf(i) + ")");
                        if(i<=46){
			beliefs.add("(activity-available colosseo hour" + String.valueOf(i) + ")");
			beliefs.add("(activity-available pantheon hour" + String.valueOf(i) + ")");
			beliefs.add("(activity-available san-pietro hour" + String.valueOf(i) + ")");
			beliefs.add("(activity-available ara-pacis hour" + String.valueOf(i) + ")");}
			
                        if (level < 4) {
				beliefs.add("(bus-scheduled termini auditorium hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled auditorium termini hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled termini pantheon hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled pantheon termini hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled colosseo trevi-fountain hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled trevi-fountain colosseo hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled san-pietro ara-pacis hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled ara-pacis san-pietro hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled trastevere ara-pacis hour" + String.valueOf(i) + ")");
				beliefs.add("(bus-scheduled ara-pacis trastevere hour" + String.valueOf(i) + ")");
			}
		}
		trainTrips = new ArrayList<TrainTrip>();
	}
	
	public void generateProblemString() {
                boolean sleepTardi=false;
		// Add activity-available beliefs for timed activities
		for (int i=0; i < goals.size(); i++) {
			switch(goals.get(i).getId()){
				case 14:
					beliefs.add("(activity-available trastevere hour12)");
					break;
				case 15:
					beliefs.add("(activity-available trastevere hour13)");
					break;
				case 16:
					beliefs.add("(activity-available trastevere hour36)");
					break;
				case 17:
					beliefs.add("(activity-available trastevere hour37)");
					break;
				case 18:
					beliefs.add("(activity-available trastevere hour20)");
					break;
				case 19:
					beliefs.add("(activity-available trastevere hour21)");
                                        sleepTardi=true;
					break;
				case 20:
					beliefs.add("(activity-available trastevere hour18)");
                                        sleepTardi=true;
					break;
				case 21:
					beliefs.add("(activity-available trastevere hour19)");
					break;
				case 22:
					beliefs.add("(activity-available trastevere hour34)");
					break;
				case 23:
					beliefs.add("(activity-available trastevere hour35)");
					break;
				case 24:
					beliefs.add("(activity-available stadio-olimpico hour15)");
					break;
				case 25:
					beliefs.add("(activity-available stadio-olimpico hour12)");
					break;
				case 26:
					beliefs.add("(activity-available stadio-olimpico hour39)");
					break;
				case 27:
					beliefs.add("(activity-available stadio-olimpico hour38)");
					break;
				case 28:
					beliefs.add("(activity-available auditorium hour20)");
                                        sleepTardi=true;
					break;
				case 29:
					beliefs.add("(activity-available auditorium hour18)");
					break;
				case 30:
					beliefs.add("(activity-available auditorium hour42)");
					break;
				case 31:
					beliefs.add("(activity-available auditorium hour39)");
					break;
				default:
					break;
			}
		}
		// Generate string
		String problemString;
		if (level < 4) {
			problemString = PROBLEM_INIT_EASY;	
		} else if (level < 7) {
			problemString = PROBLEM_INIT_MEDIUM;
		} else {
			problemString = PROBLEM_INIT;
		}

		//Add beliefs
		for (int i=0; i < beliefs.size(); i++) {
			problemString += beliefs.get(i) + "\n";
		}
		// Add sleep-hour
		problemString += "(sleep-time hour" + String.valueOf(sleepHour) + ")\n";
		// Add train trips
		for (int i=0; i < trainTrips.size(); i++) {
			problemString += trainTrips.get(i).getPddlString() + "\n";
		}
		problemString += ") \n";
		// Add goals
		problemString += "(:goal (and \n ";
		for (int i=0; i < goals.size(); i++) {
			problemString += goals.get(i).getPddlString() +"\n";
		}
		problemString += ")))";
		this.problemString = problemString;
	}
	
	public void updateProblemString(ArrayList<String> userActionsStrings) {
		// Create list of Action objects
		ArrayList<Action> userActions = new ArrayList<Action>();
		for (String actionString : userActionsStrings) {
			userActions.add(createActionFromString(actionString));
		}
		
		for (Action action: userActions) {
			applyAction(action);
		}
		generateProblemString();
		
	}
	
	private void applyAction(Action action) {
		action.generateEffects();
		for (String effect : action.getEffects()) {
		    if (isNegated(effect)) {
		    	// Remove predicate from beliefs
	    		// example effect: (not(future hour14))
	    		// example belief: (future hour14)
	    		String negatedEffect = effect.substring(4,effect.length()-1).trim();
	    		for (int i = 0; i < beliefs.size(); i++) {
		    		if (beliefs.get(i).equals(negatedEffect)) {
		    			beliefs.remove(i);
		    		}
	    		}
		    } else {
		    	//Add predicate to beliefs
		    	beliefs.add(effect);
		    }
		}
	}
	
	private Action createActionFromString(String actionString) {
		// Remove parenthesis
		String actionParams = actionString.replaceAll("[()]", "");
		String[] params = actionParams.split(" ");
		if (actionString.contains("book-train-round-trip")) {
			System.out.println("Creating a book-train-round-trip action...");
			// (book-train-round-trip ?station outward-departure ?outward-middle-hour ?outward-arrival ?return-departure ?return-middle-hour ?return-arrival)		
			return(new BookTrainRoundTrip(params[1],params[2], params[3], params[4], params[5], params[6], params[7]));
		} else if (actionString.contains("book-hotel")) {
			System.out.println("Creating a book-hotel action...");
			// (book-hotel ?p)
			return(new BookHotel(params[1]));
		} else if (actionString.contains("travel-by-foot") || actionString.contains("travel-by-bus")) {
			System.out.println("Creating a travel action...");
			// (travel-by-bus tiburtina stadio-olimpico hour15 hour16)
			return(new Travel(params[3], params[4], params[1], params[2]));
		} else if (actionString.contains("wait")) {
			System.out.println("Creating a wait action...");
			// (wait tiburtina hour15 hour16)
			return(new Wait(params[1], params[2], params[3]));
		} else if (actionString.contains("return-home")) {
			//(return-home tiburtina hour44 hour45 hour46)
			System.out.println("Creating a return-home action...");
			return(new ReturnHome(params[1], params[2], params[3], params[4]));
		} else if (actionString.contains("sleep")) {
			//(sleep europa hour23 hour24 hour25 hour26 hour27 hour28 hour29 hour30 hour31)
			//(sleep-short europa hour23 hour24 hour25 hour26 hour27)
			System.out.println("Creating a sleep action...");
			return(new Sleep(params[1], params[2], params[params.length-1]));
		} else if (actionString.contains("have-breakfast")) {
			//have-breakfast europa hour32 hour33 hour31 hour35)
			System.out.println("Creating a breakfast action...");
			return(new HaveBreakfast(params[1], params[2], params[3]));
		} else if (actionString.contains("do-activity")) {
			//(do-activity stadio-olimpico hour20 hour21 hour22 hour1 hour48)
			System.out.println("Creating a do-activity action...");
			return(new DoActivity(params[1], params[2], params[3], params[4]));
		} else if (actionString.contains("go-running")) {
			System.out.println("Creating a go-running action...");
			// (go-running ?p - place ?hour1 ?hour2 ?start-breakfast ?end-breakfast - time)
			return(new GoRunning(params[1], params[2], params[3]));
		}
		// Hopefully we will never get here
		return null;
	}
	
	private boolean isNegated(String effect) {
		// Returns true if effect is the negation of a string, false otherwise
		boolean result = effect.contains("not") ? true : false;
		return result;
	}
	
	public ArrayList<TrainTrip> getTrainTrips() {
		return trainTrips;
	}
	public void setTrainTrips(ArrayList<TrainTrip> trainTrips) {
		this.trainTrips = trainTrips;
	}
	
	public int getSleepHour() {
		return sleepHour;
	}
	public void setSleepHour(int sleepHour) {
		this.sleepHour = sleepHour;
	}
	public String getProblemString() {
		return problemString;
	}
	
	public ArrayList<Goal> getGoals() {
		return goals;
	}
	public void setGoals(ArrayList<Goal> goals) {
		this.goals = goals;
	}

	public String getNextActionUserFriendlyString(String actionString) {
		Action nextAction = createActionFromString(actionString);
		return nextAction.getHintString();
		
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
}
