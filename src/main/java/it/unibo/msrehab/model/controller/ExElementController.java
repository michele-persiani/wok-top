/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.ExElement;
import it.unibo.msrehab.model.entities.ExElement.CategoryValue;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO: add documentation
 *
 * @author floriano
 */

public class ExElementController extends BaseEntityController<ExElement>
{
    public ExElementController()
    {
        super(ExElement.class);
    }


    public List<ExElement> getAllRecordsByCategory(CategoryValue cat)
    {
        return getAllEntities(exel -> Objects.equals(exel.getCategory(), cat));
    }

    public List<ExElement> getRandomRecordsByCategory(CategoryValue cat, int n)
    {
        return sampleElements(
                n,
                getAllRecordsByCategory(cat)
        );
    }

    public List<ExElement> getUniqueRandomRecordsByCategory(CategoryValue cat, int n)
    {
        return sampleElements(
                n,
                getAllRecordsByCategory(cat),
                ExElement::getId
        );
    }

    public List<ExElement> getUniqueRandomRecordsByCategoryAndDescription(CategoryValue cat, int n, String descr)
    {
        return sampleElements(
                n,
                getAllEntities(exel -> Objects.equals(exel.getCategory(), cat) && Objects.equals(exel.getEldescr(), descr))
        );
    }

    public List<ExElement> getRandomRecordsForNbackByCategory(CategoryValue cat, int n, int nback)
    {
        List<ExElement> sampled = getRandomRecordsByCategory(cat, n);

        for (int i = nback; i < sampled.size(); i++)
            if (Math.random() < 0.5)
                sampled.set(i - nback, sampled.get(i));

        return sampled;
    }


    /**
     * Gets {@code n} random elements from the list of elements, without duplicates.
     *
     * @param n
     * @param categories
     * @return
     */
    public List<ExElement> getRandomRecordsByCategories(int n, CategoryValue... categories)
    {
        if (n <= 0 || categories.length == 0)
            return new ArrayList<>();

        List<ExElement> elements = Arrays.stream(categories)
                .map(this::getAllRecordsByCategory)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return sampleElements(
                n,
                elements
        );
    }

    /**
     * Increase the frequency of the elements from targets inside the list elements, with a certain probability.
     * Elements in targets are assured to appear at least once in elements after the call.
     * All elements from targets are assured to appear at least once in elements after the call.
     *
     * @param elements    collection that will be modified by increasing the frequency of the elements from targets.
     * @param targets     list of elements that will be used to increase the frequency of the elements in elements.
     * @param probability probability of increasing the frequency of an element from targets in elements.
     */
    public void increaseFrequencyOfElements(List<ExElement> elements, List<ExElement> targets, double probability)
    {
        for (int i = 0; i < elements.size(); i++)
            if (Math.random() < probability)
                elements.set(i, targets.get(random.nextInt(targets.size())));

        targets.stream()
                .filter(trg -> !elements.contains(trg))
                .forEach(trg ->
                {
                    elements.set(random.nextInt(elements.size()), trg);
                });

    }
}
