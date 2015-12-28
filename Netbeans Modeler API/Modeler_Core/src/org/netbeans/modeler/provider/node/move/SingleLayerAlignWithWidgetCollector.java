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
package org.netbeans.modeler.provider.node.move;

import java.awt.Rectangle;
import java.util.ArrayList;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

public final class SingleLayerAlignWithWidgetCollector implements AlignWithWidgetCollector {

    private LayerWidget collectionLayer;
    private boolean outerBounds;

    public SingleLayerAlignWithWidgetCollector(LayerWidget collectionLayer, boolean outerBounds) {
        this.collectionLayer = collectionLayer;
        this.outerBounds = outerBounds;
    }

    @Override
    public java.util.List<Rectangle> getRegions(Widget movingWidget) {
        java.util.List<Widget> children = collectionLayer.getChildren();
        ArrayList<Rectangle> regions = new ArrayList<Rectangle>(children.size());
        for (Widget widget : children) {
            if (widget != movingWidget) {
                regions.add(widget.convertLocalToScene(outerBounds ? widget.getBounds() : widget.getClientArea()));
            }
        }
        return regions;
    }

}
