package com.laynezcoder.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.models.Products;
import com.laynezcoder.resources.Resources;
import static com.laynezcoder.resources.Resources.jfxDialog;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.ESCAPE;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProductsController implements Initializable {

    private final String NO_IMAGE_AVAILABLE = "/com/laynezcoder/media/empty-image.jpg";

    private ObservableList<Products> listProducts;

    private ObservableList<Products> filterProducts;

    @FXML
    private BorderPane imageContainer;

    @FXML
    private StackPane stckProducts;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private AnchorPane rootDeleteProducts;

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
    private AnchorPane rootAddProduct;

    @FXML
    private JFXTextField txtBarCode;

    @FXML
    private JFXTextField txtNameProduct;

    @FXML
    private JFXTextField txtPurchasePrice;

    @FXML
    private Text textAddProduct;

    @FXML
    private Text titleWindowDeleteProducts;

    @FXML
    private Text descriptionWindowDeleteProduct;

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
    private JFXButton btnCancelDelete;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancelAddProduct;

    @FXML
    private JFXTextField txtPorcentage;

    @FXML
    private JFXTextField txtMinPrice;

    @FXML
    private MenuItem menuEdit;

    @FXML
    private MenuItem menuDelete;

    @FXML
    private MenuItem menuDetails;

    @FXML
    private MenuItem menuLoad;

    @FXML
    private ImageView imageProduct;

    private JFXDialog dialogAddProduct;

    private JFXDialog dialogDeleteProduct;

    private final BoxBlur blur = new BoxBlur(3, 3, 3);

    private File imageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        setFonts();
        animateNodes();
        validateUser();
        selectText();
        setValidations();
        characterLimiter();
        keyEscapeWindows();
        escapeWindowWithTextFields();
        imageContainer.setPadding(new Insets(5));
        filterProducts = FXCollections.observableArrayList();
        imageProduct.setFitHeight(imageContainer.getPrefHeight() - 10);
        imageProduct.setFitWidth(imageContainer.getPrefWidth() - 10);
    }                  

    private void setFonts() {
        Resources.setFontToJFXButton(btnCancelAddProduct, 15);
        Resources.setFontToJFXButton(btnUpdateProduct, 15);
        Resources.setFontToJFXButton(btnCancelDelete, 15);
        Resources.setFontToJFXButton(btnSaveProduct, 15);
        Resources.setFontToJFXButton(btnNewProduct, 12);
        Resources.setFontToJFXButton(btnDelete, 15);

        Resources.setFontToText(descriptionWindowDeleteProduct, 12);
        Resources.setFontToText(titleWindowDeleteProducts, 15);
        Resources.setFontToText(textAddProduct, 20);
        Resources.setFontToText(textPorcentage, 13);
        Resources.setFontToText(textPurchase, 13);
    }

    private void animateNodes() {
        Resources.fadeInUpAnimation(btnNewProduct);
        Resources.fadeInUpAnimation(tblProducts);
        Resources.fadeInUpAnimation(hBoxSearch);
    }

    private void setValidations() {
        emptyText();

        Resources.validationOfJFXTextArea(txtDescriptionProduct);

        Resources.doubleNumbersValidationTextField(txtPurchasePrice);
        Resources.doubleNumbersValidation(txtSalePrice, 10, 2);
        Resources.doubleNumbersValidationTextField(txtMinPrice);

        Resources.validationOfJFXTextField(txtPurchasePrice);
        Resources.validationOfJFXTextField(txtNameProduct);
        Resources.validationOfJFXTextField(txtPorcentage);
        Resources.validationOfJFXTextField(txtSalePrice);
        Resources.validationOfJFXTextField(txtMinPrice);
        Resources.validationOfJFXTextField(txtBarCode);

    }

    private void selectText() {
        Resources.selectTextToJFXTextArea(txtDescriptionProduct);
        Resources.selectTextToJFXTextField(txtPurchasePrice);
        Resources.selectTextToJFXTextField(txtNameProduct);
        Resources.selectTextToJFXTextField(txtSalePrice);
        Resources.selectTextToJFXTextField(txtPorcentage);
        Resources.selectTextToJFXTextField(txtMinPrice);
        Resources.selectTextToJFXTextField(txtBarCode);
    }

    @FXML
    private void showWindowAddProduct() {
        resetValidation();
        calculateSalePrice();
        enableControlsEdit();
        rootProducts.setEffect(blur);
        disableTable();

        textAddProduct.setText("Add Product");
        rootAddProduct.setVisible(true);
        btnSaveProduct.setDisable(false);
        btnUpdateProduct.setVisible(true);
        btnSaveProduct.toFront();

        dialogAddProduct = new JFXDialog();
        dialogAddProduct.setTransitionType(JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
        dialogAddProduct.setBackground(Background.EMPTY);
        dialogAddProduct.setDialogContainer(stckProducts);
        dialogAddProduct.setContent(rootAddProduct);
        Resources.styleAlert(dialogAddProduct);
        dialogAddProduct.show();

        dialogAddProduct.setOnDialogOpened(ev -> {
            txtBarCode.requestFocus();
        });

        dialogAddProduct.setOnDialogClosed(ev -> {
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            rootAddProduct.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void hideWindowAddProduct() {
        dialogAddProduct.close();
    }

    @FXML
    private void showWindowDeleteProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.showErrorAlert(stckProducts, rootProducts, tblProducts, "Select an item from the table");
        } else {
            rootProducts.setEffect(blur);
            disableTable();
            dialogDeleteProduct = new JFXDialog();
            dialogDeleteProduct.setTransitionType(JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
            dialogDeleteProduct.setBackground(Background.EMPTY);
            dialogDeleteProduct.setDialogContainer(stckProducts);
            dialogDeleteProduct.setContent(rootDeleteProducts);
            Resources.styleAlert(dialogDeleteProduct);
            rootDeleteProducts.setVisible(true);
            dialogDeleteProduct.show();

            dialogDeleteProduct.setOnDialogClosed(ev -> {
                tblProducts.setDisable(false);
                rootProducts.setEffect(null);
                rootDeleteProducts.setVisible(false);
                cleanControls();
            });
        }
    }

    @FXML
    private void hideWindowDeleteProduct() {
        try {
            dialogDeleteProduct.close();
        } catch (NullPointerException ex) {}
    }

    @FXML
    private void showWindowUptadeProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.showErrorAlert(stckProducts, rootProducts, tblProducts, "Select an item from the table");
        } else {
            showWindowAddProduct();
            textAddProduct.setText("Update product");
            btnUpdateProduct.toFront();
            selectedRecord();
        }
    }

    @FXML
    private void showWindowDetailsProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.showErrorAlert(stckProducts, rootProducts, tblProducts, "Select an item from the table");
        } else {
            showWindowAddProduct();
            textAddProduct.setText("Product details");
            btnUpdateProduct.setVisible(false);
            btnSaveProduct.setDisable(true);
            btnSaveProduct.toFront();
            disableControlsEdit();
            selectedRecord();
        }
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
            Resources.showErrorAlert(stckProducts, rootProducts, tblProducts, "An error occurred when connecting to MySQL.\n"
                    + "Check your connection to MySQL");
        }
        listProducts = FXCollections.observableArrayList(list);
        tblProducts.setItems(listProducts);
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
    }

    @FXML
    private void newProduct() {
        if (txtBarCode.getText().isEmpty()) {
            new Shake(txtBarCode).play();
        } else if (DatabaseHelper.checkIfProductExists(txtBarCode.getText()) != 0) {
            Resources.notification("Error", "There is already a product with this barcode!", "error.png");
        } else if (txtNameProduct.getText().isEmpty()) {
            new Shake(txtNameProduct).play();
        } else if (txtPurchasePrice.getText().isEmpty()) {
            new Shake(txtPurchasePrice).play();
        } else if (txtPorcentage.getText().isEmpty()) {
            new Shake(txtPorcentage).play();
        } else if (txtSalePrice.getText().isEmpty()) {
            new Shake(txtSalePrice).play();
        } else if (txtMinPrice.getText().isEmpty()) {
            new Shake(txtMinPrice).play();
        } else if (Double.parseDouble(txtMinPrice.getText()) > Double.parseDouble(txtSalePrice.getText())) {
            Resources.notification("Error", "Minimum price cannot be higher than sale price!", "error.png");
        } else if (txtDescriptionProduct.getText().isEmpty()) {
            new Shake(txtDescriptionProduct).play();
        } else {
            Products products = new Products();
            products.setBarcode(txtBarCode.getText());
            products.setProductName(txtNameProduct.getText());
            products.setDescriptionProduct(txtDescriptionProduct.getText());
            products.setPurchasePrice(Double.parseDouble(txtPurchasePrice.getText()));
            products.setPorcentage(Integer.valueOf(txtPorcentage.getText()));
            products.setSalePrice(Double.parseDouble(txtSalePrice.getText()));
            products.setMinimalPrice(Double.parseDouble(txtMinPrice.getText()));
            setInputStream(products);

            boolean result = DatabaseHelper.insertNewProduct(products, listProducts);
            if (result) {
                loadData();
                cleanControls();
                hideWindowAddProduct();
                Resources.showSuccessAlert(stckProducts, rootProducts, tblProducts, "Registry added successfully");
            } else {
                Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
            }
        }
    }

    private void setInputStream(Products products) {
        try {
            if (imageFile != null) {
                InputStream is = new FileInputStream(imageFile);
                products.setInputStream(is);
            } else {
                products.setInputStream(ProductsController.class.getResourceAsStream(NO_IMAGE_AVAILABLE));
            }
        } catch (FileNotFoundException ex) {
            Resources.notification("Error", "File not found.", "error.png");
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            products.setInputStream(ProductsController.class.getResourceAsStream(NO_IMAGE_AVAILABLE));
        }
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
                    image = new Image(NO_IMAGE_AVAILABLE, 200, 200, true, true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    @FXML
    private void updateProduct() {
        String barcode = tblProducts.getSelectionModel().getSelectedItem().getBarcode();

        if (txtBarCode.getText().isEmpty()) {
            new Shake(txtBarCode).play();
        } else if (DatabaseHelper.checkIfProductExists(txtBarCode.getText()) != 0 && !barcode.equals(txtBarCode.getText())) {
            Resources.notification("Error", "There is already a product with this barcode!", "error.png");
        } else if (txtNameProduct.getText().isEmpty()) {
            new Shake(txtNameProduct).play();
        } else if (txtPurchasePrice.getText().isEmpty()) {
            new Shake(txtPurchasePrice).play();
        } else if (txtPorcentage.getText().isEmpty()) {
            new Shake(txtPorcentage).play();
        } else if (txtSalePrice.getText().isEmpty()) {
            new Shake(txtSalePrice).play();
        } else if (txtMinPrice.getText().isEmpty()) {
            new Shake(txtMinPrice).play();
        } else if (Double.parseDouble(txtMinPrice.getText()) > Double.parseDouble(txtSalePrice.getText())) {
            Resources.notification("Error", "Minimum price cannot be higher than sale price!", "error.png");
        } else if (txtDescriptionProduct.getText().isEmpty()) {
            new Shake(txtDescriptionProduct).play();
        } else {
            Products products = tblProducts.getSelectionModel().getSelectedItem();
            products.setId(products.getId());
            products.setBarcode(txtBarCode.getText());
            products.setProductName(txtNameProduct.getText());
            products.setDescriptionProduct(txtDescriptionProduct.getText());
            products.setPurchasePrice(Double.parseDouble(txtPurchasePrice.getText()));
            products.setPorcentage(Integer.valueOf(txtPorcentage.getText()));
            products.setSalePrice(Double.parseDouble(txtSalePrice.getText()));
            products.setMinimalPrice(Double.parseDouble(txtMinPrice.getText()));
            setInputStream(products);
            
            if (imageFile != null) { 
                boolean result = DatabaseHelper.updateProduct(products);
                if (result) {
                    hideWindowAddProduct();
                    loadData();
                    cleanControls();
                    Resources.showSuccessAlert(stckProducts, rootProducts, tblProducts, "Registry updated successfully");
                } else {
                    Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
                }
            } else {
                boolean result = DatabaseHelper.updateProductIfFileIsNull(products);
                if (result) {
                    hideWindowAddProduct();
                    loadData();
                    cleanControls();
                    Resources.showSuccessAlert(stckProducts, rootProducts, tblProducts, "Registry updated successfully");
                } else {
                    Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
                } 
            }
        }
    }

    @FXML
    private void deleteProducts() {
        boolean result = DatabaseHelper.deleteProduct(tblProducts, listProducts);
        if (result) {
            loadData();
            cleanControls();
            hideWindowDeleteProduct();
            Resources.showSuccessAlert(stckProducts, rootProducts, tblProducts, "Registry deleted successfully");
        } else {
            Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
        }
    }

    private void cleanControls() {
        txtMinPrice.clear();
        txtBarCode.clear();
        txtSalePrice.clear();
        txtPorcentage.clear();
        txtNameProduct.clear();
        txtPurchasePrice.clear();
        txtDescriptionProduct.clear();
        imageProduct.setImage(new Image(NO_IMAGE_AVAILABLE));
        imageFile = null;
    }

    private void disableControlsEdit() {
        txtBarCode.setEditable(false);
        txtDescriptionProduct.setEditable(false);
        txtNameProduct.setEditable(false);
        txtPurchasePrice.setEditable(false);
        txtSalePrice.setEditable(false);
        txtPorcentage.setEditable(false);
        txtMinPrice.setEditable(false);
    }

    private void enableControlsEdit() {
        txtBarCode.setEditable(true);
        txtNameProduct.setEditable(true);
        txtPurchasePrice.setEditable(true);
        txtSalePrice.setEditable(true);
        txtDescriptionProduct.setEditable(true);
        txtPorcentage.setEditable(true);
        txtMinPrice.setEditable(true);
    }

    private void disableTable() {
        tblProducts.setDisable(true);
    }

    private void emptyText() {
        Resources.setTextIsEmpty(txtPurchasePrice);
        Resources.setTextIsEmpty(txtMinPrice);
        Resources.setTextIsEmpty(txtPorcentage);
        Resources.setTextIsEmpty(txtSalePrice);
    }

    private void resetValidation() {
        txtBarCode.resetValidation();
        txtNameProduct.resetValidation();
        txtPurchasePrice.resetValidation();
        txtPorcentage.resetValidation();
        txtSalePrice.resetValidation();
        txtMinPrice.resetValidation();
        txtDescriptionProduct.resetValidation();
    }

    private void validateUser() {
        if (DatabaseHelper.getUserType().equals("Administrator")) {
            colPorcentage.setVisible(true);
            colPurchasePrice.setVisible(true);
            btnNewProduct.setDisable(false);
            txtPurchasePrice.setVisible(true);
            txtPorcentage.setVisible(true);
            textPurchase.setVisible(false);
            textPorcentage.setVisible(false);
            setEnableMenuItem();
        } else {
            setDisableMenuItem();
            colPorcentage.setVisible(false);
            colPurchasePrice.setVisible(false);
            btnNewProduct.setDisable(true);
            txtPurchasePrice.setVisible(false);
            textPurchase.setVisible(true);
            textPorcentage.setVisible(true);
            txtPorcentage.setVisible(false);
        }
        keyDeleteProduct();
    }

    private void setDisableMenuItem() {
        menuEdit.setDisable(true);
        menuDelete.setDisable(true);
    }

    private void setEnableMenuItem() {
        menuEdit.setDisable(false);
        menuDelete.setDisable(false);
    }

    @FXML
    private void onlyTextFieldCodeBarNumbers() {
        Resources.validationOnlyNumbers(txtBarCode);
    }

    @FXML
    private void onlyTextFielSearchCodeBardNumbers() {
        Resources.validationOnlyNumbers(txtSearchBarCode);
    }

    @FXML
    private void onlyTextFieldPorcentage() {
        Resources.validationOnlyNumbers(txtPorcentage);
    }

    private void characterLimiter() {
        Resources.limitTextField(txtBarCode, 20);
        Resources.limitTextField(txtPorcentage, 3);
    }

    private void keyEscapeWindows() {
        rootProducts.setOnKeyReleased((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == ESCAPE && rootAddProduct.isVisible()) {
                hideWindowAddProduct();
            }
            if (keyEvent.getCode() == ESCAPE && rootDeleteProducts.isVisible()) {
                hideWindowDeleteProduct();
            }
            try {
                if (keyEvent.getCode() == ESCAPE && jfxDialog.isVisible()) {
                    tblProducts.setDisable(false);
                    rootProducts.setEffect(null);
                    jfxDialog.close();
                }
            } catch (NullPointerException ex) {}
        });

    }

    private void escapeWindowWithTextFields() {
        txtBarCode.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtNameProduct.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtPurchasePrice.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtSalePrice.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtDescriptionProduct.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtPorcentage.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtMinPrice.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddProduct();
                tblProducts.setDisable(false);
            }
        });
    }

    private void keyDeleteProduct() {
        if (DatabaseHelper.getUserType().equals("Administrator")) {
            rootProducts.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                    if (tblProducts.isDisable()) {
                        System.out.println("To delete, finish saving the registry or cancel the operation");
                    } else if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
                        Resources.showErrorAlert(stckProducts, rootProducts, tblProducts, "Select an item from the table");
                    } else {
                        deleteProducts();
                    }
                }
            });
        }
    }

    @FXML
    private void filterNameProduct() {
        String filterName = txtSearchProduct.getText();
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
        String filterCodeBar = txtSearchBarCode.getText();
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
            if (!txtSalePrice.getText().isEmpty() || txtSalePrice.getText().isEmpty()) {
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
            }
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
        imageFile = Resources.getImageFromFileChooser(getStage());
        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString(),
                    imageProduct.getFitWidth() - 10,
                    imageProduct.getFitHeight() - 10, true, true);
            imageProduct.setImage(image);
        } 
    }

    private Stage getStage() {
        return (Stage) btnCancelAddProduct.getScene().getWindow();
    }
}
