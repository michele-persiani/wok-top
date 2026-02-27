/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.MSRUser;

import java.util.List;
import java.util.Objects;





public class MSRUserController extends BaseEntityController<MSRUser>
{

    public MSRUserController()
    {
        super(MSRUser.class);
    }



    /**
     * Finds a user record in the database, given email and password
     * 
     * @param email the email of the user
     * @param password the password of the user
     * 
     * @return the user if the email is found in the database and the password
     * is valid, null otherwise
     */
    public MSRUser findRecord(String email, String password)
    {
        return findEntity(usr -> Objects.equals(usr.getEmail(), email) && Objects.equals(usr.getPassword(), password))
                .orElse(null);
    }
    

    public MSRUser findRecordByUsername(String username, String password)
    {
        return findEntity(usr -> Objects.equals(usr.getUsername(), username) && Objects.equals(usr.getPassword(), password))
                .orElse(null);
    }
    
    public MSRUser findRecordByEmail(String email)
    {
        return findEntity(usr -> Objects.equals(usr.getEmail(), email))
                .orElse(null);
    }
    
    public List<MSRUser> findAllPatientsInGroup(Integer gid)
    {
        return getAllEntities(usr -> usr.getMsrrole() == MSRUser.RoleValue.PATIENT && Objects.equals(usr.getGid(), gid));
    }

    public List<MSRUser> findAllPatientsNotInGroupsInCenter(Integer centerId)
    {
        return getAllEntities(usr -> usr.getMsrrole() == MSRUser.RoleValue.PATIENT && Objects.equals(usr.getGid(), -1) && Objects.equals(usr.getCid(), centerId));
    }

    public List<MSRUser> findAllPatientsInCenter(Integer centerId)
    {
        return getAllEntities(usr -> Objects.equals(usr.getCid(), centerId));
    }
}
