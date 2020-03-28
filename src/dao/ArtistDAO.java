/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.Conexion;
import interfaces.interfaceArtist;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Artist;

/**
 *
 * @author Pablo
 */
public class ArtistDAO implements interfaceArtist {

    @Override
    public boolean set(Artist artist) {
        boolean isSet = false;
        
        Connection connection = null;
        Statement statement = null;
        
        String sql = "INSERT INTO artists (id,name,active) VALUES (NULL,'" + artist.getName() + "',1)";
        
        try {
           connection = Conexion.conectar();
           statement = connection.createStatement();
           int affectedRows = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
           if (affectedRows == 0) {
               throw new SQLException("Fallo al insertar artista");
           } 
           
           try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                   artist.setId(generatedKeys.getInt(1));
                   isSet = true;
               } else {
                    throw new SQLException("Fallo al insertar artista, no se obtuvo el ID generado.");
               }
           }
           
           statement.close();
           connection.close();
        } catch (SQLException ex) {
           Logger.getLogger(ArtistDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return isSet;
    }

    @Override
    public boolean get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Artist artist) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(Artist artist) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Artist getByName(String name) {
        Artist artist = null;
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resulset = null;
        
        String sql = "SELECT * FROM artists WHERE name = '" + name + "'";
        
        connection = Conexion.conectar();
        if (connection != null) {
            try {
                statement = connection.createStatement();
                resulset = statement.executeQuery(sql);
                if (resulset.first()) {
                    artist = new Artist();
                    artist.setId(resulset.getInt(1));
                    artist.setName(resulset.getString(2));
                    boolean active = (resulset.getInt(3) != 0) ? true : false;
                    artist.setActive(active);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ArtistDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return artist;
    }
    
}
