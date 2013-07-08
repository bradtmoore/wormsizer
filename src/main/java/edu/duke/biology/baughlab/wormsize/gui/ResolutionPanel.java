/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.gui;
import edu.duke.biology.baughlab.wormsize.xml.*;
import edu.duke.biology.baughlab.wormsize.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
/**
 * Logic:
 * If micrometer file exists, zoom and objective will be available from what is
 * in the file.
 * If doesn't exist, zoom and objective will be blank, and micronsPerPixel will
 * be set by user.  And the file will be cleared.
 * 
 * @author bradleymoore
 */
public class ResolutionPanel extends javax.swing.JPanel implements IValidate {    
    protected ResolutionSettingsType rst;
    protected Micrometer micrometer;
    protected static final String DEFAULT_ZOOM = "0.0";
    
    /**
     * Creates new form ResolutionPanel
     */
    public ResolutionPanel(ResolutionSettingsType rst) {
        initComponents();
        this.rst = rst;
        
        micrometer = new Micrometer();
        if (micrometer.open(new File(rst.getMicrometerFile()))) {
            micrometerText.setText(rst.getMicrometerFile());
            hasMicrometerFile(micrometer);
            objectiveCombo.setSelectedItem(rst.getObjective());
            zoomText.setText(Double.toString(rst.getZoom()));
            computeMicronsPerPixel();    
        }
        else {
            micrometerText.setText("");
            micronsPerPixelText.setText(Double.toString(rst.getMicronsPerPixel()));
            noMicrometerFile();
            
        }
        
        micrometerText.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                micrometerTextChanged();
            }
        });
        
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                browseButtonPressed();
            }
        });                
        
        objectiveCombo.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (objectiveCombo.getSelectedIndex() > -1) {
                    computeMicronsPerPixel();
                }
            }
        });
        
        zoomText.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                computeMicronsPerPixel();
            }
        });
        
    }

    protected void browseButtonPressed()
    {
        JFileChooser jfc = new JFileChooser();
        int ret = jfc.showOpenDialog(this);
        if (ret == JFileChooser.CANCEL_OPTION) {
            return;
        }
        else {
            micrometerText.setText(jfc.getSelectedFile().getAbsolutePath());
            micrometerTextChanged();
        }
    }
    
    protected void computeMicronsPerPixel() {
        String obj = (String)objectiveCombo.getSelectedItem();
        double z = Double.parseDouble(zoomText.getText());
        micronsPerPixelText.setText(Double.toString(micrometer.getResolution(obj, z).pixelWidth));
    }
    
    protected void micrometerTextChanged() {
        micrometer = new Micrometer();
        if (micrometer.open(new File(micrometerText.getText()))) {            
            hasMicrometerFile(micrometer);
            computeMicronsPerPixel();
        }
        else {
            JOptionPane.showMessageDialog(this, String.format("Unable to open/parse micrometer file: %s", micrometerText.getText()));
            micrometerText.setText("");            
            noMicrometerFile();
        }
    }
    
    protected void hasMicrometerFile(Micrometer micrometer) {
        ArrayList<String> objectives = new ArrayList<String>();
        for (Object obj : micrometer.getObjectives()) {
            objectives.add((String)obj);
        }
        Collections.sort(objectives);
        objectiveCombo.removeAllItems();
        for (String s : objectives) {
            objectiveCombo.addItem(s);
        }
        
        objectiveCombo.setSelectedItem(objectives.get(0));
        //computeMicronsPerPixel();
    }
    
    protected void noMicrometerFile() {
        objectiveCombo.removeAllItems();
        zoomText.setText("1.0");
    }
    
    @Override
    public boolean valid() {
        if (! micrometerText.getText().isEmpty()) {
            rst.setMicrometerFile(micrometerText.getText());
            rst.setObjective((String)objectiveCombo.getSelectedItem());
            rst.setZoom(Double.parseDouble(zoomText.getText()));
        }
        
        rst.setMicronsPerPixel(Double.parseDouble(micronsPerPixelText.getText()));
        
        return true;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        objectiveCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        zoomText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        micrometerText = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        micronsPerPixelText = new javax.swing.JTextField();

        jLabel4.setText("jLabel4");

        objectiveCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Objective:");

        jLabel2.setText("Zoom:");

        zoomText.setText("jTextField1");

        jLabel3.setText("Micrometer File:");

        micrometerText.setText("jTextField2");

        browseButton.setText("Browse...");

        jLabel5.setText("Microns per Pixel:");

        micronsPerPixelText.setText("jTextField3");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(55, 55, 55))
                            .add(layout.createSequentialGroup()
                                .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(zoomText)
                                .add(objectiveCombo, 0, 123, Short.MAX_VALUE))
                            .add(micronsPerPixelText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(micrometerText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(browseButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(micrometerText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(browseButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(objectiveCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(zoomText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(micronsPerPixelText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addContainerGap(58, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField micrometerText;
    private javax.swing.JTextField micronsPerPixelText;
    private javax.swing.JComboBox objectiveCombo;
    private javax.swing.JTextField zoomText;
    // End of variables declaration//GEN-END:variables
}
