/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
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

public class MSRGroupController extends DecoratorEntityController<MSRGroup>
{

    public MSRGroupController(Model model)
    {
        super(model, MSRGroup.class);
    }

    public List<MSRGroup> findAllGroupsInCenter(Integer centerId)
    {
        return getAllEntities()
                .stream()
                .filter(gr -> Objects.equals(gr.getCid(), centerId))
                .collect(Collectors.toList());
    }
}
