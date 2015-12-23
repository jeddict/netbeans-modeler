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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;

//BUG[5612] : https://java.net/bugzilla/show_bug.cgi?id=5612
public final class AlignStrategyProvider extends AlignSupport implements MoveStrategy, MoveProvider {

    private boolean outerBounds;
    private static final BasicStroke STROKE = new BasicStroke(0.8f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{4.0f, 3.0f}, 0.0f);

    private static final AlignWithMoveDecorator ALIGN_WITH_MOVE_DECORATOR_DEFAULT = (Scene scene) -> {
        ConnectionWidget widget = new ConnectionWidget(scene);
        widget.setStroke(STROKE);
        widget.setForeground(new Color(120, 120, 150));
        return widget;
    } // ActionFactory.createDefaultAlignWithMoveDecorator()
    ;

    public AlignStrategyProvider(IModelerScene scene) {
        super(new SingleLayerAlignWithWidgetCollector(scene.getMainLayer(), true), scene.getInterractionLayer(), ALIGN_WITH_MOVE_DECORATOR_DEFAULT);
        this.outerBounds = true;
    }

    @Override
    public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
        Point widgetLocation = widget.getLocation();
        Rectangle widgetBounds = outerBounds ? widget.getBounds() : widget.getClientArea();
        Rectangle bounds = widget.convertLocalToScene(widgetBounds);
        bounds.translate(suggestedLocation.x - widgetLocation.x, suggestedLocation.y - widgetLocation.y);
        Insets insets = widget.getBorder().getInsets();
        if (!outerBounds) {
            suggestedLocation.x += insets.left;
            suggestedLocation.y += insets.top;
        }
        Point point = super.locationSuggested(widget, bounds, widget.getParentWidget().convertLocalToScene(suggestedLocation), true, true, true, true);
        if (!outerBounds) {
            point.x -= insets.left;
            point.y -= insets.top;
        }
        return widget.getParentWidget().convertSceneToLocal(point);
    }

    @Override
    public void movementStarted(Widget widget) {
        show();
    }

    @Override
    public void movementFinished(Widget widget) {
        hide();
    }

    @Override
    public Point getOriginalLocation(Widget widget) {
        return ActionFactory.createDefaultMoveProvider().getOriginalLocation(widget);
    }

    @Override
    public void setNewLocation(Widget widget, Point location) {
        ActionFactory.createDefaultMoveProvider().setNewLocation(widget, location);
    }

}
