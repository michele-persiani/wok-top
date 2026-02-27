package it.unibo.msrehab.model.entities;

import flexjson.JSON;
//import java.util.regex.Pattern;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.*;

/**
 * Class modeling a user of the
 * multiple sclerosis rehabilitation system
 */

@Entity
@Table(name = "msruser")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT l FROM MSRUser l ORDER BY l.id ASC"),
    @NamedQuery(name = "User.findAllPatients", query = "SELECT l FROM MSRUser l where l.msrrole= :msrrole ORDER BY l.surname ASC"),
    @NamedQuery(name = "User.findAllPatientsInGroup", query = "SELECT l FROM MSRUser l where l.msrrole= :msrrole AND l.gid= :gid ORDER BY l.id ASC"),
    @NamedQuery(name = "User.findAllPatientsNotInGroups",
            query = "SELECT DISTINCT u FROM MSRUser u, PersonalData p, NPSTest n, RaoTest r, WhiteTest w "
                    + "where u.msrrole= :msrrole AND u.gid=-1 AND u.id = p.userid AND u.id=n.userid AND u.id=r.userid AND u.id=w.patientid ORDER BY u.id ASC"),
    //@NamedQuery(name = "User.findAllPatientsNotInGroupsInCenter",
    //        query = "SELECT DISTINCT u FROM MSRUser u, PersonalData p, NPSTest n, RaoTest r, WhiteTest w "
    //                + "where u.msrrole= :msrrole AND u.cid= :cid AND u.gid=-1 AND u.id = p.userid AND u.id=n.userid AND u.id=r.userid AND u.id=w.patientid ORDER BY u.id ASC"),
    @NamedQuery(name = "User.findAllPatientsNotInGroupsInCenter",
            query = "SELECT DISTINCT u FROM MSRUser u, PersonalData p "
                    + "where u.msrrole= :msrrole AND u.cid= :cid AND u.gid=-1 AND u.id = p.userid ORDER BY u.id ASC"),
    @NamedQuery(name = "User.findAllPatientsInCenter", query = "SELECT l FROM MSRUser l where l.msrrole= :msrrole AND l.cid= :cid ORDER BY l.id ASC"),
    @NamedQuery(name = "User.delete", query = "DELETE FROM MSRUser l WHERE l.id= :msruserid"),
    @NamedQuery(name = "User.find", query = "SELECT l FROM MSRUser l WHERE l.email= :email"),
    @NamedQuery(name = "User.findById", query = "SELECT l FROM MSRUser l WHERE l.id= :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT l FROM MSRUser l WHERE l.username= :username"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT l FROM MSRUser l WHERE l.email= :email")})
  
public class MSRUser extends BaseEntity
{
    /**
     * Enumeration of the system roles
     */
     public enum RoleValue {
        ADMIN, OPERATOR, PATIENT;
    };

    @JSON(include = false)
    private static final long serialVersionUID = 1L;

  
    @Column(name = "photo", table = "msruser")
    @Basic    
    private String photo;    

    @Column(name = "surname", table = "msruser")
    @Basic
    @NotNull
    @Size(min=2,
            message="Cognome non valido. Deve essere di almeno due caratteri")    
    private String surname;



    @Column(name = "username", table="msruser", unique=true)
    @NotNull
    @Basic
    private String username;


    @Column(name = "name", table = "msruser")
    @Basic
    @NotNull
    @Size(min=2,
            message="Nome non valido. Deve essere di almeno due caratteri")    
    private String name;
    
    @Column(name = "email", table = "msruser", unique=true)
    @Basic

    @Pattern(regexp="[A-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
        +"[A-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
        +"(?:[A-z0-9](?:[A-z0-9-]*[A-z0-9])?\\.)+[A-z0-9](?:[A-z0-9-]*[A-z0-9])?",
             message="Email non valida")
    private String email;
    
    @Column(name = "password", table = "msruser")
    @Basic
    @Size(min=6)    
    private String password;

    @Column(name = "phone", table = "msruser")
    @Basic
    @Pattern(regexp="^[0-9]*$",
            message="Numero di telefono non valido")
    private String phone;

    @Column(name = "msrrole", table = "msruser")
    @Enumerated(EnumType.STRING)
    private RoleValue msrrole;
    
    @Column(name = "gid", table = "msruser")
    @Basic
    private Integer gid;
    
    @Column(name = "cid", table = "msruser")
    @Basic
    private Integer cid;
    

    
     public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleValue getMsrrole() {
        return msrrole;
    }

    public void setMsrrole(RoleValue msrrole) {
        this.msrrole = msrrole;
    }
    
    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }
    
    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
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
        
        
        //String strPattern
        //        = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(strPattern);
        return pattern.matcher(email).matches();

    }
    
    public static boolean isIdValid(String id) {
        
        String strPattern = "^[0-9]*$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(strPattern);
        return (pattern.matcher(id).matches());
    }

    public static boolean isPasswordValid(String password) {
        return (password.length() >= 6);
    }

    public static boolean isPhotoValid(String photo) {
        return true;
    }

    public static boolean isSurnameValid(String surname) {
        return (surname.length() > 0);
    }

    //aggiunto per username
    public static boolean isUsernameValid(String username) {
        return (username.length() > 0);
    }
    

    public static boolean isNameValid(String name) {
        return (name.length() > 0);
    }

    public static boolean isPhoneValid(String phone) {
        String strPattern = "^[0-9]*$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(strPattern);
        return (pattern.matcher(phone).matches());
    }    

}