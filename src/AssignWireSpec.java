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

/**
 * Set WireSpec attribute on wires in Integrator
 *
 * The value is determined from three places in priority order:
 *   1. From a WireSpec property on the slot connector cavity in Integrator
 *   2. From a WireSpec property on the device pin in the logical system design
 *   3. From the WireSpec attribute on the net in the logical system design
 * For wires not connected to a slot connector (or with no cavity or pin property defined),
 * the value will come from the functional net which is the same as the usually implemented behaviour.
 */
public class AssignWireSpec extends AssignWireAttributes
{
    public AssignWireSpec()
    {
        super("WireSpec","Set WireSpec from device pins","0.1","Set Wire Specification from logical pin attributes");
        setPinProperty("WireSpec");
        setCavityProperty("WireSpec");
    }

    /* This method overrides the abstract method in the base class
     * It is used to determine the result of a value clash
     *
     * This EXAMPLE implementation simply returns first lexicographically sorted value
     */
    protected String determineResult(String v1, String v2)
    {
        if (v1 == null) { return v2; }
        if (v2 == null) { return v1; }

        if (v1.compareTo(v2) < 0) {
            return v1;
        }
        return v2;
    }
}