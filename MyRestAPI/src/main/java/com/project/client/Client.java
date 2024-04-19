package com.project.client;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.project.db.Employee;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.net.URI;
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
		panel.add(createLabeledComponent("ID:", idSearchField));

		// Search by Name
		JTextField nameSearchField = createTextField();
		panel.add(createLabeledComponent("Name:", nameSearchField));

		// Search by Age
		JTextField ageSearchField = createTextField();
		panel.add(createLabeledComponent("Age:", ageSearchField));

		// Search by Sector (Dropdown)
		JComboBox<String> sectorSearchComboBox = createComboBox();
		// Populate sectorSearchComboBox with sector options
		// Example: sectorSearchComboBox.addItem("Sector 1"); (Add actual sector items
		// from your database or list)
		panel.add(createLabeledComponent("Sector:", sectorSearchComboBox));

		// Unified Search Button
		JButton searchButton = new JButton("Search");
		panel.add(searchButton);

		return panel;
	}

	private JPanel createModifyPanel(String type) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JTextField idField = createTextField();
		JTextField nameField = createTextField();
		JTextField ageField = createTextField();
		JTextField sectorField = createTextField();
		JTextField buidlingField = createTextField();
		// Populate sectorComboBox as needed

		panel.add(createLabeledComponent("ID:", idField));
		panel.add(createLabeledComponent("Name:", nameField));
		panel.add(createLabeledComponent("Age:", ageField));
		panel.add(createLabeledComponent("Sector:", sectorField));
		panel.add(createLabeledComponent("Building:", buidlingField));

		JButton actionButton = new JButton(type);
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


		// Call pull parser to parse responseXml... (return a list of employees)

//		DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
//		model.setRowCount(0);
//		try (Connection conn = DatabaseUtils.getConnection(); // Utilize your connection method
//				Statement stmt = conn.createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT id, name, age, sector FROM employees")) { // Adjust SQL as
//																									// needed
//
//			while (rs.next()) {
//				Object[] row = { rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("sector") };
//				model.addRow(row);
//			}
//		} catch (SQLException e) {
//			JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Database Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
	

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
