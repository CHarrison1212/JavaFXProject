/**
 * Concrete class extending abstract Part class.
 * This and the Outsourced class are used to show
 * polymorphism in the program.
 */

package inventorysystem_christopherharrison.Model;

public class Inhouse extends Part
{
    private int machineID;
    
    //setter and getter for machineID
    public void setMachineID(int machineID)
    {
        this.machineID = machineID;
    }
    public int getMachineID()
    {
        return this.machineID;
    }
}
