/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ats;

/**
 *
 * @author Mohit kumar
 */
import java.sql.*;
public class ConnectionDB {
    final private  String connectionUrl = "jdbc:mysql://localhost:3306/Ats";
   final private String Db = "root";
    final private String password = "5522";
    
    public Connection ConnectionDb(){
        try{
           Connection con =  DriverManager.getConnection(connectionUrl , Db , password);
           System.out.println("connection successful");
            return con; 
        }catch(SQLException e){
           System.out.println(e.getMessage());
           return null;
        }
        
    }
}
