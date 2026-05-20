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
public class model {
    
    
    Connection conn = new ConnectionDB().ConnectionDb();
    
    public String registerUser(String email , String pass){
        String query = "Insert into users(email , password) value(? , ?)";
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,email);
            ps.setString(2, pass);
            ps.execute();
            ps.close();
            return "sucess";
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        
    }
    
    public User loginUser(String email , String pass){
       
        try{
           String query = "SELECT * FROM users WHERE email = ? AND password = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                 
                User user = new User();
                 user.setId(rs.getInt("id"));
//                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                
                return user;

         
            } else {
                System.out.println("Invalid Credentials");
                 return null;
            }
            
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            return  null ;
        }
        
    }
    
public boolean approveUser(String id) {

    try {

        String query =
                "UPDATE users SET status = ? WHERE id = ?";

        PreparedStatement ps =
                conn.prepareStatement(query);

        ps.setString(1, "approved");

        ps.setInt(2, Integer.parseInt(id));

        int rows = ps.executeUpdate();

        return rows > 0;

    } catch (Exception e) {

        System.out.println(e.getMessage());

        return false;
    }
}
    public boolean RejectUser(String id) {

    try {

        String query =
                "UPDATE users SET status = ? WHERE id = ?";

        PreparedStatement ps =
                conn.prepareStatement(query);

        ps.setString(1, "reject");

        ps.setInt(2, Integer.parseInt(id));

        int rows = ps.executeUpdate();

        return rows > 0;

    } catch (Exception e) {

        System.out.println(e.getMessage());

        return false;
    }
}
    
      public ResultSet getUser(){
        try {
            String query = "select* from users where status = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1 , "pending");
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            return rs;
        
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        
    
    
}}
