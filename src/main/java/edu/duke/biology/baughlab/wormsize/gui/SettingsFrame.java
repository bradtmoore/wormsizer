/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import edu.duke.biology.baughlab.wormsize.xml.*;
import edu.duke.biology.baughlab.wormsize.BindingFactory;
import edu.duke.biology.baughlab.wormsize.BindingFactoryException;
import edu.duke.biology.baughlab.wormsize.ImageFileFilter;
import edu.duke.biology.baughlab.wormsize.WormSizerWorker;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * This class manages the workflow of configuring and running WormSizer on a single or collection of directories.  It returns
 * a property change event when the user cancels or processing is complete.
 * 
 * @author bradleymoore
 */
public class SettingsFrame extends javax.swing.JFrame implements java.beans.PropertyChangeListener {


    public static final String STATUS_PROPERTY = "STATUS";
    public static final String STATUS_CANCEL = "CANCEL";
    public static final String STATUS_DONE = "DONE";
    protected IValidate[] panels;
    protected int cur;
    protected File userFile;
    protected UserSettingsType userSettings;
    protected AdditionalPanel ap;
    protected DirectoryPanel dp;
    protected ResolutionPanel rp;
    
    /**
     * Creates new form SettingsFrame
     */
    public SettingsFrame(File userFile, UserSettingsType userSettings) {
        initComponents();
        this.userFile = userFile;
        this.userSettings = userSettings;
        
        
        dp = new DirectoryPanel(userSettings.getDirectorySettings());
        rp = new ResolutionPanel(userSettings.getResolutionSettings());
        ap = new AdditionalPanel(userSettings);
        panels = new IValidate[]{dp, rp, ap};        
        setCur(0);
        
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPressed();
            }
        });
        
        backButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                backPressed();
            }
        });
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                
                tryCancel();
            }

            
            
//            @Override
//            public void windowClosed(WindowEvent e) {
//                tryCancel();
//            }
//            
        });
        
        settingPanel.add((JPanel)panels[cur]);
        //settingPanel.validate();
        validate();
        repaint();
        
    }
    
    protected void updateSettingText(int c) {
        String[] ss = new String[]{"Choose Directory","Set Resolution","Set Additional Options"};
        if (c >= 0 && c < ss.length) {
            ss[c] = String.format("<b>%s</b>", ss[c]);            
            stepList.setText(String.format("<html><body>%s<br/>%s<br/>%s<br/></body></html>", ss[0], ss[1], ss[2]));        
        }
    }
    
    protected void setCur(int c) 
    {
        cur = c;
        backButton.setEnabled(cur != 0);
        updateSettingText(cur);
    }
    
    public void tryCancel() {
        int res = JOptionPane.showConfirmDialog(this, "Would you like to cancel?", "Cancel?", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            firePropertyChange(STATUS_PROPERTY, null, STATUS_DONE);
            dispose();
        }

    }
    
    public void nextPressed() {
        if (panels[cur].valid()) {
            writeUserSettings();
            setCur(cur + 1);
            if (cur >= panels.length) {
                AdditionalSettingsType ast = userSettings.getAdditionalSettings();
                WormSizerWorker ww = new WormSizerWorker(dp.getInputDirectories(), new File(userSettings.getDirectorySettings().getOutputDirectory()), userSettings.getResolutionSettings().getMicronsPerPixel(), ast.getRollingBallRadius(), ast.getThresholdMethod(), ast.getCloseRadius(), ast.getMinWormArea(), ast.getMaxWormArea(), ast.getMinTubeness());
                final ProgressFrame pf = new ProgressFrame(ww);
                pf.addPropertyChangeListener(ProgressFrame.STATUS_PROPERTY, this);
                setVisible(false);
                java.awt.EventQueue.invokeLater(new java.lang.Runnable() {
                    @Override
                    public void run() {                        
                        pf.setVisible(true);
                        pf.start();
                    }
                });
                //pf.setVisible(true);
                //pf.addPropertyChangeListener("RESULT", this);
                
                return;
                //firePropertyChange("RESULT",null,"done");
            }
            else {
                settingPanel.removeAll();
                settingPanel.add((JPanel)panels[cur]);
                //settingPanel.validate();
                validate();
                repaint();
            }
            
            // this is ugly...  but eh.
            if (cur == 2) {
                ArrayList<File> dirs = dp.getInputDirectories();
                File f = dirs.get(0).listFiles(new ImageFileFilter())[0];               
                ap.setPreviewPath(f);             
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(ProgressFrame.STATUS_PROPERTY) && (pce.getNewValue().equals(ProgressFrame.STATUS_DONE) || pce.getNewValue().equals(ProgressFrame.STATUS_CANCEL))) {
            firePropertyChange(STATUS_PROPERTY, null, STATUS_DONE);
            dispose();
        }
    }
    
    protected void writeUserSettings() {
        try {             
            BindingFactory.marshal(userFile, userSettings, new ObjectFactory().createUserSettings(userSettings));
        }
        catch (BindingFactoryException e) {
            throw new SettingsFrameException("Couldn't write user settings", e);
        }
    }

    public void backPressed() {
        setCur(cur - 1);
        
        settingPanel.removeAll();
        settingPanel.add((JPanel)panels[cur]);
        //settingPanel.validate();
        validate();
        repaint();
    }
    
    protected static class SettingsFrameException extends RuntimeException {

        public SettingsFrameException(String string, Throwable thrwbl) {
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

        nextButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        settingPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stepList = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        nextButton.setText(">>");

        backButton.setText("Back");

        settingPanel.setMinimumSize(new java.awt.Dimension(465, 245));
        settingPanel.setLayout(new javax.swing.BoxLayout(settingPanel, javax.swing.BoxLayout.LINE_AXIS));

        stepList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        stepList.setContentType("text/html");
        stepList.setText("<html>\n<body>\nChoose Directory<br/>\nSet Resolution<br/>\nSet Additional Options<br/>\n</body>\n</html>\n");
        jScrollPane2.setViewportView(stepList);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(settingPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(backButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(nextButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(settingPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nextButton)
                    .add(backButton)))
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
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        UserSettingsType ust = null;
        try {
            ust = BindingFactory.unmarshal(new File("./test-resources/user.xml"), new UserSettingsType());
        }
        catch (BindingFactoryException e) {
            e.printStackTrace();
        }
        
        File f = new File(System.getProperty("user.home") + File.separator + ".wormsizer" + File.separator + "user.xml");
        
        final SettingsFrame sf = new SettingsFrame(f, ust);
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                sf.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel settingPanel;
    private javax.swing.JEditorPane stepList;
    // End of variables declaration//GEN-END:variables
}
