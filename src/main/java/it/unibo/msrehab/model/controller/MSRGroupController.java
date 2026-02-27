/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.MSRGroup;

import java.util.List;
import java.util.Objects;

/**
 * TODO: add documentation
 * 
 * @author floriano
 * 
 */

public class MSRGroupController extends BaseEntityController<MSRGroup>
{

    public MSRGroupController()
    {
        super(MSRGroup.class);
    }

    public List<MSRGroup> findAllGroupsInCenter(Integer centerId)
    {
        return getAllEntities(gr -> Objects.equals(gr.getCid(), centerId));
    }
}
