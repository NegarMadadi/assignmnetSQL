package org.example.data.person;

import org.example.entity.Person;

import java.util.Collection;
import java.util.Optional;

public interface PersonDao{

    Person create (Person newPerson);
    Collection<Person> findAll();
    Optional<Person> findById(int id);
    Collection<Person> findByName(String name);
    Person update(Person updated);
    boolean deleteById(int id);
}
