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
package org.netbeans.modeler.anchors;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.node.image.SvgNodeWidget;
import test.ClosestSegment;
import test.GeometryClosestPointManager;

public class CustomPathAnchor extends Anchor {

    private boolean includeBorders;
//private int margin;
    private String type = "NONE";
    private IModelerScene modelerScene;
//private INodeWidget nodeWidget;

    public CustomPathAnchor(INodeWidget nodeWidget, boolean includeBorders) {
        super(((NodeWidget) nodeWidget).getNodeImageWidget());
//        this.nodeWidget=nodeWidget;
        this.modelerScene = nodeWidget.getModelerScene();

        this.includeBorders = includeBorders;
    }

    @Override
    public boolean allowsArbitraryConnectionPlacement() {
        return false;
    }
    //this is used by the Orthogonal Router. This router routes to the center of
    //of the anchors and then allows the anchor to adjust the end points.

    @Override
    public List<Point> compute(List<Point> points) {
        ArrayList<Point> bestPoints = new ArrayList<Point>(points);
        Point relatedLocation = getRelatedSceneLocation();

        int direction = 1;
        int index = 0;

        //the related location is the center of this anchor. It is possible that
        //the list of points started at the opposite anchor (other end of connection).
        Point endPoint = bestPoints.get(index);
        if (!endPoint.equals(relatedLocation)) {
            index = bestPoints.size() - 1;
            endPoint = bestPoints.get(index);
            direction = -1;
        }

        Widget widget = getRelatedWidget();
        Rectangle bounds = widget.getBounds();
        bounds = widget.convertLocalToScene(bounds);

        Point neighbor = bestPoints.get(index + direction);

        //moving the end point to the end of the anchor from the interior
        while (bounds.contains(neighbor)) {
            bestPoints.remove(index);
            endPoint = bestPoints.get(index);
            neighbor = bestPoints.get(index + direction);
        }

        Anchor.Result intersection = this.computeBoundaryIntersectionPoint(endPoint, neighbor);

        bestPoints.remove(index);
        bestPoints.add(index, intersection.getAnchorSceneLocation());

        return bestPoints;
    }

    @Override
    public Anchor.Result compute(Anchor.Entry entry) {
//        NodeWidget nodeWidget = (NodeWidget) getRelatedWidget().getParentWidget();
        Point relatedLocation = getRelatedSceneLocation();
        Point oppositeLocation = null;

        List<Point> points = entry.getAttachedConnectionWidget().getControlPoints();
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

        Anchor.Result boundaryIntersection
                = computeBoundaryIntersectionPoint(relatedLocation, oppositeLocation);

        if (boundaryIntersection == null) {
            return new Anchor.Result(relatedLocation, Anchor.DIRECTION_ANY);
        }

        return boundaryIntersection;
    }

    private Result computeBoundaryIntersectionPoint(Point relatedLocation, Point oppositeLocation) { //Source Center , Target Center

        Widget widget = getRelatedWidget();

        Rectangle bounds = widget.getBounds();
        if (!includeBorders) {
            Insets insets = widget.getBorder().getInsets();
            bounds.x += insets.left;
            bounds.y += insets.top;
            bounds.width -= insets.left + insets.right;
            bounds.height -= insets.top + insets.bottom;
        }
        bounds = widget.convertLocalToScene(bounds);

        if (bounds.isEmpty() || relatedLocation.equals(oppositeLocation)) {
            return null;
        }
        float dx = oppositeLocation.x - relatedLocation.x;
        float dy = oppositeLocation.y - relatedLocation.y;

        float ddx = Math.abs(dx) / (float) bounds.width;
        float ddy = Math.abs(dy) / (float) bounds.height;

        Anchor.Direction direction;

        if (ddx >= ddy) {
            direction = dx >= 0.0f ? Direction.RIGHT : Direction.LEFT;
        } else {
            direction = dy >= 0.0f ? Direction.BOTTOM : Direction.TOP;
        }

        float scale = 0.5f / Math.max(ddx, ddy);

        Point point = new Point(Math.round(relatedLocation.x + scale * dx), Math.round(relatedLocation.y + scale * dy));

//        if(direction== Direction.BOTTOM){
//            point.y = point.y - margin;
//        } else if(direction== Direction.TOP){
//            point.y = point.y + margin;
//        } else if(direction== Direction.LEFT){
//            point.x = point.x + margin;
//        } else if(direction== Direction.RIGHT){
//            point.x = point.x - margin;
//        }
        SvgNodeWidget imageWidget = (SvgNodeWidget) widget;
        Point movePo = null;
        if (imageWidget.getTransform() != null) {

            Shape shape = imageWidget.getTransform().createTransformedShape(imageWidget.getOutlineShape());
            Point widgetLoc = widget.convertLocalToScene(widget.getLocation());
            ClosestSegment cs = GeometryClosestPointManager.getClosetsPoint(shape, new Point2D.Double(point.x - widgetLoc.x, point.y - widgetLoc.y));
            int padX = 0;//padding
            int padY = 0;
            if (direction == Direction.TOP) {
                padY = - 2;
            } else if (direction == Direction.RIGHT) {
                padX = +2;
            } else if (direction == Direction.BOTTOM) {
                padY = +2;
            } else if (direction == Direction.LEFT) {
                padX = - 2;
            }

            movePo = new Point(widgetLoc.x + (int) cs.getBestPoint().getX() + padX, widgetLoc.y + (int) cs.getBestPoint().getY() + padY);

//            movePo = Util.getComputedPoint(oppositeLocation, movePo, 10);
        } else {
            movePo = point;
        }
        type = "NONE";
        return new Anchor.Result(movePo, direction);
    }
}
