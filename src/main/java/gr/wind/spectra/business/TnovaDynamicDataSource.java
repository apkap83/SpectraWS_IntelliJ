package gr.wind.spectra.business;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ResourceBundle;

public class TnovaDynamicDataSource
{
    private static String DATABASE_URL1;// = "jdbc:mysql://localhost:3306/SmartOutageDB?";
    private static String USERNAME1;// = "root";
    private static String PASSWORD1;// = "password";

    private static String DATABASE_URL2;
    private static String USERNAME2;
    private static String PASSWORD2;
    private static HikariConfig config = new HikariConfig();
    public static HikariDataSource ds;

    // Define a static logger variable so that it references the
    // Logger instance named "MyDataSource".
    private final Logger logger = LogManager.getLogger(TnovaDynamicDataSource.class);

    static
    {
        Logger logger2 = LogManager.getLogger(TnovaStaticDataSource.class);

        // Resource is obtained from file:
        // /opt/glassfish5/glassfish/domains/domain1/lib/classes/...

        DATABASE_URL1 = ResourceBundle.getBundle("nova_dynamic_database").getString("DATABASE_URL");
        USERNAME1 = ResourceBundle.getBundle("nova_dynamic_database_credentials").getString("USERNAME");
        PASSWORD1 = ResourceBundle.getBundle("nova_dynamic_database_credentials").getString("PASSWORD");

        config.setJdbcUrl(DATABASE_URL1);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(USERNAME1);
        config.setPassword(PASSWORD1);
//        config.setMaxLifetime(1800000);
//        config.setConnectionTimeout(30000);
//        config.setValidationTimeout(5000);
//        config.setMaximumPoolSize(10);

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
            logger2.fatal("Cannot start Hikari Dynamic DataSource for Nova!");
        }
    }

    public static void getUpdatedResourceData()
    {
        Logger logger2 = LogManager.getLogger(TnovaDynamicDataSource.class);

        // Resource is obtained from file:
        // /opt/glassfish5/glassfish/domains/domain1/lib/classes/...
        ds.close();
        ResourceBundle.clearCache();
        DATABASE_URL2 = ResourceBundle.getBundle("nova_dynamic_database").getString("DATABASE_URL");
        USERNAME2 = ResourceBundle.getBundle("nova_dynamic_database_credentials").getString("USERNAME");
        PASSWORD2 = ResourceBundle.getBundle("nova_dynamic_database_credentials").getString("PASSWORD");

        config.setJdbcUrl(DATABASE_URL2);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(USERNAME2);
        config.setPassword(PASSWORD2);
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(260);
        config.setValidationTimeout(250);
        config.setMaximumPoolSize(14);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "700");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        try {
            logger2.info("**** NEW DYNAMIC NOVA DATABASE ****");
            System.out.println("**** New NOVA DATABASE_URL = " + DATABASE_URL2);
            ds = new HikariDataSource(config);
        }catch (Exception e){
            logger2.fatal("Cannot connect to Hikari Dynamic DataSource for Nova!!" + e.getMessage());
            e.printStackTrace();
        }

    }
    public TnovaDynamicDataSource()
    {
    }

}