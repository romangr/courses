package DaoAndModel.DaoInterfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

/**
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
