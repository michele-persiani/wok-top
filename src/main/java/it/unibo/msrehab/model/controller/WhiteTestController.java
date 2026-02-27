/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.WhiteTest;


import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class WhiteTestController extends BaseEntityController<WhiteTest>
{

    public WhiteTestController()
    {
        super(WhiteTest.class);
    }


    public List<WhiteTest> findAllByUser(Integer id)
    {
        return getAllEntities(wt -> Objects.equals(wt.getPatientid(), id))
                .stream()
                .sorted(Comparator.comparing(WhiteTest::getTimestamp))
                .collect(Collectors.toList());
    }

}
