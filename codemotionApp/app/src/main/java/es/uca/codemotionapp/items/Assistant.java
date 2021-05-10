package es.uca.codemotionapp.items;

import java.util.Date;

public class Assistant {
    private String id,dni,name,surname,email;
    private int phone,age;
    private Date birthDate,startDate,endDate,inscriptionDate;


    public Assistant(String id,String dni, String name, String surname, String email, int phone, int age, Date birthDate, Date startDate, Date endDate,Date inscriptionDate) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.birthDate = birthDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inscriptionDate = inscriptionDate;

    }

    //We wrote all the methods becasuse we don't know if in the future the next person
    //will have access to this class.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getInscriptionDate() {
        return inscriptionDate;
    }

    public void setInscriptionDate(Date inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }
}
