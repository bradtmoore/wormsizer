/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;

import edu.duke.biology.baughlab.wormsize.xml.*;
import ij.ImagePlus;
import java.io.File;
import edu.duke.biology.baughlab.wormsize.ReviewImage;
import edu.duke.biology.baughlab.wormsize.WormImage;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import javax.swing.JOptionPane;

/**
 *
 * @author bradleymoore
 */
public class AdditionalPanel extends javax.swing.JPanel implements IValidate {

    protected UserSettingsType ust;
    protected AdditionalSettingsType ast;
    protected ReviewImage previewImage;
    protected File previewPath;
        
    /**
     * Creates new form AdditionalPanel
     */
    public AdditionalPanel(UserSettingsType ust) {
        initComponents();
        thresholdCombo.removeAllItems();
        for (String s : WormImage.METHODS) {
            thresholdCombo.addItem(s);
        }
        
        this.ust = ust;
        ast = ust.getAdditionalSettings();
        
        
        rollingText.setText(Double.toString(ast.getRollingBallRadius()));
        thresholdCombo.setSelectedItem(ast.getThresholdMethod());
        closeRadiusText.setText(Double.toString(ast.getCloseRadius()));
        minAreaText.setText(Double.toString(ast.getMinWormArea()));
        maxAreaText.setText(Double.toString(ast.getMaxWormArea()));
        minTubeText.setText(Double.toString(ast.getMinTubeness()));
        previewCheck.setSelected(ast.isDoPreview());
        reviewCheck.setSelected(ast.isDoReview());
        sampleIntervalText.setText(ast.getSampleInterval().toString());

        sampleIntervalText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
            
        });
        
        rollingText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });

        thresholdCombo.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });

        closeRadiusText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });

        minAreaText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });

        maxAreaText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });

        minTubeText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });

        previewCheck.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (valid()) {
                    checkAndShow();
                }
            }
        });
        previewImage = null;
    }

    public void setPreviewPath(File previewPath) {
        this.previewPath = previewPath;

        checkAndShow();
    }

    protected void checkAndShow() {
        if (previewCheck.isSelected() && valid()) {
            WormImage wi = new WormImage();
            ImagePlus ip = ij.IJ.openImage(previewPath.getAbsolutePath());
            wi.process(ip, ust.getResolutionSettings().getMicronsPerPixel(), ast.getRollingBallRadius(), ast.getThresholdMethod(), ast.getCloseRadius(), ast.getMinWormArea(), ast.getMaxWormArea(), ast.getMinTubeness(), previewPath.getName(), ast.getSampleInterval().intValue());
            ip.close();
            
            if (previewImage == null) {
                previewImage = new ReviewImage(wi.getWormOutput(), ij.IJ.openImage(previewPath.getAbsolutePath()));
            }
            else {
                previewImage.setWormOutput(wi.getWormOutput());
                
            }
            
            previewImage.show();
            previewImage.renderAll();
        }
    }

    public void hideImage() {
    }
//
//    public void setPreviewImage(File previewImage) {
//        if (this.previewImage != null) {
//            this.previewImage.close();
//        }
//        
//        this.previewImage = previewImage;
//    }

    @Override
    public boolean valid() {
        String ans = null;

        try {
            ast.setRollingBallRadius(Double.parseDouble(rollingText.getText()));
        } catch (NumberFormatException e) {
            ans = "Value must be a real number: Rolling Ball Radius";
        }

        try {
            ast.setCloseRadius(Double.parseDouble(closeRadiusText.getText()));
        } catch (NumberFormatException e) {
            ans = "Value must be a real number: Close Radius";
        }

        try {
            ast.setMinWormArea(Double.parseDouble(minAreaText.getText()));
        } catch (NumberFormatException e) {
            ans = "Value must be a real number: Minimum Area Filter";
        }

        try {
            ast.setMaxWormArea(Double.parseDouble(maxAreaText.getText()));
        } catch (NumberFormatException e) {
            ans = "Value must be a real number: Maximum Area Filter";
        }

        try {
            ast.setMinTubeness(Double.parseDouble(minTubeText.getText()));
        } catch (NumberFormatException e) {
            ans = "Value must be a real number: Minimum Tubeness Filter";
        }
        
     
        ast.setSampleInterval(new BigInteger(sampleIntervalText.getText()));
        

        ast.setThresholdMethod((String)this.thresholdCombo.getSelectedItem());
        if (ans != null) {
            JOptionPane.showMessageDialog(this, ans);
        }
        
        ast.setDoPreview(previewCheck.isSelected());
        ast.setDoReview(reviewCheck.isSelected());
        
        return ans == null;
    } 
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        rollingText = new javax.swing.JTextField();
        closeRadiusText = new javax.swing.JTextField();
        thresholdCombo = new javax.swing.JComboBox();
        minAreaText = new javax.swing.JTextField();
        maxAreaText = new javax.swing.JTextField();
        minTubeText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        previewCheck = new javax.swing.JCheckBox();
        reviewCheck = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        sampleIntervalText = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(463, 245));

        jLabel1.setText("Rolling Ball Radius:");

        jLabel2.setText("Threshold Method:");

        jLabel3.setText("Close Radius:");

        jLabel4.setText("Worm Area Filter:");

        jLabel5.setText("Minimum Tubeness Filter:");

        rollingText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        rollingText.setText("jTextField1");

        closeRadiusText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        closeRadiusText.setText("jTextField3");

        thresholdCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        thresholdCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thresholdComboActionPerformed(evt);
            }
        });

        minAreaText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        minAreaText.setText("jTextField2");
        minAreaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minAreaTextActionPerformed(evt);
            }
        });

        maxAreaText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        maxAreaText.setText("jTextField4");

        minTubeText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        minTubeText.setText("jTextField5");

        jLabel6.setText("to");

        previewCheck.setText("Preview");

        reviewCheck.setText("Review after Processing");
        reviewCheck.setEnabled(false);
        reviewCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewCheckActionPerformed(evt);
            }
        });

        jLabel7.setText("Sample Interval Width:");

        sampleIntervalText.setText("jTextField2");
        sampleIntervalText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleIntervalTextActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(294, 294, 294)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(rollingText)
                        .add(6, 6, 6))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, thresholdCombo, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(closeRadiusText)
                        .addContainerGap())))
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(reviewCheck)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(previewCheck)
                        .add(101, 101, 101))
                    .add(layout.createSequentialGroup()
                        .add(78, 78, 78)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(minTubeText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 78, Short.MAX_VALUE)
                                .add(minAreaText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(maxAreaText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel7)
                                    .add(jLabel1)
                                    .add(jLabel3)
                                    .add(jLabel2))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(sampleIntervalText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(rollingText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(thresholdCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(closeRadiusText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(maxAreaText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel6)
                        .add(minAreaText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel4)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(minTubeText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(51, 51, 51)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(previewCheck)
                            .add(reviewCheck)))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(sampleIntervalText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel7))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void thresholdComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thresholdComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thresholdComboActionPerformed

    private void minAreaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minAreaTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_minAreaTextActionPerformed

    private void reviewCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reviewCheckActionPerformed

    private void sampleIntervalTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleIntervalTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sampleIntervalTextActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField closeRadiusText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField maxAreaText;
    private javax.swing.JTextField minAreaText;
    private javax.swing.JTextField minTubeText;
    private javax.swing.JCheckBox previewCheck;
    private javax.swing.JCheckBox reviewCheck;
    private javax.swing.JTextField rollingText;
    private javax.swing.JTextField sampleIntervalText;
    private javax.swing.JComboBox thresholdCombo;
    // End of variables declaration//GEN-END:variables
}
