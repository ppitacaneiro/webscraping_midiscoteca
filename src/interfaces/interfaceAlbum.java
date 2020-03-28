/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import models.Album;

/**
 *
 * @author Pablo
 */
public interface interfaceAlbum {
    public boolean set(Album album);
    public boolean update(Album album);
    public boolean delete(Album album);
    public List<Album> get();
}
