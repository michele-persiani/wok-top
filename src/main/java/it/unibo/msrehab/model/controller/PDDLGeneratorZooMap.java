package it.unibo.msrehab.model.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.heuristics.relaxation.Heuristic;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.hsp.HSP;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.Plan;
import it.unibo.msrehab.model.pddl.ZooMap;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
*
* Bartolomeo Lombardi
*/

public class PDDLGeneratorZooMap
{
    
    private static final String DOMAIN_NAME = "zoo";
    private static final String PROBLEM_NAME = "zoo";
    private static final String DOMAIN_FILE = "(define (domain zoo)\n" +
            "  (:requirements :strips :equality :negative-preconditions)\n" +
            "  (:predicates\n" +
            "    (at ?thing ?place)          ; thing si trova in place\n" +
            "    (visitor ?v)                ; v è un visitatore\n" +
            "    (camel ?c)                  ; c è un cammello\n" +
            "    (riding ?visitor ?thing)    ; il visitatore sta andando su qualcosa\n" +
            "    (mobile ?thing)             ; thing è mobile (probabilmente superfluo\n" +
            "                                ; rispetto a riding ma lo lasiamo)\n" +
            "\n" +
            "    (road ?from ?to)            ; sentiero\n" +
            "    (shadow ?from ?to)          ; ombra\n" +
            "    (camelroad ?from ?to)       ; strada per cammello\n" +
            "\n" +
            "    (to-discover ?place)        ; il visitatore deve ancora peassare per questo posto\n" +
            "    (able-to-walk ?from ?to)    ; il visitatore può ancora percorrere quella strada?\n" +
            "    (able-to-climb ?visitor)    ; il visitatore può ancora salire sul cammello?\n" +
            "    (able-to-ride ?from ?to)    ; il cammello può ancora percorrere quella strada?\n" +
            "  )\n" +
            "\n" +
            "  (:action Walk\n" +
            "    :parameters (?visitor ?from ?to)\n" +
            "    :precondition (and (visitor ?visitor)\n" +
            "        (at ?visitor ?from)\n" +
            "        (mobile ?visitor)\n" +
            "        (road ?from ?to)\n" +
            "        (not (= ?from ?to))\n" +
            "        (able-to-walk ?from ?to))\n" +
            "    :effect (and (at ?visitor ?to)\n" +
            "        (not (to-discover ?to))\n" +
            "        (not (at ?visitor ?from))\n" +
            "        (not (able-to-walk ?to ?from))\n" +
            "        (not (able-to-walk ?from ?to)))\n" +
            "  )\n" +
            "\n" +
            "  (:action Shadow-Walk\n" +
            "    :parameters (?visitor ?from ?to)\n" +
            "    :precondition (and (visitor ?visitor)\n" +
            "        (at ?visitor ?from)\n" +
            "        (mobile ?visitor)\n" +
            "        (shadow ?from ?to)\n" +
            "        (not (= ?from ?to)))\n" +
            "    :effect (and (at ?visitor ?to)\n" +
            "        (not (to-discover ?to))\n" +
            "        (not (at ?visitor ?from)))\n" +
            "  )\n" +
            "\n" +
            "  (:action Get-on-Camel\n" +
            "    :parameters (?visitor ?place ?camel)\n" +
            "    :precondition (and (visitor ?visitor)\n" +
            "          (at ?visitor ?place)\n" +
            "          (camel ?camel)\n" +
            "          (at ?camel ?place)\n" +
            "          (able-to-climb ?visitor)\n" +
            "          (not (riding ?visitor ?camel))\n" +
            "          (mobile ?visitor))\n" +
            "    :effect (and (riding ?visitor ?camel)\n" +
            "        (not (to-discover ?place))\n" +
            "          (mobile ?camel)\n" +
            "          (not (at ?visitor ?place))\n" +
            "          (not (mobile ?visitor)))\n" +
            "  )\n" +
            "\n" +
            "  ; attenzione qui riding non c'è\n" +
            "  (:action Ride-Camel\n" +
            "    :parameters (?visitor ?camel ?from ?to)\n" +
            "    :precondition (and (camelroad ?from ?to)\n" +
            "          (at ?camel ?from)\n" +
            "          (visitor ?visitor)\n" +
            "          (mobile ?camel)\n" +
            "          (riding ?visitor ?camel)\n" +
            "          (able-to-ride ?from ?to)\n" +
            "          (not (= ?from ?to)))\n" +
            "    :effect (and (at ?camel ?to)\n" +
            "          (not (to-discover ?to))\n" +
            "          (not (at ?camel ?from))\n" +
            "          (not (able-to-ride ?from ?to))\n" +
            "          (not (able-to-ride ?to ?from)))\n" +
            "  )\n" +
            "\n" +
            "  (:action Get-off-Camel\n" +
            "    :parameters (?visitor ?place ?camel)\n" +
            "    :precondition (and (visitor ?visitor)\n" +
            "          (camel ?camel)\n" +
            "          (riding ?visitor ?camel)\n" +
            "          (at ?camel ?place))\n" +
            "    :effect (and (at ?visitor ?place)\n" +
            "          (mobile ?visitor)\n" +
            "          (not (to-discover ?place))\n" +
            "          (not (able-to-climb ?visitor))\n" +
            "          (not (riding ?visitor ?camel))\n" +
            "          (not (mobile ?camel)))\n" +
            "  )\n" +
            ")";

    public static int[][] easyMapSafariZoo(List<String> listGoals, StringBuilder inFinalPlace, List<String> parameters, int targetGoal){
        //targetGoal                             // target visitabili

        //listGoals  --target da visitare
        //parameters --azioni da fare per calcolare lo score

        List<String> places = new ArrayList<>();        // posti visibili sulla mappa
        Map<String, Integer> animal = new HashMap<>();  // bind con gli animali del piano			
        Map<Integer, Point> nodes = new HashMap<>();    // punti degli animali da posizionare su gameMap

        int[][] gameMap =
        {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
            {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
            {0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,1,0,0,0,1,1,0,0,0,5,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,1,1,1,0,0,0,1,0,0,0,0,5,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,5,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,5,1,1,1,1,1,0,0},
            {0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,1,0,0},
            {0,0,1,1,1,1,1,1,1,0,1,1,1,0,0,0,0,5,0,0,0,0,1,0,0},
            {0,0,1,0,0,0,1,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0},
            {0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0},
            {0,0,0,0,0,0,1,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,1,0,0},
            {0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        };


        places.addAll(Arrays.asList
                        ("lama", "bar", 
                        "orso", "leone",
                        "elefante", "ippopotamo",
                        "tigre", "scimmia",
                        "uccello", "scoiattolo", 
                        "serpente", "toilette"));

        int randFinal = new Random().nextInt(places.size());
        inFinalPlace.append(places.get(randFinal));

        places.remove(randFinal);

        int inFinalIndex = 19 + new Random().nextInt(22 - 19 + 1);

        for (int i= 0; i < targetGoal; i++)
        {
            int place = new Random().nextInt(places.size());
            listGoals.add(places.get(place));
            places.remove(place);
        } 

        animal.put("orso", 6);
        animal.put("tigre", 7);
        animal.put("scimmia", 8);
        animal.put("leone", 9);
        animal.put("elefante", 10);
        animal.put("ippopotamo", 11);
        animal.put("lama", 12);
        animal.put("serpente", 13);
        animal.put("uccello", 14);
        animal.put("scoiattolo", 15);		
        animal.put("bar", 16);
        animal.put("toilette", 17);

        nodes.put(11, new Point(17,16));
        nodes.put(12, new Point(11,17));
        nodes.put(13, new Point(13,8));
        nodes.put(14, new Point(11,12));
        nodes.put(15, new Point(13,2));
        nodes.put(16, new Point(11,22));
        nodes.put(17, new Point(6,2));
        nodes.put(18, new Point(5,14));
        nodes.put(19, new Point(1,17));
        nodes.put(20, new Point(1,8));
        nodes.put(21, new Point(3,1));
        nodes.put(22, new Point(3,23));


        boolean isPlanned = false;
        final ProblemFactory factory = new ProblemFactory();

        do {
            String PROBLEM_FILE = "";

            ZooMap map = new ZooMap();

            map.setArrival(inFinalPlace.toString(), inFinalIndex);
            map.addPlacesToVisit(listGoals);
            map.generateConfiguration("easy");

            PROBLEM_FILE += map.defineProblem(DOMAIN_NAME, PROBLEM_NAME);
            PROBLEM_FILE += map.getObjectsSyntaxPDDL();
            PROBLEM_FILE += map.getInitializationSyntaxPDDL();
            PROBLEM_FILE += map.getGoalSyntaxPDDL();

            // Parse the domain and the problem
            ErrorManager errorManager;
            try {
                    errorManager = factory.parseFromString(DOMAIN_FILE, PROBLEM_FILE);
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
                    System.out.println("\nGoal can be simplified to FALSE. No search will solve it");
                    System.exit(0);
            }

            // Create the planner
            final HSP planner = new HSP();
            planner.setHeuristicType(Heuristic.Type.FAST_FORWARD);
            planner.setSaveState(false);
            // Search for a solution plan
            final Plan plan = planner.search(pb);
            if (plan != null) {
                System.out.println(String.format("\n\nFound plan as follows:\n"));
                System.out.println(pb.toString(plan));
                for (int i = 0; i < plan.timeSpecifiers().size(); i++) {
                    // Actions
                    for (BitOp action : plan.getActionSet(i)) {
                        // Parameters
                        for (int j = 0; j < action.getArity(); j++) {
                            final int index = action.getValueOfParameter(j);
                            //System.out.println(action.getName());
                            if (action.getArity() == 3 && index != -1 && index != 0) {
                                //parameters.add(pb.getConstants().get(index));                                
                                if( i != 0 ) {
                                    if(parameters.get(parameters.size() - 1) != pb.getConstants().get(index))
                                        parameters.add(pb.getConstants().get(index));
                                }else { 
                                    parameters.add(pb.getConstants().get(index));
                                }
                            }
                        }
                    }
                }
                for(Map.Entry<Integer, String> entry : map.element.entrySet()) {
                        gameMap[nodes.get(entry.getKey()).x][nodes.get(entry.getKey()).y] = animal.get(entry.getValue());
                }

                isPlanned = true;
            } else {
                    System.out.append(String.format("\nNo plan found, trying another configuration...\n\n"));
            }
        } while (!isPlanned);           

        return gameMap;   
    }
    
    public static int[][] mediumMapSafariZoo(List<String> listGoals, StringBuilder inFinalPlace, List<String> parameters, int targetGoal){
    //targetGoal;                             // target visitabili

    //listGoals  --target da visitare
    //parameters --azioni da fare per calcolare lo score

    List<String> places = new ArrayList<>();        // posti visibili sulla mappa
    Map<String, Integer> animal = new HashMap<>();  // bind con gli animali del piano			
    Map<Integer, Point> nodes = new HashMap<>();    // punti degli animali da posizionare su gameMap

    int[][] gameMap =
    {
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
        {0,0,1,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,0,0},
        {0,0,2,0,0,0,0,0,2,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,0,0,2,0,0,0,1,1,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,0,2,2,2,2,2,2,0,0,0,1,0,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,2,2,2,2,2,2,1,2,2,2,2,5,2,2,2,2,2,0,0},
        {0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,2,0,0},
        {0,0,2,2,2,2,1,1,1,0,1,1,1,0,0,0,0,5,0,0,0,0,2,0,0},
        {0,0,2,0,0,0,2,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,2,0,0},
        {0,0,0,2,2,2,1,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,2,0,0},
        {0,0,0,0,0,0,2,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,2,0,0},
        {0,0,0,0,0,0,2,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,2,0,0},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    };


    places.addAll(Arrays.asList
                    ("lama", "bar", 
                    "orso", "leone",
                    "elefante", "ippopotamo",
                    "tigre", "scimmia",
                    "uccello", "scoiattolo", 
                    "serpente", "toilette"));

    int randFinal = new Random().nextInt(places.size());
    inFinalPlace.append(places.get(randFinal));

    places.remove(randFinal);

    int inFinalIndex = 19 + new Random().nextInt(22 - 19 + 1);

    for (int i= 0; i < targetGoal; i++)
    {
        int place = new Random().nextInt(places.size());
        listGoals.add(places.get(place));
        places.remove(place);
    } 

    animal.put("orso", 6);
    animal.put("tigre", 7);
    animal.put("scimmia", 8);
    animal.put("leone", 9);
    animal.put("elefante", 10);
    animal.put("ippopotamo", 11);
    animal.put("lama", 12);
    animal.put("serpente", 13);
    animal.put("uccello", 14);
    animal.put("scoiattolo", 15);		
    animal.put("bar", 16);
    animal.put("toilette", 17);

    nodes.put(11, new Point(17,16));
    nodes.put(12, new Point(11,17));
    nodes.put(13, new Point(13,8));
    nodes.put(14, new Point(11,12));
    nodes.put(15, new Point(13,2));
    nodes.put(16, new Point(11,22));
    nodes.put(17, new Point(6,2));
    nodes.put(18, new Point(5,14));
    nodes.put(19, new Point(1,17));
    nodes.put(20, new Point(1,8));
    nodes.put(21, new Point(3,1));
    nodes.put(22, new Point(3,23));


    boolean isPlanned = false;
    final ProblemFactory factory = new ProblemFactory();

    do {
        String PROBLEM_FILE = "";

        ZooMap map = new ZooMap();

        map.setArrival(inFinalPlace.toString(), inFinalIndex);
        map.addPlacesToVisit(listGoals);
        map.generateConfiguration("medium");

        PROBLEM_FILE += map.defineProblem(DOMAIN_NAME, PROBLEM_NAME);
        PROBLEM_FILE += map.getObjectsSyntaxPDDL();
        PROBLEM_FILE += map.getInitializationSyntaxPDDL();
        PROBLEM_FILE += map.getGoalSyntaxPDDL();

        // Parse the domain and the problem
        ErrorManager errorManager;
        try {
                errorManager = factory.parseFromString(DOMAIN_FILE, PROBLEM_FILE);
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
                System.out.println("\nGoal can be simplified to FALSE. No search will solve it");
                System.exit(0);
        }

        // Create the planner
        final HSP planner = new HSP();
        planner.setHeuristicType(Heuristic.Type.FAST_FORWARD);
        planner.setSaveState(false);
        // Search for a solution plan
        final Plan plan = planner.search(pb);
        if (plan != null) {
            System.out.println(String.format("\n\nFound plan as follows:\n"));
            System.out.println(pb.toString(plan));
            for (int i = 0; i < plan.timeSpecifiers().size(); i++) {
                // Actions
                for (BitOp action : plan.getActionSet(i)) {
                    // Parameters
                    for (int j = 0; j < action.getArity(); j++) {
                        final int index = action.getValueOfParameter(j);
                        //System.out.println(action.getName());
                        if (action.getArity() == 3 && index != -1 && index != 0) {
                            //parameters.add(pb.getConstants().get(index));                                
                            if( i != 0 ) {
                                if(parameters.get(parameters.size() - 1) != pb.getConstants().get(index))
                                    parameters.add(pb.getConstants().get(index));
                            }else { 
                                parameters.add(pb.getConstants().get(index));
                            }
                        }
                    }
                }
            }
            for(Map.Entry<Integer, String> entry : map.element.entrySet()) {
                    gameMap[nodes.get(entry.getKey()).x][nodes.get(entry.getKey()).y] = animal.get(entry.getValue());
            }

            isPlanned = true;
        } else {
                System.out.append(String.format("\nNo plan found, trying another configuration...\n\n"));
        }
    } while (!isPlanned);           

    return gameMap;   
}
    
    public static int[][] difficultMapSafariZoo(List<String> listGoals, StringBuilder inFinalPlace, List<String> parameters, int targetGoal){
    //targetGoal  --target visitabili
    //listGoals  --target da visitare
    //parameters --azioni da fare per calcolare lo score

    List<String> places = new ArrayList<>();        // posti visibili sulla mappa
    Map<String, Integer> animal = new HashMap<>();  // bind con gli animali del piano			
    Map<Integer, Point> nodes = new HashMap<>();    // punti degli animali da posizionare su gameMap

    int[][] gameMap =
    {
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
        {0,0,1,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,0,0},
        {0,0,2,0,0,0,0,0,2,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,0,0,2,0,0,0,1,1,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,0,2,2,2,2,2,2,0,0,0,1,0,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,5,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,2,2,2,2,2,2,1,2,2,2,2,5,2,2,2,2,2,0,0},
        {0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,2,0,0},
        {0,0,2,2,2,2,1,1,1,0,1,1,1,0,0,0,0,5,0,0,0,0,2,0,0},
        {0,0,2,0,0,0,2,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,2,0,0},
        {0,0,0,2,2,2,1,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,2,0,0},
        {0,0,0,0,0,0,2,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,2,0,0},
        {0,0,0,0,0,0,2,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,2,0,0},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    };


    places.addAll(Arrays.asList
                    ("lama", "bar", 
                    "orso", "leone",
                    "elefante", "ippopotamo",
                    "tigre", "scimmia",
                    "uccello", "scoiattolo", 
                    "serpente", "toilette"));

    int randFinal = new Random().nextInt(places.size());
    inFinalPlace.append(places.get(randFinal));

    places.remove(randFinal);

    int inFinalIndex = 19 + new Random().nextInt(22 - 19 + 1);

    for (int i= 0; i < targetGoal; i++)
    {
        int place = new Random().nextInt(places.size());
        listGoals.add(places.get(place));
        places.remove(place);
    } 

    animal.put("orso", 6);
    animal.put("tigre", 7);
    animal.put("scimmia", 8);
    animal.put("leone", 9);
    animal.put("elefante", 10);
    animal.put("ippopotamo", 11);
    animal.put("lama", 12);
    animal.put("serpente", 13);
    animal.put("uccello", 14);
    animal.put("scoiattolo", 15);		
    animal.put("bar", 16);
    animal.put("toilette", 17);

    nodes.put(11, new Point(17,16));
    nodes.put(12, new Point(11,17));
    nodes.put(13, new Point(13,8));
    nodes.put(14, new Point(11,12));
    nodes.put(15, new Point(13,2));
    nodes.put(16, new Point(11,22));
    nodes.put(17, new Point(6,2));
    nodes.put(18, new Point(5,14));
    nodes.put(19, new Point(1,17));
    nodes.put(20, new Point(1,8));
    nodes.put(21, new Point(3,1));
    nodes.put(22, new Point(3,23));


    boolean isPlanned = false;
    final ProblemFactory factory = new ProblemFactory();

    do {
        String PROBLEM_FILE = "";

        ZooMap map = new ZooMap();

        map.setArrival(inFinalPlace.toString(), inFinalIndex);
        map.addPlacesToVisit(listGoals);
        map.generateConfiguration("difficult");

        PROBLEM_FILE += map.defineProblem(DOMAIN_NAME, PROBLEM_NAME);
        PROBLEM_FILE += map.getObjectsSyntaxPDDL();
        PROBLEM_FILE += map.getInitializationSyntaxPDDL();
        PROBLEM_FILE += map.getGoalSyntaxPDDL();

        // Parse the domain and the problem
        ErrorManager errorManager;
        try {
                errorManager = factory.parseFromString(DOMAIN_FILE, PROBLEM_FILE);
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
                System.out.println("\nGoal can be simplified to FALSE. No search will solve it");
                System.exit(0);
        }

        // Create the planner
        final HSP planner = new HSP();
        planner.setHeuristicType(Heuristic.Type.FAST_FORWARD);
        planner.setSaveState(false);
        // Search for a solution plan
        final Plan plan = planner.search(pb);
        if (plan != null) {
            System.out.println(String.format("\n\nFound plan as follows:\n"));
            System.out.println(pb.toString(plan));
            for (int i = 0; i < plan.timeSpecifiers().size(); i++) {
                // Actions
                for (BitOp action : plan.getActionSet(i)) {
                    // Parameters
                    for (int j = 0; j < action.getArity(); j++) {
                        final int index = action.getValueOfParameter(j);
                        //System.out.println(action.getName());
                        if (action.getArity() == 3 && index != -1 && index != 0) {
                            //parameters.add(pb.getConstants().get(index));                                
                            if( i != 0 ) {
                                if(parameters.get(parameters.size() - 1) != pb.getConstants().get(index))
                                    parameters.add(pb.getConstants().get(index));
                            }else { 
                                parameters.add(pb.getConstants().get(index));
                            }
                        }
                    }
                }
            }
            for(Map.Entry<Integer, String> entry : map.element.entrySet()) {
                    gameMap[nodes.get(entry.getKey()).x][nodes.get(entry.getKey()).y] = animal.get(entry.getValue());
            }

            isPlanned = true;
        } else {
                System.out.append(String.format("\nNo plan found, trying another configuration...\n\n"));
        }
    } while (!isPlanned);           

    return gameMap;   
}
    
    
    // test hard mappa dello zoo versione BADS 
//    public static int[][] hardMapZoo(List<String> listGoals, StringBuilder inFinalPlace, List<String> parameters) {
//
//        int targetGoal = 6;                             // target visitabili
//
//        //listGoals  --target da visitare
//        //parameters --azioni da fare per calcolare lo score
//
//        List<String> places = new ArrayList<>();        // posti visibili sulla mappa
//        Map<String, Integer> animal = new HashMap<>();  // bind con gli animali del piano			
//        Map<Integer, Point> nodes = new HashMap<>();    // punti degli animali da posizionare su gameMap
//
//        int[][] gameMap =
//        {
//            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {3, 0, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
//            {0, 0, 2, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
//            {0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
//            {0, 0, 1, 1, 1, 2, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0},
//            {0, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0},
//            {0, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 1, 1, 0},
//            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 0, 0},
//            {4, 0, 1, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 4, 0},
//            {3, 0, 1, 0, 4, 0, 0, 0, 0, 3, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 4, 0, 3, 0},
//            {1, 1, 1, 0, 3, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 1, 1, 2, 0, 0, 2, 3, 0, 0, 0},
//            {0, 4, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 1, 1, 2, 0},
//            {4, 3, 2, 0, 0, 0, 2, 4, 0, 1, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 2, 0, 0, 4, 0},
//            {3, 0, 2, 0, 0, 0, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 3, 0},
//            {0, 0, 2, 4, 2, 2, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 2, 0, 4, 0, 2, 4, 0, 0, 0},
//            {0, 4, 2, 3, 2, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 3, 0, 2, 3, 0, 0, 0},
//            {0, 3, 2, 2, 2, 0, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 0},
//            {4, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}	
//        };
//
//
//        places.addAll(Arrays.asList
//                        ("lama", "bar", 
//                        "orso", "leone",
//                        "elefante", "ippopotamo",
//                        "tigre", "scimmia",
//                        "uccello", "scoiattolo", 
//                        "serpente", "toilette"));
//
//        int randFinal = new Random().nextInt(places.size());
//        inFinalPlace.append(places.get(randFinal));
//
//        places.remove(randFinal);
//
//        int inFinalIndex = 19 + new Random().nextInt(22 - 19 + 1);
//
//        for (int i= 0; i < targetGoal; i++)
//        {
//            int place = new Random().nextInt(places.size());
//            listGoals.add(places.get(place));
//            places.remove(place);
//        } 
//
//        animal.put("orso", 6);
//        animal.put("tigre", 7);
//        animal.put("scimmia", 8);
//        animal.put("leone", 9);
//        animal.put("elefante", 10);
//        animal.put("ippopotamo", 11);
//        animal.put("lama", 12);
//        animal.put("serpente", 13);
//        animal.put("uccello", 14);
//        animal.put("scoiattolo", 15);		
//        animal.put("bar", 16);
//        animal.put("toilette", 17);
//
//        nodes.put(11, new Point(4,5));
//        nodes.put(12, new Point(7,5));
//        nodes.put(13, new Point(12,6));
//        nodes.put(14, new Point(10,9));
//        nodes.put(15, new Point(17,7));
//        nodes.put(16, new Point(4,14));
//        nodes.put(17, new Point(17,11));
//        nodes.put(18, new Point(10,17));
//        nodes.put(19, new Point(4,23));
//        nodes.put(20, new Point(11,23));
//        nodes.put(21, new Point(16,23));
//        nodes.put(22, new Point(1,20));
//
//
//        boolean isPlanned = false;
//        final ProblemFactory factory = new ProblemFactory();
//
//        do {
//            String PROBLEM_FILE = "";
//
//            ZooMap map = new ZooMap();
//
//            map.setArrival(inFinalPlace.toString(), inFinalIndex);
//            map.addPlacesToVisit(listGoals);
//            map.generateConfiguration("difficult");
//
//            PROBLEM_FILE += map.defineProblem(DOMAIN_NAME, PROBLEM_NAME);
//            PROBLEM_FILE += map.getObjectsSyntaxPDDL();
//            PROBLEM_FILE += map.getInitializationSyntaxPDDL();
//            PROBLEM_FILE += map.getGoalSyntaxPDDL();
//
//            // Parse the domain and the problem
//            ErrorManager errorManager;
//            try {
//                    errorManager = factory.parse(DOMAIN_FILE, PROBLEM_FILE);
//                    if (!errorManager.isEmpty()) {
//                errorManager.printAll();
//                System.exit(0);
//                    }
//            } catch (IOException e1) {
//                    e1.printStackTrace();
//            }
//
//            // Encode and simplify the planning problem in a compact representation
//            final CodedProblem pb = factory.encode();
//            if (!pb.isSolvable()) {
//                    System.out.println("\nGoal can be simplified to FALSE. No search will solve it");
//                    System.exit(0);
//            }
//
//            // Create the planner
//            final HSP planner = new HSP();
//
//            // Search for a solution plan
//            final Plan plan = planner.search(pb);
//            if (plan != null) {
//                System.out.println(String.format("\n\nFound plan as follows:\n"));
//                System.out.println(pb.toString(plan));
//
//                for (int i = 0; i < plan.timeSpecifiers().size(); i++) {
//                    // Actions
//                    for (BitOp action : plan.getActionSet(i)) {
//                        // Parameters -- gestire il percorso esatto via codice
//                        for (int j = 0; j < action.getArity(); j++) {
//                            final int index = action.getValueOfParameter(j);
//                            //System.out.println(action.getArity());
//                            if (action.getArity() == 3 && index != -1 && index != 0) {
//                                        parameters.add(pb.getConstants().get(index));
//                                        if(parameters.size() != 0 && parameters.get(parameters.size() - 1) != pb.getConstants().get(index)) {
//                                                        parameters.add(pb.getConstants().get(index));
//                                        }
//                            }else if(action.getArity() == 4 && index != -1 && index != 0 && index != 1) {
//                                        parameters.add(pb.getConstants().get(index));
//
////			                    	if(parameters.size() != 0) {
////		                    			if(parameters.get(parameters.size() -1) != pb.getConstants().get(index))
////		                    				parameters.add(pb.getConstants().get(index));
////		                    		}
////		                    }else {
////		                    		parameters.add(pb.getConstants().get(index));
//                            }
//                        }
//                    }
//                }
//
//
//                for(Map.Entry<Integer, String> entry : map.element.entrySet()) {
//                        gameMap[nodes.get(entry.getKey()).x][nodes.get(entry.getKey()).y] = animal.get(entry.getValue());
//                }
//
//                isPlanned = true;
//            } else {
//                    System.out.append(String.format("\nNo plan found, trying another configuration...\n\n"));
//            }
//        } while (!isPlanned);           
//
//        return gameMap;
//    }
}
