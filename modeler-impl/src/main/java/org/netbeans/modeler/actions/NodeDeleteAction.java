/**
 * Copyright [2016] Gaurav Gupta
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
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 * @author Gaurav Gupta
 */
public class NodeDeleteAction extends WidgetAction.Adapter {

    public State keyTyped(Widget widget, WidgetKeyEvent event) {
        if (widget instanceof INodeWidget && event.getKeyChar() == KeyEvent.VK_DELETE) {
            GraphPinScene scene = (GraphPinScene) widget.getScene();
            if (widget instanceof INodeWidget) {
                ((INodeWidget) widget).remove(true);
                return State.CONSUMED;
            }
        }
        return State.REJECTED;
    }
}
