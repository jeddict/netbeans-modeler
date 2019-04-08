/**
 * Copyright 2013-2019 Gaurav Gupta
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

import java.util.List;
import java.util.Map;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;

/**
 *
 *
 */
public interface IElementConfigFactory {

    public void init();

    public ElementConfig getElementConfig();

    public List<Element> getElements(String id);

    public List<Element> getElements(Class<?> classType);

    public Element getElement(Class<?> classType);

    public void initializeObjectValue(final Object object/*TBaseElement baseElement*/); // this method used to initialize object default value of JAXB from XML instead of hardcoding in object

    public void createPropertySet(ElementPropertySet set, final Object object, final Map<String, PropertyChangeListener<?>> propertyChangeHandlers);

    public void createPropertySet(ElementPropertySet set, final Object object, final Map<String, PropertyChangeListener<?>> propertyChangeHandlers, boolean inherit);

}
