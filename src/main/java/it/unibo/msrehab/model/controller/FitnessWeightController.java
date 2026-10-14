package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.FitnessWeight;
import it.unibo.msrehab.model.entities.History;


/**
 * @author danger
 */
public class FitnessWeightController extends DecoratorEntityController<FitnessWeight>
{

    public FitnessWeightController(Model model)
    {
        super(model, FitnessWeight.class);
    }



    public double calculateRelativePerformance(String difficulty, double absPerformance)
    {
        switch (difficulty)
        {
            case "easy":
                return absPerformance * 0.33;
            case "medium":
                return absPerformance * 0.66;
            case "hard":
            case "difficult":
            default:
                return absPerformance;
        }
    }

    public Double calculateFitness(Boolean difficultyFlag, History h)
    {
        double ft = 0;

        FitnessWeight w = getEntity(h.getExid());

        double pC = h.getpCorrect();
        double pM = h.getpMissed();
        double pW = h.getpWrong();
        double pT = h.getpTime();

        double beta = w.getBeta();
        double thr = w.getThr();
        boolean timeFlag = w.getTimeflag();
        double maxT = h.getMaxtime();
        String difficulty = h.getDifficulty();

        double precision = pC / (pC + pW);

        if (Double.isNaN(precision))
            precision = 1;

        double recall = pC / (pC + pM);

        if (Double.isNaN(recall))
            recall = 1;

        double t = (maxT - pT) / maxT;
        double t1 = (1 - thr) * t + thr;

        if (precision == 0 && recall == 0)
            ft = 0;
        else
            ft = (1 + Math.pow(beta, 2.0)) * (precision * recall / (Math.pow(beta, 2.0) * precision + recall));


        if (timeFlag)
            ft *= t1;


        if (difficultyFlag)
        {
            double d = 0;// (DifficultyValue.valueOf(difficulty).ordinal()+1)*33;
            switch (difficulty)
            {
                case "easy":
                    d = 0.33;
                    break;
                case "medium":
                    d = 0.66;
                    break;
                case "difficult":
                default:
                    d = 1.0;
                    break;
            }
            ft = ft * d;
        }

        return Math.max(0, Math.min(1, ft));
    }

}
