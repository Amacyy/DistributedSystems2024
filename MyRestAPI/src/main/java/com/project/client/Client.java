package com.project.client;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.project.db.Employee;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Client extends JFrame implements ActionListener {
	private JTable employeesTable;
	private JButton createTableButton = new JButton("Create Table");
	private JButton fillTableButton = new JButton("Fill Table");

	public Client() {
		initializeGUI();
	}

	private void initializeGUI() {
		setTitle("Employee Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1153, 660); // Adjust size as needed
		setLocationRelativeTo(null);

		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JTabbedPane tabbedPane = new JTabbedPane();

		// Adding tabs with a consistent interface
		tabbedPane.addTab("Init", createInitPanel());
		tabbedPane.addTab("Search", createSearchPanel());
		tabbedPane.addTab("Insert", createModifyPanel("Insert"));
		tabbedPane.addTab("Update", createModifyPanel("Update"));
		tabbedPane.addTab("Delete", createModifyPanel("Delete"));

		JPanel tablePanel = new JPanel(new BorderLayout());
		employeesTable = new JTable(
				new DefaultTableModel(new Object[] { "ID", "Name", "Age", "Sector", "Building" }, 0));
		JScrollPane tableScrollPane = new JScrollPane(employeesTable);
		tablePanel.add(tableScrollPane, BorderLayout.CENTER);

		splitPane.setLeftComponent(tabbedPane);
		splitPane.setRightComponent(tablePanel);
		splitPane.setDividerLocation(300); // Adjust for proper layout

		tabbedPane.setMinimumSize(new Dimension(300, getHeight()));
		tablePanel.setMinimumSize(new Dimension(500, getHeight()));
	}

	private JPanel createInitPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createTableButton);
		createTableButton.addActionListener(this);

		panel.add(fillTableButton);
		fillTableButton.addActionListener(this);

		return panel;
	}

	// Creates the panel used for search with specific fields and one button
	private JPanel createSearchPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Search by ID
	    JTextField idSearchField = createTextField();
	    JButton idSearchButton = new JButton("Search by ID");
	    idSearchButton.addActionListener(e -> searchById(idSearchField.getText()));
	    panel.add(createLabeledComponentWithButton("ID:", idSearchField, idSearchButton));

		// Search by Name
	    JTextField nameSearchField = createTextField();
	    JButton nameSearchButton = new JButton("Search by Name");
	    nameSearchButton.addActionListener(e -> searchByName(nameSearchField.getText()));
	    panel.add(createLabeledComponentWithButton("Name:", nameSearchField, nameSearchButton));

		// Search by Age
	    JTextField ageSearchField = createTextField();
	    JButton ageSearchButton = new JButton("Search by Age");
	    ageSearchButton.addActionListener(e -> searchByAge(ageSearchField.getText()));
	    panel.add(createLabeledComponentWithButton("Age:", ageSearchField, ageSearchButton));


	    JTextField sectorSearchField = createTextField();
	    JButton sectorSearchButton = new JButton("Search by Sector");
	    sectorSearchButton.addActionListener(e -> searchBySector(sectorSearchField.getText()));
	    panel.add(createLabeledComponentWithButton("Sector:", sectorSearchField, sectorSearchButton));


		// Unified Search Button
	    JButton searchButton = new JButton("Search All");
	    searchButton.addActionListener(e -> fetchAllEmployees());
	    panel.add(searchButton);

		return panel;
	}
	
	private JPanel createLabeledComponentWithButton(String label, JTextField textField, JButton button) {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	    panel.add(new JLabel(label));
	    panel.add(Box.createRigidArea(new Dimension(5, 0)));
	    panel.add(textField);
	    panel.add(button);
	    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    return panel;
	}

	private JPanel createModifyPanel(String type) {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	    // Shared text fields; initialized here to avoid duplication in if-else blocks
	    JTextField idField = createTextField();
	    JTextField nameField = createTextField();
	    JTextField ageField = createTextField();
	    JTextField sectorField = createTextField();
	    JTextField buildingField = createTextField();

	    // Add ID field for all types since it's common for Insert, Update, and Delete
	    panel.add(createLabeledComponent("ID:", idField));

	    // Conditional addition of text fields based on the action type
	    if (type.equals("Insert") || type.equals("Update")) {
	        // For Insert and Update, we need all fields
	        panel.add(createLabeledComponent("Name:", nameField));
	        panel.add(createLabeledComponent("Age:", ageField));
	        panel.add(createLabeledComponent("Sector:", sectorField));
	        panel.add(createLabeledComponent("Building:", buildingField));
	    }

	    // Button for insert, update, or delete based on type
	    JButton actionButton = new JButton(type);
	    if (type.equals("Insert")) {
	        actionButton.addActionListener(e -> insertEmployee(
	            idField.getText(),
	            nameField.getText(),
	            ageField.getText(),
	            sectorField.getText(),
	            buildingField.getText()
	        ));
	    } else if (type.equals("Update")) {
	        actionButton.addActionListener(e -> updateEmployee(
	            idField.getText(),
	            nameField.getText(),
	            ageField.getText(),
	            sectorField.getText(),
	            buildingField.getText()
	        ));
	    } else if (type.equals("Delete")) {
	        // For Delete, we only need the ID field; the rest are not added
	        actionButton.addActionListener(e -> deleteEmployeeById(idField.getText()));
	    }

	    panel.add(actionButton);

	    return panel;
	}





	private JTextField createTextField() {
		JTextField textField = new JTextField();
		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
		return textField;
	}

	private JComboBox<String> createComboBox() {
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getPreferredSize().height));
		return comboBox;
	}

	private JPanel createLabeledComponent(String label, JComponent component) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new JLabel(label));
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(component);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Client().setVisible(true));
	}

	private void sendCreateTableRequest() throws Exception {
		URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080)
				.setPath("/MyRestAPI/rest/employees/createTable").build();

		HttpPost req = new HttpPost(uri);

		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = client.execute(req);

		if (resp.getStatusLine().getStatusCode() == 500) {
			throw new Exception("Error creating tables");
		}
	}

	private void sendFillTableRequest() throws Exception {
		URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080)
				.setPath("/MyRestAPI/rest/employees/fillTable").build();

		HttpPost req = new HttpPost(uri);

		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = client.execute(req);

		if (resp.getStatusLine().getStatusCode() == 500) {
			throw new Exception("Error filling tables");
		}
	}

	private void sendGetEmployessRequest() throws Exception {
		URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080)
				.setPath("/MyRestAPI/rest/employees").build();

		HttpGet req = new HttpGet(uri);

		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = client.execute(req);

		HttpEntity entity = resp.getEntity();


		String responseXml = EntityUtils.toString(entity);
		
		List<Employee> employees = parseXMLWithPullParser(responseXml);
        updateEmployeeTable(employees);
	}
	
	
	private List<Employee> parseXMLWithPullParser(String xmlData) throws Exception {
        List<Employee> employees = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        parser.setInput(new StringReader(xmlData));
        int eventType = parser.getEventType();
        Employee currentEmployee = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("employee".equalsIgnoreCase(tagName)) {
                        currentEmployee = new Employee();
                    } else if (currentEmployee != null) {
                        switch (tagName.toLowerCase()) {
                            case "id":
                                currentEmployee.setId(Integer.parseInt(parser.nextText()));
                                break;
                            case "name":
                                currentEmployee.setName(parser.nextText());
                                break;
                            case "age":
                                currentEmployee.setAge(Integer.parseInt(parser.nextText()));
                                break;
                            //case "sector":
                             //   currentEmployee.setSector(parser.nextText());
                               // break;
                            case "building":
                                currentEmployee.setBuilding(parser.nextText());
                                break;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("employee".equalsIgnoreCase(tagName) && currentEmployee != null) {
                        employees.add(currentEmployee);
                        currentEmployee = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return employees;
    }
	
	private void updateEmployeeTable(List<Employee> employees) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
            model.setRowCount(0);
            for (Employee employee : employees) {
                model.addRow(new Object[]{
                        employee.getId(),
                        employee.getName(),
                        employee.getAge(),
                        employee.getSector(),
                        employee.getBuilding()
                });
            }
        });
    }
	
	private void searchById(String id) {
	    // Assuming Apache HttpClient for REST API interaction
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/id/" + id)
	                .build();

	        HttpGet request = new HttpGet(uri);
	        CloseableHttpClient client = HttpClients.createDefault();
	        CloseableHttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();

	        String responseString = EntityUtils.toString(entity);
	        // Assuming the response is XML, parse it and update your table
	        List<Employee> employees = parseXMLWithPullParser(responseString);
	        updateEmployeeTable(employees);

	        client.close();
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error searching by ID: " + e.getMessage());
	    }
	}
	
	private void searchByName(String name) {
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/name/" + URLEncoder.encode(name, StandardCharsets.UTF_8.toString()))
	                .build();

	        HttpGet request = new HttpGet(uri);
	        try (CloseableHttpClient client = HttpClients.createDefault();
	             CloseableHttpResponse response = client.execute(request)) {
	            HttpEntity entity = response.getEntity();
	            String responseString = EntityUtils.toString(entity);
	            List<Employee> employees = parseXMLWithPullParser(responseString);
	            updateEmployeeTable(employees);
	        }
	    } catch (URISyntaxException e) {
	        JOptionPane.showMessageDialog(null, "Error in URI Syntax: " + e.getMessage());
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "IO Exception: " + e.getMessage());
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "General Error: " + e.getMessage());
	    }
	}
	
	private void searchByAge(String age) {
	    // Assuming Apache HttpClient for REST API interaction
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/age/" + URLEncoder.encode(age, StandardCharsets.UTF_8.toString()))
	                .build();

	        HttpGet request = new HttpGet(uri);
	        try (CloseableHttpClient client = HttpClients.createDefault();
	             CloseableHttpResponse response = client.execute(request)) {
	            HttpEntity entity = response.getEntity();
	            String responseString = EntityUtils.toString(entity);
	            // Assuming the response is XML, parse it and update your table
	            List<Employee> employees = parseXMLWithPullParser(responseString);
	            updateEmployeeTable(employees);
	        }
	    } catch (URISyntaxException e) {
	        JOptionPane.showMessageDialog(null, "Error in URI Syntax: " + e.getMessage());
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "IO Exception: " + e.getMessage());
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "General Error: " + e.getMessage());
	    }
	}
	
	private void searchBySector(String sector) {
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/searchsector/" + URLEncoder.encode(sector, StandardCharsets.UTF_8.toString()))
	                .build();

	        HttpGet request = new HttpGet(uri);
	        try (CloseableHttpClient client = HttpClients.createDefault();
	             CloseableHttpResponse response = client.execute(request)) {
	            HttpEntity entity = response.getEntity();
	            String responseString = EntityUtils.toString(entity);
	            // Assuming the response is XML, parse it and update your table
	            List<Employee> employees = parseXMLWithPullParser(responseString);
	            updateEmployeeTable(employees);
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error searching by Sector: " + e.getMessage());
	    }
	}
	
	private void fetchAllEmployees() {
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees")
	                .build();

	        HttpGet request = new HttpGet(uri);
	        CloseableHttpClient client = HttpClients.createDefault();
	        CloseableHttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();

	        String responseXml = EntityUtils.toString(entity);
	        List<Employee> employees = parseXMLWithPullParser(responseXml);
	        updateEmployeeTable(employees);

	        client.close();
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error fetching all employees: " + e.getMessage());
	    }
	}
	
	private void insertEmployee(String id, String name, String age, String sector, String building) {
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/insert")  // Ensure this is the correct endpoint
	                .build();

	        List<NameValuePair> params = new ArrayList<>();
	        params.add(new BasicNameValuePair("id", id));
	        params.add(new BasicNameValuePair("name", name));
	        params.add(new BasicNameValuePair("age", age));
	        params.add(new BasicNameValuePair("sector", sector));
	        params.add(new BasicNameValuePair("building", building));

	        HttpPost post = new HttpPost(uri);
	        post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

	        try (CloseableHttpClient client = HttpClients.createDefault();
	             CloseableHttpResponse response = client.execute(post)) {
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                JOptionPane.showMessageDialog(null, "Employee inserted successfully");
	       
	            } else {
	                JOptionPane.showMessageDialog(null, "Failed to insert employee. Status: " + response.getStatusLine().getStatusCode());
	            }
	        }
	    } catch (URISyntaxException | IOException e) {
	        JOptionPane.showMessageDialog(null, "Error inserting employee: " + e.getMessage());
	    }
	}
	
	private void updateEmployee(String id, String name, String age, String sector, String building) {
	    try {
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/update")  // Make sure this endpoint is configured to handle updates
	                .build();

	        List<NameValuePair> params = new ArrayList<>();
	        params.add(new BasicNameValuePair("id", id));
	        params.add(new BasicNameValuePair("name", name));
	        params.add(new BasicNameValuePair("age", age));
	        params.add(new BasicNameValuePair("sector", sector));
	        params.add(new BasicNameValuePair("building", building));

	        HttpPut put = new HttpPut(uri);
	        put.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

	        try (CloseableHttpClient client = HttpClients.createDefault();
	             CloseableHttpResponse response = client.execute(put)) {
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                JOptionPane.showMessageDialog(null, "Employee updated successfully");
	            } else {
	                JOptionPane.showMessageDialog(null, "Failed to update employee. Status: " + response.getStatusLine().getStatusCode());
	            }
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error updating employee: " + e.getMessage());
	    }
	}
	
	private void deleteEmployeeById(String id) {
	    try {
	        // Build the URI for the DELETE request
	        URI uri = new URIBuilder()
	                .setScheme("http")
	                .setHost("localhost")
	                .setPort(8080)
	                .setPath("/MyRestAPI/rest/employees/delete/" + URLEncoder.encode(id, StandardCharsets.UTF_8.name()))
	                .build();

	        // Create the HTTP DELETE request
	        HttpDelete delete = new HttpDelete(uri);

	        // Execute the request using HttpClient
	        try (CloseableHttpClient client = HttpClients.createDefault();
	             CloseableHttpResponse response = client.execute(delete)) {
	            // Check the response status code
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                JOptionPane.showMessageDialog(null, "Employee deleted successfully");
	            } else {
	                JOptionPane.showMessageDialog(null, "Failed to delete employee. Status: " + response.getStatusLine().getStatusCode());
	            }
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error deleting employee: " + e.getMessage());
	    }
	}





	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == createTableButton) {
			try {
				sendCreateTableRequest();
				sendGetEmployessRequest();
				JOptionPane.showMessageDialog(this, "Table has been created.");
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		} else if (e.getSource() == fillTableButton) {
			try {
				sendFillTableRequest();
				sendGetEmployessRequest();
				JOptionPane.showMessageDialog(this, "Table has been populated.");
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}

	}
}
