package com.tamik.database;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// test_database for guest role

public class GUI {
    static DB_Functions db = new DB_Functions();

    public static void OpenSecondWindow(Connection conn, String username, String password){
        JFrame CreateOrDropDB = new JFrame("Create or delete database");
        CreateOrDropDB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CreateOrDropDB.setSize(300, 150);
        JPanel createAndDropDBPanel = new JPanel();
        JLabel databaseLabel = new JLabel("Database name");
        JTextField databaseTextField = new JTextField(20);
        createAndDropDBPanel.add(databaseLabel);
        createAndDropDBPanel.add(databaseTextField);
        JButton createDBButton = new JButton("Create");
        JButton dropDBButton = new JButton("Delete");
        JButton openDBButton = new JButton("Open");
        createAndDropDBPanel.add(createDBButton);
        createAndDropDBPanel.add(openDBButton);
        createAndDropDBPanel.add(dropDBButton);

        createDBButton.addActionListener(e -> {
            try {
                String databaseName = databaseTextField.getText();
                Statement stm = conn.createStatement();
                stm.executeUpdate("CREATE DATABASE " + databaseName);
                JOptionPane.showMessageDialog(null, "Database created");
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        dropDBButton.addActionListener(e -> {
            try {
                String databaseName = databaseTextField.getText();
                Statement stm = conn.createStatement();
                stm.executeUpdate("DROP DATABASE " + databaseName);
                JOptionPane.showMessageDialog(null, "Database deleted!");
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        openDBButton.addActionListener(e -> {
            try {
                String databaseName = databaseTextField.getText();
                Connection openDatabaseConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseName, username, password);
                db.createTableFunction(openDatabaseConnection);
                CallableStatement callableStatement = openDatabaseConnection.prepareCall("SELECT createTable()");
                callableStatement.executeQuery();
                JOptionPane.showMessageDialog(null, "Successfully connected to " + databaseName + " !");
                gui2 dialog = new gui2(openDatabaseConnection);
                dialog.pack();
                dialog.setVisible(true);
            } catch (Exception connectionException) {
                JOptionPane.showMessageDialog(null, connectionException);
            }
        });

        CreateOrDropDB.add(createAndDropDBPanel);
        CreateOrDropDB.setVisible(true);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame authorizationFrame = new JFrame("Authorization");
        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username");
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password");
        JPanel buttonPanel = new JPanel();
        JButton connectButton = new JButton("Connect");

        JTextField usernameTextField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        authorizationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        authorizationFrame.setSize(500, 300);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        buttonPanel.add(connectButton);
        authorizationFrame.setLayout(new GridLayout(6, 1));
        authorizationFrame.add(usernamePanel);
        authorizationFrame.add(passwordPanel);
        authorizationFrame.add(buttonPanel);
        authorizationFrame.setVisible(true);

        // Авторизация и подключение к БД
        connectButton.addActionListener(e -> {
            try {
                String username = usernameTextField.getText();
                String password = String.valueOf(passwordField.getPassword());
                Connection conn = null;
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + "postgres", username, password);
                JOptionPane.showMessageDialog(null, "Connection established");
                OpenSecondWindow(conn, username, password);

            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, exception);
            }
        });
    }
}