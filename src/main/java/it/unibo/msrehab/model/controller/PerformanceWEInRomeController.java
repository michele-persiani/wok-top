/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.PerformanceWEInRome;

/**
 *
 * @author sara
 */
public class PerformanceWEInRomeController extends JPAEntityController<PerformanceWEInRome>
{
    public PerformanceWEInRomeController()
    {
        super(PerformanceWEInRome.class, PersistenceUnit.DEFAULT_PU);
    }

}
