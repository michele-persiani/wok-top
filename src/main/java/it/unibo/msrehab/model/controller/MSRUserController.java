/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.MSRUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;
import java.util.stream.Collectors;


public class MSRUserController extends JPAEntityController<MSRUser>
{
    public MSRUserController()
    {
        super(MSRUser.class, PersistenceUnit.DEFAULT_PU);
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
    public MSRUser findRecordByEmail(String email, String password)
    {
        return getAllEntities()
                .stream()
                .filter(usr -> Objects.equals(usr.getEmail(), email) && Objects.equals(usr.getPassword(), password))
                .findFirst()
                .orElse(null);
    }
    

    public MSRUser findRecordByUsername(String username, String password)
    {
        return getAllEntities()
                .stream()
                .filter(usr -> Objects.equals(usr.getUsername(), username) && Objects.equals(usr.getPassword(), password))
                .findFirst()
                .orElse(null);
    }
    
    public MSRUser findRecordByEmail(String email)
    {
        return getAllEntities()
                .stream()
                .filter(usr -> Objects.equals(usr.getEmail(), email))
                .findFirst()
                .orElse(null);
    }
    
    public List<MSRUser> findAllPatientsInGroup(Integer gid)
    {
        return getAllEntities()
                .stream()
                .filter(usr -> usr.getMsrrole() == MSRUser.RoleValue.PATIENT && Objects.equals(usr.getGid(), gid))
                .collect(Collectors.toList());
    }

    public List<MSRUser> findAllPatientsNotInGroupsInCenter(Integer centerId)
    {
        return getAllEntities()
                .stream()
                .filter(usr -> usr.getMsrrole() == MSRUser.RoleValue.PATIENT)
                .filter(usr -> Objects.equals(usr.getGid(), -1) || usr.getGid() == null)
                .filter(usr -> Objects.equals(usr.getCid(), centerId))
                .collect(Collectors.toList());
    }

    public List<MSRUser> findAllPatientsInCenter(Integer centerId)
    {
        return getAllEntities()
                .stream()
                .filter(usr -> Objects.equals(usr.getCid(), centerId))
                .collect(Collectors.toList());
    }


    /**
     * Copies properties from one object to another
     * @param source
     * @destination
     * @return
     */
    public void copyNonNullProperties(MSRUser source, MSRUser destination, String... ignoreProperties)
    {
        Set<String> ignorePropertiesSet = getNullPropertyNames(source);
        ignorePropertiesSet.addAll(Arrays.asList(ignoreProperties));

        BeanUtils.copyProperties(source, destination, (String[]) ignorePropertiesSet.toArray());
    }

    /**
     * Returns an array of null properties of an object
     * @param source
     * @return
     */
    public Set<String> getNullPropertyNames (MSRUser source)
    {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds)
        {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames;
    }


}
