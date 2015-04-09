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
package org.netbeans.modeler.properties.entity.custom.editor.combobox.client.support;

import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.listener.ComboBoxListener;
import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.mvel2.MVEL;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.internal.ComboBoxPropertyEditorSupport;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.ComboBoxValue;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.PropertySupport;

public class ComboBoxPropertySupport extends PropertySupport.ReadWrite<ComboBoxValue> {

    private ModelerFile modelerFile;
    private ComboBoxListener comboBoxListener;
    private PropertyEditor propertyEditor;
    private PropertyVisibilityHandler propertyVisibilityHandler;
    public ComboBoxPropertySupport(ModelerFile modelerFile, String id, String name, String description, ComboBoxListener comboBoxListener, PropertyVisibilityHandler propertyVisibilityHandler) {
        super(id, ComboBoxValue.class, name, description);
        this.modelerFile = modelerFile;
        this.comboBoxListener = comboBoxListener;
        this.propertyVisibilityHandler = propertyVisibilityHandler;
    }

    public ComboBoxPropertySupport(ModelerFile modelerFile, String id, String name, String description, ComboBoxListener comboBoxListener) {
        this(modelerFile, id, name, description, comboBoxListener, (PropertyVisibilityHandler)null);
    }
    
    
    public ComboBoxPropertySupport(ModelerFile modelerFile, String id, String name, String description, ComboBoxListener comboBoxListener , String visible , Object object) {
        super(id, ComboBoxValue.class, name, description);
        this.modelerFile = modelerFile;
        this.comboBoxListener = comboBoxListener;
        this.propertyVisibilityHandler = ElementPropertySet.createPropertyVisibilityHandler(modelerFile, object, visible);
    }
    
    @Override
    public ComboBoxValue getValue() throws IllegalAccessException, InvocationTargetException {
        return comboBoxListener.getItem();
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        if (propertyEditor == null) {
            propertyEditor = new ComboBoxPropertyEditorSupport(modelerFile, comboBoxListener);
        }
        return propertyEditor;

    }

    @Override
    public void setValue(ComboBoxValue newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//  value is set by listener , not here

    }

    /**
     * @return the propertyVisibilityHandler
     */
    public PropertyVisibilityHandler getPropertyVisibilityHandler() {
        return propertyVisibilityHandler;
    }

 
}
