package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client extends JFrame {
    private JTable employeesTable;

    public Client() {
        initializeGUI();
        loadEmployeesFromDatabase();
    }
    
    public class DatabaseUtils {
        static Connection getConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB;ifexists=true");
        }
    }
    
    private void loadEmployeesFromDatabase() {
        DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
        try (Connection conn = DatabaseUtils.getConnection(); // Utilize your connection method
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, age, sector FROM employees")) { // Adjust SQL as needed

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("sector")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
        tabbedPane.addTab("Search", createSearchPanel());
        tabbedPane.addTab("Insert", createModifyPanel("Insert"));
        tabbedPane.addTab("Update", createModifyPanel("Update"));
        tabbedPane.addTab("Delete", createModifyPanel("Delete"));

        JPanel tablePanel = new JPanel(new BorderLayout());
        employeesTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "Age", "Sector"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(employeesTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(tabbedPane);
        splitPane.setRightComponent(tablePanel);
        splitPane.setDividerLocation(300); // Adjust for proper layout

        tabbedPane.setMinimumSize(new Dimension(300, getHeight()));
        tablePanel.setMinimumSize(new Dimension(500, getHeight()));
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
        // Example: sectorSearchComboBox.addItem("Sector 1"); (Add actual sector items from your database or list)
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
        JComboBox<String> sectorComboBox = createComboBox();
        // Populate sectorComboBox as needed

        panel.add(createLabeledComponent("ID:", idField));
        panel.add(createLabeledComponent("Name:", nameField));
        panel.add(createLabeledComponent("Age:", ageField));
        panel.add(createLabeledComponent("Sector:", sectorComboBox));

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
}
