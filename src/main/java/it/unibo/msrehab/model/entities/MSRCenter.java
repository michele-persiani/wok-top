package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.*;

/**
 * Class modeling a rehabilitation center of the
 * multiple sclerosis rehabilitation system
 */

@Entity
@Table(name = "msrcenter")
@NamedQueries({
    @NamedQuery(name = "MSRCenter.findAll", query = "SELECT l FROM MSRCenter l ORDER BY l.id ASC"),
    @NamedQuery(name = "MSRCenter.delete", query = "DELETE FROM MSRCenter l WHERE l.id= :msrcenterid"),
    @NamedQuery(name = "MSRCenter.findById", query = "SELECT l FROM MSRCenter l WHERE l.id= :id")})

public class MSRCenter extends BaseEntity
{
    
    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    @Column(name = "name", table = "msrcenter")
    @Basic
    @NotNull
    @Size(min=2,
            message="Nome non valido. Deve essere di almeno due caratteri")    
    private String name;
    
    @Column(name = "email", table = "msrcenter", unique=true)
    @Basic
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
             message="Email non valida")
    private String email;

    @Column(name = "phone", table = "msrcenter")
    @Basic
    @Pattern(regexp="^[0-9]*$",
            message="Numero di telefono non valido")
    private String phone;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public static boolean isNameValid(String name) {
        return !name.isEmpty();
    }

    /**
     * Checks if an email address is formally valid
     *
     * @param email the email address
     * @return true if email is valid, false otherwise
     */
    public static boolean isEmailValid(String email) {
        
        String strPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
                + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
                + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
                
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(strPattern);
        return pattern.matcher(email).matches();

    }
    
    public static boolean isPhoneValid(String phone) {
        String strPattern = "^[0-9]*$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(strPattern);
        return (pattern.matcher(phone).matches());
    }    

}