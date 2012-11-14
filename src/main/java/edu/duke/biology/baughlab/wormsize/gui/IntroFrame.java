/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;

import edu.duke.biology.baughlab.wormsize.*;
import edu.duke.biology.baughlab.wormsize.xml.*;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JFileChooser;
import java.io.File;
import ij.IJ;
import javax.swing.event.HyperlinkEvent;

/**
 *
 * @author bradleymoore
 */
public class IntroFrame extends javax.swing.JFrame implements java.beans.PropertyChangeListener {
    protected File userSettingsDir;
    protected File userSettingsFile;
    protected UserSettingsType userSettings;
    protected SettingsFrame sf;
    protected ReviewFrame rf;
    
    /**
     * Creates new form IntroFrame
     */
    public IntroFrame() {
        initComponents();               
        
        setTitle(String.format("WormSizer: (ver. %s)", getClass().getPackage().getImplementationVersion()));
        
        // consider moving this logic - should be an error that hits the user immediately
        userSettingsDir = new File(System.getProperty("user.home") + File.separator + ".wormsizer");
        userSettingsDir.mkdirs(); // sloppy, but should work fine
        userSettingsFile = new File(userSettingsDir.getAbsolutePath() + File.separator + "user_settings.xml");
        
        runButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                runButtonPressed();
            }
        });
        
        reviewButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                reviewButtonPressed();
            }
        });
        
        editorPane.getCaret().setVisible(false);
        editorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                try {
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    }
                }
                catch (Exception ex) {
                    // do nothing
                }
            }
        });
        
        if (userSettingsFile.exists()) {
            try {
                userSettings = BindingFactory.unmarshal(userSettingsFile, new UserSettingsType());
            }
            catch (BindingFactoryException e) {
                userSettings = UserSettingsDefault.getInstance();
                //throw new IntroFrameException(String.format("Could not read, loading default: %s", userSettingsFile.getAbsolutePath()), e);
                IJ.log(String.format("Could not read, loading default: %s", userSettingsFile.getAbsolutePath()));
            }
        }
        else {
            userSettings = UserSettingsDefault.getInstance();
        }
    }

    protected void runButtonPressed()
    {   
        sf = new SettingsFrame(userSettingsFile, userSettings);
        sf.addPropertyChangeListener(SettingsFrame.STATUS_PROPERTY, this);
        this.setVisible(false);
        java.awt.EventQueue.invokeLater(new java.lang.Runnable() {
            @Override
            public void run() {
                sf.setVisible(true);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == sf && evt.getPropertyName().equals(SettingsFrame.STATUS_PROPERTY) && (evt.getNewValue().equals(SettingsFrame.STATUS_DONE) || evt.getNewValue().equals(SettingsFrame.STATUS_CANCEL))) {
            this.setVisible(true);
        }
        else if (evt.getSource() == rf && evt.getPropertyName().equals(ReviewFrame.STATUS_PROPERTY) && evt.getNewValue().equals(ReviewFrame.STATUS_DONE)) {
            this.setVisible(true);
        }
    }
    
    protected void reviewButtonPressed()
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Open WormSizer XML File");
        jfc.setFileFilter(new XMLFileFilter());
        jfc.setCurrentDirectory(new File ((userSettings.getReviewDefaultDirectory() == null) ? "" : userSettings.getReviewDefaultDirectory()));
        int ret = jfc.showOpenDialog(jfc);
        
        if (ret == JFileChooser.APPROVE_OPTION) {
            rf = new ReviewFrame(jfc.getSelectedFile());
            rf.addPropertyChangeListener(ReviewFrame.STATUS_PROPERTY, this);
            
            // update the default directory
            userSettings.setReviewDefaultDirectory(jfc.getSelectedFile().getParent());
            try {
                BindingFactory.marshal(userSettingsFile, userSettings, new ObjectFactory().createUserSettings(userSettings));
            }
            catch (BindingFactoryException e) {
                throw new IntroFrameException("Could not write to settings file", e);
            }
            
            this.setVisible(false);
            rf.setVisible(true);
            rf.start();
        }
        
    }
    
    protected static class IntroFrameException extends RuntimeException {

        public IntroFrameException(String message, Throwable cause) {
            super(message, cause);
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();
        reviewButton = new javax.swing.JButton();
        runButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("WormSizer");

        editorPane.setEditable(false);
        editorPane.setEditorKit(new  javax.swing.text.html.HTMLEditorKit());
        editorPane.setText("<html>\n<body>\nWormSizer is developed at the <a href=\"http://www.biology.duke.edu/baughlab/\">Baugh Lab</a>.<br/>\n<br/>\nInstructions can be found <a href=\"http://www.duke.edu/~bm93/wormsizer.html\">here</a>.<br/>\n<br/>\nWormSizer is free to use and open source.  If you use WormSizer results in your work, please cite us:<br/>\n<a href=\"\">[To Be Added]</a>\n</body>\n</html>");
        editorPane.setFocusable(false);
        jScrollPane1.setViewportView(editorPane);

        reviewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mag1.png"))); // NOI18N
        reviewButton.setToolTipText("Review Results");

        runButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward.png"))); // NOI18N
        runButton.setToolTipText("Analyze Images");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(runButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(reviewButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 37, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 364, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(53, 53, 53)
                        .add(runButton)
                        .add(18, 18, 18)
                        .add(reviewButton)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(34, 34, 34)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(IntroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new IntroFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton reviewButton;
    private javax.swing.JButton runButton;
    // End of variables declaration//GEN-END:variables
}
