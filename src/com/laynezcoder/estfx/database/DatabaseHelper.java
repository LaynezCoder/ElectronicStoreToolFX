/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laynezcoder.estfx.database;

import com.jfoenix.controls.JFXDialog;
import com.laynezcoder.estfx.models.Customers;
import com.laynezcoder.estfx.models.Products;
import com.laynezcoder.estfx.models.Quotes;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.models.UserSession;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;

public class DatabaseHelper {
    
    private static final UserSession session =  UserSession.getInstace();

    public static boolean insertNewCustomer(Customers customers) {
        try {
            String sql = "INSERT INTO Customers (customerName, customerNumber, customerEmail, it, userId) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customers.getName());
            preparedStatement.setString(2, customers.getPhone());
            preparedStatement.setString(3, customers.getEmail());
            preparedStatement.setString(4, customers.getIt());
            preparedStatement.setInt(5, session.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deleteCustomer(TableView<Customers> tbl) {
        try {
            String sql = "DELETE FROM Customers WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, tbl.getSelectionModel().getSelectedItem().getId());
            preparedStatement.execute();
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
            preparedStatement.setString(1, customers.getName());
            preparedStatement.setString(2, customers.getPhone());
            preparedStatement.setString(3, customers.getEmail());
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

    public static boolean insertNewQuote(Quotes quotes) {
        try {
            String sql = "INSERT INTO Quotes (descriptionQuote, requestDate, price, existence, realization, report, customerId, userId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, quotes.getDescription());
            preparedStatement.setDate(2, new java.sql.Date(quotes.getRequestDate().getTime()));
            preparedStatement.setDouble(3, quotes.getPrice());
            preparedStatement.setString(4, quotes.getExistence());
            preparedStatement.setString(5, quotes.getRealization());
            preparedStatement.setString(6, quotes.getReport());
            preparedStatement.setInt(7, quotes.getCustomerId());
            preparedStatement.setInt(8, session.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deeleteQuotes(TableView<Quotes> tbl) {
        try {
            String sql = "DELETE FROM Quotes WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, tbl.getSelectionModel().getSelectedItem().getId());
            preparedStatement.execute();
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
            preparedStatement.setString(1, quotes.getDescription());
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
            String sql = "INSERT INTO Products (barcode, productName, purchasePrice, porcentage, salePrice, minimalPrice, descriptionProduct, imageProduct) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, products.getBarcode());
            preparedStatement.setString(2, products.getProductName());
            preparedStatement.setDouble(3, products.getPurchasePrice());
            preparedStatement.setInt(4, products.getPorcentage());
            preparedStatement.setDouble(5, products.getSalePrice());
            preparedStatement.setDouble(6, products.getMinimalPrice());
            preparedStatement.setString(7, products.getDescriptionProduct());
            preparedStatement.setBlob(8, products.getProductImage());
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
                    + "porcentage = ?, salePrice = ?, minimalPrice = ?, descriptionProduct = ?, imageProduct = ? "
                    + "WHERE  id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, products.getBarcode());
            preparedStatement.setString(2, products.getProductName());
            preparedStatement.setDouble(3, products.getPurchasePrice());
            preparedStatement.setInt(4, products.getPorcentage());
            preparedStatement.setDouble(5, products.getSalePrice());
            preparedStatement.setDouble(6, products.getMinimalPrice());
            preparedStatement.setString(7, products.getDescriptionProduct());
            preparedStatement.setBlob(8, products.getProductImage());
            preparedStatement.setInt(9, products.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateProductIfFileIsNull(Products products) {
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

    public static Image getProductImage(int id) {
        Image image = null;
        try {
            String sql = "SELECT imageProduct FROM Products WHERE id = ?";
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InputStream img = rs.getBinaryStream("imageProduct");
                if (img != null) {
                    image = new Image(img);
                } else {
                    image = new Image(ResourcesPackages.NO_IMAGE_AVAILABLE);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            image = new Image(ResourcesPackages.NO_IMAGE_AVAILABLE);
        }
        return image;
    }

    public static boolean insertNewUser(Users users, ObservableList<Users> listUsers) {
        try {
            String sql = "INSERT INTO Users (nameUser, email, pass, userType, profileImage) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, users.getName());
            preparedStatement.setString(2, users.getUsername());
            preparedStatement.setString(3, users.getPassword());
            preparedStatement.setString(4, users.getUserType());
            preparedStatement.setBlob(5, users.getProfileImage());
            preparedStatement.execute();
            listUsers.add(users);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean insertNewUser(Users users) {
        try {
            String sql = "INSERT INTO Users (nameUser, email, pass, userType, profileImage) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, users.getName());
            preparedStatement.setString(2, users.getUsername());
            preparedStatement.setString(3, users.getPassword());
            preparedStatement.setString(4, users.getUserType());
            preparedStatement.setBlob(5, users.getProfileImage());
            preparedStatement.execute(); 
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
            preparedStatement.setString(1, users.getName());
            preparedStatement.setString(2, users.getUsername());
            preparedStatement.setString(3, users.getPassword());
            preparedStatement.setString(4, users.getUserType());
            preparedStatement.setInt(5, users.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateUserInformation(Users users) {
        try {
            String sql = "UPDATE Users SET fullname = ?, username = ?, pass = ?, biography = ?, dialogTransition = ?, linkProfile = ? WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, users.getName());
            preparedStatement.setString(2, users.getUsername());
            preparedStatement.setString(3, users.getPassword());
            preparedStatement.setString(4, users.getBiography());
            preparedStatement.setString(5, users.getDialogTransition());
            preparedStatement.setString(6, users.getLinkProfile());
            preparedStatement.setInt(7, users.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateImageFromSettings(InputStream inputStream) {
        try {
            String sql = "UPDATE Users SET profileImage = ? WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setBlob(1, inputStream);
            preparedStatement.setInt(2, session.getId());
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
            String sql = "SELECT COUNT(*) FROM Users WHERE username = BINARY ?";
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

    public final static JFXDialog.DialogTransition dialogTransition() {
        return JFXDialog.DialogTransition.valueOf(session.getDialogTransition());
    }
    
    public static void insertUserSession(int id) {
        try {
            String sql = "INSERT INTO UserSession (userId) VALUES (?)";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
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
            count = 0;
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getQuotes(java.sql.Date date) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes WHERE requestDate = ?";
            PreparedStatement preparedStatementQuotes = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatementQuotes.setDate(1, new java.sql.Date(date.getTime()));
            
            ResultSet rs = preparedStatementQuotes.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            count = 0;
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
        return count;
    }

    public static int getProducts(java.sql.Date date) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products WHERE insertionDate = ?";
            PreparedStatement preparedStetementProducts = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStetementProducts.setDate(1, new java.sql.Date(date.getTime()));
            
            ResultSet rs = preparedStetementProducts.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            count = 0;
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count; 
    }
    
    public static int getCustomers() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Customers";
            PreparedStatement preparedStatementCustomers = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);

            ResultSet rs = preparedStatementCustomers.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            count = 0;
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getQuotes() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes";
            PreparedStatement preparedStatementQuotes = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            
            ResultSet rs = preparedStatementQuotes.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            count = 0;
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
        return count;
    }

    public static int getProducts() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products";
            PreparedStatement preparedStetementProducts = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            
            ResultSet rs = preparedStetementProducts.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            count = 0;
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count; 
    }
}
