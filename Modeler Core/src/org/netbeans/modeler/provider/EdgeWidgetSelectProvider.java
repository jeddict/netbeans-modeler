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
package org.netbeans.modeler.provider;

import java.awt.Point;
import java.util.Collections;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.scene.AbstractModelerScene;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;

/**
 *
 *
 */
public class EdgeWidgetSelectProvider implements SelectProvider {

    @Override
    public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
        return false;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
        return true;
    }

    @Override
    public void select(Widget widget, Point localLocation, boolean invertSelection) {
        IEdgeWidget edgeWidget = (IEdgeWidget) widget;
        IModelerScene scene = ((EdgeWidget) edgeWidget).getModelerScene();
        edgeWidget.exploreProperties();
        /* Ref ObjectScene.java */
        Object object = scene.findObject(widget);
        scene.setFocusedObject(object);
        if (object != null) {
            if (!invertSelection && scene.getSelectedObjects().contains(object)) {
                return;
            }
            scene.userSelectionSuggested(Collections.singleton(object), invertSelection);
        } else {
            scene.userSelectionSuggested(Collections.emptySet(), invertSelection);
        }
        /* Ref ObjectScene.java */
    }
}
