/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.MSRGroup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TODO: add documentation
 * 
 * @author floriano
 * 
 */

public class MSRGroupController extends JPAEntityController<MSRGroup>
{

    public MSRGroupController()
    {
        super(MSRGroup.class, PersistenceUnit.DEFAULT_PU);
    }

    public List<MSRGroup> findAllGroupsInCenter(Integer centerId)
    {
        return getAllEntities()
                .stream()
                .filter(gr -> Objects.equals(gr.getCid(), centerId))
                .collect(Collectors.toList());
    }
}
