/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.MSRSession;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * TODO: add documentation
 *
 * @author floriano
 *
 */
public class MSRSessionController extends DecoratorEntityController<MSRSession>
{

    public MSRSessionController(Model model)
    {
        super(model, MSRSession.class);
    }


    public List<MSRSession> findAllByUserOrGroup(Integer usrgrpid)
    {
        return getAllEntities()
                .stream()
                .filter(s -> Objects.equals(s.getUsrgrpid(), usrgrpid))
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllActiveByUserOrGroup(Integer usrgrpid)
    {
        return getAllEntities()
                .stream()
                .filter(s -> Objects.equals(s.getUsrgrpid(), usrgrpid) && s.getActive())
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllHospitalByUserOrGroup(Integer usrgrpid)
    {
        return getAllEntities()
                .stream()
                .filter(s -> s.getHospital() && Objects.equals(s.getUsrgrpid(), usrgrpid))
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllHospitalActiveForGroups()
    {
        return getAllEntities()
                .stream()
                .filter(s -> s.getHospital() && s.getActive() && s.getForgroup())
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllHomeActiveForGroups()
    {
        return getAllEntities()
                .stream()
                .filter(s -> !s.getHospital() && s.getActive() && s.getForgroup())
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllForPatientsInGroup(Integer grpid)
    {
        return getAllEntities()
                .stream()
                .filter(s -> !s.getForgroup() && Objects.equals(s.getGrpid(), grpid))
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllHospitalActiveForPatients()
    {
        return getAllEntities()
                .stream()
                .filter(s -> s.getHospital() && s.getActive() && !s.getForgroup())
                .collect(Collectors.toList());
    }

    public List<MSRSession> findAllHomeActiveForPatients()
    {
        return getAllEntities()
                .stream()
                .filter(s -> !s.getHospital() && s.getActive() && !s.getForgroup())
                .collect(Collectors.toList());
    }

    /**
     * Updates a session exercises field by marking an exercise as done or not
     *
     * @param sessid     id for the session to update
     * @param exerciseId id for the exercise to update in the session's exercises field'
     * @param done       field done of the exercise, marking the exercise as done or not
     * @return true if the update succeeds, false otherwise
     */
    public boolean updateExerciseDone(int sessid, int exerciseId, boolean done)
    {
        MSRSession sess = getEntity(sessid);
        JSONArray jsonArr = new JSONArray(sess.getExercises());
        JSONObject json;
        boolean sessionEnded = true;
        for (int i = 0; i < jsonArr.length(); i++)
        {
            json = jsonArr.getJSONObject(i);
            if (json.getInt("id") == exerciseId)
            {
                json.put("done", done);
                jsonArr.put(i, json);
            }
            sessionEnded = sessionEnded && json.getBoolean("done");
        }
        sess.setExercises(jsonArr.toString());
        sess.setActive(!sessionEnded);
        updateEntity(sess);
        return true;
    }



    public boolean hasVisibleExercises(MSRSession session)
    {
        JSONArray jsonArray = new JSONArray(session.getExercises());
        return IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .map(jobj -> jobj.getInt("id"))
                .anyMatch(id -> getExerciseVisibilityInSession(session, id))
                ;
    }

    public void setExerciseVisibilityInSession(MSRSession session, int exerciseId, boolean visible)
    {
        JSONArray jsonArray = new JSONArray(session.getExercises());
        IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .filter(obj -> obj.getInt("id") == exerciseId)
                .findFirst()
                .ifPresent(obj -> obj.put("visible", visible));
        session.setExercises(jsonArray.toString());

        putEntity(session);
    }

    public boolean getExerciseVisibilityInSession(MSRSession session, int exerciseId)
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
