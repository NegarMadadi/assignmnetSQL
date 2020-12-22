package org.example.data.todo_item;

import org.example.data.MyDataSource;
import org.example.data.person.PersonDao;
import org.example.data.person.PersonRepositoryJDBC;
import org.example.entity.Person;
import org.example.entity.Todo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TodoRepositoryJDBC implements TodoDao {

    public static final String INSERT_WITH_ASSIGNEE = "INSERT INTO todo_item(title, description, deadline, done, assignee_id) VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_WITHOUT_ASSIGNEE = "INSERT INTO todo_item(title, description, deadline, done) VALUES (?, ?, ?, ?)";
    public static final String TODO_FIND_ID = "SELECT * FROM todo_item WHERE todo_id = ?";
    public static final String TODO_FIND_DONE = "SELECT * FROM todo_item WHERE done = ?";
    public static final String TODO_FIND_ASSIGNEEID = "SELECT * FROM todo_item WHERE assignee_id = ?";
    public static final String TODO_FIND_NOTASSIGNED = "SELECT * FROM todo_item WHERE assignee_id is NULL";

    private PersonDao personDao;

    public TodoRepositoryJDBC(PersonDao personDao){
        this.personDao = personDao;
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Todo create(Todo newTodo) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet keySet = null;

        try {
            connection = MyDataSource.getConnection();
            statement = connection.prepareStatement(INSERT_WITH_ASSIGNEE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newTodo.getTitle());
            statement.setString(2, newTodo.getDescription());
            statement.setObject(3, newTodo.getDeadline());
            statement.setBoolean(4, newTodo.isDone());
            if (newTodo.getAssignee() == null) {
                statement.setObject(5, null);
            } else {
                statement.setInt(5, newTodo.getAssignee().getPerson_id());
            }
            statement.execute();
            keySet = statement.getGeneratedKeys();
            while (keySet.next()) {
                newTodo = new Todo(
                        keySet.getInt(1),
                        newTodo.getTitle(),
                        newTodo.getDescription(),
                        newTodo.getDeadline(),
                        newTodo.isDone(),
                        newTodo.getAssignee()

                );
            }

        } catch (SQLException ex) {

            ex.printStackTrace();
        }
        return newTodo;
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Todo> findAll() {
        List<Todo> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = MyDataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM todo_item");
            resultSet = statement.executeQuery();
            {

                while (resultSet.next()) {
                    result.add(createTodoFromResultSet(resultSet));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;

    }

//-----------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Optional<Todo> findById(int id) {

        Optional<Todo> result = Optional.empty();

        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = createFindByIdStatement(connection, TODO_FIND_ID, id);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {

                Todo todo = createTodoFromResultSet(resultSet);
                result = Optional.of(todo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return result;
    }

    private Todo createTodoFromResultSet(ResultSet resultSet) throws SQLException {
        Todo todo = new Todo(
                resultSet.getInt("todo_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getObject("deadline", LocalDate.class),
                resultSet.getBoolean("done"),
                null
        );

        PersonDao personDao = new PersonRepositoryJDBC();
        Optional<Person> optionalPerson = personDao.findById(resultSet.getInt("assignee_id"));
        if (optionalPerson.isPresent()) {
            todo.setAssignee(optionalPerson.get());
        }
        return todo;
    }

    private PreparedStatement createFindByIdStatement(Connection connection, String sql, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        return statement;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Todo> findByDoneStatus(boolean done) {
        Optional<Todo> todoOptional = Optional.empty();
        List<Todo> result = new ArrayList<>();
        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = createFindByDoneStatusStatement(connection, TODO_FIND_DONE, done);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {

                Todo todo = createTodoFromResultSet(resultSet);
                todoOptional = Optional.of(todo);
                if(todoOptional.isPresent())
                    result.add(todoOptional.get());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return result;
    }
    private PreparedStatement createFindByDoneStatusStatement(Connection connection, String sql, boolean done) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setBoolean(1, done);
        return statement;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Todo> findByAssignee(int PersonId) {
        Optional<Todo> todoOptional = Optional.empty();
        List<Todo> result = new ArrayList<>();
        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = createFindByAssigneeIdStatement(connection, TODO_FIND_ASSIGNEEID, PersonId);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {

                Todo todo = createTodoFromResultSet(resultSet);
                todoOptional = Optional.of(todo);
                if(todoOptional.isPresent())
                    result.add(todoOptional.get());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return result;
    }
    private PreparedStatement createFindByAssigneeIdStatement(Connection connection, String sql, int personId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, personId);
        return statement;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Todo> findByAssignee(Person person) {
        if (personDao.findById(person.getPerson_id()).isPresent()){
            return findByAssignee(person.getPerson_id());
        }
        return new ArrayList<>();
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Todo> findByUnassignedTodoItems() {
        Optional<Todo> todoOptional = Optional.empty();
        List<Todo> result = new ArrayList<>();
        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = createFindNotAssigneeStatement(connection, TODO_FIND_NOTASSIGNED);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {

                Todo todo = createTodoFromResultSet(resultSet);
                todoOptional = Optional.of(todo);
                if(todoOptional.isPresent())
                    result.add(todoOptional.get());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return result;
    }

    public static final String UPDATE_TODO = "UPDATE todo_item SET title = ?, description = ?, deadline = ?, done = ?, assignee_id = ? WHERE (todo_id = ?)";
    @Override
    public Todo update(Todo todo) {

        if (todo.getTodo_id() == 0){
            throw new IllegalArgumentException();
        }

        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_TODO)){
                statement.setString(1, todo.getTitle());
                statement.setString(2, todo.getDescription());
                statement.setObject(3, todo.getDeadline());
                statement.setBoolean(4,todo.isDone());
                statement.setInt(5,todo.getAssignee() != null ? todo.getAssignee().getPerson_id() : null);
                statement.setInt(6,todo.getTodo_id());

                statement.execute();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return todo;
    }

    private PreparedStatement createFindNotAssigneeStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static final String DELETE_TODO = "DELETE FROM todo_item WHERE todo_id = ?";
    @Override
    public boolean deleteById(int id) {
        int rowAffected = 0;
        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_TODO)){
            statement.setInt(1,id);
            rowAffected = statement.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return true;
    }

}
