package ar.edu.utnfrc.backend.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DbInitializer {
    
    public static void initialize(javax.sql.DataSource dataSource) throws Exception {
        String ddlScript = readDdlScript();
        executeDdl(dataSource, ddlScript);
    }
    
    private static String readDdlScript() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(DbInitializer.class.getResourceAsStream(
                        "/sql/database-ddl.sql")))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    
    private static void executeDdl(javax.sql.DataSource dataSource, String ddlScript) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Dividir por punto y coma y ejecutar cada sentencia
            String[] statements = ddlScript.split(";");
            for (String sql : statements) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            System.out.println("[OK] DDL ejecutado correctamente.");
        }
    }
}