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
package org.netbeans.modeler.provider.connection.controlpoint;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.edge.EdgeWidget;

public final class MoveControlPointAction extends WidgetAction.LockedAdapter {

    private MoveControlPointProvider provider;
    private ConnectionWidget.RoutingPolicy routingPolicy;

    private ConnectionWidget movingWidget = null;
    private Point controlPointLocation;
    private int controlPointIndex;
    private Point lastLocation = null;

    public MoveControlPointAction(MoveControlPointProvider provider, ConnectionWidget.RoutingPolicy routingPolicy) {
        this.provider = provider;
        this.routingPolicy = routingPolicy;
    }

    @Override
    protected boolean isLocked() {
        return movingWidget != null;
    }

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        if (isLocked()) {
            return State.createLocked(widget, this);
        }
        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 1) {
            if (widget instanceof ConnectionWidget) {
                ConnectionWidget conn = (ConnectionWidget) widget;
                controlPointIndex = conn.getControlPointHitAt(event.getPoint());
                if (controlPointIndex >= 0) {
                    movingWidget = conn;
                    controlPointLocation = new Point(conn.getControlPoints().get(controlPointIndex));
                    lastLocation = new Point(event.getPoint());
                    return State.createLocked(widget, this);
                } else {
                    movingWidget = null;
                }
            }
        }
        return State.REJECTED;
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent event) {
        State state = move(widget, event.getPoint()) ? State.CONSUMED : State.REJECTED;
        movingWidget = null;
        // Custom Code Start here
        EdgeWidget edge = (EdgeWidget) widget;
        edge.manageControlPoint();
        // Custom Code End here

        return state;
    }

    @Override
    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        if (move(widget, event.getPoint())) {
            return State.createLocked(widget, this);
        } else {
            movingWidget = null;
            return State.REJECTED;
        }
    }

    private boolean move(Widget widget, Point newLocation) {
        if (movingWidget != widget) {
            return false;
        }

        java.util.List<Point> controlPoints = movingWidget.getControlPoints();
        if (controlPointIndex < 0 || controlPointIndex >= controlPoints.size()) {
            return false;
        }

        Point location = new Point(controlPointLocation);
        location.translate(newLocation.x - lastLocation.x, newLocation.y - lastLocation.y);

        controlPoints = provider.locationSuggested(movingWidget, controlPointIndex, location);
        if (controlPoints == null) {
            return false;
        }

        if (routingPolicy != null) {
            movingWidget.setRoutingPolicy(routingPolicy);
        }
        movingWidget.setControlPoints(controlPoints, false);
//          // Custom Code Start here
//        EdgeWidget edge = (EdgeWidget)widget;
//         edge.manageControlPoint(controlPointIndex);
//        // Custom Code End here
        return true;
    }

}
