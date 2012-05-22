/* =========================================================
 * ExecutableXML.java
 *
 * Author:      kmchugh
 * Created:     03-Jun-2008, 15:05:12
 * 
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

import Goliath.Constants.StringFormatType;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 03-Jun-2008
 * @author      kmchugh
**/
public class ExecutableXML extends Goliath.Object
{
    private StringBuilder m_oScript = new StringBuilder();
    private StringBuilder m_oXML = new StringBuilder();
    private String m_cControlName;
    private boolean m_lReplaceNodes = false;
    
    /** Creates a new instance of ExecutableXML */
    public ExecutableXML()
    {
    }
    
    public void appendScript(String tcScript)
    {
        m_oScript.append(tcScript);
    }
    
    public void appendResponseXML(String tcHTML)
    {
        m_oXML.append(tcHTML);
    }
    
    public String getScript()
    {
        return m_oScript.toString();
    }
    
    public String getResponseXML()
    {
        return m_oXML.toString();
    }
    
    public String getControlName()
    {
        return m_cControlName;
    }
    public void setControlName(String tcControlName)
    {
        m_cControlName = tcControlName;
    }
    
    public boolean getReplaceNodes()
    {
        return m_lReplaceNodes;
    }
    public void setReplaceNodes(boolean tlReplace)
    {
        m_lReplaceNodes = tlReplace;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        StringBuilder loBuilder = new StringBuilder("<ExecutableXML>");
        if (m_oScript != null && m_oScript.length() > 0)
        {
            loBuilder.append("<ResponseScript>");
            loBuilder.append(m_oScript);
            loBuilder.append("</ResponseScript>");
        }

        // TODO: m_oXML should be the internal xml writer when it is complete
        if (m_oXML != null && m_oXML.length() > 0)
        {
            loBuilder.append("<ResponseXML>");
            loBuilder.append(m_oXML);
            loBuilder.append("</ResponseXML>");
        }
        loBuilder.append("</ExecutableXML>");
        return loBuilder.toString();
    }       
}
