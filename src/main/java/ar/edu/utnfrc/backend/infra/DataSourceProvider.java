package ar.edu.utnfrc.backend.infra;

import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;

public class DataSourceProvider {
    
    private static final String JDBC_URL = "jdbc:h2:mem:database;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    
    private static DataSource dataSource;
    
    public static DataSource getDataSource() {
        if (dataSource == null) {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(JDBC_URL);
            ds.setUser(USER);
            ds.setPassword(PASSWORD);
            dataSource = ds;
        }
        return dataSource;
    }
}