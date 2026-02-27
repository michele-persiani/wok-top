package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class modeling an exercise element of the multiple sclerosis rehabilitation
 * system
 */
@Entity
@Table(name = "exelement")
@NamedQueries({
    @NamedQuery(name = "ExElement.findAll", query = "SELECT l FROM ExElement l ORDER BY l.id ASC"),
    @NamedQuery(name = "ExElement.findAllByCategory", query = "SELECT l FROM ExElement l WHERE l.category= :categoryid ORDER BY l.id ASC"),
    @NamedQuery(name = "ExElement.findAllByCategoryAndDescription", query = "SELECT l FROM ExElement l WHERE l.category= :categoryid AND l.eldescr= :descr ORDER BY l.id ASC"),
    @NamedQuery(name = "ExElement.delete", query = "DELETE FROM ExElement l WHERE l.id= :exelementid")})
//    @NamedQuery(name = "ExElement.find", query = "SELECT l FROM ExElement l WHERE l.email= :email")})

@XmlRootElement
public class ExElement extends BaseEntity
{

    /**
     * Enumeration of the element categories
     */
    public enum CategoryValue {
        I_FRUIT, I_VEGETABLE, I_ANIMAL, I_CHESS, I_ARROW, I_ARROW_B, I_FACE,
        I_MIXED,
        I_FRUIT__I_VEGETABLE, I_VEGETABLE__I_ANIMAL, I_FRUIT__I_ANIMAL,
        I_FACE__W_NAME,
        W_NAME_COM_F, W_NAME_ITA_F, W_NAME_FRN_F,
        W_NAME_COM_M, W_NAME_ITA_M, W_NAME_FRN_M,
        S_BEEP;
    };

    @JSON(include = false)
    private static final long serialVersionUID = 1L;


    @Column(name = "category", table = "exelement")
    @Enumerated(EnumType.STRING)
    private CategoryValue category;

    @Column(name = "elname", table = "exelement")
    @Basic
    private String elname;

    @Column(name = "eldescr", table = "exelement")
    @Basic
    private String eldescr;

    @Column(name = "url", table = "exelement")
    @Basic
    private String url;


    public CategoryValue getCategory() {
        return category;
    }

    public void setCategory(CategoryValue category) {
        this.category = category;
    }

    public String getElname() {
        return elname;
    }

    public void setElname(String elname) {
        this.elname = elname;
    }

    public String getEldescr() {
        return eldescr;
    }

    public void setEldescr(String eldescr) {
        this.eldescr = eldescr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return String.valueOf(getId()); // Needs to be only id for method buildExElementListFromIds() of EcerciseService
    }
}
