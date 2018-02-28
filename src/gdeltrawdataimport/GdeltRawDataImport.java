/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Author: Hariharan Arunachalam, Sudeep Basnet
 * Date: May 31, 2016 (5:00:18 PM), Nov 11-2017
 * Explicit author permission required before this code is reused for any purpose - more like please let me know :)
 */
package gdeltrawdataimport;

import java.io.IOException;
import java.text.ParseException;
import org.joda.time.*;
import org.joda.time.format.*;
/**
 *
 * @author Hariharan, SudBasnet
 */
public class GdeltRawDataImport {
    private static int limitCount = 400;
    private static DateTimeFormatter ft_month = DateTimeFormat.forPattern("yyyyMM");
    private static DateTimeFormatter ft_day = DateTimeFormat.forPattern("yyyyMMdd");
    private static DateTimeFormatter ft_year = DateTimeFormat.forPattern("yyyy");
    public static DateTime currentDay;
    // give location of folder where the file is to be put: 
    private static String extractLocation = "/Users/sudbasnet/Documents/DataLoaderMySQL/dist/importLocation/";
    
    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ParseException, IOException {
        DateTime todaysDate = new DateTime();
        DateTime yesterdaysDate = todaysDate.minusDays(1);
        String yesterdaysDateString = yesterdaysDate.toString(ft_day);
        
        String startday = yesterdaysDateString; // must be in "yyyyMMdd" format
        String endday = yesterdaysDateString; // must be in "yyyyMMdd" format
        
        if (args.length > 1){
            startday = args[0];
            endday = args[1];
            if(args.length > 2){
                extractLocation = args[2];
            }
        }
        
        downloadData(startday, endday);
    }
    
    public static void downloadData(String startdate, String enddate) throws ParseException, IOException{
        String filename = null;
        String downloadedFile = null;
        String convertedFile = null;
        String filename_extension = null;
        DateTimeFormatter ft = ft_day;
        String update = "daily";
        DateTime startdate_d = ft.parseDateTime(startdate);
        DateTime enddate_d = ft.parseDateTime(enddate);   
        currentDay = startdate_d;
        
        while (currentDay.isBefore(enddate_d.plusDays(1))){
            if(currentDay.isBefore(ft_day.parseDateTime("20060101"))){
                ft = ft_year; 
                update = "yearly";
                filename_extension = "";
            } else if (currentDay.isAfter(ft_day.parseDateTime("20051231")) && currentDay.isBefore(ft_day.parseDateTime("20130401"))){
                ft = ft_month;
                update = "monthly";
                filename_extension = "";
            } else {
                ft = ft_day;
                update = "daily";
                filename_extension = ".export.CSV";
            }
            
            // create the file's name
            filename = currentDay.toString(ft);
            
            // download the file
            getFile(filename + filename_extension, extractLocation);
            downloadedFile = extractLocation + filename + filename_extension ;
            System.out.println("downloaded csv file: "+ downloadedFile);
            
            // convert the csv file
            convertedFile = ReadGDELT.readGdeltCsv(downloadedFile, extractLocation);
            ReadGDELT.deleteCSV(downloadedFile); // deleting the previous file to save space
            System.out.println("converted text file: "+ convertedFile);
            //next file's name
            switch (update) {
                case "monthly":
                    currentDay = currentDay.plusMonths(1);
                    break;
                case "yearly":
                    currentDay = currentDay.plusYears(1);
                    break;
                case "daily":
                    currentDay = currentDay.plusDays(1);
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void getFile(String csvName, String targetDir){
        String filename = csvName + ".zip";
        String downloadDir = targetDir; //the directory where downloaded files are kept  
        System.out.println("http://data.gdeltproject.org/events/"+filename);
        DownloadZip.downloadZIP("http://data.gdeltproject.org/events/"+filename, downloadDir+filename);
        // extract the ZIP file
        try {
            UnzipUtility.unzip(downloadDir+filename, downloadDir);
            UnzipUtility.deleteZIP(downloadDir+filename); // delete the downloaded zip file   
        } catch (IOException ex) {
            
        }
    }
}