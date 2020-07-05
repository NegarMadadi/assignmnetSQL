package org.example.entity;

import java.util.Objects;

public class Person {
// fields
    private  Integer person_id;
    private String first_name;
    private String last_name;

// constructors
    public Person(Integer person_id, String first_name, String last_name) {
        this.person_id = person_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }


    public Person(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }


    //getters and setters
    public Integer getPerson_id() {
        return person_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    //hashcode and equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return getPerson_id() == person.getPerson_id() &&
                Objects.equals(getFirst_name(), person.getFirst_name()) &&
                Objects.equals(getLast_name(), person.getLast_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPerson_id(), getFirst_name(), getLast_name());
    }


    //toString method
    @Override
    public String toString() {
        return "Person{" +
                "person_id=" + person_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}


