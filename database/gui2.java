package com.tamik.database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class gui2 extends JDialog {
    private JPanel contentPane;
    private JButton updateButton;
    private JButton AddButton;
    private JButton deleteButton;
    private JTable table;
    private JTextField IdField;
    private JTextField NameField;
    private JTextField GroupField;
    private JTextField MarkField;
    private JButton searchByNameButton;
    private JButton clearTableButton;
    private JPanel LowerPanel;
    private JPanel ButtonsPanel;
    private JPanel TablePanel;
    private JLabel IdLabel;
    private JLabel NameLabel;
    private JLabel WasPresentLabel;
    private JCheckBox WasPresentCheckBox;
    private JLabel StudentsDatabaseLabel;
    private JLabel GroupLabel;
    private JLabel MarkLabel;
    private JButton refreshButton;
    private CallableStatement cstat1 = null;
    private DB_Functions db = new DB_Functions();

    public gui2(Connection conn) {
        setContentPane(contentPane);
        setModal(true);

        populateTable(conn, table);

        AddButton.addActionListener(e -> {
            try {
                if (WasPresentCheckBox.isSelected()) {
                    cstat1 = conn.prepareCall("SELECT InsertData(?, ?, ?, ?, ?)");
                    cstat1.setInt(1, Integer.parseInt(IdField.getText()));
                    cstat1.setString(2, NameField.getText());
                    cstat1.setBoolean(3, true);
                    cstat1.setInt(4, Integer.parseInt(GroupField.getText()));
                    cstat1.setFloat(5, Float.parseFloat(MarkField.getText()));
                } else {
                    cstat1 = conn.prepareCall("SELECT InsertData(?, ?, ?, ?, ?)");
                    cstat1.setInt(1, Integer.parseInt(IdField.getText()));
                    cstat1.setString(2, NameField.getText());
                    cstat1.setBoolean(3, false);
                    cstat1.setInt(4, Integer.parseInt(GroupField.getText()));
                    cstat1.setFloat(5, Float.parseFloat(MarkField.getText()));
                }
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                if (WasPresentCheckBox.isSelected()) {
                    cstat1 = conn.prepareCall("SELECT UpdateTuple(?, ?, ?, ?, ?)");
                    cstat1.setInt(1, Integer.parseInt(IdField.getText()));
                    cstat1.setString(2, NameField.getText());
                    cstat1.setBoolean(3, true);
                    cstat1.setInt(4, Integer.parseInt(GroupField.getText()));
                    cstat1.setFloat(5, Float.parseFloat(MarkField.getText()));
                } else {
                    cstat1 = conn.prepareCall("SELECT UpdateTuple(?, ?, ?, ?, ?)");
                    cstat1.setInt(1, Integer.parseInt(IdField.getText()));
                    cstat1.setString(2, NameField.getText());
                    cstat1.setBoolean(3, false);
                    cstat1.setInt(4, Integer.parseInt(GroupField.getText()));
                    cstat1.setFloat(5, Float.parseFloat(MarkField.getText()));
                }
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                cstat1 = conn.prepareCall("SELECT DeleteByName(?)");
                cstat1.setString(1, NameField.getText());
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        clearTableButton.addActionListener(e -> {
            try {
                cstat1 = conn.prepareCall("SELECT TruncateTable()");
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        searchByNameButton.addActionListener(e -> {
            try {
                populateTableWithSearchResults(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });
    }
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // data
        Object[][] data = new Object[30][columnCount];
        int rowCount = 0;
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                data[rowCount][i - 1] = rs.getObject(i);
            }
            rowCount++;
        }

        return new DefaultTableModel(data, columnNames);
    }

    public void populateTable(Connection conn, JTable table) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from students;");

            table.setModel(buildTableModel(rs));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateTableWithSearchResults(Connection conn, JTable table) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SearchByName(" + "'" + NameField.getText() + "'" +")");
            table.setModel(buildTableModel(rs));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
