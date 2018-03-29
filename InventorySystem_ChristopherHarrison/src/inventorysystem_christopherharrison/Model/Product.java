/**
 *
 * Represents products that also contain
 * at least one part from the Part class
 * subclasses Inhouse and Outsourced
 */
package inventorysystem_christopherharrison.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product 
{
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
    private int productID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;
    
    //setter and getter for associatedParts
    public void setParts(ObservableList<Part> partsList)
    {
        for(Part p : partsList)
        {
            this.addAssociatedPart(p);
        }
    }
    
    public ObservableList<Part> getParts()
    {
        return associatedParts;
    }

    public void addAssociatedPart(Part p)
    {
        associatedParts.add(p);
    }
    
    public boolean removeAssociatedPart(int partID)
    {
        for(int x = 0;x<associatedParts.size();x++)
        {
            if(associatedParts.get(x).getPartID()==partID)
            {
                associatedParts.remove(x);
                return true;
            }
        }
        return false;
    }
    
    public Part lookupAssociatedPart(int partID)
    {
        for(Part p : associatedParts)
        {
            if(p.getPartID()==partID)
            {
                return p;
            }
        }
        return null;
    }
    
    //setter and getter for name
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }
    
    //setter and getter for price
    public void setPrice(double price)
    {
        this.price = price;
    }
    public double getPrice()
    {
        return this.price;
    }

    //setter and getter for inStock
    public void setInStock(int inStock)
    {
        this.inStock = inStock;
    }
    public int getInStock()
    {
        return this.inStock;
    }
    
    //setter and getter for min
    public void setMin(int min)
    {
        this.min = min;
    }
    public int getMin()
    {
        return this.min;
    }
    
    //setter and getter for max
    public void setMax(int max)
    {
        this.max = max;
    }
    public int getMax()
    {
        return this.max;
    }
    
    //setter and getter for productID
    public void setProductID(int productID)
    {
        this.productID = productID;
    }
    public int getProductID()
    {
        return this.productID;
    }
    
    public double totalOfParts()
    {
        double partsTotal = 0.0;
        for(Part p: associatedParts)
        {
            partsTotal+=p.getPrice();
        }
        return partsTotal;
    }

}
