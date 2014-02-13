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
package org.netbeans.modeler.widget.properties.generic;

import java.lang.reflect.InvocationTargetException;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.nodes.PropertySupport;

/**
 *
 *
 */
public class ElementCustomPropertySupport<T extends Object> extends PropertySupport.Reflection<T> {// PropertySupport.ReadOnly {

    Object object = null;
    String propertyName = null;
    Class<T> classType = null;
    PropertyChangeListener propertyChangeListener;
    private ModelerFile modelerFile;

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType, String propertyName, PropertyChangeListener propertyChangeListener) throws NoSuchMethodException, NoSuchFieldException {
        super(object, classType, propertyName);
        this.modelerFile = modelerFile;
        this.object = object;
        this.propertyName = propertyName;
        this.classType = classType;
        this.propertyChangeListener = propertyChangeListener;

        this.setDisplayName(propertyName);
        this.setShortDescription(propertyName);

    }

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType, String propertyName, String displayName, String description, PropertyChangeListener propertyChangeListener) throws NoSuchMethodException, NoSuchFieldException {
        super(object, classType, propertyName);
        this.modelerFile = modelerFile;
        this.object = object;
        this.propertyName = propertyName;
        this.classType = classType;
        this.propertyChangeListener = propertyChangeListener;

        this.setDisplayName(displayName);
        this.setShortDescription(description);

    }

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType, String getter, String setter, String displayName, String description, PropertyChangeListener propertyChangeListener) throws NoSuchMethodException {
        super(object, classType, getter, setter);
        this.modelerFile = modelerFile;
        this.object = object;
        //this.propertyName = propertyName;
        this.classType = classType;
        this.propertyChangeListener = propertyChangeListener;

        this.setDisplayName(displayName);
        this.setShortDescription(description);

    }

    @Override
    public T getValue() throws IllegalAccessException, InvocationTargetException {
        T value = super.getValue();
        if (value == null) {
            if (classType == String.class) {
                value = (T) "";
            } else if (classType == Boolean.class) {
                value = (T) Boolean.FALSE;
            }
        }
        return value;
    }

    @Override
    public void setValue(T t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super.setValue(t);
        propertyChangeListener.changePerformed(t);
        modelerFile.getModelerPanelTopComponent().changePersistenceState(false);

    }
}
