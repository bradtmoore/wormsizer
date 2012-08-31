/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.xml;

import edu.duke.biology.baughlab.wormsize.BindingFactory;
import edu.duke.biology.baughlab.wormsize.BindingFactoryException;
import java.io.File;
import java.net.URI;
import java.io.InputStream;
/**
 *
 * @author bradleymoore
 */
public class UserSettingsDefault {
    protected static final String DEFAULT_PATH = "default-user_settings.xml";
    
    public static UserSettingsType getInstance() {
        UserSettingsType ans = null;
        URI uri = null;
        InputStream is = null;
        try {
            // loads the default file that is built into this jar
            //File defaultFile = new File(new UserSettingsDefault().getClass().getClassLoader().getResource(DEFAULT_PATH).toURI());      
            //uri = new UserSettingsDefault().getClass().getClassLoader().getResource(DEFAULT_PATH).toURI();
            is = new UserSettingsDefault().getClass().getClassLoader().getResourceAsStream(DEFAULT_PATH);
            //uri = UserSettingsDefault.class.getResource(DEFAULT_PATH).toURI();
            //uri = Thread.currentThread().getContextClassLoader().getResource(DEFAULT_PATH).toURI();
            //File defaultFile = new File(uri);
            ans = BindingFactory.unmarshal(is, new UserSettingsType());
        }
        catch (Exception e) {
            //String s = (uri == null)? "null" : uri.toString();
            throw new UserSettingsDefaultException(String.format("Could not open user settings resource"), e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e) {
                    
                }
            }
        }
        
        return ans;
    }
    
    protected static class UserSettingsDefaultException extends RuntimeException {

        public UserSettingsDefaultException(String message, Throwable cause) {
            super(message, cause);
        }
        
    }
}
