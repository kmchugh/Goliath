/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Arguments;

/**
 *
 * @author kmchugh
 */
public interface ISingleParameterArguments<T extends java.lang.Object>
        extends IArguments
{

    T getParameter();
}
