/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Arguments;

/**
 *
 * @author kmchugh
 */
public interface IDoubleParameterArguments<S extends java.lang.Object, T extends java.lang.Object>
        extends IArguments
{
    S getParameter1();
    T getParameter2();
}
