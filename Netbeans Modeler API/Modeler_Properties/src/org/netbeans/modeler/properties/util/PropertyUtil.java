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
package org.netbeans.modeler.properties.util;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.modeler.properties.embedded.EmbeddedPropertySupport;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.support.ComboBoxPropertySupport;
import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;

public class PropertyUtil {

    public static AbstractNode getNode(IBaseElementWidget baseElementWidget, AbstractNode node, String displayName, Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
        if (node == null) {
            node = new BasePropertyViewManager(baseElementWidget);
            node.setDisplayName(displayName);
        }
        return getNode(node, propertyVisibilityHandlerList);
    }

    public static AbstractNode getNode(AbstractNode node) {
//        for (Node.PropertySet propertySet : node.getPropertySets()) {
//            for (Node.Property property : propertySet.getProperties()) {
//                if (property.getClass() == NEntityPropertySupport.class) {
//                    NEntityPropertySupport attributeProperty = (NEntityPropertySupport) property;
//                    attributeProperty.getAttributeEntity().getTableDataListener().initCount();
//                }
//            }
//
//        }
        return node;
    }

    public static AbstractNode getNode(AbstractNode node, Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
//        AbstractNode baseNode = (AbstractNode) node;
        for (Node.PropertySet propertySet : node.getPropertySets()) {
            propertySet.setHidden(false);
            int hiddenPropertyCount = 0;
            for (Node.Property property : propertySet.getProperties()) {
                property.setHidden(false);
                if (property.getClass() == ElementCustomPropertySupport.class) {
                    ElementCustomPropertySupport elementCustomPropertySupport = (ElementCustomPropertySupport) property;
                    if (elementCustomPropertySupport.getPropertyVisibilityHandler() != null) {
                        if (!elementCustomPropertySupport.getPropertyVisibilityHandler().isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    }
                } else if (property.getClass() == EmbeddedPropertySupport.class) {
                    EmbeddedPropertySupport embeddedPropertySupport = (EmbeddedPropertySupport) property;
                    PropertyVisibilityHandler propertyVisibilityHandler = propertyVisibilityHandlerList.get(embeddedPropertySupport.getEntity().getName());
                    if (propertyVisibilityHandler != null) {
                        if (!propertyVisibilityHandler.isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    }
                } else if (property.getClass() == ComboBoxPropertySupport.class) {
                    ComboBoxPropertySupport comboBoxPropertySupport = (ComboBoxPropertySupport) property;
                    PropertyVisibilityHandler propertyVisibilityHandler = propertyVisibilityHandlerList.get(comboBoxPropertySupport.getName());
                    if (propertyVisibilityHandler != null) {
                        if (!propertyVisibilityHandler.isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    }
                } else if (property.getClass() == NEntityPropertySupport.class) {
                    NEntityPropertySupport attributeProperty = (NEntityPropertySupport) property;
                    attributeProperty.getAttributeEntity().getTableDataListener().initCount();
                }
            }
            if (hiddenPropertyCount == propertySet.getProperties().length) {
                propertySet.setHidden(true);
            }
        }
        return node;
    }

}
