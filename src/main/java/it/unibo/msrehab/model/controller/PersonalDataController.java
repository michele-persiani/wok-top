/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.PersonalData;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class PersonalDataController extends BaseEntityController<PersonalData>
{

    public PersonalDataController()
    {
        super(PersonalData.class);
    }

    public List<PersonalData> findAllByUser(Integer id)
    {
        return getAllEntities(tst -> Objects.equals(tst.getUserid(), id))
                .stream()
                .sorted(Comparator.comparing(PersonalData::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

}
