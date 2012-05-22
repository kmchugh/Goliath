package Goliath.XML;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.LogType;
import Goliath.Constants.XMLFormatType;
import Goliath.DynamicCode.Java;
import Goliath.Exceptions.FileAlreadyExistsException;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Interfaces.IXMLFormatter;
import Goliath.SingletonHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Node;

// TODO: Formatter ordering
// TODO: Prevent formatter  over riding
// TODO: Allow formatter wrapping
// TODO: Should be able to use flyweight pattern with this.
// TODO: This needs to be refactored so that it is easier to add new formatters.  Need to pay more attention to the XMLFormatType
/**
 * Converts an object between object format and xml format.  Uses XMLFormatters
 * for specific formatting.
 * @author kenmchugh
 */
public class XMLFormatter<T> extends Goliath.Object
        implements Goliath.Interfaces.IXMLFormatter<T>
{
    private static HashTable<Class, IXMLFormatter> g_oFormatters;

    /**
     * Gets the default formatter, treats it like a singleton
     * @return the Default XML formatter
     */
    private static IXMLFormatter getDefaultFormatter()
    {
        return SingletonHandler.getInstance(XMLFormatter.class);
    }

    /**
     * <p>Gets the XMLFormatter for the specified class. If there is no specific
     * formatter that supports the class, then a formatter which supports the
     * nearest class in the is-a structure will be returned.</p>
     *
     * <p>For example: ClassA extends ClassB* extends ClassC extends ClassD* <br/>
     * Note: Those with * have a XMLFormatter</p>
     *
     * <p>We are looking for a formatter for ClassA, since ClassA doesn't have
     * a formatter, this will return the formatter for ClassB. Even though
     * ClassA also extends ClassD and ClassD has a formatter, ClassA is closer
     * to ClassB.</p>
     *
     * @param toClass toClass the class that the XMLFormatter supports
     * @return the XMLFormatter
     */
    public static IXMLFormatter getXMLFormatter(Class toClass)
    {
        if (g_oFormatters == null)
        {
            g_oFormatters = new HashTable<Class, IXMLFormatter>();
        }

        if (g_oFormatters.containsKey(toClass))
        {
            return g_oFormatters.get(toClass);
        }

        // Need to load and cache the formatter for this type


        // TODO: Need to implement the usage of FormatTypes
        List<IXMLFormatter> loFormatters = Application.getInstance().getObjectCache().getObjects(IXMLFormatter.class, "supports");
        IXMLFormatter loDefaultFormatter = getDefaultFormatter();

        IXMLFormatter loCurrent = null;
        for (IXMLFormatter loFormatter : loFormatters)
        {
            // Taking note of what this formatter (examined in the loop) supports.
            Class loSupportedClass = loFormatter.supports();
            if (loSupportedClass == toClass)
            {
                loCurrent = loFormatter;
                break;
            }

            // If this formatter is not a default formatter
            // and the class (whose formatter we are looking for) is equal or assignable
            // to the class this formatter supports
            if (loFormatter.getClass() != loDefaultFormatter.getClass() && Goliath.DynamicCode.Java.isEqualOrAssignable(loSupportedClass, toClass))
            {
                // Get the formatter whose supported class is at the closest distance to the class whose formatter we're looking for

                // If currently there's no selected formatter, or the class supported by the currently selected
                // extends the class supported by this formatter
                if (loCurrent == null || Goliath.DynamicCode.Java.isEqualOrAssignable(loCurrent.supports(), loSupportedClass))
                {
                    loCurrent = loFormatter;
                }
            }
        }
        g_oFormatters.put(toClass, (loCurrent == null) ? loDefaultFormatter : loCurrent);
        return g_oFormatters.get(toClass);
    }

    /**
     * Writes the object specified to the File specified.  Any file contents will be over written if tlOverwrite is true
     * @param toObject The object to write to XML
     * @param toFile The file to write to
     * @param tcRootElement The element to use as the root element of the document
     * @param tlOverwrite true if we should overwrite existing files
     * @throws FileAlreadyExistsException if the file already exists and tlOverwrite is false
     */
    public static void writeXMLFile(java.lang.Object toObject, File toFile, String tcRootElement, boolean tlOverwrite)
            throws FileAlreadyExistsException
    {
        writeXMLFile(toObject, toFile, XMLFormatType.TYPED(), tcRootElement, tlOverwrite);
    }

    /**
     * Writes the object specified to the XML File
     * @param toObject The object to write
     * @param toFile the file to write to
     * @param toFormatType the format type
     * @param tcRootElement the name of the root element for the file
     * @param tlOverwrite true to over write an existing file
     * @throws FileAlreadyExistsException if the file exists and tlOverwrite is false
     */
    public static void writeXMLFile(java.lang.Object toObject, File toFile, XMLFormatType toFormatType, String tcRootElement, boolean tlOverwrite)
            throws FileAlreadyExistsException
    {
        if (!toFile.exists())
        {
            // Create the file so it is available for streaming
            try
            {
                toFile.createNewFile();
            }
            catch(Throwable ex)
            {
                Application.getInstance().log(ex);
                return;
            }
        }
        else if (toFile.exists() && ! tlOverwrite)
        {
            throw new FileAlreadyExistsException(toFile);
        }
        
        // First we have to begin writing the document and create the root element
        XMLOutputFactory loFactory = XMLOutputFactory.newInstance();

        try
        {
            XMLStreamWriter loWriter = loFactory.createXMLStreamWriter(new FileOutputStream(toFile), "utf-8");
            loWriter.writeStartDocument("utf-8", "1.0");
            loWriter.writeStartElement(tcRootElement);


            // Write the actual object here
            try
            {
                appendToXMLStream(toObject, toFormatType, loWriter, null);
            }
            catch(Throwable ex)
            {
                Application.getInstance().log(ex);
            }

            loWriter.writeEndElement();
            loWriter.writeEndDocument();
            loWriter.close();

        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
            return;
        }
    }

    /**
     * Appends the XML representing toObject to the specified string builder using the format toFormatType
     * @param toObject The object to convert to XML
     * @param toFormatType The format type of the XML
     * @param toStringBuilder The string builder to append the xml to
     */
    public static void appendToXMLString(java.lang.Object toObject, XMLFormatType toFormatType, StringBuilder toStringBuilder)
    {
        IXMLFormatter loDefaultFormatter = toObject != null ? getXMLFormatter(toObject.getClass()) : getDefaultFormatter();

        toStringBuilder.append(loDefaultFormatter.toXMLString(toObject));
    }


    /**
     * Appends the XML representing toObject to the specified Stream
     * @param toObject The object to convert to XML
     * @param toFormatType The format of the object to write
     * @param toStream The stream to write the xml to
     */
    public static void appendToXMLStream(java.lang.Object toObject, XMLFormatType toFormatType, OutputStream toStream)
    {
       XMLOutputFactory loFactory = XMLOutputFactory.newInstance();

        try
        {
            XMLStreamWriter loWriter = loFactory.createXMLStreamWriter(toStream);
            appendToXMLStream(toObject, toFormatType, loWriter, null);
        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }

    /**
     * Appends the XML representing toObject to the specified Stream
     * @param toObject The object to convert to XML
     * @param toFormatType The format of the object to write
     * @param toStream The stream to write the xml to
     */
    public static void appendToXMLStream(java.lang.Object toObject, XMLFormatType toFormatType, XMLStreamWriter toStream, String tcTag)
    {
        IXMLFormatter loCurrent = getXMLFormatter(toObject.getClass());
        try
        {
            loCurrent.appendToXMLStream(toStream, toObject, toFormatType, tcTag);
        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }

    /**
     * Extracts a single object from the xml file specified
     * @param toFile the file to read the xml from
     * @return the object contained within the xml file
     * @throws FileNotFoundException if the specified file was not found
     */
    public static java.lang.Object parseXMLFromFile(File toFile)
            throws FileNotFoundException
    {
        if (!toFile.exists())
        {
            throw new FileNotFoundException(toFile);
        }

        XMLInputFactory loFactory = XMLInputFactory.newInstance();
        try
        {
            XMLStreamReader loReader = loFactory.createXMLStreamReader(new FileInputStream(toFile), "UTF-8");

            IXMLFormatter loFormatter = getDefaultFormatter();

            return loFormatter.fromXMLReader(loReader, XMLFormatType.TYPED(), null);

        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            return null;
        }
    }

    /**
     * Extracts a single object from the xml node specified
     * @param toNode the node containing the object data
     * @return the object
     */
    public static java.lang.Object parseXMLFromNode(Node toNode)
    {
        // TODO: Look at the history of this method, it may not be needed, if it is, it can definitely be optimised
        try
        {
            XMLStreamReader loReader = XMLInputFactory.newInstance().createXMLStreamReader(new StreamSource(new java.io.StringReader(Goliath.XML.Utilities.toString(toNode))));

            IXMLFormatter loFormatter = getDefaultFormatter();

            return loFormatter.fromXMLReader(loReader, XMLFormatType.TYPED(), null);

        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            return null;
        }
    }


    /**
     * Creates a new instance of the XMLFormatter
     */
    public XMLFormatter()
    {
    }

    /**
     * Returns a string based on the object
     *
     * @param  toObject the object to get a string from
     * @return  a string representation of the object
     */
    @Override
    public String toXMLString(T toObject)
    {
        return toXMLString(toObject, XMLFormatType.DEFAULT());
    }

    /**
     * Returns a string based on the object
     *
     * @param  toObject the object to get a string from
     * @return  a string representation of the object
     */
    @Override
    public String toXMLString(T toObject, XMLFormatType toType)
    {

        ByteArrayOutputStream loStream = new ByteArrayOutputStream();

        XMLOutputFactory loFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter loWriter = null;
        try
        {
            loWriter = loFactory.createXMLStreamWriter(loStream);
            appendToXMLStream(loWriter, toObject, toType, null);
            loWriter.flush();
            return Goliath.Utilities.toString(loStream.toByteArray());
        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        finally
        {
            try
            {
                if (loWriter != null)
                {
                    loWriter.close();
                }
                if (loStream != null)
                {
                    loStream.close();
                }
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        return null;
    }


    /**
     * Parses an object from the xml Stream Reader
     * @param toReader The xml reader that is being used to iterate over the xml
     * @param toFormatType The format the xml is in
     * @param toObject The object that is currently being parsed
     * @return the object that was found in the xml stream
     */
    @Override
    public final java.lang.Object fromXMLReader(XMLStreamReader toReader, XMLFormatType toFormatType, java.lang.Object toObject)
    {
        // Parse out the object from the stream
        return iterateReader(toReader, toObject, toFormatType);
    }

    /**
     * Iterates over the elements in the xml, calling methods as required
     * @param toReader The reader that contains the xml
     * @param toObject the object that we are getting the contents of
     * @param toFormatType the format type of the xml
     * @return the object that was created at this level
     */
    protected final java.lang.Object iterateReader(XMLStreamReader toReader, java.lang.Object toObject, XMLFormatType toFormatType)
    {
        java.lang.Object loReturn = null;
        try
        {   while(toReader.hasNext())
            {
                switch(toReader.getEventType())
                {
                    case XMLStreamConstants.START_ELEMENT:
                    {
                        int lnAttributeCount = toReader.getAttributeCount();
                        // Get all of the attributes from this  element
                        PropertySet loAttributes = new PropertySet(lnAttributeCount);
                        for (int i=0; i< lnAttributeCount; i++)
                        {
                            loAttributes.put(toReader.getAttributeLocalName(i), toReader.getAttributeValue(i));
                        }
                        loReturn = startedElement(toReader, toReader.getLocalName(), loAttributes, toObject, toFormatType);
                        if (loReturn != null)
                        {
                            return loReturn;
                        }
                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT:
                    {
                        if (toObject == null || (toObject != null && isEndingElement(toReader.getLocalName(), toObject, toFormatType)))
                        {
                            return toObject;
                        }
                        break;
                    }
                    default:
                        break;
                }
                toReader.next();
            }
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return loReturn;
    }

    /**
     * Checks if the node is the ending element for this object type
     * @param tcNodeName the name of the node that we are checking
     * @param toObject the object that is being created
     * @param toFormatType the format type that is being used
     * @return true if this is the ending node
     */
    private boolean isEndingElement(String tcNodeName, java.lang.Object toObject, XMLFormatType toFormatType)
    {
        return toObject.getClass().getSimpleName().equalsIgnoreCase(tcNodeName);
    }

    /**
     * Hook to create the object specified, subclasses can override this for more specific functions
     * @param toClass the class to create
     * @param toAttributes the attributes/properties of the object to create
     * @param toObject the enclosing object
     * @return the object that is created
     */
    protected T onCreateObject(Class<T> toClass, PropertySet toAttributes, Object toObject)
    {
        return Goliath.DynamicCode.Java.createObject(toClass, null);
    }

    /**
     * A hook to allow child classes to process the object <code>toValue</code> based on the tag attribute "class" value
     * 
     * @param toValue       the object just created
     * @param toAttributes  the attributes listed in this object's xml tag
     * @param toObject      the parent object
     */
    protected void processClassTag(java.lang.Object toValue, PropertySet toAttributes, Object toObject)
    {
        
    }
    
    /**
     * Parses the starting element and tries to extract an object from it.  If an object could not be extracted, then onStartedElement will be called
     * @param toReader The reader containing the xml
     * @param tcNodeName The name of the node being parsed
     * @param toAttributes The list of attributes in this node
     * @param toObject The object that is currently being created
     * @param toFormatType The format of the XML
     * @return The new object if on was created, or null if the start element did not result in the creation of a new element
     */
    private java.lang.Object startedElement(XMLStreamReader toReader, String tcNodeName, PropertySet toAttributes, java.lang.Object toObject, XMLFormatType toFormatType)
    {
        boolean llIsTyped = isTypeSpecified(toAttributes);
        boolean llIsClassed = isClassSpecified(toAttributes);
        
        // An element has started, we need to decide if it is an object or not
        if (llIsTyped || llIsClassed)
        {
            java.lang.Object loValue = null;

            // This is a new object or property being created
            try
            {
                Class loClass = null;
                XMLFormatter loFormatter = null;

                // Tries to load the class first from the 'type' attribute value.
                // If there's no such class, try to use the 'class' attribute value by first converting all '_' to '.'
                String lcClassname = llIsTyped ? toAttributes.<String>getProperty("type") : llIsClassed ? toAttributes.<String>getProperty("class").replace('_', '.') : null;
                if (!Goliath.Utilities.isNullOrEmpty(lcClassname))
                {
                    loClass = Class.forName(lcClassname);
                }

                if (loClass != null)
                {
                    // If this is a primitive, then loAttributes.get("type") we just return the object
                    if (Goliath.DynamicCode.Java.isPrimitive(loClass))
                    {
                        String lcString = toReader.getElementText();
                        loValue = Goliath.DynamicCode.Java.stringToPrimitive(lcString, loClass);
                    }
                    else
                    {
                        // TODO : this needs to be done properly
                        loFormatter = (XMLFormatter)getXMLFormatter(loClass);
                        loValue = loFormatter.onCreateObject(loClass, toAttributes, toObject);
                    }

                    // Do something to the loaded object based on the 'class' attribute
                    if (llIsClassed && loValue != null)
                    {
                        processClassTag(loValue, toAttributes, toObject);
                    }
                }

                iterateToNextPosition(toReader);

                // Now we will create this new object in a recursive fashion
                if (loFormatter != null && loValue != null && !loFormatter.finishedOnCreate())
                {
                    loFormatter.iterateReader(toReader, loValue, toFormatType);
                }

                try
                {
                    // If toObject is not null, the inner value is not null and the inner value is a property of toObject
                    // we set the property of toObject to inner value
                    if (toObject != null && loValue != null && Goliath.DynamicCode.Java.getPropertyType(toObject, tcNodeName) != null)
                    {
                        // We need to set the property to the value parsed
                        Goliath.DynamicCode.Java.setPropertyValue(toObject, tcNodeName, loValue);

                        // We return null here because we are not creating a new object, this will allow the parsing to continue
                        return null;
                    }
                    else
                    {
                        // We are not parsing part of another object, so we can just return now
                        return loValue;
                    }
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
            catch(Throwable ex)
            {
                Application.getInstance().log(ex.toString() + "\n\n occurred reading XML on node " + tcNodeName +  (toAttributes.containsKey("type") ? " node type was " + toAttributes.get("type") : ""), LogType.ERROR());
            }
        }

        // Either this is not a new object, or it was not possible to parse the type from the reader
        onStartedElement(toReader, tcNodeName, toAttributes, toObject, toFormatType);
        return null;
    }


    protected boolean finishedOnCreate()
    {
        return false;
    }


    /**
     * Moves the Stream Reader to the next node with the specified name
     * @param toReader The reader to move
     * @param tcNodeName the name of the node to move to
     */
    protected final void iterateUntilStarting(XMLStreamReader toReader, String tcNodeName)
    {
        try
        {
            while(toReader.hasNext())
            {
                if (toReader.getEventType() == XMLStreamConstants.START_ELEMENT && toReader.getLocalName().equalsIgnoreCase(tcNodeName))
                {
                    return;
                }
                toReader.next();
            }
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }

    /**
     * Moves the Stream Reader to the next node
     * @param toReader The reader to move
     */
    protected final void iterateToNextPosition(XMLStreamReader toReader)
    {
        try
        {
            if (toReader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                toReader.next();
            }
            // TODO: Find out why this code is commented out
            /*
            while(toReader.hasNext())
            {
                if (toReader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    return;
                }
                toReader.next();
            }
             *
             */
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }

    /**
     * Hook method to allow subclasses to take action when an element has started
     * @param toReader The xml reader containing the xml
     * @param tcNodeName The name of the current element
     * @param toAttributes The attributes attached to the current element
     * @param toObject The object that we are in the process of creating
     * @param toFormatType The format of the XML
     */
    protected void onStartedElement(XMLStreamReader toReader, String tcNodeName, PropertySet toAttributes, java.lang.Object toObject, XMLFormatType toFormatType)
    {
    }


    @Override
    public final void appendToXMLStream(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType, String tcTag)
    {
        /* If we are in the situation where we are writing a simple object (string, int, long, etc)
         * as an xml string, the simple object will simply be written as is with no tags.
         * if this object is part of a composition, then it is up to the parent object to have completed the wrapping
         * It is not the responsibility of the formatter to know about parent objects, only to know about the current object
        */

        if (Java.isPrimitive(toObject) && !this.allowContent(toObject, toFormatType))
        {
            try
            {
                appendObjectString(toStream, toObject);
            }
            catch(Throwable ex)
            {
                // We were unable to write the content for some reason
                Application.getInstance().log(ex);
            }
        }
        else
        {
            try
            {
                writeStartTag(toStream, toObject, toFormatType, tcTag);
                if (allowContent(toObject, toFormatType))
                {
                    writeContent(toStream, toObject, toFormatType);
                }
                writeEndTag(toStream, toObject, toFormatType);
            }
            catch(Throwable ex)
            {
                // We were unable to write the content for some reason
                Application.getInstance().log(ex);
            }
        }
    }

    /**
     * Writes out the start tag for the formatter
     * @param toStream the XML Stream writer we are writing to
     * @param toObject the object being written
     * @param toFormatType the format type
     * @param tcTag the tag that we are writing
     * @throws XMLStreamException if there are errors writing to the stream
     */
    protected final void writeStartTag(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType, String tcTag)
            throws XMLStreamException
    {
        String lcTag = Goliath.Utilities.isNullOrEmpty(tcTag) ? getTagName(toObject) : tcTag;
        toStream.writeStartElement(lcTag);
        appendTagAttributes(toStream, toObject, toFormatType);
    }

    /**
     * Writes out the end tag for the formatter
     * @param toStream the XML Stream writer we are writing to
     * @param toObject the object being written
     * @param toFormatType the format type
     * @throws XMLStreamException if there are errors writing to the stream
     */
    protected final void writeEndTag(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType)
            throws XMLStreamException
    {
        toStream.writeEndElement();
    }

    /**
     * Appends all of the required attribtes to the XML Stream Writer
     * @param toStream the stream writer being written to
     * @param toObject the object being written
     * @param toFormatType the format we are writing in
     */
    protected final void appendTagAttributes(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType)
    {
        List<String> loAttributes = getAttributeList(toObject);

        if (loAttributes != null && loAttributes.size() > 0)
        {
            for (String lcProperty : loAttributes)
            {
                try
                {
                    Java.MethodDefinition loDef = Goliath.DynamicCode.Java.getMethodDefinition(toObject.getClass(), lcProperty);
                    Method loMethod = loDef.getAccessor();
                    if (loMethod == null && loDef.getFunctions().size() > 0)
                    {
                        loMethod = (Method)loDef.getFunctions().get(0);
                    }

                    java.lang.Object loProperty = null;
                    if (loMethod != null)
                    {
                        try
                        {
                            loProperty = loMethod.invoke(toObject, (java.lang.Object[])null);
                        }
                        catch (Throwable ex)
                        {
                            Application.getInstance().log(ex);
                            loProperty = null;
                        }
                    }
                    else
                    {
                        Application.getInstance().log("Property [" + lcProperty + "] not found on object type " + toObject.getClass().getName(), LogType.WARNING());
                    }
                    if (loProperty != null)
                    {
                        try
                        {
                            toStream.writeAttribute(lcProperty, loProperty.toString());
                        }
                        catch(Throwable ex)
                        {
                            // We were unable to write the content for some reason
                            Application.getInstance().log(ex);
                        }
                    }
                }
                catch(Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
        }


        // Add typed attributes if needed
        if (isTypeSpecified(toFormatType))
        {
            try
            {
                toStream.writeAttribute("type", toObject.getClass().getName());
            }
            catch(Throwable ex)
            {
                // We were unable to write the content for some reason
                Application.getInstance().log(ex);
            }
        }

        if (isClassSpecified(toFormatType))
        {
            try
            {
                toStream.writeAttribute("class", getClassAttributeValue(toObject));
            }
            catch(Throwable ex)
            {
                // We were unable to write the content for some reason
                Application.getInstance().log(ex);
            }
        }
    }

    /**
     * Writes out the content of the XML
     * @param toStream the XML  Stream to write to
     * @param toObject the object being written
     * @param toFormatType the format type being written
     */
    protected final void writeContent(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType)
    {
        if (allowContent(toObject, toFormatType))
        {
            onWriteContent(toStream, toObject, toFormatType);
        }
    }

    /**
     * Gets the list of properties for the specified object
     * @param toObject the object to get the property list for
     * @return the list of properties for the object specified
     */
    protected List<String> onGetPropertyList(T toObject)
    {
        return Goliath.DynamicCode.Java.getPropertyMethods(toObject.getClass());
    }

    /**
     * Writes the content of the xml
     * @param toStream the stream to write to
     * @param toObject the object being written
     * @param toFormatType the format used to write
     */
    protected void onWriteContent(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType)
    {
        List<String> loAttributes = getAttributeList(toObject);
        List<String> loProperties = onGetPropertyList(toObject);
        for (String lcProperty : loProperties)
        {
            if (!loAttributes.contains(lcProperty))
            {
                java.lang.Object loValue = Goliath.DynamicCode.Java.getPropertyValue(toObject, lcProperty);
                if (loValue != null)
                {
                    // If this is a primitive we need to wrap it because it is a property
                    if (Goliath.DynamicCode.Java.isPrimitive(loValue))
                    {
                        try
                        {
                            toStream.writeStartElement(lcProperty);
                            if (isTypeSpecified(toFormatType))
                            {
                                toStream.writeAttribute("type", loValue.getClass().getName());
                            }
                            if (isClassSpecified(toFormatType))
                            {
                                toStream.writeAttribute("class", getClassAttributeValue(loValue));
                            }
                            XMLFormatter.appendToXMLStream(loValue, toFormatType, toStream, null);
                            toStream.writeEndElement();
                        }
                        catch(Throwable ex)
                        {
                            Application.getInstance().log(ex);
                        }
                    }
                    else
                    {
                        XMLFormatter.appendToXMLStream(loValue, toFormatType, toStream, lcProperty);
                    }
                }
            }
        }
    }

    /**
     * Appends the string value of the object
     * @param toStream the xml stream to write to
     * @param toObject the object to write
     * @throws XMLStreamException if there are problems writing to the stream
     */
    protected void appendObjectString(XMLStreamWriter toStream,  java.lang.Object toObject)
            throws XMLStreamException
    {
        if (toObject.getClass().getSimpleName().equals("byte[]"))
        {
            toStream.writeCData(Goliath.Utilities.toString((byte[])toObject));
        }
        else
        {
            toStream.writeCharacters(toObject.toString());
        }

        // TODO: Make sure items written to the string do not include characters that are not allowed in xml, if they do.  Convert them.
    }

    /**
     * Checks if this type of object allows content
     * @param toObject the object to check
     * @param toFormatType the format being checked
     * @return true if content is allowed, false otherwise
     */
    protected boolean allowContent(T toObject, XMLFormatType toFormatType)
    {
        return !Java.isPrimitive(toObject);
    }

    /**
     * Gets the tag name for the object specified
     * @param toObject the object to get the tag name for
     * @return the name of the tag that should represent this object
     */
    protected String getTagName(T toObject)
    {
        return toObject.getClass().getSimpleName();
    }

    /**
     * The type that this formatter supports
     * @return the type supported by this formatter
     */
    @Override
    public Class<T> supports()
    {
        return  (Class<T>)java.lang.Object.class;
    }

    /**
     * Gets the list of attributes that should be written
     * @param toObject the object to get the attributes for
     * @return the list of attributes
     */
    protected List<String> getAttributeList(T toObject)
    {
        return new List<String>();
    }

    /**
     * Gets the value to use when setting class attributes
     * @param toValue the object to get the value for
     * @return the value
     */
    @Goliath.Annotations.NotProperty
    protected String getClassAttributeValue(java.lang.Object toValue)
    {
        // TODO: Check this method
        String lcClassAttributeValue = toValue.getClass().getName();
        lcClassAttributeValue = lcClassAttributeValue.replace('.', '_');
        
        return lcClassAttributeValue;
    }

    // TODO: The following four methods may no longer be needed

    protected boolean isTypeSpecified(XMLFormatType toFormatType)
    {
        return toFormatType == XMLFormatType.TYPED();
    }

    protected boolean isClassSpecified(XMLFormatType toFormatType)
    {
        return toFormatType == XMLFormatType.GSP();
    }

    protected boolean isTypeSpecified(PropertySet toAttributes)
    {
        return toAttributes.containsKey("type");
    }

    protected boolean isClassSpecified(PropertySet toAttributes)
    {
        return toAttributes.containsKey("class");
    }
}
