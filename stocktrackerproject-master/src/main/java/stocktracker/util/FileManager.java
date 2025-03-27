package stocktracker.util;

import stocktracker.model.*;
import stocktracker.service.StockMarket;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for handling file I/O operations
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */

public class FileManager {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");



    /**
     * Saves user data to a text file
     *
     * @param user The user to save
     * @param filename The filename to save to
     * @throws IOException If there's an error writing to the file
     */
    public static void saveToFile(User user, String filename) throws IOException{

    }
}
