package com.api.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.sun.istack.logging.Logger;

public enum SectorDAO {
    instance;

    private Connection con = null;

    private SectorDAO() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsql://localhost/dsdb", "SA", "");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SectorDAO.class.getName(), null).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Sector>fillDropdown(){
    	Sector s1 = new Sector();
    	List<Sector>arr = new ArrayList<Sector>();
    	try {
			PreparedStatement pstmt = con.prepareStatement("SELECT * from sector");
			ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					int ID = rs.getInt("id");
					String BUILDING = rs.getString("building");
					s1 = new Sector(ID, BUILDING);
					arr.add(s1);
				}
			}catch(Exception e) {
				e.printStackTrace();
		}
		return arr;
	}
}