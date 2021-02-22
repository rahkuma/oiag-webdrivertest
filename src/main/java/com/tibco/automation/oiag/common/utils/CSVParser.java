package com.tibco.automation.oiag.common.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class CSVParser {

    public static String read( File goldenFile, String toIgnore ) {
        String temp = "";
        CSVReader reader = null;
        try {
            reader = new CSVReader( new FileReader( goldenFile ) );
            String[] header = reader.readNext();
            int index = 0;

            if ( toIgnore != null && !toIgnore.isEmpty() ) {
                for ( int i = 0; i < header.length; i++ ) {
                	if ( header[i].equalsIgnoreCase( toIgnore ) ) {
                		index = i;
                    }
                }
            }

            for ( int i = 0; i < header.length; i++ ) {
                if ( i == index && toIgnore != null) {
                    continue;
                }
                temp += header[i] + " | ";
            }
            temp += "\n";

            String[] nextLine;
            while ( ( nextLine = reader.readNext() ) != null ) {
                for ( int i = 0; i < nextLine.length; i++ ) {
                    nextLine[i] = nextLine[i].equals( "" ) ? "-" : nextLine[i];
                    nextLine[i] = nextLine[i].replace( "||", "__" ).replace( "\\", "" ).replace("/", "");
                    if ( i == index && toIgnore != null) {
                        continue;
                    }
                    temp += nextLine[i].replace( "\n", "" ).replaceAll("\\s+", "").replace("Message:", "").trim() + "::";
                }
                temp += "\n";
            }

        }
        catch ( Exception e ) {
            e.printStackTrace();
            return "";
        }
        finally {
            if ( reader != null ) {
                try {
                    reader.close();
                }
                catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }
}
