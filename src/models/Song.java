/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Pablo
 */
public class Song {
    private int id;
    private String title;
    private boolean active;

    public Song() {
    }

    public Song(int id, String title, boolean active) {
        this.id = id;
        this.title = title;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Song{" + "id=" + id + ", title=" + title + ", active=" + active + '}';
    }
    
    
}
