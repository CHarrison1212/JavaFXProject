/**
 * Abstract template used by Inhouse and Outsourced classes.
 */

package inventorysystem_christopherharrison.Model;

public abstract class Part 
{
    private int partID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;
    
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
    
    //setter and getter for partID
    public void setPartID(int partID)
    {
        this.partID = partID;
    }
    public int getPartID()
    {
        return this.partID;
    }
    
}
