/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystem_christopherharrison.View_Controller;

import inventorysystem_christopherharrison.Model.Part;
import inventorysystem_christopherharrison.Model.Inhouse;
import inventorysystem_christopherharrison.Model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class AddModifyPartController implements Initializable {

    @FXML
    private Label lblFormName;
    
    @FXML
    private RadioButton rbInHouse;

    @FXML
    private RadioButton rbOutsourced;

    @FXML
    private Label lblSourcing;

    @FXML
    private Button btnSavePart;
   
    @FXML
    private TextField tfPartID;

    @FXML
    private TextField tfPartName;

    @FXML
    private TextField tfInv;

    @FXML
    private TextField tfPrice;

    @FXML
    private TextField tfMax;

    @FXML
    private TextField tfMin;    
    
    @FXML
    private TextField tfCompanyName;

    @FXML 
    private Button btnCancel;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        lblFormName.setText(MainScreenController.workingForm); 
        
        //Set PartID to one greater than max PartID if existing parts
        if(lblFormName.getText().contains("Modify")) //then modify existing part
        {
           Part p =  MainScreenController.inventoryList.lookupPart(MainScreenController.partIDToMod);
           if(p!=null)
           {
               tfPartID.setText(Integer.toString(p.getPartID()));
               tfPartName.setText(p.getName());
               tfInv.setText(Integer.toString(p.getInStock()));
               tfPrice.setText(Double.toString(p.getPrice()));
               tfMax.setText(Integer.toString(p.getMax()));
               tfMin.setText(Integer.toString(p.getMin()));
               
               //if Inhouse part
               if(p instanceof Inhouse)
               {
                   rbInHouse.setSelected(true);
                   tfCompanyName.setText( Integer.toString( ((Inhouse)p).getMachineID() ) );                   
               }
               else //if Outsourced part
               {
                   rbOutsourced.setSelected(true);
                   tfCompanyName.setText(((Outsourced)p).getCompanyName());
               }
           }
        }
        //set sourcing (Machine ID or Company Name) label after part determined
        lblSourcing.setText(getSourcingText());
        

    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException 
    {
        String errMessage = ""; //error message if exception thrown
        int testInt = 0;
        double testDouble = 0.0;
        
        if(event.getSource()==btnSavePart)
        {
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
                
                 if(rbInHouse.isSelected())
                 {
                    errMessage = "Machine ID field must be an integer!";
                    testInt = Integer.parseInt(tfCompanyName.getText());
                 }
                
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
                
                Part singlePart; //determine if Inhouse or Outsourced below.
                
                if(rbInHouse.isSelected())
                {
                 //Inhouse(int partID, String name, double price, int inStock, int min, int max, int machineID)
                  singlePart = new Inhouse();
                  //Casting to Inhouse to reference Inhouse methods
                  ((Inhouse)singlePart).setMachineID(Integer.parseInt(tfCompanyName.getText()));
                }
                else
                {
                    singlePart = new Outsourced();
                    //Casting to Outsourced to reference Outsourced methods
                  ((Outsourced)singlePart).setCompanyName(tfCompanyName.getText());
                } 
                
                singlePart.setName(tfPartName.getText());
                singlePart.setInStock(Integer.parseInt(tfInv.getText()));
                singlePart.setPrice(Double.parseDouble(tfPrice.getText()));
                singlePart.setMax(Integer.parseInt(tfMax.getText()));
                singlePart.setMin(Integer.parseInt(tfMin.getText()));

                //determine if adding  a new part or modifying existing part
                if(lblFormName.getText().contains("Add")) //then adding new part
                {
                    int partID = MainScreenController.inventoryList.getNextPartID();
                    singlePart.setPartID(partID);
                    if(rbInHouse.isSelected())
                    {
                        MainScreenController.inventoryList.addPart((Inhouse)singlePart);
                    }
                    else
                    {
                        MainScreenController.inventoryList.addPart((Outsourced)singlePart);
                    }
                }
                else //modifying existing part
                {
                    singlePart.setPartID(Integer.parseInt(tfPartID.getText()));
                    if(rbInHouse.isSelected())
                    {
                        MainScreenController.inventoryList.updatePart((Inhouse)singlePart);
                    }
                    else
                    {
                        MainScreenController.inventoryList.updatePart((Outsourced)singlePart);
                    }

                }
                
                //Alert that part was saved.
                alertMessage("Part Saved","Part " + tfPartName.getText() + " was saved.");

                //close Add Part window
                Stage stage = (Stage) btnSavePart.getScene().getWindow();
                stage.close();
            }
            catch(IllegalArgumentException i)
            {
                Alert saveConfirmation = new Alert(AlertType.ERROR);
                saveConfirmation.setTitle("Input Error");
                saveConfirmation.setHeaderText(null);
                saveConfirmation.setContentText(errMessage);
                saveConfirmation.showAndWait();
                
            }
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
     */
    private String checkFields()
    {
        tfPartName.setText(tfPartName.getText().trim());
        tfInv.setText(tfInv.getText().trim());
        tfPrice.setText(tfPrice.getText().trim());
        tfMin.setText(tfMin.getText().trim());
        tfMax.setText(tfMax.getText().trim());
        tfCompanyName.setText(tfCompanyName.getText().trim());
        if(tfPartName.getText().isEmpty())
        {
            return "Part Name field cannot be left blank!";
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
        if(tfCompanyName.getText().isEmpty())
        {
            if(rbInHouse.isSelected())
            {
                return "Machine ID field cannot be left blank!";
            }
            else
            {
                return "Company Name field cannot be left blank!";
            }
        }
        return "";
        
    }
    
    /**
     *
     * 
     * @param event
     */
    @FXML
    public void handleRadioButtons(ActionEvent event)
    {
        lblSourcing.setText(getSourcingText());
    }
    
    /**
     * Sets  the text to use for lblSourcing based on radio buttons
     */
    public String getSourcingText()
    {
        String labelText = "";
        if(rbInHouse.isSelected())
        {
            labelText = "Machine ID";
        }
        else if (rbOutsourced.isSelected())
        {
            labelText = "Company Name";
        }
        return labelText;
    }
    
    
}
