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

import org.netbeans.api.visual.action.CycleFocusProvider;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import static org.netbeans.modeler.core.engine.ModelerDiagramEngine.PIN_WIDGET_SELECT_PROVIDER;
import org.netbeans.modeler.widget.pin.PinWidget;

/**
 * @author Gaurav Gupta
 */
public class CyclePinFocusProvider implements CycleFocusProvider {

    @Override
    public boolean switchPreviousFocus(Widget widget) {
        Scene scene = widget.getScene();
        return scene instanceof ObjectScene && switchFocus((ObjectScene) scene, false);
    }

    @Override
    public boolean switchNextFocus(Widget widget) {
        Scene scene = widget.getScene();
        return scene instanceof ObjectScene && switchFocus((ObjectScene) scene, true);
    }

    @SuppressWarnings("unchecked")
    private boolean switchFocus(ObjectScene scene, boolean forwardDirection) {
        if (scene.getFocusedWidget() instanceof PinWidget) {
            PinWidget pinWidget = (PinWidget) scene.getFocusedWidget();
            boolean detected = false;
            Widget detectedWidget = null;
            for (int i = 0; i < pinWidget.getPNodeWidget().getChildren().size(); i++) {
                Widget widget = pinWidget.getPNodeWidget().getChildren().get(i);
                if (widget instanceof PinWidget) {
                    if (detected) {
                        detectedWidget = widget;
                        break;
                    } else if (pinWidget == widget) {
                        detected = true;
                    }
                }
            }
            if (detectedWidget == null) { //means selected element was last in list
                for (int i = 0; i < pinWidget.getPNodeWidget().getChildren().size(); i++) {
                    Widget widget = pinWidget.getPNodeWidget().getChildren().get(i);
                    if (widget instanceof PinWidget) {
                        detectedWidget = widget;
                        break;
                    }
                }
            }
            PIN_WIDGET_SELECT_PROVIDER.select(detectedWidget, null, false);
            return true;
        } else {
            return false;
        }
    }

}
