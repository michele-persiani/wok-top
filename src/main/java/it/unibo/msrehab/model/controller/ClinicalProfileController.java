/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.ClinicalProfile;

import java.util.*;
import java.util.stream.Collectors;


public class ClinicalProfileController extends BaseEntityController<ClinicalProfile>
{

    public ClinicalProfileController()
    {
        super(ClinicalProfile.class);
    }


    public List<ClinicalProfile> findAllByUser(Integer id)
    {
        return getAllEntities(cp -> Objects.equals(cp.getUserid(), id))
                .stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(ClinicalProfile::getTimestamp).reversed()))
                .collect(Collectors.toList());
    }

}
