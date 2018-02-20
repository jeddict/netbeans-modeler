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
package org.netbeans.modeler.widget.properties.generic;

import java.lang.reflect.InvocationTargetException;
import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.config.element.ModelerSheetProperty;
import org.openide.nodes.PropertySupport;

public class ElementPropertySupport extends PropertySupport.Reflection implements ModelerSheetProperty {

    private final Object object;
    private String propertyName;
    private final Class classType;
    
    private Attribute attribute;

    public ElementPropertySupport(Object object, Class classType, String propertyName) throws NoSuchMethodException, NoSuchFieldException {
        super(object, classType, propertyName);

        this.object = object;
        this.propertyName = propertyName;
        this.classType = classType;

        this.setDisplayName(propertyName + ":");
        this.setShortDescription(" Description : " + propertyName);

    }

    public ElementPropertySupport(Object object, Class classType, String propertyName, String displayName, String description) throws NoSuchMethodException, NoSuchFieldException {
        super(object, classType, propertyName);

        this.object = object;
        this.propertyName = propertyName;
        this.classType = classType;

        this.setDisplayName(displayName);
        this.setShortDescription(description);

    }

    public ElementPropertySupport(Object object, Class classType, String getter, String setter, String displayName, String description) throws NoSuchMethodException {
        super(object, classType, getter, setter);

        this.object = object;
        this.classType = classType;

        this.setDisplayName(displayName);
        this.setShortDescription(description);

    }
    
     public ElementPropertySupport(Object object, String getter, String setter, Attribute attribute) throws NoSuchMethodException {
        this(object, attribute.getClassType(),getter, setter, attribute.getDisplayName(), attribute.getShortDescription());
        this.attribute=attribute;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        Object value = null;

        value = super.getValue();

        if (value == null) {
            if (classType == String.class) {
                value = "";
            } else if (classType == Boolean.class) {
                value = Boolean.FALSE;
            } 
        }
        return value;
    }



    @Override
    public String getBefore() {
        if(attribute!=null){
            return attribute.getBefore();
        }
        return null;
    }

    @Override
    public String getAfter() {
        if(attribute!=null){
            return attribute.getAfter();
        }
        return null;
    }
}
