/* ========================================================
 * Utilities.java
 *
 * Author:      christinedorothy
 * Created:     Jun 7, 2011, 11:13:56 PM
 *
 * Description
 * --------------------------------------------------------
 * This is a utilities class that contains methods for different
 * basic XML functions in the framework.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.XML;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.XMLFormatType;
import Goliath.Environment;
import Goliath.Exceptions.FileAlreadyExistsException;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Exceptions.UncheckedException;
import Goliath.Watchers.ObjectWatcher;
import Goliath.Watchers.WatcherManager;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


        
/**
 * This is a utilities class that contains methods for different
 * basic XML functions in the framework.
 *
 * @version     1.0 Jun 7, 2011
 * @author      christinedorothy
**/
public final class Utilities extends Goliath.Object
{
    private static ConcurrentHashMap<String, Templates> g_oTransformTemplates;
    private static HashTable<TransformerFactory, List<Throwable>> g_oErrors;
    

    public static <T> T fromXML(File toFile, String tcRoot)
            throws FileNotFoundException
    {
        return (T) XMLFormatter.parseXMLFromFile(toFile);
    }

    public static String makeSafeForXML(String tcString)
    {
        if (Goliath.Utilities.isNullOrEmpty(tcString))
        {
            return tcString;
        }

        if (tcString.contains("<") || tcString.contains("&") || tcString.contains(">"))
        {
            StringBuilder lcBuffer = new StringBuilder();
            for (int i = 0, lnLength = tcString.length(); i < lnLength; i++)
            {
                char lcChar = tcString.charAt(i);
                if (lcChar < 32 || lcChar > 126 || lcChar == '<' || lcChar == '&' || lcChar == '>')
                {
                    lcBuffer.append("&#");
                    lcBuffer.append((int) lcChar);
                    lcBuffer.append(";");
                }
                else
                {
                    lcBuffer.append(lcChar);
                }
            }
            return lcBuffer.toString();
        }
        return tcString;
    }

    public static String toXMLString(java.lang.Object toObject)
    {
        return toXMLString(toObject, XMLFormatType.DEFAULT());
    }

    public static String toXMLString(java.lang.Object toObject, XMLFormatType toFormatType)
    {
        StringBuilder loXML = new StringBuilder();
        XMLFormatter.appendToXMLString(toObject, toFormatType, loXML);
        return loXML.toString();
    }

    public static void toXMLFile(java.lang.Object toObject, File toFile, String tcRootElement)
    {
        try
        {
            toXMLFile(toObject, toFile, tcRootElement, true);
        }
        catch (FileAlreadyExistsException ex)
        {
            // this can not happen because we have said we want to overwrite
        }
    }

    public static void toXMLFile(java.lang.Object toObject, File toFile, String tcRootElement, boolean tlOverwrite)
            throws FileAlreadyExistsException
    {
        XMLFormatter.writeXMLFile(toObject, toFile, tcRootElement, tlOverwrite);

    }

    public static Document toXML(File toFile)
            throws FileNotFoundException
    {
        if (!toFile.exists())
        {
            throw new FileNotFoundException(toFile);
        }

        try
        {
            DocumentBuilderFactory loFactory = DocumentBuilderFactory.newInstance();

            // The file exists, we read it and return it as a DOM
            return loFactory.newDocumentBuilder().parse(toFile);
        }
        catch (Throwable ex)
        {
            Goliath.Applications.Application.getInstance().log(ex);
        }
        return null;
    }

    public static Document toXML(String tcXMLString)
    {
        try
        {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new java.io.StringReader(tcXMLString)));
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return null;
    }
    
    /**
     * Performs an in memory transformation
     * @param tcXML the xml to transform
     * @param toTransformer the transformer transform with
     * @return an xml document that is the result of the transform
     * @throws Goliath.Exceptions.FileNotFoundException if either file was not found
     * @throws javax.xml.transform.TransformerException if there was a problem during the transformation
     */
    public static Document transform(Transformer toTransformer, String tcXML)
            throws FileNotFoundException,
                   TransformerException
    {
        // Make sure the parameters are not null
        Goliath.Utilities.checkParameterNotNull("toTransformer", toTransformer);
        Goliath.Utilities.checkParameterNotNull("tcXML", tcXML);

        try
        {
            // TODO: Make the replacement value a static constant
            tcXML = tcXML.replaceAll("&", "[-amp-]");

            // Remove anything before the "<?xml" definition or we will get a content not allowed on prolog error
            tcXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + stripXMLHeader(tcXML);

            Source loSource = new StreamSource(new java.io.StringReader(tcXML.toString()));

            Document loDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Result loResult = new DOMResult(loDocument);

            toTransformer.transform(loSource, loResult);

            return loDocument;
        }
        catch (ParserConfigurationException ex)
        {
            throw new Goliath.Exceptions.UncheckedException(ex);
        }
        catch (TransformerConfigurationException ex)
        {
            throw new Goliath.Exceptions.UncheckedException(ex);
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            throw new Goliath.Exceptions.UncheckedException(ex);
        }

    }

    /**
     * Adds the specified error to the transformer error list
     * @param toFactory the transformer factory to add the error to
     * @param toError the error to add
     */
    private static void addTransformerError(TransformerFactory toFactory, Throwable toError)
    {
        if (g_oErrors == null)
        {
            g_oErrors = new HashTable<TransformerFactory, List<Throwable>>();
        }

        synchronized(g_oErrors)
        {
            if (!g_oErrors.containsKey(toFactory))
            {
                g_oErrors.put(toFactory, new List<Throwable>());
            }

            g_oErrors.get(toFactory).add(toError);
        }
    }


    /**
     * Gets the list of errors for the transformer, if no errors, then returns an
     * empty list
     * @param toFactory the factory to get the errors for
     * @return the list of errors for this factory, or an empty list if there are no errors
     */
    private static List<Throwable> getErrors(TransformerFactory toFactory)
    {
        List<Throwable> loReturn = null;

        if (g_oErrors != null)
        {
            synchronized(g_oErrors)
            {
                if (g_oErrors.containsKey(toFactory))
                {
                    loReturn = new List<Throwable>(g_oErrors.get(toFactory));
                }
            }
        }
        return loReturn == null ? new List<Throwable>(0) : loReturn;
    }

    /**
     * Clears all of the errors for a specified transformer factory
     * @param toFactory the factory to clear the errors for
     */
    private static void clearErrors(TransformerFactory toFactory)
    {
        if (g_oErrors != null)
        {
            synchronized(g_oErrors)
            {
                if (g_oErrors.containsKey(toFactory))
                {
                    g_oErrors.remove(toFactory);
                }
            }
        }
    }
    
    /**
     * Transforms the XML document passed in using the XSLT file specified
     * @param toXML the XML Document
     * @param tcXSLTFile the XSLT file
     * @return the result of the transformation
     */
    public static Document transform(Document toXML, Goliath.IO.File toXSLTFile)
    {
        // TODO: Look at refactoring the transformations
        try
        {
            Templates loFactory = TransformerFactory.newInstance().newTemplates(new StreamSource(toXSLTFile));

            Document loDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Result loResult = new DOMResult(loDocument);

            loFactory.newTransformer().transform(new DOMSource(toXML), loResult);

            return loDocument;
        }
        catch (Throwable ex)
        {
            return null;
        }
    }

    /**
     * Performs an in memory transformation
     * @param tcXML the xml to transform
     * @param tcXSLTFileName the xslt to transform with
     * @param tlXMLisFileName
     * @return an xml document that is the result of the transform
     * @throws Goliath.Exceptions.FileNotFoundException if either file was not found
     * @throws javax.xml.transform.TransformerException if there was a problem during the transformation
     */
    public static Document transform(String tcXML, String tcXSLTFileName, boolean tlXMLisFileName)
            throws FileNotFoundException,
                   TransformerException
    {

        // Make sure the parameters are not null
        Goliath.Utilities.checkParameterNotNull("tcXML", tcXML);
        Goliath.Utilities.checkParameterNotNull("tcXSLTFileName", tcXSLTFileName);

        try
        {
            Goliath.IO.File loFile = new Goliath.IO.File(tcXSLTFileName);
            if (g_oTransformTemplates == null)
            {
                g_oTransformTemplates = new ConcurrentHashMap<String, Templates>();
            }

            Templates loTemplate = g_oTransformTemplates.get(loFile.getAbsolutePath());
                
            if (loTemplate == null)
            {
                final TransformerFactory loFactory = TransformerFactory.newInstance();
                loFactory.setErrorListener(new ErrorListener() {

                    @Override
                    public void warning(TransformerException toException) throws TransformerException
                    {
                        // Only output errors
                        //Application.getInstance().log(toException.getLocalizedMessage(), LogType.WARNING());
                    }

                    @Override
                    public void error(TransformerException toException) throws TransformerException
                    {
                        addTransformerError(loFactory, toException);
                    }

                    @Override
                    public void fatalError(TransformerException toException) throws TransformerException
                    {
                        addTransformerError(loFactory, toException);
                    }
                });


                try
                {
                    loTemplate = loFactory.newTemplates(new StreamSource(new java.io.File(tcXSLTFileName)));

                    if (!loFile.isTemporary())
                    {
                        g_oTransformTemplates.put(loFile.getAbsolutePath(), loTemplate);

                        new ObjectWatcher<File>(loFile)
                        {
                            // TODO: This can miss a file particularly when a file gets modified and transformed one after the other on the same thread, see serving the HTML and GSP pages for an example
                            private long m_nModified = 0;

                            @Override
                            public boolean watcherCondition()
                            {
                                File loFile = getObject();
                                if (m_nModified <= 0 && loFile.exists())
                                {
                                    m_nModified = loFile.lastModified();
                                }
                                return loFile.lastModified() != m_nModified;
                            }

                            @Override
                            public void watcherAction()
                            {
                                synchronized (g_oTransformTemplates)
                                {
                                    WatcherManager.getInstance().removeWatcher(this);
                                    g_oTransformTemplates.remove(getObject().getAbsolutePath());
                                }
                            }
                        };
                    }
                }
                catch (Throwable ex)
                {
                    StringBuilder loBuilder = new StringBuilder(ex.getLocalizedMessage());
                    loBuilder.append(Environment.NEWLINE());

                    for (Throwable loError : getErrors(loFactory))
                    {
                        loBuilder.append(loError.getLocalizedMessage());
                        loBuilder.append(Environment.NEWLINE());
                    }
                    Goliath.Exceptions.Exception loEx = new Goliath.Exceptions.Exception(loBuilder.toString(), false);
                    throw loEx;
                }
                finally
                {
                    clearErrors(loFactory);
                }
            }
            return transform(loTemplate.newTransformer(), (tlXMLisFileName ? Goliath.IO.Utilities.File.toString(new File(tcXML)) : tcXML));
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            throw new UncheckedException(ex);
        }
    }

    /**
     * Performs an in memory transformation
     * @param tcXMLFileName the xml file to transform
     * @param tcXSLTFileName the xslt file to transform with
     * @return an xml document that is the result of the transform
     * @throws Goliath.Exceptions.FileNotFoundException if either file was not found
     * @throws javax.xml.transform.TransformerException if there was a problem during the transformation
     */
    public static Document transform(String tcXMLFileName, String tcXSLTFileName)
            throws FileNotFoundException,
                   TransformerException
    {
        return transform(tcXMLFileName, tcXSLTFileName, true);
    }

    /**
     * Gets a list of nodes with the specified tag name
     * @param toNode the node to begin looking for the elements from
     * @param tcNodeName the case sensitive node name to find
     * @param tnDepth The depth in the children that this method will search
     * @return the list of nodes that match, or an empty list if there were no matching
     */
    public static List<Node> getElementsByTagName(Node toNode, String tcNodeName, int tnDepth, boolean tlExcludeRoot)
    {
        List<Node> loReturn = new List<Node>(0);

        if (tnDepth < 0)
        {
            return loReturn;
        }

        // Check the current node first
        if (!tlExcludeRoot && toNode.getNodeName().equalsIgnoreCase(tcNodeName))
        {
            loReturn.add(toNode);
        }

        for (int i = 0; i < toNode.getChildNodes().getLength(); i++)
        {
            loReturn.addAll(getElementsByTagName(toNode.getChildNodes().item(i), tcNodeName, tnDepth - 1, tlExcludeRoot));
        }

        return loReturn;
    }

    public static List<Node> getElementsByTagName(Node toNode, String tcNodeName, int tnDepth)
    {
        return getElementsByTagName(toNode, tcNodeName, tnDepth, false);

    }

    /**
     * Gets a list of nodes with the specified tag name
     * @param toNode the node to begin looking for the elements from
     * @param tcNodeName the case sensitive node name to find
     * @return the list of nodes that match, or an empty list if there were no matching
     */
    public static List<Node> getElementsByTagName(Node toNode, String tcNodeName)
    {
        return getElementsByTagName(toNode, tcNodeName, 9999, false);
    }

    /**
     * Gets the value of a node of the specified element
     * @param toNode the node to begin looking for the elements from
     * @param tcEleemnt the element whose value we would like to retrieve
     * @return the value of the element, or an empty string if there is no match
     */
    public static String getElementValue(Node toNode, String tcElement)
    {
        List<Node> loNodes = Goliath.XML.Utilities.getElementsByTagName(toNode, tcElement);
        return loNodes.size() > 0 ? loNodes.get(0).getTextContent(): "";
    }

    /**
     * Gets the value of an attribute or an empty string if the attribute doesn't exist
     * @param toNode the node to get the value from
     * @param tcAttributeName the case sensitive name of the attribute
     * @return the value to return
     */
    public static String getAttributeValue(Node toNode, String tcAttributeName)
    {
        Node loNode = toNode.getAttributes().getNamedItem(tcAttributeName);
        if (loNode == null && Goliath.XML.Utilities.toString(toNode).toLowerCase().indexOf(tcAttributeName.toLowerCase()) >= 0)
        {
            NamedNodeMap loMap = toNode.getAttributes();
            // Nothing was found the quick way, so now try for a case insensitive search
            for (int i = 0, lnLength = loMap.getLength(); i < lnLength; i++)
            {
                if (loMap.item(i).getNodeName().toLowerCase().equalsIgnoreCase(tcAttributeName))
                {
                    return loMap.item(i).getTextContent();
                }
            }
        }
        return (loNode == null) ? "" : loNode.getTextContent();
    }

    /**
     * Removes the XML header fromt he string passed in
     * @param tcXMLString the string to remove the header from
     * @return the string without the header
     */
    public static String stripXMLHeader(String tcXMLString)
    {
        return Goliath.Utilities.getRegexMatcher("\\<\\?xml.+?>", tcXMLString).replaceAll("");
    }

    /**
     * Strips out the XML header and the xsl stylesheet from the string
     * @param tcXMLString the string to remove the xsl stylesheet wrapping
     * @return the string without xsl stylesheet wrapping elements
     */
    public static String stripXSLT(String tcXMLString)
    {
        return Goliath.Utilities.getRegexMatcher("(?:\\<\\?xml.+?>)|(?:\\<xsl:(output|stylesheet)\\s[^<]*)|(?:\\</xsl:stylesheet>)", tcXMLString).replaceAll("");
    }

    public static File toFile(Document toDocument, String tcFileName)
    {
        try
        {
            return toFile(toDocument, tcFileName, true);
        }
        catch (FileAlreadyExistsException ex)
        {
            // This will never occur
            return null;
        }
    }

    // TODO: Need to sort out a better way of reading/writing xml files.
    public static File toFile(Document toDocument, String tcFileName, boolean tlOverwrite)
            throws FileAlreadyExistsException
    {
        File loFile = new File(tcFileName);
        if (loFile.exists() && !tlOverwrite)
        {
            throw new Goliath.Exceptions.FileAlreadyExistsException(loFile);
        }
        else if (loFile.exists() && tlOverwrite)
        {
            loFile.delete();
        }

        try
        {
            // Prepare the DOM document for writing
            Source loSource = new DOMSource(toDocument);

            Result loResult = new StreamResult(loFile);

            // Write the DOM document to the file
            Transformer loTransformer = TransformerFactory.newInstance().newTransformer();
            loTransformer.transform(loSource, loResult);
        }
        catch (TransformerConfigurationException ex)
        {
            Goliath.Applications.Application.getInstance().log(ex);
        }
        catch (TransformerException ex)
        {
            Goliath.Applications.Application.getInstance().log(ex);
        }
        return loFile;
    }

    /**
     * Returns a string representation of an XML Document
     * @param toDocument the document to convert to a string
     * @return A string version of the XML Document
     */
    public static String toString(org.w3c.dom.Document toDocument)
    {
        try
        {
            javax.xml.transform.Transformer loTransformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();

            javax.xml.transform.dom.DOMSource loSource = new javax.xml.transform.dom.DOMSource(toDocument);

            java.io.StringWriter loWriter = new java.io.StringWriter();
            javax.xml.transform.stream.StreamResult loResult = new javax.xml.transform.stream.StreamResult(loWriter);
            loTransformer.transform(loSource, loResult);

            return loWriter.toString();
        }
        catch (Exception ex)
        {
            UncheckedException loEx = new Goliath.Exceptions.UncheckedException(ex);
        }
        return "";
    }

    /**
     * Gets the string representation of the node tree.
     * @param toNode the Node to get the string representation of
     * @return the String that represents the node
     */
    public static String toString(org.w3c.dom.Node toNode)
    {
        StringBuilder loBuilder = new StringBuilder("");
        toString(toNode, loBuilder);
        return loBuilder.toString();
    }

    /**
     * Appends the string representation of the node provided to the provided string builder
     * @param toNode the node to get the string representation of
     * @param loBuilder the builder to append the string to
     */
    public static void toString(org.w3c.dom.Node toNode, StringBuilder loBuilder)
    {
        switch (toNode.getNodeType())
        {
            case org.w3c.dom.Node.ELEMENT_NODE:     // Element
                loBuilder.append("<");
                loBuilder.append(toNode.getNodeName());
                for (int j = 0; j < toNode.getAttributes().getLength(); j++)
                {
                    org.w3c.dom.Node loAttribute = toNode.getAttributes().item(j);
                    loBuilder.append(" ");
                    loBuilder.append(loAttribute.getNodeName());
                    loBuilder.append("=\"");
                    loBuilder.append(loAttribute.getNodeValue());
                    loBuilder.append("\"");
                }
                loBuilder.append(">");
                for (int i = 0; i < toNode.getChildNodes().getLength(); i++)
                {
                    toString(toNode.getChildNodes().item(i), loBuilder);
                }
                loBuilder.append("</");
                loBuilder.append(toNode.getNodeName());
                loBuilder.append(">");
                break;
            case org.w3c.dom.Node.TEXT_NODE:
                loBuilder.append(toNode.getNodeValue());
                break;
            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                loBuilder.append("<![CDATA[");
                loBuilder.append(toNode.getNodeValue());
                loBuilder.append("]]>");
                break;
            case org.w3c.dom.Node.COMMENT_NODE:
                // DO Nothing for this at the moment
                break;
            default:
                int y = toNode.getNodeType();
        }
    }

    /**
     * Converts a collection of dataobjects to an xml string
     * @param toCollection the collection of data objects
     * @return an xml string with the data objects
     */
    public static <T extends Goliath.Object> String toXMLString(java.util.List<T> toCollection)
    {
        org.w3c.dom.Document toDocument = toXML(toCollection);
        if (toDocument != null)
        {
            return Goliath.XML.Utilities.toString(toXML(toCollection));
        }
        return "";
    }

    /**
     * Converts a collection of Data objects to an xml document
     * @param toCollection the collection of data objects
     * @return and xml document
     */
    public static <T extends Goliath.Object> org.w3c.dom.Document toXML(java.util.List<T> toCollection)
    {
        // TODO: This should use a Goliath collection interface rather than java.util.List<T>
        if (toCollection.isEmpty())
        {
            return null;
        }
        Goliath.Collections.List<String> loFields = Goliath.DynamicCode.Java.getPropertyMethods(toCollection.get(0).getClass());

        try
        {
            org.w3c.dom.Document loDoc = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            org.w3c.dom.Node loRoot = loDoc.createElement(toCollection.getClass().getSimpleName());
            loDoc.appendChild(loRoot);

            // Create a new node for each object
            for (T loObject : toCollection)
            {
                org.w3c.dom.Node loRow = loDoc.createElement(loObject.getClass().getSimpleName());
                loRoot.appendChild(loRow);

                for (String tcColumn: loFields)
                {
                    org.w3c.dom.Node loValue = loDoc.createElement(tcColumn);
                    loRow.appendChild(loValue);
                    java.lang.Object loReturnedValue = Goliath.DynamicCode.Java.getPropertyValue(loObject, tcColumn, false);
                    if (loReturnedValue == null)
                    {
                        loReturnedValue = "";
                    }
                    // For dates we actually want to return the time, javascript can
                    // take care of conversions
                    if (Goliath.Date.class.isAssignableFrom(loReturnedValue.getClass()))
                    {
                        loReturnedValue = new Long(((Goliath.Date)loReturnedValue).getDate().getTime());
                    }
                    else if (loReturnedValue.getClass().getSimpleName().equalsIgnoreCase("Date"))
                    {
                        loReturnedValue = new Long(((java.util.Date)loReturnedValue).getTime());
                    }
                    loValue.appendChild(loDoc.createTextNode(loReturnedValue.toString()));
                }
            }
            return loDoc;
        }
        catch(DOMException ex)
        {
            Exception loEX = new Goliath.Exceptions.Exception(ex);
        }
        catch(ParserConfigurationException ex)
        {
            Exception loEX = new Goliath.Exceptions.Exception(ex);
        }
        return null;
    }
}
