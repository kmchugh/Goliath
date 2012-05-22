/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Messaging;

import Goliath.Applications.Application;
import Goliath.Constants.LogType;
import Goliath.Interfaces.Messaging.IMessageBroker;

/**
 *
 * @author kenmchugh
 */
public class MessageBroker extends Goliath.Object
    implements IMessageBroker
{

    private boolean m_lIsStarted;


    public MessageBroker()
    {
        m_lIsStarted = false;
    }

    @Override
    public final void start()
    {
        Application.getInstance().log("Starting Message Broker " + getClass().getSimpleName(), LogType.TRACE());

        

        m_lIsStarted = true;

        Application.getInstance().log("Started Message Broker " + getClass().getSimpleName(), LogType.TRACE());
    }

    @Override
    public boolean isStarted()
    {
        return m_lIsStarted;
    }

    @Override
    public final void stop()
    {
        Application.getInstance().log("Stopping Message Broker " + getClass().getSimpleName(), LogType.TRACE());
        m_lIsStarted = false;

        

        Application.getInstance().log("Stopped Message Broker " + getClass().getSimpleName(), LogType.TRACE());
    }

}
