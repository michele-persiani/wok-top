/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.MagneticResonance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author floriano
 */
public class MagneticResonanceController extends BaseEntityController<MagneticResonance>
{

    public MagneticResonanceController()
    {
        super(MagneticResonance.class);
    }


    public List<MagneticResonance> findAllByClinicalprofileid(Integer id)
    {
        return new ArrayList<>(getAllEntities(mr -> Objects.equals(mr.getClinicalprofileid(), id)));
    }


}
