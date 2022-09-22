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
package org.netbeans.modeler.widget.edge;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.IWidget;

public interface IConnectionWidget extends IWidget {

    /**
     * Forces path routing.
     */
    void calculateRouting();

    /**
     * Returns a location of control point at the specified index in the list of
     * control points.
     *
     * @param index index of the control point to return
     * @return the point; null if the control point does not exist
     */
    Point getControlPoint(int index);

    /**
     * Returns the cut distance at control points.
     *
     * @return the cut distance
     * @since 2.5
     */
    int getControlPointCutDistance();

    /**
     * Returns an index of a control point that is hit by the local location
     *
     * @param localLocation the local location
     * @return the index; -1 if no control point was hit
     */
    int getControlPointHitAt(Point localLocation);

    /**
     * Returns a point shape of control points of the connection widget.
     *
     * @return the control points shape
     */
    PointShape getControlPointShape();

    /**
     * Returns a list of control points.
     *
     * @return the list of control points
     */
    List<Point> getControlPoints();

    /**
     * Returns the cursor for control point.
     *
     * @return the cursor
     * @since 2.3
     */
    Cursor getControlPointsCursor();

    /**
     * Returns a point shape of end points of the connection widget.
     *
     * @return the end points shape
     */
    PointShape getEndPointShape();

    /**
     * Returns the first control point.
     *
     * @return the first control point; null, if list of control points is empty
     */
    Point getFirstControlPoint();

    /**
     * Returns the last control point.
     *
     * @return the last control point; null, if list of control points is empty
     */
    Point getLastControlPoint();

    /**
     * Returns line color of the widget.
     *
     * @return the line color; null if no line color is specified
     */
    Color getLineColor();

    /**
     * Returns the control-points-based path router of the connection widget.
     *
     * @return the path router
     */
    Router getRouter();

    /**
     * Returns a routing policy.
     *
     * @return the routing policy
     * @since 2.9
     */
    RoutingPolicy getRoutingPolicy();

    /**
     * Returns a source anchor of the connection widget.
     *
     * @return the source anchor
     */
    Anchor getSourceAnchor();

    /**
     * Returns an anchor entry representing the source of the connection widget.
     *
     * @return the anchor entry representing the source of the connection widget
     */
    Anchor.Entry getSourceAnchorEntry();

    /**
     * Returns an anchor shape of the source of the connection widget.
     *
     * @return the source anchor shape
     */
    AnchorShape getSourceAnchorShape();

    /**
     * Returns a stroke of the connection widget.
     *
     * @return the stroke
     */
    Stroke getStroke();

    /**
     * Returns a target anchor of the connection widget.
     *
     * @return the target anchor
     */
    Anchor getTargetAnchor();

    /**
     * Returns an anchor entry representing the target of the connection widget.
     *
     * @return the anchor entry representing the target of the connection widget
     */
    Anchor.Entry getTargetAnchorEntry();

    /**
     * Returns an anchor shape of the target of the connection widget.
     *
     * @return the target anchor shape
     */
    AnchorShape getTargetAnchorShape();

    /**
     * Returns the rotation of the target anchor shape.
     *
     * @return the target anchor shape rotation
     */
    double getTargetAnchorShapeRotation();

    /**
     * Returns whether the local location hits the first control point (also
     * meant to be the source anchor).
     *
     * @param localLocation the local location
     * @return true if it hits the first control point
     */
    boolean isFirstControlPointHitAt(Point localLocation);

    /**
     * Returns whether a specified local location is a part of the connection
     * widget. It checks whether the location is close to the
     * control-points-based path (up to 4px from the line), close to the anchors
     * (defined by AnchorShape) or close to the control points (PointShape).
     *
     * @param localLocation the local locaytion
     * @return true, if the location is a part of the connection widget
     */
    @Override
    boolean isHitAt(Point localLocation);

    /**
     * Returns whether the local location hits the last control point (also
     * meant to be the target anchor).
     *
     * @param localLocation the local location
     * @return true if it hits the last control point
     */
    boolean isLastControlPointHitAt(Point localLocation);

    /**
     * Returns whether the control (and end) points are painted
     *
     * @return true, if the control points (and end points) are painted
     */
    boolean isPaintControlPoints();

    /**
     * Returns whether the connection widget is routed.
     *
     * @return true if the connection widget is routed
     */
    boolean isRouted();

    /**
     * Returns whether the connection widget is validated and routed.
     *
     * @return true, if the connection widget is validated and routed
     */
    @Override
    boolean isValidated();

    /**
     * Implements the widget-state specific look of the widget.
     *
     * @param previousState the previous state
     * @param state the new state
     */
    void notifyStateChanged(ObjectState previousState, ObjectState state);

    /**
     * Removes a constraint for a child widget.
     *
     * @param childWidget the child widget
     */
    void removeConstraint(Widget childWidget);

    /**
     * Schedules the connection widget for re-routing its path.
     */
    void reroute();

    /**
     * Sets a constraint for a child widget when ConnectionWidgetLayout (by
     * default) is used.
     *
     * @param childWidget the child widget for which the constraint is set
     * @param alignment the alignment specified relatively to the origin point
     * @param placementInPercentage the placement on a path in percentage of the
     * path length
     */
    void setConstraint(Widget childWidget, LayoutFactory.ConnectionWidgetLayoutAlignment alignment, float placementInPercentage);

    /**
     * Sets a constraint for a child widget when ConnectionWidgetLayout (by
     * default) is used.
     *
     * @param childWidget the child widget for which the constraint is set
     * @param alignment the alignment specified relatively to the origin point
     * @param placementAtDistance the placement on a path in pixels as a
     * distance from the source anchor
     */
    void setConstraint(Widget childWidget, LayoutFactory.ConnectionWidgetLayoutAlignment alignment, int placementAtDistance);

    /**
     * Sets the cut distance at control points.
     *
     * @param controlPointCutDistance if positive number, then the path is cut
     * to render smooth corners; otherwise the path is rendered using control
     * points only
     * @since 2.5
     */
    void setControlPointCutDistance(int controlPointCutDistance);

    /**
     * Sets a point shape of control points of the connection widget.
     *
     * @param controlPointShape the control points shape
     */
    void setControlPointShape(PointShape controlPointShape);

    /**
     * Sets control points.
     *
     * @param controlPoints the list of control points
     * @param sceneLocations if true, then controlPoints argyment is taken as a
     * list of scene locations; if false, then controlPoints argument is taken
     * as a list of local locations
     */
    void setControlPoints(Collection<Point> controlPoints, boolean sceneLocations);

    /**
     * Sets a control points cursor. The cursor is used only when mouse is over
     * a visible control point
     *
     * @param controlPointsCursor the control points cursor
     * @since 2.3
     */
    void setControlPointsCursor(Cursor controlPointsCursor);

    /**
     * Sets a point shape of end points of the connection widget.
     *
     * @param endPointShape the end points shape
     */
    void setEndPointShape(PointShape endPointShape);

    /**
     * Sets a line color of the widget.
     *
     * @param lineColor the line color; if null, then the line color will be
     * resolved from LookFeel of the scene.
     */
    void setLineColor(Color lineColor);

    /**
     * Sets whether the control (and end) points are painted
     *
     * @param paintControlPoints if true, then control points are painted
     */
    void setPaintControlPoints(boolean paintControlPoints);

    /**
     * Sets a control-points-based path router of the connection widget.
     *
     * @param router the path router
     */
    void setRouter(Router router);

    /**
     * Sets a routing policy. It invokes re-routing in case of routing policy
     * change unless its is changed to DISABLE_ROUTING.
     *
     * @param routingPolicy the new routing policy
     * @since 2.9
     */
    void setRoutingPolicy(RoutingPolicy routingPolicy);

    /**
     * Sets a source anchor of the connection widget.
     *
     * @param sourceAnchor the source anchor
     */
    void setSourceAnchor(Anchor sourceAnchor);

    /**
     * Sets the anchor shape of the source of the connection widget.
     *
     * @param sourceAnchorShape the source anchor shape
     */
    void setSourceAnchorShape(AnchorShape sourceAnchorShape);

    /**
     * Sets a stroke.
     *
     * @param stroke the stroke
     */
    void setStroke(Stroke stroke);

    /**
     * Sets a target anchor of the connection widget.
     *
     * @param targetAnchor the target anchor
     */
    void setTargetAnchor(Anchor targetAnchor);

    /**
     * Sets the anchor shape of the target of the connection widget.
     *
     * @param targetAnchorShape the target anchor shape
     */
    void setTargetAnchorShape(AnchorShape targetAnchorShape);

}
