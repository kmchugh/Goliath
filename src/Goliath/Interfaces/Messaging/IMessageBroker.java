/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Messaging;

/**
 *
 * @author kenmchugh
 */
public interface IMessageBroker
{
    void start();
    void stop();
    boolean isStarted();
}
