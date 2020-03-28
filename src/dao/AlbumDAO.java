/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import interfaces.interfaceAlbum;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Album;
import models.Artist;
import models.Genre;
import models.Song;
import models.Style;

/**
 *
 * @author Pablo
 */
public class AlbumDAO implements interfaceAlbum {

    private Artist artist;
    private ArtistDAO artistDao;
    private SongDAO songDao;
    private StyleDAO styleDao;
    private GenreDAO genreDao;

    public AlbumDAO() {
        artistDao = new ArtistDAO();
        songDao = new SongDAO();
        styleDao = new StyleDAO();
        genreDao = new GenreDAO();
    }

    @Override
    public boolean set(Album album) {
        Connection connection = null;
        Statement statement = null;
        boolean isSet = false;
        int idArtist = 0;
        String sql;

        artist = artistDao.getByName(album.getArtist());
        if (artist == null) {
            artist = new Artist();
            artist.setName(album.getArtist());
            boolean isRegistered = artistDao.set(artist);
            if (isRegistered) {
                idArtist = artist.getId();
            }
        } else {
            idArtist = artist.getId();
        }

        sql = "INSERT INTO albums (id,id_artist,title,image_cover,year,active) "
                + "VALUES (NULL,'" + idArtist + "','" + album.getTitle() + "','" + album.getImageCover() + "'," + album.getYear() + ",1)";

        try {
            connection = Conexion.conectar();
            statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                throw new SQLException("Fallo al insertar album");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    album.setId(generatedKeys.getInt(1));
                    this.setSongsOfAlbum(album);
                    this.setStylesOfAlbum(album);
                    this.setGenresOfAlbum(album);
                    isSet = true;
                } else {
                    throw new SQLException("Fallo al insertar album, no se obtuvo el ID generado.");
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(AlbumDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isSet;
    }

    @Override
    public boolean update(Album album) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(Album album) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Album> get() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resulset = null;

        List<Album> listAlbums = new ArrayList<>();

        String sql = "SELECT a.id,ar.name,a.title,a.image_cover,a.year,a.active\n"
                + "FROM albums AS a\n"
                + "INNER JOIN artists AS ar\n"
                + "ON a.id_artist = ar.id\n"
                + "WHERE \n"
                + "(\n"
                + "   a.active = 1\n"
                + "   AND ar.active = 1\n"
                + ")";

        connection = Conexion.conectar();
        if (connection != null) {
            try {
                statement = connection.createStatement();
                resulset = statement.executeQuery(sql);

                while (resulset.next()) {
                    Album album = new Album();
                    album.setId(resulset.getInt(1));
                    album.setArtist(resulset.getString(2));
                    album.setTitle(resulset.getString(3));
                    album.setImageCover(resulset.getString(4));
                    album.setYear(resulset.getInt(5));
                    boolean active = (resulset.getInt(6) != 0) ? true : false;
                    album.setActive(active);
                    listAlbums.add(album);
                }
                statement.close();
                resulset.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(AlbumDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listAlbums;
    }

    public boolean setSongsOfAlbum(Album album) {
        boolean isSet = false;
        ArrayList<Song> songs = new ArrayList<>();
        songs = album.getSongs();
        int idAlbum = album.getId();

        for (Song song : songs) {
            isSet = songDao.set(song, idAlbum);
        }

        return isSet;
    }

    public boolean setStylesOfAlbum(Album album) {
        boolean isSet = false;
        ArrayList<Style> styles = new ArrayList<>();
        styles = album.getStyles();
        int idAlbum = album.getId();
        int idStyle = 0;

        for (Style style : styles) {
            if (styleDao.getStyleByName(style.getName()) == null) { 
                styleDao.set(style);
            }

            idStyle = style.getId();
            if(this.setRelationBetweenAlbumAndStyles(idAlbum, idStyle)) {
                isSet = true;
            }
        }

        return isSet;
    }
    
    public boolean setGenresOfAlbum(Album album) {
        boolean isSet = false;
        ArrayList<Genre> genres = new ArrayList<>();
        genres = album.getGenres();
        int idAlbum = album.getId();
        int idGenre = 0;

        for (Genre genre : genres) {
            if (genreDao.getGenreByName(genre.getName()) == null) { 
                genreDao.set(genre);
            }

            idGenre = genre.getId();
            if(this.setRelationBetweenAlbumAndGenres(idAlbum, idGenre)) {
                isSet = true;
            }
        }

        return isSet;
    }

    public boolean setRelationBetweenAlbumAndStyles(int idAlbum, int idStyle) {
        Connection connection = null;
        Statement statement = null;
        boolean isSet = true;
        int idArtist = 0;
        String sql;
        Style style;

        sql = "INSERT INTO album_styles (id,id_album,id_style,active) VALUES (NULL," + idAlbum + "," + idStyle + ",1)";

        try {
            connection = Conexion.conectar();
            statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                isSet = false;
                throw new SQLException("Fallo al insertar relación entre estilo y album");
            }

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(AlbumDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isSet;
    }
    
    public boolean setRelationBetweenAlbumAndGenres(int idAlbum, int idGenre) {
        Connection connection = null;
        Statement statement = null;
        boolean isSet = true;
        int idArtist = 0;
        String sql;
        Genre genres;

        sql = "INSERT INTO album_genres (id,id_album,id_genre,active) VALUES (NULL," + idAlbum + "," + idGenre + ",1)";

        try {
            connection = Conexion.conectar();
            statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                isSet = false;
                throw new SQLException("Fallo al insertar relación entre género y album");
            }

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(AlbumDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isSet;
    }
}
