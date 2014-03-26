/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author doxu
 */
public class AssignWireColor extends AssignWireAttributes
{
    public AssignWireColor()
    {
        super("WireColor","Set WireColor from device pins","0.2","Set Wire Color from logical pin attributes");
        setPinProperty("WireColor");
        setCavityProperty("WireColor");
    }

    /* This method overrides the abstract method in the base class
     * It is used to determine the result of a value clash
     *
     * This implementation returns the largest numerical value
     */
    protected String determineResult(String v1, String v2)
    {
        if (v1 == null) { return v2; }
        if (v2 == null) { return v1; }

        double d1 = v1.length();
        double d2 = v2.length();
        if (d1 > d2) {
            return v1;
        }
        return v2;
    }
}