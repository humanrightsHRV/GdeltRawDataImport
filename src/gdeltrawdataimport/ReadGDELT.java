/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdeltrawdataimport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author sudbasnet
 */
public class ReadGDELT {
    public static String readGdeltCsv(String readDir, String writeDir) throws FileNotFoundException, IOException {
        String sourceUrl = null;
        String returnFilePath = null;
        BufferedReader br = null;				
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter out = null;
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyyMMdd");
        try {
            String sCurrentLine;
            String eventCat = "";
            File file1 = new File(readDir);
            String name = file1.getName();
            int filenameLength = name.length();
            // only read .CSV file
            if (".CSV".equals(name.substring(filenameLength-4).toUpperCase())){
                br = new BufferedReader(new FileReader(file1));
                // create the output filename
                char[] chr = name.toCharArray();
                StringBuffer n = new StringBuffer();
                for(int c = 0; c < chr.length; c++){
                    if(chr[c] != '.'){
                        n.append(Character.toString(chr[c]));
                    } else
                        break;
                }
                // put the filename into the write directory
                returnFilePath = writeDir + n + ".txt";
                fw = new FileWriter(returnFilePath, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                
                
                System.out.println("Output directory: " + returnFilePath);
                
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] attr = sCurrentLine.replaceAll("\t\"",",").replaceAll("\"\t",",").replaceAll("\"",",").split("\t");
                    // choose only INDIA, PAKISTAN and BANGLADESH
                    eventCat = eventCategory(attr[26]);
                    if( (attr[51].equals("IN")) && !eventCat.equals("Unknown")){
                        if (GdeltRawDataImport.currentDay.isBefore(ft.parseDateTime("20130401"))){
                            // there is no sourceURL in data before 20130401
                            sourceUrl = "no url";
                        } else {
                            sourceUrl = attr[57];
                        }
                            out.write(attr[0]  + "\t" +  //Uniqueid
                                attr[1].substring(6, 8)  + "\t" + //Day
                                attr[1].substring(4, 6)  + "\t" + //Month
                                attr[3]  + "\t" + //Year
                                attr[26] + "\t" + //EventCode
                                attr[34] + "\t" + //AvgTone
                                attr[51] + "\t" + //CountryCode
                                attr[49] + "\t" + //Type
                                attr[50] + "\t" + //Fullname
                                attr[53] + "\t" + //Lat
                                attr[54] + "\t" + //Lon
                                sourceUrl + "\t" + //url
                                eventCat + "\n") ; //converted eventCategory
                    }
                } //end while
                out.close();
            }//end if CSV 
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
                if(bw != null) bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return returnFilePath;
    }
    
    public static void deleteCSV(String csvDir){
        File f = new File(csvDir);
        f.delete();
    }
    
    public static String eventCategory(String x){
        String ret = "Unknown";
        if(x.equals("020") || x.equals("021") ||x.equals("0212")||
        x.equals("0213")|| x.equals("0214")||x.equals("022")||
        x.equals("023")||x.equals("0231")||x.equals("0232")||x.equals("0233")||
        x.equals("0234")||x.equals("024")||x.equals("0241")||x.equals("0242")||
        x.equals("0243")||x.equals("0244")||x.equals("025")||x.equals("0251")||
        x.equals("0252")||x.equals("0253")||x.equals("0254")||x.equals("0255")||
        x.equals("0256")||x.equals("026")||x.equals("027")||x.equals("028")){
            ret = "Appeal";
        }
        //System.out.println("HERE! 3 pointer" );
        else if(x.equals("100") ||x.equals("101") ||x.equals("1011") ||x.equals("1012") ||x.equals("1013") ||
        x.equals("1014") ||x.equals("102") ||x.equals("103") ||x.equals("1031") ||x.equals("1032") ||
        x.equals("1033") ||x.equals("1034") ||x.equals("104") ||x.equals("1041") ||x.equals("1042") ||
        x.equals("1043") ||x.equals("1044") ||x.equals("105") ||x.equals("1051") ||
        x.equals("1052") ||x.equals("1052") ||x.equals("1053") ||x.equals("1054")||
        x.equals("1055") ||x.equals("1056") ||x.equals("106") ||x.equals("107") ||x.equals("108") ){
            ret = "Demand";
        }
        else if(x.equals("130") ||x.equals("131") ||x.equals("1311") ||x.equals("1312") ||x.equals("1313") ||
        x.equals("132") ||x.equals("1321") ||x.equals("1322") ||x.equals("1323") ||x.equals("1324") ||
        x.equals("133") ||x.equals("134") ||x.equals("135") ||x.equals("136") ||x.equals("137") ||
        x.equals("138") ||x.equals("1381") ||x.equals("1382") ||x.equals("1383") ||
        x.equals("1384") ||x.equals("1385") ||x.equals("139")){
            ret = "Threaten";
        }
        else if(x.equals("140") ||x.equals("141") ||x.equals("1411") ||x.equals("1412") ||x.equals("1413") ||
        x.equals("1414") ||x.equals("142") ||x.equals("1421") ||x.equals("1422") ||x.equals("1423") ||
        x.equals("1424") ||x.equals("143") ||x.equals("1431") ||x.equals("1432") ||x.equals("1433") ||
        x.equals("1434") ||x.equals("144") ||x.equals("1441") ||x.equals("1442") ||
        x.equals("1443") ||x.equals("1444") ||x.equals("145") ||x.equals("1451")||
        x.equals("1452") ||x.equals("1453") ||x.equals("1454") ){
            ret = "Protest";
        }
        else if(x.equals("170") ||x.equals("171") ||x.equals("1711") ||x.equals("1712")||
        x.equals("172") ||x.equals("1721") ||x.equals("1722") ||x.equals("1723") ||
        x.equals("1724") ||x.equals("173") ||x.equals("173") ||x.equals("175") ||
        x.equals("176") ){
            ret = "Coerce";
        }
        else if(x.equals("180") ||x.equals("181") ||x.equals("182") ||x.equals("1821")||
        x.equals("1822") ||x.equals("1823") ||x.equals("183") ||x.equals("1831") ||
        x.equals("1832") ||x.equals("1833") ||x.equals("1834") ||x.equals("184") ||
        x.equals("185") ||x.equals("186")){
            ret = "Assault";
        }
        else if(x.equals("190") ||x.equals("191") ||x.equals("192") ||x.equals("193")||
        x.equals("194") ||x.equals("195") ||x.equals("1951") ||x.equals("1952") ||
        x.equals("196") ){
            ret = "Fight";
        }
        else if(x.equals("200") ||x.equals("201") ||x.equals("202") ||x.equals("203")||
        x.equals("204") ||x.equals("2041") ||x.equals("2042") ){
            ret = "Engage";
        }
        return ret;
    }
}