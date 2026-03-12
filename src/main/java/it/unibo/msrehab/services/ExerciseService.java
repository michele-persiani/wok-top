package it.unibo.msrehab.services;

import com.sun.istack.internal.Nullable;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.controller.*;
import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.model.entities.ExElement.CategoryValue;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseCategoryValue.*;

import it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_ALT_CMP;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_CHS_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_VEG_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_RFLXS_MOTORBIKE;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_SEL_STD_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_SEL_STD_CHS_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_SEL_STD_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_SEL_STD_VEG_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_CHS_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_VEG_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.NBACK_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.NBACK_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.NBACK_VEG_RL;

import it.unibo.msrehab.model.entities.PersonalData.Gender;

import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unibo.msrehab.rl.impl.NextLevelAgent;
import it.unibo.msrehab.rl.impl.ExerciseAgent;
import it.unibo.msrehab.rl.model.IModel;
import it.unibo.msrehab.rl.model.JPAController;
import it.unibo.msrehab.rl.ExerciseHelper;
import it.unibo.msrehab.model.entities.ThresholdAgentConfig;
import it.unibo.msrehab.rl.utils.Lists;
import it.unibo.msrehab.rl.utils.ParametersParser;
import it.unibo.msrehab.rl.utils.ScoreComparator;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * Class that provides servlets for exercises.
 */
@Controller
public class ExerciseService
{

    private final int NUM_FEAT_ATT_1 = 6; //dimensione array cambio diff
    private final int NUM_FEAT_ATT_2 = 5;
    private final int NUM_FEAT_ATT_3 = 5;
    private final int NUM_FEAT_ATT_4 = 6;
    private final int NUM_FEAT_MEM_1 = 5;
    private final int NUM_FEAT_MEM_2 = 4;
    private final int NUM_FEAT_NBACK = 4;
    private final int NUM_FEAT_MEM_LONG_1 = 4;
    private final int NUM_FEAT_RES_INH = 4;

    private final int NUM_FEAT_MEM_5 = 4;

    //private final int NUM_FEAT_MEM_NOT_INCL = 5;
    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);
    private final ExElementController exElementController;
    private final HistoryController historyController;
    private final MSRSessionController sessionController;
    private final Configuration config;
    private final FitnessWeightController fitnessController;
    private final ChangeDifficultyController changeDiffController;

    private final ExerciseController exerciseController;

    private final MSRUserController userController;


    private final IModel model = JPAController.getInstance();



    public ExerciseService()
    {
        super();
        this.exElementController = new ExElementController();
        this.historyController = new HistoryController();
        this.sessionController = new MSRSessionController();
        this.fitnessController = new FitnessWeightController();
        this.changeDiffController = new ChangeDifficultyController();
        this.exerciseController = new ExerciseController();
        this.userController = new MSRUserController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
    }


    public static Map<String, Object> createParametersAttention1(Integer level, Integer[] diffVar, ExerciseNameValue exname)
    {


        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int ntargets = 0, nelements = 0, time = 0, ncols = 0;
        String alignment = "", color = "", distractor = "";
        switch (level)
        {
            case -1: // training
                ntargets = 2;
                diffVar[0] = 1;
                nelements = 12;
                diffVar[1] = 1;
                ncols = 4;
                alignment = "aligned";
                diffVar[2] = 1;
                distractor = "no";
                diffVar[3] = 1;
                time = 3;
                diffVar[4] = 1;
                color = "color";
                diffVar[5] = 1;
                break;
            case 1: // easy min
                ntargets = 2;
                diffVar[0] = 1;
                nelements = 30;
                diffVar[1] = 1;
                ncols = 4;
                alignment = "aligned";
                diffVar[2] = 1;
                distractor = "no";
                diffVar[3] = 1;
                time = 3;
                diffVar[4] = 1;
                color = "color";
                diffVar[5] = 1;
                break;
            case 2:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 30;
                diffVar[1] = 1;
                ncols = 4;
                alignment = "aligned";
                diffVar[2] = 1;
                distractor = "no";
                diffVar[3] = 1;
                time = 3;
                diffVar[4] = 1;
                color = "color";
                diffVar[5] = 1;
                break;
            case 3:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 1;
                distractor = "no";
                diffVar[3] = 1;
                time = 3;
                diffVar[4] = 1;
                color = "color";
                diffVar[5] = 1;
                break;
            case 4:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 2;
                distractor = "no";
                diffVar[3] = 1;
                time = 3;
                diffVar[4] = 1;
                color = "color";
                diffVar[5] = 1;
                break;
            case 5: // medium min
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 66;
                diffVar[1] = 2;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 2;
                distractor = "no";
                diffVar[3] = 2;
                time = 3;
                diffVar[4] = 1;
                color = "color";
                diffVar[5] = 1;
                break;
            case 6:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 66;
                diffVar[1] = 2;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 2;
                distractor = "no";
                diffVar[3] = 2;
                time = 2;
                diffVar[4] = 2;
                color = "color";
                diffVar[5] = 1;
                break;
            case 7:
                ntargets = 4;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 2;
                distractor = "no";
                diffVar[3] = 2;
                time = 2;
                diffVar[4] = 2;
                color = "omo";
                diffVar[5] = 2;
                break;
            case 8:
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 66;
                diffVar[1] = 2;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 2;
                distractor = "no";
                diffVar[3] = 2;
                time = 2;
                diffVar[4] = 2;
                color = "omo";
                diffVar[5] = 2;
                break;
            case 9: // difficult min
                ntargets = 5;
                diffVar[0] = 3;
                nelements = 66;
                diffVar[1] = 3;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 2;
                distractor = "no";
                diffVar[3] = 2;
                time = 2;
                diffVar[4] = 2;
                color = "omo";
                diffVar[5] = 2;
                break;
            case 10:
                ntargets = 5;
                diffVar[0] = 3;
                nelements = 75;
                diffVar[1] = 3;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 3;
                distractor = "no";
                diffVar[3] = 2;
                time = 2;
                diffVar[4] = 2;
                color = "omo";
                diffVar[5] = 2;
                break;
            case 11:
                ntargets = 5;
                diffVar[0] = 3;
                nelements = 75;
                diffVar[1] = 3;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 3;
                distractor = "no";//simple complex
                diffVar[3] = 3;
                time = 2;
                diffVar[4] = 2;
                color = "bw";
                diffVar[5] = 2;
                break;
            case 12:
                ntargets = 6;
                diffVar[0] = 3;
                nelements = 75;
                diffVar[1] = 3;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 3;
                distractor = "no";
                diffVar[3] = 3;
                time = 2;
                diffVar[4] = 3;
                color = "omo";
                diffVar[5] = 2;
                break;
            case 13:
                ntargets = 6;
                diffVar[0] = 3;
                nelements = 75;
                diffVar[1] = 3;
                ncols = 3;
                alignment = "aligned";
                diffVar[2] = 3;
                distractor = "no";
                diffVar[3] = 3;
                time = 2;
                diffVar[4] = 3;
                color = "bw";
                diffVar[5] = 3;
                break;
        }

        parameters.put("ntargets", ntargets);
        parameters.put("nelements", nelements);
        parameters.put("alignment", alignment);
        parameters.put("color", color);
        parameters.put("distractor", distractor);
        parameters.put("time", time);
        parameters.put("ncols", ncols);

        return parameters;
    }

    public static Map<String, Object> createParametersAttention2(Integer level, Integer[] diffVar)
    {

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int ntargets = 0, nelements = 0; //time = 0;
        double time = 0.0;

        String color = "", distractor = "";
        switch (level)
        {
            case -1: // training
                ntargets = 2;
                diffVar[0] = 1;
                nelements = 12;
                diffVar[1] = 1;
                //nelements = 5; diffVar[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 1: // easy min
                ntargets = 2;
                diffVar[0] = 1;
                nelements = 30;
                diffVar[1] = 1;
                //nelements = 5; diffVar[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 2:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 30;
                diffVar[1] = 1;
                //nelements = 5; diffVar[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 3:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 4: // medium min
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                //time = 4;
                time = 2;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 5:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 1;
                break;
            case 6:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 66;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 7:
                ntargets = 3;
                diffVar[0] = 3;
                nelements = 80;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 8: // difficult min
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 66;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 9:
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 66;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                //time = 3;
                time = 1.5;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 10:
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 80;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                //time = 2;
                time = 1.5;
                diffVar[3] = 3;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 11:
                ntargets = 5;
                diffVar[0] = 3;
                nelements = 66;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                //time = 2;
                time = 1.5;
                diffVar[3] = 3;
                color = "omo";
                diffVar[4] = 3;
                break;
            case 12:
                ntargets = 5;
                diffVar[0] = 3;
                nelements = 80;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                //time = 2;
                time = 1.5;
                diffVar[3] = 3;
                color = "omo";
                diffVar[4] = 3;
                break;
        }

        parameters.put("ntargets", ntargets);
        parameters.put("nelements", nelements);
        parameters.put("color", color);
        parameters.put("distractor", distractor);
        parameters.put("time", time);

        return parameters;

    }

    public static Map<String, Object> createParametersAttention3(Integer level, Integer[] diffVar)
    {

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int iterations = 0, nelementspertarget = 0; //time = 0;

        double time = 0.0, frequenza = 0.0;
        String color = "";
        switch (level)
        {
            case -1: // training
                iterations = 2;
                diffVar[0] = 1;
                nelementspertarget = 10;//elementi totali per iterazione
                diffVar[1] = 1;
                frequenza = 0.7;
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 1: // easy min
                iterations = 2;
                diffVar[0] = 1;
                nelementspertarget = 20;
                diffVar[1] = 1;
                frequenza = 0.6;
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 2:
                iterations = 3;
                diffVar[0] = 2;
                nelementspertarget = 20;
                diffVar[1] = 1;
                frequenza = 0.6;
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 3:
                iterations = 3;
                diffVar[0] = 2;
                nelementspertarget = 15;
                diffVar[1] = 2;
                frequenza = 0.6;
                diffVar[2] = 1;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 4: // medium min
                iterations = 3;
                diffVar[0] = 2;
                nelementspertarget = 15;
                diffVar[1] = 2;
                frequenza = 0.5;
                diffVar[2] = 2;
                //time = 4;
                time = 2.5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 5:
                iterations = 3;
                diffVar[0] = 2;
                nelementspertarget = 15;
                diffVar[1] = 2;
                frequenza = 0.5;
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "color";
                diffVar[4] = 1;
                break;
            case 6:
                iterations = 3;
                diffVar[0] = 2;
                nelementspertarget = 15;
                diffVar[1] = 2;
                frequenza = 0.4;
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 7:
                iterations = 4;
                diffVar[0] = 3;
                nelementspertarget = 15;
                diffVar[1] = 2;
                frequenza = 0.4;
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 8: // difficult min
                iterations = 4;
                diffVar[0] = 3;
                nelementspertarget = 10;
                diffVar[1] = 3;
                frequenza = 0.4;
                diffVar[2] = 2;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 9:
                iterations = 4;
                diffVar[0] = 3;
                nelementspertarget = 10;
                diffVar[1] = 3;
                frequenza = 0.3;
                diffVar[2] = 3;
                //time = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 10:
                iterations = 4;
                diffVar[0] = 3;
                nelementspertarget = 10;
                diffVar[1] = 3;
                frequenza = 0.2;
                diffVar[2] = 3;
                //time = 2;
                time = 1.5;
                diffVar[3] = 3;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 11:
                iterations = 4;
                diffVar[0] = 3;
                nelementspertarget = 10;
                diffVar[1] = 3;
                frequenza = 0.2;
                diffVar[2] = 3;
                //time = 2;
                time = 1.5;
                diffVar[3] = 3;
                color = "bw";
                diffVar[4] = 3;
                break;
        }

        parameters.put("iterations", iterations);
        parameters.put("nelementspertarget", nelementspertarget);
        parameters.put("color", color);
        parameters.put("frequenza", frequenza);
        parameters.put("time", time);

        return parameters;

    }

    public static Map<String, Object> createParametersAttention4(Integer level, Integer[] diffVar)
    {

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int ntargets = 0, nelements = 0, time = 0;
        String color = "", distractor = "", soundinterval = "";
        switch (level)
        {
            case -1: //training
                ntargets = 2;
                diffVar[0] = 1;
                nelements = 12;
                diffVar[1] = 1;
                //nelements = 5; diffVar2[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                time = 4;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                soundinterval = "long";
                diffVar[5] = 1;
                break;
            case 1: // easy min
                ntargets = 2;
                diffVar[0] = 1;
                nelements = 30;
                diffVar[1] = 1;
                //nelements = 5; diffVar2[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                time = 4;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                soundinterval = "long";
                diffVar[5] = 1;
                break;
            case 2:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 30;
                diffVar[1] = 1;
                //nelements = 5; diffVar2[1] = 1;                
                distractor = "no";
                diffVar[2] = 1;
                time = 4;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                soundinterval = "long";
                diffVar[5] = 1;
                break;
            case 3:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                //nelements = 10; diffVar2[1] = 2;
                distractor = "no";
                diffVar[2] = 1;
                time = 4;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                soundinterval = "long";
                diffVar[5] = 1;
                break;
            case 4:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 3;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                soundinterval = "long";
                diffVar[5] = 1;
                break;
            case 5: // medium min
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 48;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 3;
                diffVar[3] = 2;
                color = "color";
                diffVar[4] = 1;
                soundinterval = "medium";
                diffVar[5] = 1;
                break;
            case 6:
                ntargets = 3;
                diffVar[0] = 2;
                nelements = 66;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 3;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                soundinterval = "medium";
                diffVar[5] = 1;
                break;
            case 7:
                ntargets = 4;
                diffVar[0] = 2;
                nelements = 66;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 3;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                soundinterval = "medium";
                diffVar[5] = 2;
                break;
            case 8:
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 66;
                diffVar[1] = 2;
                //nelements = 10; diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 3;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                soundinterval = "short";
                diffVar[5] = 2;
                break;
            case 9: // difficult min
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 80;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 2;
                time = 3;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                soundinterval = "short";
                diffVar[5] = 2;
                break;
            case 10:
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 80;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                time = 2;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                soundinterval = "short";
                diffVar[5] = 2;
                break;
            case 11:
                ntargets = 4;
                diffVar[0] = 3;
                nelements = 80;
                diffVar[1] = 3;
                //nelements = 15; diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                time = 2;
                diffVar[3] = 3;
                color = "bw";
                diffVar[4] = 2;
                soundinterval = "short";
                diffVar[5] = 2;
                break;

        }

        parameters.put("ntargets", ntargets);
        parameters.put("nelements", nelements);
        parameters.put("color", color);
        parameters.put("distractor", distractor);
        parameters.put("time", time);
        parameters.put("soundinterval", soundinterval);

        return parameters;
    }

    public static Map<String, Object> createParametersMemory1(Integer level, Integer[] diffVar)
    {

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int nelements = 0, ntargets = 0, time = 0;
        String color = "", distractor = "";

        switch (level)
        {
            case -1: // training
                nelements = 4;
                diffVar[0] = 1;
                ntargets = 2;
                diffVar[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                time = 5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 1: // easy min
                nelements = 6;//era 6
                diffVar[0] = 1;
                ntargets = 3;
                diffVar[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                time = 5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 2:
                nelements = 7;
                diffVar[0] = 2;
                ntargets = 3;
                diffVar[1] = 1;
                distractor = "no";
                diffVar[2] = 1;
                time = 5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 3:
                nelements = 8;
                diffVar[0] = 2;
                ntargets = 4;
                diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 1;
                time = 5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 4: // medium min
                nelements = 9; //era 8
                diffVar[0] = 2;
                ntargets = 4;
                diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 5;
                diffVar[3] = 1;
                color = "color";
                diffVar[4] = 1;
                break;
            case 5:
                nelements = 9;//era 8
                diffVar[0] = 2;
                ntargets = 5;//era 4
                diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 4;
                diffVar[3] = 2;
                color = "color";
                diffVar[4] = 1;
                break;
            case 6:
                nelements = 9;//era 8
                diffVar[0] = 2;
                ntargets = 5;//era 4
                diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 4;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 7:
                nelements = 10;
                diffVar[0] = 3;
                ntargets = 6;//era 4
                diffVar[1] = 2;
                distractor = "no";
                diffVar[2] = 2;
                time = 4;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 8: // difficult min
                nelements = 11;//era 10
                diffVar[0] = 3;
                ntargets = 6;//era 5
                diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 2;
                time = 4;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 9:
                nelements = 11;//era 10
                diffVar[0] = 3;
                ntargets = 6;//era 5
                diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                time = 3;
                diffVar[3] = 2;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 10:
                nelements = 11;//era 10
                diffVar[0] = 3;
                ntargets = 7;//era 5
                diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                time = 3;
                diffVar[3] = 3;
                color = "omo";
                diffVar[4] = 2;
                break;
            case 11:
                nelements = 12;//era 10
                diffVar[0] = 3;
                ntargets = 7;//era 5
                diffVar[1] = 3;
                distractor = "no";
                diffVar[2] = 3;
                time = 3;
                diffVar[3] = 3;
                color = "bw";
                diffVar[4] = 3;
                break;
        }

        parameters.put("nelements", nelements);
        parameters.put("ntargets", ntargets);
        parameters.put("distractor", distractor);
        parameters.put("time", time);
        parameters.put("color", color);

        return parameters;

    }

    public static Map<String, Object> createParametersMemory2(Integer level, Integer[] diffVar, ExerciseNameValue exname)
    {


        Map<String, Object> parameters = new LinkedHashMap<>();

        int nelements = 0, time = 0, ntargets = 0;
        String color = "", distractor = "";

        switch (level)
        {
            case -1: // training
                nelements = 3;
                ntargets = 3;
                diffVar[0] = 1;
                distractor = "no";
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 1: // easy min
                nelements = 3;
                ntargets = 3;
                diffVar[0] = 1;
                distractor = "no";
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 2:
                nelements = 3;//era 4
                ntargets = 3;
                diffVar[0] = 2;
                distractor = "no";
                diffVar[1] = 1;
                time = 2;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 3: // medium min
                nelements = 4;
                ntargets = 4;
                diffVar[0] = 2;
                distractor = "no";
                diffVar[1] = 2;
                time = 2;
                diffVar[2] = 2;
                color = "color";
                diffVar[3] = 1;
                break;
            case 4://min
                nelements = 4;
                ntargets = 4;
                diffVar[0] = 2;
                distractor = "no";
                diffVar[1] = 2;
                time = 2;
                diffVar[2] = 2;
                color = "omo";
                diffVar[3] = 2;
                break;
            case 5://media
                nelements = 5;
                ntargets = 5;
                diffVar[0] = 3;
                distractor = "no";
                diffVar[1] = 2;
                time = 2;
                diffVar[2] = 2;
                color = "color";
                diffVar[3] = 2;
                break;
            case 6: // 
                nelements = 5;
                ntargets = 5;
                diffVar[0] = 3;
                distractor = "no";
                diffVar[1] = 3;
                time = 2;
                diffVar[2] = 2;
                color = "omo";
                diffVar[3] = 2;
                break;
            case 7:
                nelements = 6;//era 5
                ntargets = 6;
                diffVar[0] = 3;
                distractor = "no";
                diffVar[1] = 3;
                time = 3;
                diffVar[2] = 3;
                color = "omo";
                diffVar[3] = 2;
                break;
            case 8://alta
                nelements = 6;//era 5
                ntargets = 6;
                diffVar[0] = 3;
                distractor = "no";
                diffVar[1] = 3;
                time = 2;
                diffVar[2] = 3;
                color = "bw";
                diffVar[3] = 3;
                break;
            case 9:
                nelements = 7;//non c'era
                ntargets = 7;
                diffVar[0] = 3;
                distractor = "no";
                diffVar[1] = 3;
                time = 2;
                diffVar[2] = 3;
                color = "omo";
                diffVar[3] = 2;
                break;
            case 10:
                nelements = 7;//non c'era
                ntargets = 7;
                diffVar[0] = 3;
                distractor = "no";
                diffVar[1] = 3;
                time = 2;
                diffVar[2] = 3;
                color = "bw";
                diffVar[3] = 3;
                break;
        }

        parameters.put("nelements", nelements);
        parameters.put("ntargets", ntargets);
        parameters.put("distractor", distractor);
        parameters.put("time", time);
        parameters.put("color", color);


        return parameters;

    }

    public static Map<String, Object> createParametersMemory4(Integer level, Integer[] diffVar)
    {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        int nfaces = 0, time = 0;
        String name = "", namediff = "";
        switch (level)
        {
            case -1: // training
                nfaces = 3;
                diffVar[0] = 1;
                time = 60;      // 1 hour = unlimited
                diffVar[1] = 1;
                name = "surname";
                diffVar[2] = 1;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 1: // easy min
                nfaces = 2;
                diffVar[0] = 1;
                time = 60;      // 1 hour = unlimited
                diffVar[1] = 1;
                name = "name-surname";
                diffVar[2] = 1;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 2: // easy min
                nfaces = 3;
                diffVar[0] = 1;
                time = 60;      // 1 hour = unlimited
                diffVar[1] = 1;
                name = "surname";
                diffVar[2] = 1;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 3:
                nfaces = 3;
                diffVar[0] = 1;
                time = 60;      // 1 hour = unlimited
                diffVar[1] = 1;
                name = "name-surname";
                diffVar[2] = 2;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 4:
                nfaces = 4;
                diffVar[0] = 1;
                time = 60;      // 1 hour = unlimited
                diffVar[1] = 1;
                name = "surname";
                diffVar[2] = 2;
                namediff = "common";
                diffVar[3] = 2;
                break;
            case 5: // easy max
                nfaces = 4;
                diffVar[0] = 1;
                time = 60;      // 1 hour = unlimited
                diffVar[1] = 1;
                name = "name-surname";
                diffVar[2] = 2;
                namediff = "italian";
                diffVar[3] = 3;
                break;
            case 6: // medium min
                nfaces = 5;
                diffVar[0] = 2;
                time = 60;
                diffVar[1] = 2;
                name = "surname";
                diffVar[2] = 1;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 7:
                nfaces = 5;
                diffVar[0] = 2;
                time = 60;
                diffVar[1] = 2;
                name = "name-surname";
                diffVar[2] = 2;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 8:
                nfaces = 6;
                diffVar[0] = 2;
                time = 60;
                diffVar[1] = 2;
                name = "surname";
                diffVar[2] = 2;
                namediff = "italian";
                diffVar[3] = 2;
                break;
            case 9: // medium max
                nfaces = 6;
                diffVar[0] = 2;
                time = 60;
                diffVar[1] = 2;
                name = "name-surname";
                diffVar[2] = 2;
                namediff = "italian";
                diffVar[3] = 3;
                break;
            case 10: // difficult min
                nfaces = 7;
                diffVar[0] = 3;
                time = 60;
                diffVar[1] = 2;
                name = "surname";
                diffVar[2] = 2;
                namediff = "common";
                diffVar[3] = 1;
                break;
            case 11:
                nfaces = 7;
                diffVar[0] = 3;
                time = 60;
                diffVar[1] = 3;
                name = "surname";
                diffVar[2] = 2;
                namediff = "italian";
                diffVar[3] = 1;
                break;

        }
        parameters.put("nfaces", nfaces);
        parameters.put("time", time);
        parameters.put("name", name);
        parameters.put("namediff", namediff);

        return parameters;
    }

    public static Map<String, Object> createParametersMemory5(Integer level, Integer[] diffVar)
    {

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int nelements = 0, time = 0, ntargets = 0, nRigheCol = 0;
        String color = "";

        switch (level)
        {
            case -1: // training
                nelements = 3;
                ntargets = 3;
                diffVar[0] = 1;
                nRigheCol = 2;
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 1: // easy min
                nelements = 3;
                ntargets = 3;
                diffVar[0] = 1;
                nRigheCol = 2;
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 2:
                nelements = 3;//era 4
                ntargets = 3;
                diffVar[0] = 2;
                nRigheCol = 2;
                diffVar[1] = 1;
                time = 2;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 3: // medium min
                nelements = 4;
                ntargets = 4;
                diffVar[0] = 2;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 2;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 4://min
                nelements = 4;
                ntargets = 4;
                diffVar[0] = 2;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 2;
                diffVar[2] = 1;
                color = "omo";
                diffVar[3] = 1;
                break;
            case 5://media
                nelements = 5;
                ntargets = 5;
                diffVar[0] = 3;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 2;
                diffVar[2] = 1;
                color = "color";
                diffVar[3] = 1;
                break;
            case 6: // 
                nelements = 5;
                ntargets = 5;
                diffVar[0] = 3;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 2;
                diffVar[2] = 1;
                color = "omo";
                diffVar[3] = 1;
                break;
            case 7:
                nelements = 6;//era 5
                ntargets = 6;
                diffVar[0] = 3;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "omo";
                diffVar[3] = 1;
                break;
            case 8://alta
                nelements = 6;//era 5
                ntargets = 6;
                diffVar[0] = 3;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "bw";
                diffVar[3] = 1;
                break;
            case 9:
                nelements = 7;//non c'era
                ntargets = 7;
                diffVar[0] = 3;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "omo";
                diffVar[3] = 1;
                break;
            case 10:
                nelements = 7;//non c'era
                ntargets = 7;
                diffVar[0] = 3;
                nRigheCol = 3;
                diffVar[1] = 1;
                time = 3;
                diffVar[2] = 1;
                color = "bw";
                diffVar[3] = 1;
                break;
        }

        parameters.put("nelements", nelements);
        parameters.put("ntargets", ntargets);
        parameters.put("nRigheCol", nRigheCol);
        parameters.put("time", time);
        parameters.put("color", color);

        return parameters;

    }

    public static Map<String, Object> createParametersNback(Integer level, Integer[] diffVar, ExerciseNameValue exname)
    {

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        int nback = 0, time = 0, nelements = 0;
        String color = "";
        switch (level)
        {
            case -1: // training
                nback = 1;
                diffVar[0] = 1;
                time = 4;
                diffVar[1] = 1;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;

            case 1: // easy min
                nback = 1;
                diffVar[0] = 1;
                time = 3;
                diffVar[1] = 1;
                color = "color";
                diffVar[2] = 1;
                nelements = 30;
                diffVar[3] = 1;
                break;
            case 2:
                nback = 1;
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 2;
                color = "color";
                diffVar[2] = 1;
                nelements = 40;
                diffVar[3] = 1;
                break;
            case 3:
                nback = 2;
                diffVar[0] = 1;
                time = 4;
                diffVar[1] = 3;
                color = "color";
                diffVar[2] = 2;
                nelements = 30;
                diffVar[3] = 1;
                break;
            case 4://medio
                nback = 2;
                diffVar[0] = 1;
                time = 3;
                diffVar[1] = 3;
                color = "color";
                diffVar[2] = 3;
                nelements = 30;
                diffVar[3] = 1;
                break;
            case 5:
                nback = 2;
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 3;
                color = "omo";
                diffVar[2] = 3;
                nelements = 40;
                diffVar[3] = 3;
                break;
            case 6: // medio
                nback = 2;
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 1;
                color = "omo";
                diffVar[2] = 1;
                nelements = 45;
                diffVar[3] = 1;
                break;
            case 7://diffi
                nback = 3;
                diffVar[0] = 2;
                time = 4;
                diffVar[1] = 2;
                color = "color";
                diffVar[2] = 1;
                nelements = 30;
                diffVar[3] = 1;
                break;
            case 8:
                nback = 3;
                diffVar[0] = 2;
                time = 3;
                diffVar[1] = 3;
                color = "omo";
                diffVar[2] = 1;
                nelements = 40;
                diffVar[3] = 1;
                break;
            case 9:
                nback = 3;
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 3;
                color = "omo";
                diffVar[2] = 1;
                nelements = 50;
                diffVar[3] = 1;
                break;


        }

        parameters.put("nback", nback);
        parameters.put("time", time);
        parameters.put("color", color);
        parameters.put("nelements", nelements);

        return parameters;

    }

    public static Map<String, Object> createParametersExecfunct1(Integer level, Integer[] diffVar)
    {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        int time = 0, nelements = 0;
        String answer = "", color = "";
        switch (level)
        {
            case -1: // training
                answer = "fixed";
                diffVar[0] = 1;
                //answer = "alternate"; diffVar9[0] = 1;
                //answer = "random"; diffVar9[0] = 1;
                time = 4;
                diffVar[1] = 1;
                color = "color";
                diffVar[2] = 1;
                nelements = 10;
                diffVar[3] = 1;
                break;
            case 1: // easy min
                answer = "fixed";
                diffVar[0] = 1;
                time = 4;
                diffVar[1] = 1;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 2:
                answer = "fixed";
                diffVar[0] = 1;
                time = 3;
                diffVar[1] = 2;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 3:
                answer = "fixed";
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 3;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 4:
                answer = "fixed";
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 3;
                color = "omo";
                diffVar[2] = 2;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 5:
                answer = "fixed";
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 6:
                answer = "fixed";
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 60;
                diffVar[3] = 2;
                break;
            case 7: // easy max
                answer = "fixed";
                diffVar[0] = 1;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 100;
                diffVar[3] = 3;
                break;
            case 8: // medium min
                answer = "alternate";
                diffVar[0] = 2;
                time = 4;
                diffVar[1] = 1;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 9:
                answer = "alternate";
                diffVar[0] = 2;
                time = 3;
                diffVar[1] = 2;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 10:
                answer = "alternate";
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 3;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 11:
                answer = "alternate";
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 3;
                color = "omo";
                diffVar[2] = 2;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 12:
                answer = "alternate";
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 13:
                answer = "alternate";
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 60;
                diffVar[3] = 2;
                break;
            case 14: // medium max
                answer = "alternate";
                diffVar[0] = 2;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 100;
                diffVar[3] = 3;
                break;
            case 15: // difficult min
                answer = "random";
                diffVar[0] = 3;
                time = 4;
                diffVar[1] = 1;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 16:
                answer = "random";
                diffVar[0] = 3;
                time = 3;
                diffVar[1] = 2;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 17:
                answer = "random";
                diffVar[0] = 3;
                time = 2;
                diffVar[1] = 3;
                color = "color";
                diffVar[2] = 1;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 18:
                answer = "random";
                diffVar[0] = 3;
                time = 2;
                diffVar[1] = 3;
                color = "omo";
                diffVar[2] = 2;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 19:
                answer = "random";
                diffVar[0] = 3;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 20;
                diffVar[3] = 1;
                break;
            case 20:
                answer = "random";
                diffVar[0] = 3;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 60;
                diffVar[3] = 2;
                break;
            case 21: // difficult max
                answer = "random";
                diffVar[0] = 3;
                time = 2;
                diffVar[1] = 3;
                color = "bw";
                diffVar[2] = 3;
                nelements = 100;
                diffVar[3] = 3;
                break;
        }

        parameters.put("answer", answer);
        parameters.put("time", time);
        parameters.put("color", color);
        parameters.put("nelements", nelements);

        return parameters;

    }

    public static boolean isRLDriven(ExerciseNameValue exName)
    {
        return Lists.of(
                        ATT_SEL_STD_ANM_RL,
                        ATT_SEL_STD_CHS_RL,
                        ATT_SEL_STD_FRT_RL,
                        ATT_SEL_STD_VEG_RL,
                        MEM_VIS_2_ANM_RL,
                        MEM_VIS_2_FRT_RL,
                        MEM_VIS_2_VEG_RL,
                        MEM_VIS_2_CHS_RL,
                        NBACK_ANM_RL,
                        NBACK_FRT_RL,
                        NBACK_VEG_RL,
                        ATT_DIV_FRT_RL,
                        ATT_DIV_ANM_RL,
                        ATT_DIV_CHS_RL,
                        ATT_DIV_VEG_RL,
                        ATT_RFLXS_MOTORBIKE
                )
                .contains(exName);
    }


    /*
     @param lastHistory accept the last history entry for this kind of exercise
     @return newLevel if there is a different level for this entry, elsewere -1
     */
    public static int findChangedLevel(ChangeDifficultyController changeDiffController, List<History> lastHistory)
    {
        int newLevel = -1;
        if (lastHistory != null && !lastHistory.isEmpty())
        {
            History lh = lastHistory.get(0);
            List<ChangeDifficulty> cdlist = changeDiffController.findFromHistory(lh.getId());
            if (cdlist != null && !cdlist.isEmpty())
                newLevel = cdlist.get(0).getLevel();
        }
        return newLevel;
    }


    /**
     * Creates and stores in the db the next assignment for the given exercise, user, session, difficulty and agent.
     * Level and pass threshold of the assignment are decided using the agent, or the incremental strategy (two passes in a row)
     * if not supplied.
     * @param exerciseId
     * @param userId
     * @param sessionId
     * @param difficulty
     * @param rlagent
     * @return
     */
    private History createNextAssigment(
            Integer exerciseId,
            Integer userId,
            Integer sessionId,
            String difficulty,
            @Nullable Integer rlagent,
            @Nullable Integer currentLevel
    )
    {
        Exercise exercise = exerciseController.getEntityOrThrow(exerciseId);
        MSRUser user = userController.getEntityOrThrow(userId);


        History history = new History();


        // Get next level and threshold
        PassThreshold passThreshold;
        if ("training".equals(difficulty) || "demo".equals(difficulty))
        {
            passThreshold = PassThreshold.create(-1, -1, 0);
        }
        else if (isRLDriven(exercise.getName()) && Objects.equals(rlagent, 0))
        {
            // Non usato al momento
            passThreshold = findNextLevelFromAgent(exerciseId, userId, sessionId, difficulty, ExerciseAgent.LEVEL_AGENT.createAgent(exercise, user));
            history.setLevelStrategy(History.LevelStrategy.LEVEL);
        }
        else if (isRLDriven(exercise.getName()) && Objects.equals(rlagent, 1))
        {
            passThreshold = getNextThreshold(exerciseId, userId, sessionId, difficulty);
            history.setLevelStrategy(History.LevelStrategy.ADAPTIVE);
        }
        else
        {
            //passThreshold = findNextLevelFromAgent(exerciseId, userId, sessionId, difficulty, ExerciseAgent.INCREMENTAL_AGENT.createAgent(exercise, userId));
            passThreshold = getNextLevelIncrementally(exerciseId, userId, sessionId, difficulty, exercise, currentLevel);
            history.setLevelStrategy(History.LevelStrategy.INCREMENTAL);
        }



        // Store history for assigned exercise
        history.setExid(exerciseId);
        history.setUserid(userId);
        history.setSessid(sessionId);
        history.setDifficulty(exercise.getDifficulty(passThreshold.getLevel()));
        history.setLevel(passThreshold.getLevel());
        history.setPassThreshold(passThreshold.getThreshold());
        history.setTimestamp(System.currentTimeMillis());
        if (!("training".equals(difficulty) || "demo".equals(difficulty)) ){
        if (!historyController.insertEntity(history))
        {
            logger.error("Error inserting assignment");
            throw new RuntimeException("Error inserting assignment");
        }

        logger.info("Next level/threshold: " + history.getLevel() + "/" + history.getPassThreshold());
    }

        return history;
    }


    private static long epochMillisToEpochDay(long epochMillis)
    {
        return epochMillis / (24L * 60L * 60L * 1000L);
    }


    private PassThreshold getNextLevelIncrementally(
            Integer exerciseId,
            Integer userId,
            Integer sessionId,
            String difficulty,
            Exercise exercise,
            @Nullable Integer currentLevel
    )
    {

        List<History> lastHistory = historyController.findAllSolvedByUserAndExerciseAndSessidAndAgent(userId, exerciseId, sessionId, History.LevelStrategy.INCREMENTAL)
                .stream()
                .filter(h -> epochMillisToEpochDay(h.getTimestamp()) == epochMillisToEpochDay(System.currentTimeMillis()))
                .sorted(Comparator.comparing(History::getTimestamp).reversed())
                .limit(2)
                .collect(Collectors.toList())
                ;


        double threshold = fitnessController.getFitnessWeightOrThrow(exerciseId).getThr();


        if(currentLevel == null)
        {
            // currentLevel == null -> siamo in create
            if (lastHistory.isEmpty())
                return PassThreshold.create(-1, exercise.getLevel(difficulty), threshold);
            else
                return PassThreshold.create(-1, lastHistory.get(0).getLevel(), threshold);
        }
        else
        {
            // currentLevel != null -> siamo in phase3
            if (lastHistory.size() <= 1)
                return PassThreshold.create(-1, currentLevel, threshold);
            else
            {
                History h0 = lastHistory.get(0);
                History h1 = lastHistory.get(1);

                int level = currentLevel;
                if(h0.getPassed() && h1.getPassed() && h0.getLevel() == currentLevel && h1.getLevel() == currentLevel)
                    level += 1;
                return PassThreshold.create(-1, level, threshold);
            }
        }
    }


    private PassThreshold getNextThreshold(Integer exerciseId, Integer userId, Integer sessionId, String difficulty)
    {
        Exercise exercise = exerciseController.getEntityOrThrow(exerciseId);

        ThresholdAgentConfig config = ThresholdAgentConfig.getSingletonEntity(model);

        double lowerThreshold = config.getLowerThreshold();
        double startThreshold = config.getStartThreshold();//da modificare con una differenza dal massimo
        double thresholdDeltaPassed = config.getThresholdDeltaPassed();
        double thresholdDeltaNotPassed = config.getThresholdDeltaNotPassed();

        Optional<History> history = historyController.findLastSolvedByUserAndExerciseAndSessionAndAgent(userId, exerciseId, sessionId, History.LevelStrategy.INCREMENTAL);


        int nextLevel = exercise.getLevel(difficulty);
        double nextThreshold = startThreshold;


        if(!history.isPresent())                                // Previously no exercises were assigned
            return PassThreshold.create(-1, nextLevel, startThreshold);

        History lastHistory = history.get();

        // Non serve in quanto si prendono solo gli esercizi gia risolti
        if (!lastHistory.isSolved())                            // Exercises were assigned but none solved
            return PassThreshold.create(-1, lastHistory.getLevel(), lastHistory.getPassThreshold());


        // Here we know last history was solved so it has performance, threshold, etc.


        // Il campo is passed ha gia considerato l'esito dell'esercizio (performance >= threshold)
        //double performance = lastHistory.getAbsperformance();

        nextLevel = lastHistory.getLevel();
        nextThreshold = lastHistory.getPassThreshold();


        if (lastHistory.getPassed()) // ie. exercise is passed
        {
            nextThreshold = Math.min(nextThreshold + thresholdDeltaPassed, 1);
            nextLevel += 1;
        }
        else
        {
            nextThreshold += thresholdDeltaNotPassed;
            if (nextThreshold <=  lowerThreshold)// startThreshold- diff)//)
            {
                nextThreshold = startThreshold;
                nextLevel -= 1;
            }
        }

        nextLevel = exercise.clampLevel(nextLevel);

        return PassThreshold.create(-1, nextLevel, nextThreshold);
    }


    private PassThreshold findNextLevelFromAgent(Integer exerciseId, Integer userId, Integer sessionId, String difficulty, NextLevelAgent agent)
    {
        Exercise exercise = exerciseController.findEntity(exerciseId).orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        double threshold = fitnessController.getFitnessWeightOrThrow(exerciseId).getThr();

        Optional<History> lastHistory = historyController.findLastByUserAndExerciseAndSessid(userId, exerciseId, sessionId);

        if(lastHistory.isPresent())
            threshold = lastHistory.get().getPassThreshold();

        int level = lastHistory
                .map(h -> agent.getNextLevel(h.getLevel()))
                // Check if the difficulty was changed
                .map(lvl -> changeDiffController.findChangedLevel(exerciseId, userId, sessionId, lvl))
                .map(exercise::clampLevel)
                .orElse(exercise.getLevel(difficulty));

        return PassThreshold.create(-1, level, threshold);
    }



    // RL Exercise

    @RequestMapping(value = "/createattention1", method = RequestMethod.GET)
    public ModelAndView createattention1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            HttpServletRequest request,
            Model model)
    {
        logger.info("createattention1()");

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        String argument = "";

        if (ATT_SEL_STD.toString().equals(type))
            argument = "diffVar1";
        else if (ATT_SEL_STD_FAC.toString().equals(type))
            argument = "diffVar1Fac";
        else if (ATT_SEL_STD_ORI.toString().equals(type))
            argument = "diffVar1Ori";
        diffVar = (Integer[]) (httpSess.getAttribute(argument));
        if (diffVar == null)
            diffVar = new Integer[NUM_FEAT_ATT_1];


        Exercise exercise = exerciseController.getEntityOrThrow(exerciseid);

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, null);

        int level = assignment.getLevel();
        int assignmentId = assignment.getId();

        difficulty = exercise.getDifficulty(level);


        Map<String, Object> parameters = createParametersAttention1(level, diffVar, exname);

        ParametersParser parser = new ParametersParser(parameters);

        Integer ntargets = parser.getIntegerParameter("ntargets", null);
        Integer nelements = parser.getIntegerParameter("nelements", null);
        String alignment = parser.getStringParameter("alignment", null);
        String color = parser.getStringParameter("color", null);
        String distractor = parser.getStringParameter("distractor", null);
        Integer time = parser.getIntegerParameter("time", null);
        Integer ncols = parser.getIntegerParameter("ncols", null);


        if (ATT_SEL_STD.toString().equals(type))
            httpSess.setAttribute("diffVar1", diffVar);
        else if (ATT_SEL_STD_FAC.toString().equals(type))
            httpSess.setAttribute("diffVar1Fac", diffVar);
        else if (ATT_SEL_STD_ORI.toString().equals(type))
            httpSess.setAttribute("diffVar1Ori", diffVar);


        String url = "/attention1phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&ntargets=" + ntargets
                + "&nelements=" + nelements
                + "&alignment=" + alignment
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&ncols=" + ncols
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignmentId
                ;


        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/attention1", method = RequestMethod.GET)
    public ModelAndView attention1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = false) ExerciseNameValue exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.info("attention1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        model.addAttribute("rl", isRLDriven(exname));
        return new ModelAndView("attention1");
    }

    @RequestMapping(value = "/attention1phase1", method = RequestMethod.GET)
    public ModelAndView attention1phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "alignment", required = true) String alignment,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "ncols", required = true) Integer ncols,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            Model model)
    {
        logger.info("attention1phase1()");

        List<ExElement> exElementList = exElementController.getRandomRecordsByCategory(CategoryValue.valueOf(category), nelements);
        List<ExElement> targetElementList = exElementController.sampleElements(ntargets, exElementList);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("alignment", alignment);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("ncols", ncols);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("targetElementList", targetElementList);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("assignmentid", assignmentid);
        model.addAttribute("rlagent", rlagent);
        return new ModelAndView("attention1a");
    }


    @RequestMapping(value = "/attention1phase2", method = RequestMethod.GET)
    public ModelAndView attention1phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "alignment", required = true) String alignment,
            @RequestParam(value = "targetElementList", required = true) String targetElementIds,
            @RequestParam(value = "exElementList", required = true) String exElementIds,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "ncols", required = true) Integer ncols,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {
        logger.info("attention1phase2()");

        List<ExElement> l = buildExElementListFromIds(exElementIds);
        List<ExElement> l1 = buildExElementListFromIds(targetElementIds);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("targetElementList", l1);
        model.addAttribute("exElementList", l);
        model.addAttribute("alignment", alignment);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("ncols", ncols);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("assignmentid", assignmentid);
        model.addAttribute("rlagent", rlagent);
        return new ModelAndView("attention1b");
    }

    @RequestMapping(value = "/attention1phase3", method = RequestMethod.GET)
    public ModelAndView attention1phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "alignment", required = true) String alignment,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "ncols", required = true) Integer ncols,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) Exercise.ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model)
    {
        logger.info("attention1phase2()");
        if (patientid == -1)
        {
            if (difficulty.equals("training"))
                return new ModelAndView("redirect: patienttraining");
            else
                return new ModelAndView("redirect: patientdemo");
        }

        if (difficulty.equals("training") || difficulty.equals("demo"))
            return new ModelAndView("redirect: patientrehabilitation");

        Exercise exercise = exerciseController.getEntityOrThrow(exerciseid);
        History history = historyController.getEntityOrThrow(assignmentid);

        history.setExid(exerciseid);
        history.setPassed(passed);
        history.setUserid(patientid);
        history.setpTime(pTime);
        history.setpCorrect(pCorrect);
        history.setpMissed(pMissed);
        history.setpWrong(pWrong);
        history.setMaxtime((double) time * nelements);
        history.setPassed(passed);
        history.setLevel(level);
        history.setDifficulty(difficulty);
        history.setSessid(sessid);
        // Copiato dall'altro  Non si capisce perche qui non c'era?
        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setTimestamp(t);



        // Calculate performance
        double ft;
        ft = fitnessController.calculateFitness(false, history);

        history.setAbsperformance(ft);

        ft = fitnessController.calculateFitness(true, history);
        history.setRelperformance(ft);

        if (!historyController.putEntity(history))
        {
            logger.error("Error adding History to DB");
            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
            model.addAttribute("back", "patienthome");
            model.addAttribute("home", "patienthome");
            return new ModelAndView("error");
        }

        if (Objects.equals(level, exercise.getMaxLevel()) && passed) // Max level is completed, mark session as done and redirect user to home
        {
            if (!sessionController.updateExerciseDone(sessid, exerciseid, true))
            {
                logger.error("Error updating MSRSession to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
            return new ModelAndView("redirect:patienthome");
        }


        // Forward user to the next exercise
//siamo sicuri che la vecchia history sia sul db salvata e committata

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, level);
        level = assignment.getLevel();

        difficulty = exercise.getDifficulty(level);


        HttpSession httpSess = request.getSession();

        Integer[] diffVar = null;
        if (ATT_SEL_STD.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar1"));
        else if (ATT_SEL_STD_FAC.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar1Fac"));
        else if (ATT_SEL_STD_ORI.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar1Ori"));


        Map<String, Object> parameters = createParametersAttention1(level, diffVar, exname);

        ParametersParser parser = new ParametersParser(parameters);

        ntargets = parser.getIntegerParameter("ntargets", null);
        nelements = parser.getIntegerParameter("nelements", null);
        alignment = parser.getStringParameter("alignment", null);
        color = parser.getStringParameter("color", null);
        distractor = parser.getStringParameter("distractor", null);
        time = parser.getIntegerParameter("time", null);
        ncols = parser.getIntegerParameter("ncols", null);


        String url = "/attention1phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + passed
                + "&ntargets=" + ntargets
                + "&nelements=" + nelements
                + "&alignment=" + alignment
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&ncols=" + ncols
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignment.getId()
                ;



        return new ModelAndView("redirect:" + url);
    }



    @RequestMapping(value = "/createattention2", method = RequestMethod.GET)
    public ModelAndView createattention2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        String argument = "";

        if (ATT_SEL_STD.toString().equals(type))
            argument = "diffVar2";
        else if (ATT_SEL_STD_FAC.toString().equals(type))
            argument = "diffVar2Fac";
        else if (ATT_SEL_STD_ORI.toString().equals(type))
            argument = "diffVar2Ori";

        diffVar = (Integer[]) (httpSess.getAttribute(argument));
        if (diffVar == null)
            diffVar = new Integer[NUM_FEAT_ATT_2];


        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, null, null);
        int level = assignment.getLevel();

        Map<String, Object> parameters = createParametersAttention2(level, diffVar);
        if (ATT_SEL_FLW.toString().equals(type))
        {
            httpSess.setAttribute("diffVar2", diffVar);
        } else if (ATT_SEL_FLW_FAC.toString().equals(type))
        {
            httpSess.setAttribute("diffVar2Fac", diffVar);
        } else if (ATT_SEL_FLW_ORI.toString().equals(type))
        {
            httpSess.setAttribute("diffVar2Ori", diffVar);
        }
        Integer ntargets = (Integer) (parameters.get("ntargets"));
        Integer nelements = (Integer) (parameters.get("nelements"));
        String color = (String) (parameters.get("color"));
        String distractor = (String) (parameters.get("distractor"));
        //Integer time = (Integer) (parameters.get("time"));
        Double time = (Double) (parameters.get("time"));

        String url = "/attention2phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&ntargets=" + ntargets
                + "&nelements=" + nelements
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&assignmentid=" + assignment.getId();
                ;
        return new ModelAndView("redirect:" + url);
    }



    @RequestMapping(value = "/attention2", method = RequestMethod.GET)
    public ModelAndView attention2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("attention2()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("attention2");
    }

    @RequestMapping(value = "/attention2phase1", method = RequestMethod.GET)
    public ModelAndView attention2phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            //@RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "time", required = true) Double time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {

        logger.debug("attention2phase1()");

        List<ExElement> exElementList = exElementController.getRandomRecordsByCategory(CategoryValue.valueOf(category), nelements);
        List<ExElement> targetElementList = exElementController.sampleElements(ntargets, exElementList);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("nelements", nelements);
        model.addAttribute("targetElementList", targetElementList);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("assignmentid", assignmentid);
        return new ModelAndView("attention2a");
    }

    @RequestMapping(value = "/attention2phase2", method = RequestMethod.GET)
    public ModelAndView attention2phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "targetElementList", required = true) String targetElementIds,
            @RequestParam(value = "exElementList", required = true) String exElementIds,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            //@RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "time", required = true) Double time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {

        logger.debug("attention2phase2()");

        List<ExElement> l = buildExElementListFromIds(exElementIds);
        List<ExElement> l1 = buildExElementListFromIds(targetElementIds);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("targetElementList", l1);
        model.addAttribute("exElementList", l);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("assignmentid", assignmentid);
        return new ModelAndView("attention2b");
    }


    @RequestMapping(value = "/attention2phase3", method = RequestMethod.GET)
    public ModelAndView attention2phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            //@RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "time", required = true) Double time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws ServletException, IOException
    {

        if (patientid == -1)
        {
            if (difficulty.equals("training"))
            {
                return new ModelAndView("redirect: patienttraining");
            } else
            {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else
        {
            if (difficulty.equals("training") || difficulty.equals("demo"))
            {
                return new ModelAndView("redirect: patientrehabilitation");
            } else
            {
                int newLevel = changeDiffController.findChangedLevel(exerciseid, patientid, sessid, -1);

                History history = historyController.getEntityOrThrow(assignmentid);
                history.setExid(exerciseid);
                history.setPassed(passed);
                history.setUserid(patientid);
                history.setpTime(pTime);
                history.setpCorrect(pCorrect);
                history.setpMissed(pMissed);
                history.setpWrong(pWrong);
                history.setMaxtime((double) time * nelements);
                history.setDifficulty(difficulty);

                // Calculate performance
                double ft = fitnessController.calculateFitness(false, history);
                history.setAbsperformance(ft);
                ft = fitnessController.calculateFitness(true, history);
                history.setRelperformance(ft);

                history.setPassed(passed);

                history.setLevel(level);
                history.setSessid(sessid);

                Long t = LocalDateTime.now().toDateTime().getMillis();
                history.setTimestamp(t);

                String url;
                HttpSession httpSess = request.getSession();

                Integer[] diffVar = null;
                if (ATT_SEL_FLW.toString().equals(type))
                {
                    diffVar = (Integer[]) (httpSess.getAttribute("diffVar2"));
                } else if (ATT_SEL_FLW_FAC.toString().equals(type))
                {
                    diffVar = (Integer[]) (httpSess.getAttribute("diffVar2Fac"));
                } else if (ATT_SEL_FLW_ORI.toString().equals(type))
                {
                    diffVar = (Integer[]) (httpSess.getAttribute("diffVar2Ori"));
                }

                // Check if the difficulty has been
                // increased / decreased by the operator
                if ((lastexercisepassed && passed) || (newLevel != -1))
                {
                    // increase difficulty
                    if (newLevel != -1)
                    {
                        level = newLevel;
                    } else
                    {
                        level++;
                    }

                    if (level >= 1 && level <= 3)
                    {
                        difficulty = "easy";
                    } else if (level >= 4 && level <= 7)
                    {
                        difficulty = "medium";
                    } else if (level >= 8)
                    {
                        difficulty = "difficult";
                    }


                    //  if (allMaxFeatures) {
                    if (level > 12)
                    {
                        MSRSession sess = sessionController.findEntity(sessid).orElse(null);
                        JSONArray jsonArr = new JSONArray(sess.getExercises());
                        JSONObject json;
                        boolean sessionEnded = true;
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            json = jsonArr.getJSONObject(i);
                            if (json.getInt("id") == exerciseid)
                            {
                                json.put("done", true);
                                jsonArr.put(i, json);
                            }
                            sessionEnded = sessionEnded && json.getBoolean("done");
                        }
                        sess.setExercises(jsonArr.toString());
                        sess.setActive(!sessionEnded);
                        if (!sessionController.updateEntity(sess))
                        {
                            logger.error("Error updating MSRSession to DB");
                            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                            model.addAttribute("back", "patienthome");
                            model.addAttribute("home", "patienthome");
                            return new ModelAndView("error");
                        }
                        url = "patienthome";
                    } else
                    {
                        switch (level)
                        {

                            case 1: // easy min
                                ntargets = 2;
                                diffVar[0] = 1;
                                nelements = 30;
                                diffVar[1] = 1;
                                //nelements = 5; diffVar[1] = 1;
                                distractor = "no";
                                diffVar[2] = 1;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 2:
                                ntargets = 3;
                                diffVar[0] = 2;
                                nelements = 30;
                                diffVar[1] = 1;
                                //nelements = 5; diffVar[1] = 1;
                                distractor = "no";
                                diffVar[2] = 1;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 3:
                                ntargets = 3;
                                diffVar[0] = 2;
                                nelements = 48;
                                diffVar[1] = 2;
                                //nelements = 10; diffVar[1] = 2;
                                distractor = "no";
                                diffVar[2] = 1;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 4: // medium min
                                ntargets = 3;
                                diffVar[0] = 2;
                                nelements = 48;
                                diffVar[1] = 2;
                                //nelements = 10; diffVar[1] = 2;
                                distractor = "no";
                                diffVar[2] = 2;
                                //time = 4;
                                time = 2.0;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 5:
                                ntargets = 3;
                                diffVar[0] = 2;
                                nelements = 48;
                                diffVar[1] = 2;
                                //nelements = 10; diffVar[1] = 2;
                                distractor = "no";
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 1;
                                break;
                            case 6:
                                ntargets = 3;
                                diffVar[0] = 2;
                                nelements = 66;
                                diffVar[1] = 2;
                                //nelements = 10; diffVar[1] = 2;
                                distractor = "no";
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 7:
                                ntargets = 3;
                                diffVar[0] = 3;
                                nelements = 80;
                                diffVar[1] = 2;
                                //nelements = 10; diffVar[1] = 2;
                                distractor = "no";
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 8: // difficult min
                                ntargets = 4;
                                diffVar[0] = 3;
                                nelements = 66;
                                diffVar[1] = 3;
                                //nelements = 15; diffVar[1] = 3;
                                distractor = "no";
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 9:
                                ntargets = 4;
                                diffVar[0] = 3;
                                nelements = 66;
                                diffVar[1] = 3;
                                //nelements = 15; diffVar[1] = 3;
                                distractor = "no";
                                diffVar[2] = 3;
                                //time = 3;
                                time = 1.5;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 10:
                                ntargets = 4;
                                diffVar[0] = 3;
                                nelements = 80;
                                diffVar[1] = 3;
                                //nelements = 15; diffVar[1] = 3;
                                distractor = "no";
                                diffVar[2] = 3;
                                //time = 2;
                                time = 1.5;
                                diffVar[3] = 3;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 11:
                                ntargets = 5;
                                diffVar[0] = 3;
                                nelements = 66;
                                diffVar[1] = 3;
                                //nelements = 15; diffVar[1] = 3;
                                distractor = "no";
                                diffVar[2] = 3;
                                //time = 2;
                                time = 1.5;
                                diffVar[3] = 3;
                                color = "omo";
                                diffVar[4] = 3;
                                break;
                            case 12:
                                ntargets = 5;
                                diffVar[0] = 3;
                                nelements = 80;
                                diffVar[1] = 3;
                                //nelements = 15; diffVar[1] = 3;
                                distractor = "no";
                                diffVar[2] = 3;
                                //time = 2;
                                time = 1.5;
                                diffVar[3] = 3;
                                color = "omo";
                                diffVar[4] = 3;
                                break;
                        }


                        History nextAssignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, -1, level);

                        url = "/attention2phase1"
                                + "?difficulty=" + difficulty
                                + "&level=" + level
                                + "&patientid=" + patientid
                                + "&exerciseid=" + exerciseid
                                + "&category=" + category
                                + "&lastexercisepassed=" + false
                                + "&ntargets=" + ntargets
                                + "&nelements=" + nelements
                                + "&color=" + color
                                + "&distractor=" + distractor
                                + "&time=" + time
                                + "&sessid=" + sessid
                                + "&type=" + type
                                + "&exname=" + exname
                                + "&assignmentid=" + nextAssignment.getId();
                        ;
                    }
                } else
                {
                    // same difficulty different exercise
                    url = "/attention2phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + level
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&category=" + category
                            + "&lastexercisepassed=" + passed
                            + "&ntargets=" + ntargets
                            + "&nelements=" + nelements
                            + "&color=" + color
                            + "&distractor=" + distractor
                            + "&time=" + time
                            + "&sessid=" + sessid
                            + "&type=" + type
                            + "&exname=" + exname;
                }

                if (!historyController.putEntity(history))
                {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }

                if (ATT_SEL_FLW.toString().equals(type))
                {
                    httpSess.setAttribute("diffVar2", diffVar);
                } else if (ATT_SEL_FLW_FAC.toString().equals(type))
                {
                    httpSess.setAttribute("diffVar2Fac", diffVar);
                } else if (ATT_SEL_FLW_ORI.toString().equals(type))
                {
                    httpSess.setAttribute("diffVar2Ori", diffVar);
                }

                return new ModelAndView("redirect:" + url);
            }
        }
    }

    @RequestMapping(value = "/createattention3", method = RequestMethod.GET)
    public ModelAndView createattention3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        String argument = "";
        if (ATT_SEL_STD.toString().equals(type))
        {
            argument = "diffVar3";
        } else if (ATT_SEL_STD_FAC.toString().equals(type))
        {
            argument = "diffVar3Fac";
        } else if (ATT_SEL_STD_ORI.toString().equals(type))
        {
            argument = "diffVar3Ori";
        }
        diffVar = (Integer[]) (httpSess.getAttribute(argument));
        if (diffVar == null)
        {
            diffVar = new Integer[NUM_FEAT_ATT_3];
        }

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, null, null);
        int level = assignment.getLevel();

        Map<String, Object> parameters = createParametersAttention3(level, diffVar);
        if (ATT_ALT.toString().equals(type))
            httpSess.setAttribute("diffVar3", diffVar);
        else if (ATT_ALT_FAC.toString().equals(type))
            httpSess.setAttribute("diffVar3Fac", diffVar);
        else if (ATT_ALT_ORI.toString().equals(type))
            httpSess.setAttribute("diffVar3Ori", diffVar);


        Integer iterations = (Integer) (parameters.get("iterations"));
        Integer nelementspertarget = (Integer) (parameters.get("nelementspertarget"));
        String color = (String) (parameters.get("color"));
        Double frequenza = (Double) (parameters.get("frequenza"));
        //Integer time = (Integer) (parameters.get("time"));
        Double time = (Double) (parameters.get("time"));

        String url = "/attention3phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&iterations=" + iterations
                + "&nelementspertarget=" + nelementspertarget
                + "&color=" + color
                + "&frequenza=" + frequenza
                + "&time=" + time
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname;
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/attention3", method = RequestMethod.GET)
    public ModelAndView attention3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("attention3()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("attention3");
    }

    @RequestMapping(value = "/attention3phase1", method = RequestMethod.GET)
    public ModelAndView attention3phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "iterations", required = true) Integer iterations,
            @RequestParam(value = "nelementspertarget", required = true) Integer nelementspertarget,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "frequenza", required = true) Double frequenza,
            //@RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "time", required = true) Double time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model)
    {

        logger.debug("attention3phase1()");

        CategoryValue categoryValue = CategoryValue.valueOf(category);


        ExElement targetelement1 = exElementController.getRandomRecordsByCategory(categoryValue, 1).get(0);

        ExElement targetelement2;
        if (ATT_ALT_CMP.toString().equals(exname))
        {
            targetelement2 = exElementController.getAllRecordsByCategory(categoryValue)
                    .stream()
                    .sorted(ScoreComparator.randomComparator())
                    .filter(el -> !Objects.equals(el.getEldescr(), targetelement1.getEldescr()))
                    .findFirst()
                    .orElse(targetelement1);

        } else
        {
            targetelement2 = exElementController.getAllRecordsByCategory(categoryValue)
                    .stream()
                    .sorted(ScoreComparator.randomComparator())
                    .filter(el -> !Objects.equals(el.getId(), targetelement1.getId()))
                    .findFirst()
                    .orElse(targetelement1);
        }

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("leftiterations", iterations);
        model.addAttribute("iterations", iterations);
        model.addAttribute("targetelement1", targetelement1);
        model.addAttribute("targetelement2", targetelement2);
        model.addAttribute("firsttarget", true);
        model.addAttribute("color", color);
        model.addAttribute("frequenza", frequenza);
        model.addAttribute("time", time);
        model.addAttribute("nelementspertarget", nelementspertarget);
        model.addAttribute("passed", true);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);

        return new ModelAndView("attention3a");
    }


    @RequestMapping(value = "/attention3phase2", method = RequestMethod.GET)
    public ModelAndView attention3phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "leftiterations", required = true) Integer leftiterations,
            @RequestParam(value = "iterations", required = true) Integer iterations,
            @RequestParam(value = "targetelement1", required = true) String targetelement1,
            @RequestParam(value = "targetelement2", required = true) String targetelement2,
            @RequestParam(value = "firsttarget", required = true) Boolean firsttarget,
            @RequestParam(value = "nelementspertarget", required = true) Integer nelementspertarget,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "frequenza", required = true) Double frequenza,
            //@RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "time", required = true) Double time,
            //@RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "pTime", required = false, defaultValue = "0") Double pTime,
            @RequestParam(value = "pCorrect", required = false, defaultValue = "0") Integer pCorrect,
            @RequestParam(value = "pMissed", required = false, defaultValue = "0") Integer pMissed,
            @RequestParam(value = "pWrong", required = false, defaultValue = "0") Integer pWrong,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model)
    {

        logger.debug("attention3phase2()");
        ExElement t0;
        Integer targetId = Integer.parseInt(targetelement1);
        ExElement target = exElementController.findEntity(targetId).orElse(null);

        Integer noTargetId = Integer.parseInt(targetelement2);
        ExElement noTarget = exElementController.findEntity(noTargetId).orElse(null);

        //  nelementspertarget=nelementspertarget*2;
        List<ExElement> exElementList = new ArrayList<>();
        //  = exElementController.getRandomRecordsByCategory(CategoryValue.valueOf(category), nelementspertarget);


        for (int i = 0; i < iterations; i++)
        {
            List<ExElement> exElementList2
                    = exElementController.getRandomRecordsByCategory(CategoryValue.valueOf(category), nelementspertarget);

            int nElementiTarget = (int) Math.round(nelementspertarget * frequenza);

            ExElement nt0;
            int index;
            if (i % 2 == 0)
            {
                t0 = target;
                nt0 = noTarget;
            } else
            {
                t0 = noTarget;
                nt0 = target;
            }

            int cntSubstitutionsTarget = 0;
            //inizio modifica

            int conta = exElementList2.stream()
                    .mapToInt(el -> Objects.equals(target, el) ? 1 : 0)
                    .sum();


            cntSubstitutionsTarget = nElementiTarget - conta;

            if (cntSubstitutionsTarget > 0)
            {

                for (int ii = 0; ii < exElementList2.size() - 1 && cntSubstitutionsTarget > 0; ii++)
                {
                    if (!Objects.equals(target, exElementList2.get(ii)))
                    {
                        exElementList2.set(ii, t0);
                        cntSubstitutionsTarget--;
                    }
                }

            } else if (cntSubstitutionsTarget < 0)
            {

                for (int ii = 0; ii < exElementList2.size() - 1 && cntSubstitutionsTarget < 0; ii++)
                {
                    if (Objects.equals(target, exElementList2.get(ii)))
                    {
                        exElementList2.set(ii, nt0);
                        cntSubstitutionsTarget++;
                    }
                }
            }

            for (int ii = 0; ii < (exElementList2.size() - 1) / 2; ii++)
            {
                index = (int) (Math.random() * nelementspertarget);
                int posDaSpostare = exElementList2.size() - (1 + ii);
                ExElement e = exElementList2.get(index);
                exElementList2.set(index, exElementList2.get(posDaSpostare));
                exElementList2.set(posDaSpostare, e);
            }

            //fine modifica per frequenza

            exElementList.addAll(exElementList2);
        }

        //Fine aggiunta
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("iterations", iterations);
        model.addAttribute("leftiterations", --leftiterations);
        model.addAttribute("targetelement1", targetelement1);
        model.addAttribute("targetelement2", targetelement2);
        if (ATT_ALT_CMP.toString().equals(exname))
        {
            model.addAttribute("targetelement1img", exElementController.findEntity(Integer.parseInt(targetelement1)).orElse(null).getEldescr());
            model.addAttribute("targetelement2img", exElementController.findEntity(Integer.parseInt(targetelement2)).orElse(null).getEldescr());
        } else
        {
            model.addAttribute("targetelement1img", exElementController.findEntity(Integer.parseInt(targetelement1)).orElse(null).getUrl());
            model.addAttribute("targetelement2img", exElementController.findEntity(Integer.parseInt(targetelement2)).orElse(null).getUrl());
        }
        model.addAttribute("firsttarget", !firsttarget);

        model.addAttribute("exElementList", exElementList);
        model.addAttribute("nelementspertarget", nelementspertarget);
        model.addAttribute("target", target);
        model.addAttribute("noTarget", noTarget);
        model.addAttribute("color", color);
        model.addAttribute("frequenza", frequenza);
        model.addAttribute("time", time);
        //model.addAttribute("passed", passed);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);

        if (pCorrect != null)
        {
            model.addAttribute("pCorrect", pCorrect);
            model.addAttribute("pMissed", pMissed);
            model.addAttribute("pWrong", pWrong);
            model.addAttribute("pTime", pTime);
        } else
        {
            model.addAttribute("pCorrect", 0);
            model.addAttribute("pMissed", 0);
            model.addAttribute("pWrong", 0);
            model.addAttribute("pTime", 0);
        }
        return new ModelAndView("attention3b");

    }


    //fine modifica

    @RequestMapping(value = "/attention3phase3", method = RequestMethod.GET)
    public ModelAndView attention3phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelementspertarget", required = true) Integer nelementspertarget,
            @RequestParam(value = "iterations", required = true) Integer iterations,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "frequenza", required = true) Double frequenza,
            //@RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "time", required = true) Double time,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {

        if (patientid == -1)
        {
            if (difficulty.equals("training"))
            {
                return new ModelAndView("redirect: patienttraining");
            } else
            {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else
        {
            if (difficulty.equals("training") || difficulty.equals("demo"))
            {
                return new ModelAndView("redirect: patientrehabilitation");
            } else
            {
                List<History> lastHistory = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
                int newLevel = findChangedLevel(changeDiffController, lastHistory);

                History history = historyController.findLastUnsolvedByUserAndExerciseAndSession(patientid, exerciseid, sessid)
                        .orElseThrow(() -> new IllegalStateException("An assigned history must be present"));
                ;

                history.setExid(exerciseid);
                history.setPassed(passed);
                history.setUserid(patientid);
                history.setpTime(pTime);
                history.setpCorrect(pCorrect);
                history.setpMissed(pMissed);
                history.setpWrong(pWrong);
                history.setMaxtime(time * nelementspertarget * iterations);
                history.setDifficulty(difficulty);

                // Calculate performance
                double ft = Double.NaN;
                ft = fitnessController.calculateFitness(false, history);
                history.setAbsperformance(ft);
                ft = fitnessController.calculateFitness(true, history);
                history.setRelperformance(ft);

                history.setPassed(passed);
                history.setPassThreshold(fitnessController.getFitnessWeightOrThrow(exerciseid).getThr());
                history.setLevel(level);
                history.setSessid(sessid);
                history.setLevelStrategy(History.LevelStrategy.INCREMENTAL);

                Long t = LocalDateTime.now().toDateTime().getMillis();
                history.setTimestamp(t);
                
               /* if (historyController.addRecord(history)==-1) {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");                    
                }*/

                String url;
                HttpSession httpSess = request.getSession();

                Integer[] diffVar = null;
                if (ATT_ALT.toString().equals(type))
                {
                    diffVar = (Integer[]) (httpSess.getAttribute("diffVar3"));
                } else if (ATT_ALT_FAC.toString().equals(type))
                {
                    diffVar = (Integer[]) (httpSess.getAttribute("diffVar3Fac"));
                } else if (ATT_ALT_ORI.toString().equals(type))
                {
                    diffVar = (Integer[]) (httpSess.getAttribute("diffVar3Ori"));
                }

                // Check if the difficulty has been
                // increased / decreased by the operator            
                if ((lastexercisepassed && passed) || (newLevel != -1))
                {
                    // increase difficulty
                    if (newLevel != -1)
                    {
                        level = newLevel;
                    } else
                    {
                        level++;
                    }
                    if (level >= 1 && level <= 3)
                    {
                        difficulty = "easy";
                    } else if (level >= 4 && level <= 7)
                    {
                        difficulty = "medium";
                    } else if (level >= 8)
                    {
                        difficulty = "difficult";
                    }
                    history.setDifficulty(difficulty);
                    history.setLevel(level);
 
                
               /*     boolean allMaxFeatures = true;
                    for (int i = 0; i < NUM_FEAT_ATT_3-1; i++) {
                        allMaxFeatures = allMaxFeatures && diffVar[i] == 3;
                    }
                    // No bw in exercises with arrows
                    if(ATT_ALT_ORI.toString().equals(type)) {
                        allMaxFeatures =  allMaxFeatures && diffVar[NUM_FEAT_ATT_3-1] == 2;
                    }
                    else {
                        allMaxFeatures =  allMaxFeatures && diffVar[NUM_FEAT_ATT_3-1] == 3;
                    }*/


                    if (level > 11)
                    {
                        MSRSession sess = sessionController.findEntity(sessid).orElse(null);
                        JSONArray jsonArr = new JSONArray(sess.getExercises());
                        JSONObject json;
                        boolean sessionEnded = true;
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            json = jsonArr.getJSONObject(i);
                            if (json.getInt("id") == exerciseid)
                            {
                                json.put("done", true);
                                jsonArr.put(i, json);
                            }
                            sessionEnded = sessionEnded && json.getBoolean("done");
                        }
                        sess.setExercises(jsonArr.toString());
                        sess.setActive(!sessionEnded);
                        if (!sessionController.updateEntity(sess))
                        {
                            logger.error("Error updating MSRSession to DB");
                            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                            model.addAttribute("back", "patienthome");
                            model.addAttribute("home", "patienthome");
                            return new ModelAndView("error");
                        }
                        url = "patienthome";
                    } else
                    {

                        switch (level)
                        {
                            case 1: // easy min
                                iterations = 2;
                                diffVar[0] = 1;
                                nelementspertarget = 20;
                                diffVar[1] = 1;
                                frequenza = 0.6;
                                diffVar[2] = 1;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 2:
                                iterations = 3;
                                diffVar[0] = 2;
                                nelementspertarget = 20;
                                diffVar[1] = 1;
                                frequenza = 0.6;
                                diffVar[2] = 1;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 3:
                                iterations = 3;
                                diffVar[0] = 2;
                                nelementspertarget = 15;
                                diffVar[1] = 2;
                                frequenza = 0.6;
                                diffVar[2] = 1;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 4: // medium min
                                iterations = 3;
                                diffVar[0] = 2;
                                nelementspertarget = 15;
                                diffVar[1] = 2;
                                frequenza = 0.5;
                                diffVar[2] = 2;
                                //time = 4;
                                time = 2.5;
                                diffVar[3] = 1;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 5:
                                iterations = 3;
                                diffVar[0] = 2;
                                nelementspertarget = 15;
                                diffVar[1] = 2;
                                frequenza = 0.5;
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "color";
                                diffVar[4] = 1;
                                break;
                            case 6:
                                iterations = 3;
                                diffVar[0] = 2;
                                nelementspertarget = 15;
                                diffVar[1] = 2;
                                frequenza = 0.4;
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 7:
                                iterations = 4;
                                diffVar[0] = 3;
                                nelementspertarget = 15;
                                diffVar[1] = 2;
                                frequenza = 0.4;
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 8: // difficult min
                                iterations = 4;
                                diffVar[0] = 3;
                                nelementspertarget = 10;
                                diffVar[1] = 3;
                                frequenza = 0.4;
                                diffVar[2] = 2;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 9:
                                iterations = 4;
                                diffVar[0] = 3;
                                nelementspertarget = 10;
                                diffVar[1] = 3;
                                frequenza = 0.3;
                                diffVar[2] = 3;
                                //time = 3;
                                time = 2.0;
                                diffVar[3] = 2;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 10:
                                iterations = 4;
                                diffVar[0] = 3;
                                nelementspertarget = 10;
                                diffVar[1] = 3;
                                frequenza = 0.2;
                                diffVar[2] = 3;
                                //time = 2;
                                time = 1.5;
                                diffVar[3] = 3;
                                color = "omo";
                                diffVar[4] = 2;
                                break;
                            case 11:
                                iterations = 4;
                                diffVar[0] = 3;
                                nelementspertarget = 10;
                                diffVar[1] = 3;
                                frequenza = 0.2;
                                diffVar[2] = 3;
                                //time = 2;
                                time = 1.5;
                                diffVar[3] = 3;
                                color = "bw";
                                diffVar[4] = 3;
                                break;
                        }

                        url = "/attention3phase1"
                                + "?difficulty=" + difficulty
                                + "&level=" + level
                                + "&patientid=" + patientid
                                + "&exerciseid=" + exerciseid
                                + "&category=" + category
                                + "&lastexercisepassed=" + false
                                + "&iterations=" + iterations
                                + "&nelementspertarget=" + nelementspertarget
                                + "&color=" + color
                                + "&frequenza=" + frequenza
                                + "&time=" + time
                                + "&leftiterations=" + iterations
                                + "&passed=" + true
                                + "&sessid=" + sessid
                                + "&type=" + type
                                + "&exname=" + exname;
                    }
                } else
                {
                    // same difficulty different exercise
                    url = "/attention3phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + level
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&category=" + category
                            + "&lastexercisepassed=" + passed
                            + "&iterations=" + iterations
                            + "&nelementspertarget=" + nelementspertarget
                            + "&color=" + color
                            + "&frequenza=" + frequenza
                            + "&time=" + time
                            + "&leftiterations=" + iterations
                            + "&passed=" + true
                            + "&sessid=" + sessid
                            + "&type=" + type
                            + "&exname=" + exname;
                }

                if (!historyController.putEntity(history))
                {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }
                if (ATT_ALT.toString().equals(type))
                    httpSess.setAttribute("diffVar3", diffVar);
                else if (ATT_ALT_FAC.toString().equals(type))
                    httpSess.setAttribute("diffVar3Fac", diffVar);
                else if (ATT_ALT_ORI.toString().equals(type))
                    httpSess.setAttribute("diffVar3Ori", diffVar);

                return new ModelAndView("redirect:" + url);
            }
        }
    }

    // RL Exercise

    @RequestMapping(value = "/createattention4", method = RequestMethod.GET)
    public ModelAndView createattention4(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        String argument = "";
        if (ATT_SEL_STD.toString().equals(type))
        {
            argument = "diffVar4";
        } else if (ATT_SEL_STD_FAC.toString().equals(type))
        {
            argument = "diffVar4Fac";
        } else if (ATT_SEL_STD_ORI.toString().equals(type))
        {
            argument = "diffVar4Ori";
        }
        diffVar = (Integer[]) (httpSess.getAttribute(argument));
        if (diffVar == null)
        {
            diffVar = new Integer[NUM_FEAT_ATT_4];
        }

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, null);
        int level = assignment.getLevel();

        // Uguale a creationattention1 viene individuato il livello di difficolta dato il livello dell'esercizio
        Exercise exercise = exerciseController.getEntityOrThrow(exerciseid);
        difficulty=exercise.getDifficulty(level);


        Map<String, Object> parameters = createParametersAttention4(level, diffVar);
        if (ATT_DIV.toString().equals(type))
        {
            httpSess.setAttribute("diffVar4", diffVar);
        } else if (ATT_DIV_FAC.toString().equals(type))
        {
            httpSess.setAttribute("diffVar4Fac", diffVar);
        } else if (ATT_DIV_ORI.toString().equals(type))
        {
            httpSess.setAttribute("diffVar4Ori", diffVar);
        }


        Integer ntargets = (Integer) (parameters.get("ntargets"));
        Integer nelements = (Integer) (parameters.get("nelements"));
        String color = (String) (parameters.get("color"));
        String distractor = (String) (parameters.get("distractor"));
        Integer time = (Integer) (parameters.get("time"));
        String soundinterval = (String) (parameters.get("soundinterval"));


        String url = "/attention4phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&ntargets=" + ntargets
                + "&nelements=" + nelements
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&soundinterval=" + soundinterval
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignment.getId()
                ;
        return new ModelAndView("redirect:" + url);
    }


    @RequestMapping(value = "/attention4", method = RequestMethod.GET)
    public ModelAndView attention4(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = false) ExerciseNameValue exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("attention4()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        model.addAttribute("rl", isRLDriven(exname));
        return new ModelAndView("attention4");
    }

    @RequestMapping(value = "/attention4phase1", method = RequestMethod.GET)
    public ModelAndView attention4phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "soundinterval", required = true) String soundinterval,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {

        logger.debug("attention4phase1()");

        List<ExElement> exElementList = exElementController.getRandomRecordsByCategory(CategoryValue.valueOf(category), nelements);
        List<ExElement> targetElementList = exElementController.sampleElements(ntargets, exElementList);

        exElementController.increaseFrequencyOfElements(exElementList, targetElementList, 0.2);


        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("nelements", nelements);
        model.addAttribute("targetElementList", targetElementList);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("soundinterval", soundinterval);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("assignmentid", assignmentid);

        return new ModelAndView("attention4a");
    }

    @RequestMapping(value = "/attention4phase2", method = RequestMethod.GET)
    public ModelAndView attention4phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "targetElementIds", required = true) String targetElementIds,
            @RequestParam(value = "exElementIds", required = true) String exElementIds,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "soundinterval", required = true) String soundinterval,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {
        logger.debug("attention4phase2()");

        List<ExElement> l = buildExElementListFromIds(exElementIds);
        List<ExElement> l1 = buildExElementListFromIds(targetElementIds);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("targetElementList", l1);
        model.addAttribute("exElementList", l);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("soundinterval", soundinterval);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("assignmentid", assignmentid);
        return new ModelAndView("attention4b");
    }


    @RequestMapping(value = "/attention4phase3", method = RequestMethod.GET)
    public ModelAndView attention4phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "soundinterval", required = true) String soundinterval,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "pSoundTime", required = true) Double pSoundTime,
            @RequestParam(value = "pSoundCorrect", required = true) Integer pSoundCorrect,
            @RequestParam(value = "pSoundMissed", required = true) Integer pSoundMissed,
            @RequestParam(value = "pSoundWrong", required = true) Integer pSoundWrong,
            @RequestParam(value = "perf", required = false) Double perf,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model,
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        if (patientid == -1)
            if (difficulty.equals("training"))
                return new ModelAndView("redirect: patienttraining");
            else
                return new ModelAndView("redirect: patientdemo");

        if (difficulty.equals("training") || difficulty.equals("demo"))
            return new ModelAndView("redirect: patientrehabilitation");

        Exercise exercise = exerciseController.getEntityOrThrow(exerciseid);
        History history = historyController.getEntityOrThrow(assignmentid);

        history.setExid(exerciseid);
        history.setPassed(passed);
        history.setUserid(patientid);
        history.setpTime(pTime + pSoundTime);
        history.setpCorrect(pCorrect + pSoundCorrect);
        history.setpMissed(pMissed + pSoundMissed);
        history.setpWrong(pWrong + pSoundWrong);
        history.setMaxtime((double) ((time + 2) * nelements)); // The max reaction time to sound is 2 secs
        history.setDifficulty(difficulty);
        history.setLevel(level);
        history.setSessid(sessid);
        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setTimestamp(t);


        // Calculate performance, if needed
        double ft = perf != null ? perf : fitnessController.calculateFitness(false, history);
        history.setAbsperformance(ft);

        double relFt = fitnessController.calculateRelativePerformance(difficulty, ft);
        history.setRelperformance(relFt);

        if (!historyController.putEntity(history))
        {
            logger.error("Error adding History to DB");
            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
            model.addAttribute("back", "patienthome");
            model.addAttribute("home", "patienthome");
            return new ModelAndView("error");
        }



        if (Objects.equals(level, exercise.getMaxLevel()) && passed) // Max level is completed, mark session as done and redirect user to home
        {
            if (!sessionController.updateExerciseDone(sessid, exerciseid, true))
            {
                logger.error("Error updating MSRSession to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
            return new ModelAndView("redirect:patienthome");
        }


        // Forward patient to next level
        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, null);
        level = assignment.getLevel();
        difficulty = exercise.getDifficulty(level);


        HttpSession httpSess = request.getSession();

        Integer[] diffVar = null;
        if (ATT_DIV.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar4"));
        else if (ATT_DIV_FAC.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar4Fac"));
        else if (ATT_DIV_ORI.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar4Ori"));


        Map<String, Object> parameters = createParametersAttention4(level, diffVar);

        // Non presente in attention1, non e' chiaro il perche
        httpSess.setAttribute("diffVar6", diffVar);

        ParametersParser parser = new ParametersParser(parameters);
        nelements = parser.getIntegerParameter("nelements", null);
        ntargets = parser.getIntegerParameter("ntargets", null);
        time = parser.getIntegerParameter("time", null);
        distractor = parser.getStringParameter("distractor", null);
        color = parser.getStringParameter("color", null);
        soundinterval = parser.getStringParameter("soundinterval", null);


        String url = "/attention4phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + true
                + "&ntargets=" + ntargets
                + "&nelements=" + nelements
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&soundinterval=" + soundinterval
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignment.getId()
                ;
        return new ModelAndView("redirect:" + url);


    }


    // RL Exercise

    @RequestMapping(value = "/createreflexes1", method = RequestMethod.GET)
    public ModelAndView createreflexes1(
            @RequestParam(value = "level", required = false, defaultValue = "1") Integer level,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "lastexercisepassed", required = false, defaultValue = "false") boolean lastexercisepassed,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            HttpServletRequest request,
            Model model)
    {
        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, null);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("level", level);
        parameters.put("difficulty", difficulty);
        parameters.put("exname", exname);
        parameters.put("patientid", patientid);
        parameters.put("exerciseid", exerciseid);
        parameters.put("sessid", sessid);
        parameters.put("type", type);
        parameters.put("lastexercisepassed", lastexercisepassed);
        parameters.put("rlagent", rlagent);
        parameters.put("assignmentid", assignment.getId());

        return new ModelAndView("reflexes1a")
                .addAllObjects(parameters);
    }

    @RequestMapping(value = "/reflexes1", method = RequestMethod.GET)
    public ModelAndView reflexes1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = false) ExerciseNameValue exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("reflexes1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        model.addAttribute("rl", isRLDriven(exname));
        return new ModelAndView("reflexes1");
    }


    @RequestMapping(value = "/reflexes1phase1", method = RequestMethod.GET)
    public ModelAndView reflexes1phase1(
            @RequestParam(value = "level", required = false, defaultValue = "1") Integer level,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "lastexercisepassed", required = false, defaultValue = "false") boolean lastexercisepassed,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            HttpServletRequest request,
            Model model)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("level", level);
        parameters.put("difficulty", difficulty);
        parameters.put("exname", exname);
        parameters.put("patientid", patientid);
        parameters.put("exerciseid", exerciseid);
        parameters.put("sessid", sessid);
        parameters.put("type", type);
        parameters.put("lastexercisepassed", lastexercisepassed);
        parameters.put("rlagent", rlagent);
        parameters.put("assignmentid", assignmentid);

        return new ModelAndView("reflexes1a")
                .addAllObjects(parameters);
    }


    @RequestMapping(value = "/reflexes1phase2", method = RequestMethod.GET)
    public ModelAndView reflexes1phase2(
            @RequestParam(value = "level", required = false, defaultValue = "0") Integer level,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "lastexercisepassed", required = false, defaultValue = "false") boolean lastexercisepassed,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            HttpServletRequest request,
            Model model)
    {
        ExerciseHelper helper = ExerciseHelper.create(exerciseid);


        if (difficulty == null)
            difficulty = helper.getDifficultyString(level);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("level", level);
        parameters.put("difficulty", difficulty);
        parameters.put("exname", exname);
        parameters.put("patientid", patientid);
        parameters.put("exerciseid", exerciseid);
        parameters.put("sessid", sessid);
        parameters.put("type", type);
        parameters.put("lastexercisepassed", lastexercisepassed);
        parameters.put("rlagent", rlagent);
        parameters.put("assignmentid", assignmentid);


        return new ModelAndView("reflexes1b")
                .addAllObjects(parameters);
    }

    @RequestMapping(value = "/reflexes1phase3", method = RequestMethod.GET)
    public ModelAndView reflexes1phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "time", required = true) Double time,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) Exercise.ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws ServletException, IOException
    {

        logger.info("reflexes1phase3()");
        if (patientid == -1)
            if (difficulty.equals("training"))
                return new ModelAndView("redirect: patienttraining");
            else
                return new ModelAndView("redirect: patientdemo");


        if (difficulty.equals("training") || difficulty.equals("demo"))
            return new ModelAndView("redirect: patientrehabilitation");


        History history = historyController.getEntityOrThrow(assignmentid);

        history.setExid(exerciseid);
        history.setPassed(passed);
        history.setUserid(patientid);
        history.setpTime(pTime);
        history.setpCorrect(pCorrect);
        history.setpMissed(pMissed);
        history.setpWrong(pWrong);
        history.setMaxtime(time);
        history.setPassed(passed);
        history.setLevel(level);
        history.setDifficulty(difficulty);
        history.setSessid(sessid);

        // Calculate performance
        double ft;
        ft = fitnessController.calculateFitness(false, history);
        history.setAbsperformance(ft);

        ft = fitnessController.calculateFitness(true, history);
        history.setRelperformance(ft);

        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setTimestamp(t);

        if (!historyController.putEntity(history))
        {
            logger.error("Error adding History to DB");
            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
            model.addAttribute("back", "patienthome");
            model.addAttribute("home", "patienthome");
            return new ModelAndView("error");
        }


        ExerciseHelper helper = ExerciseHelper.create(exerciseid);

        if (level == helper.getMaxLevel() && passed) // Max level is completed, mark session as done and redirect user to home
        {
            if (!sessionController.updateExerciseDone(sessid, exerciseid, true))
            {
                logger.error("Error updating MSRSession to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
            return new ModelAndView("redirect:patienthome");
        }

        // Forward user to next exercise

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, level);

        level = assignment.getLevel();

        difficulty = helper.getDifficultyString(level);

        String url = "/reflexes1phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&lastexercisepassed=" + false
                + "&time=" + time
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent;


        return new ModelAndView("redirect:" + url);


    }


    @RequestMapping(value = "/creatememory1", method = RequestMethod.GET)
    public ModelAndView creatememory1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        String argument = "";
        if (MEM_VIS_1.toString().equals(type))
        {
            argument = "diffVar5";
        } else if (MEM_VIS_1_FAC.toString().equals(type))
        {
            argument = "diffVar5Fac";
        } else if (MEM_VIS_1_ORI.toString().equals(type))
        {
            argument = "diffVar5Ori";
        }
        diffVar = (Integer[]) (httpSess.getAttribute(argument));
        if (diffVar == null)
        {
            diffVar = new Integer[NUM_FEAT_MEM_1];
        }

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, null, null);

        int level = assignment.getLevel();

        difficulty = ExerciseHelper.create(exerciseid).getDifficultyString(level);

        Map<String, Object> parameters = createParametersMemory1(level, diffVar);
        if (MEM_VIS_1.toString().equals(type))
        {
            httpSess.setAttribute("diffVar5", diffVar);
        } else if (MEM_VIS_1_FAC.toString().equals(type))
        {
            httpSess.setAttribute("diffVar5Fac", diffVar);
        } else if (MEM_VIS_1_ORI.toString().equals(type))
        {
            httpSess.setAttribute("diffVar5Ori", diffVar);
        }
        Integer ntargets = (Integer) (parameters.get("ntargets"));
        Integer nelements = (Integer) (parameters.get("nelements"));
        String color = (String) (parameters.get("color"));
        String distractor = (String) (parameters.get("distractor"));
        Integer time = (Integer) (parameters.get("time"));

        String url = "/memory1phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&ntargets=" + ntargets
                + "&nelements=" + nelements
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname;
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/memory1", method = RequestMethod.GET)
    public ModelAndView memory1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("memory1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("memory1");
    }

    @RequestMapping(value = "/memory1phase1", method = RequestMethod.GET)
    public ModelAndView memory1phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) String level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model)
    {

        logger.debug("memory1phase1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("nelements", nelements);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);

        return new ModelAndView("memory1a");

    }

    @RequestMapping(value = "/memory1phase2", method = RequestMethod.GET)
    public ModelAndView memory1phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) String level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model)
    {

        logger.debug("memory1phase2()");

        List<ExElement> targetElementList;
        List<ExElement> exElementList = exElementController.getUniqueRandomRecordsByCategory(CategoryValue.valueOf(category), nelements);


        if (exname.equals(ExerciseNameValue.MEM_VIS_1_CMP.toString()))
            targetElementList = exElementController.sampleElements(ntargets, exElementList, ExElement::getEldescr);
        else
            targetElementList = exElementController.sampleElements(ntargets, exElementList, ExElement::getElname);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("nelements", nelements);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("targetElementList", targetElementList);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);

        return new ModelAndView("memory1b");

    }

    @RequestMapping(value = "/memory1phase3", method = RequestMethod.GET)
    public ModelAndView memory1phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "exElementIds", required = true) String exElementIds,
            @RequestParam(value = "targetElementIds", required = true) String targetElementIds,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model)
    {

        List<ExElement> l = buildExElementListFromIds(exElementIds);
        List<ExElement> l1 = buildExElementListFromIds(targetElementIds);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("exElementList", l);
        model.addAttribute("targetElementList", l1);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);

        return new ModelAndView("memory1c");
    }

    @RequestMapping(value = "/memory1phase4", method = RequestMethod.GET)
    public ModelAndView memory1phase4(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            Model model,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

        if (patientid == -1)
        {
            if (difficulty.equals("training"))
            {
                return new ModelAndView("redirect: patienttraining");
            } else
            {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else if (difficulty.equals("training") || difficulty.equals("demo"))
        {
            return new ModelAndView("redirect: patientrehabilitation");
        } else
        {
            int newLevel = changeDiffController.findChangedLevel(exerciseid, patientid, sessid, -1);


            History history = historyController.findLastUnsolvedByUserAndExerciseAndSession(patientid, exerciseid, sessid)
                    .orElseThrow(() -> new IllegalStateException("An assigned history must be present"));

            history.setExid(exerciseid);
            history.setPassed(passed);
            history.setUserid(patientid);
            history.setpTime(pTime);
            history.setpCorrect(pCorrect);
            history.setpMissed(pMissed);
            history.setpWrong(pWrong);
            history.setMaxtime((double) time * nelements);
            //history.setStatus(SessionValue.valueOf(session).ordinal());
            //history.setDifficulty(session);
            history.setDifficulty(difficulty);

            // Calculate performance
            double ft = Double.NaN;
            ft = fitnessController.calculateFitness(false, history);
            history.setAbsperformance(ft);
            ft = fitnessController.calculateFitness(true, history);
            history.setRelperformance(ft);

            Long t = LocalDateTime.now().toDateTime().getMillis();
            history.setTimestamp(t);
            history.setLevel(level);
            history.setSessid(sessid);

          /*  if (historyController.addRecord(history) == -1) {
                logger.error("Error adding History to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }*/

            // Check if the difficulty has been
            // increased / decreased by the operator                
            String url;

            //boolean lastExercisePassed = false;
            HttpSession httpSess = request.getSession();

            Integer[] diffVar = null;
            if (MEM_VIS_1.toString().equals(type))
            {
                //lastExercisePassed = lastExerciseMem1Passed;
                diffVar = (Integer[]) (httpSess.getAttribute("diffVar5"));
                //diffVar = diffVar5;
            } else if (MEM_VIS_1_FAC.toString().equals(type))
            {
                //lastExercisePassed = lastExerciseMem1FacPassed;
                diffVar = (Integer[]) (httpSess.getAttribute("diffVar5Fac"));
                //diffVar = diffVar5Fac;
            } else if (MEM_VIS_1_ORI.toString().equals(type))
            {
                //lastExercisePassed = lastExerciseMem1OriPassed;
                diffVar = (Integer[]) (httpSess.getAttribute("diffVar5Ori"));
                //diffVar = diffVar5Ori;
            }

            if ((lastexercisepassed && passed) || (newLevel != -1))
            {
                // increase difficulty
                if (newLevel != -1)
                {
                    level = newLevel;
                } else
                {
                    level++;
                }
                if (level >= 1 && level <= 3)
                {
                    difficulty = "easy";
                } else if (level >= 4 && level <= 7)
                {
                    difficulty = "medium";
                } else if (level >= 8)
                {
                    difficulty = "difficult";
                }
                history.setDifficulty(difficulty);
                history.setLevel(level);


                //  boolean allMaxFeatures = true;
                if ((lastexercisepassed && passed) || (newLevel != -1))
                {

                    switch (level)
                    {

                        case 1: // easy min
                            nelements = 6;//era 6
                            ntargets = 3;
                            distractor = "no";
                            time = 5;
                            color = "color";
                            break;
                        case 2:
                            nelements = 7;
                            ntargets = 3;
                            distractor = "no";
                            time = 5;
                            color = "color";
                            break;
                        case 3:
                            nelements = 8;
                            ntargets = 4;
                            distractor = "no";
                            time = 5;
                            color = "color";
                            break;
                        case 4: // medium min
                            nelements = 9; //era 8
                            ntargets = 4;
                            distractor = "no";
                            time = 5;
                            color = "color";
                            break;
                        case 5:
                            nelements = 9;//era 8
                            ntargets = 5;//era 4
                            distractor = "no";
                            time = 4;
                            color = "color";
                            break;
                        case 6:
                            nelements = 9;//era 8
                            ntargets = 5;//era 4
                            distractor = "no";
                            time = 4;
                            color = "omo";
                            break;
                        case 7:
                            nelements = 10;
                            ntargets = 6;//era 4
                            distractor = "no";
                            time = 4;
                            color = "omo";
                            break;
                        case 8: // difficult min
                            nelements = 11;//era 10
                            ntargets = 6;//era 5
                            distractor = "no";
                            time = 4;
                            color = "omo";
                            break;
                        case 9:
                            nelements = 11;//era 10
                            ntargets = 6;//era 5
                            distractor = "no";
                            time = 3;
                            color = "omo";
                            break;
                        case 10:
                            nelements = 11;//era 10
                            ntargets = 7;//era 5
                            distractor = "no";
                            time = 3;
                            color = "omo";
                            break;
                        case 11:
                            nelements = 12;//era 10
                            ntargets = 7;//era 5
                            distractor = "no";
                            time = 3;
                            color = "bw";
                            break;
                    }

                }
               /*

                        allMaxFeatures = true;
                        for (int i = 0; i < NUM_FEAT_MEM_1; i++) {
                            allMaxFeatures = allMaxFeatures && diffVar[i] == 3;
                        }
                //if (allMaxFeatures) {
                        //    if (color.equals("color")) {
                        //        color = "omo";
                        //    }
                        //    else if (color.equals("omo")) {
                        //        color = "bw";
                        //    }
                        //}
                // TODO Remove 
                    //if(level==2) {
                        //    allMaxFeatures = true;
                        //}                                

                    } while (repeatVarSelection && !allMaxFeatures);*/

                //if (allMaxFeatures) {
                if (level > 11)
                {
                    MSRSession sess = sessionController.findEntity(sessid).orElse(null);
                    JSONArray jsonArr = new JSONArray(sess.getExercises());
                    JSONObject json;
                    boolean sessionEnded = true;
                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        json = jsonArr.getJSONObject(i);
                        if (json.getInt("id") == exerciseid)
                        {
                            json.put("done", true);
                            jsonArr.put(i, json);
                        }
                        sessionEnded = sessionEnded && json.getBoolean("done");
                    }
                    sess.setExercises(jsonArr.toString());
                    sess.setActive(!sessionEnded);
                    if (!sessionController.updateEntity(sess))
                    {
                        logger.error("Error updating MSRSession to DB");
                        model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                        model.addAttribute("back", "patienthome");
                        model.addAttribute("home", "patienthome");
                        return new ModelAndView("error");
                    }
                    url = "patienthome";
                } else
                {
                    url = "/memory1phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + level
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&category=" + category
                            + "&lastexercisepassed=" + false
                            + "&nelements=" + nelements
                            + "&ntargets=" + ntargets
                            + "&color=" + color
                            + "&distractor=" + distractor
                            + "&time=" + time
                            + "&sessid=" + sessid
                            + "&type=" + type
                            + "&exname=" + exname;

                    //lastExercisePassed = false;
                }
            } else
            {
                // same difficulty different exercise
                url = "/memory1phase1"
                        + "?difficulty=" + difficulty
                        + "&level=" + level
                        + "&patientid=" + patientid
                        + "&exerciseid=" + exerciseid
                        + "&category=" + category
                        + "&lastexercisepassed=" + passed
                        + "&nelements=" + nelements
                        + "&ntargets=" + ntargets
                        + "&color=" + color
                        + "&distractor=" + distractor
                        + "&time=" + time
                        + "&sessid=" + sessid
                        + "&type=" + type
                        + "&exname=" + exname;

                //lastExercisePassed = passed;
            }


            if (!historyController.putEntity(history))
            {
                logger.error("Error adding History to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }

            if (MEM_VIS_1.toString().equals(type))
            {
                //lastExerciseMem1Passed = lastExercisePassed;
                httpSess.setAttribute("diffVar5", diffVar);
                //diffVar5 = diffVar;
            } else if (MEM_VIS_1_FAC.toString().equals(type))
            {
                //lastExerciseMem1FacPassed = lastExercisePassed;
                httpSess.setAttribute("diffVar5Fac", diffVar);
                //diffVar5Fac = diffVar;
            } else if (MEM_VIS_1_ORI.toString().equals(type))
            {
                //lastExerciseMem1OriPassed = lastExercisePassed;
                httpSess.setAttribute("diffVar5Ori", diffVar);
                //diffVar5Ori = diffVar;
            }

            return new ModelAndView("redirect:" + url);
        }
    }


    // RL Exercise

    @RequestMapping(value = "/creatememory2", method = RequestMethod.GET)
    public ModelAndView creatememory2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            HttpServletRequest request,
            Model model)
    {
        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, null);

        int level = assignment.getLevel();

        difficulty = ExerciseHelper.create(exerciseid).getDifficultyString(level);

        HttpSession httpSess = request.getSession();
        Integer[] diffVar;
        diffVar = (Integer[]) (httpSess.getAttribute("diffVar6"));
        if (diffVar == null)
            diffVar = new Integer[NUM_FEAT_MEM_2];

        Map<String, Object> parameters = createParametersMemory2(level, diffVar, exname);

        ParametersParser parser = new ParametersParser(parameters);
        Integer nelements = parser.getIntegerParameter("nelements", null);
        Integer ntargets = parser.getIntegerParameter("ntargets", null);
        Integer time = parser.getIntegerParameter("time", null);
        String distractor = parser.getStringParameter("distractor", null);
        String color = parser.getStringParameter("color", null);

        httpSess.setAttribute("diffVar6", diffVar);

        String url = "/memory2phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&nelements=" + nelements
                + "&ntargets=" + ntargets
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&sessid=" + sessid
                + "&exname=" + exname
                + "&assignmentid=" + assignment.getId()
                ;

        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/memory2", method = RequestMethod.GET)
    public ModelAndView memory2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("memory2()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        model.addAttribute("rl", isRLDriven(exname));
        return new ModelAndView("memory2");
    }

    @RequestMapping(value = "/memory2phase1", method = RequestMethod.GET)
    public ModelAndView memory2phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {

        logger.debug("memory2phase1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("assignmentid", assignmentid);

        return new ModelAndView("memory2a");

    }


    @RequestMapping(value = "/memory2phase2", method = RequestMethod.GET)
    public ModelAndView memory2phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) String assignmentid,
            Model model)
    {
        logger.debug("memory2phase2()");

        List<ExElement> exElementList = exElementController.getUniqueRandomRecordsByCategory(CategoryValue.valueOf(category), nelements);

        List<ExElement> target = new ArrayList<>();

        for (int i = 0; i < ntargets; )
        {

            int index = (int) (Math.random() * nelements);
            ExElement tmp = exElementList.get(index);
            int contaUguali = 0;
            for (int j = 0; j < i; j++)
                if (tmp.equals(target.get(j)))
                    contaUguali++;

            if (contaUguali == 0)
            {
                target.add(tmp);
                i++;
            } else contaUguali = 0;

        }

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("target", target);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("color", color);
        model.addAttribute("distractor", distractor);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("assignmentid", assignmentid);

        return new ModelAndView("memory2b");

    }


    @RequestMapping(value = "/memory2phase3", method = RequestMethod.GET)
    public ModelAndView memory2phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "distractor", required = true) String distractor,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        if (patientid == -1)
            if (difficulty.equals("training"))
                return new ModelAndView("redirect: patienttraining");
            else
                return new ModelAndView("redirect: patientdemo");


        if (difficulty.equals("training") || difficulty.equals("demo"))
            return new ModelAndView("redirect: patientrehabilitation");

        Exercise exercise = exerciseController.getEntityOrThrow(exerciseid);

        History history = historyController.getEntityOrThrow(assignmentid);

        history.setExid(exerciseid);
        history.setPassed(passed);
        history.setUserid(patientid);
        history.setpTime(pTime);
        history.setpCorrect(pCorrect);
        history.setpMissed(pMissed);
        history.setpWrong(pWrong);
        history.setMaxtime((double) time * nelements);
        history.setDifficulty(difficulty);
        history.setLevel(level);
        history.setSessid(sessid);

        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setTimestamp(t);

        // Calculate performance
        double ft = Double.NaN;
        ft = fitnessController.calculateFitness(false, history);
        history.setAbsperformance(ft);

        ft = fitnessController.calculateFitness(true, history);
        history.setRelperformance(ft);


        if (!historyController.putEntity(history))
        {
            logger.error("Error adding History to DB");
            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
            model.addAttribute("back", "patienthome");
            model.addAttribute("home", "patienthome");
            return new ModelAndView("error");
        }


        if (level == exercise.getMaxLevel() && passed) // Max level is completed, mark session as done and redirect user to home
        {
            if (!sessionController.updateExerciseDone(sessid, exerciseid, true))
            {
                logger.error("Error updating MSRSession to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
            return new ModelAndView("redirect:patienthome");
        }



        // Forward user to next level

        HttpSession httpSess = request.getSession();
        Integer[] diffVar = (Integer[]) (httpSess.getAttribute("diffVar6"));

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, level);
        level = assignment.getLevel();

        difficulty = exercise.getDifficulty(level);


        Map<String, Object> parameters = createParametersMemory2(level, diffVar, exname);
        httpSess.setAttribute("diffVar6", diffVar);

        ParametersParser parser = new ParametersParser(parameters);
        nelements = parser.getIntegerParameter("nelements", null);
        ntargets = parser.getIntegerParameter("ntargets", null);
        time = parser.getIntegerParameter("time", null);
        distractor = parser.getStringParameter("distractor", null);
        color = parser.getStringParameter("color", null);


        String url = "/memory2phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + passed
                + "&nelements=" + nelements
                + "&ntargets=" + ntargets
                + "&color=" + color
                + "&distractor=" + distractor
                + "&time=" + time
                + "&sessid=" + sessid
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignment.getId();
                ;
        return new ModelAndView("redirect:" + url);

    }



    // RL Exercise

    @RequestMapping(value = "/createnback", method = RequestMethod.GET)
    public ModelAndView createnback(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        String argument = "";

        if (ATT_SEL_STD.toString().equals(type))
            argument = "diffVar7";
        else if (ATT_SEL_STD_FAC.toString().equals(type))
            argument = "diffVar7Fac";
        else if (ATT_SEL_STD_ORI.toString().equals(type))
            argument = "diffVar7Ori";

        diffVar = (Integer[]) (httpSess.getAttribute(argument));
        if (diffVar == null)
            diffVar = new Integer[NUM_FEAT_NBACK];


        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, null);
        int level = assignment.getLevel();

        difficulty = ExerciseHelper.create(exerciseid).getDifficultyString(level);


        Map<String, Object> parameters = createParametersNback(level, diffVar, exname);

        if (NBACK.toString().equals(type))
            httpSess.setAttribute("diffVar7", diffVar);
        else if (NBACK_FAC.toString().equals(type))
            httpSess.setAttribute("diffVar7Fac", diffVar);
        else if (NBACK_ORI.toString().equals(type))
            httpSess.setAttribute("diffVar7Ori", diffVar);

        ParametersParser parser = new ParametersParser(parameters);

        Integer nback = parser.getIntegerParameter("nback", null);
        Integer time = parser.getIntegerParameter("time", null);
        String color = parser.getStringParameter("color", null);
        Integer nelements = parser.getIntegerParameter("nelements", null);

        String url = "/nbackphase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&nback=" + nback
                + "&time=" + time
                + "&color=" + color
                + "&nelements=" + nelements
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignment.getId()
                ;
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/nback", method = RequestMethod.GET)
    public ModelAndView nback(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            Model model)
    {
        logger.debug("nback()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("rl", true);
        return new ModelAndView("nback");
    }

    @RequestMapping(value = "/nbackphase1", method = RequestMethod.GET)
    public ModelAndView nbackphase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nback", required = true) Integer nback,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {

        logger.debug("nbackphase1()");

        List<ExElement> exElementList = exElementController.getRandomRecordsForNbackByCategory(CategoryValue.valueOf(category), nelements, nback);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nback", nback);
        model.addAttribute("time", time);
        model.addAttribute("color", color);
        model.addAttribute("nelements", nelements);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("assignmentid", assignmentid);
        return new ModelAndView("nbacka");
    }


    @RequestMapping(value = "/nbackphase2", method = RequestMethod.GET)
    public ModelAndView nbackphase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nback", required = true) Integer nback,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "exElementList", required = true) String exElementIds,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) String exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model)
    {
        logger.debug("nbackphase2()");

        List<ExElement> l = buildExElementListFromIds(exElementIds);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nback", nback);
        model.addAttribute("time", time);
        model.addAttribute("color", color);
        model.addAttribute("nelements", nelements);
        model.addAttribute("exElementList", l);
        model.addAttribute("sessid", sessid);
        model.addAttribute("type", type);
        model.addAttribute("exname", exname);
        model.addAttribute("rlagent", rlagent);
        model.addAttribute("assignmentid", assignmentid);
        return new ModelAndView("nbackb");
    }


    @RequestMapping(value = "/nbackphase3", method = RequestMethod.GET)
    public ModelAndView nbackphase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nback", required = true) Integer nback,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "exname", required = true) ExerciseNameValue exname,
            @RequestParam(value = "rlagent", required = false, defaultValue = "-1") Integer rlagent,
            @RequestParam(value = "assignmentid", required = true) Integer assignmentid,
            Model model,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

        if (patientid == -1)
            if (difficulty.equals("training"))
                return new ModelAndView("redirect: patienttraining");
            else
                return new ModelAndView("redirect: patientdemo");

        if (difficulty.equals("training") || difficulty.equals("demo"))
            return new ModelAndView("redirect: patientrehabilitation");

        Exercise exercise = exerciseController.getEntityOrThrow(exerciseid);
        History history = historyController.getEntityOrThrow(assignmentid);


        history.setExid(exerciseid);
        history.setPassed(passed);
        history.setUserid(patientid);
        history.setpTime(pTime);
        history.setpCorrect(pCorrect);
        history.setpMissed(pMissed);
        history.setpWrong(pWrong);
        history.setMaxtime((double) time * nelements);
        history.setDifficulty(difficulty);
        history.setLevel(level);
        history.setSessid(sessid);
        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setTimestamp(t);

        // Calculate performance
        double ft = Double.NaN;
        ft = fitnessController.calculateFitness(false, history);
        history.setAbsperformance(ft);

        ft = fitnessController.calculateFitness(true, history);
        history.setRelperformance(ft);

        if (!historyController.putEntity(history))
        {
            logger.error("Error adding History to DB");
            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
            model.addAttribute("back", "patienthome");
            model.addAttribute("home", "patienthome");
            return new ModelAndView("error");
        }


        if (Objects.equals(level, exercise.getMaxLevel()) && passed) // Max level is completed, mark session as done and redirect user to home
        {
            if (!sessionController.updateExerciseDone(sessid, exerciseid, true))
            {
                logger.error("Error updating MSRSession to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
            return new ModelAndView("redirect:patienthome");
        }

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, rlagent, level);
        level = assignment.getLevel();


        difficulty = exercise.getDifficulty(level);


        HttpSession httpSess = request.getSession();
        Integer[] diffVar = null;
        if (NBACK.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar7"));
        else if (NBACK_FAC.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar7Fac"));
        else if (NBACK_ORI.toString().equals(type))
            diffVar = (Integer[]) (httpSess.getAttribute("diffVar7Ori"));

        Map<String, Object> parameters = createParametersNback(level, diffVar, exname);
        ParametersParser parser = new ParametersParser(parameters);
        Integer newnback = parser.getIntegerParameter("nback", null);
        Integer newtime = parser.getIntegerParameter("time", null);
        String newcolor = parser.getStringParameter("color", null);
        Integer newnelements = parser.getIntegerParameter("nelements", null);

        if (NBACK.toString().equals(type))
            httpSess.setAttribute("diffVar7", diffVar);
        else if (NBACK_FAC.toString().equals(type))
            httpSess.setAttribute("diffVar7Fac", diffVar);
        else if (NBACK_ORI.toString().equals(type))
            httpSess.setAttribute("diffVar7Ori", diffVar);


        String url = "/nbackphase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + passed
                + "&nback=" + newnback
                + "&time=" + newtime
                + "&color=" + newcolor
                + "&nelements=" + newnelements
                + "&sessid=" + sessid
                + "&type=" + type
                + "&exname=" + exname
                + "&rlagent=" + rlagent
                + "&assignmentid=" + assignment.getId()
                ;


        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/creatememory4", method = RequestMethod.GET)
    public ModelAndView creatememory4(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        diffVar = (Integer[]) (httpSess.getAttribute("diffVar8"));
        if (diffVar == null)
            diffVar = new Integer[NUM_FEAT_MEM_LONG_1];


        History assigment = createNextAssigment(exerciseid, patientid, sessid, difficulty, null, null);
        int level = assigment.getLevel();

        difficulty = ExerciseHelper.create(exerciseid).getDifficultyString(level);

        Map<String, Object> parameters = createParametersMemory4(level, diffVar);
        httpSess.setAttribute("diffVar8", diffVar);
        Integer nback = (Integer) (parameters.get("nfaces"));
        Integer time = (Integer) (parameters.get("time"));
        String color = (String) (parameters.get("name"));
        String namediff = (String) (parameters.get("namediff"));

        String url = "/memory4phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&nfaces=" + nback
                + "&time=" + time
                + "&name=" + color
                + "&namediff=" + namediff
                + "&sessid=" + sessid;
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/memory4", method = RequestMethod.GET)
    public ModelAndView memory4(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("memory4()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("memory4");
    }

    @RequestMapping(value = "/memory4phase1", method = RequestMethod.GET)
    public ModelAndView memory4phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nfaces", required = true) Integer nfaces,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "namediff", required = true) String namediff,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            Model model)
    {

        logger.debug("memory4phase1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nfaces", nfaces);
        model.addAttribute("time", time);
        model.addAttribute("name", name);
        model.addAttribute("namediff", namediff);
        model.addAttribute("sessid", sessid);
        return new ModelAndView("memory4a");
    }

    @RequestMapping(value = "/memory4phase2", method = RequestMethod.GET)
    public ModelAndView memory4phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nfaces", required = true) Integer nfaces,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "namediff", required = true) String namediff,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            Model model)
    {

        logger.debug("memory4phase2()");

        String[] cats = category.split("__");

        if ("common".equals(namediff))
        {
            cats[1] = cats[1] + "_COM";
        } else if ("italian".equals(namediff))
        {
            cats[1] = cats[1] + "_ITA";
        } else if ("foreigner".equals(namediff))
        {
            cats[1] = cats[1] + "_FRN";
        }

        List<ExElement> faceListFemale = exElementController.getUniqueRandomRecordsByCategoryAndDescription(CategoryValue.valueOf(cats[0]), nfaces / 2, Gender.F.name());
        List<ExElement> faceListMale = exElementController.getUniqueRandomRecordsByCategoryAndDescription(CategoryValue.valueOf(cats[0]), nfaces - nfaces / 2, Gender.M.name());
        List<ExElement> faceList = new ArrayList<ExElement>();
        faceList.addAll(faceListFemale);
        faceList.addAll(faceListMale);

        List<ExElement> nameElListFemale = exElementController.getUniqueRandomRecordsByCategory(CategoryValue.valueOf(cats[1] + "_F"), nfaces / 2);
        List<ExElement> nameElListMale = exElementController.getUniqueRandomRecordsByCategory(CategoryValue.valueOf(cats[1] + "_M"), nfaces - nfaces / 2);
        List<String> nameListFemale = new ArrayList<String>(),
                nameListMale = new ArrayList<String>(),
                nameList = new ArrayList<String>();

        for (ExElement nameElList1 : nameElListFemale)
        {
            String n = nameElList1.getEldescr();
            if ("surname".equals(name))
            {
                n = n.split(" ")[1];
            }
            nameListFemale.add(n);
        }

        for (ExElement nameElList1 : nameElListMale)
        {
            String n = nameElList1.getEldescr();
            if ("surname".equals(name))
            {
                n = n.split(" ")[1];
            }
            nameListMale.add(n);
        }

        nameList.addAll(nameListFemale);
        nameList.addAll(nameListMale);

        Collections.shuffle(nameList, new Random(1));
        Collections.shuffle(faceList, new Random(1));

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nfaces", nfaces);
        model.addAttribute("time", time);
        model.addAttribute("name", name);
        model.addAttribute("namediff", namediff);
        model.addAttribute("sessid", sessid);
        model.addAttribute("facelist", faceList);
        model.addAttribute("namelist", nameList);

        return new ModelAndView("memory4b");
    }

    @RequestMapping(value = "/memory4phase3", method = RequestMethod.GET)
    public ModelAndView memory4phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nfaces", required = true) Integer nfaces,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "namediff", required = true) String namediff,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "facelist", required = true) String facelist,
            @RequestParam(value = "namelist", required = true) String namelist,
            Model model)
    {

        logger.debug("memory4phase3()");

        List<ExElement> l = buildExElementListFromIds(facelist);

        String a = namelist.replaceAll("\\,\\s+", ",");
        String[] l1 = a.substring(1, a.length() - 1).split("\\,");

        List<SimpleEntry<ExElement, String>> facesNames = new ArrayList<SimpleEntry<ExElement, String>>();
        for (int i = 0; i < l.size(); i++)
        {
            facesNames.add(new AbstractMap.SimpleEntry<ExElement, String>(l.get(i), l1[i]));
        }

        Collections.shuffle(facesNames);

        for (int i = 0; i < l.size(); i++)
        {
            l.set(i, facesNames.get(i).getKey());
            l1[i] = facesNames.get(i).getValue();
        }

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nfaces", nfaces);
        model.addAttribute("time", time);
        model.addAttribute("name", name);
        model.addAttribute("namediff", namediff);
        model.addAttribute("sessid", sessid);
        model.addAttribute("facelist", l);
        model.addAttribute("namelist", l1);

        return new ModelAndView("memory4c");
    }

    @RequestMapping(value = "/memory4phase4", method = RequestMethod.GET)
    public ModelAndView memory4phase4(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nfaces", required = true) Integer nfaces,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "namediff", required = true) String namediff,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            Model model,
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        if (patientid == -1)
        {
            if (difficulty.equals("training"))
            {
                return new ModelAndView("redirect: patienttraining");
            } else
            {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else
        {
            if (difficulty.equals("training") || difficulty.equals("demo"))
            {
                return new ModelAndView("redirect: patientrehabilitation");
            } else
            {
                int newLevel = changeDiffController.findChangedLevel(exerciseid, patientid, sessid, -1);


                History history = historyController.findLastUnsolvedByUserAndExerciseAndSession(patientid, exerciseid, sessid)
                        .orElseThrow(() -> new IllegalStateException("An assigned history must be present"));
                ;
                history.setExid(exerciseid);
                history.setPassed(passed);
                history.setUserid(patientid);
                history.setpTime(pTime);
                history.setpCorrect(pCorrect);
                history.setpMissed(pMissed);
                history.setpWrong(pWrong);
                history.setMaxtime((double) time * nfaces * 60); // time is in minutes
                history.setDifficulty(difficulty);

                // Calculate performance
                double ft = Double.NaN;
                ft = fitnessController.calculateFitness(false, history);
                history.setAbsperformance(ft);
                ft = fitnessController.calculateFitness(true, history);
                history.setRelperformance(ft);

                Long t = LocalDateTime.now().toDateTime().getMillis();
                history.setTimestamp(t);

                history.setLevel(level);
                history.setSessid(sessid);

              /*  if (historyController.addRecord(history)==-1) {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");                    
                }*/

                String url;
                HttpSession httpSess = request.getSession();
                Integer[] diffVar = (Integer[]) (httpSess.getAttribute("diffVar8"));

                if ((lastexercisepassed && passed) || (newLevel != -1))
                {
                    // set difficulty
                    if (newLevel != -1)
                    {
                        level = newLevel;
                    } else
                    {
                        level++;
                    }
                    history.setLevel(level);

                    //inizio modoficia gennaio 2022

                    switch (level)
                    {

                        case 1: // easy min
                            nfaces = 2;
                            diffVar[0] = 1;
                            time = 60;      // 1 hour = unlimited
                            diffVar[1] = 1;
                            name = "name-surname";
                            diffVar[2] = 1;
                            namediff = "common";
                            diffVar[3] = 1;
                            break;
                        case 2: // easy min
                            nfaces = 3;
                            diffVar[0] = 1;
                            time = 60;      // 1 hour = unlimited
                            diffVar[1] = 1;
                            name = "surname";
                            diffVar[2] = 1;
                            namediff = "common";
                            diffVar[3] = 1;
                            break;
                        case 3:
                            nfaces = 3;
                            diffVar[0] = 1;
                            time = 60;      // 1 hour = unlimited
                            diffVar[1] = 1;
                            name = "name-surname";
                            diffVar[2] = 2;
                            namediff = "common";
                            diffVar[3] = 1;
                            break;
                        case 4:
                            nfaces = 4;
                            diffVar[0] = 1;
                            time = 60;      // 1 hour = unlimited
                            diffVar[1] = 1;
                            name = "surname";
                            diffVar[2] = 2;
                            namediff = "common";
                            diffVar[3] = 2;
                            break;
                        case 5: // easy max
                            nfaces = 4;
                            diffVar[0] = 1;
                            time = 60;      // 1 hour = unlimited
                            diffVar[1] = 1;
                            name = "name-surname";
                            diffVar[2] = 2;
                            namediff = "italian";
                            diffVar[3] = 3;
                            break;
                        case 6: // medium min
                            nfaces = 5;
                            diffVar[0] = 2;
                            time = 60;
                            diffVar[1] = 2;
                            name = "surname";
                            diffVar[2] = 1;
                            namediff = "common";
                            diffVar[3] = 1;
                            break;
                        case 7:
                            nfaces = 5;
                            diffVar[0] = 2;
                            time = 60;
                            diffVar[1] = 2;
                            name = "name-surname";
                            diffVar[2] = 2;
                            namediff = "common";
                            diffVar[3] = 1;
                            break;
                        case 8:
                            nfaces = 6;
                            diffVar[0] = 2;
                            time = 60;
                            diffVar[1] = 2;
                            name = "surname";
                            diffVar[2] = 2;
                            namediff = "italian";
                            diffVar[3] = 2;
                            break;
                        case 9: // medium max
                            nfaces = 6;
                            diffVar[0] = 2;
                            time = 60;
                            diffVar[1] = 2;
                            name = "name-surname";
                            diffVar[2] = 2;
                            namediff = "italian";
                            diffVar[3] = 3;
                            break;
                        case 10: // difficult min
                            nfaces = 7;
                            diffVar[0] = 3;
                            time = 60;
                            diffVar[1] = 2;
                            name = "surname";
                            diffVar[2] = 2;
                            namediff = "common";
                            diffVar[3] = 1;
                            break;
                        case 11:
                            nfaces = 7;
                            diffVar[0] = 3;
                            time = 60;
                            diffVar[1] = 3;
                            name = "surname";
                            diffVar[2] = 2;
                            namediff = "italian";
                            diffVar[3] = 1;
                            break;

                    }

                    //fine modifica gennaio 2022

                    if (level > 11)
                    { // Max level
                        MSRSession sess = sessionController.findEntity(sessid).orElse(null);
                        JSONArray jsonArr = new JSONArray(sess.getExercises());
                        JSONObject json;
                        boolean sessionEnded = true;
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            json = jsonArr.getJSONObject(i);
                            if (json.getInt("id") == exerciseid)
                            {
                                json.put("done", true);
                                jsonArr.put(i, json);
                            }
                            sessionEnded = sessionEnded && json.getBoolean("done");
                        }
                        sess.setExercises(jsonArr.toString());
                        sess.setActive(!sessionEnded);
                        if (!sessionController.updateEntity(sess))
                        {
                            logger.error("Error updating MSRSession to DB");
                            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                            model.addAttribute("back", "patienthome");
                            model.addAttribute("home", "patienthome");
                            return new ModelAndView("error");
                        }
                        url = "patienthome";
                    } else
                    {
                        if (level >= 1 && level <= 4)
                        {
                            difficulty = "easy";
                        } else if (level >= 5 && level <= 8)
                        {
                            difficulty = "medium";
                        } else if (level >= 9 && level <= 10)
                        {
                            difficulty = "difficult";
                        }
                        history.setDifficulty(difficulty);

                        Map<String, Object> parameters = createParametersMemory4(level, diffVar);

                        Integer newnfaces = (Integer) (parameters.get("nfaces"));
                        Integer newtime = (Integer) (parameters.get("time"));
                        String newname = (String) (parameters.get("name"));
                        String newnamediff = (String) (parameters.get("namediff"));

                        url = "/memory4phase1"
                                + "?difficulty=" + difficulty
                                + "&level=" + level
                                + "&patientid=" + patientid
                                + "&exerciseid=" + exerciseid
                                + "&category=" + category
                                + "&lastexercisepassed=" + false
                                + "&nfaces=" + newnfaces
                                + "&time=" + newtime
                                + "&name=" + newname
                                + "&namediff=" + newnamediff
                                + "&sessid=" + sessid;
                        //lastExerciseMem4Passed = false;
                    }
                } else
                {
                    // same difficulty different exercise
                    url = "/memory4phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + level
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&category=" + category
                            + "&lastexercisepassed=" + passed
                            + "&nfaces=" + nfaces
                            + "&time=" + time
                            + "&name=" + name
                            + "&namediff=" + namediff
                            + "&sessid=" + sessid;

                    //lastExerciseMem4Passed = passed;
                }
                if (!historyController.putEntity(history))
                {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }
                httpSess.setAttribute("diffVar8", diffVar);
                return new ModelAndView("redirect:" + url);
            }
        }
    }


    @RequestMapping(value = "/creatememory5", method = RequestMethod.GET)
    public ModelAndView creatememory5(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        diffVar = (Integer[]) (httpSess.getAttribute("diffVar10"));
        if (diffVar == null)
        {
            diffVar = new Integer[NUM_FEAT_MEM_5];
        }

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, null, null);
        int level = assignment.getLevel();

        difficulty = ExerciseHelper.create(exerciseid).getDifficultyString(level);

        Map<String, Object> parameters = createParametersMemory5(level, diffVar);
        httpSess.setAttribute("diffVar10", diffVar);
        Integer nelements = (Integer) (parameters.get("nelements"));
        Integer ntargets = (Integer) (parameters.get("ntargets"));
        Integer time = (Integer) (parameters.get("time"));
        Integer nRigheCol = (Integer) (parameters.get("nRigheCol"));
        String color = (String) (parameters.get("color"));


        String url = "/memory5phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&nelements=" + nelements
                + "&ntargets=" + ntargets
                + "&color=" + color
                + "&nRigheCol=" + nRigheCol
                + "&time=" + time
                + "&sessid=" + sessid;
        return new ModelAndView("redirect:" + url);
    }


    @RequestMapping(value = "/memory5", method = RequestMethod.GET)
    public ModelAndView memory5(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("memory5()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("memory5");
    }

    @RequestMapping(value = "/memory5phase1", method = RequestMethod.GET)
    public ModelAndView memory5phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nRigheCol", required = true) Integer nRigheCol,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            Model model)
    {

        logger.debug("memory5phase1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("color", color);
        model.addAttribute("nRigheCol", nRigheCol);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);

        return new ModelAndView("memory5a");

    }

    @RequestMapping(value = "/memory5phase2", method = RequestMethod.GET)
    public ModelAndView memory5phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nRigheCol", required = true) Integer nRigheCol,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            Model model)
    {

        logger.debug("memory5phase2()");

        List<ExElement> exElementList
                = exElementController.getUniqueRandomRecordsByCategory(CategoryValue.valueOf(category), nelements);

        //  int index = (int) (Math.random() * nelements);
        // ExElement target = exElementList.get(index);


        List<ExElement> target = new ArrayList<ExElement>();
        for (int i = 0; i < ntargets; )
        {

            int index = (int) (Math.random() * nelements);
            ExElement tmp = exElementList.get(index);
            int contaUguali = 0;
            for (int j = 0; j < i; j++)
            {
                if (tmp.equals(target.get(j)))
                {
                    contaUguali++;
                }
            }
            if (contaUguali == 0)
            {
                target.add(tmp);
                i++;
            } else contaUguali = 0;

        }

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("nelements", nelements);
        model.addAttribute("ntargets", ntargets);
        model.addAttribute("target", target);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("color", color);
        model.addAttribute("nRigheCol", nRigheCol);
        model.addAttribute("time", time);
        model.addAttribute("sessid", sessid);

        return new ModelAndView("memory5b");

    }

    @RequestMapping(value = "/memory5phase3", method = RequestMethod.GET)
    public ModelAndView memory5phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "ntargets", required = true) Integer ntargets,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nRigheCol", required = true) Integer nRigheCol,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            Model model,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

        if (patientid == -1)
        {
            if (difficulty.equals("training"))
            {
                return new ModelAndView("redirect: patienttraining");
            } else
            {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else
        {
            if (difficulty.equals("training") || difficulty.equals("demo"))
            {
                return new ModelAndView("redirect: patientrehabilitation");
            } else
            {
                int newLevel = changeDiffController.findChangedLevel(exerciseid, patientid, sessid, -1);

                History history = historyController.findLastUnsolvedByUserAndExerciseAndSession(patientid, exerciseid, sessid)
                        .orElseThrow(() -> new IllegalStateException("An assigned history must be present"));
                ;
                history.setExid(exerciseid);
                history.setPassed(passed);
                history.setUserid(patientid);
                history.setpTime(pTime);
                history.setpCorrect(pCorrect);
                history.setpMissed(pMissed);
                history.setpWrong(pWrong);
                history.setMaxtime((double) time * nelements);
                history.setDifficulty(difficulty);
                //history.setStatus(SessionValue.valueOf(session).ordinal());
                //history.setDifficulty(session);
                //history.setStatus(new Integer(42));
                // Calculate performance
                double ft = Double.NaN;
                ft = fitnessController.calculateFitness(false, history);
                history.setAbsperformance(ft);
                ft = fitnessController.calculateFitness(true, history);
                history.setRelperformance(ft);

                Long t = LocalDateTime.now().toDateTime().getMillis();
                history.setTimestamp(t);

                history.setLevel(level);
                history.setSessid(sessid);

            /*    if (historyController.addRecord(history)==-1) {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");                    
                }*/

                // Check if the difficulty has been
                // increased / decreased by the operator                
                String url;
                HttpSession httpSess = request.getSession();
                Integer[] diffVar = (Integer[]) (httpSess.getAttribute("diffVar10"));

                if ((lastexercisepassed && passed) || (newLevel != -1))
                {
                    // increase difficulty
                    if (newLevel != -1)
                    {
                        level = newLevel;
                    } else
                    {
                        level++;
                    }
                    if (level >= 1 && level <= 4)
                    {
                        difficulty = "easy";
                    } else if (level >= 5 && level <= 7)
                    {
                        difficulty = "medium";
                    } else if (level >= 8)
                    {
                        difficulty = "difficult";
                    }
                    history.setLevel(level);
                    history.setDifficulty(difficulty);
                    int var;
                    boolean allMaxFeatures, repeatVarSelection;
                    List<Integer> l = Arrays.asList(diffVar);


                    switch (level)
                    {

                        case 1: // easy min
                            nelements = 3;
                            ntargets = 3;
                            diffVar[0] = 1;
                            nRigheCol = 2;
                            diffVar[1] = 1;
                            time = 3;
                            diffVar[2] = 1;
                            color = "color";
                            diffVar[3] = 1;
                            break;
                        case 2:
                            nelements = 3;//era 4
                            ntargets = 3;
                            diffVar[0] = 2;
                            nRigheCol = 2;
                            diffVar[1] = 1;
                            time = 2;
                            diffVar[2] = 1;
                            color = "color";
                            diffVar[3] = 1;
                            break;
                        case 3: // medium min
                            nelements = 4;
                            ntargets = 4;
                            diffVar[0] = 2;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 2;
                            diffVar[2] = 1;
                            color = "color";
                            diffVar[3] = 1;
                            break;
                        case 4://min
                            nelements = 4;
                            ntargets = 4;
                            diffVar[0] = 2;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 2;
                            diffVar[2] = 1;
                            color = "omo";
                            diffVar[3] = 1;
                            break;
                        case 5://media
                            nelements = 5;
                            ntargets = 5;
                            diffVar[0] = 3;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 2;
                            diffVar[2] = 1;
                            color = "color";
                            diffVar[3] = 1;
                            break;
                        case 6: //
                            nelements = 5;
                            ntargets = 5;
                            diffVar[0] = 3;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 2;
                            diffVar[2] = 1;
                            color = "omo";
                            diffVar[3] = 1;
                            break;
                        case 7:
                            nelements = 6;//era 5
                            ntargets = 6;
                            diffVar[0] = 3;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 3;
                            diffVar[2] = 1;
                            color = "omo";
                            diffVar[3] = 1;
                            break;
                        case 8://alta
                            nelements = 6;//era 5
                            ntargets = 6;
                            diffVar[0] = 3;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 3;
                            diffVar[2] = 1;
                            color = "bw";
                            diffVar[3] = 1;
                            break;
                        case 9:
                            nelements = 7;//non c'era
                            ntargets = 7;
                            diffVar[0] = 3;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 3;
                            diffVar[2] = 1;
                            color = "omo";
                            diffVar[3] = 1;
                            break;
                        case 10:
                            nelements = 7;//non c'era
                            ntargets = 7;
                            diffVar[0] = 3;
                            nRigheCol = 3;
                            diffVar[1] = 1;
                            time = 3;
                            diffVar[2] = 1;
                            color = "bw";
                            diffVar[3] = 1;
                            break;
                    }


                    if (level > 10)
                    {
                        MSRSession sess = sessionController.findEntity(sessid).orElse(null);
                        JSONArray jsonArr = new JSONArray(sess.getExercises());
                        JSONObject json;
                        boolean sessionEnded = true;
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            json = jsonArr.getJSONObject(i);
                            if (json.getInt("id") == exerciseid)
                            {
                                json.put("done", true);
                                jsonArr.put(i, json);
                            }
                            sessionEnded = sessionEnded && json.getBoolean("done");
                        }
                        sess.setExercises(jsonArr.toString());
                        sess.setActive(!sessionEnded);
                        if (!sessionController.updateEntity(sess))
                        {
                            logger.error("Error updating MSRSession to DB");
                            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                            model.addAttribute("back", "patienthome");
                            model.addAttribute("home", "patienthome");
                            return new ModelAndView("error");
                        }
                        url = "patienthome";
                    } else
                    {
                        url = "/memory5phase1"
                                + "?difficulty=" + difficulty
                                + "&level=" + level
                                + "&patientid=" + patientid
                                + "&exerciseid=" + exerciseid
                                + "&category=" + category
                                + "&lastexercisepassed=" + lastexercisepassed
                                + "&nelements=" + nelements
                                + "&ntargets=" + ntargets
                                + "&color=" + color
                                + "&nRigheCol=" + nRigheCol
                                + "&time=" + time
                                + "&sessid=" + sessid;

                        //lastExerciseMem2Passed = false;
                    }
                } else
                {
                    // same difficulty different exercise
                    url = "/memory5phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + level
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&category=" + category
                            + "&lastexercisepassed=" + passed
                            + "&nelements=" + nelements
                            + "&ntargets=" + ntargets
                            + "&color=" + color
                            + "&nRigheCol=" + nRigheCol
                            + "&time=" + time
                            + "&sessid=" + sessid;

                    //lastExerciseMem2Passed = passed;
                }
                if (!historyController.putEntity(history))
                {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }
                httpSess.setAttribute("diffVar10", diffVar);
                return new ModelAndView("redirect:" + url);
            }
        }
    }


    @RequestMapping(value = "/createexecfunct1", method = RequestMethod.GET)
    public ModelAndView createexecfunct1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        diffVar = (Integer[]) (httpSess.getAttribute("diffVar7"));
        if (diffVar == null)
        {
            diffVar = new Integer[NUM_FEAT_NBACK];
        }

        History assignment = createNextAssigment(exerciseid, patientid, sessid, difficulty, null, null);

        int level = assignment.getLevel();

        difficulty = ExerciseHelper.create(exerciseid).getDifficultyString(level);


        Map<String, Object> parameters = createParametersExecfunct1(level, diffVar);
        httpSess.setAttribute("diffVar7", diffVar);
        String answer = (String) (parameters.get("answer"));
        Integer time = (Integer) (parameters.get("time"));
        String color = (String) (parameters.get("color"));
        Integer nelements = (Integer) (parameters.get("nelements"));

        String url = "/execfunct1phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&category=" + category
                + "&lastexercisepassed=" + false
                + "&answer=" + answer
                + "&time=" + time
                + "&color=" + color
                + "&nelements=" + nelements
                + "&sessid=" + sessid;
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/execfunct1", method = RequestMethod.GET)
    public ModelAndView execfunct1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
    {

        logger.debug("execfunct()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("execfunct1");
    }

    @RequestMapping(value = "/execfunct1phase1", method = RequestMethod.GET)
    public ModelAndView execfunct1phase1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "answer", required = true) String answer,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            Model model)
    {

        logger.debug("execfunct1phase1()");

        String[] cats = category.split("__");

        String[] cats1 = new String[2];

        List<ExElement> exElementList
                = exElementController.getRandomRecordsByCategories(
                nelements, CategoryValue.valueOf(cats[0]),
                CategoryValue.valueOf(cats[1])
        );
        int idx = (int) (Math.random() * (exElementList.size()));
        String inhElement = exElementList.get(idx).getEldescr();

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("answer", answer);
        model.addAttribute("time", time);
        model.addAttribute("color", color);
        model.addAttribute("nelements", nelements);
        model.addAttribute("exElementList", exElementList);
        model.addAttribute("sessid", sessid);
        model.addAttribute("cat1", cats[0]);
        model.addAttribute("cat2", cats[1]);
        model.addAttribute("inhElement", inhElement);
        return new ModelAndView("execfunct1a");
    }

    @RequestMapping(value = "/execfunct1phase2", method = RequestMethod.GET)
    public ModelAndView execfunct1phase2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "answer", required = true) String answer,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "exElementList", required = true) String exElementIds,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "cat1", required = true) String cat1,
            @RequestParam(value = "cat2", required = true) String cat2,
            @RequestParam(value = "inhElement", required = true) String inhElement,
            Model model)
    {

        logger.debug("execfunct1phase2()");

        List<ExElement> l = buildExElementListFromIds(exElementIds);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("category", category);
        model.addAttribute("lastexercisepassed", lastexercisepassed);
        model.addAttribute("cat1", cat1);
        model.addAttribute("cat2", cat2);
        model.addAttribute("inhElement", inhElement);
        model.addAttribute("answer", answer);
        model.addAttribute("time", time);
        model.addAttribute("color", color);
        model.addAttribute("nelements", nelements);
        model.addAttribute("exElementList", l);
        model.addAttribute("sessid", sessid);
        return new ModelAndView("execfunct1b");
    }

    @RequestMapping(value = "/execfunct1phase3", method = RequestMethod.GET)
    public ModelAndView execfunct1phase3(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "lastexercisepassed", required = true) Boolean lastexercisepassed,
            @RequestParam(value = "cat1", required = true) String cat1,
            @RequestParam(value = "cat2", required = true) String cat2,
            @RequestParam(value = "inhElement", required = true) String inhElement,
            @RequestParam(value = "answer", required = true) String answer,
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "color", required = true) String color,
            @RequestParam(value = "nelements", required = true) Integer nelements,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "pCorrect", required = true) Integer pCorrect,
            @RequestParam(value = "pMissed", required = true) Integer pMissed,
            @RequestParam(value = "pWrong", required = true) Integer pWrong,
            Model model,
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        if (patientid == -1)
        {
            if (difficulty.equals("training"))
            {
                return new ModelAndView("redirect: patienttraining");
            } else
            {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else
        {
            if (difficulty.equals("training") || difficulty.equals("demo"))
            {
                return new ModelAndView("redirect: patientrehabilitation");
            } else
            {
                int newLevel = changeDiffController.findChangedLevel(exerciseid, patientid, sessid, -1);


                History history = historyController.findLastUnsolvedByUserAndExerciseAndSession(patientid, exerciseid, sessid)
                        .orElseThrow(() -> new IllegalStateException("An assigned history must be present"));
                ;


                history.setExid(exerciseid);
                history.setPassed(passed);
                history.setUserid(patientid);
                history.setpTime(pTime);
                history.setpCorrect(pCorrect);
                history.setpMissed(pMissed);
                history.setpWrong(pWrong);
                history.setMaxtime((double) time * nelements);
                history.setDifficulty(difficulty);

                // Calculate performance
                double ft = Double.NaN;
                ft = fitnessController.calculateFitness(false, history);
                history.setAbsperformance(ft);
                ft = fitnessController.calculateFitness(true, history);
                history.setRelperformance(ft);

                Long t = LocalDateTime.now().toDateTime().getMillis();
                history.setTimestamp(t);
                history.setLevel(level);
                history.setSessid(sessid);

           /*     if (historyController.addRecord(history)==-1) {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");                    
                }*/

                String url;
                HttpSession httpSess = request.getSession();
                Integer[] diffVar = (Integer[]) (httpSess.getAttribute("diffVar7"));

                if ((lastexercisepassed && passed) || (newLevel != -1))
                {
                    // set difficulty
                    if (newLevel != -1)
                    {
                        level = newLevel;
                    } else
                    {
                        level++;
                    }
                    history.setLevel(level);
                    if (level > 21)
                    { // Max level
                        MSRSession sess = sessionController.findEntity(sessid).orElse(null);
                        JSONArray jsonArr = new JSONArray(sess.getExercises());
                        JSONObject json;
                        boolean sessionEnded = true;
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            json = jsonArr.getJSONObject(i);
                            if (json.getInt("id") == exerciseid)
                            {
                                json.put("done", true);
                                jsonArr.put(i, json);
                            }
                            sessionEnded = sessionEnded && json.getBoolean("done");
                        }
                        sess.setExercises(jsonArr.toString());
                        sess.setActive(!sessionEnded);
                        if (!sessionController.updateEntity(sess))
                        {
                            logger.error("Error updating MSRSession to DB");
                            model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                            model.addAttribute("back", "patienthome");
                            model.addAttribute("home", "patienthome");
                            return new ModelAndView("error");
                        }
                        url = "patienthome";
                    } else
                    {
                        if (level >= 1 && level <= 7)
                        {
                            difficulty = "easy";
                        } else if (level >= 8 && level <= 14)
                        {
                            difficulty = "medium";
                        } else if (level >= 15 && level <= 21)
                        {
                            difficulty = "difficult";
                        }
                        history.setDifficulty(difficulty);
                        Map<String, Object> parameters = createParametersExecfunct1(level, diffVar);

                        String newanswer = (String) (parameters.get("answer"));
                        Integer newtime = (Integer) (parameters.get("time"));
                        String newcolor = (String) (parameters.get("color"));
                        Integer newnelements = (Integer) (parameters.get("nelements"));

                        url = "/execfunct1phase1"
                                + "?difficulty=" + difficulty
                                + "&level=" + level
                                + "&patientid=" + patientid
                                + "&exerciseid=" + exerciseid
                                + "&category=" + category
                                + "&lastexercisepassed=" + false
                                + "&answer=" + newanswer
                                + "&time=" + newtime
                                + "&color=" + newcolor
                                + "&nelements=" + newnelements
                                + "&sessid=" + sessid;
                        //lastExerciseFunEx1Passed = false;
                    }
                } else
                {
                    // same difficulty different exercise
                    url = "/execfunct1phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + level
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&category=" + category
                            + "&lastexercisepassed=" + passed
                            + "&answer=" + answer
                            + "&time=" + time
                            + "&color=" + color
                            + "&nelements=" + nelements
                            + "&sessid=" + sessid;

                    //lastExerciseFunEx1Passed = passed;
                }
                if (!historyController.putEntity(history))
                {
                    logger.error("Error adding History to DB");
                    model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                    model.addAttribute("back", "patienthome");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }
                httpSess.setAttribute("diffVar7", diffVar);
                return new ModelAndView("redirect:" + url);
            }
        }
    }

    @RequestMapping(value = "/createexecfunct2", method = RequestMethod.GET)
    public ModelAndView createexecfunct2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            HttpServletRequest request,
            Model model)
    {

        Integer[] diffVar;
        HttpSession httpSess = request.getSession();
        diffVar = (Integer[]) (httpSess.getAttribute("diffVar9"));
        if (diffVar == null)
        {
            diffVar = new Integer[NUM_FEAT_RES_INH];
        }

        Integer level;

        if (difficulty.equals("training") || difficulty.equals("demo"))
        {
            level = -1;
        } else
        {
            List<History> historyList = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
            if (historyList.isEmpty())
            {
                if ("easy".equals(difficulty))
                {
                    level = 1;
                } else if ("medium".equals(difficulty))
                {
                    level = 4;
                } else
                { // "difficult".equals(difficulty)
                    level = 8;
                }
            } else
            {
                level = historyList.get(0).getLevel();
                List<ChangeDifficulty> cdl = changeDiffController.findFromHistory(historyList.get(0).getId());
                if ((cdl != null) && (!cdl.isEmpty()))
                {
                    level = cdl.get(0).getLevel();
                }
                if (level >= 1 && level <= 3)
                {
                    difficulty = "easy";
                } else if (level >= 4 && level <= 7)
                {
                    difficulty = "medium";
                } else if (level >= 8)
                {
                    difficulty = "difficult";
                }
                //difficulty = historyList.get(0).getDifficulty();
            }
        }
        Map<String, Object> parameters = createParametersExecfunct1(level, diffVar);
        httpSess.setAttribute("diffVar9", diffVar);
        String answer = (String) (parameters.get("answer"));
        Integer time = (Integer) (parameters.get("time"));
        String color = (String) (parameters.get("color"));
        Integer nelements = (Integer) (parameters.get("nelements"));

        String url = "/execfunct2phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&lastexercisepassed=" + false
                + "&answer=" + answer
                + "&time=" + time
                + "&color=" + color
                + "&nelements=" + nelements
                + "&sessid=" + sessid;
        return new ModelAndView("redirect:" + url);
    }


    @RequestMapping(value = "/getperformance", method = RequestMethod.GET)
    public ResponseEntity<String> getperformance(
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "ptime", required = true) Double ptime,
            @RequestParam(value = "pcorrect", required = true) Integer pcorrect,
            @RequestParam(value = "pwrong", required = true) Integer pwrong,
            @RequestParam(value = "pmissed", required = true) Integer pmissed,
            @RequestParam(value = "maxtime", required = true) Double maxtime,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "assignmentid", required = false) Integer assignmentid,
            HttpServletRequest request)
    {
        History history = new History();
        history.setExid(exerciseid);
        history.setUserid(patientid);
        history.setpTime(ptime);
        history.setpCorrect(pcorrect);
        history.setpWrong(pwrong);
        history.setpMissed(pmissed);
        history.setMaxtime(maxtime);
        history.setSessid(sessid);
        history.setDifficulty(difficulty);
        history.setLevel(level);

        // Calculate performance
        double perf = fitnessController.calculateFitness(false, history);

        double thr = fitnessController.getFitnessWeightOrThrow(exerciseid).getThr();
        if(assignmentid != null)
            thr = historyController.getEntityOrThrow(assignmentid).getPassThreshold();

        boolean passed = perf >= thr;


        HttpHeaders headers = new HttpHeaders();

        JSONObject res = new JSONObject();
        res.put("perf", perf);
        res.put("passed", passed);
        res.put("thr", thr);

        return new ResponseEntity<>(res.toString(), headers, HttpStatus.OK);
    }


    private List<ExElement> buildExElementListFromIds(String exElementIds) throws NumberFormatException
    {
        String[] b = exElementIds.substring(1, exElementIds.length() - 1)
                .replaceAll("\\s+", "")
                .split(",");


        boolean allPresent = Arrays.stream(b)
                .filter(id -> !Objects.equals(id, ""))
                .mapToInt(Integer::parseInt)
                .mapToObj(id -> exElementController.getAllEntities(e -> Objects.equals(e.getId(), id)))
                .mapToLong(List::size)
                .sum() == b.length;


        logger.info(String.format("Element ids: %s", exElementIds));
        logger.info(String.format("Ids: %s", String.join(",", b)));


        if (!allPresent)
            logger.error("Error building ExElement list from ids: some elements not found in DB");

        return Arrays.stream(b)
                .filter(id -> !Objects.equals(id, ""))
                .mapToInt(Integer::parseInt)
                .mapToObj(exElementController::findEntity)
                .map(opt -> opt.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
