package dao_and_model.dao_interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Interface, classes of DAO layer have to implement
 * Roman 15.05.2016.
 */
interface Dao<T> {
    void update(T entity);
    void delete(T entity);
    Optional<T> getById(int id);

    default PreparedStatement getStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
