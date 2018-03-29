/**
 * Concrete class extending abstract Part class.
 * This and the Inhouse class are used to show
 * polymorphism in the program.
 */
package inventorysystem_christopherharrison.Model;

public class Outsourced extends Part
{
    private String companyName;

    //getter and getter for companyName
    public  String getCompanyName()
    {
        return this.companyName;
    }
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    
}
