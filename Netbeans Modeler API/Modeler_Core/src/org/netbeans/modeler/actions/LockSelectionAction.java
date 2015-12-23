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
package org.netbeans.modeler.actions;

import java.awt.event.KeyEvent;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetKeyEvent;
import org.netbeans.api.visual.action.WidgetAction.WidgetMouseEvent;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.scene.AbstractModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;

/**
 *
 *
 */
public class LockSelectionAction extends WidgetAction.Adapter {

    @Override
    public State keyPressed(Widget widget, WidgetKeyEvent event) {
        State retVal = super.keyTyped(widget, event);

        if (retVal != State.CONSUMED) {
            if (widget.getScene() instanceof AbstractModelerScene) {
                AbstractModelerScene scene = (AbstractModelerScene) widget.getScene();
                if (event.isShiftDown() == true) {
                    if (event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT) {
                        System.out.println("shift left");
                        retVal = State.CONSUMED;
                    }
                }
                if (event.isControlDown() == true) {
                    if (event.getKeyCode() == KeyEvent.VK_S) {
                        System.out.println("ctrl s = SAVE");
                        retVal = State.CONSUMED;
                    }
                }

                if ((event.isControlDown() == true)/* && (event.isShiftDown() == true)*/) {
                    if (event.getKeyCode() == KeyEvent.VK_EQUALS) {
                        IFlowElementWidget data = getWidgetDataObject(scene, widget);
                        if (data != null) {
                            scene.addLockedSelected(data);
                        }
                        retVal = State.CONSUMED;
                    } else if (event.getKeyCode() == KeyEvent.VK_MINUS) {
                        IFlowElementWidget data = getWidgetDataObject(scene, widget);
                        if (data != null) {
                            scene.removeLockSelected(data);
                        }
                        retVal = State.CONSUMED;
                    }
                }
            }
        }

        return retVal;
    }

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        if (widget.getScene() instanceof AbstractModelerScene) {
            AbstractModelerScene scene = (AbstractModelerScene) widget.getScene();
            scene.clearLockedSelected();
        }

        return State.REJECTED;
    }

    private IFlowElementWidget getWidgetDataObject(ObjectScene scene, Widget widget) {
        IFlowElementWidget retVal = null;

        if (scene.findObject(widget) instanceof IFlowElementWidget) {
            retVal = (IFlowElementWidget) scene.findObject(widget);
        }

        return retVal;
    }
}
