package gr.wind.spectra.business;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ResourceBundle;

public class TnovaStaticDataSource
{
    private static String DATABASE_URL1;// = "jdbc:mysql://localhost:3306/SmartOutageDB?";
    private static String USERNAME1;// = "root";
    private static String PASSWORD1;// = "password";

    private static HikariConfig config = new HikariConfig();
    public static HikariDataSource ds;

    // Define a static logger variable so that it references the
    // Logger instance named "MyDataSource".
    private final Logger logger = LogManager.getLogger(TnovaStaticDataSource.class);
    static
    {
        Logger logger2 = LogManager.getLogger(TnovaStaticDataSource.class);

        // Resource is obtained from file:
        // /opt/glassfish5/glassfish/domains/domain1/lib/classes/database.properties
        DATABASE_URL1 = ResourceBundle.getBundle("nova_static_database").getString("DATABASE_URL");
        USERNAME1 = ResourceBundle.getBundle("nova_static_database_credentials").getString("USERNAME");
        PASSWORD1 = ResourceBundle.getBundle("nova_static_database_credentials").getString("PASSWORD");

        config.setJdbcUrl(DATABASE_URL1);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(USERNAME1);
        config.setPassword(PASSWORD1);

        // Connection Pool Settings
        config.setMaximumPoolSize(18);        // Keep as is if the workload is stable
        config.setMinimumIdle(5);             // Maintain a few idle connections
        config.setIdleTimeout(300000);        // 5 min before idle connections are removed
        config.setMaxLifetime(1800000);       // Max connection age: 30 min (prevents stale connections)

        // Connection Acquisition
        config.setConnectionTimeout(30000); // Wait 30 seconds for a free connection
        config.setValidationTimeout(5000);  // Allow 5 seconds for connection validation

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "700");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        try {
            ds = new HikariDataSource(config);
        }catch (Exception e){
            logger2.fatal("Cannot start Hikari Static DataSource for Nova" + e.getMessage());
            e.printStackTrace();
        }
    }

    public TnovaStaticDataSource()
    {
    }
}