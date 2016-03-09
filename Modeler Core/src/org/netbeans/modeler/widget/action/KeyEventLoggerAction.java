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
package org.netbeans.modeler.widget.action;

import java.awt.event.KeyEvent;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;

/**
 *
 *
 */
public class KeyEventLoggerAction extends WidgetAction.Adapter {

    @Override
    public State keyPressed(Widget widget, WidgetKeyEvent event) {
        return State.REJECTED;
    }

    @Override
    public State keyReleased(Widget widget, WidgetKeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_DELETE) {
            GraphScene s = (GraphScene) widget.getScene();
            if (widget instanceof NodeWidget) {
                s.removeNode(s.findObject(widget));
                return State.CONSUMED;
            } else if (widget instanceof EdgeWidget) {
                widget.removeFromParent();
                return State.CONSUMED;
            }
        }

        return State.REJECTED;
    }

    @Override
    public State keyTyped(Widget widget, WidgetKeyEvent event) {
        return State.REJECTED;
    }
}
