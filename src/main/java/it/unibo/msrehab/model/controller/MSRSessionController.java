/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.MSRSession;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: add documentation
 *
 * @author floriano
 *
 */
public class MSRSessionController extends BaseEntityController<MSRSession>
{

    private EntityManagerFactory emf;
    private String peristenceUnit = "DEFAULT_PU";
    private static final Logger logger = LoggerFactory.getLogger(MSRSessionController.class);

    public MSRSessionController()
    {
        super(MSRSession.class);
    }

    private EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(this.peristenceUnit);
        }
        return emf.createEntityManager();
    }


    public List<MSRSession> findAllByUserOrGroup(Integer usrgrpid)
    {
        return getAllEntities(s -> Objects.equals(s.getUsrgrpid(), usrgrpid));
    }

    public List<MSRSession> findAllActiveByUserOrGroup(Integer usrgrpid)
    {
        return getAllEntities(s -> Objects.equals(s.getUsrgrpid(), usrgrpid) && s.getActive());
    }

    public List<MSRSession> findAllHospitalByUserOrGroup(Integer usrgrpid)
    {
        return getAllEntities(s -> s.getHospital() && Objects.equals(s.getUsrgrpid(), usrgrpid));
    }

    public List<MSRSession> findAllHospitalActiveForGroups()
    {
        return getAllEntities(s -> s.getHospital() && s.getActive() && s.getForgroup());
    }

    public List<MSRSession> findAllHomeActiveForGroups()
    {
        return getAllEntities(s -> !s.getHospital() && s.getActive() && s.getForgroup());
    }

    public List<MSRSession> findAllForPatientsInGroup(Integer grpid)
    {
        return getAllEntities(s -> !s.getForgroup() && Objects.equals(s.getGrpid(), grpid));
    }

    public List<MSRSession> findAllHospitalActiveForPatients()
    {
        return getAllEntities(s -> s.getHospital() && s.getActive() && !s.getForgroup());
    }

    public List<MSRSession> findAllHomeActiveForPatients()
    {
        return getAllEntities(s -> !s.getHospital() && s.getActive() && !s.getForgroup());
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
        MSRSession sess = findEntity(sessid).get();
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
        return updateEntity(sess);
    }

}
