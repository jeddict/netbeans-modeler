/**
 * Copyright 2013-2018 Gaurav Gupta
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
package org.netbeans.modeler.actions;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

public class PanAction extends WidgetAction.LockedAdapter {

    private Scene scene;
    private JScrollPane scrollPane;
    private Point lastLocation;
    public Cursor MOUSE_PRESSED;
    private Cursor MOUSE_RELEASED;

    private void initCursor() {
        MOUSE_PRESSED = Utilities.createCustomCursor(scene.getView(),
                ImageUtilities.icon2Image(ImageUtil.getInstance().getIcon("pan-closed-hand.gif")),
                "PanClosedHand");
        MOUSE_RELEASED = Utilities.createCustomCursor(scene.getView(),
                ImageUtilities.icon2Image(ImageUtil.getInstance().getIcon("pan-open-hand.gif")),
                "PanOpenedHand");
    }

    @Override
    protected boolean isLocked() {
        return scrollPane != null;
    }

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        scene = widget.getScene();
        if (isLocked()) {
            return State.createLocked(widget, this);
        }
        if (event.getButton() == MouseEvent.BUTTON1) {
            scrollPane = findScrollPane(scene.getView());
            if (scrollPane != null) {
                if (MOUSE_PRESSED == null) {
                    initCursor();
                }
                scene.getView().setCursor(MOUSE_PRESSED);
                lastLocation = scene.convertSceneToView(widget.convertLocalToScene(event.getPoint()));
                SwingUtilities.convertPointToScreen(lastLocation, scene.getView());
                return State.createLocked(widget, this);
            }
        }
        return State.REJECTED;
    }

    private JScrollPane findScrollPane(JComponent component) {
        for (;;) {
            if (component == null) {
                return null;
            }
            if (component instanceof JScrollPane) {
                return ((JScrollPane) component);
            }
            Container parent = component.getParent();
            if (!(parent instanceof JComponent)) {
                return null;
            }
            component = (JComponent) parent;
        }
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent event) {
        if (MOUSE_RELEASED == null) {
            initCursor();
        }

        boolean state = pan(widget, event.getPoint());
        if (state) {
            scrollPane = null;
        }
        scene.getView().setCursor(MOUSE_RELEASED);
        return state ? State.createLocked(widget, this) : State.REJECTED;
    }

    @Override
    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        return pan(widget, event.getPoint()) ? State.createLocked(widget, this) : State.REJECTED;
    }

    private boolean pan(Widget widget, Point newLocation) {
        if (scrollPane == null || scene != widget.getScene()) {
            return false;
        }
        newLocation = scene.convertSceneToView(widget.convertLocalToScene(newLocation));
        SwingUtilities.convertPointToScreen(newLocation, scene.getView());
        JComponent view = scene.getView();
        view.setCursor(MOUSE_PRESSED);
        Rectangle rectangle = view.getVisibleRect();
        rectangle.x += lastLocation.x - newLocation.x;
        rectangle.y += lastLocation.y - newLocation.y;
        view.scrollRectToVisible(rectangle);
        lastLocation = newLocation;
        return true;
    }

}
