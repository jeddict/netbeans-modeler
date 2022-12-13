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
package org.netbeans.modeler.widget.context.action;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.AnchorShapeFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.anchorshape.ArrowWithCrossedCircleAnchorShape;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.openide.windows.TopComponent;

/**
 * Override at least isTargetWidget and createConnection methods. isSourceWidget
 * is always called before isTargetWidget.
 *
 *
 */
public class ConnectAction extends WidgetAction.LockedAdapter {

    private static final int MIN_DIFFERENCE = 5;

    private ConnectDecorator decorator;
    private Widget interractionLayer;
    private ExConnectProvider provider;

    private ConnectionWidget connectionWidget = null;
    private Widget sourceWidget = null;
    private Widget targetWidget = null;
    private Point startingPoint = null;

    private int type;//type of created message (?if message?), see BaseElement for keys
    private boolean targetExistButNotValid = false;
    private AnchorShape standardShape = AnchorShapeFactory.createArrowAnchorShape(45, 12);
    private AnchorShape notValidTargetShape = (AnchorShape) new ArrowWithCrossedCircleAnchorShape(12, 20, 0);

    public ConnectAction(ConnectDecorator decorator, Widget interractionLayer, ExConnectProvider provider) {
        this.decorator = decorator;
        this.interractionLayer = interractionLayer;
        this.provider = provider;
    }

    @Override
    protected boolean isLocked() {
        return sourceWidget != null;
    }

    @Override
    public WidgetAction.State mousePressed(Widget widget, WidgetAction.WidgetMouseEvent event) {
        if (isLocked()) {
            return WidgetAction.State.createLocked(widget, this);
        }
        return mousePressedCore(widget, event);
    }

    protected State mousePressedCore(Widget widget, WidgetMouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 1) {
            if (provider.isSourceWidget(widget)) {
                sourceWidget = widget;
                targetWidget = null;
                startingPoint = new Point(event.getPoint());
                connectionWidget = decorator.createConnectionWidget(interractionLayer.getScene());
//                connectionWidget.setTargetAnchorShape (standardShape);
                connectionWidget.setSourceAnchor(decorator.createSourceAnchor(widget));
                interractionLayer.addChild(connectionWidget);
                interractionLayer.bringToFront();
                return WidgetAction.State.createLocked(widget, this);
            } else {
                NBModelerUtil.showContextPalette(((INodeWidget) widget).getModelerScene(), (INodeWidget) widget);
                ((IModelerScene) widget.getScene()).setActiveTool(DesignerTools.SELECT);//??TBD is it always select before usage?, may it be restored in selectionChanged?
            }
        } else if (event.getButton() == MouseEvent.BUTTON3) {
            if (isLocked()) {
                cancel();//TBD check why don't work correctly
            }
            return State.CONSUMED;
        }
        return State.REJECTED;
    }

    @Override
    public WidgetAction.State mouseReleased(Widget widget, WidgetAction.WidgetMouseEvent event) {
        if (connectionWidget == null) {
            cancel();
            return State.CONSUMED;
        }
        Point point = event.getPoint();
        boolean state = move(widget, point);
        if (state && (event.getButton() == MouseEvent.BUTTON1)) {
            if (targetWidget != null) {
                if (Math.abs(startingPoint.x - point.x) >= MIN_DIFFERENCE || Math.abs(startingPoint.y - point.y) >= MIN_DIFFERENCE) {
                    provider.createConnection(sourceWidget, targetWidget);
                }
                cancel();
            } else if (targetExistButNotValid) {
                cancel();
            } else {
                cancel();
            }
        } else {
            cancel();
        }
        return state ? State.CONSUMED : State.REJECTED;
    }

    private void cancel() {
        ((TopComponent) ((IModelerScene) interractionLayer.getScene()).getModelerPanelTopComponent()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        sourceWidget = null;
        targetWidget = null;
        startingPoint = null;
        if (connectionWidget != null) {
            connectionWidget.setSourceAnchor(null);
            connectionWidget.setTargetAnchor(null);
            interractionLayer.removeChild(connectionWidget);
            interractionLayer.bringToBack();
        }
        connectionWidget = null;
    }

    @Override
    public WidgetAction.State mouseDragged(Widget widget, WidgetAction.WidgetMouseEvent event) {
        return move(widget, event.getPoint()) ? State.createLocked(widget, this) : State.REJECTED;
    }

    private boolean move(Widget widget, Point point) {

        targetExistButNotValid = false;
        if (sourceWidget != widget || connectionWidget == null) {
            return false;
        }
        connectionWidget.setForeground(Color.BLACK);

        Point targetSceneLocation = widget.convertLocalToScene(point);

        targetWidget = resolveTargetWidgetCore(interractionLayer.getScene(), targetSceneLocation);

        if ((targetWidget == null || targetWidget instanceof Scene) && targetExistButNotValid) {
            connectionWidget.setTargetAnchorShape(notValidTargetShape);
        } else {
            connectionWidget.setTargetAnchorShape(standardShape);
        }

        Anchor targetAnchor = null;
        if (targetWidget != null) {
            targetAnchor = decorator.createTargetAnchor(targetWidget);
        }

        if (targetAnchor == null) {
            targetAnchor = decorator.createFloatAnchor(targetSceneLocation);
        }
        connectionWidget.setTargetAnchor(targetAnchor);
        return true;
    }

    private Widget resolveTargetWidgetCore(Scene scene, Point sceneLocation) {
        if (provider != null) {
            if (provider.hasCustomTargetWidgetResolver(scene)) {
                return provider.resolveTargetWidget(scene, sceneLocation);
            }
        }
        Point sceneOrigin = scene.getLocation();

        sceneLocation = new Point(sceneLocation.x + sceneOrigin.x, sceneLocation.y + sceneOrigin.y);
        Widget[] result = new Widget[]{null};
        resolveTargetWidgetCoreDive(result, scene, sceneLocation);
        return result[0];
    }

    private boolean resolveTargetWidgetCoreDive(Widget[] result, Widget widget, Point parentLocation) {
        Point widgetLocation = widget.getLocation();
        Point location = new Point(parentLocation.x - widgetLocation.x, parentLocation.y - widgetLocation.y);

        if (!widget.getBounds().contains(location)) {
            return false;
        }

        java.util.List<Widget> children = widget.getChildren();

        for (int i = children.size() - 1; i >= 0; i--) {
            if (!(children.get(i) instanceof LabelWidget) && !(children.get(i) instanceof ConnectionWidget)) {
                if (resolveTargetWidgetCoreDive(result, children.get(i), location)) {
                    return true;
                }
            }
        }

        if (!widget.isHitAt(location)) {
            return false;
        }

        ConnectorState state = provider.isTargetWidget(sourceWidget, widget);
        if (state == ConnectorState.REJECT) {
            return false;
        }
        if (state == ConnectorState.ACCEPT) {
            result[0] = widget;
        }
        return true;
    }

    @Override
    public State keyPressed(Widget widget, WidgetKeyEvent event) {
        return super.keyPressed(widget, event);
    }

}
