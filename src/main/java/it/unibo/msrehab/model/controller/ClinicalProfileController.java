/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.ClinicalProfile;

import java.util.*;
import java.util.stream.Collectors;


public class ClinicalProfileController extends JPAEntityController<ClinicalProfile>
{

    public ClinicalProfileController()
    {
        super(ClinicalProfile.class, PersistenceUnit.DEFAULT_PU);
    }


    public List<ClinicalProfile> findAllByUser(Integer id)
    {
        return getAllEntities()
                .stream()
                .filter(cp -> Objects.equals(cp.getUserid(), id))
                .sorted(Comparator.nullsLast(Comparator.comparing(ClinicalProfile::getTimestamp).reversed()))
                .collect(Collectors.toList());
    }

}
