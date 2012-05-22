/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Arguments;

/**
 *
 * @author kmchugh
 */
public interface IArguments
{
    java.lang.Object getParameter(String tcKey);
    void setParameter(String tcKey, java.lang.Object toValue);
    void removeParameter(String tcKey);
}
