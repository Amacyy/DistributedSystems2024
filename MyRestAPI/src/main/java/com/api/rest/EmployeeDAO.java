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

public enum EmployeeDAO {
	instance;
	
	private Connection con = null;
	
	private EmployeeDAO() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			con = DriverManager.getConnection("jdbc:hsql://localhost/oneDB","SA","");
		}catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}catch(SQLException ex) {
			Logger.getLogger(EmployeeDAO.class.getName(), null).log(Level.SEVERE, null, ex);
		}
	}
	
	public List<Employee>getEmployee(int ID){
		Employee e1 = new Employee();
		List<Employee>arr = new ArrayList<Employee>();
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT e.id, e.name, e.age, e.sector, s.building FROM employees e JOIN sector s on e.sector = s.id where e.sector = s.id and e.id="
					+ID);
			ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					String NAME = rs.getString("name");
					int AGE = rs.getInt("age");
					int SECTOR = rs.getInt("sector");
					String BUILDING = rs.getString("building");
					e1 = new Employee(ID, NAME, AGE, SECTOR, BUILDING);
					arr.add(e1);
				}
			}catch(Exception e) {
				e.printStackTrace();
		}
		return arr;
	}
	
	 List<Employee> getEmployeeByName(String name) {
	        List<Employee> arr = new ArrayList<Employee>();
	        try {
	            PreparedStatement pstmt = con.prepareStatement(
	                    "SELECT e.id, e.name, e.age, e.sector, s.building FROM employees e JOIN sector s on e.sector = s.id where e.name=?");
	            pstmt.setString(1, name);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                int ID = rs.getInt("id");
	                int AGE = rs.getInt("age");
	                int SECTOR = rs.getInt("sector");
	                String BUILDING = rs.getString("building");
	                Employee employee = new Employee(ID, name, AGE, SECTOR, BUILDING);
	                arr.add(employee);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return arr;
	    }
	
	List<Employee> getEmployeeByAge(int age) {
        List<Employee> arr = new ArrayList<Employee>();
        try {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT e.id, e.name, e.age, e.sector, s.building FROM employees e JOIN sector s on e.sector = s.id where e.age=?");
            pstmt.setInt(1, age);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("id");
                String NAME = rs.getString("name");
                int SECTOR = rs.getInt("sector");
                String BUILDING = rs.getString("building");
                Employee employee = new Employee(ID, NAME, age, SECTOR, BUILDING);
                arr.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    List<Employee> getEmployeeBySector(int sectorId) {
        List<Employee> arr = new ArrayList<Employee>();
        try {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT e.id, e.name, e.age, e.sector, s.building FROM employees e JOIN sector s on e.sector = s.id where e.sector=?");
            pstmt.setInt(1, sectorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("id");
                String NAME = rs.getString("name");
                int AGE = rs.getInt("age");
                String BUILDING = rs.getString("building");
                Employee employee = new Employee(ID, NAME, AGE, sectorId, BUILDING);
                arr.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }
    
    List<Employee> getEmployeeByBuilding(String building) {
        List<Employee> arr = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT e.id, e.name, e.age, e.sector, s.building FROM employees e JOIN sector s on e.sector = s.id where s.building=?");
            pstmt.setString(1, building);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("id");
                String NAME = rs.getString("name");
                int AGE = rs.getInt("age");
                int SECTOR = rs.getInt("sector");
                Employee employee = new Employee(ID, NAME, AGE, SECTOR, building);
                arr.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    List<Employee> getAllEmployees() {
        List<Employee> arr = new ArrayList<Employee>();
        try {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT e.id, e.name, e.age, e.sector, s.building FROM employees e JOIN sector s on e.sector = s.id");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("id");
                String NAME = rs.getString("name");
                int AGE = rs.getInt("age");
                int SECTOR = rs.getInt("sector");
                String BUILDING = rs.getString("building");
                Employee employee = new Employee(ID, NAME, AGE, SECTOR, BUILDING);
                arr.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }
	
	public boolean createEmployee(String name, int age, int sector) {
		boolean res = false;
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT into employees (name, age, sector) values('' + name + '' + age + ', ' + sector + ')");
			res = pstmt.execute();
			return res;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return res;
		}
	
	
		public void deleteEmployee(int id) {
			try {
				PreparedStatement pstmt = con.prepareStatement("DELETE from employees where id + " +id);
				pstmt.execute();
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
		
		void deleteAllEmployees() {
			try {
				PreparedStatement pstmt = con.prepareStatement("DELETE from employees");
				pstmt.execute();
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
		
		public void editEmployee(int id, String name, int age, int sector) {
			try {
				PreparedStatement pstmt = con.prepareStatement("UPDATE employees set name='"+name + "',age="+age
						+ ", sector=" + sector + "where id ="+id);
				pstmt.execute();
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
		
		void resetTables()throws SQLException {
				PreparedStatement pstmt;
				List<String>arr = new ArrayList<String>();
				arr.add("INSERT INTO EMPLOYEES (name, age, sector) VALUES('John Doe',30,102);");
			    arr.add("INSERT INTO EMPLOYEES (name, age, sector) VALUES('Jane Smith',28,103);");
			    arr.add("INSERT INTO EMPLOYEES (name, age, sector) VALUES('Alice Johnson',35,102);");
			    arr.add("INSERT INTO EMPLOYEES (name, age, sector) VALUES('Bob Williams',32,104);");
			    
			    try {
			        con.setAutoCommit(false); // Start transaction
			        for (String sql : arr) {
			            pstmt = con.prepareStatement(sql);
			            pstmt.executeUpdate();
			        }
			        con.commit(); // Commit transaction
			        con.setAutoCommit(true); // Reset auto-commit mode
			    } catch (SQLException e) {
			        con.rollback(); // Rollback transaction in case of error
			        e.printStackTrace();
			    }
			}
	}
		
		


