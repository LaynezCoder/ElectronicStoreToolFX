package com.laynezcoder.database;

import com.laynezcoder.models.Customers;
import com.laynezcoder.models.Products;
import com.laynezcoder.models.Quotes;
import com.laynezcoder.models.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class DatabaseHelper {

    public static boolean insertNewCustomer(Customers customers, ObservableList<Customers> listCustomers) {
        try {
            String sql = "INSERT INTO Customers (customerName, customerNumber, customerEmail, it) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customers.getCustomerName());
            preparedStatement.setString(2, customers.getCustomerNumber());
            preparedStatement.setString(3, customers.getCustomerEmail());
            preparedStatement.setString(4, customers.getIt());
            preparedStatement.execute();
            listCustomers.add(customers);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deleteCustomer(TableView<Customers> tbl, ObservableList<Customers> listCustomers) {
        try {
            String sql = "DELETE FROM Customers WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, tbl.getSelectionModel().getSelectedItem().getId());
            preparedStatement.execute();
            listCustomers.remove(tbl.getSelectionModel().getSelectedIndex());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateCustomer(Customers customers) {
        try {
            String sql = "UPDATE Customers SET customerName = ?, customerNumber = ?, customerEmail = ?, it = ? WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customers.getCustomerName());
            preparedStatement.setString(2, customers.getCustomerNumber());
            preparedStatement.setString(3, customers.getCustomerEmail());
            preparedStatement.setString(4, customers.getIt());
            preparedStatement.setInt(5, customers.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static Customers searchCustomer(String name) {
        Customers cutomers = null;
        try {
            String sql = "SELECT id, customerName FROM Customers WHERE customerName = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customerName");
                cutomers = new Customers(id, customerName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cutomers;
    }

    public static boolean insertNewQuote(Quotes quotes, ObservableList<Quotes> listQuotes) {
        try {
            String sql = "INSERT INTO Quotes (descriptionQuote, requestDate, price, existence, realization, report, customerId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, quotes.getDescriptionQuote());
            preparedStatement.setDate(2, new java.sql.Date(quotes.getRequestDate().getTime()));
            preparedStatement.setDouble(3, quotes.getPrice());
            preparedStatement.setString(4, quotes.getExistence());
            preparedStatement.setString(5, quotes.getRealization());
            preparedStatement.setString(6, quotes.getReport());
            preparedStatement.setInt(7, quotes.getCustomerId());
            preparedStatement.execute();
            listQuotes.add(quotes);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deeleteQuotes(TableView<Quotes> tbl, ObservableList<Quotes> listQuotes) {
        try {
            String sql = "DELETE FROM Quotes WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, tbl.getSelectionModel().getSelectedItem().getId());
            preparedStatement.execute();
            listQuotes.remove(tbl.getSelectionModel().getSelectedIndex());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateQuotes(Quotes quotes) {
        try {
            String sql = "UPDATE Quotes SET descriptionQuote = ?, requestDate = ?, price = ?, existence = ?, realization = ?, report = ? WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, quotes.getDescriptionQuote());
            preparedStatement.setDate(2, new java.sql.Date(quotes.getRequestDate().getTime()));
            preparedStatement.setDouble(3, quotes.getPrice());
            preparedStatement.setString(4, quotes.getExistence());
            preparedStatement.setString(5, quotes.getRealization());
            preparedStatement.setString(6, quotes.getReport());
            preparedStatement.setInt(7, quotes.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean insertNewProduct(Products products, ObservableList<Products> listProducts) {
        try {
            String sql = "INSERT INTO Products (barcode, productName, purchasePrice, porcentage, salePrice, minimalPrice, descriptionProduct) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, products.getBarcode());
            preparedStatement.setString(2, products.getProductName());
            preparedStatement.setDouble(3, products.getPurchasePrice());
            preparedStatement.setInt(4, products.getPorcentage());
            preparedStatement.setDouble(5, products.getSalePrice());
            preparedStatement.setDouble(6, products.getMinimalPrice());
            preparedStatement.setString(7, products.getDescriptionProduct());
            preparedStatement.execute();
            listProducts.add(products);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deleteProduct(TableView<Products> tbl, ObservableList<Products> listProducts) {
        try {
            String sql = "DELETE FROM Products WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, tbl.getSelectionModel().getSelectedItem().getId());
            preparedStatement.execute();
            listProducts.remove(tbl.getSelectionModel().getSelectedIndex());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateProduct(Products products) {
        try {
            String sql = "UPDATE Products SET barcode = ?, productName = ?, purchasePrice = ?, "
                    + "porcentage = ?, salePrice = ?, minimalPrice = ?, descriptionProduct = ? "
                    + "WHERE  id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, products.getBarcode());
            preparedStatement.setString(2, products.getProductName());
            preparedStatement.setDouble(3, products.getPurchasePrice());
            preparedStatement.setInt(4, products.getPorcentage());
            preparedStatement.setDouble(5, products.getSalePrice());
            preparedStatement.setDouble(6, products.getMinimalPrice());
            preparedStatement.setString(7, products.getDescriptionProduct());
            preparedStatement.setInt(8, products.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static int checkIfProductExists(String barcode) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products WHERE barcode = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, barcode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static boolean insertNewUser(Users users, ObservableList<Users> listUsers) {
        try {
            String sql = "INSERT INTO Users (nameUser, email, pass, userType) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, users.getNameUser());
            preparedStatement.setString(2, users.getEmail());
            preparedStatement.setString(3, users.getPass());
            preparedStatement.setString(4, users.getUserType());
            preparedStatement.execute();
            listUsers.add(users);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deleteUser(TableView<Users> tbl, ObservableList<Users> listUsers) {
        try {
            String sql = "DELETE FROM Users WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, tbl.getSelectionModel().getSelectedItem().getId());
            preparedStatement.execute();
            listUsers.remove(tbl.getSelectionModel().getSelectedIndex());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateUser(Users users) {
        try {
            String sql = "UPDATE Users SET nameUser = ?, email = ?, pass = ?, userType = ? WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, users.getNameUser());
            preparedStatement.setString(2, users.getEmail());
            preparedStatement.setString(3, users.getPass());
            preparedStatement.setString(4, users.getUserType());
            preparedStatement.setInt(5, users.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateUserFromSettings(Users users) {
        try {
            String sql = "UPDATE Users SET nameUser = ?, email = ?, pass = ?, biography = ?, dialogTransition = ? WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, users.getNameUser());
            preparedStatement.setString(2, users.getEmail());
            preparedStatement.setString(3, users.getPass());
            preparedStatement.setString(4, users.getBiography());
            preparedStatement.setString(5, users.getDialogTransition());
            preparedStatement.setInt(6, users.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static int checkIfUserExists(String username) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Users WHERE email = BINARY ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static boolean insertUsserSession(int id) {
        try {
            String sql = "INSERT INTO UserSession (userId) VALUES (?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static int checkIfUserExists() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Users";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static String getDialogTransition() {
        String dialogTransition = null;
        try {
            String sql = "SELECT Users.dialogTransition FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dialogTransition = resultSet.getString("dialogTransition");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);  
            dialogTransition = "CENTER";
        }
        return dialogTransition;
    }

    public static String getUserType() {
        String userType = null;
        try {
            String sql = "SELECT Users.userType FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userType = resultSet.getString("userType");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userType;
    }

    public static int getIdUserSession() {
        int userId = 0;
        try {
            String sql = "SELECT userId FROM UserSession";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt("userId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userId;
    }

    public static String getUserSession() {
        String user = null;
        try {
            String sql = "SELECT Users.email FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = resultSet.getString("email");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public static void logout() {
        try {
            String sql = "TRUNCATE TABLE UserSession";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getCustomers(java.sql.Date date) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Customers WHERE insertionDate = ?";
            PreparedStatement preparedStatementCustomers = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatementCustomers.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = preparedStatementCustomers.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getQuotes(java.sql.Date date) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes WHERE requestDate = ?";
            PreparedStatement preparedStatementCustomers = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatementCustomers.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = preparedStatementCustomers.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getProducts(java.sql.Date date) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products WHERE insertionDate = ?";
            PreparedStatement preparedStatementCustomers = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatementCustomers.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = preparedStatementCustomers.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

}
