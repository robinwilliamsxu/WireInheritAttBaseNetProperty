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
public abstract class AssignWireAttributes implements IXWireAttributeConstraint
{
    private String m_wireAtt = null;
    private String m_pinProp = null;
    private String m_cavProp = null;
    private String m_name = null;
    private String m_version = null;
    private String m_description = null;

    protected AssignWireAttributes(String attribute, String name, String version, String description)
	{
                 m_wireAtt = attribute;
		m_name = name;
		m_version = version;
		m_description = description;
    }

    protected abstract String determineResult(String v1, String v2);

    protected void setPinProperty(String propName) {
        m_pinProp = propName;
    }

    protected void setCavityProperty(String propName) {
        m_cavProp = propName;
    }

    public boolean match(IXWire ixWire, IXSignal ixSignal, IXHarness ixHarness, IXAttributesResult ixAttributesResult)
    {
        String val = wireGetProp(ixWire, ixSignal);
        if (val != null && val.trim().length() > 0) {
            ixAttributesResult.addAttribute(m_wireAtt,val.trim());
            return true;
        }
        return false;
    }

    private String wireGetProp(IXWire wire, IXSignal sig)
	{
		String retVal = null;

		for (IXAbstractPin pin : wire.getAbstractPins())
		{
			String val = pinGetProp(pin,sig);
			retVal = determineResult(retVal, val);
		}
        if (retVal == null || retVal.trim().isEmpty()) {
            for (IXAbstractConductor cond : sig.getFunctionalConductors()) {
                String val = cond.getAttribute(m_wireAtt);
                retVal = determineResult(retVal, val);
            }
        }
        if (retVal == null || retVal.trim().isEmpty()) {
            return null;
        }
        return retVal;
	}

	private String pinGetProp(IXAbstractPin pin, IXSignal sig)
	{
        // Only interested in connector cavities
        // We ignore splices completely
        if (pin instanceof IXCavity)
		{
			IXCavity cav = (IXCavity)pin;
			if (!(cav.getOwner() instanceof IXConnector)) {
				return null;
			}
			IXConnector conn = (IXConnector)cav.getOwner();
			if (!conn.isInline()) {
				return getValueForCavity(cav, sig);
			}
			else if (conn.isInline() && !conn.getMatedConnectors().isEmpty())
			{
				IXCavity cav2 = cav.getMatedCavity();
				for (IXAbstractConductor cond : cav2.getConductors())
				{
					if (cond instanceof IXWire)
					{
						IXWire wire = (IXWire)cond;
						for (IXAbstractPin pin2 : wire.getAbstractPins())
						{
							if (pin2 == cav2)
								continue;
							return pinGetProp(pin2,sig);
						}
					}
				}
			}
		}
		return null;
	}

	private String getValueForCavity(IXCavity cav, IXSignal sig)
	{
		String retVal = null;
        // First check on the cavity itself
        if (m_cavProp != null) {
            String val = cav.getProperty(m_cavProp);
            if (val != null && val.trim().length() > 0) {
                return val;
            }
        }
        // Now check on the logical pin(s) mapped to this cavity
        if (m_pinProp != null) {
            for (IXAbstractPin pin : cav.getFunctionalPins())
            {
                if (pinConnectsToSignal(pin,sig)) {
                    String val = pin.getProperty(m_pinProp);
                    if (val != null && val.trim().length() > 0) {
                        retVal = determineResult(retVal,val);
                    }
                }
            }
        }
        return retVal;
	}

    private boolean pinConnectsToSignal(IXAbstractPin pin, IXSignal sig)
    {
        for (IXAbstractConductor cond : pin.getConductors()) {
            if (sig.getFunctionalConductors().contains(cond)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return m_description;
    }

    public String getName() {
        return m_name;
    }

    public String getVersion() {
        return m_version;
    }

}