/**
 * Copyright [2014] Gaurav Gupta
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
package org.netbeans.modeler.properties.nentity;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.RowValue;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.internal.EntityComponent;
import org.openide.explorer.propertysheet.PropertyEnv;

public class NEntityEditor extends JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private PropertyEditor editor;

    private NAttributeEntity attributeEntity;

    public NEntityEditor() {
        this.setSize(1900, 900);
    }

    public NEntityEditor(NAttributeEntity attributeEntity, PropertyEditor editor, PropertyEnv env) {
        if (env != null) {
            this.env = env;
            this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            this.env.addPropertyChangeListener(this);
        }
        this.editor = editor;
        this.setSize(1900, 900);
        this.attributeEntity = attributeEntity;
        initComponents();
        this.setInternalAttributeEntity(attributeEntity);
    }

    public void setAttributeEntity(NAttributeEntity attributeEntity) {
        this.attributeEntity = attributeEntity;
        initComponents();
        this.setInternalAttributeEntity(attributeEntity);
    }

    /**
     * @param attributeEntity the attributeEntity to set
     */
    private void setInternalAttributeEntity(NAttributeEntity attributeEntity) {

        attributeEntity.getTableDataListener().initData();
        attributeEntity.getCustomDialog().setRootComponent(jTableAttribute);
        jTableAttribute.setModel(new javax.swing.table.DefaultTableModel(
                attributeEntity.getTableDataListener().getData().toArray(new Object[][]{}),
                attributeEntity.getColumnsName().toArray(new String[0])) {
                    Class[] types = NEntityEditor.this.attributeEntity.getColumnsType().toArray(new Class[0]);
                    Boolean[] canEdit = NEntityEditor.this.attributeEntity.getColumnsEditable().toArray(new Boolean[0]);

                    @Override
                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                });

        for (Column column : attributeEntity.getColumns()) {
            if (column.isHidden()) {
                jTableAttribute.getColumn(column.getName()).setPreferredWidth(0);
                jTableAttribute.getColumn(column.getName()).setMinWidth(0);
                jTableAttribute.getColumn(column.getName()).setWidth(0);
                jTableAttribute.getColumn(column.getName()).setMaxWidth(0);
            } else {
                if (column.getWidth() != -1) {
                    jTableAttribute.getColumn(column.getName()).setPreferredWidth(column.getWidth());
                    jTableAttribute.getColumn(column.getName()).setWidth(column.getWidth());
//                    jTableAttribute.getColumn(column.getName()).getWidth()
                }
            }

        }

        DefaultListSelectionModel dlsm = (DefaultListSelectionModel) this.jTableAttribute.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                jTablePropertiesListSelectionValueChanged(e);
            }
        });

//       this.setTableData(attributeEntity.getData());
//
        int i = 0;
        for (Column column : attributeEntity.getColumns()) {
            if (!column.getValues().isEmpty()) {
                comboloader(jTableAttribute.getColumnModel().getColumn(i), column);
            }
            i++;
        }
    }

    private void comboloader(TableColumn gradeColumn, Column column) {
        JComboBox comboBox = new JComboBox();
        comboBox.removeAllItems();
        comboBox.setModel(new javax.swing.DefaultComboBoxModel(column.getValues().toArray()));
        gradeColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        action_Panel_Wrapper = new javax.swing.JPanel();
        action_Panel = new javax.swing.JLayeredPane();
        jButtonNewProperty = new javax.swing.JButton();
        jButtonModifyProperty = new javax.swing.JButton();
        jButtonDeleteProperty = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableAttribute = new javax.swing.JTable();

        rootPanel.setLayout(new java.awt.BorderLayout());

        action_Panel_Wrapper.setMinimumSize(new java.awt.Dimension(100, 10));
        action_Panel_Wrapper.setPreferredSize(new java.awt.Dimension(100, 32));

        action_Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButtonNewProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/widget/properties/resource/icon_plus.png"))); // NOI18N
        jButtonNewProperty.setText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonNewProperty.text")); // NOI18N
        jButtonNewProperty.setToolTipText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonNewProperty.toolTipText")); // NOI18N
        jButtonNewProperty.setActionCommand(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonNewProperty.actionCommand")); // NOI18N
        jButtonNewProperty.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButtonNewProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewPropertyActionPerformed(evt);
            }
        });

        jButtonModifyProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/widget/properties/resource/edit.png"))); // NOI18N
        jButtonModifyProperty.setText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonModifyProperty.text")); // NOI18N
        jButtonModifyProperty.setToolTipText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonModifyProperty.toolTipText")); // NOI18N
        jButtonModifyProperty.setActionCommand(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonModifyProperty.actionCommand")); // NOI18N
        jButtonModifyProperty.setEnabled(false);
        jButtonModifyProperty.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButtonModifyProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyPropertyActionPerformed(evt);
            }
        });

        jButtonDeleteProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/widget/properties/resource/delete.png"))); // NOI18N
        jButtonDeleteProperty.setText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonDeleteProperty.text")); // NOI18N
        jButtonDeleteProperty.setToolTipText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonDeleteProperty.toolTipText")); // NOI18N
        jButtonDeleteProperty.setActionCommand(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonDeleteProperty.actionCommand")); // NOI18N
        jButtonDeleteProperty.setEnabled(false);
        jButtonDeleteProperty.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButtonDeleteProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePropertyActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout action_PanelLayout = new org.jdesktop.layout.GroupLayout(action_Panel);
        action_Panel.setLayout(action_PanelLayout);
        action_PanelLayout.setHorizontalGroup(
            action_PanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(action_PanelLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jButtonNewProperty)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonModifyProperty)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonDeleteProperty)
                .add(6, 6, 6))
        );
        action_PanelLayout.setVerticalGroup(
            action_PanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(action_PanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jButtonNewProperty)
                .add(jButtonModifyProperty)
                .add(jButtonDeleteProperty))
        );
        action_Panel.setLayer(jButtonNewProperty, javax.swing.JLayeredPane.DEFAULT_LAYER);
        action_Panel.setLayer(jButtonModifyProperty, javax.swing.JLayeredPane.DEFAULT_LAYER);
        action_Panel.setLayer(jButtonDeleteProperty, javax.swing.JLayeredPane.DEFAULT_LAYER);

        org.jdesktop.layout.GroupLayout action_Panel_WrapperLayout = new org.jdesktop.layout.GroupLayout(action_Panel_Wrapper);
        action_Panel_Wrapper.setLayout(action_Panel_WrapperLayout);
        action_Panel_WrapperLayout.setHorizontalGroup(
            action_Panel_WrapperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(action_Panel_WrapperLayout.createSequentialGroup()
                .add(action_Panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 245, Short.MAX_VALUE))
        );
        action_Panel_WrapperLayout.setVerticalGroup(
            action_Panel_WrapperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(action_Panel_WrapperLayout.createSequentialGroup()
                .add(action_Panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 5, Short.MAX_VALUE))
        );

        rootPanel.add(action_Panel_Wrapper, java.awt.BorderLayout.NORTH);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(32767, 32767));
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });

        jTableAttribute.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Default"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableAttribute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAttributeMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableAttribute);
        if (jTableAttribute.getColumnModel().getColumnCount() > 0) {
            jTableAttribute.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "CustomNAttributeEditor.jTableAttribute.columnModel.title1")); // NOI18N
        }

        rootPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(rootPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, attributeEntity.getTable().getWidth(), Short.MAX_VALUE)
                .add(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(rootPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,attributeEntity.getTable().getHeight(), Short.MAX_VALUE)
                .add(4, 4, 4))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTableAttributeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAttributeMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && jTableAttribute.getSelectedRow() >= 0) {
            if (attributeEntity.isEnableActionPanel()) {
                jButtonModifyPropertyActionPerformed(new java.awt.event.ActionEvent(jButtonModifyProperty, 0, ""));
            } else {
                jButtonModifyPropertyActionPerformed(null);
            }

        }
    }//GEN-LAST:event_jTableAttributeMouseClicked

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jButtonNewPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewPropertyActionPerformed
        EntityComponent attrDialog = attributeEntity.getCustomDialog();//new CustomAttributeDialog(Misc.getMainFrame(), true);
//        jrpd.setProperties(getPropertiesList());
        attrDialog.init();
        attrDialog.createEntity(RowValue.class);
        attrDialog.setVisible(true);

        DefaultTableModel dtm = (DefaultTableModel) jTableAttribute.getModel();
        if (attrDialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Object[] row = ((RowValue) attrDialog.getEntity()).getRow();
            dtm.addRow(row);
            jTableAttribute.updateUI();
//            data.add(row);

        }
    }//GEN-LAST:event_jButtonNewPropertyActionPerformed

    private void jButtonModifyPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyPropertyActionPerformed
        int index = jTableAttribute.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jTableAttribute.getModel();

        EntityComponent attrDialog = attributeEntity.getCustomDialog();

        RowValue rowValue = new RowValue(getRow(index));
//       rowValue.setTableModel(dtm);

        attrDialog.init();
        attrDialog.updateEntity(rowValue);

        attrDialog.setVisible(true);

        if (attrDialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Object[] row = ((RowValue) attrDialog.getEntity()).getRow();
            int i = 0;
            for (Object value : row) {
                dtm.setValueAt(value, index, i++);
            }
            jTableAttribute.updateUI();
//            data.add(row);
        }
    }//GEN-LAST:event_jButtonModifyPropertyActionPerformed

    private void jButtonDeletePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePropertyActionPerformed
        int[] rows = jTableAttribute.getSelectedRows();
        DefaultTableModel dtm = (DefaultTableModel) jTableAttribute.getModel();
        for (int i = rows.length - 1; i >= 0; --i) {
            dtm.removeRow(rows[i]);  //jTableProperties.convertRowIndexToModel(rows[i])
        }
    }//GEN-LAST:event_jButtonDeletePropertyActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane action_Panel;
    private javax.swing.JPanel action_Panel_Wrapper;
    private javax.swing.JButton jButtonDeleteProperty;
    private javax.swing.JButton jButtonModifyProperty;
    private javax.swing.JButton jButtonNewProperty;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableAttribute;
    private javax.swing.JPanel rootPanel;
    // End of variables declaration//GEN-END:variables

    private Object[] getRow(int index) {
        DefaultTableModel dtm = (DefaultTableModel) jTableAttribute.getModel();
        int columnSize = attributeEntity.getColumns().size();
        Object[] row = new Object[columnSize];
        for (int j = 0; j < columnSize; j++) {
            row[j] = dtm.getValueAt(index, j);
        }
        return row;
    }

    @Override //Save Button Clicked
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {

            editor.setValue(getSavedModel());
        }
    }

    public List<Object[]> getSavedModel() {
        DefaultTableModel dtm = (DefaultTableModel) jTableAttribute.getModel();
        List<Object[]> data = new LinkedList<Object[]>();
        for (int i = 0; i < dtm.getRowCount(); ++i) {
            data.add(getRow(i));
        }
        return data;
        //CustomNAttributeSupport>setValue

    }

//    public void clearRow() {
//        DefaultTableModel defaultTableModel = (DefaultTableModel) jTableAttribute.getModel(); //remove previos session row/data
//        if (defaultTableModel != null) {
//            for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
//                defaultTableModel.removeRow(i);
//            }
//        }
//        defaultTableModel.addRow(new Object[]{});
//        jTableAttribute.updateUI();
//        defaultTableModel.removeRow(0);
//        jTableAttribute.updateUI();
//
////        jTableAttribute.setModel(new DefaultTableModel());
//    }
    public void jTablePropertiesListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (attributeEntity.isEnableActionPanel()) {
            if (this.jTableAttribute.getSelectedRowCount() <= 0) {
                this.jButtonModifyProperty.setEnabled(false);
                this.jButtonDeleteProperty.setEnabled(false);
            } else {
                this.jButtonModifyProperty.setEnabled(true);
                this.jButtonDeleteProperty.setEnabled(true);
            }
        }
    }

}
