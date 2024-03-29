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
package org.netbeans.modeler.properties.combobox;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyEditor;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.properties.spec.ComboBoxValue;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

public class ComboBoxInplaceEditor implements InplaceEditor {

    private JComboBox comboBox;
    private JComponent comboBoxComponent;
    private PropertyEditor editor = null;
    private ComboBoxListener comboBoxListener;

    public ComboBoxInplaceEditor(final ModelerFile modelerFile, final ComboBoxListener comboBoxListener) {
        this.comboBoxListener = comboBoxListener;
        if (comboBoxListener.getActionHandler() == null) {
            comboBoxComponent = new JComboBox();
            comboBox = (JComboBox) comboBoxComponent;
        } else {
            comboBoxComponent = new JComboBoxPanel(modelerFile, comboBoxListener.getActionHandler());
            comboBox = ((JComboBoxPanel) comboBoxComponent).getComboBox();
        }
        comboBox.addItemListener((java.awt.event.ItemEvent event) -> {
            try {
                if (event.getStateChange() == ItemEvent.SELECTED && stateActive) {
                    comboBoxListener.setItem((ComboBoxValue) comboBox.getModel().getSelectedItem());
                }
                modelerFile.getModelerPanelTopComponent().changePersistenceState(false);
            } catch (Exception ex) {
                modelerFile.handleException(ex);
            }
        });

    }

    private boolean stateActive;

    @Override
    public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
        editor = propertyEditor;
        stateActive = false;
        reset();
        stateActive = true;
    }

    @Override
    public JComponent getComponent() {
        return comboBoxComponent;
    }

    @Override
    public void clear() {
        editor = null;
        model = null;
    }

    @Override
    public Object getValue() {
        ((JComponent) comboBoxComponent.getParent()).requestFocus();
        comboBoxComponent.updateUI();
        comboBoxComponent.repaint();
        return comboBox.getSelectedItem();
    }

    @Override
    public void setValue(Object object) {
//do nothing - logic implemented in itemStateChanged
    }

    @Override
    public boolean supportsTextEntry() {
        return true;
    }

    @Override
    public void reset() {
        List<ComboBoxValue> values = comboBoxListener.getItemList();
        comboBox.removeAllItems();
        for (ComboBoxValue comValue : values) {
            comboBox.addItem(comValue);
        }
        ComboBoxValue str = (ComboBoxValue) editor.getValue();
        if (str != null) {
            comboBox.setSelectedItem(str);
        }

    }

    @Override
    public KeyStroke[] getKeyStrokes() {
        return new KeyStroke[0];
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        return editor;
    }

    @Override
    public PropertyModel getPropertyModel() {
        return model;
    }
    private PropertyModel model;

    @Override
    public void setPropertyModel(PropertyModel propertyModel) {
        this.model = propertyModel;
    }

    @Override
    public boolean isKnownComponent(Component component) {
        return component == comboBoxComponent || comboBoxComponent.isAncestorOf(component);
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
//do nothing - not needed for this component
    }

    @Override
    public void removeActionListener(ActionListener actionListener) {
//do nothing - not needed for this component
    }
}
