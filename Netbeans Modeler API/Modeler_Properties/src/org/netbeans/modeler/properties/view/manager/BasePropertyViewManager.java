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

import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

public class BasePropertyViewManager extends AbstractNode {

    private IBaseElementWidget baseElementWidget;
    private IModelerScene scene;

    public BasePropertyViewManager(IBaseElementWidget baseElementWidget) {
        super(Children.LEAF);
        this.baseElementWidget = baseElementWidget;
        if (baseElementWidget instanceof INodeWidget) {
            scene = ((INodeWidget) baseElementWidget).getModelerScene();
        } else if (baseElementWidget instanceof IEdgeWidget) {
            scene = ((IEdgeWidget) baseElementWidget).getModelerScene();
        } else if (baseElementWidget instanceof IModelerScene) {
            scene = (IModelerScene) baseElementWidget;
        } else if (baseElementWidget instanceof IPinWidget) {
            scene = ((IPinWidget) baseElementWidget).getModelerScene();
        }
    }

    //ELEMENT_UPGRADE
    @Override
    //http://blog.emilianbold.ro/2007/01/netbeans-platform-combobox-in-property.html // cmbo
    //http://netbeans-org.1045718.n5.nabble.com/Widgets-and-properties-sheet-td2986719.html
    //https://blogs.oracle.com/geertjan/entry/connecting_shapes_showing_properties
    //https://platform.netbeans.org/tutorials/60/nbm-property-editors.html  ////custom calendar
    //https://platform.netbeans.org/tutorials/nbm-property-editors.html   //custom calendar
    //http://netbeans.dzone.com/nb-icon-checkbox-property-changer
    //https://platform.netbeans.org/tutorials/60/nbm-nodesapi2.html    //multitab dnd
    //http://forums.netbeans.org/ntopic52707.html
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        ElementPropertySet elementPropertySet = new ElementPropertySet(scene.getModelerFile(), sheet);
        baseElementWidget.createPropertySet(elementPropertySet);

        for (Sheet.Set set : elementPropertySet.getGroups()) {
            elementPropertySet.getSheet().put(set);
        }

        return sheet;
    }
}
