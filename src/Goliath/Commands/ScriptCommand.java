/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Commands;

import Goliath.Arguments.Arguments;

/**
 *
 * @author kenmchugh
 */
public class ScriptCommand<A extends Arguments, T> extends Goliath.Commands.Command<Arguments, T>
        implements Goliath.Interfaces.Commands.IScriptCommand<Arguments, T>

{
    private String m_cScript = null;

    /** Creates a new instance of ScriptCommand */
    public ScriptCommand(String tcScript)
    {
        super(false);
        Goliath.Utilities.checkParameterNotNull("tcScript", tcScript);
        m_cScript = tcScript;
    }
    
    public String getScript()
    {
        return m_cScript;
    }

    @Override
    public T doExecute() throws Throwable
    {
        // Do nothing
        return null;
    }
}