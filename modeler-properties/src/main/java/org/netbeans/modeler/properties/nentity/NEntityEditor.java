/**
 * Copyright 2013-2022 Gaurav Gupta
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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.netbeans.modeler.properties.EntityComponent;
import org.netbeans.modeler.properties.spec.RowValue;
import org.openide.explorer.propertysheet.PropertyEnv;

public class NEntityEditor extends JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private PropertyEditor editor;
    private NAttributeEntity attributeEntity;
    private TableModel tableModel;

    public NEntityEditor() {
        this.setSize(1900, 900);
    }

    public static NEntityEditor createInstance(JLayeredPane container, int witdh, int height) {
        container.removeAll();
        NEntityEditor editor = new NEntityEditor();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(container);
        container.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(editor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, witdh, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, height, Short.MAX_VALUE)
                                .addContainerGap())
        );
        container.setLayer(editor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        return editor;
    }

    public NEntityEditor(NAttributeEntity attributeEntity, PropertyEditor editor, PropertyEnv env) {
        if (env != null) {
            this.env = env;
            this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            this.env.addPropertyChangeListener(this);
        }
        this.editor = editor;
        this.setSize(1900, 900);
        setAttributeEntity(attributeEntity);
    }

    public void setAttributeEntity(NAttributeEntity attributeEntity) {
        this.attributeEntity = attributeEntity;
        initComponents();
        if (attributeEntity.getCustomDialog() == null) {
            jButtonNewProperty.hide();
            jButtonModifyProperty.hide();
        }
        this.setInternalAttributeEntity(attributeEntity);
    }

    /**
     * @param attributeEntity the attributeEntity to set
     */
    private void setInternalAttributeEntity(NAttributeEntity attributeEntity) {

        attributeEntity.getTableDataListener().initData();
        if (attributeEntity.getCustomDialog() != null) {
            attributeEntity.getCustomDialog().setRootComponent(attributeTable);
        }
        tableModel = new javax.swing.table.DefaultTableModel(
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
        };
        attributeTable.setModel(tableModel);

        updateTableUI();

        for (Column column : attributeEntity.getColumns()) {
            if (column.isHidden()) {
                attributeTable.getColumn(column.getName()).setPreferredWidth(0);
                attributeTable.getColumn(column.getName()).setMinWidth(0);
                attributeTable.getColumn(column.getName()).setWidth(0);
                attributeTable.getColumn(column.getName()).setMaxWidth(0);
            } else {
                if (column.getWidth() != -1) {
                    attributeTable.getColumn(column.getName()).setPreferredWidth(column.getWidth());
                    attributeTable.getColumn(column.getName()).setWidth(column.getWidth());
                }
            }

        }

        DefaultListSelectionModel dlsm = (DefaultListSelectionModel) this.attributeTable.getSelectionModel();
        dlsm.addListSelectionListener(this::jTablePropertiesListSelectionValueChanged);

        int i = 0;
        for (Column column : attributeEntity.getColumns()) {
            if (!column.getValues().isEmpty()) {
                comboloader(attributeTable.getColumnModel().getColumn(i), column);
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

    private Object[] getRow(int index) {
        DefaultTableModel dtm = (DefaultTableModel) attributeTable.getModel();
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
        DefaultTableModel dtm = (DefaultTableModel) attributeTable.getModel();
        List<Object[]> data = new LinkedList<>();
        for (int i = 0; i < dtm.getRowCount(); ++i) {
            data.add(getRow(i));
        }
        return data;
    }

    public void jTablePropertiesListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (attributeEntity.isEnableActionPanel()) {
            if (this.attributeTable.getSelectedRowCount() <= 0) {
                this.jButtonModifyProperty.setEnabled(false);
                this.jButtonDeleteProperty.setEnabled(false);
            } else {
                this.jButtonModifyProperty.setEnabled(true);
                this.jButtonDeleteProperty.setEnabled(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        actionPanelWrapper = new javax.swing.JPanel();
        action_Panel = new javax.swing.JLayeredPane();
        jButtonNewProperty = new javax.swing.JButton();
        jButtonModifyProperty = new javax.swing.JButton();
        jButtonDeleteProperty = new javax.swing.JButton();
        contentPane = new javax.swing.JScrollPane();
        attributeTable = new javax.swing.JTable();

        rootPanel.setLayout(new java.awt.BorderLayout());

        actionPanelWrapper.setMinimumSize(new java.awt.Dimension(100, 10));
        actionPanelWrapper.setPreferredSize(new java.awt.Dimension(100, 32));

        action_Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButtonNewProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/icon_plus.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButtonNewProperty, org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonNewProperty.text")); // NOI18N
        jButtonNewProperty.setToolTipText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonNewProperty.toolTipText")); // NOI18N
        jButtonNewProperty.setActionCommand(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonNewProperty.actionCommand")); // NOI18N
        jButtonNewProperty.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButtonNewProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewPropertyActionPerformed(evt);
            }
        });

        jButtonModifyProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButtonModifyProperty, org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonModifyProperty.text")); // NOI18N
        jButtonModifyProperty.setToolTipText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonModifyProperty.toolTipText")); // NOI18N
        jButtonModifyProperty.setActionCommand(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonModifyProperty.actionCommand")); // NOI18N
        jButtonModifyProperty.setEnabled(false);
        jButtonModifyProperty.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButtonModifyProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyPropertyActionPerformed(evt);
            }
        });

        jButtonDeleteProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/delete.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButtonDeleteProperty, org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonDeleteProperty.text")); // NOI18N
        jButtonDeleteProperty.setToolTipText(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonDeleteProperty.toolTipText")); // NOI18N
        jButtonDeleteProperty.setActionCommand(org.openide.util.NbBundle.getMessage(NEntityEditor.class, "NEntityEditor.jButtonDeleteProperty.actionCommand")); // NOI18N
        jButtonDeleteProperty.setEnabled(false);
        jButtonDeleteProperty.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButtonDeleteProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePropertyActionPerformed(evt);
            }
        });

        action_Panel.setLayer(jButtonNewProperty, javax.swing.JLayeredPane.DEFAULT_LAYER);
        action_Panel.setLayer(jButtonModifyProperty, javax.swing.JLayeredPane.DEFAULT_LAYER);
        action_Panel.setLayer(jButtonDeleteProperty, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout action_PanelLayout = new javax.swing.GroupLayout(action_Panel);
        action_Panel.setLayout(action_PanelLayout);
        action_PanelLayout.setHorizontalGroup(
            action_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(action_PanelLayout.createSequentialGroup()
                .addComponent(jButtonNewProperty, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonModifyProperty, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteProperty, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        action_PanelLayout.setVerticalGroup(
            action_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(action_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonNewProperty)
                .addComponent(jButtonModifyProperty)
                .addComponent(jButtonDeleteProperty))
        );

        javax.swing.GroupLayout actionPanelWrapperLayout = new javax.swing.GroupLayout(actionPanelWrapper);
        actionPanelWrapper.setLayout(actionPanelWrapperLayout);
        actionPanelWrapperLayout.setHorizontalGroup(
            actionPanelWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelWrapperLayout.createSequentialGroup()
                .addComponent(action_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        actionPanelWrapperLayout.setVerticalGroup(
            actionPanelWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelWrapperLayout.createSequentialGroup()
                .addComponent(action_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        rootPanel.add(actionPanelWrapper, java.awt.BorderLayout.NORTH);

        contentPane.setPreferredSize(new java.awt.Dimension(32767, 32767));
        contentPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contentPaneMouseClicked(evt);
            }
        });

        attributeTable.setModel(new javax.swing.table.DefaultTableModel(
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
        attributeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                attributeTableMouseClicked(evt);
            }
        });
        contentPane.setViewportView(attributeTable);

        rootPanel.add(contentPane, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewPropertyActionPerformed
        EntityComponent attrDialog = attributeEntity.getCustomDialog();

        if (!attrDialog.isLoaded()) {
            attrDialog.postConstruct();
            attrDialog.setLoaded();
        }
        attrDialog.init();
        attrDialog.createEntity(RowValue.class);
        attrDialog.setVisible(true);

        DefaultTableModel dtm = (DefaultTableModel) attributeTable.getModel();
        if (attrDialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Object[] row = ((RowValue) attrDialog.getEntity()).getRow();
            dtm.addRow(row);
            updateTableUI();
        }
    }//GEN-LAST:event_jButtonNewPropertyActionPerformed

    private void jButtonModifyPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyPropertyActionPerformed
        int index = attributeTable.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) attributeTable.getModel();

        EntityComponent attrDialog = attributeEntity.getCustomDialog();

        if (attrDialog != null) {
            RowValue rowValue = new RowValue(getRow(index));

            if (!attrDialog.isLoaded()) {
                attrDialog.postConstruct();
                attrDialog.setLoaded();
            }
            attrDialog.init();
            attrDialog.updateEntity(rowValue);

            attrDialog.setVisible(true);

            if (attrDialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                Object[] row = ((RowValue) attrDialog.getEntity()).getRow();
                int i = 0;
                for (Object value : row) {
                    dtm.setValueAt(value, index, i++);
                }

                updateTableUI();
            }
        }
    }//GEN-LAST:event_jButtonModifyPropertyActionPerformed

    private void jButtonDeletePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePropertyActionPerformed
        int[] rows = attributeTable.getSelectedRows();
        DefaultTableModel dtm = (DefaultTableModel) attributeTable.getModel();
        for (int i = rows.length - 1; i >= 0; --i) {
            dtm.removeRow(rows[i]);
        }
        updateTableUI();
    }//GEN-LAST:event_jButtonDeletePropertyActionPerformed

    private void attributeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_attributeTableMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && attributeTable.getSelectedRow() >= 0) {
            if (attributeEntity.isEnableActionPanel()) {
                jButtonModifyPropertyActionPerformed(new java.awt.event.ActionEvent(jButtonModifyProperty, 0, ""));
            } else {
                jButtonModifyPropertyActionPerformed(null);
            }

        }
    }//GEN-LAST:event_attributeTableMouseClicked

    private void contentPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contentPaneMouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_contentPaneMouseClicked

    public void updateTableUI() {
        int columnIndex = 0;
        for (Column column : attributeEntity.getColumns()) {
            if (column.isAutoIncrement()) {
                for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
                    tableModel.setValueAt(column.getAutoIncrementSufix() + rowIndex, rowIndex, columnIndex);
                }
            }
            columnIndex++;
        }
        attributeTable.updateUI();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanelWrapper;
    private javax.swing.JLayeredPane action_Panel;
    private javax.swing.JTable attributeTable;
    private javax.swing.JScrollPane contentPane;
    private javax.swing.JButton jButtonDeleteProperty;
    private javax.swing.JButton jButtonModifyProperty;
    private javax.swing.JButton jButtonNewProperty;
    private javax.swing.JPanel rootPanel;
    // End of variables declaration//GEN-END:variables
}
