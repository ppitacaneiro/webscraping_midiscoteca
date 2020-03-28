package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import webscrapping.SearchData;

public class SearchDataAlbum {
    
    private static final String FILENAME = "discoteca_test.xlsx";
    private static final String PATHFILE = "C:\\Users\\Pablo\\Documents\\DISCOTECA\\" + FILENAME;
    private static String artist;
    private static String disc;
    
    public static void main(String[] args) throws IOException {
        
        FileInputStream file = null;
        XSSFWorkbook book = null;
        
        try {
            file = new FileInputStream(new File(PATHFILE));
        } catch (FileNotFoundException ex) {
            ex.getMessage();
        }
        
        try {
            book = new XSSFWorkbook(file);
        } catch (IOException ex) {
            ex.getMessage();
        }
        
        XSSFSheet sheet = book.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;

            while (cellIterator.hasNext()) {
                cell = cellIterator.next();

                if (cell.getColumnIndex() == 0) {
                    artist = cell.toString();
                    System.out.println("Artista -> " + cell.toString());
                    
                } else if (cell.getColumnIndex() == 1) {
                    System.out.println("Disco -> " + cell.toString());
                    disc = cell.toString();
                    
                    SearchData search = new SearchData(artist,disc);
                }
            }
        }
    }
}
