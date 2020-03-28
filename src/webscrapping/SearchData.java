/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscrapping;

import dao.AlbumDAO;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import models.Album;
import models.Genre;
import models.Song;
import models.Style;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Pablo
 */
public class SearchData {
    
    private final static String URL_DISCOGS = "https://www.discogs.com/es/";
    private final static String PATH_SAVEIMAGES = "C:\\Users\\Pablo\\Documents\\DISCOTECA\\images\\";
    private String artist;
    private String disc;
    
    public SearchData(String artist,String disc) throws IOException {
        
        this.artist = artist;
        this.disc = disc;
      
        Album album = new Album(); 
        AlbumDAO albumDao = new AlbumDAO();
        
        String searchTermArtista = "";
        String linkPageAlbum = "";
        
        try {
            searchTermArtista = createSearchTermArtist(disc);
            String url = URL_DISCOGS + "search/?q=" + searchTermArtista + "&type=all";

            linkPageAlbum = this.createLinkPageDisc(disc, artist);

            System.out.println("Searching..." + url);
            System.out.println("linkPageAlbum => " + linkPageAlbum);

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
               System.out.println(link.attr("abs:href"));
               if (link.attr("abs:href").toLowerCase().contains(linkPageAlbum.toLowerCase())) {
                   album = this.searchDataAlbum(link.attr("abs:href"));
                   
                   break;
               }
            }
            System.out.println(album.toString());
            
            if (album.getTitle() != null && album.getArtist() != null) {
                if (albumDao.set(album)) {
                    System.out.println("ALBUM GUARDADO");
                } else {
                    System.out.println("ERROR al insertar datos del album " + album.getTitle() + "en la BBDD");
                }
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    
    private String createSearchTermArtist(String disco) {
        String searchString = "";
        String[] partsDisc = disco.split(" ");
        
        for (String partsDisc1 : partsDisc) {
            searchString += partsDisc1 + "+";
        }
        searchString = this.removeLastChar(searchString);
        
        return searchString;
    }
    
    private String createLinkPageDisc(String disco,String artista) {
        String linkPageDisc = URL_DISCOGS;
        linkPageDisc += this.createNameDiscArtist(disco, artista);
     
        return linkPageDisc;
    }
    
    private String createNameDiscArtist(String disco,String artista) {
        String nameDiscArtist = "";
        String [] artistStrParts = artista.split(" ");
        String [] discStrParts = disco.split(" ");
        
        for (String artistStrPart : artistStrParts) {
            nameDiscArtist += artistStrPart + "-";
        }
        for (String discStrPart : discStrParts) {
            nameDiscArtist += discStrPart + "-";
        }
        
        nameDiscArtist = this.removeLastChar(nameDiscArtist);
                
        return nameDiscArtist;
    }
    
    private Album searchDataAlbum(String link) throws IOException {
        
        Album album = new Album();
        
        album.setArtist(this.artist.toLowerCase());
        album.setTitle(this.disc.toLowerCase());
        
        ArrayList<Song> songsArrayList = new ArrayList();
        ArrayList<Style> styleArrayList = new ArrayList();
        ArrayList<Genre> genreArrayList = new ArrayList();
        
        Document doc = Jsoup.connect(link).get();
        
        Elements divImage = doc.getElementsByClass("image_gallery image_gallery_large");
        Elements infoAlbum = doc.getElementsByClass("content");
        Elements songsAlbum = doc.select("span.tracklist_track_title");
        Elements tracklistDuration = doc.select("td.tracklist_track_duration > span");
        
        try {
            songsAlbum.forEach((song) -> {
                Song s = new Song();
                String songTitle = this.escapeQuotes(song.text());
                s.setTitle(songTitle);
                songsArrayList.add(s);
            });
            
            infoAlbum.stream().map((info) -> info.getElementsByTag("a")).forEachOrdered((Elements infoLinks) -> {
                infoLinks.forEach((infoLink) -> {
                    String linkHref = infoLink.attr("href");
                    String linkText = infoLink.text();
                    if (linkHref.contains("genre")) {
                        Genre genre = new Genre();
                        genre.setName(linkText);
                        genreArrayList.add(genre);
                    }
                    if (linkHref.contains("style")) {
                        Style style = new Style();
                        style.setName(linkText);
                        styleArrayList.add(style);
                    }
                    if (linkHref.contains("year")) {
                        if (this.isFormatYearOk(linkText)) {
                            int year = Integer.parseInt(linkText);
                            album.setYear(year);
                        }
                    }
                });
            });
            
            for (Element div : divImage) {
               Elements imgs = div.getElementsByTag("img");
               if(this.saveImage(imgs.attr("abs:src"))) {
                  String fileName = this.createNameDiscArtist(this.disc, this.artist).toLowerCase();
                  album.setImageCover(fileName);
               }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        album.setSongs(songsArrayList);
        album.setGenres(genreArrayList);
        album.setStyles(styleArrayList);
        
        return album;
    }
    
    private boolean isFormatYearOk(String date) {
        boolean isYearValid = false;
        if (date.length() == 4 && this.isNumeric(date)) {
            isYearValid = true;
        }
        
        return isYearValid;
    }
    
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private String escapeQuotes(String stringQuotes) {
        String stringSanitized;
        
        stringSanitized = stringQuotes.replaceAll("'", "''");
        
        return stringSanitized;
    }
    
    private boolean saveImage(String url) throws MalformedURLException, IOException, AccessDeniedException {
        String fileName = this.createNameDiscArtist(this.disc, this.artist).toLowerCase();
        boolean isFileSavedSuccess = false;
        
        try(InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(PATH_SAVEIMAGES + fileName + ".jpg"));
            isFileSavedSuccess = true;
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        } 
        
        return isFileSavedSuccess;
    }
}
