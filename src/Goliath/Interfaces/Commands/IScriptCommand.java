/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Commands;

import Goliath.Arguments.Arguments;

/**
 *
 * @author kenmchugh
 */
public interface IScriptCommand<A extends Arguments, T> extends Goliath.Interfaces.Commands.ICommand<A, T>
{
    String getScript();
}