/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import models.Song;
/**
 *
 * @author Pablo
 */
public interface interfaceSong {
    public boolean set(Song song, int idAlbum);
    public boolean get();
    public boolean update(Song song);
    public boolean delete(Song song);
}
