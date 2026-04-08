import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        // Frame
        JFrame frame = new JFrame("Finance Tracker");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 250));

        // Title
        JLabel title = new JLabel("Finance Tracker");
        title.setBounds(200, 20, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Amount
        JLabel amtLabel = new JLabel("Amount:");
        amtLabel.setBounds(100, 80, 100, 25);
        JTextField amtField = new JTextField();
        amtField.setBounds(200, 80, 150, 25);

        // Type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(100, 120, 100, 25);
        String[] types = {"income", "expense"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(200, 120, 150, 25);

        // Category
        JLabel catLabel = new JLabel("Category:");
        catLabel.setBounds(100, 160, 100, 25);
        JTextField catField = new JTextField();
        catField.setBounds(200, 160, 150, 25);

        // Buttons
        JButton addBtn = new JButton("Add Transaction");
        addBtn.setBounds(200, 210, 180, 35);

        JButton viewBtn = new JButton("View Transactions");
        viewBtn.setBounds(200, 260, 180, 35);

        // Delete section
        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(100, 310, 100, 25);
        JTextField idField = new JTextField();
        idField.setBounds(200, 310, 150, 25);

        JButton deleteBtn = new JButton("Delete Transaction");
        deleteBtn.setBounds(200, 350, 180, 35);

        // Database details
        String url = "jdbc:postgresql://localhost:5432/finance_tracker";
        String user = "postgres";
        String password = "2011";

        // ADD TRANSACTION
        addBtn.addActionListener(e -> {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);

                String sql = "INSERT INTO transactions (amount, type, category, date) VALUES (?, ?, ?, CURRENT_DATE)";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(amtField.getText()));
                pst.setString(2, typeBox.getSelectedItem().toString());
                pst.setString(3, catField.getText());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Transaction Added!");

                amtField.setText("");
                catField.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // VIEW TRANSACTIONS
        viewBtn.addActionListener(e -> {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");

                String[] columns = {"ID", "Amount", "Type", "Category", "Date"};
                Object[][] data = new Object[50][5];

                int i = 0;
                while (rs.next()) {
                    data[i][0] = rs.getInt("id");
                    data[i][1] = rs.getInt("amount");
                    data[i][2] = rs.getString("type");
                    data[i][3] = rs.getString("category");
                    data[i][4] = rs.getString("date");
                    i++;
                }

                JTable table = new JTable(data, columns);
                JScrollPane sp = new JScrollPane(table);

                JFrame tableFrame = new JFrame("Transactions");
                tableFrame.setSize(600, 400);
                tableFrame.add(sp);
                tableFrame.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // DELETE TRANSACTION
        deleteBtn.addActionListener(e -> {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);

                String sql = "DELETE FROM transactions WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(idField.getText()));

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "ID not found!");
                }

                idField.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add components
        panel.add(title);
        panel.add(amtLabel);
        panel.add(amtField);
        panel.add(typeLabel);
        panel.add(typeBox);
        panel.add(catLabel);
        panel.add(catField);
        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(idLabel);
        panel.add(idField);
        panel.add(deleteBtn);

        frame.add(panel);
        frame.setVisible(true);
    }
}