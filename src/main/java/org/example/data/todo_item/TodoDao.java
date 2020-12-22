package org.example.data.todo_item;

import org.example.entity.Person;
import org.example.entity.Todo;

import java.util.Collection;
import java.util.Optional;

public interface TodoDao {

    Todo create (Todo newTodo);
    Collection<Todo> findAll();
    Optional<Todo> findById (int id);
    Collection<Todo> findByDoneStatus (boolean done);
    Collection<Todo> findByAssignee (int PersonId);
    Collection<Todo> findByAssignee (Person person);
    Collection<Todo> findByUnassignedTodoItems();
    Todo update(Todo update);
    boolean deleteById(int id);

}
