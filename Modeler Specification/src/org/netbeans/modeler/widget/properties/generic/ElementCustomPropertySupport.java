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
import org.apache.commons.lang.StringUtils;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

/**
 *
 *
 * @param <T>
 */
public class ElementCustomPropertySupport<T> extends PropertySupport.Reflection<T> {// PropertySupport.ReadOnly {

//    private Object object = null;
//    private String propertyName = null;
    private Class<T> classType = null;
    private PropertyChangeListener propertyChangeListener;
    private PropertyVisibilityHandler propertyVisibilityHandler;
    private ModelerFile modelerFile;
    private String propertyId;

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType, String propertyId,String propertyName, PropertyChangeListener propertyChangeListener) throws NoSuchMethodException, NoSuchFieldException {
        this(modelerFile, object, classType, propertyId, propertyName, propertyName, propertyName, propertyChangeListener);
    }

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType, String propertyId,String propertyName, String displayName, String description, PropertyChangeListener propertyChangeListener) throws NoSuchMethodException, NoSuchFieldException {
        this(modelerFile, object, classType, propertyId, propertyName, displayName, description, propertyChangeListener, null);
    }

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType,String propertyId, String propertyName, String displayName, String description, PropertyChangeListener propertyChangeListener, PropertyVisibilityHandler propertyVisibilityHandler) throws NoSuchMethodException, NoSuchFieldException {
        super(object, classType, propertyName);
        this.modelerFile = modelerFile;
        this.propertyId = propertyId;
//        this.propertyName = propertyName;
        this.classType = classType;
        this.propertyChangeListener = propertyChangeListener;
        this.propertyVisibilityHandler = propertyVisibilityHandler;
        this.setDisplayName(displayName);
        this.setShortDescription(description);

    }

    public ElementCustomPropertySupport(ModelerFile modelerFile, Object object, Class<T> classType, String getter, String setter,String propertyId, String displayName, String description, PropertyChangeListener propertyChangeListener) throws NoSuchMethodException {
        super(object, classType, getter, setter);
        this.modelerFile = modelerFile;
        this.propertyId = propertyId;
//        this.object = object;
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
        if (propertyChangeListener != null) {
            propertyChangeListener.changePerformed(t);
        }
        modelerFile.getModelerPanelTopComponent().changePersistenceState(false);

    }

    /**
     * @return the propertyVisibilityHandler
     */
    public PropertyVisibilityHandler getPropertyVisibilityHandler() {
        return propertyVisibilityHandler;
    }

    /**
     * @param propertyVisibilityHandler the propertyVisibilityHandler to set
     */
    public void setPropertyVisibilityHandler(PropertyVisibilityHandler propertyVisibilityHandler) {
        this.propertyVisibilityHandler = propertyVisibilityHandler;
    }
    
    public String getName(){
        if(StringUtils.isBlank(propertyId)){
            return super.getName();
        }
        return propertyId;
    }
}
// class BooleanWrapper {
//    private Boolean booleanValue;
//    public Boolean getBooleanValue() {
//        return booleanValue;
//    }
//    public void setBooleanValue(Boolean booleanValue) {
//        this.booleanValue=booleanValue;
//    }
//}
