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

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.IWidget;

public final class MoveAction extends WidgetAction.LockedAdapter {

    private IWidget actionWidget;
    private MoveStrategy moveStrategy;
    private MoveProvider moveProvider;
    private MoveStrategy alignStrategy;
    private MoveProvider alignProvider;

    private Widget movingWidget = null;
    private Point dragSceneLocation = null;
    private Point originalSceneLocation = null;
    private Point initialMouseLocation = null;

    public MoveAction(IWidget actionWidget, MoveStrategy strategy, MoveProvider provider, MoveStrategy alignStrategy, MoveProvider alignProvider) {
        this.actionWidget = actionWidget;
        this.moveStrategy = strategy;
        this.moveProvider = provider;
        this.alignStrategy = alignStrategy;
        this.alignProvider = alignProvider;
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
            movingWidget = widget;
            initialMouseLocation = event.getPoint();
            originalSceneLocation = moveProvider.getOriginalLocation(widget);
            if (originalSceneLocation == null) {
                originalSceneLocation = new Point();
            }
            dragSceneLocation = widget.convertLocalToScene(event.getPoint());
            if (actionWidget.getModelerScene().isAlignSupport()) {
                alignProvider.movementStarted(widget);
            }
            moveProvider.movementStarted(widget);
            return State.createLocked(widget, this);
        }
        return State.REJECTED;
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent event) {
        boolean state;
        if (initialMouseLocation != null && initialMouseLocation.equals(event.getPoint())) {
            state = true;
        } else {
            state = move(widget, event.getPoint());
        }
        if (state) {
            movingWidget = null;
            dragSceneLocation = null;
            originalSceneLocation = null;
            initialMouseLocation = null;
            if (actionWidget.getModelerScene().isAlignSupport()) {
                alignProvider.movementFinished(widget);
            }
            moveProvider.movementFinished(widget);
        }
        return state ? State.CONSUMED : State.REJECTED;
    }

    @Override
    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        return move(widget, event.getPoint()) ? State.createLocked(widget, this) : State.REJECTED;
    }

    private boolean move(Widget widget, Point newLocation) {
        if (movingWidget != widget) {
            return false;
        }
        initialMouseLocation = null;
        newLocation = widget.convertLocalToScene(newLocation);
        Point location = new Point(originalSceneLocation.x + newLocation.x - dragSceneLocation.x, originalSceneLocation.y + newLocation.y - dragSceneLocation.y);
        if (actionWidget.getModelerScene().isAlignSupport()) {
            moveProvider.setNewLocation(widget, alignStrategy.locationSuggested(widget, originalSceneLocation, location));
        } else {
            moveProvider.setNewLocation(widget, location);
        }
        return true;
    }

}
