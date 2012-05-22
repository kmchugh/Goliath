/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Arguments;

import Goliath.Interfaces.Arguments.ISingleParameterArguments;

/**
 *
 * @author kmchugh
 */
public class SingleParameterArguments<T extends java.lang.Object> extends Arguments
        implements ISingleParameterArguments<T>
{
    public SingleParameterArguments(T toArg)
    {
        setParameter("arg1", toArg);
    }

    public T getParameter()
    {
        return (T)getParameter("arg1");
    }
}
