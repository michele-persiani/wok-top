/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.MagneticResonance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author floriano
 */
public class MagneticResonanceController extends JPAEntityController<MagneticResonance>
{

    public MagneticResonanceController()
    {
        super(MagneticResonance.class, PersistenceUnit.DEFAULT_PU);
    }


    public List<MagneticResonance> findAllByClinicalprofileid(Integer id)
    {
        return getAllEntities()
                .stream()
                .filter(mr -> Objects.equals(mr.getClinicalprofileid(), id))
                .collect(Collectors.toList());
    }


}
