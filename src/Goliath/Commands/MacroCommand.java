/* =========================================================
 * MacroCommand.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 12:55:12
 * 
 * Description
 * --------------------------------------------------------
 * This class is used to represent to be a list of commands that are
 * executed in the order they are added to the collection.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Commands;

import Goliath.Arguments.Arguments;
import Goliath.Collections.List;
import Goliath.Exceptions.ProcessNotComplete;

/**
 * This class is used to represent to be a list of commands that are
 * executed in the order they are added to the collection.
 *
 * @version     1.0 28-Jan-2008
 * @author      kmchugh
**/
public class MacroCommand<A extends Arguments, T> extends Goliath.Commands.Command<A, Goliath.Collections.List<T>>
{
    private Goliath.Collections.List<Goliath.Interfaces.Commands.ICommand<A, T>> m_oCommands = new Goliath.Collections.List<Goliath.Interfaces.Commands.ICommand<A, T>>();
    private boolean m_lGeneratedResult = false;
    
    /** Creates a new instance of MacroCommand */
    public MacroCommand()
    {
    }
    
    public boolean addCommand(Goliath.Interfaces.Commands.ICommand<A, T> toCommand)
    {
        return m_oCommands.add(toCommand);                
    }

    @Override
    public List<T> doExecute() throws Throwable
    {
        Goliath.Collections.List<T> loList = new Goliath.Collections.List<T>();
        double i=0;
        for (Goliath.Interfaces.Commands.ICommand<A, T> loCommand : m_oCommands) 
        {
            if (isCancelled())
            {
                break;
            }
            i++;
            try
            {
                if (loCommand.execute(getArguments()))
                {
                    loList.add(loCommand.getResult());                    
                }
            }
            catch(Throwable t)
            {
                addError(new Goliath.Exceptions.Exception(t.getLocalizedMessage()));                
            }
            finally
            {
                setProgress((int)((i/(double)loList.size()) * 100.00));
                m_lGeneratedResult = false;
            }
        }
        return loList;
    }

    @Override
    public List<Throwable> getErrors()
    {
        Goliath.Collections.List<Throwable> loErrors = new Goliath.Collections.List<Throwable>();
        loErrors.addAll(super.getErrors());
        for (Goliath.Interfaces.Commands.ICommand<A, T> loCommand : m_oCommands) 
        {
            loErrors.addAll(loCommand.getErrors());                        
        }
        return loErrors;
    }
    
    

    @Override
    public List<T> getResult() throws ProcessNotComplete
    {
        synchronized(this)
        {
            if (!m_lGeneratedResult)
            {
                Goliath.Collections.List<T> loList = new Goliath.Collections.List<T>();
                for (Goliath.Interfaces.Commands.ICommand<A, T> loCommand : m_oCommands) 
                {
                    loList.add(loCommand.getResult());                        
                }
                setReturnValue(loList);
                m_lGeneratedResult = true;
            }
            return super.getResult(); 
        }
    }
    
    
}
