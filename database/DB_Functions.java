package com.tamik.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_Functions {

    // Database connection
//    public Connection connect_to_postgres(String database, String user, String pass){
//        Connection conn = null;
//        try {
//            Class.forName("org.postgresql.Driver");
//            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, user, pass);
//            if (conn != null){
//                System.out.println("Connection established");
//            }
//            else{
//                System.out.println("Connection failed");
//            }
//        }
//        catch (Exception e){
//            System.out.println(e);
//        }
//        return conn;
//    }

    // Creating Stored Procedures For Further Calling
    public void createTableFunction(Connection conn) throws SQLException {

        Statement stm = conn.createStatement();

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION CreateTable() \n" +
                    "RETURNS void \n" +
                    "AS \n" +
                    "$$ \n" +
                    "BEGIN \n" +
                    "EXECUTE 'CREATE TABLE IF NOT EXISTS students( \n" +
                    "id int PRIMARY KEY CHECK (id > 0), \n" +
                    "name varchar(100) NOT NULL, \n" +
                    "was_present boolean NOT NULL, \n" +
                    "st_group int CHECK (st_group > 0 AND st_group < 3), \n" +
                    "mark float \n" +
                    ")'; \n" +
                    "END; \n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION ViewTableContent()\n" +
                    "RETURNS TABLE (s_id INTEGER, s_name VARCHAR(100), s_was_present BOOLEAN, s_st_group INTEGER, s_mark FLOAT)\n" +
                    "AS \n" +
                    "$$\n" +
                    "BEGIN \n" +
                    "RETURN QUERY SELECT id, name, was_present, st_group, mark FROM students;\n" +
                    "END; \n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION TruncateTable() \n" +
                    "RETURNS void \n" +
                    "AS \n" +
                    "$$ \n" +
                    "BEGIN \n" +
                    "EXECUTE 'TRUNCATE students'; \n" +
                    "END; \n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION InsertData(\n" +
                    "id_value int,\n" +
                    "name_value varchar(100),\n" +
                    "was_present_flag bool,\n" +
                    "st_group_value int,\n" +
                    "mark_value float\n" +
                    ")\n" +
                    "RETURNS void \n" +
                    "AS\n" +
                    "$$\n" +
                    "BEGIN\n" +
                    "EXECUTE 'INSERT INTO students (id, name, was_present, st_group, mark) VALUES ($1, $2, $3, $4, $5)' \n" +
                    "USING id_value, name_value, was_present_flag, st_group_value, mark_value;\n" +
                    "END;\n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION UpdateTuple(id_value int, name_value varchar(100),\n" +
                    "was_present_flag bool, st_group_value int, mark_value float) \n" +
                    "RETURNS void \n" +
                    "AS \n" +
                    "$$ \n" +
                    "BEGIN \n" +
                    "UPDATE students SET name = name_value, \n" +
                    "was_present = was_present_flag, st_group = st_group_value, mark = mark_value \n" +
                    "WHERE id = id_value;\n" +
                    "END;\n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION SearchByName(name_value varchar(100))\n" +
                    "RETURNS TABLE (s_id INTEGER, s_name VARCHAR(100), s_was_present BOOLEAN, s_st_group INTEGER, s_mark FLOAT)\n" +
                    "AS \n" +
                    "$$\n" +
                    "BEGIN \n" +
                    "RETURN QUERY SELECT id, name, was_present, st_group, mark FROM students WHERE name = name_value;\n" +
                    "END; \n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };

        try {
            stm.executeUpdate("CREATE OR REPLACE FUNCTION DeleteByName(name_value varchar(100)) \n" +
                    "RETURNS void \n" +
                    "AS \n" +
                    "$$ \n" +
                    "BEGIN \n" +
                    "EXECUTE FORMAT('DELETE FROM students WHERE name = %L', name_value); \n" +
                    "END; \n" +
                    "$$ LANGUAGE plpgsql;");
        }
        catch (Exception e){
            System.out.println(e);
        };
    }
}
