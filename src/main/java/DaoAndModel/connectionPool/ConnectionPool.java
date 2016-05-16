package DaoAndModel.connectionPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> reservedConnections;

    private String driverName;
    private String url;
    private String user;
    private String password;
    private int poolSize;
    private boolean closing;

    public ConnectionPool() {

        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("resources/db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driverName = properties.getProperty("driver", "org.h2.Driver");
        url = properties.getProperty("url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        user = properties.getProperty("user", "");
        password = properties.getProperty("password", "");
        poolSize = Integer.parseInt(properties.getProperty("poolsize", "5"));

        initPoolData();
    }

    public ConnectionPool(String driverName, String url, String user, String password, int poolSize) {
        this.driverName = driverName;
        this.url = url;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;

        initPoolData();
    }

    private void initPoolData() {
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driverName);
            reservedConnections = new ArrayBlockingQueue<>(poolSize);
            freeConnections = new ArrayBlockingQueue<>(poolSize);

            for (int i = 0; i < poolSize; i++) {
                String longUrl = url + "?user=" + user + "&password=" + password;
                //Connection connection = DriverManager.getConnection(url, user, password);
                /*Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost/Courses?user=postgres&password=Axe2013"
                );*/
                Connection connection = DriverManager.getConnection(longUrl);
                PooledConnection pooledConnection =
                        PooledConnection.wrap(connection, freeConnections, reservedConnections);
                freeConnections.add(pooledConnection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQLException in ConnectionPool", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find database driver class", e);
        }
    }

    public Connection takeConnection() {
        if (closing) throw new RuntimeException("Connection pool is closing, connection can't be taken");

        Connection connection = null;
            try {
                connection = freeConnections.take();
                reservedConnections.add(connection);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error connecting to the data source.", e);
            }
        return connection;
    }

    public void close() {
        closing = true;
        for (Connection connection : freeConnections) {
            PooledConnection pooledConnection = (PooledConnection) connection;
            try {
                pooledConnection.reallyClose();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
