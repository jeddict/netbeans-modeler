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
package org.netbeans.modeler.properties.view.manager;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.modeler.properties.embedded.EmbeddedPropertySupport;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.support.ComboBoxPropertySupport;
import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;

public class BasePropertyViewManager extends AbstractNode implements IPropertyManager {

    private IBaseElementWidget baseElementWidget;
    private IModelerScene modelerScene;

    public BasePropertyViewManager(IBaseElementWidget baseElementWidget) {
        super(Children.LEAF);
        this.baseElementWidget = baseElementWidget;
        if (baseElementWidget instanceof INodeWidget) {
            modelerScene = ((INodeWidget) baseElementWidget).getModelerScene();
        } else if (baseElementWidget instanceof IEdgeWidget) {
            modelerScene = ((IEdgeWidget) baseElementWidget).getModelerScene();
        } else if (baseElementWidget instanceof IModelerScene) {
            modelerScene = (IModelerScene) baseElementWidget;
        } else if (baseElementWidget instanceof IPinWidget) {
            modelerScene = ((IPinWidget) baseElementWidget).getModelerScene();
        }
    }

    private Sheet sheet;
    private ElementPropertySet elementPropertySet;

    @Override
    protected Sheet createSheet() {
        return sheet;//reloadSheet(new HashMap<>()); //bug : duplicate invocation of reloadSheet already called from PropertyUtil
    }

    public Sheet reloadSheet(Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
        if (elementPropertySet == null) {
            sheet = super.createSheet();
            elementPropertySet = new ElementPropertySet(getModelerScene().getModelerFile(), sheet);
            getBaseElementWidget().createPropertySet(elementPropertySet);
            elementPropertySet.executeProperties();
        } else {
            for (String key : elementPropertySet.getGroupKey()) {
                sheet.remove(key);
            }
        }
        createSheet(sheet, propertyVisibilityHandlerList);
        return sheet;
    }

    private void createSheet(Sheet sheet, Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
        for (Sheet.Set propertySet : elementPropertySet.getGroups()) {
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
                    } else if (embeddedPropertySupport.getPropertyVisibilityHandler() != null) {
                        if (!embeddedPropertySupport.getPropertyVisibilityHandler().isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    }
                } else if (property.getClass() == ComboBoxPropertySupport.class) {
                    ComboBoxPropertySupport comboBoxPropertySupport = (ComboBoxPropertySupport) property;
                    PropertyVisibilityHandler propertyVisibilityHandler = propertyVisibilityHandlerList.get(comboBoxPropertySupport.getName());//target to remove propertyVisibilityHandlerList and convert to comboBoxPropertySupport.getPropertyVisibilityHandler()
                    if (propertyVisibilityHandler != null) {
                        if (!propertyVisibilityHandler.isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    } else if (comboBoxPropertySupport.getPropertyVisibilityHandler() != null) {
                        if (!comboBoxPropertySupport.getPropertyVisibilityHandler().isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    }
                } else if (property.getClass() == NEntityPropertySupport.class) {
                    NEntityPropertySupport nEntityPropertySupport = (NEntityPropertySupport) property;
                    nEntityPropertySupport.getAttributeEntity().getTableDataListener().initCount();
                    if (nEntityPropertySupport.getPropertyVisibilityHandler() != null) {
                        if (!nEntityPropertySupport.getPropertyVisibilityHandler().isVisible()) {
                            property.setHidden(true);
                            hiddenPropertyCount++;
                        }
                    }
                }

                //propertyVisibilityHandlerList is Obselete remove this functionality in future
                //.getPropertyVisibilityHandler() is the right way
            }
            if (hiddenPropertyCount != propertySet.getProperties().length) {
//                propertySet.setHidden(true);
                sheet.put(propertySet);
            }
        }
    }

    /**
     * @return the modelerScene
     */
    @Override
    public IModelerScene getModelerScene() {
        return modelerScene;
    }

    /**
     * @return the baseElementWidget
     */
    @Override
    public IBaseElementWidget getBaseElementWidget() {
        return baseElementWidget;
    }
    
    public ElementPropertySet getElementPropertySet() {
        return elementPropertySet;
    }
}
