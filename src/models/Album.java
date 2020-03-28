/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author Pablo
 */
public class Album {
    
    private int id;
    private String artist;
    private String title;
    private String imageCover;
    private int year;
    private boolean active;
    private ArrayList<Style> styles = new ArrayList<>();
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Genre> genres = new ArrayList<>();

    public Album() {
    }

    public Album(int id, String artist, String title, String imageCover, int year, boolean active) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.imageCover = imageCover;
        this.year = year;
        this.active = active;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }    

    public ArrayList<Style> getStyles() {
        return styles;
    }

    public void setStyles(ArrayList<Style> styles) {
        this.styles = styles;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Album{" + "id=" + id + ", artist=" + artist + ", title=" + title + ", imageCover=" + imageCover + ", year=" + year + ", active=" + active + ", styles=" + styles + ", songs=" + songs + ", genres=" + genres + '}';
    }

    
}
