package dao_and_model.connection_pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

/**
 * Proxy for {@link Connection} with custom methods to work in {@link ConnectionPool}.
 */
interface PooledConnection extends ConnectionProxy {

    BlockingQueue<Connection> getFreeConnections();
    BlockingQueue<Connection> getReservedConnections();

    /**
     * @return {@link Connection} wrapped in {@link PooledConnection}.
    */
    static PooledConnection wrap(Connection connection, BlockingQueue<Connection> freeConnections, BlockingQueue<Connection> reservedConnections) {
        return new PooledConnection() {
            @Override
            public BlockingQueue<Connection> getFreeConnections() {
                return freeConnections;
            }

            @Override
            public BlockingQueue<Connection> getReservedConnections() {
                return reservedConnections;
            }

            @Override
            public Connection toSrc() {
                return connection;
            }
        };
    }

    /**
     * Closes connection.
     * @throws SQLException
     */
    default void reallyClose() throws SQLException {
        toSrc().close();
    }

    /**
     * Return PooledConnection to {@code freeConnections}
     * @throws SQLException
     */
    @Override
    default void close() throws SQLException {
        if (toSrc().isClosed()) {
            throw new SQLException("Attempting to close closed connection.");
        }
        if (toSrc().isReadOnly()) {
            toSrc().setReadOnly(false);
        }
        if (!getReservedConnections().remove(this)) {
            throw new SQLException("Error deleting connection from the given away connections pool.");
        }
        if (!getFreeConnections().offer(this)) {
            throw new SQLException("Error allocating connection in the pool.");
        }
    }
}
