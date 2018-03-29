/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystem_christopherharrison.View_Controller;

import inventorysystem_christopherharrison.Model.Inhouse;
import inventorysystem_christopherharrison.Model.Outsourced;
import inventorysystem_christopherharrison.Model.Part;
import inventorysystem_christopherharrison.Model.Product;
import static inventorysystem_christopherharrison.View_Controller.MainScreenController.inventoryList;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class AddModifyProductController implements Initializable {

    @FXML
    private Label lblFormName;
    
    //Begin section for tableview using allParts
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
    //end section for tableview using allParts

    //Begin section for tableview using associatedParts in Product
    @FXML
    private TableView<Part> tvAssociatedPartsTable;

    @FXML
    private TableColumn<Part, Integer> colAssociatedPartsPartID;

    @FXML
    private TableColumn<Part, String> colAssociatedPartsPartName;

    @FXML
    private TableColumn<Part, Integer> colAssociatedPartsPartsInventoryLevel;

    @FXML
    private TableColumn<Part, Double> colAssociatedPartsPriceCostperUnit; //Part
    //end section for tableview using associatedParts in Product
    

    @FXML
    private Button btnAddPart;
    
    @FXML 
    private Button btnSaveProduct;
    
    @FXML
    private Button btnDeletePart;
    
    @FXML
    private Button btnFilterParts;
    
    @FXML 
    private Button btnCancel;
    
    @FXML
    private TextField filterParts;
    
    @FXML
    private TextField tfProductID;
    
    @FXML
    private TextField tfProductName;
    
    @FXML
    private TextField tfInv;
    
    @FXML
    private TextField tfPrice;
    
    @FXML
    private TextField tfMax;
    
    @FXML
    private TextField tfMin;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException 
    {
        if(event.getSource()==btnAddPart)
        {
            if(!tvPartsTable.getSelectionModel().isEmpty())
            {
                Part partSelected = tvPartsTable.getSelectionModel().getSelectedItem();
                int partIDToAdd = partSelected.getPartID();
                String partName = partSelected.getName();
                
                tvAssociatedPartsTable.getItems().add(partSelected);
                updateAssociatedPartTableView(tvAssociatedPartsTable.getItems());
            }
            else
            {
                alertMessage("Part cannot be added!","You have to select a part to add.");

            }
        }
        else if(event.getSource()==btnSaveProduct)
        {
            String errMessage = "";
            int testInt =0;
            double testDouble = 0.0;
            
            try
            {
                //begin field validation
                
                //checkFields trims input fields and checks for empty strings
                //NOTE: moved checkFields logic into separate method
                //      since it's the same checks for all the textfields.
                errMessage = checkFields();
                if(!errMessage.isEmpty()) //if not empty string, raise error
                {
                    throw new IllegalArgumentException(errMessage);
                }
                
                //check that integer and double values are populated where needed
                errMessage = "Inv field must be an integer!";
                testInt = Integer.parseInt(tfInv.getText());

                errMessage = "Price/Cost field must be a number!";
                testDouble = Double.parseDouble(tfPrice.getText());
                
                errMessage = "Max field must be an integer!";
                testInt = Integer.parseInt(tfMax.getText());

                errMessage = "Min field must be an integer!";
                testInt = Integer.parseInt(tfMin.getText());
                
                errMessage = ""; //clear error message if no numeric error above

                
                //check if tfInv > tfMax
                if(Integer.parseInt(tfInv.getText())>Integer.parseInt(tfMax.getText()))
                {
                    errMessage = "Inv value cannot be greater than Max value!";
                    throw new IllegalArgumentException("Inv value cannot be greater than Max value!");
                }
                //check if tfInv < tfMin
                if(Integer.parseInt(tfInv.getText())<Integer.parseInt(tfMin.getText()))
                {
                    errMessage = "Inv value cannot be less than Min value!";
                    throw new IllegalArgumentException("Inv value cannot be less than Min value!");
                }
                //check if min > max
                if(Integer.parseInt(tfMin.getText())>Integer.parseInt(tfMax.getText()))
                {
                    errMessage = "Min value cannot be greater than Max value!";
                    throw new IllegalArgumentException("Min value cannot be greater than Max value!");
                }
                
                //end of field validation
                
                Product singleProduct = new Product(); 
                
                
                singleProduct.setName(tfProductName.getText());
                singleProduct.setInStock(Integer.parseInt(tfInv.getText()));
                singleProduct.setPrice(Double.parseDouble(tfPrice.getText()));
                singleProduct.setMax(Integer.parseInt(tfMax.getText()));
                singleProduct.setMin(Integer.parseInt(tfMin.getText()));
                
                boolean partCheck = false;
                //determine if adding  a new product or modifying existing product
                if(lblFormName.getText().contains("Add")) //then adding new product
                {
                    if(tvAssociatedPartsTable.getItems().isEmpty())
                    {
                       alertMessage("Error Saving Product!","Product " + tfProductName.getText() + " cannot be saved without at least one associated part.");
                       partCheck = false;
                    }
                    else
                    {
                        int partID = MainScreenController.inventoryList.getNextProductID();
                        singleProduct.setProductID(partID);
                        singleProduct.setParts(tvAssociatedPartsTable.getItems());
                        if(singleProduct.totalOfParts()>singleProduct.getPrice())
                        {
                            partCheck = false;
                            alertMessage("Error Saving Product!","Product price $" + tfPrice.getText() + " is less than value of associated parts $"+ singleProduct.totalOfParts());
                        }
                        else
                        {
                            MainScreenController.inventoryList.addProduct(singleProduct);
                            partCheck = true;
                        }
                    }
                }
                else //modifying existing part
                {
                    singleProduct.setProductID(MainScreenController.productIDToMod);

                    if(tvAssociatedPartsTable.getItems().isEmpty())
                    {
                        //Allow user to answer confirmation to upadate exising Product
                        //without parts. The reason to do this would be to allow
                        //deletion of Product from main screen.
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm Update Product With No Parts");
                        alert.setHeaderText("Saving " + "Product: " + singleProduct.getProductID() + ", Product Name: " + 
                                singleProduct.getName()+ " without any associated parts!");
                        alert.setContentText("Are you sure you want to do this?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK)
                        {
                            partCheck = true;
                            singleProduct.setProductID(Integer.parseInt(tfProductID.getText()));
                            singleProduct.setParts(tvAssociatedPartsTable.getItems());
                            MainScreenController.inventoryList.updateProduct(singleProduct);
                            
                        }
                        else
                        {
                            partCheck = false;
                            alertMessage("Error Updating Product!","Product " + tfProductName.getText() + " cannot be saved without at least one associated part.");
                        }
                        
                        
                    }
                    else
                    {
                        singleProduct.setProductID(Integer.parseInt(tfProductID.getText()));
                        singleProduct.setParts(tvAssociatedPartsTable.getItems());
                        if(singleProduct.totalOfParts()>singleProduct.getPrice())
                        {
                            partCheck = false;
                            alertMessage("Error Saving Product!","Product price $" + tfPrice.getText() + " is less than value of associated parts $"+ singleProduct.totalOfParts());
                        }
                        else
                        {
                            MainScreenController.inventoryList.updateProduct(singleProduct);
                            partCheck = true;
                        }
                    }
                }
                
                if(partCheck)
                {
                    //Alert that Product was saved.
                    alertMessage("Product Saved","Product " + tfProductName.getText() + " was saved.");

                    //close Add Part window
                    Stage stage = (Stage) btnSaveProduct.getScene().getWindow();
                    stage.close();
                }
            }
            catch(IllegalArgumentException i)
            {
                Alert saveConfirmation = new Alert(Alert.AlertType.ERROR);
                saveConfirmation.setTitle("Input Error");
                saveConfirmation.setHeaderText(null);
                saveConfirmation.setContentText(errMessage);
                saveConfirmation.showAndWait();
                
            }
        }

        else if(event.getSource()==btnFilterParts)//filter Part tableview 
        {
            searchPartList();
        }
        else if(event.getSource()==btnCancel)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("You are about to exit to the main screen. Any unsaved work will be lost.");
            alert.setContentText("Are you sure you want to do this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                Stage stage = (Stage)btnCancel.getScene().getWindow();
                stage.close();
            }
        }
        
        else if(event.getSource()==btnDeletePart) //delete Part from list
        {
            //check if part is selected
            if(!tvAssociatedPartsTable.getSelectionModel().isEmpty())
            {
                Part partSelected = tvAssociatedPartsTable.getSelectionModel().getSelectedItem();
                int partIDToDelete = partSelected.getPartID();
                String partName = partSelected.getName();
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Deleting " + "Part ID: " + partIDToDelete + ", Part Name: " + partName);
                alert.setContentText("Are you sure you want to do this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    //if true, successful deletion
                    if(tvAssociatedPartsTable.getItems().remove(partSelected))
                    {
                        tvAssociatedPartsTable.refresh();
                        alertMessage("Part Deleted","Part ID: " + partIDToDelete + ", Part Name: " + partName + " has been deleted");
                    }
                    else //error deleting part
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
    }

    @FXML
    private void onEnter(ActionEvent event) throws IOException
    {
        if(event.getSource() == filterParts)
        {
            searchPartList();
        }
    }    

    public void alertMessage(String title, String context)
    {
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setTitle(title);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText(context);
        alertMessage.showAndWait();
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

    public void updateAssociatedPartTableView(ObservableList<Part> data)
    {
        PropertyValueFactory<Part, Integer> associatedPartIDProperty = new PropertyValueFactory<>("partID");
        PropertyValueFactory<Part, String> associatedPartNameProperty = new PropertyValueFactory<>("name");
        PropertyValueFactory<Part, Integer> associatedPartInvProperty = new PropertyValueFactory<>("inStock");
        PropertyValueFactory<Part, Double> associatedPartPriceProperty = new PropertyValueFactory<>("price");

        colAssociatedPartsPartID.setCellValueFactory(associatedPartIDProperty);
        colAssociatedPartsPartName.setCellValueFactory(associatedPartNameProperty);
        colAssociatedPartsPartsInventoryLevel.setCellValueFactory(associatedPartInvProperty);
        colAssociatedPartsPriceCostperUnit.setCellValueFactory(associatedPartPriceProperty);

        tvAssociatedPartsTable.setItems(data);
        tvAssociatedPartsTable.refresh();
    }
    

    private String checkFields()
    {
        tfProductName.setText(tfProductName.getText().trim());
        tfInv.setText(tfInv.getText().trim());
        tfPrice.setText(tfPrice.getText().trim());
        tfMin.setText(tfMin.getText().trim());
        tfMax.setText(tfMax.getText().trim());
        if(tfProductName.getText().isEmpty())
        {
            return "Product Name field cannot be left blank!";
        }
        if(tfInv.getText().isEmpty())
        {
            return "Inv field cannot be left blank!";
        }
        if(tfPrice.getText().isEmpty())
        {
            return "Price/Cost field cannot be left blank!";
        }
        if(tfMax.getText().isEmpty())
        {
            return "Max field cannot be left blank!";
        }
        if(tfMin.getText().isEmpty())
        {
            return "Min field cannot be left blank!";
        }
        return "";
        
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        lblFormName.setText(MainScreenController.workingForm);
        if(!inventoryList.getAllParts().isEmpty())
        {
            updatePartTableView(inventoryList.getAllParts());
        }
        if(lblFormName.getText().contains("Modify")) //then modify existing part
        {
           Product p =  MainScreenController.inventoryList.lookupProduct(MainScreenController.productIDToMod);
           if(p!=null)
           {
               tfProductID.setText(Integer.toString(p.getProductID()));
               tfProductName.setText(p.getName());
               tfInv.setText(Integer.toString(p.getInStock()));
               tfPrice.setText(Double.toString(p.getPrice()));
               tfMax.setText(Integer.toString(p.getMax()));
               tfMin.setText(Integer.toString(p.getMin()));
               updateAssociatedPartTableView(p.getParts());
           }
        }
        else
        {
            tfInv.setText("0"); //defaulting to zero inventory level
        }
        
    }    
    
}
