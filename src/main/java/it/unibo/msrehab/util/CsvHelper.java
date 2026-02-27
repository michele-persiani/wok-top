/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author floriano
 */
public class CsvHelper {
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CsvHelper.class);
    
    private static final String soCommand =
            "sqlite3 -header -csv %s ";
            
    private static final String sqlCommand =
            "select * from %s where %s;";

    //private static final String sqlCommand1
    //        = "select * from %s where msrrole='PATIENT';";

    
    public static int export(String dbPath, String csvsPath, String table, String condition) {
        
        int errCode=-1;
        
        try {
            String sql;
            //if("msruser".equals(table)) {
                sql = String.format(sqlCommand, table, condition);
            //}
            //else if("hystory".equals(table)) {
            //    sql = String.format(sqlCommand2, table);
            //}
            //else {
            //    sql = String.format(sqlCommand, table);
            //}
            
            String[] soCommands = String.format(soCommand, dbPath).split(" ");
            
            List<String> cmdList = new ArrayList<String>(Arrays.asList(soCommands));
            cmdList.add(sql);
            
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(cmdList);
            Process process = pb.start();
            if((errCode = process.waitFor())==0) {
                PrintWriter outWriter = new PrintWriter(new FileWriter(csvsPath+File.separator+table+".csv"));
                outWriter.println(output(process.getInputStream()));
                outWriter.close();
            }
            else {
                logger.error("SQL error while exporting table");
            }
            
        } catch (IOException ex) {
            logger.error("IOException while exporting table: " + ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error("InterruptedException while exporting table: " + ex.getMessage());            
        }
        
        return errCode;

    }
 
    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
            
            
//            String exportCommand = "/usr/bin/sqlite3 -header -csv /Users/floriano/NetBeansProjects/MS-rehabilitation/MS-rehab/MS-rehab.db \"select * from clinicalprofile;\"";
//            CommandLine cmdLine = new CommandLine("sqlite3");
//            //cmdLine.addArgument("-header");
//            //cmdLine.addArgument("-csv");
//            cmdLine.addArgument("/Users/floriano/NetBeansProjects/MS-rehabilitation/MS-rehab/MS-rehab.db");
//            cmdLine.addArgument("'select * from clinicalprofile;'");
//            
//            DefaultExecutor executor = new DefaultExecutor();
//            //executor.setExitValue(1);
//            ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
//            executor.setWatchdog(watchdog);
//            int exitValue = executor.execute(cmdLine);
//            
//            //String homeDirectory = System.getProperty("user.home");
////            String dir = System.getProperty("user.dir");
////            String u = System.getProperty("user.name");
////            Process pr = Runtime.getRuntime().exec(exportCommand);
////            int exitCode = pr.waitFor();
////            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
////            while ((line=buf.readLine())!=null) {
////                System.out.println(line);
////            }
//            return exitValue;
//        } catch (IOException ex) {
//            logger.error("...export failed" + ex.getMessage());
//        }        
//        return -1;
//    }
}