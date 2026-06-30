package it.unibo.msrehab.model.entities;

import it.unibo.msrehab.model.controller.MSRSessionController;
import org.json.JSONArray;

import java.util.stream.IntStream;

public class EntityUtils
{


    public static boolean hasVisibleExercises(MSRSession session)
    {
        JSONArray jsonArray = new JSONArray(session.getExercises());
        return IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .map(jobj -> jobj.getInt("id"))
                .anyMatch(id -> getExerciseVisibilityInSession(session, id))
                ;
    }

    public static boolean setExerciseVisibilityInSession(MSRSession session, int exerciseId, boolean visible)
    {
        JSONArray jsonArray = new JSONArray(session.getExercises());
        IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .filter(obj -> obj.getInt("id") == exerciseId)
                .findFirst()
                .ifPresent(obj -> obj.put("visible", visible));
        session.setExercises(jsonArray.toString());
        MSRSessionController controller = new MSRSessionController();
        return controller.putEntity(session);
    }

    public static boolean getExerciseVisibilityInSession(MSRSession session, int exerciseId)
    {
        JSONArray jsonArray = new JSONArray(session.getExercises());
        return IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .filter(obj -> obj.getInt("id") == exerciseId)
                .findFirst()
                .map(obj -> {
                    if(obj.has("visible"))
                        return obj.getBoolean("visible");
                    return true;
                })
                .orElse(true);
    }
}
