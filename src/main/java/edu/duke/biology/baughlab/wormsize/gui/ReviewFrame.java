/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;
import java.io.*;
import edu.duke.biology.baughlab.wormsize.xml.*;
import edu.duke.biology.baughlab.wormsize.BindingFactory;
import edu.duke.biology.baughlab.wormsize.BindingFactoryException;
import edu.duke.biology.baughlab.wormsize.ReviewImage;
import ij.ImagePlus;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import java.awt.event.KeyListener;
import java.util.HashSet;

/**
 *
 * @author bradleymoore
 */
public class ReviewFrame extends javax.swing.JFrame {
    public static final String STATUS_PROPERTY = "STATUS";
    public static final String STATUS_DONE = "DONE";
    
    protected File outputFile;
    protected ExperimentOutputType eot;
    protected int curImage;
    protected int curWorm;
    protected ReviewImage reviewImage;
    
    /**
     * Creates new form ReviewFrame
     */
    public ReviewFrame(File outputFile) {
        initComponents();
        
        this.outputFile = outputFile;
        curImage = 0;
        curWorm = 0;
        reviewImage = null;               
        
        renderToggle.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (renderToggle.isSelected()) {
                    reviewImage.setRenderStyle(ReviewImage.HIDE_OVERLAY);
                    renderToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/worm2.png")));
                }
                else
                {
                    reviewImage.setRenderStyle(ReviewImage.DEFAULT);
                    renderToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/worm1.png")));
                }
            }
        });
        
        prevImageButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                decImage();
            }
        });
        
        prevWormButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                decWorm();
            }
        });
        
        nextWormButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                incWorm();
            }
        });
        
        nextImageButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                incImage();
            }
        });
        
        passToggle.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (passToggle.isSelected()) {
                    setPass(true);
                }
                else {
                    setPass(null);
                }
                
            }
        });
        
        failToggle.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (failToggle.isSelected()) {
                    setPass(false);
                }
                else {
                    setPass(null);
                }
                
            }
        });
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                tryCancel(false);
            }            
        });
        
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'p') {
                    setPass(true);
                }
                else if (e.getKeyChar() == 'f') {
                    setPass(false);
                }
                else if (e.getKeyChar() == 'w') {
                    renderToggle.doClick();
                }
            }            
        });
        
        setFocusable(true);
    }
    
    protected void setPass(Boolean pass) {
        eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().get(curWorm).setPass(pass);
        writeExperiment(eot);
        
        if (pass != null) {
            if (eot.getWormOutputs().getWormOutput().size() == curImage+1 && eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().size() == curWorm+1) {
                tryCancel(true);
            }
            else {
                incWorm();
            }
        }
    }
        
    protected void tryCancel(boolean finished) {
        String msg = "Would you like to quit reviewing images?";
        if (finished) {
            msg = "There are no more images to review.  Would you like to quit?";
        }
        int ret = JOptionPane.showConfirmDialog(this, msg, "Would you like to quit?", JOptionPane.OK_CANCEL_OPTION);
        if (ret == JOptionPane.OK_OPTION) {
            firePropertyChange(ReviewFrame.STATUS_PROPERTY, null, STATUS_DONE);
            if (reviewImage != null) {
                reviewImage.close();
            }
            dispose();
        }
    }
    
    protected void writeExperiment(ExperimentOutputType eot) {
        try {
            BindingFactory.marshal(outputFile, eot, new ObjectFactory().createExperimentOutput(eot));            
        }
        catch (BindingFactoryException e) {
            throw new ReviewFrameException("Could not save", e);
        }
        try {
            ExperimentOutputCSV.writeToCSV(eot, outputFile.getParentFile());           
        }
        catch (IOException e) {
            throw new ReviewFrameException("Error writing to CSV file", e);
        }
    }
    
    protected void checkButtons() {
        prevImageButton.setEnabled(curImage > 0);
        prevWormButton.setEnabled(curImage > 0 || curWorm > 0);
        
        nextWormButton.setEnabled(curWorm < eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().size() - 1 || curImage < eot.getWormOutputs().getWormOutput().size() - 1);
        nextImageButton.setEnabled(curImage < eot.getWormOutputs().getWormOutput().size() - 1);        
        
        Boolean pass = eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().get(curWorm).isPass();
        passToggle.setSelected(pass != null && pass);
        failToggle.setSelected(pass != null && !pass);
        //passToggle.setEnabled(!passToggle.isSelected());
        //failToggle.setEnabled(!failToggle.isSelected());
    }
    
    protected void incWorm() {
        if (curWorm < eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().size() - 1) {
            loadNextWorm();
        }
        else {
            incImage();
        }
        checkButtons();
    }
    
    protected void incImage() {
        curImage++;
        curWorm = 0;
        loadImage(curImage);
        checkButtons();
    }
    
    protected void decWorm() {
        if (curWorm > 0) {
            loadPrevWorm();            
        }
        else {
            decImage();
        }
        checkButtons();
    }
    
    protected void decImage() {
        curImage--;
        curWorm = eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().size() - 1;
        loadImage(curImage);
        reviewImage.renderOnly(curWorm);
        checkButtons();
    }
    
    public boolean hasPrev() {
        return curImage > 0 || curWorm > 0;
    }
    
    public boolean hasNext() {
        return curImage < eot.getWormOutputs().getWormOutput().size() - 1 || curWorm < eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().size() - 1;
    }
    
    public void start() {
        try {
            eot = BindingFactory.unmarshal(outputFile, new ExperimentOutputType());
        }
        catch (BindingFactoryException e) {
            throw new ReviewFrameException("Couldn't open file", e);
        }
        
        loadImage(curImage);
        checkButtons();
    }
    
    protected void loadImage(int i) {
        if (reviewImage != null) {
            reviewImage.close();
        }
        WormOutputType wo = eot.getWormOutputs().getWormOutput().get(i);
        ImagePlus ip = ij.IJ.openImage(outputFile.getParent() + File.separator + wo.getImageFile());
        reviewImage = new ReviewImage(wo, ip);        
        reviewImage.show();
        
        // move image to under toolbar
        reviewImage.getWindow().setLocation(this.getX(), this.getY() + this.getHeight());
        
        HashSet<Character> filteredKeys = new HashSet<Character>();
        filteredKeys.add('p');
        filteredKeys.add('f');
        filteredKeys.add('w');
        
        KeyListener[] listeners = reviewImage.getWindow().getKeyListeners();        
        for (int j = 0; j < listeners.length; j++) {
            reviewImage.getWindow().removeKeyListener(listeners[j]);
            reviewImage.getWindow().addKeyListener(new FilteredKeyListener(listeners[j], filteredKeys));
        }
                        
        reviewImage.getWindow().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'p') {
                    setPass(true);
                }
                else if (e.getKeyChar() == 'f') {
                    setPass(false);
                }
                else if (e.getKeyChar() == 'w') {
                    renderToggle.doClick();
                }
            }            
        });
        
        KeyListener[] listeners2 = reviewImage.getWindow().getCanvas().getKeyListeners();
        for (int j = 0; j < listeners2.length; j++) {
            reviewImage.getWindow().getCanvas().removeKeyListener(listeners2[j]);
            reviewImage.getWindow().addKeyListener(new FilteredKeyListener(listeners[j], filteredKeys));
        }
                                
        reviewImage.getWindow().getCanvas().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'p') {
                    setPass(true);
                }
                else if (e.getKeyChar() == 'f') {
                    setPass(false);
                }
                else if (e.getKeyChar() == 'w') {
                    renderToggle.doClick();
                }
            }            
        });
        
        checkTitle();
        ip.close();
        checkButtons();
    }
    
    public void checkTitle() {
        setTitle(String.format("Viewing Image %d of %d, Worm %d of %d", curImage+1, eot.getWormOutputs().getWormOutput().size(), curWorm+1, eot.getWormOutputs().getWormOutput().get(curImage).getWorms().getWorm().size()));
    }
    
    protected void loadPrevWorm() {       
        curWorm--;        
        reviewImage.renderPrev();
        checkTitle();
    }
    
    protected void loadNextWorm() {
        curWorm++;
        reviewImage.renderNext();
        checkTitle();
    }
    
    protected static class ReviewFrameException extends RuntimeException {

        public ReviewFrameException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        nextWormButton = new javax.swing.JButton();
        prevImageButton = new javax.swing.JButton();
        nextImageButton = new javax.swing.JButton();
        renderToggle = new javax.swing.JToggleButton();
        prevWormButton = new javax.swing.JButton();
        passToggle = new javax.swing.JToggleButton();
        failToggle = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        nextWormButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/f1.png"))); // NOI18N
        nextWormButton.setToolTipText("Next Worm");
        nextWormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextWormButtonActionPerformed(evt);
            }
        });

        prevImageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bb1.png"))); // NOI18N
        prevImageButton.setToolTipText("Previous Image");
        prevImageButton.setEnabled(false);
        prevImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevImageButtonActionPerformed(evt);
            }
        });

        nextImageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ff1.png"))); // NOI18N
        nextImageButton.setToolTipText("Next Image");

        renderToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/worm1.png"))); // NOI18N
        renderToggle.setToolTipText("Toggle Overlay");

        prevWormButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/b1.png"))); // NOI18N
        prevWormButton.setToolTipText("Previous Worm");
        prevWormButton.setEnabled(false);
        prevWormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevWormButtonActionPerformed(evt);
            }
        });

        passToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check1.png"))); // NOI18N
        passToggle.setToolTipText("Pass Worm");

        failToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/x1.png"))); // NOI18N
        failToggle.setToolTipText("Fail Worm");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(36, 36, 36)
                .add(prevImageButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(prevWormButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(nextWormButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nextImageButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(35, 35, 35)
                .add(renderToggle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(passToggle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(failToggle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(failToggle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(passToggle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(prevWormButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(nextImageButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(prevImageButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(nextWormButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(renderToggle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prevImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevImageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prevImageButtonActionPerformed

    private void nextWormButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextWormButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nextWormButtonActionPerformed

    private void prevWormButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevWormButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prevWormButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReviewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReviewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReviewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReviewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                //new ReviewFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton failToggle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nextImageButton;
    private javax.swing.JButton nextWormButton;
    private javax.swing.JToggleButton passToggle;
    private javax.swing.JButton prevImageButton;
    private javax.swing.JButton prevWormButton;
    private javax.swing.JToggleButton renderToggle;
    // End of variables declaration//GEN-END:variables
}
