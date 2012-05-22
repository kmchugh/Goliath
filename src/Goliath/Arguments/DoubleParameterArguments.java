/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Arguments;

import Goliath.Interfaces.Arguments.IDoubleParameterArguments;

/**
 *
 * @author kmchugh
 */
public class DoubleParameterArguments<S extends java.lang.Object, T extends java.lang.Object> extends Arguments
        implements IDoubleParameterArguments<S, T>
{
    public DoubleParameterArguments(S toArg1, T toArg2)
    {
        setParameter("arg1", toArg1);
        setParameter("arg2", toArg2);
    }

    public S getParameter1()
    {
        return (S)getParameter("arg1");
    }

    public T getParameter2()
    {
        return (T)getParameter("arg2");
    }
}
