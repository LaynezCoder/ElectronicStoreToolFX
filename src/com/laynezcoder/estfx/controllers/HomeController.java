package com.laynezcoder.estfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Quotes;
import com.laynezcoder.estfx.resources.Constants;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class HomeController implements Initializable {

    private ObservableList<Quotes> listQuotes;

    private ObservableList<Quotes> filterQuotes;

    @FXML
    private StackPane stckHome;

    @FXML
    private AnchorPane rootSearchMain;

    @FXML
    private AnchorPane rootHome;

    @FXML
    private TextField txtSearchRecentCustomer;

    @FXML
    private AnchorPane rootWelcome;

    @FXML
    private Text textDescriptionWelcome;

    @FXML
    private Text textWelcome;

    @FXML
    private Text textCustomers;

    @FXML
    private Text texQuotes;

    @FXML
    private Text textProducts;

    @FXML
    private Text textRecentQuotes;

    @FXML
    private Label labelTotalCustomers;

    @FXML
    private Label labelTotalQuotes;

    @FXML
    private Label labelNowQuotes;

    @FXML
    private Label labelTotalProduct;

    @FXML
    private TableView<Quotes> tblQuotes;

    @FXML
    private AnchorPane rootTotalCustomers;

    @FXML
    private AnchorPane rootTotalQuotes;

    @FXML
    private AnchorPane rootRecentQuotes;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private TableColumn<Quotes, Integer> colId;

    @FXML
    private TableColumn<Quotes, String> colDescription;

    @FXML
    private TableColumn<Quotes, String> colPrice;

    @FXML
    private TableColumn<Quotes, String> colDate;

    @FXML
    private TableColumn<Quotes, String> colCustomerName;

    @FXML
    private TableColumn<Quotes, JFXButton> colExistence;

    @FXML
    private TableColumn<Quotes, JFXButton> colRealization;

    @FXML
    private TableColumn<Quotes, JFXButton> colReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animationsNodes();
        customerCounter();
        productsCounter();
        setWelcomeText();
        loadData();
        selectText();
        filterQuotes = FXCollections.observableArrayList();
    }

    private void selectText() {
        TextFieldMask.selectText(txtSearchRecentCustomer);
    }

    private void animationsNodes() {
        Animations.fadeInUp(rootSearchMain);
        Animations.fadeInUp(rootWelcome);
        Animations.fadeInUp(tblQuotes);
        Animations.fadeInUp(rootTotalCustomers);
        Animations.fadeInUp(rootTotalQuotes);
        Animations.fadeInUp(rootRecentQuotes);
        Animations.fadeInUp(rootProducts);
    }

    private void customerCounter() {
        try {
            String sql = "SELECT COUNT(*) FROM Customers";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatetent.executeQuery();

            int total = 0;
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
            labelTotalCustomers.setText(String.valueOf(total));
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void productsCounter() {
        try {
            String sql = "SELECT COUNT(*) FROM Products";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatetent.executeQuery();

            int total = 0;
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
            labelTotalProduct.setText(String.valueOf(total));
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setWelcomeText() {
        try {
            String sql = "SELECT COUNT(*) FROM Quotes";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            String sql2 = "SELECT Users.nameUser FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatementTwo = DatabaseConnection.getInstance().getConnection().prepareStatement(sql2);
            ResultSet resultSetTwo = preparedStatementTwo.executeQuery();

            int total = 0;
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
            labelTotalQuotes.setText(String.valueOf(total));

            while (resultSetTwo.next()) {
                String name = resultSetTwo.getString("nameUser");
                switch (total) {
                    case 10:
                        setText(name, 10);
                        break;
                    case 20:
                        setText(name, 20);
                        break;
                    case 30:
                        setText(name, 30);
                        break;
                    case 50:
                        setText(name, 50);
                        break;
                    case 100:
                        setText(name, 100);
                        break;
                    case 500:
                        setText(name, 500);
                        break;
                    default:
                        textWelcome.setText("¡Welcome back, " + name + "!");
                        textDescriptionWelcome.setText("¿What do you think if you start adding a new client?");
                        break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setText(String name, int total) {
        textWelcome.setText("¡Congratulations " + name + ", " + total + " new quotes have been registered!");
        textDescriptionWelcome.setText("!Nice job!");
    }

    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionQuote"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colExistence.setCellValueFactory(new JFXButtonExistsCellValueFactory());
        colRealization.setCellValueFactory(new JFXButtonRealizedCellValueFactory());
        colReport.setCellValueFactory(new JFXButtonReportCellValueFactory());
    }

    private void loadTable() {
        ArrayList<Quotes> list = new ArrayList<>();
        try {
            int total = 0;
            String sql = "SELECT q.id, q.descriptionQuote, q.requestDate, q.price, q.existence, q.realization, q.report, c.customerName\nFROM Quotes AS q\n"
                    + "INNER JOIN Customers AS c ON q.customerId = c.id WHERE requestDate = DATE(NOW())";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String descriptionQuote = resultSet.getString("descriptionQuote");
                Date requestDate = resultSet.getDate("requestDate");
                Double price = resultSet.getDouble("price");
                String existence = resultSet.getString("existence");
                String realization = resultSet.getString("realization");
                String report = resultSet.getString("report");
                String customerName = resultSet.getString("customerName");
                list.add(new Quotes(id, descriptionQuote, requestDate, price, existence, realization, report, customerName));
                total++;
            }
            labelNowQuotes.setText(String.valueOf(total));
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckHome, rootHome, rootHome, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
        listQuotes = FXCollections.observableArrayList(list);
        tblQuotes.setItems(listQuotes);
    }

    @FXML
    private void filterQuotes() {
        String filter = txtSearchRecentCustomer.getText().trim();
        if (filter.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            filterQuotes.clear();
            for (Quotes q : listQuotes) {
                if (q.getDescriptionQuote().toLowerCase().contains(filter.toLowerCase())) {
                    filterQuotes.add(q);
                }
            }
            tblQuotes.setItems(filterQuotes);
        }
    }

    private class JFXButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getExistence());
            button.getStylesheets().add(Constants.LIGHT_THEME);
            button.setPrefWidth(colExistence.getWidth() / 0.5);

            if (item.getExistence().equals(Constants.EXISTENT)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getReport());
            button.getStylesheets().add(Constants.LIGHT_THEME);
            button.setPrefWidth(colReport.getWidth() / 0.5);

            if (item.getReport().equals(Constants.REPORTED)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getRealization());
            button.getStylesheets().add(Constants.LIGHT_THEME);
            button.setPrefWidth(colRealization.getWidth() / 0.5);

            if (item.getRealization().equals(Constants.REALIZED)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
