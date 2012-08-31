/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

/**
 *
 * @author bradleymoore
 */
public class FilteredKeyListener extends java.awt.event.KeyAdapter {

    protected KeyListener listener;
    protected Set<Character> filteredKeys;

    public FilteredKeyListener(KeyListener listener, Set<Character> filteredKeys) {
        this.listener = listener;
        this.filteredKeys = filteredKeys;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!filteredKeys.contains(e.getKeyChar())) {
            listener.keyPressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!filteredKeys.contains(e.getKeyChar())) {
            listener.keyTyped(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!filteredKeys.contains(e.getKeyChar())) {
            listener.keyReleased(e);
        }
    }
}
