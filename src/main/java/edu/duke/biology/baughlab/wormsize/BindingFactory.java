package edu.duke.biology.baughlab.wormsize;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import javax.xml.bind.*;
import javax.xml.stream.*;
import javax.xml.namespace.*;
/**
 * This is a wrapper for the JAXB xml marshaller and unmarshaller that makes it a one-liner to read or write XML.
 * 
 * @author bradleymoore
 */
public class BindingFactory {
    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


    /**
     * Writes a JAXB generated class to the specified XML file
     * @param <E> The type of the JAX generated class
     * @param f The file to write to
     * @param t The instance to save to XML
     * @param je A JAXB element created with the ObjectFactory class
     * @throws BindingFactoryException 
     */
    public static <E> void marshal(File f, E t, JAXBElement<E> je) throws BindingFactoryException
    {
        try
        {
            JAXBContext jc = getJAXBContext(t); //JAXBContext.newInstance(t.getClass().getPackage().getName());
            Marshaller m = jc.createMarshaller();
            m.marshal(je, f);
        }
        catch (Exception e)
        {
            throw new BindingFactoryException(String.format("Error writing class %s to %s", t.getClass().getName(), f.getAbsolutePath()), e);
        }
    }

    /**
     * Ran into really weird issues trying to get this to work in ImageJ's environment.  This ensures we are using the right classloader.
     * @param <E>
     * @param tmp
     * @return
     * @throws JAXBException 
     */
    protected static <E> JAXBContext getJAXBContext(E tmp) throws JAXBException {
        JAXBContext jc = null;//JAXBContext.newInstance(tmp.getClass().getPackage().getName());
        if (jc == null ) {
            //ij.IJ.log("IN HERE DAMNIT");
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(new BindingFactory().getClass().getClassLoader()); //this.getClass().getClassLoader()); //THIS FIXED IT
                jc = JAXBContext.newInstance(tmp.getClass().getPackage().getName());
            } 
            finally {
                Thread.currentThread().setContextClassLoader(cl);
            }
        }
        
        return jc;
    } 

    
    /**
     * Read the given XML file into a JAXB generated class.
     * @param <E> The type of the class to write into.
     * @param is The stream containing XML.
     * @param tmp An example of the class.
     * @return
     * @throws BindingFactoryException 
     */
      public static <E> E unmarshal(InputStream is, E tmp) throws BindingFactoryException
    {
        E ans = null;
        javax.xml.bind.JAXBElement je = null;
        FileReader fr = null;
        javax.xml.stream.XMLStreamReader sr = null;
        try
        {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            JAXBContext jc = getJAXBContext(tmp);//JAXBContext.newInstance(tmp.getClass().getPackage().getName());
            Unmarshaller um = jc.createUnmarshaller();  
            sr = javax.xml.stream.XMLInputFactory.newFactory().createXMLStreamReader(is);                        
            je = um.unmarshal(sr, tmp.getClass());
            ans = (E)je.getValue();
        }
        catch (Exception e)
        {
            throw new BindingFactoryException(String.format("Error parsing stream into class %s", tmp.getClass().getName()), e);
        }
        finally
        {
            if (sr != null)
            {
                try
                {
                    sr.close();
                }
                catch (XMLStreamException e)
                {

                }
            }
            if (fr != null)
            {
                try
                {
                    fr.close();
                }
                catch (IOException e)
                {

                }
            }
        }

        return ans;
    }
    
    /**
     * Creates the object of type E represented by the XML file f.  This is a simple
     * wrapper around the JAXB marshalling classes.
     *
     * @param <E> the type of object to be unmarshalled
     * @param f the XML file to read
     * @param tmp a dummy object used to get type parameters (e.g. package name)
     * @return the unmarshalled object from f
     * @throws BindingFactoryException on IO or parsing errors
     */
    public static <E> E unmarshal(File f, E tmp) throws BindingFactoryException
    {
        E ans = null;
        javax.xml.bind.JAXBElement je = null;
        FileReader fr = null;
        javax.xml.stream.XMLStreamReader sr = null;
        try
        {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            JAXBContext jc = getJAXBContext(tmp);//JAXBContext.newInstance(tmp.getClass().getPackage().getName());
            Unmarshaller um = jc.createUnmarshaller();
            fr = new FileReader(f);
            sr = XMLInputFactory.newInstance().createXMLStreamReader(fr);
            je = um.unmarshal(sr, tmp.getClass());
            ans = (E)je.getValue();
        }
        catch (Exception e)
        {
            throw new BindingFactoryException(String.format("Error parsing %s into class %s", f.getAbsolutePath(), tmp.getClass().getName()), e);
        }
        finally
        {
            if (sr != null)
            {
                try
                {
                    sr.close();
                }
                catch (XMLStreamException e)
                {

                }
            }
            if (fr != null)
            {
                try
                {
                    fr.close();
                }
                catch (IOException e)
                {

                }
            }
        }

        return ans;
    }
}


