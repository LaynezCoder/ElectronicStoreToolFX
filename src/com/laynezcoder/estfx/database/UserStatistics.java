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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserStatistics {

    public static int getCustomersAdded(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Customers WHERE userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getQuotesAdded(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes WHERE userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getProductsAdded(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products WHERE userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getCustomersAddedFromLastWeek(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Customers WHERE YEARWEEK(insertionDate) = YEARWEEK(NOW() - INTERVAL 1 WEEK) "
                    + "AND userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
                System.out.println(count);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getQuotesAddedFromLastWeek(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes WHERE YEARWEEK(insertionDate) = YEARWEEK(NOW() - INTERVAL 1 WEEK) "
                    + "AND userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getProductsAddedFromLastWeek(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products WHERE YEARWEEK(insertionDate) = YEARWEEK(NOW() - INTERVAL 1 WEEK) "
                    + "AND userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getCustomersAddedFromThisWeek(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Customers WHERE insertionDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) "
                    + "AND NOW() AND userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
                System.out.println(count);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getQuotesAddedFromThisWeek(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes WHERE insertionDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) "
                    + "AND NOW() AND userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static int getProductsAddedFromThisWeek(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Products WHERE insertionDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) "
                    + "AND NOW() AND userId = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static String getInsertionDate(int id) {
        String date = null;
        try {
            String sql = "SELECT insertionDate FROM Users WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                date = result.getDate(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static ResultSet getCustomerHistory(int id) {
        ResultSet result = null;
        try {
            String sql = "SELECT customerName, insertionDate FROM Customers WHERE userId = ? ORDER BY insertionDate DESC LIMIT 50";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            result = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static ResultSet getQuotesHistory(int id) {
        ResultSet result = null;
        try {
            String sql = "SELECT descriptionQuote, insertionDate FROM Quotes WHERE userId = ? ORDER BY insertionDate DESC LIMIT 50";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            result = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int getSessionsStarted(int id) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM UserSession WHERE id = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
}
