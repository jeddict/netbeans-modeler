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
package org.netbeans.modeler.config.element;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.Exceptions;

public class ElementConfigFactory {

    private ElementConfig elementConfig;

    public ElementConfigFactory(ElementConfig elementConfig) {
        this.elementConfig = elementConfig;
    }

    public ElementConfig getElementConfig() {
        return elementConfig;
    }

    public List<Element> getElements(String id) {
        List<Element> elements = new LinkedList<>();
        for (Element element_TMP : elementConfig.getElements()) {
            if (element_TMP.getId().equals(id)) {
                elements.add(element_TMP);
            }
        }
        return elements;
    }

    public List<Element> getElements(Class classType) {
        return getElements(null , classType);
    }
    
    public List<Element> getElements(String category, Class classType) {
        List<Element> elements = new LinkedList<>();
        for (Element element_TMP : elementConfig.getElements()) {
            if (element_TMP.getClassType() == null) {
                throw new IllegalArgumentException(element_TMP.getId() + " class not found");
            }
            if ((StringUtils.equalsIgnoreCase(category, element_TMP.getCategory())
                    || StringUtils.equalsIgnoreCase(category, element_TMP.getId()))
                    && element_TMP.getClassType().isAssignableFrom(classType)) { //all super class
                elements.add(element_TMP);
            }
        }
        return elements;
    }
    
    public Element getElement(Class classType) {
        return getElement(null, classType);
    }
    
    public Element getElement(String category, Class classType) {
        for (Element element_TMP : elementConfig.getElements()) {
            if (StringUtils.equals(category, element_TMP.getCategory()) &&  element_TMP.getClassType() == classType) {
                return element_TMP;
            }
        }
        return null;
    }

    public void initializeObjectValue(final Object object) { // this method used to initialize object default value of JAXB from XML instead of hardcoding in object
        try {
            for (Element element : getElements(object.getClass())) {
                for (final Attribute attribute : element.getAttributes()) {
                    if (attribute.getValue() != null && !attribute.getValue().trim().isEmpty()) {
                        BeanUtils.setProperty(object, attribute.getName(), attribute.getValue());
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
