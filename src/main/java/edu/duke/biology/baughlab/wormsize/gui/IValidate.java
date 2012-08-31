/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;

/**
 *
 * @author bradleymoore
 */
public interface IValidate {
    /**
     * Validates the form and returns an error message if any.
     * 
     * @return null if the content is valid, a non-null string if there was an error.
     */
    public boolean valid();
}
