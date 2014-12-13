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
package org.netbeans.modeler.properties.entity.custom.editor.combobox.internal;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import org.netbeans.modeler.locale.I18n;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.Entity;

public abstract class EntityComponent<T> extends javax.swing.JDialog {

    private Component rootComponent;//implemented for table but todo for combobox
    private int dialogResult;

    public EntityComponent(String title, boolean modal) {
        this((Frame) null, title, modal);
    }

    public EntityComponent(java.awt.Frame parent, boolean modal) {
        this(parent, "", modal);
    }

    public EntityComponent(java.awt.Frame parent, String title, boolean modal) {
        super(parent, title, modal);

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

    private Entity<T> entity;

    public abstract void init();

    public abstract void createEntity(Class<? extends Entity> entityWrapperType);

    public abstract void updateEntity(Entity<T> entity);

    public Entity<T> getEntity() {
        return entity;
    }

    public void setEntity(Entity<T> entity) {
        this.entity = entity;
    }

    /**
     * Closes the dialog
     *
     * @param evt
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
     * @return the dialogResult
     */
    public int getDialogResult() {
        return dialogResult;
    }

    /**
     * @param dialogResult the dialogResult to set
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    /**
     * @return the rootComponent
     */
    public Component getRootComponent() {
        return rootComponent;
    }

    /**
     * @param rootComponent the rootComponent to set
     */
    public void setRootComponent(Component rootComponent) {
        this.rootComponent = rootComponent;
    }

}
