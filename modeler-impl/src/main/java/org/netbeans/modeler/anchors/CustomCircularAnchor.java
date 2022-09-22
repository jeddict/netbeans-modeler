/**
 * Copyright 2013-2022 Gaurav Gupta
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
package org.netbeans.modeler.anchors;

import java.awt.Point;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;

public final class CustomCircularAnchor extends Anchor {

    private String type = "NONE";
    private final IModelerScene modelerScene;
    private final INodeWidget nodeWidget;

    public CustomCircularAnchor(INodeWidget nodeWidget) {
        super(((NodeWidget) nodeWidget).getNodeImageWidget());
        this.nodeWidget = nodeWidget;
        this.modelerScene = nodeWidget.getModelerScene();
    }

    @Override
    public Result compute(Entry entry) {
        Point relatedLocation = getRelatedSceneLocation();
        Point oppositeLocation;

        java.util.List<Point> points = entry.getAttachedConnectionWidget().getControlPoints();

        if (entry.getAttachedConnectionWidget() instanceof IFlowEdgeWidget) {
            IFlowEdgeWidget flowEdgeWidget = (IFlowEdgeWidget) entry.getAttachedConnectionWidget();
            INodeWidget sourceNodeWidget = (INodeWidget) flowEdgeWidget.getSourceWidget();
            INodeWidget targetNodeWidget = (INodeWidget) flowEdgeWidget.getTargetWidget();
            if (!sourceNodeWidget.isAnchorEnable() && !targetNodeWidget.isAnchorEnable() && !points.isEmpty()) {        //if edge both nodes anchor not enabled (if any one is allowed then calculated[means this if condition failed])
                if (entry.isAttachedToConnectionSource()) {
                    return new Anchor.Result(points.get(0), Anchor.DIRECTION_ANY); //return current
                } else {
                    return new Anchor.Result(points.get(points.size() - 1), Anchor.DIRECTION_ANY); //return current
                }
            }
        }

        if (points.size() > 2) {
            if (entry.isAttachedToConnectionSource()) {
                oppositeLocation = points.get(1);
                type = "SOURCE";
            } else {
                oppositeLocation = points.get(points.size() - 2);
                type = "TARGET";
            }
        } else {
            oppositeLocation = getOppositeSceneLocation(entry);
            type = "NONE";
        }

        return computeBoundaryIntersectionPoint((int) nodeWidget.getBounds().getWidth() / 2, relatedLocation, oppositeLocation);
    }

    private Result computeBoundaryIntersectionPoint(int radius, Point relatedLocation, Point oppositeLocation) { //Source Center , Target Center
        double angle = Math.atan2(oppositeLocation.y - relatedLocation.y, oppositeLocation.x - relatedLocation.x);
        Point location = new Point(relatedLocation.x + (int) (radius * Math.cos(angle)), relatedLocation.y + (int) (radius * Math.sin(angle)));
        type = "NONE";
        return new Anchor.Result(location, Anchor.DIRECTION_ANY); // TODO - resolve direction
    }

}
