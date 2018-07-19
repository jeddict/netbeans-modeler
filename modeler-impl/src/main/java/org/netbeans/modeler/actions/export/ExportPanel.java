/**
 * Copyright 2013-2018 Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.modeler.actions.export;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.export.ExportType;
import org.netbeans.modeler.specification.export.IExportManager;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.tool.writer.DiagramImageWriter;
import org.netbeans.modeler.tool.writer.DocumentWriter;
import org.openide.DialogDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;

public class ExportPanel extends javax.swing.JPanel implements DocumentListener, ChangeListener, ItemListener {

    private DialogDescriptor descriptor;
    private IModelerScene scene;
    private double ratio = 1.0;

    public ExportPanel() {
        initComponents();

        qualitySlider.setMaximum(100);
        qualitySlider.setMinimum(1);
        qualitySlider.setValue(100);
        qualitySlider.addChangeListener(this);
        qualityField.setText(Integer.toString(100));

        widthField.getDocument().addDocumentListener(this);
        heightField.getDocument().addDocumentListener(this);
        qualityField.getDocument().addDocumentListener(this);

        setQualityComponentsEnabled(false);

        actualSizeBtn.addItemListener(this);
        fitInWindowBtn.addItemListener(this);
        currentZoomLevelBtn.addItemListener(this);
        customBtn.addItemListener(this);
    }

    private void setQualityComponentsEnabled(boolean enabled) {
        jPanel3.setEnabled(enabled);

        qualitySlider.setEnabled(enabled);
        qualityField.setEnabled(enabled);
        qualityLbl.setEnabled(enabled);
        highLbl.setEnabled(enabled);
        lowLbl.setEnabled(enabled);
    }

    public void setDialogDescriptor(DialogDescriptor d) {
        descriptor = d;
    }

    public void initValue(IModelerScene scene) {
        this.scene = scene;

        IExportManager exportManager = scene.getModelerFile().getModelerDiagramModel().getExportManager();
        DefaultComboBoxModel boxModel = new DefaultComboBoxModel(
                new ExportType[]{ExportType.png, ExportType.jpg});
        imageTypeComboBox.setModel(boxModel);
        if (exportManager != null) {
            exportManager.getExportType().forEach((file) -> {
                boxModel.addElement(file);
            });
        }
        manageVisibilityState();

        Rectangle sceneRec = scene.getPreferredBounds();
        Rectangle viewRect = scene.getView().getVisibleRect();

        widthField.getDocument().removeDocumentListener(this);
        heightField.getDocument().removeDocumentListener(this);
        if (fitInWindowBtn.isSelected()) {
            double scale = Math.min((double) viewRect.width / (double) sceneRec.width,
                    (double) viewRect.height / (double) sceneRec.height);
            widthField.setText(Integer.toString((int) ((double) sceneRec.width * scale)));
            heightField.setText(Integer.toString((int) ((double) sceneRec.height * scale)));
        } else if (actualSizeBtn.isSelected()) {
            widthField.setText(Integer.toString(sceneRec.width));
            heightField.setText(Integer.toString(sceneRec.height));
        } else if (currentZoomLevelBtn.isSelected()) {
            widthField.setText(Integer.toString((int) ((double) sceneRec.width * scene.getZoomFactor())));
            heightField.setText(Integer.toString((int) ((double) sceneRec.height * scene.getZoomFactor())));
        }
        widthField.getDocument().addDocumentListener(this);
        heightField.getDocument().addDocumentListener(this);

        ModelerFile modelerFile = scene.getModelerPanelTopComponent().getModelerFile();

        String ext;
        if (imageTypeComboBox.getSelectedItem() instanceof ExportType) {
            ext = ((ExportType) imageTypeComboBox.getSelectedItem()).getName();
        } else {
            ext = ((IExportManager.FileType) imageTypeComboBox.getSelectedItem()).getExtension();
        }

        String imageFile;
        File file = modelerFile.getFile();
        if(file!=null){
            imageFile = file.getParent() + File.separator + file.getName() + "." + ext;
        } else {
            imageFile = System.getProperty("user.home") + File.separator + "Export." + ext;
        }
        
        fileNameField.setText(imageFile);
        fileNameField.setCaretPosition(0);

        ratio = (double) sceneRec.height / (double) sceneRec.width;
    }

    private void setFileName(String ext) {
        String f = fileNameField.getText();
        int i = f.lastIndexOf(".");
        if (i > 0) {
            f = f.substring(0, i);
        }
        fileNameField.setText(f + "." + ext);
    }

    public void exportImage() {
        try {

            if (imageTypeComboBox.getSelectedItem() instanceof IExportManager.FileType) {
                DocumentWriter.write(scene, (IExportManager.FileType) imageTypeComboBox.getSelectedItem(), new File(fileNameField.getText()));
            } else {
//            File file = new File(fileNameField.getText());
                FileImageOutputStream os = new FileImageOutputStream(new File(fileNameField.getText()));
//            SceneExporter.ImageType sel = (ImageType) imageTypeComboBox.getSelectedItem();

                int zoomType = DiagramImageWriter.ACTUAL_SIZE;
//            SceneExporter.ZoomType zoomType = SceneExporter.ZoomType.ACTUAL_SIZE;
                if (currentZoomLevelBtn.isSelected()) {
                    zoomType = DiagramImageWriter.CURRENT_ZOOM_LEVEL;
//                zoomType = SceneExporter.ZoomType.CURRENT_ZOOM_LEVEL;
                } else if (actualSizeBtn.isSelected()) {
                    zoomType = DiagramImageWriter.ACTUAL_SIZE;
//                zoomType = SceneExporter.ZoomType.ACTUAL_SIZE;
                } else if (customBtn.isSelected()) {
                    zoomType = DiagramImageWriter.CUSTOM_SIZE;
//                zoomType = SceneExporter.ZoomType.CUSTOM_SIZE;
                } else if (fitInWindowBtn.isSelected()) {
                    zoomType = DiagramImageWriter.FIT_IN_WINDOW;
//                zoomType = SceneExporter.ZoomType.FIT_IN_WINDOW;
                }

//            boolean selectedOnly = selectedOnlyCheckBox.isSelected();
                boolean visibleAreaOnly = visibleOnlyCheckBox.isSelected();
                int quality = Integer.valueOf(qualityField.getText());
                int width = Integer.valueOf(widthField.getText());
                int height = Integer.valueOf(heightField.getText());

                DiagramImageWriter.write(scene,
                        ((ExportType) imageTypeComboBox.getSelectedItem()),
                        os, visibleAreaOnly, zoomType, false, quality, width, height);
            }
//            SceneExporter.createImage(scene, file, sel, zoomType, visibleAreaOnly, selectedOnly, quality, width, height);
        } catch (IOException e) {
        }
    }

    private void setValid(boolean valid) {
        descriptor.setValid(valid);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        if (e.getDocument() == widthField.getDocument()) {
            try {
                int w = Integer.parseInt(widthField.getText());
                heightField.setText(Integer.toString((int) (w * ratio)));
                setValid(true);
            } catch (Exception ex) {
                setValid(false);
            }
        } else if (e.getDocument() == heightField.getDocument()) {
            try {
                int h = Integer.parseInt(heightField.getText());
                widthField.setText(Integer.toString((int) (h / ratio)));
                setValid(true);
            } catch (Exception ex) {
                setValid(false);
            }
        } else if (e.getDocument() == qualityField.getDocument()) {
            try {
                int quality = Integer.parseInt(qualityField.getText());
                if (quality < 1 || quality > 100) {
                    setValid(false);
                    return;
                }
                qualitySlider.removeChangeListener(this);
                qualitySlider.setValue(quality);
                qualitySlider.addChangeListener(this);
                setValid(true);
            } catch (Exception ex) {
                setValid(false);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == qualitySlider) {
            qualityField.getDocument().removeDocumentListener(this);
            qualityField.setText(Integer.toString(qualitySlider.getValue()));
            qualityField.getDocument().addDocumentListener(this);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        Rectangle sceneRec = scene.getPreferredBounds();
        Rectangle viewRect = scene.getView().getVisibleRect();

        widthField.getDocument().removeDocumentListener(this);
        heightField.getDocument().removeDocumentListener(this);
        if (event.getSource() == customBtn) {
            widthField.setEditable(customBtn.isSelected());
            heightField.setEditable(customBtn.isSelected());
        } else if (event.getSource() == fitInWindowBtn) {
            double scale = Math.min((double) viewRect.width / (double) sceneRec.width,
                    (double) viewRect.height / (double) sceneRec.height);
            widthField.setText(Integer.toString((int) ((double) sceneRec.width * scale)));
            heightField.setText(Integer.toString((int) ((double) sceneRec.height * scale)));
        } else if (event.getSource() == actualSizeBtn) {
            widthField.setText(Integer.toString(sceneRec.width));
            heightField.setText(Integer.toString(sceneRec.height));
        } else if (event.getSource() == currentZoomLevelBtn) {
            widthField.setText(Integer.toString((int) ((double) sceneRec.width * scene.getZoomFactor())));
            heightField.setText(Integer.toString((int) ((double) sceneRec.height * scene.getZoomFactor())));
        }
        widthField.getDocument().addDocumentListener(this);
        heightField.getDocument().addDocumentListener(this);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        imageTypeLbl = new javax.swing.JLabel();
        fileNameLbl = new javax.swing.JLabel();
        imageTypeComboBox = new javax.swing.JComboBox();
        fileNameField = new javax.swing.JTextField();
        browseBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        visibleOnlyCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lowLbl = new javax.swing.JLabel();
        qualitySlider = new javax.swing.JSlider();
        highLbl = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        qualityLbl = new javax.swing.JLabel();
        qualityField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        currentZoomLevelBtn = new javax.swing.JRadioButton();
        actualSizeBtn = new javax.swing.JRadioButton();
        fitInWindowBtn = new javax.swing.JRadioButton();
        customBtn = new javax.swing.JRadioButton();
        widthLbl = new javax.swing.JLabel();
        heightLbl = new javax.swing.JLabel();
        widthField = new javax.swing.JTextField();
        heightField = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ExportPanel.class, "LBL_ExportPanel_Image"))); // NOI18N

        imageTypeLbl.setLabelFor(imageTypeComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(imageTypeLbl, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.imageTypeLbl.text")); // NOI18N

        fileNameLbl.setLabelFor(fileNameField);
        org.openide.awt.Mnemonics.setLocalizedText(fileNameLbl, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.fileNameLbl.text")); // NOI18N

        imageTypeComboBox.setMaximumSize(new java.awt.Dimension(300, 30));
        imageTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                imageTypeComboBoxItemStateChanged(evt);
            }
        });
        imageTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageTypeComboBoxActionPerformed(evt);
            }
        });

        fileNameField.setColumns(20);

        org.openide.awt.Mnemonics.setLocalizedText(browseBtn, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.browseBtn.text")); // NOI18N
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileNameLbl)
                    .addComponent(imageTypeLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageTypeComboBox, 0, 242, Short.MAX_VALUE)
                    .addComponent(fileNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browseBtn)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imageTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imageTypeLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileNameLbl)
                    .addComponent(browseBtn)
                    .addComponent(fileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        imageTypeLbl.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.imageTypeLbl.AccessibleContext.accessibleName")); // NOI18N
        imageTypeLbl.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.imageTypeLbl.text")); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ExportPanel.class, "LBL_ExportPanel_ImageContent"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(visibleOnlyCheckBox, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.visibleOnlyCheckBox.text")); // NOI18N
        visibleOnlyCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                visibleOnlyCheckBoxStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(visibleOnlyCheckBox)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(visibleOnlyCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        visibleOnlyCheckBox.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.visibleOnlyCheckBox.AccessibleContext.accessibleName")); // NOI18N
        visibleOnlyCheckBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.visibleOnlyCheckBox.AccessibleContext.accessibleDescription")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ExportPanel.class, "LBL_ExportPanel_ImageQuality"))); // NOI18N

        jPanel5.setLayout(new java.awt.GridBagLayout());

        lowLbl.setLabelFor(qualitySlider);
        org.openide.awt.Mnemonics.setLocalizedText(lowLbl, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.lowLbl.text")); // NOI18N
        jPanel5.add(lowLbl, new java.awt.GridBagConstraints());

        qualitySlider.setMajorTickSpacing(5);
        qualitySlider.setPaintTicks(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(qualitySlider, gridBagConstraints);

        highLbl.setText(org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.highLbl.text")); // NOI18N
        highLbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel5.add(highLbl, new java.awt.GridBagConstraints());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        qualityLbl.setLabelFor(qualityField);
        org.openide.awt.Mnemonics.setLocalizedText(qualityLbl, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.qualityLbl.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        jPanel6.add(qualityLbl, gridBagConstraints);

        qualityField.setMinimumSize(new java.awt.Dimension(30, 19));
        qualityField.setPreferredSize(new java.awt.Dimension(30, 19));
        jPanel6.add(qualityField, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ExportPanel.class, "LBL_ExportPanel_ImageSize"))); // NOI18N

        buttonGroup1.add(currentZoomLevelBtn);
        org.openide.awt.Mnemonics.setLocalizedText(currentZoomLevelBtn, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.currentZoomLevelBtn.text")); // NOI18N

        buttonGroup1.add(actualSizeBtn);
        actualSizeBtn.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(actualSizeBtn, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.actualSizeBtn.text")); // NOI18N

        buttonGroup1.add(fitInWindowBtn);
        org.openide.awt.Mnemonics.setLocalizedText(fitInWindowBtn, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.fitInWindowBtn.text")); // NOI18N

        buttonGroup1.add(customBtn);
        org.openide.awt.Mnemonics.setLocalizedText(customBtn, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.customBtn.text")); // NOI18N

        widthLbl.setLabelFor(widthField);
        org.openide.awt.Mnemonics.setLocalizedText(widthLbl, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.widthLbl.text")); // NOI18N

        heightLbl.setLabelFor(heightField);
        org.openide.awt.Mnemonics.setLocalizedText(heightLbl, org.openide.util.NbBundle.getMessage(ExportPanel.class, "ExportPanel.heightLbl.text")); // NOI18N

        widthField.setEditable(false);
        widthField.setColumns(5);

        heightField.setEditable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customBtn)
                    .addComponent(currentZoomLevelBtn)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(actualSizeBtn)
                            .addComponent(fitInWindowBtn))
                        .addGap(86, 86, 86)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(heightLbl)
                            .addComponent(widthLbl))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(widthField, 0, 1, Short.MAX_VALUE)
                    .addComponent(heightField, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(currentZoomLevelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualSizeBtn)
                    .addComponent(widthLbl)
                    .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fitInWindowBtn)
                    .addComponent(heightLbl)
                    .addComponent(widthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
        JFileChooser chooser = new JFileChooser(scene.getName());
        FileUtil.preventFileChooserSymlinkTraversal(chooser, null);

        chooser.setDialogTitle(NbBundle.getMessage(ExportPanel.class, "LBL_Export_Image_Location")); // NOI18N
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String path = fileNameField.getText();

        if (path.length() > 0) {
            File f = new File(path);
            chooser.setSelectedFile(f);
        }

        String ext;
        if (imageTypeComboBox.getSelectedItem() instanceof ExportType) {
            ext = ((ExportType) imageTypeComboBox.getSelectedItem()).getName();
        } else {
            ext = ((IExportManager.FileType) imageTypeComboBox.getSelectedItem()).getExtension();
        }

        FileFilter filter = new ExportFilter(ext);
        chooser.setFileFilter(filter);
        chooser.setFileHidingEnabled(true);
        chooser.setApproveButtonText("Set");

        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
            File imageFile = chooser.getSelectedFile();
            fileNameField.setText(imageFile.getAbsolutePath());
        }

    }//GEN-LAST:event_browseBtnActionPerformed

    private void manageVisibilityState() {
        if (imageTypeComboBox.getSelectedItem() instanceof ExportType) {
            setQualityComponentsEnabled(imageTypeComboBox.getSelectedItem() == ExportType.jpg);
            jPanel2.setEnabled(true);
            jPanel4.setEnabled(true);
            visibleOnlyCheckBox.setEnabled(true);
            currentZoomLevelBtn.setSelected(visibleOnlyCheckBox.isSelected());
            for (Component c : jPanel4.getComponents()) {
                c.setEnabled(!visibleOnlyCheckBox.isSelected());
            }
        } else {
            setQualityComponentsEnabled(false);
            jPanel2.setEnabled(false);
            jPanel4.setEnabled(false);
            visibleOnlyCheckBox.setEnabled(false);

            currentZoomLevelBtn.setSelected(false);

            for (Component c : jPanel4.getComponents()) {
                c.setEnabled(false);
            }

        }
    }

    private void imageTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_imageTypeComboBoxItemStateChanged
        manageVisibilityState();
        if (imageTypeComboBox.getSelectedItem() instanceof ExportType) {
            setFileName(((ExportType) imageTypeComboBox.getSelectedItem()).getName());
        } else {
            setFileName(((IExportManager.FileType) imageTypeComboBox.getSelectedItem()).getExtension());
        }
    }//GEN-LAST:event_imageTypeComboBoxItemStateChanged

    private void visibleOnlyCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_visibleOnlyCheckBoxStateChanged
        if (visibleOnlyCheckBox.isSelected()) {
            currentZoomLevelBtn.setSelected(true);
        }
        for (Component c : jPanel4.getComponents()) {
            c.setEnabled(!visibleOnlyCheckBox.isSelected());
        }

    }//GEN-LAST:event_visibleOnlyCheckBoxStateChanged

    private void imageTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_imageTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_imageTypeComboBoxActionPerformed

    }//GEN-LAST:event_imageTypeComboBoxActionPerformed

    class ExportFilter extends FileFilter {

        private String imageType = null;

        public ExportFilter(String imageType) {
            this.imageType = imageType;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if ((imageType.equalsIgnoreCase("jpg")
                        && (extension.equals("jpeg") || extension.equals("jpg")))) {
                    return true;
                } else if (imageType.equalsIgnoreCase("png") && (extension.equals("png"))) {
                    return true;
                } else if ((extension.equals(imageType))) {
                    return true;
                }

            }

            return false;
        }

        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return imageType;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton actualSizeBtn;
    private javax.swing.JButton browseBtn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton currentZoomLevelBtn;
    private javax.swing.JRadioButton customBtn;
    private javax.swing.JTextField fileNameField;
    private javax.swing.JLabel fileNameLbl;
    private javax.swing.JRadioButton fitInWindowBtn;
    private javax.swing.JTextField heightField;
    private javax.swing.JLabel heightLbl;
    private javax.swing.JLabel highLbl;
    private javax.swing.JComboBox imageTypeComboBox;
    private javax.swing.JLabel imageTypeLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lowLbl;
    private javax.swing.JTextField qualityField;
    private javax.swing.JLabel qualityLbl;
    private javax.swing.JSlider qualitySlider;
    private javax.swing.JCheckBox visibleOnlyCheckBox;
    private javax.swing.JTextField widthField;
    private javax.swing.JLabel widthLbl;
    // End of variables declaration//GEN-END:variables
}
