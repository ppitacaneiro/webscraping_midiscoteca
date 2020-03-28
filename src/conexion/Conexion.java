/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Pablo
 */
public class Conexion {
    
    private static final String PASSWORD = "";
    private static final String USER = "root";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "midiscoteca";
    
    public static Connection conectar() {
        
        Connection connection = null;
        
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
        
        try {
            connection = (Connection) DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return connection;
    }
    
}
