/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.PerformanceWEInRome;

/**
 *
 * @author sara
 */
public class PerformanceWEInRomeController extends DecoratorEntityController<PerformanceWEInRome>
{
    public PerformanceWEInRomeController(Model model)
    {
        super(model, PerformanceWEInRome.class);
    }

}
