/**
 * Used to add, delete, update, and look up
 * inventory and parts information.
 * 
 * @author Christopher Harrison
 */
package inventorysystem_christopherharrison.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory 
{
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    
    public ObservableList<Product> getProducts()
    {
        return products;
    }

    public ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    
    public void addPart(Part partToAdd)
    {
        allParts.add(partToAdd);
    }

    public void addProduct(Product productToAdd)
    {
        products.add(productToAdd);
    }
    
    //get next available partID
    public int getNextPartID()
    {
        int maxPartID = 0;
        int partID;
        for (Part p : allParts)
        {
            
            partID = p.getPartID();
            if(partID > maxPartID)  
            {
                maxPartID = partID;
            }
        }
        return maxPartID+1;
    }

    //get next available partID
    public int getNextProductID()
    {
        int maxProductID = 0;
        int productID;
        for (Product p : products)
        {
            
            productID = p.getProductID();
            if(productID > maxProductID)  
            {
                maxProductID = productID;
            }
        }
        return maxProductID+1;
    }
    
    //find a specific part by partID
    public Part lookupPart(int partID)
    {
       for(Part p : allParts) 
       {
           if(p.getPartID()== partID)
           {
               return p;
           }
       }
       return null;
    }

    //find a specific product by productID
    public Product lookupProduct(int productID)
    {
       for(Product p : products) 
       {
           if(p.getProductID()== productID)
           {
               return p;
           }
       }
       return null;
    }
    
    //including because it's specified in UML class design
    //but using the updatePart with a different signature to update part
    //by passing a part in.
    public void updatePart(int partID)
    {
      //not using. Using updatePart(Part p) instead
    }
    
    //updating part by passing in part to update. 
    public void updatePart(Part p)
    {
        for(int x = 0;x<allParts.size();x++)
        {
            if(allParts.get(x).getPartID()==p.getPartID())
            {
                allParts.set(x, p); //replace existing part stored here
                break;
            }
        }
    }

    //updating productt by passing in product to update. 
    public void updateProduct(Product p)
    {
        for(int x = 0;x<products.size();x++)
        {
            if(products.get(x).getProductID()==p.getProductID())
            {
                products.set(x, p); //replace existing part stored here
                break;
            }
        }
    }
    
    
    public boolean deletePart(Part p)
    {
        for(int x = 0;x<allParts.size();x++)
        {
            if(allParts.get(x).getPartID()==p.getPartID())
            {
                allParts.remove(x); //remove existing part stored here
                return true;
            }
        }
       return false;
    }

    public boolean removeProduct(Product p)
    {
        for(int x = 0;x<products.size();x++)
        {
            if(products.get(x).getProductID()==p.getProductID())
            {
                products.remove(x); //replace existing product stored here
                return true;
            }
        }
       return false;
    }
    
    
    public ObservableList<Part> searchPart(String searchCriteria)
    {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for(Part p: allParts)
        {
            if( (Integer.toString(p.getPartID()).equals(searchCriteria)) || p.getName().toLowerCase().contains(searchCriteria.toLowerCase()) )
            {
                foundParts.add(p);
            }
        }
        return foundParts;
    }

        public ObservableList<Product> searchProduct(String searchCriteria)
    {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for(Product p: products)
        {
            if( (Integer.toString(p.getProductID()).equals(searchCriteria)) || p.getName().toLowerCase().contains(searchCriteria.toLowerCase()) )
            {
                foundProducts.add(p);
            }
        }
        return foundProducts;
    }

    
}
