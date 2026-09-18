package it.unibo.msrehab.services;

import flexjson.JSONSerializer;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.model.entities.MSRUser;
import org.joda.time.LocalDate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service per la fornitura delle metriche di esecuzione degli esercizi
 */
@Controller
public class MetricsService
{

    private final Model model = JPAModel.getInstance();
    private final JSONSerializer serializer = new JSONSerializer();




    // Metriche history

    public static final String DATE = "date";
    public static final String TOTAL_TIME = "total_time";
    public static final String EFFECTIVE_TIME = "effective_time";
    public static final String TOTAL_ATTEMPTS = "total_attempts";
    public static final String COMPLETED_ATTEMPTS = "completed_attempts";
    public static final String INTERRUPTED_ATTEMPTS = "interrupted_attempts";
    public static final String PASSED_ATTEMPTS = "passed_attempts";
    private static final String FAILED_ATTEMPTS = "failed_attempts";
    public static final String MEAN_PERFORMANCE = "mean_performance";
    private static final String MAX_LEVEL_REACHED = "max_level_reached";
    private static final String TOTAL_RESPONSES = "total_responses";
    private static final String CORRECT_RESPONSES = "correct_responses";
    private static final String WRONG_RESPONSES = "wrong_responses";
    private static final String MISSING_RESPONSES = "missing_responses";
    private static final String REL_COMPLETED_ATTEMPTS = "rel_completed_attempts";
    private static final String REL_INTERRUPTED_ATTEMPTS = "rel_interrupted_attempts";
    private static final String REL_PASSED_ATTEMPTS = "rel_passed_attempts";
    private static final String REL_FAILED_ATTEMPTS = "rel_failed_attempts";
    private static final String REL_CORRECT_RESPONSES = "rel_correct_responses";
    private static final String REL_WRONG_RESPONSES = "rel_wrong_responses";
    private static final String REL_MISSING_RESPONSES = "rel_missing_responses";

    public static final Map<String, String> displayNames = new HashMap<>();
    public static final List<String> fields = new ArrayList<>();


    static {


        displayNames.put(DATE, "Data");

        displayNames.put(TOTAL_TIME, "Tempo di connessione totale (mins)");
        displayNames.put(EFFECTIVE_TIME, "Tempo di attivita' effettiva (mins)");
        displayNames.put(TOTAL_ATTEMPTS, "Tentativi totali");

        displayNames.put(COMPLETED_ATTEMPTS, "Tentativi completati");
        displayNames.put(INTERRUPTED_ATTEMPTS, "Tentativi interrotti");
        displayNames.put(PASSED_ATTEMPTS, "Tentativi superati");
        displayNames.put(FAILED_ATTEMPTS, "Tentativi falliti");
        displayNames.put(MEAN_PERFORMANCE, "Performance media");
        displayNames.put(MAX_LEVEL_REACHED, "Livello massimo raggiunto");
        displayNames.put(TOTAL_RESPONSES, "Risposte totali");
        displayNames.put(CORRECT_RESPONSES, "Risposte corrette");
        displayNames.put(WRONG_RESPONSES, "Risposte sbagliate");
        displayNames.put(MISSING_RESPONSES, "Omissioni");
        displayNames.put(REL_COMPLETED_ATTEMPTS, "Tentativi completati (%)");
        displayNames.put(REL_INTERRUPTED_ATTEMPTS, "Tentativi interrotti (%)");
        displayNames.put(REL_PASSED_ATTEMPTS, "Tentativi superati (%)");
        displayNames.put(REL_FAILED_ATTEMPTS, "Tentativi falliti (%)");
        displayNames.put(REL_CORRECT_RESPONSES, "Risposte corrette (%)");
        displayNames.put(REL_WRONG_RESPONSES, "Risposte sbagliate (%)");
        displayNames.put(REL_MISSING_RESPONSES, "Omissioni (%)");

        fields.addAll(Arrays.asList(
                DATE,
                TOTAL_TIME,
                EFFECTIVE_TIME,
                TOTAL_ATTEMPTS,
                COMPLETED_ATTEMPTS,
                INTERRUPTED_ATTEMPTS,
                PASSED_ATTEMPTS,
                FAILED_ATTEMPTS,
                MEAN_PERFORMANCE,
                MAX_LEVEL_REACHED,
                TOTAL_RESPONSES,
                CORRECT_RESPONSES,
                WRONG_RESPONSES,
                MISSING_RESPONSES,
                REL_COMPLETED_ATTEMPTS,
                REL_INTERRUPTED_ATTEMPTS,
                REL_PASSED_ATTEMPTS,
                REL_FAILED_ATTEMPTS,
                REL_CORRECT_RESPONSES,
                REL_WRONG_RESPONSES,
                REL_MISSING_RESPONSES
        ));

    }


    private String getCreationDate(History history)
    {
        return new LocalDate(history.getCreationTimestamp()).toString("dd-MM-yyyy");
    }

    private Exercise getExercise(History history)
    {
        return model.getEntityController(Exercise.class)
                .findEntity(history.getExid())
                .orElseThrow(IllegalStateException::new);
    }

    private MSRUser getPatient(History history)
    {
        return model.getEntityController(MSRUser.class)
                .findEntity(history.getUserid())
                .orElseThrow(IllegalStateException::new);
    }

    private Map<String, Object> computeSessionMetrics(String date, List<History> sessionHistory)
    {

        /*
        Metriche calcolate per ogni sessione di esercizi. Si possono poi calcolare per gli esercizi singoli
        */
        /*
            1 Tempo di connessione totale
            Numero complessivo di minuti in cui l’utente risulta collegato sulla piattaforma WOK-TOP durante la singola sessione.
            Dati da login e scadenza sessione
         */
        double timestampFirstAttempt = sessionHistory.stream()
                .mapToDouble(History::getCreationTimestamp)
                .min()
                .orElse(Double.NaN);

        double timestampLastAttempt = sessionHistory.stream()
                .mapToDouble(h -> h.isSolved() ? h.getFinishTimestamp() : h.getCreationTimestamp())
                .max()
                .orElse(Double.NaN);

        double totalTime = (timestampLastAttempt - timestampFirstAttempt) / 60000.0;
       /*
    2. Tempo effettivo (parametro 2)
       Tempo effettivamente passato a risolvere gli esercizi
       */

        double effectiveTime = sessionHistory.stream()
                .filter(History::isSolved)
                .mapToDouble(h -> (h.getFinishTimestamp() - h.getCreationTimestamp()) / 60000.0)
                .sum();


        /*
    3. Tentativi totali (parametro 4)
       Numero di tentativi totali
       Tentativi totali = interrotti + completati
       */

        double totalAttempts = sessionHistory.size();
        /*
    4. Tentativi interrotti (parametro 5)
       Numero di tentativi interrotti premendo il tasto interrompi
       */
        double interruptedAttempts = sessionHistory.stream()
                .filter(h -> !h.isSolved())
                .count();
        /*
    5. Tentativi completati (parametro 3)
       Numero di tentativi completati per i quali si e’ ricevuto un risultato
       Tentativi completati = tentativi con successo + tentativi con insuccesso
       */
        double completedAttempts = sessionHistory.stream()
                .filter(History::isSolved)
                .count();

        /*
    6. Tentativi con successo (parametro 6)
       Tentativi completati con successo
       */

        double passedAttempts = sessionHistory.stream()
                .filter(h -> h.isSolved() && h.getPassed())
                .count();
        /*
    7. Tentativi con insuccesso
       Tentativi completati con insuccesso
       */

        double failedAttempts = sessionHistory.stream()
                .filter(h -> h.isSolved() && !h.getPassed())
                .count();
        /*
    8. Performance Media (parametro 7)
       Performance media
       */
        double meanPerformance = sessionHistory.stream()
                .filter(History::isSolved)
                .mapToDouble(History::getAbsperformance)
                .average()
                .orElse(Double.NaN);
        /*
    9. Livello massimo raggiunto
       Livello massimo raggiunto nella sessione
       */
        double maxLevelReached = sessionHistory.stream()
                .mapToDouble(History::getLevel)
                .max()
                .orElse(Double.NaN);

        /*
    10. Risposte totali
       Risposte totali nei tentativi completati
       Risposte totali = corrette + incorrette + omissioni
       */
        double totalResponses = sessionHistory.stream()
                .filter(History::isSolved)
                .mapToDouble(h -> h.getpCorrect() + h.getpWrong() + h.getpMissed())
                .sum();
        /*
    11. Risposte corrette
       Risposte corrette nei tentativi completati
       */
        double correctResponses = sessionHistory.stream()
                .filter(History::isSolved)
                .mapToDouble(History::getpCorrect)
                .sum();
        /*
    12. Risposte incorrette
       Risposte incorrette nei tentativi completati

       */
        double wrongResponses = sessionHistory.stream()
                .filter(History::isSolved)
                .mapToDouble(History::getpWrong)
                .sum();
        /*
    13. Risposte omissioni
       Omissioni nei tentativi completati

       */
        double missingResponses = sessionHistory.stream()
                .filter(History::isSolved)
                .mapToDouble(History::getpMissed)
                .sum();
        /*
    14. Frequenze relative di risposte e tentativi (4+3 metriche) (parametro 8)
       Frequenze relative per le metriche 4,5,6,7,10,11,12
     */

        double relCompletedAttempts = completedAttempts / totalAttempts;
        double relInterruptedAttempts = interruptedAttempts / totalAttempts;
        double relPassedAttempts = passedAttempts / completedAttempts;
        double relFailedAttempts = failedAttempts / completedAttempts;

        double relCorrectResponses = correctResponses / totalResponses;
        double relIncorrectResponses = wrongResponses / totalResponses;
        double relMissingResponses = missingResponses / totalResponses;

        HashMap<String, Object> metrics = new HashMap<>();
        metrics.put(DATE, date);

        metrics.put(TOTAL_TIME, totalTime);
        metrics.put(EFFECTIVE_TIME, effectiveTime);

        metrics.put(TOTAL_ATTEMPTS, totalAttempts);
        metrics.put(COMPLETED_ATTEMPTS, completedAttempts);
        metrics.put(INTERRUPTED_ATTEMPTS, interruptedAttempts);
        metrics.put(PASSED_ATTEMPTS, passedAttempts);
        metrics.put(FAILED_ATTEMPTS, failedAttempts);
        metrics.put(MAX_LEVEL_REACHED, maxLevelReached);

        metrics.put(REL_COMPLETED_ATTEMPTS, relCompletedAttempts);
        metrics.put(REL_INTERRUPTED_ATTEMPTS, relInterruptedAttempts);
        metrics.put(REL_PASSED_ATTEMPTS, relPassedAttempts);
        metrics.put(REL_FAILED_ATTEMPTS, relFailedAttempts);

        metrics.put(MEAN_PERFORMANCE, meanPerformance);
        metrics.put(TOTAL_RESPONSES, totalResponses);
        metrics.put(CORRECT_RESPONSES, correctResponses);
        metrics.put(WRONG_RESPONSES, wrongResponses);
        metrics.put(MISSING_RESPONSES, missingResponses);

        metrics.put(REL_CORRECT_RESPONSES, relCorrectResponses);
        metrics.put(REL_WRONG_RESPONSES, relIncorrectResponses);
        metrics.put(REL_MISSING_RESPONSES, relMissingResponses);

        return metrics;



    }



    /*
     * Returns the exercises metrics as a json object
     * {
     * fields : list of fields
     * displayNames : map of field -> field readable name
     * data : list of datapoints as maps where each entry is a map of field-value pair
     * }
     *
     * @param request
     * @param userId           users of the metrics to be returned. If null all users are returned
     * @param exerciseCategory categories of the exercises to be returned. If null all exercise categories are returned
     * @param exerciseName     names of the exercises to be returned. If null all exercise names are returned
     * @return
     */
    @RequestMapping(value = "/metrics/history", method = RequestMethod.GET)
    public HttpEntity<String> getHistoryMetrics(
            HttpServletRequest request,
            @RequestParam(required = false) Integer[] userId,
            @RequestParam(required = false) String[] exerciseCategory,
            @RequestParam(required = false) String[] exerciseName,
            @RequestParam(required = false) Integer[] userGroup
    )
    {
        Map<String, List<History>> sessions = model.getEntityController(History.class)
                .getAllEntities()
                .stream()
                .filter(h -> userId == null || Arrays.asList(userId).contains(h.getUserid()))
                .filter(h -> exerciseCategory == null || Arrays.asList(exerciseCategory).contains(getExercise(h).getCategory().toString()))
                .filter(h -> exerciseName == null || Arrays.asList(exerciseName).contains(getExercise(h).getName().toString()))
                .filter(h -> userGroup == null || Arrays.asList(userGroup).contains(getPatient(h).getGid()))
                .collect(Collectors.groupingBy(this::getCreationDate));

        List<Map<String, Object>> sessionMetricsList = sessions.keySet()
                .stream()
                .sorted()
                .map(e -> computeSessionMetrics(e, sessions.get(e)))
                .collect(Collectors.toList());


        Map<String, Object> metrics = new HashMap<>();
        metrics.put("fields", fields);
        metrics.put("displayNames", displayNames);
        metrics.put("data", sessionMetricsList);

        return ResponseEntity.ok().body(
                serializer.deepSerialize(metrics)
        );

    }

}
