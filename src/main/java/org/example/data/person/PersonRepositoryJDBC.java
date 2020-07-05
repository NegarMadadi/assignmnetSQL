package org.example.data.person;

import org.example.data.MyDataSource;
import org.example.entity.Person;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PersonRepositoryJDBC implements PersonDao {

    public static final String FIND_BY_ID = "select * FROM person WHERE todo_id = ?";
    public static final String FIND_BY_NAME_LIKE = "SELECT * FROM person where first_name LIKE ?";



    @Override
    public Person create(Person person) {
        if (person.getPerson_id() != 0) {
            throw new IllegalArgumentException();
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet keySet = null;

        try {
            connection = MyDataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO person(first_Name, Last_Name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, person.getFirst_name());
            statement.setString(2, person.getLast_name());

            statement.execute();
            keySet = statement.getGeneratedKeys();


            while (keySet.next()) {
                person = new Person(
                        keySet.getInt(1),
                        person.getFirst_name(),
                        person.getLast_name()
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return person;
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Person> findAll() {
        List<Person> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = MyDataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM person");
            resultSet = statement.executeQuery();
            {

                while (resultSet.next()) {
                    result.add(createPersonFromResultSet(resultSet));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //--------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Optional<Person> findById(int id) {

        Optional<Person> personOptional = Optional.empty();

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement statement = createFindByIdStatement(connection, FIND_BY_ID, id);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                personOptional = Optional.of(createPersonFromResultSet(resultSet));
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        return personOptional;
    }

    private Person createPersonFromResultSet(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getInt("Person_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")


        );
    }


    private PreparedStatement createFindByIdStatement(Connection connection, String findById, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
        statement.setInt(1, id);
        return statement;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<Person> findByName(String name) {

        List<Person> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = MyDataSource.getConnection();
            statement = connection.prepareStatement(FIND_BY_NAME_LIKE);
            statement.setString(1, name.concat("%"));
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(createPersonFromResultSet(resultSet));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    public static final String UPDATE_PERSON = "UPDATE person SET first_name = ?, last_name = ?  WHERE person_id= ? ";
    @Override
    public Person update(Person updatedPerson) {

        if (updatedPerson.getPerson_id() == 0){
            throw new IllegalArgumentException();
        }

       try (
           Connection connection = MyDataSource.getConnection();
           PreparedStatement statement = connection.prepareStatement(UPDATE_PERSON)){
           statement.setString(1, updatedPerson.getFirst_name());
           statement.setString(2, updatedPerson.getLast_name());

           statement.execute();

           }catch (SQLException ex){
           ex.printStackTrace();
       }
       return updatedPerson;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------

    public static final String DELETE_PERSON = "DELETE FROM person WHERE person_id = ?";
    @Override
    public boolean deleteById(int id) {
        int rowAffected = 0;
        try (
                Connection connection = MyDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_PERSON)){



            rowAffected = statement.executeUpdate();

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return true;
    }
}
