/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.NPSTest;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author floriano
 */
public class NPSTestController extends BaseEntityController<NPSTest>
{
    public NPSTestController()
    {
        super(NPSTest.class);
    }

    public List<NPSTest> findAllByUser(Integer id)
    {
        return getAllEntities(tst -> Objects.equals(tst.getUserid(), id))
                .stream()
                .sorted(Comparator.comparing(NPSTest::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}
