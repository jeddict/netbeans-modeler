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
package org.netbeans.modeler.config.element;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.netbeans.modeler.specification.model.document.ITextElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.generic.ElementPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
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
        List<Element> elements = new LinkedList<Element>();
        for (Element element_TMP : elementConfig.getElements()) {
            if (element_TMP.getId().equals(id)) {
                elements.add(element_TMP);
            }
        }
        return elements;
    }

    public List<Element> getElements(Class classType) {
        List<Element> elements = new LinkedList<Element>();
        for (Element element_TMP : elementConfig.getElements()) {
            if (element_TMP.getClassType().isAssignableFrom(classType)) { //all super class
                elements.add(element_TMP);
            }
        }
        return elements;
    }

    public Element getElement(Class classType) {
        for (Element element_TMP : elementConfig.getElements()) {
            if (element_TMP.getClassType() == classType) {
                return element_TMP;
            }
        }
        return null;
    }

    public void initializeObjectValue(final Object object/*TBaseElement baseElement*/) { // this method used to initialize object default value of JAXB from XML instead of hardcoding in object
        try {
            for (Element element : getElements(object.getClass())) {
                for (final Attribute attribute : element.getAttributes()) {
                    if (attribute.getValue() != null && !attribute.getValue().trim().isEmpty()) {
                        BeanUtils.setProperty(object, attribute.getName(), attribute.getValue());
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void createPropertySet(ElementPropertySet set, final Object object, final Map<String, PropertyChangeListener> propertyChangeHandlers) {
        createPropertySet(set, object, propertyChangeHandlers, null, true);
    }

    public void createPropertySet(ElementPropertySet set, final Object object) {
        createPropertySet(set, object, null, null, true);
    }

    public void createPropertySet(ElementPropertySet set, final Object object, final Map<String, PropertyChangeListener> propertyChangeHandlers, boolean inherit) {
        createPropertySet(set, object, propertyChangeHandlers, null, true);
    }

    public void createPropertySet(ElementPropertySet set, final Object object, final Map<String, PropertyChangeListener> propertyChangeHandlers, final Map<String, PropertyVisibilityHandler> propertyVisiblityHandlers) {
        createPropertySet(set, object, propertyChangeHandlers, propertyVisiblityHandlers, true);
    }

    private void createPropertySet(ElementPropertySet set, final Object object, final Map<String, PropertyChangeListener> propertyChangeHandlers, final Map<String, PropertyVisibilityHandler> propertyVisiblityHandlers, boolean inherit) {
        if (inherit) {
            for (Element element : getElements(object.getClass())) {
                createPropertySetInternal(set, object, element, propertyChangeHandlers, propertyVisiblityHandlers);
            }
        } else {
            Element element = getElement(object.getClass());
            if (element != null) {
                createPropertySetInternal(set, object, element, propertyChangeHandlers, propertyVisiblityHandlers);
            }
        }
    }

    private void createPropertySetInternal(ElementPropertySet set, final Object object, Element element, final Map<String, PropertyChangeListener> propertyChangeHandlers, final Map<String, PropertyVisibilityHandler> propertyVisiblityHandlers) {

//        PropertyChangeHandler p = new PropertyChangeHandler<Object>() {
//    public void manage(Object value){}
//};
//        p.manage("");
        try {
            if (element != null) {
                for (final Attribute attribute : element.getAttributes()) {
                    if (attribute.getClassType() == ITextElement.class) {
                        final String name = attribute.getName();
                        final ITextElement expression = (ITextElement) PropertyUtils.getProperty(object, name);//return must not be null//(TExpression) PropertyUtils.getProperty(object, id) == null ? new TExpression() : (TExpression) PropertyUtils.getProperty(object, id);
//                        PropertyUtils.setProperty(object, id, expression);

                        set.put(attribute.getGroupId(), new ElementCustomPropertySupport(set.getModelerFile(), expression, String.class, "content",
                                attribute.getDisplayName(), attribute.getShortDescription(),
                                new PropertyChangeListener<String>() {
                                    @Override
                                    public void changePerformed(String value) {
                                        if (expression.getContent() == null || expression.getContent().isEmpty()) {
                                            try {
                                                PropertyUtils.setProperty(object, name, null);
                                            } catch (IllegalAccessException ex) {
                                                Exceptions.printStackTrace(ex);
                                            } catch (InvocationTargetException ex) {
                                                Exceptions.printStackTrace(ex);
                                            } catch (NoSuchMethodException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }
                                        }
                                        if (propertyChangeHandlers != null && propertyChangeHandlers.get(name) != null) {
                                            propertyChangeHandlers.get(name).changePerformed(value);
                                        }
                                    }
                                }, propertyVisiblityHandlers == null ? null : propertyVisiblityHandlers.get(attribute.getId())));

                    } else {
                        if (attribute.isReadOnly()) {
                            String value = BeanUtils.getProperty(object, attribute.getName());
                            if (value == null) {
                                BeanUtils.setProperty(object, attribute.getName(), attribute.getValue());
                            }
                            set.put(attribute.getGroupId(), new ElementPropertySupport(object, attribute.getClassType(), attribute.getFieldGetter(), null, attribute.getDisplayName(), attribute.getShortDescription()));
                        } else {
                            PropertyVisibilityHandler propertyVisibilityHandler = propertyVisiblityHandlers == null ? null : propertyVisiblityHandlers.get(attribute.getId());
                            set.put(attribute.getGroupId(), new ElementCustomPropertySupport(set.getModelerFile(), object, attribute.getClassType(),
                                    attribute.getName(), attribute.getDisplayName(), attribute.getShortDescription(),
                                    new PropertyChangeListener<Object>() {
                                        @Override
                                        public void changePerformed(Object value) {
                                            try {
                                                if (value != null) {
                                                    if (value instanceof String) {
                                                        if (!((String) value).isEmpty()) {
                                                            BeanUtils.setProperty(object, attribute.getName(), value);
                                                        } else {
                                                            BeanUtils.setProperty(object, attribute.getName(), null);
                                                        }
                                                    } else {
                                                        BeanUtils.setProperty(object, attribute.getName(), value);
                                                    }
                                                } else {
                                                    BeanUtils.setProperty(object, attribute.getName(), null);
                                                }
                                            } catch (IllegalAccessException ex) {
                                                Exceptions.printStackTrace(ex);
                                            } catch (InvocationTargetException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }
                                            if (propertyChangeHandlers != null && propertyChangeHandlers.get(attribute.getId()) != null) {
                                                propertyChangeHandlers.get(attribute.getId()).changePerformed(value);
                                            }
                                        }
                                    }, propertyVisibilityHandler));

                        }
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}
