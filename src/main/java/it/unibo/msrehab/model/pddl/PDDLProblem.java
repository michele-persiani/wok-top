/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.heuristics.relaxation.Heuristic;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.hsp.HSP;
import fr.uga.pddl4j.util.Plan;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.unibo.msrehab.model.entities.Environment;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author danger
 */
public class PDDLProblem
{

    public String JAVA_PDDL4J_PATH = "";
    public static final int MAX_EXECUTION_TIME = 10;
    private final String domain = "(define (domain mattina)\n"
            + "      (:requirements :strips :typing :equality :adl)\n"
            + "      (:types\n"
            + "          tempo item - object\n"
            + "          place - location\n"
            + "          waitplace - place\n"
            + "          vehicle - item\n"
            + "      )\n"
            + "      (:predicates\n"
            + "          (now ?t - tempo)\n"
            + "          (next ?t1 - tempo ?t2 - tempo)\n"
            + "          (past ?t - tempo)\n"
            + "          (future ?t - tempo)\n"
            + "          (be ?src - location)\n"
            + "          (path ?src - location ?dst - location)\n"
            + "          (road ?src - location ?dst - location)\n"
            + "          (have ?pos - location ?item - item)\n"
            + "          (have-in-change  ?pos - location ?item - item ?change - item)\n"
            + "          (got ?item - item)\n"
            + "          (got-in-change ?change - item ?item - item)\n"
            + "          (open-hour ?pos - location ?t - tempo ?tc - tempo)\n"
            + "          (first-number ?t - tempo)\n"
            + "          (stand-1h ?pos - waitplace)\n"
            + "          (stand-2h ?pos - waitplace)\n"
            + "          (stand-3h ?pos - waitplace)\n"
            + "          (stand-4h ?pos - waitplace)\n"
            + "          (be-at ?src - location ?t - tempo)\n"
            + "          (be-at2 ?src - location ?t - tempo)\n"
            + "          (be-at3 ?src - location ?t - tempo)\n"
            + "          (drop-at ?item - item ?pos - location ?t - tempo)\n"
            + "          (got-at ?item - item ?pos - location ?t - tempo)\n"
            + "          (can-drop ?item - item ?pos - location)\n"
            + "      )\n"
            + "\n"
            + "      (:action move-to\n"
            + "        :parameters (?src - place ?dst - place ?t0 - tempo ?t1 - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?src)\n"
            + "                        (path ?src ?dst)\n"
            + "                        (now ?t0)\n"
            + "                        (next ?t0 ?t1)\n"
            + "                        (future ?t1)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (be ?src))\n"
            + "                    (be ?dst)\n"
            + "                    (not (now ?t0))\n"
            + "                    (not (future ?t1))\n"
            + "                    (now ?t1)\n"
            + "                    (past ?t0)\n"
            + "                    (be-at ?dst ?t1)\n"
            + "                    (be-at2 ?dst ?t1)\n"
            + "                    (be-at3 ?dst ?t1)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "      (:action drive-to\n"
            + "        :parameters (?src - place ?dst - place ?vehicle - vehicle ?t - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?src)\n"
            + "                        (got ?vehicle)\n"
            + "                        (road ?src ?dst)\n"
            + "				(not (past ?t))\n"			
            + "                        (now ?t)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (be ?src))\n"
            + "                    (be ?dst)\n"
            + "                    (be-at ?dst ?t)\n"
             + "                   (be-at2 ?dst ?t)\n"
            + "                   (be-at3 ?dst ?t)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "\n"
            + "      (:action take-item-at\n"
            + "        :parameters (?pos - place ?item - item ?tp - tempo ?t - tempo ?tc - tempo ?tnow - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?pos)\n"
            + "                        (have ?pos ?item)\n"
            + "                        (open-hour ?pos ?t ?tc)\n"
            + "                        (past ?tp)\n"
            + "                        (next ?tp ?t)\n"
            + "                        (now ?tnow)\n"
            + "                        (future ?tc)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (have ?pos ?item))\n"
            + "                    (got ?item)\n"
            + "                    (got-at ?item ?pos ?tnow)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "      (:action take-item-at-now\n"
            + "        :parameters (?pos - place ?item - item ?tp - tempo ?t - tempo ?tc - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?pos)\n"
            + "                        (have ?pos ?item)\n"
            + "                        (open-hour ?pos ?t ?tc)\n"
            + "                        (past ?tp)\n"
            + "                        (next ?tp ?t)\n"
            + "                        (now ?t)\n"
            + "                        (future ?tc)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (have ?pos ?item))\n"
            + "                    (got ?item)\n"
            + "                    (got-at ?item ?pos ?t)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "      (:action change-item\n"
            + "        :parameters (?pos - place ?item - item ?change - item ?tp - tempo ?t - tempo ?tc - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?pos)\n"
            + "                        (got ?change)\n"
            + "                        (have-in-change ?pos ?item ?change)\n" +
            "						 (have ?pos ?item)\n"
            + "                        (open-hour ?pos ?t ?tc)\n"
            + "                        (past ?tp)\n"
            + "                        (next ?tp ?t)\n"
            + "                        (future ?tc)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (have-in-change ?pos ?item ?change))\n" +
            "					 (not (have ?pos ?item))\n"
            + "                    (not (got ?change))\n"
            + "                    (got ?item)\n"
            + "                    (got-in-change ?change ?item)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "      (:action drop-item-at\n"
            + "        :parameters (?pos - place ?item - item ?tp - tempo ?t - tempo ?tc - tempo ?tnow - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?pos)\n"
            + "                        (got ?item)\n"
            + "                        (open-hour ?pos ?t ?tc)\n"
            + "                        (past ?tp)\n"
            + "                        (next ?tp ?t)\n"
            + "                        (future ?tc)\n"
            + "                        (now ?tnow)\n"
            + //"                       (can-drop ?item ?pos)\n" +
            "                     )\n"
            + "        :effect (and\n"
            + "                    (have ?pos ?item)\n"
            + "                    (not (got ?item))\n"
            + "                    (drop-at ?item ?pos ?tnow)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "      (:action drop-item-at-now\n"
            + "        :parameters (?pos - place ?item - item ?tp - tempo ?t - tempo ?tc - tempo)\n"
            + "        :precondition (and\n"
            + "                        (be ?pos)\n"
            + "                        (got ?item)\n"
            + "                        (open-hour ?pos ?t ?tc)\n"
            + "                        (past ?tp)\n"
            + "                        (next ?tp ?t)\n"
            + "                        (future ?tc)\n"
            + "                        (now ?t)\n"
            + //"                        (can-drop ?item ?pos)\n" +
            "                     )\n"
            + "        :effect (and\n"
            + "                    (have ?pos ?item)\n"
            + "                    (not (got ?item))\n"
            + "                    (drop-at ?item ?pos ?t)\n"
            + "                )\n"
            + "      )\n"
            + "\n"
            + "      (:action still-1h\n"
            + "        :parameters (?pos - waitplace ?t0 - tempo ?t1 - tempo)\n"
            + "        :precondition (and\n"
            + "                        (now ?t0)\n"
            + "                        (next ?t0 ?t1)\n"
            + "                        (future ?t1)\n"
            + "                        (be ?pos)\n"
            + "\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (now ?t0))\n"
            + "                    (not (future ?t1))\n"
            + "                    (now ?t1)\n"
            + "                    (past ?t0)\n"
            + "                    (stand-1h ?pos)\n"
             + "                    (be-at ?pos ?t1)\n"
            +  "                    (be-at2 ?pos ?t1)\n"
            + "                )\n"
            + "      )\n"
            + "      (:action still-2h\n"
            + "        :parameters (?pos - waitplace ?t0 - tempo ?t1 - tempo ?t2 - tempo)\n"
            + "        :precondition (and\n"
            + "                        (now ?t0)\n"
            + "                        (next ?t0 ?t1)\n"
            + "                        (next ?t1 ?t2)\n"
            + "                        (future ?t1)\n"
            + "                        (be ?pos)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (now ?t0))\n"
            + "                    (not (future ?t1))\n"
            + "                    (not (future ?t2))\n"
            + "                    (now ?t2)\n"
            + "                    (past ?t0)\n"
            + "                    (past ?t1)\n"
            + "                    (stand-2h ?pos)\n"
            + "                    (be-at ?pos ?t1)\n"
            + "                    (be-at ?pos ?t2)\n"
            + "                )\n"
            + "      )\n"
            + "      (:action still-3h\n"
            + "        :parameters (?pos - waitplace ?t0 - tempo ?t1 - tempo ?t2 - tempo ?t3 - tempo)\n"
            + "        :precondition (and\n"
            + "                        (now ?t0)\n"
            + "                        (next ?t0 ?t1)\n"
            + "                        (next ?t1 ?t2)\n"
            + "                        (next ?t2 ?t3)\n"
            + "                        (future ?t1)\n"
            + "                        (be ?pos)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (now ?t0))\n"
            + "                    (not (future ?t1))\n"
            + "                    (not (future ?t2))\n"
            + "                    (not (future ?t3))\n"
            + "                    (now ?t3)\n"
            + "                    (past ?t0)\n"
            + "                    (past ?t1)\n"
            + "                    (past ?t2)\n"
            + "                    (stand-3h ?pos)\n"
            + "                    (be-at ?pos ?t1)\n"
            + "                    (be-at ?pos ?t2)\n"
            + "                    (be-at ?pos ?t3)\n"
            + "                )\n"
            + "      )\n"
            + "      (:action still-4h\n"
            + "        :parameters (?pos - waitplace ?t0 - tempo ?t1 - tempo ?t2 - tempo ?t3 - tempo ?t4 - tempo)\n"
            + "        :precondition (and\n"
            + "                        (now ?t0)\n"
            + "                        (next ?t0 ?t1)\n"
            + "                        (next ?t1 ?t2)\n"
            + "                        (next ?t2 ?t3)\n"
            + "                        (next ?t3 ?t4)\n"
            + "                        (future ?t1)\n"
            + "                        (be ?pos)\n"
            + "                     )\n"
            + "        :effect (and\n"
            + "                    (not (now ?t0))\n"
            + "                    (not (future ?t1))\n"
            + "                    (not (future ?t2))\n"
            + "                    (not (future ?t3))\n"
            + "                    (not (future ?t4))\n"
            + "                    (now ?t4)\n"
            + "                    (past ?t0)\n"
            + "                    (past ?t1)\n"
            + "                    (past ?t2)\n"
            + "                    (past ?t3)\n"
            + "                    (stand-4h ?pos)\n"
            + "			    (be-at ?pos ?t1)\n"
            + "			(be-at ?pos ?t2)\n"
            + "			(be-at ?pos ?t3)\n"
            + "                    (be-at ?pos ?t4)\n"
            + "                )\n"
            + "      )\n"
            + ")";

    public String getProblemHead() {
        return problemHead;
    }

    public void setProblemHead(String problemHead) {
        this.problemHead = problemHead;
    }

    public String getDomain() {
        return domain;
    }

    public String getMapjson() {
        return mapjson;
    }

    public void setMapjson(String mapjson) {
        this.mapjson = mapjson;
    }

    public List<PDDLGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<PDDLGoal> goals) {
        this.goals = goals;
    }

    public List<PDDLGoal> getBelief() {
        return belief;
    }

    public void setBelief(List<PDDLGoal> belief) {
        this.belief = belief;
    }

    public List<PDDLAction> getActions() {
        return actions;
    }

    public void setActions(List<PDDLAction> actions) {
        this.actions = actions;
    }

    //PDDLMap m;
    private String problemHead;
    private String mapjson;
    List<PDDLGoal> goals = new ArrayList<PDDLGoal>();
    List<PDDLGoal> belief = new ArrayList<PDDLGoal>();

    private List<PDDLAction> actions = new ArrayList<PDDLAction>();

    private Configuration config;

    public PDDLProblem(String problemHead, List<PDDLGoal> goals, List<PDDLGoal> belief, String mapjson) {
        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
        JAVA_PDDL4J_PATH = config.getPddlPath();

        this.problemHead = problemHead;
        this.mapjson = mapjson;
        this.goals = goals;
        this.belief = belief;

        String a[] = domain.split("\\(:action");
        for (int i = 1; i < a.length; i++) {
            actions.add(new PDDLAction(a[i]));
        }
    }

    public PDDLProblem(String problemHead, String goals, String belief, String mapjson) {
        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
        System.out.println("PDDL PATH: " + config.getPddlPath());
        JAVA_PDDL4J_PATH = config.getPddlPath();

        this.problemHead = problemHead;
        this.mapjson = mapjson;
        this.goals = splitStringInGoals(goals);
        this.belief = splitStringInGoals(belief);

        String a[] = domain.split("\\(:action");
        for (int i = 1; i < a.length; i++) {
            actions.add(new PDDLAction(a[i]));
        }
    }

    public static List<PDDLGoal> splitStringInGoals(String s) {
        String[] sg = s.split("\n");
        List<PDDLGoal> g = new ArrayList<PDDLGoal>();
        for (int i = 0; i < sg.length; i++) {
            g.add(new PDDLGoal(sg[i]));
        }
        return g;
    }

    public static String concatGoalsInString(List<PDDLGoal> g) {
        String s = "";
        for (int i = 0; i < g.size(); i++) {
            s += g.get(i).toString() + "\n";
        }
        return s;
    }

    public boolean hasSolution() {
        String plan = thinkSolution();
        if (plan != "") {
            System.out.println(plan);
            return true;
        } else {
            return false;
        }
    }

//    public String thinkSolution() {
//        ProcessBuilder pb = new ProcessBuilder();
//        String solution = "";
//        String problem = getProblem();
//        System.out.println("risolvo questo problema: " + problem);
//        try {
//            File fd = File.createTempFile("domain.", "pddl");
//            PrintWriter writer = new PrintWriter(fd, "UTF-8");
//            writer.print(domain);
//            writer.close();
//            File fp = File.createTempFile("problem.", "pddl");
//            writer = new PrintWriter(fp, "UTF-8");
//            writer.print(problem);
//            writer.close();
//            String cmd = "java -javaagent:" + JAVA_PDDL4J_PATH + " -server -Xms2048m -Xmx2048m fr.uga.pddl4j.planners.hsp.HSP -o " + fd.getAbsolutePath() + " -f " + fp.getAbsolutePath() + " -t " + MAX_EXECUTION_TIME + " -s false";
//            String[] commands = cmd.split(" ");
//            System.out.println(cmd);
//            pb.command(commands);
//            //Process pr = new ProcessBuilder(cmd).start();
//            Process pr = pb.start();
//            boolean finished = waitFor(pr, MAX_EXECUTION_TIME, TimeUnit.SECONDS);
//            if (finished) {
//                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//                String line = null;
//                try {
//                    while ((line = input.readLine()) != null) {
//                        System.out.println(line);
//                        solution += line + "\n";
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("fine!");
//                if (solution.contains("found plan as follows:")) {
//                    solution = (solution.split("found plan as follows:")[1]);
//                    System.out.println("soluzione" + solution);
//
//                    String step[] = solution.split("\n");
//                    solution = "";
//                    for (int i = 0; i < step.length; i++) {
//                        String t = sanifyGoal(step[i]);
//                        System.out.println(step[i] + " " + t);
//                        if (t != null) {
//                            solution += t + "\n";
//                        }
//                    }
//                } else {
//                    solution = "";
//                }
//            } else {
//                System.out.println("Andato in loop!");
//            }
//            fd.delete();
//            fp.delete();
//            pr.destroy();
//
//        } catch (Exception e) {
//            System.out.println("Errororen! ");
//            e.printStackTrace();
//        }
//        return solution;
//    }
    
    public String thinkSolution(){
        ProcessBuilder pb = new ProcessBuilder();
        String solution="";
        String problem=getProblem();
        System.out.println("risolvo questo problema: "+problem);
        try{
            
            // Create the problem factory
            final ProblemFactory factory = new ProblemFactory();
            
            // Parse the domain and the problem
            ErrorManager errorManager = factory.parseFromString(domain, problem);
            if (!errorManager.isEmpty()) {
                errorManager.printAll();
                System.exit(0) ;
            }            
            
            // Encode and simplify the planning problem in a compact representation
            final CodedProblem prb = factory.encode();
            if (!prb.isSolvable()) {
                System.out.println("Goal can be simplified to FALSE. no search will solve it");
            }
            
            // Create the planner and choose the Fast Forward heuristic
            HSP planner = new HSP();
            planner.setHeuristicType(Heuristic.Type.FAST_FORWARD);
            planner.setSaveState(false);
            planner.setTimeOut(MAX_EXECUTION_TIME);
            // Search for a solution plan
            final Plan plan = planner.search(prb);
            if (plan != null && !prb.toString(plan).isEmpty()) {
                System.out.println(String.format("%nfound plan as follows:%n%n"));
                System.out.println (prb.toString(plan));
                solution=prb.toString(plan);
            }            
           
        }catch(Exception e){
            System.out.println("Errororen! ");
            e.printStackTrace();
        }
        return solution;
    }
    

    private static String sanifyGoal(String g) {
        if (g.contains("(") && g.contains(")")) {
            return "(" + ((g.split("\\(")[1]).split("\\)")[0]).replaceAll(" +", " ").replaceAll("^ ", "") + ")";
        } else {
            return null;
        }
    }

    public static int calculateDifficultyFromSolution(String solution) {
        String step[] = solution.split("\n");
        int dif = 0;
        for (int i = 0; i < step.length; i++) {
            if (step[i].contains("(move-to") || step[i].contains("(drive-to")) {
                dif++;
            }
            if (step[i].contains("(take-item")) {
                if (step[i].contains("(take-item-at"))//more easy!
                {
                    dif++;
                } else {
                    dif += 2;
                }
            }
            if (step[i].contains("(drop-item")) {
                if (step[i].contains("(drop-item-at"))//more easy!
                {
                    dif += 2;
                } else {
                    dif += 3;
                }
            }
            if (step[i].contains("(change-item")) {
                dif++;
            }
            if ((step[i].contains("(still-")) && ((i == 0) || !step[i - 1].contains("(still-"))) {
                dif++;
            }
        }
        return dif;
    }

    /*
     public String thinkSolutionInside(){
     String problem = getProblem();
     System.out.println("Think: 1");
     //loadAgent();
     final HSP planner = new HSP();
     // Creates the problem factory
     final ProblemFactory factory = new ProblemFactory();
     ErrorManager errorManager;
     System.out.println("Think: 2");
     try {
     errorManager = factory.parse(domain, problem);
     if (!errorManager.isEmpty()) {
     errorManager.printAll();
     System.exit(0);
     }
     System.out.println("Think: 3");
     } catch (IOException ex) {
     java.util.logging.Logger.getLogger(PDDLProblem.class.getName()).log(Level.SEVERE, null, ex);
     }
      
     System.out.println("Think: 4");
     // Encode and simplify the planning problem in a compact representation
     final CodedProblem pb = factory.encode();
     System.out.println("Think: 5");
     if (!pb.isSolvable()) {
     return "goal can be simplified to FALSE. no search will solve it";
     }
     System.out.println("Think: 6");
     planner.setTimeOut(3);//seconds!
     // Searche for a solution plan
     System.out.println("Think: Sto risolvendo il piano");
     final Plan plan = planner.search(pb);
     System.out.println("Think: Piano risolto!");
     if (plan != null) {
     return pb.toString(plan);
     } else {
     return "plan not found";
     }
     }*/
    public boolean binds(PDDLGoal action) {
        System.out.println("cerco un bind per " + action);
        List<PDDLGoal> result = null;
        for (int i = 0; i < actions.size(); i++) {
            System.out.println("Azione:" + actions.get(i) + "****");
            result = actions.get(i).binds(action, this);
            if (result != null) {
                System.out.println("ma questo bind esiste: " + result);
                for (int j = 0; j < result.size(); j++) {
                    updateBelief(result.get(j));
                }
                return true;
            }
        }
        return false;
    }

    public void updateBelief(PDDLGoal newbelief) {
        System.out.println("Aggiorno le belief: " + newbelief);
        int idx = belief.indexOf(newbelief);
        if (idx == -1) {//not found
            int idxn = belief.indexOf(newbelief.getNegate());
            if (idxn == -1) {//not found the negate I can add the belief
                if (!newbelief.isNegate())//aggiungo la belief solo se è positiva
                {
                    belief.add(newbelief);
                }
            } else {//esiste la negata
                belief.remove(idxn);
                if (!newbelief.isNegate())//aggiungo la belief solo se è positiva
                {
                    belief.add(newbelief);
                }
            }
        }
    }

    public int indexOfBelief(PDDLGoal g) {
        System.out.println("Cerco la belief " + g);
        int idx = -1;
        PDDLGoal checkGoal;
        if (g.isNegate()) {
        	// Se il belief contiene un not, devo controllare che NON ci sia
        	checkGoal = g.getNegate();
        	System.out.println("Goal negato, cerco " + checkGoal);
        } else {
        	checkGoal = g;
        }
        for (int i = 0; i < belief.size(); i++) {
        	
            if (belief.get(i).equals(checkGoal)) {
                System.out.println(belief.get(i));
                idx = i;
            }
        }
        System.out.println("Si trova all'indice " + idx + " mentre per indexOf " + belief.indexOf(g));
        return belief.indexOf(g);
    }

    private String getProblem() {
        String p = problemHead;

        p += "    )\n";
        p += "    (:init\n";
        for (int i = 0; i < belief.size(); i++) {
            p += "        " + belief.get(i) + "\n";
        }
        p += "    )\n";
        p += "    (:goal (and\n";
        for (int i = 0; i < goals.size(); i++) {
            p += "            " + goals.get(i).toString() + "\n";
        }
        p += "        )\n";
        p += "    )\n";
        p += ")";
        return p;
    }

    private String twoDigit(int x) {
        String xx = "" + x;
        if (xx.length() < 2) {
            xx = "0" + xx;
        }
        return xx;
    }

    public String getProblemDescription(Environment e) {
        String g = "";
        for (int i = 0; i < belief.size(); i++) {
            if (belief.get(i).getHead().equals("open-hour")) {
                System.out.println("trovata belief");
                String[] bind = belief.get(i).getBinds();
                if ((!bind[1].contains("rotonda")) && (!bind[1].contains("casa"))) {
                    int apreh = Integer.parseInt(bind[2].split("p")[1]);
                    int chiudeh = Integer.parseInt(bind[3].split("p")[1]);
                    int aprem = (apreh % 2 * 30);
                    apreh = apreh / 2;
                    int chiudem = (chiudeh % 2 * 30);
                    chiudeh = chiudeh / 2;
                    JSONObject j = new JSONObject(e.getEnvironment());
                    JSONArray places = j.getJSONArray("places");
                    String article = "";
                    for (int l = 0; l < places.length(); l++) {
                        //  System.out.println(bind[1]+" =?= "+(((JSONObject)places.get(l)).getString("name")));
                        if (bind[1].equals(((JSONObject) places.get(l)).getString("name"))) {
                            article = ((JSONObject) places.get(l)).getString("article");
                            article = article.substring(0, 1).toUpperCase() + article.substring(1).toLowerCase();
                        }
                    }
                    g += article + " <b>" + bind[1] + "</b> è aperta dalle <b>" + apreh + ":" + twoDigit(aprem) + "</b> alle <b>" + chiudeh + ":" + twoDigit(chiudem) + "</b><br />\n";
                }
            }
        }
        g += "<br />\n";
        for (int i = 0; i < goals.size(); i++) {
            g += e.getNaturalLanguageGoal(goals.get(i)) + "\n";
        }
        return g;
    }

    public boolean isSolved() {
//    	System.out.println("GOALS: " + goals.toString());
//    	System.out.print("Beliefs: " + belief.toString());
        boolean isSolved = belief.containsAll(goals);
        System.out.println("Is solved? " + isSolved);
        return isSolved;
    }

    public List<PDDLGoal> unsolvedGoal() {
        System.out.print("Unsolved goals: ");
        List<PDDLGoal> tgoals = new ArrayList<PDDLGoal>(goals);
        for (int i = 0; i < belief.size(); i++) {
            tgoals.remove(belief.get(i));
        }
        System.out.print(tgoals.toString());
        return tgoals;
    }

    private boolean waitFor(Process pr, long timeout, TimeUnit unit)
            throws InterruptedException {
        long startTime = System.nanoTime();
        long rem = unit.toNanos(timeout);

        do {
            try {
                pr.exitValue();
                return true;
            } catch (IllegalThreadStateException ex) {
                if (rem > 0) {
                    Thread.sleep(
                            Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
                }
            }
            rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
        } while (rem > 0);
        return false;
    }

}
