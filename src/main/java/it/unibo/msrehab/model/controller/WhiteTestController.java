/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.WhiteTest;


import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class WhiteTestController extends DecoratorEntityController<WhiteTest>
{

    public WhiteTestController(Model model)
    {
        super(model, WhiteTest.class);
    }


    public List<WhiteTest> findAllByUser(Integer id)
    {
        return getAllEntities()
                .stream()
                .filter(wt -> Objects.equals(wt.getPatientid(), id))
                .sorted(Comparator.comparing(WhiteTest::getTimestamp))
                .collect(Collectors.toList());
    }

}
