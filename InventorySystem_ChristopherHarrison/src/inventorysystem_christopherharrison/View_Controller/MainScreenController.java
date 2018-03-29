/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystem_christopherharrison.View_Controller;

import inventorysystem_christopherharrison.Model.Inventory;
import inventorysystem_christopherharrison.Model.Part;
import inventorysystem_christopherharrison.Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

/**
 * FXML Controller class
 *
 * @author Chris Harrison
 * 
 */
public class MainScreenController implements Initializable {

    public static String workingForm = ""; //used for display label on child forms 
    public  static Inventory inventoryList = new Inventory();
    public static int partIDToMod = 0; //keep track of selected part
    public static int productIDToMod = 0; //keep track of selected part

    @FXML
    private Button btnFilterParts;

    @FXML
    private Button btnFilterProducts;
    
    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnAddPart;
   
    @FXML
    private Button btnModifyPart;
    
    @FXML
    private Button btnDeletePart;
    
    @FXML
    private Button btnDeleteProduct;
    
    @FXML
    private Button btnAddProduct;
    
    @FXML
    private Button btnModifyProduct;

    @FXML
    private TableView<Part> tvPartsTable;

    @FXML
    private TableColumn<Part, Integer> colPartID;

    @FXML
    private TableColumn<Part, String> colPartName;

    @FXML
    private TableColumn<Part, Integer> colPartsInventoryLevel;

    @FXML
    private TableColumn<Part, Double> colPriceCostperUnit; //Part

    @FXML
    private TableView<Product> tvProductsTable;

    @FXML
    private TableColumn<Product, Integer> colProductID;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, Integer> colProductInventoryLevel;

    @FXML
    private TableColumn<Product, Double> colPriceperUnit;//Product
    
    @FXML
    private TextField filterParts;
    
    @FXML
    private TextField filterProducts;
    
  
    @FXML
    private void onEnter(ActionEvent event) throws IOException
    {
        if(event.getSource() == filterParts)
        {
            searchPartList();
        }
        if(event.getSource() == filterProducts)
        {
            searchProductList();
        }
    }    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException 
    {
        if(event.getSource()==btnAddPart)
        {
            setTheStage("AddModifyPart.fxml", "Add Part");
            updatePartTableView(inventoryList.getAllParts());
        }
        else if(event.getSource()==btnModifyPart)
        {
            //check if any items are selected. before loading modify parts
            if(!tvPartsTable.getSelectionModel().isEmpty())
            {
                Part partSelected = tvPartsTable.getSelectionModel().getSelectedItem();
                partIDToMod = partSelected.getPartID();
                setTheStage("AddModifyPart.fxml", "Modify Part");
            }
        }
        else if(event.getSource()==btnDeletePart) //delete Part from list
        {
            //check if part is selected
            if(!tvPartsTable.getSelectionModel().isEmpty())
            {
                Part partSelected = tvPartsTable.getSelectionModel().getSelectedItem();
                int partIDToDelete = partSelected.getPartID();
                String partName = partSelected.getName();
                
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Deleting " + "Part ID: " + partIDToDelete + ", Part Name: " + partName);
                alert.setContentText("Are you sure you want to do this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    boolean deleteSuccess = false;
                    deleteSuccess = inventoryList.deletePart(partSelected);
                    tvPartsTable.setItems(inventoryList.getAllParts());
                    tvPartsTable.refresh();
                    if(deleteSuccess)
                    {
                        alertMessage("Part Deleted","Part ID: " + partIDToDelete + ", Part Name: " + partName + " has been deleted");
                    }
                    else //deletion error
                    {
                        alertMessage("Deletion error!","Could not delete Part ID: " + partIDToDelete + ", Part Name: " + partName );
                    }
                }
                else 
                {
                    alertMessage("Deletion Aborted","Part ID: " + partIDToDelete + ", Part Name: " + partName + " has not been deleted");
                }                
            }
            else
            {
                alertMessage("No Part Selected","No part has been selected for deletion.");
            }
        }
        else if(event.getSource()==btnDeleteProduct) //delete Product from list
        {
            //check if part is selected
            if(!tvProductsTable.getSelectionModel().isEmpty())
            {
                Product productSelected = tvProductsTable.getSelectionModel().getSelectedItem();
                int productIDToDelete = productSelected.getProductID();
                String productName = productSelected.getName();
                if(!productSelected.getParts().isEmpty())
                {
                    alertMessage("Product cannot be deleted!","Product has associated parts. Remove parts before deleting.");
                }
                else
                {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Delete");
                    alert.setHeaderText("Deleting " + "Product ID: " + productIDToDelete + ", Product Name: " + productName);
                    alert.setContentText("Are you sure you want to do this?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK)
                    {
                        boolean deleteSuccess = false;
                        deleteSuccess = inventoryList.removeProduct(productSelected);
                        tvProductsTable.setItems(inventoryList.getProducts());
                        tvProductsTable.refresh();
                        if(deleteSuccess)
                        {
                            alertMessage("Part Deleted","Product ID: " + productIDToDelete + ", Product Name: " + productName + " has been deleted");
                        }
                        else //deletion error
                        {
                            alertMessage("Deletion error!","Could not delete Product ID: " + productIDToDelete + ", Product Name: " + productName );
                        }
                    }
                    else 
                    {
                        alertMessage("Deletion Aborted","Part ID: " + productIDToDelete + ", Product Name: " + productName + " has not been deleted");
                    }
                }                
            }
            else
            {
                alertMessage("No Product Selected","No product has been selected for deletion.");
            }
        }
        else if(event.getSource()==btnFilterParts)//filter Part tableview 
        {
            searchPartList();
        }
        else if(event.getSource()==btnFilterProducts)//filter Product tableview 
        {
            searchProductList();
        }
        else if(event.getSource()==btnAddProduct)
        {
            //if parts exist, allow user create a new product
            if(!inventoryList.getAllParts().isEmpty())
            {
                setTheStage("AddModifyProduct.fxml", "Add Product");
                updateProductTableView(inventoryList.getProducts());
            }
            else //alert user that they need to have parts to create a product
            {
                alertMessage("No Parts Exist!","Cannot load Add Product screen if no parts exist.");
            }
        }
        else if(event.getSource()==btnModifyProduct)
        {
            if(!tvProductsTable.getSelectionModel().isEmpty())
            {
                Product productSelected = tvProductsTable.getSelectionModel().getSelectedItem();
                productIDToMod = productSelected.getProductID();
                setTheStage("AddModifyProduct.fxml", "Modify Product");
            }
            updateProductTableView(inventoryList.getProducts());
        }
        else if(event.getSource()==btnExit)
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("You are about to exit the Inventory Management program.");
            alert.setContentText("Are you sure you want to do this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                Stage stage = (Stage)btnExit.getScene().getWindow();
                stage.close();
            }
            
        }

    }
    
    public void searchPartList()
    {
            //return allParts if search field is empty
            String searchCriteria = filterParts.getText().trim();
            //return allParts if search field is empty
            if(searchCriteria.isEmpty())
            {
                alertMessage("No Items Found", "Search Criteria Did Not Return Any Rows. Displaying all exisiting parts if any.");
                updatePartTableView(inventoryList.getAllParts());
            }
            else
            {
                ObservableList<Part> tempData = inventoryList.searchPart(searchCriteria);
                if(!tempData.isEmpty()) //rows found
                {
                    updatePartTableView(tempData);
                }
                else //display all parts if nothing found
                {
                    updatePartTableView(inventoryList.getAllParts());
                    alertMessage("No Items Found", "Search Criteria Did Not Return Any Rows. Displaying all existing parts if any.");
                }
            }
        
    }
    
        public void searchProductList()
    {
            //return allParts if search field is empty
            String searchCriteria = filterProducts.getText().trim();
            //return allParts if search field is empty
            if(searchCriteria.isEmpty())
            {
                alertMessage("No Items Found", "Search Criteria Did Not Return Any Rows. Displaying all exisiting products if any.");
                updateProductTableView(inventoryList.getProducts());
            }
            else
            {
                ObservableList<Product> tempData = inventoryList.searchProduct(searchCriteria);
                if(!tempData.isEmpty()) //rows found
                {
                    updateProductTableView(tempData);
                }
                else //display all parts if nothing found
                {
                    updateProductTableView(inventoryList.getProducts());
                    alertMessage("No Items Found", "Search Criteria Did Not Return Any Rows. Displaying all existing products if any.");
                }
            }
        
    }

    
    public void updatePartTableView(ObservableList<Part> data)
    {
        PropertyValueFactory<Part, Integer> partIDProperty = new PropertyValueFactory<>("partID");
        PropertyValueFactory<Part, String> partNameProperty = new PropertyValueFactory<>("name");
        PropertyValueFactory<Part, Integer> partInvProperty = new PropertyValueFactory<>("inStock");
        PropertyValueFactory<Part, Double> partPriceProperty = new PropertyValueFactory<>("price");

        colPartID.setCellValueFactory(partIDProperty);
        colPartName.setCellValueFactory(partNameProperty);
        colPartsInventoryLevel.setCellValueFactory(partInvProperty);
        colPriceCostperUnit.setCellValueFactory(partPriceProperty);

        tvPartsTable.setItems(data);
        tvPartsTable.refresh();
        
    }

    public void updateProductTableView(ObservableList<Product> data)
    {
        PropertyValueFactory<Product, Integer> productIDProperty = new PropertyValueFactory<>("productID");
        PropertyValueFactory<Product, String> productNameProperty = new PropertyValueFactory<>("name");
        PropertyValueFactory<Product, Integer> productInvProperty = new PropertyValueFactory<>("inStock");
        PropertyValueFactory<Product, Double> productPriceProperty = new PropertyValueFactory<>("price");

        colProductID.setCellValueFactory(productIDProperty);
        colProductName.setCellValueFactory(productNameProperty);
        colProductInventoryLevel.setCellValueFactory(productInvProperty);
        colPriceperUnit.setCellValueFactory(productPriceProperty);

        tvProductsTable.setItems(data);
        tvProductsTable.refresh();
        
    }

    
    public void alertMessage(String title, String context)
    {
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setTitle(title);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText(context);
        alertMessage.showAndWait();
    }
    /**
     * 
     * Loads a pop-up modal form.
     * @param formName name of the FXML form to load
     * @param labelText set text of label to display on form.
     * 
     */
    private void setTheStage(String formName, String labelText) throws IOException
    {
        MainScreenController.workingForm = labelText;
        Stage stage;
        Parent root;
        stage = new Stage();
        root = FXMLLoader.load(getClass().getResource(formName));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnAddPart.getScene().getWindow());
        stage.showAndWait();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
