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

import java.awt.Dimension;
import java.awt.Toolkit;
import org.netbeans.modeler.locale.I18n;

/**
 *
 *
 */
public abstract class DynamicEntityComponent extends javax.swing.JDialog {

    private NAttributeEntity attributeEntity;
    protected Object[] row;
    private int dialogResult;

//    @Override
    public Object[] getRow() {
        return row;
    }

//    @Override
    public void setRow(Object[] row) {
        this.row = row;
    }

    /**
     * @return the attributeEntity
     */
    public NAttributeEntity getAttributeEntity() {
        return attributeEntity;
    }

    public DynamicEntityComponent(NAttributeEntity attributeEntity) {
        this(attributeEntity, null/*Misc.getMainFrame()*/, true);
    }

    public DynamicEntityComponent(NAttributeEntity attributeEntity, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.attributeEntity = attributeEntity;
        row = new Object[attributeEntity.getColumns().size()];

        javax.swing.KeyStroke escape = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelActionPerformed(e);
            }
        };

        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);

        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - this.getWidth()) / 2;
            int y = (screenSize.height - this.getHeight()) / 2;
            this.setLocation(x, y);
        }
        super.setVisible(visible);
    }

    public abstract void createEntity();

    public abstract void updateEntity();

    /**
     * Closes the dialog
     */
    protected void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CLOSED_OPTION);
        dispose();
    }

    protected void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }

    protected void saveActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }

    /**
     * Getter for property dialogResult.
     *
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult() {
        return dialogResult;
    }

    /**
     * Setter for property dialogResult.
     *
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
}
