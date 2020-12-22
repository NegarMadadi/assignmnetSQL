package org.example;

import org.example.data.MyDataSource;
import org.example.data.person.PersonDao;
import org.example.data.person.PersonRepositoryJDBC;
import org.example.data.todo_item.TodoDao;
import org.example.data.todo_item.TodoRepositoryJDBC;
import org.example.entity.Person;
import org.example.entity.Todo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class App
{
    public static void main( String[] args ) throws SQLException {
        PersonDao peopleREPO = new PersonRepositoryJDBC();
        peopleREPO.create(new Person("Sanaz", "Nazari"));
        peopleREPO.create(new Person("Manizhe", "Pirborna"));
        peopleREPO.create(new Person("Ali", "Rezayi"));
        peopleREPO.create(new Person("Bahram", "Saberi"));
        peopleREPO.create(new Person("Liam", "Naderi"));
        System.out.println("Find all testing:");
        for (Person person:peopleREPO.findAll()){
            System.out.println(person.toString());
        }
        System.out.println("Check find by Name and Id:");
        Collection<Person> samplePeople = peopleREPO.findByName("Manizhe");
        Optional<Person> lastSamplePerson = Optional.empty();
        for(Person samplePerson : samplePeople){
            Person findByIdPerson = peopleREPO.findById(samplePerson.getPerson_id()).get();
            System.out.println(findByIdPerson.toString());
            lastSamplePerson = Optional.of(findByIdPerson);
        }
        System.out.println("Check update:");
        if(lastSamplePerson.isPresent()){
            Person temp = lastSamplePerson.get();
            temp.setFirst_name("Sara");
            peopleREPO.update(temp);
            System.out.println(peopleREPO.findById(temp.getPerson_id()).get().toString());
            System.out.println("Check delete:");
            peopleREPO.deleteById(temp.getPerson_id());
        }
        System.out.println("After deleting:");
        for (Person person:peopleREPO.findAll()){
            System.out.println(person.toString());
        }
        TodoDao todoItemsDAO = new TodoRepositoryJDBC(peopleREPO);
        System.out.println("Creating todo Check:");
        Person assignee = new Person("Leon","Brown");
        assignee = peopleREPO.create(assignee);
        Todo todo1 = new Todo("First Title","First Description", LocalDate.now().plusDays(10),false,assignee);
        Todo todo2 = new Todo("Second Title","Second Description",LocalDate.now().plusDays(5),true,assignee);
        Todo todo3 = new Todo("Third Title","Third Description",LocalDate.now().plusDays(3),false);
        todo1 = todoItemsDAO.create(todo1);
        todo2 = todoItemsDAO.create(todo2);
        todo3 = todoItemsDAO.create(todo3);
        System.out.println("Find all testing:");
        for (Todo todo : todoItemsDAO.findAll()){
            System.out.println(todo.toString());
        }
        System.out.println("Find by id testing:");
        System.out.println(todoItemsDAO.findById(todo1.getTodo_id()).get().toString());
        System.out.println("Find by done status:");
        for (Todo item:todoItemsDAO.findByDoneStatus(true)){
            System.out.println(item.toString());
        }
        System.out.println("Find by assignee id:");
        for (Todo item:todoItemsDAO.findByAssignee(assignee.getPerson_id())){
            System.out.println(item.toString());
        }
        System.out.println("Find by assignee:");
        for (Todo item:todoItemsDAO.findByAssignee(assignee)){
            System.out.println(item.toString());
        }
        System.out.println("Find not assigned:");
        for (Todo item:todoItemsDAO.findByUnassignedTodoItems()){
            System.out.println(item.toString());
        }
        todo3.setAssignee(assignee);
        todo3.setDeadline(LocalDate.now().plusDays(100));
        todo3.setDone(true);
        System.out.println("Update testing:");
        todoItemsDAO.update(todo3);
        System.out.println("Find by assignee after updating:");
        for (Todo item:todoItemsDAO.findByAssignee(assignee)){
            System.out.println(item.toString());
        }
        System.out.println("Deleting todo testing:");
        todoItemsDAO.deleteById(todo2.getTodo_id());
        System.out.println("Find all after deleting:");
        for (Todo todo : todoItemsDAO.findAll()){
            System.out.println(todo.toString());
        }
    }
}
