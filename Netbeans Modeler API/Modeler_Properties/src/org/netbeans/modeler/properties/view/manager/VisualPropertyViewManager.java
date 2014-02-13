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

import org.netbeans.modeler.config.element.ElementConfig;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

/**
 *
 *
 */
public class VisualPropertyViewManager extends AbstractNode {

    private IBaseElementWidget baseElementWidget;
    private IModelerScene scene;

    public VisualPropertyViewManager(IBaseElementWidget baseElementWidget) {
        super(Children.LEAF);
        this.baseElementWidget = baseElementWidget;
        if (baseElementWidget instanceof INodeWidget) {
            scene = ((INodeWidget) baseElementWidget).getModelerScene();
        } else if (baseElementWidget instanceof IEdgeWidget) {
            scene = ((IEdgeWidget) baseElementWidget).getModelerScene();
        } else if (baseElementWidget instanceof IModelerScene) {
            scene = (IModelerScene) baseElementWidget;
        }
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        ElementPropertySet elementPropertySet = new ElementPropertySet(scene.getModelerFile(), sheet);
        baseElementWidget.createVisualPropertySet(elementPropertySet);

        for (Sheet.Set set : elementPropertySet.getGroups()) {
            elementPropertySet.getSheet().put(set);
        }

        return sheet;
    }

}
