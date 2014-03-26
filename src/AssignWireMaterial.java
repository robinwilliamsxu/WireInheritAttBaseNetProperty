/**
 * Copyright 2013 Mentor Graphics Corporation. All Rights Reserved.
 * <p>
 * Recipients who obtain this code directly from Mentor Graphics use it solely
 * for internal purposes to serve as example Java or JavaScript plugins.
 * This code may not be used in a commercial distribution. Recipients may
 * duplicate the code provided that all notices are fully reproduced with
 * and remain in the code. No part of this code may be modified, reproduced,
 * translated, used, distributed, disclosed or provided to third parties
 * without the prior written consent of Mentor Graphics, except as expressly
 * authorized above.
 * <p>
 * THE CODE IS MADE AVAILABLE "AS IS" WITHOUT WARRANTY OR SUPPORT OF ANY KIND.
 * MENTOR GRAPHICS OFFERS NO EXPRESS OR IMPLIED WARRANTIES AND SPECIFICALLY
 * DISCLAIMS ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR WARRANTY OF NON-INFRINGEMENT. IN NO EVENT SHALL MENTOR GRAPHICS OR ITS
 * LICENSORS BE LIABLE FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING LOST PROFITS OR SAVINGS) WHETHER BASED ON CONTRACT, TORT
 * OR ANY OTHER LEGAL THEORY, EVEN IF MENTOR GRAPHICS OR ITS LICENSORS HAVE BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * <p>
 */

import com.mentor.chs.plugin.constraint.IXWireAttributeConstraint;
import com.mentor.chs.plugin.constraint.IXAttributesResult;
import com.mentor.chs.api.IXWire;
import com.mentor.chs.api.IXSignal;
import com.mentor.chs.api.IXHarness;
import com.mentor.chs.api.IXAbstractPin;
import com.mentor.chs.api.IXCavity;
import com.mentor.chs.api.IXConnector;
import com.mentor.chs.api.IXAbstractConductor;

/**
 * Abstract class used for setting wire attributes in Integrator based on logic device pin properties
 * This class contains all the traversal functions to gather and propagate the property values
 * The value is determined from three potential places in priority order:
 *   1. From the slot connector cavity in Integrator
 *   2. From the device pin in the logical system design
 *   3. From the net in the logical system design
 * The first two of these are optional and properties do not have to be supplied.
 *
 * An implementing sub-class needs to do the following:
 * - Call the constructor supplying the wire attribute to set as well as name, version and description
 * - Supply an implemntation of determineResult() which handles property value clashes
 */
public class AssignWireMaterial extends AssignWireAttributes
{
    public AssignWireMaterial()
    {
        super("WireMaterial","Set WireMaterial from device pins","0.2","Set WireMaterial from logical pin attributes");
        setPinProperty("WireMaterial");
        setCavityProperty("WireMaterial");
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