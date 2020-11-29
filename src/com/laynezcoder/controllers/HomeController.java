package com.laynezcoder.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.models.Quotes;
import com.laynezcoder.resources.Resources;
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
import javafx.scene.image.ImageView;
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

    @FXML
    private AnchorPane rootCongrulations;

    @FXML
    private Text textCongrulations;

    @FXML
    private Text subTextCongrulations;

    @FXML
    private ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animationsNodes();
        customerCounter();
        quoteCounter();
        productsCounter();
        setTextWelcomeQuotes();
        loadData();
        setFonts();
        selectText();
        filterQuotes = FXCollections.observableArrayList();
    }

    //The congratulations dialog is optional
    private void dialogCongrulations(String text, String subtext) {
        JFXDialog dialog = new JFXDialog(stckHome, rootCongrulations, JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
        Resources.styleAlert(dialog);
        rootCongrulations.setVisible(true);
        dialog.show();

        textCongrulations.setText(text);
        subTextCongrulations.setText(subtext);

        dialog.setOnDialogClosed(ev -> {
            rootCongrulations.setVisible(false);
        });
    }

    private void selectText() {
        Resources.selectTextToTextField(txtSearchRecentCustomer);
    }

    private void setFonts() {
        Resources.setFontToText(textWelcome, 25);
        Resources.setFontToText(textDescriptionWelcome, 15);
        Resources.setFontToText(textCustomers, 15);
        Resources.setFontToText(texQuotes, 15);
        Resources.setFontToText(textProducts, 15);
        Resources.setFontToText(textRecentQuotes, 15);
        Resources.setFontToText(textCongrulations, 15);
        Resources.setFontToText(subTextCongrulations, 15);
    }

    private void animationsNodes() {
        Resources.fadeInUpAnimation(rootSearchMain);
        Resources.fadeInUpAnimation(rootWelcome);
        Resources.fadeInUpAnimation(tblQuotes);
        Resources.fadeInUpAnimation(rootTotalCustomers);
        Resources.fadeInUpAnimation(rootTotalQuotes);
        Resources.fadeInUpAnimation(rootRecentQuotes);
        Resources.fadeInUpAnimation(rootProducts);
    }

    private void customerCounter() {
        try {
            String sql = "SELECT * FROM Customers";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatetent.executeQuery();
            int total = 0;
            while (resultSet.next()) {
                total++;
            }
            if (total == 0) {
                labelTotalCustomers.setText("0");
            } else {
                labelTotalCustomers.setText(String.valueOf(total));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void quoteCounter() {
        try {
            String sql = "SELECT * FROM Quotes";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatetent.executeQuery();
            int total = 0;
            while (resultSet.next()) {
                total++;
            }
            if (total == 0) {
                labelTotalQuotes.setText("0");
            } else {
                labelTotalQuotes.setText(String.valueOf(total));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void productsCounter() {
        try {
            String sql = "SELECT * FROM Products";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatetent.executeQuery();
            int total = 0;
            while (resultSet.next()) {
                total++;
            }
            if (total == 0) {
                labelTotalProduct.setText("0");
            } else {
                labelTotalProduct.setText(String.valueOf(total));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTextWelcomeQuotes() {
        try {
            String sql = "SELECT * FROM Quotes";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            String sql2 = "SELECT Users.nameUser FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatementTwo = DatabaseConnection.getInstance().getConnection().prepareStatement(sql2);
            ResultSet resultSetTwo = preparedStatementTwo.executeQuery();

            int total = 0;
            while (resultSet.next()) {
                total++;
            }

            while (resultSetTwo.next()) {
                String name = resultSetTwo.getString("nameUser");
                switch (total) {
                    case 10:
                        textWelcome.setText("¡Congratulations " + name + ", 10 new quotes have been registered!");
                        textDescriptionWelcome.setText("!Nice job!");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                    case 20:
                        textWelcome.setText("¡Congratulations " + name + ", 20 new quotes have been registered!");
                        textDescriptionWelcome.setText("!Nice job!");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                    case 30:
                        textWelcome.setText("¡Congratulations " + name + ", 30 new quotes have been registered!");
                        textDescriptionWelcome.setText("!Nice job!");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                    case 50:
                        textWelcome.setText("¡Congratulations " + name + ", 40 new quotes have been registered!");
                        textDescriptionWelcome.setText("!Nice job!");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                    case 100:
                        textWelcome.setText("¡Congratulations " + name + ", 100 new quotes have been registered!");
                        textDescriptionWelcome.setText("!Nice job!");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                    case 500:
                        textWelcome.setText("¡Congratulations " + name + ", 500 new quotes have been registered!");
                        textDescriptionWelcome.setText("!Nice job!");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                    default:
                        textWelcome.setText("¡Welcome back, " + name + "!");
                        textDescriptionWelcome.setText("¿What do you think if you start adding a new client?");
                        //dialogCongrulations(textWelcome.getText(), textDescriptionWelcome.getText());
                        break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            String sql = "SELECT q.id, q.descriptionQuote, q.requestDate, q.price, q.existence, q.realization, q.report, c.customerName\n"
                    + "FROM Quotes AS q\n"
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
                String customeName = resultSet.getString("customerName");
                list.add(new Quotes(id, descriptionQuote, requestDate, price, existence, realization, report, customeName));
                total++;
            }

            if (total == 0) {
                labelNowQuotes.setText("0");
            } else {
                labelNowQuotes.setText(String.valueOf(total));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listQuotes = FXCollections.observableArrayList(list);
        tblQuotes.setItems(listQuotes);
    }

    @FXML
    private void filterQuotes() {
        String filter = txtSearchRecentCustomer.getText();
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

            JFXButton button = new JFXButton();
            button.setText(item.getExistence());
            button.getStylesheets().add((Quotes.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            button.setPrefWidth(colExistence.getWidth() / 0.5);

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            if (item.getExistence().equals("Existent")) {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));
                button.getStyleClass().addAll("cell-button-exists", "table-row-cell");
                button.setGraphic(icon);
            } else {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CLOSE));
                button.getStyleClass().addAll("cell-button-not-exists", "table-row-cell");
                button.setGraphic(icon);
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            JFXButton button = new JFXButton();
            button.setText(item.getReport());
            button.getStylesheets().add((Quotes.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            button.setPrefWidth(colReport.getWidth() / 0.5);

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            if (item.getReport().equals("Reported")) {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));
                button.getStyleClass().addAll("cell-button-exists", "table-row-cell");
                button.setGraphic(icon);
            } else {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CLOSE));
                button.getStyleClass().addAll("cell-button-not-exists", "table-row-cell");
                button.setGraphic(icon);
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            JFXButton button = new JFXButton();
            button.setText(item.getRealization());
            button.getStylesheets().add((Quotes.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            button.setPrefWidth(colRealization.getWidth() / 0.5);

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            if (item.getRealization().equals("Realized")) {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));
                button.getStyleClass().addAll("cell-button-exists", "table-row-cell");
                button.setGraphic(icon);
            } else {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CLOSE));
                button.getStyleClass().addAll("cell-button-not-exists", "table-row-cell");
                button.setGraphic(icon);
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
