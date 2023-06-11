package com.tamik.database;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.sql.*;
import java.util.Locale;

public class database_editor {

    static DB_Functions db = new DB_Functions();
    static String[] comboBoxValues = {"True", "False"};
    private CallableStatement cstat1 = null;

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // data
        Object[][] data = new Object[100][columnCount];
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students");

            table.setModel(buildTableModel(rs));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open_database_editor(Connection conn) {

        JFrame database_editor = new JFrame("Database Editor");
        database_editor.setSize(900, 600);
        database_editor.setLayout(new FlowLayout());
        database_editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField NameField = new JTextField();
        JTextField GroupField = new JTextField();
        JTextField MarkField = new JTextField();
        JTextField IDField = new JTextField();
        JTable table = new JTable();
        JTextField SearchNameField = new JTextField();
        JButton searchButton = new JButton();
        JButton addButton = new JButton();
        JButton updateButton = new JButton();
        JButton deleteButton = new JButton();
        JButton clearTableButton = new JButton();
        JComboBox<String> WasPresentComboBox = new JComboBox<>(comboBoxValues);
        JPanel DataPanel = new JPanel();
        JPanel ClearSearchPanel = new JPanel();
        JPanel FunctionsPanel = new JPanel();
        JPanel IndentPanel = new JPanel();
        JPanel MainPanel = new JPanel();
        JLabel StudentsDBMSLabel = new JLabel();
        JLabel NameLabel = new JLabel();
        JLabel WasPresentLabel = new JLabel();
        JLabel GroupLabel = new JLabel();
        JLabel MarkLabel = new JLabel();
        JLabel SearchByNameLabel = new JLabel();
        JLabel IdLabel = new JLabel();

        MainPanel.add(StudentsDBMSLabel);
        MainPanel.add(DataPanel);
        MainPanel.add(table);
        MainPanel.add(ClearSearchPanel);
        MainPanel.add(FunctionsPanel);
        MainPanel.add(IndentPanel);

        DataPanel.add(IdLabel);
        DataPanel.add(NameLabel);
        DataPanel.add(WasPresentLabel);
        DataPanel.add(GroupLabel);
        DataPanel.add(MarkLabel);
        DataPanel.add(IDField);
        DataPanel.add(NameField);
        DataPanel.add(GroupField);
        DataPanel.add(MarkField);
        DataPanel.add(WasPresentComboBox);

        ClearSearchPanel.add(clearTableButton);
        ClearSearchPanel.add(SearchByNameLabel);
        ClearSearchPanel.add(SearchNameField);
        ClearSearchPanel.add(searchButton);

        FunctionsPanel.add(addButton);
        FunctionsPanel.add(updateButton);
        FunctionsPanel.add(deleteButton);

        database_editor.add(MainPanel);
        database_editor.add(DataPanel);
        database_editor.add(ClearSearchPanel);
        database_editor.add(IndentPanel);

        clearTableButton.addActionListener(e -> {
            try {
                cstat1 = conn.prepareCall("SELECT TruncateTable(?)");
                cstat1.setString(1, "Students");
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
                JOptionPane.showMessageDialog(null, "Таблица очищена");
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        searchButton.addActionListener(e -> {
            try {
                ResultSet rs = cstat1.executeQuery("SELECT * FROM Students");
                cstat1.setString(1, "Students");
                cstat1.setString(2, SearchNameField.getText());
                cstat1.executeQuery();
                cstat1.close();

                table.setModel(buildTableModel(rs));

                rs.close();
                cstat1.close();
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        addButton.addActionListener(e -> {
            try {
                cstat1 = conn.prepareCall("SELECT InsertData(?, ?, ?, ?, ?, ?)");
                cstat1.setString(1, "Students");
                cstat1.setInt(2, Integer.parseInt(IDField.getText()));
                cstat1.setString(3, NameField.getText());
                cstat1.setBoolean(4, Boolean.valueOf((Boolean) WasPresentComboBox.getSelectedItem()));
                cstat1.setInt(5, Integer.parseInt(GroupField.getText()));
                cstat1.setFloat(6, Float.valueOf(MarkField.getText()));
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                cstat1 = conn.prepareCall("SELECT UpdateTuple(?, ?, ?, ?, ?, ?)");
                cstat1.setString(1, "Students");
                cstat1.setInt(2, Integer.parseInt(IDField.getText()));
                cstat1.setString(3, NameField.getText());
                cstat1.setBoolean(4, Boolean.valueOf((Boolean) WasPresentComboBox.getSelectedItem()));
                cstat1.setInt(5, Integer.parseInt(GroupField.getText()));
                cstat1.setFloat(6, Float.valueOf(MarkField.getText()));
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                cstat1 = conn.prepareCall("SELECT DeleteByName(?, ?)");
                cstat1.setString(1, "Students");
                cstat1.setString(2, NameField.getText());
                cstat1.executeQuery();
                cstat1.close();
                populateTable(conn, table);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        database_editor.setVisible(true);
    }
}
