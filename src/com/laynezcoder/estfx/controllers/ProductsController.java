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
package com.laynezcoder.estfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Products;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.preferences.Preferences;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.util.ContextMenu;
import com.laynezcoder.estfx.util.JFXDialogTool;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProductsController implements Initializable {

    private final ColorAdjust colorAdjust = new ColorAdjust();

    private final long LIMIT = 1000000;

    private final String ALREADY_EXISTS = "There is already a product with this barcode";

    private final String IS_GREATER = "Minimum price cannot be higher than sale price";

    private ObservableList<Products> listProducts;

    private ObservableList<Products> filterProducts;

    @FXML
    private StackPane stckProducts;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private AnchorPane containerDeleteProducts;

    @FXML
    private HBox hBoxSearch;

    @FXML
    private TextField txtSearchProduct;

    @FXML
    private TextField txtSearchBarCode;

    @FXML
    private JFXButton btnNewProduct;

    @FXML
    private TableView<Products> tblProducts;

    @FXML
    private TableColumn<Products, Integer> colId;

    @FXML
    private TableColumn<Products, Integer> colBarcode;

    @FXML
    private TableColumn<Products, String> colName;

    @FXML
    private TableColumn<Products, String> colDescription;

    @FXML
    private TableColumn<Products, Double> colPurchasePrice;

    @FXML
    private TableColumn<Products, Double> colSalePrice;

    @FXML
    private TableColumn<Products, Integer> colPorcentage;

    @FXML
    private TableColumn<Products, Double> colMinimalPrice;

    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private JFXTextField txtBarCode;

    @FXML
    private JFXTextField txtNameProduct;

    @FXML
    private JFXTextField txtPurchasePrice;

    @FXML
    private Text textAddProduct;

    @FXML
    private Text textPurchase;

    @FXML
    private Text textPorcentage;

    @FXML
    private JFXTextField txtSalePrice;

    @FXML
    private JFXTextArea txtDescriptionProduct;

    @FXML
    private JFXButton btnUpdateProduct;

    @FXML
    private JFXButton btnSaveProduct;

    @FXML
    private JFXButton btnCancelAddProduct;

    @FXML
    private JFXTextField txtPorcentage;

    @FXML
    private JFXTextField txtMinPrice;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private HBox imageContainer;

    private JFXDialogTool dialogAddProduct;

    private JFXDialogTool dialogDeleteProduct;

    private static final Stage stage = new Stage();

    private File imageFile;

    private ContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        setMask();
        animateNodes();
        selectText();
        validateUser();
        characterLimiter();
        initializeImage();
        setTextIfFieldIsEmpty();
        closeDialogWithTextFields();
        closeDialogWithEscapeKey();
    }

    private void setContextMenu() {
        contextMenu = new ContextMenu(tblProducts);

        contextMenu.setActionEdit(ev -> {
            showDialogEditProduct();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteProduct();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsProduct();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void initializeImage() {
        imageContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                colorAdjust.setBrightness(0.25);
                imageProduct.setEffect(colorAdjust);
            } else {
                imageProduct.setEffect(null);
            }
        });

        imageContainer.setPadding(new Insets(5));
        filterProducts = FXCollections.observableArrayList();
        imageProduct.setFitHeight(imageContainer.getPrefHeight() - 10);
        imageProduct.setFitWidth(imageContainer.getPrefWidth() - 10);
    }

    private void animateNodes() {
        Animations.fadeInUp(btnNewProduct);
        Animations.fadeInUp(tblProducts);
        Animations.fadeInUp(hBoxSearch);
    }

    private void setMask() {
        TextFieldMask.onlyDoubleNumbers10Integers(txtSalePrice);
        TextFieldMask.onlyDoubleNumbers5Integers(txtPurchasePrice);
        TextFieldMask.onlyDoubleNumbers5Integers(txtMinPrice);
        TextFieldMask.onlyNumbers(txtBarCode);
        TextFieldMask.onlyNumbers(txtSearchBarCode);
        TextFieldMask.onlyNumbers(txtPorcentage);
    }

    private void selectText() {
        TextFieldMask.selectText(txtNameProduct);
        TextFieldMask.selectText(txtSalePrice);
        TextFieldMask.selectText(txtMinPrice);
        TextFieldMask.selectText(txtBarCode);
        TextFieldMask.selectText(txtPorcentage);
        TextFieldMask.selectText(txtPurchasePrice);
        TextFieldMask.selectTextToJFXTextArea(txtDescriptionProduct);
    }

    private void setTextIfFieldIsEmpty() {
        TextFieldMask.setTextIfFieldIsEmpty(txtPurchasePrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtMinPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtPorcentage);
        TextFieldMask.setTextIfFieldIsEmpty(txtSalePrice);
    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtBarCode, 20);
        TextFieldMask.characterLimit(txtPorcentage, 3);
    }

    @FXML
    private void showDialogAddProduct() {
        resetValidation();
        calculateSalePrice();
        enableEditControls();
        disableTable();
        rootProducts.setEffect(Constants.BOX_BLUR_EFFECT);

        textAddProduct.setText("Add Product");
        imageContainer.toFront();
        containerAddProduct.setVisible(true);
        btnSaveProduct.setDisable(false);
        btnUpdateProduct.setVisible(true);
        btnSaveProduct.toFront();

        dialogAddProduct = new JFXDialogTool(containerAddProduct, stckProducts);
        dialogAddProduct.show();

        dialogAddProduct.setOnDialogOpened(ev -> {
            txtBarCode.requestFocus();
        });

        dialogAddProduct.setOnDialogClosed(ev -> {
            closeStage();
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerAddProduct.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddProduct() {
        if (dialogAddProduct != null) {
            dialogAddProduct.close();
        }
    }

    @FXML
    private void showDialogDeleteProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        rootProducts.setEffect(Constants.BOX_BLUR_EFFECT);
        containerDeleteProducts.setVisible(true);
        disableTable();

        dialogDeleteProduct = new JFXDialogTool(containerDeleteProducts, stckProducts);
        dialogDeleteProduct.show();

        dialogDeleteProduct.setOnDialogClosed(ev -> {
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerDeleteProducts.setVisible(false);
            cleanControls();
        });

    }

    @FXML
    private void hideDialogDeleteProduct() {
        if (dialogDeleteProduct != null) {
            dialogDeleteProduct.close();
        }
    }

    @FXML
    private void showDialogEditProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
        btnUpdateProduct.toFront();
        textAddProduct.setText("Update product");
        selectedRecord();

    }

    @FXML
    private void showDialogDetailsProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
        textAddProduct.setText("Product details");
        selectedRecord();
        paneContainer.toFront();
        btnUpdateProduct.setVisible(false);
        btnSaveProduct.setDisable(true);
        btnSaveProduct.toFront();
        disableEditControls();
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionProduct"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colPorcentage.setCellValueFactory(new PropertyValueFactory<>("porcentage"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colMinimalPrice.setCellValueFactory(new PropertyValueFactory<>("minimalPrice"));
    }

    private void loadTable() {
        ArrayList<Products> list = new ArrayList<>();
        try {
            String sql = "SELECT id, barcode, productName, purchasePrice, porcentage, salePrice, minimalPrice, descriptionProduct FROM Products";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String barcode = resultSet.getString("barcode");
                String productName = resultSet.getString("productName");
                Double purchasePrice = resultSet.getDouble("purchasePrice");
                int porcentage = resultSet.getInt("porcentage");
                Double salePrice = resultSet.getDouble("salePrice");
                Double minimalPrice = resultSet.getDouble("minimalPrice");
                String descriptionProduct = resultSet.getString("descriptionProduct");
                list.add(new Products(id, barcode, productName, purchasePrice, porcentage, salePrice, minimalPrice, descriptionProduct));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
        listProducts = FXCollections.observableArrayList(list);
        tblProducts.setItems(listProducts);
        tblProducts.setFixedCellSize(30);
    }

    private void selectedRecord() {
        Products products = tblProducts.getSelectionModel().getSelectedItem();
        txtBarCode.setText(String.valueOf(products.getBarcode()));
        txtNameProduct.setText(products.getProductName());
        txtPurchasePrice.setText(String.valueOf(products.getPurchasePrice()));
        txtPorcentage.setText(String.valueOf(products.getPorcentage()));
        txtSalePrice.setText(String.valueOf(products.getSalePrice()));
        txtDescriptionProduct.setText(products.getDescriptionProduct());
        txtMinPrice.setText(String.valueOf(products.getMinimalPrice()));
        imageProduct.setImage(getImage(products.getId()));
        expandImage(products.getId(), products.getProductName());
    }

    @FXML
    private void newProduct() {
        String barcode = txtBarCode.getText().trim();
        String productName = txtNameProduct.getText().trim();
        String purchasePrice = txtPurchasePrice.getText().trim();
        String porcentage = txtPorcentage.getText().trim();
        String salePrice = txtSalePrice.getText().trim();
        String minPrice = txtMinPrice.getText().trim();
        String description = txtDescriptionProduct.getText().trim();

        if (barcode.isEmpty()) {
            txtBarCode.requestFocus();
            Animations.shake(txtBarCode);
            return;
        }

        if (DatabaseHelper.checkIfProductExists(barcode) != 0) {
            txtBarCode.requestFocus();
            NotificationsBuilder.create(NotificationType.ERROR, ALREADY_EXISTS);
            return;
        }

        if (productName.isEmpty()) {
            txtNameProduct.requestFocus();
            Animations.shake(txtNameProduct);
            return;
        }

        if (purchasePrice.isEmpty()) {
            txtPurchasePrice.requestFocus();
            Animations.shake(txtPurchasePrice);
            return;
        }

        if (porcentage.isEmpty()) {
            txtPorcentage.requestFocus();
            Animations.shake(txtPorcentage);
            return;
        }

        if (salePrice.isEmpty()) {
            txtSalePrice.requestFocus();
            Animations.shake(txtSalePrice);
            return;
        }

        if (minPrice.isEmpty()) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            return;
        }

        if (Double.parseDouble(minPrice) > Double.parseDouble(salePrice)) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            NotificationsBuilder.create(NotificationType.ERROR, IS_GREATER);
            return;
        }

        if (description.isEmpty()) {
            txtDescriptionProduct.requestFocus();
            Animations.shake(txtDescriptionProduct);
            return;
        }

        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
            return;
        }

        Products products = new Products();
        products.setBarcode(barcode);
        products.setProductName(productName);
        products.setDescriptionProduct(description);
        products.setPurchasePrice(Double.parseDouble(purchasePrice));
        products.setPorcentage(Integer.valueOf(porcentage));
        products.setSalePrice(Double.parseDouble(salePrice));
        products.setMinimalPrice(Double.parseDouble(minPrice));
        products.setProductImage(getInputStream());

        boolean result = DatabaseHelper.insertNewProduct(products, listProducts);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAddProduct();
            AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_ADDED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    private InputStream getInputStream() {
        InputStream is;
        try {
            if (imageFile != null) {
                is = new FileInputStream(imageFile);
            } else {
                is = ProductsController.class.getResourceAsStream(Constants.NO_IMAGE_AVAILABLE);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.INFORMATION, Constants.MESSAGE_IMAGE_NOT_FOUND);
            is = ProductsController.class.getResourceAsStream(Constants.NO_IMAGE_AVAILABLE);
        }
        return is;
    }

    private Image getImage(int id) {
        Image image = null;
        try {
            String sql = "SELECT imageProduct FROM Products WHERE id = ?";
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InputStream img = rs.getBinaryStream("imageProduct");
                if (img != null) {
                    image = new Image(img, 200, 200, true, true);
                } else {
                    image = new Image(Constants.NO_IMAGE_AVAILABLE, 200, 200, true, true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    @FXML
    private void updateProduct() {
        String barcode = txtBarCode.getText().trim();
        String productName = txtNameProduct.getText().trim();
        String purchasePrice = txtPurchasePrice.getText().trim();
        String porcentage = txtPorcentage.getText().trim();
        String salePrice = txtSalePrice.getText().trim();
        String minPrice = txtMinPrice.getText().trim();
        String description = txtDescriptionProduct.getText().trim();
        String barcodeFromTable = tblProducts.getSelectionModel().getSelectedItem().getBarcode();

        if (barcode.isEmpty()) {
            txtBarCode.requestFocus();
            Animations.shake(txtBarCode);
            return;
        }

        if (DatabaseHelper.checkIfProductExists(barcode) != 0 && !barcodeFromTable.equals(barcode)) {
            txtBarCode.requestFocus();
            Animations.shake(txtBarCode);
            NotificationsBuilder.create(NotificationType.ERROR, ALREADY_EXISTS);
            return;
        }

        if (productName.isEmpty()) {
            txtNameProduct.requestFocus();
            Animations.shake(txtNameProduct);
            return;
        }

        if (purchasePrice.isEmpty()) {
            txtPurchasePrice.requestFocus();
            Animations.shake(txtPurchasePrice);
            return;
        }

        if (porcentage.isEmpty()) {
            txtPorcentage.requestFocus();
            Animations.shake(txtPorcentage);
            return;
        }

        if (salePrice.isEmpty()) {
            txtSalePrice.requestFocus();
            Animations.shake(txtSalePrice);
            return;
        }

        if (minPrice.isEmpty()) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            return;
        }

        if (Double.parseDouble(minPrice) > Double.parseDouble(salePrice)) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            NotificationsBuilder.create(NotificationType.ERROR, IS_GREATER);
            return;
        }

        if (description.isEmpty()) {
            txtDescriptionProduct.requestFocus();
            Animations.shake(txtDescriptionProduct);
            return;
        }

        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
            return;
        }

        Products products = tblProducts.getSelectionModel().getSelectedItem();
        products.setId(products.getId());
        products.setBarcode(barcode);
        products.setProductName(productName);
        products.setDescriptionProduct(description);
        products.setPurchasePrice(Double.parseDouble(purchasePrice));
        products.setPorcentage(Integer.valueOf(porcentage));
        products.setSalePrice(Double.parseDouble(salePrice));
        products.setMinimalPrice(Double.parseDouble(minPrice));
        products.setProductImage(getInputStream());

        if (imageFile != null) {
            boolean result = DatabaseHelper.updateProduct(products);
            if (result) {
                closeDialogAddProduct();
                loadData();
                cleanControls();
                AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_UPDATED);
            } else {
                NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
            }
        } else {
            boolean result = DatabaseHelper.updateProductIfFileIsNull(products);
            if (result) {
                closeDialogAddProduct();
                loadData();
                cleanControls();
                AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_UPDATED);
            } else {
                NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
            }
        }
    }

    @FXML
    private void deleteProducts() {
        boolean result = DatabaseHelper.deleteProduct(tblProducts, listProducts);
        if (result) {
            loadData();
            cleanControls();
            hideDialogDeleteProduct();
            AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_DELETED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    private void cleanControls() {
        imageFile = null;
        txtPurchasePrice.clear();
        txtMinPrice.clear();
        txtBarCode.clear();
        txtNameProduct.clear();
        txtPorcentage.clear();
        txtSalePrice.clear();
        txtDescriptionProduct.clear();
        imageProduct.setImage(new Image(Constants.NO_IMAGE_AVAILABLE));
    }

    private void disableEditControls() {
        txtBarCode.setEditable(false);
        txtDescriptionProduct.setEditable(false);
        txtNameProduct.setEditable(false);
        txtPurchasePrice.setEditable(false);
        txtSalePrice.setEditable(false);
        txtPorcentage.setEditable(false);
        txtMinPrice.setEditable(false);
    }

    private void enableEditControls() {
        txtBarCode.setEditable(true);
        txtNameProduct.setEditable(true);
        txtSalePrice.setEditable(true);
        txtMinPrice.setEditable(true);
        txtPorcentage.setEditable(true);
        txtPurchasePrice.setEditable(true);
        txtDescriptionProduct.setEditable(true);
    }

    private void disableTable() {
        tblProducts.setDisable(true);
    }

    private void resetValidation() {
        txtBarCode.resetValidation();
        txtPorcentage.resetValidation();
        txtSalePrice.resetValidation();
        txtMinPrice.resetValidation();
        txtNameProduct.resetValidation();
        txtPurchasePrice.resetValidation();
        txtDescriptionProduct.resetValidation();
    }

    private void validateUser() {
        setContextMenu();
        if (DatabaseHelper.getUserType().equals("Administrator")) {
            deleteUserDeleteKey();
            
            colPorcentage.setVisible(true);
            colPurchasePrice.setVisible(true);
            btnNewProduct.setDisable(false);
            txtPurchasePrice.setVisible(true);
            txtPorcentage.setVisible(true);
            textPurchase.setVisible(false);
            textPorcentage.setVisible(false);
            setEnableMenuItem();
        } else {
            colPorcentage.setVisible(false);
            colPurchasePrice.setVisible(false);
            btnNewProduct.setDisable(true);
            txtPurchasePrice.setVisible(false);
            textPurchase.setVisible(true);
            textPorcentage.setVisible(true);
            txtPorcentage.setVisible(false);
            setDisableMenuItem();
        }
    }

    private void setDisableMenuItem() {
        contextMenu.getEditButton().setDisable(true);
        contextMenu.getDeleteButton().setDisable(true);
    }

    private void setEnableMenuItem() {
        contextMenu.getEditButton().setDisable(false);
        contextMenu.getDeleteButton().setDisable(false);
    }

    private void closeDialogWithEscapeKey() {
        rootProducts.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                hideDialogDeleteProduct();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblProducts.setDisable(false);
                rootProducts.setEffect(null);
                AlertsBuilder.close();
            }

        });
    }

    private void closeDialogWithTextFields() {
        txtBarCode.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtNameProduct.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtPurchasePrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtSalePrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtDescriptionProduct.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtPorcentage.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtMinPrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
                tblProducts.setDisable(false);
            }
        });
    }

    public static void closeStage() {
        if (stage != null) {
            stage.hide();
        }
    }

    private void deleteUserDeleteKey() {
        rootProducts.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblProducts.isDisable()) {
                    return;
                }

                if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
                    return;
                }

                deleteProducts();
            }
        });
    }

    @FXML
    private void filterNameProduct() {
        String filterName = txtSearchProduct.getText().trim();
        if (filterName.isEmpty()) {
            tblProducts.setItems(listProducts);
        } else {
            filterProducts.clear();
            for (Products p : listProducts) {
                if (p.getProductName().toLowerCase().contains(filterName.toLowerCase())) {
                    filterProducts.add(p);
                }
            }
            tblProducts.setItems(filterProducts);
        }
    }

    @FXML
    private void filterCodeBar() {
        String filterCodeBar = txtSearchBarCode.getText().trim();
        if (filterCodeBar.isEmpty()) {
            tblProducts.setItems(listProducts);
        } else {
            filterProducts.clear();
            for (Products p : listProducts) {
                if (p.getBarcode().toLowerCase().contains(filterCodeBar.toLowerCase())) {
                    filterProducts.add(p);
                }
            }
            tblProducts.setItems(filterProducts);
        }
    }

    private void calculateSalePrice() {
        txtPurchasePrice.setOnKeyReleased(ev -> {
            if (txtPurchasePrice.getText().isEmpty()) {
                txtPurchasePrice.setText("0");
            }

            if (txtPurchasePrice.isFocused() && txtPurchasePrice.getText().equals("0")) {
                txtPurchasePrice.selectAll();
            }

            if (txtPorcentage.getText().isEmpty()) {
                txtPorcentage.setText("0");
            }

            double purchasePrice = Double.valueOf(txtPurchasePrice.getText());
            int porcentage = Integer.parseInt(txtPorcentage.getText());
            double salePrice = ((purchasePrice * porcentage) / 100) + purchasePrice;
            txtSalePrice.setText(String.valueOf(salePrice));
        });

        txtPorcentage.setOnKeyReleased(ev -> {
            if (txtPorcentage.isFocused() && txtPorcentage.getText().isEmpty()) {
                txtPorcentage.setText("0");
            }

            if (txtPorcentage.isFocused() && txtPorcentage.getText().equals("0")) {
                txtPorcentage.selectAll();
            }

            if (txtPurchasePrice.getText().isEmpty()) {
                txtPurchasePrice.setText("0");
            }

            double purchasePrice = Double.valueOf(txtPurchasePrice.getText());
            int porcentage = Integer.parseInt(txtPorcentage.getText());
            double salePrice = ((purchasePrice * porcentage) / 100) + purchasePrice;
            txtSalePrice.setText(String.valueOf(salePrice));
        });
    }

    @FXML
    private void showFileChooser() {
        imageFile = getImageFromFileChooser(getStage());
        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString(), 200, 200, true, true);
            imageProduct.setImage(image);
            setInitialDirectory();
        }
    }

    private Stage getStage() {
        return (Stage) btnCancelAddProduct.getScene().getWindow();
    }

    private void setInitialDirectory() {
        Preferences preferences = Preferences.getPreferences();
        preferences.setInitialPathFileChooserProductsController(imageFile.getParent());
        Preferences.writePreferencesToFile(preferences);
    }

    private File getImageFromFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterImages = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(extFilterImages);
        fileChooser.setInitialDirectory(getInitialDirectoy());
        fileChooser.setTitle("Select an image");

        File selectedImage = fileChooser.showOpenDialog(stage);
        return selectedImage;
    }

    private File getInitialDirectoy() {
        Preferences preferences = Preferences.getPreferences();
        File initPath = new File(preferences.getInitialPathFileChooserProductsController());
        if (!initPath.exists()) {
            preferences.setInitialPathFileChooserProductsController(System.getProperty("user.home"));
            Preferences.writePreferencesToFile(preferences);
            initPath = new File(preferences.getInitialPathFileChooserProductsController());
        }
        return initPath;
    }

    private void expandImage(int id, String title) {
        paneContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                colorAdjust.setBrightness(0.25);
                imageProduct.setEffect(colorAdjust);
            } else {
                imageProduct.setEffect(null);
            }
        });

        paneContainer.setOnMouseClicked(ev -> {
            final Image image = DatabaseHelper.getProductImage(id);
            final ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(550);

            final BorderPane boderPane = new BorderPane(imageView);
            boderPane.setStyle("-fx-background-color: white");
            boderPane.setCenter(imageView);

            final ScrollPane root = new ScrollPane(boderPane);
            root.setStyle("-fx-background-color: white");
            root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            root.getStylesheets().add(Constants.LIGHT_THEME);
            root.getStyleClass().add("scroll-bar");

            root.setFitToHeight(true);
            root.setFitToWidth(true);

            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.setScene(new Scene(root, 550, 555));
            stage.setTitle(title);
            stage.show();
        });
    }
}
