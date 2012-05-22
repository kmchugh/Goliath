package Goliath.PropertyHandlers;


public class FilePropertyHandler extends PropertyHandler
{
    /**
     * Creates a new File property handler
     * @param tcPropertiesFileName The name of the file the properties will be saved to
     * @param tcRootNodeName the root name of the root element for the file properties
     * @param tcNameSpaceSeparator the character used to mark the separator in name spaces.  for example Application.Settings is separated by '.'
     */
    public FilePropertyHandler(String tcPropertiesFileName, String tcRootNodeName, String tcNameSpaceSeparator)
    {
        addBehaviour("File", new FileIOBehaviour(tcPropertiesFileName, tcRootNodeName, tcNameSpaceSeparator));
    }
}
