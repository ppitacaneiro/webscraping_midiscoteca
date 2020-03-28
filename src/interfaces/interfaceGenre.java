/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import models.Genre;

/**
 *
 * @author Pablo
 */
public interface interfaceGenre {
    public boolean set(Genre genre);
    public boolean get();
    public boolean update(Genre genre);
    public boolean delete(Genre genre);
}
