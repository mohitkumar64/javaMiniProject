/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ats;

import java.sql.*;

/**
 *
 * @author Mohit kumar
 */
public class ATS {

    public static void main(String[] args) {
        ConnectionDB conc = new ConnectionDB();
        try {
            Connection con = conc.ConnectionDb();
            app frame = new app();
            frame.showRegister();

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }

    }
}
