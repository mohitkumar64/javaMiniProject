/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ats;

/**
 *
 * @author Mohit kumar
 */
import org.apache.tika.Tika;
import java.io.File;
public class pdfParser {
    
    public String ParsePdf(File file){
        try {

            Tika tika = new Tika();

            String text = tika.parseToString(file);
            return text;
//            System.out.println(text);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        
    }
}
