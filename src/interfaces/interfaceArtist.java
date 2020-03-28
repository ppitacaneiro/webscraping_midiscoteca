/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import models.Artist;

/**
 *
 * @author Pablo
 */
public interface interfaceArtist {
    public boolean set(Artist artist);
    public boolean get();
    public boolean update(Artist artist);
    public boolean delete(Artist artist);
}
