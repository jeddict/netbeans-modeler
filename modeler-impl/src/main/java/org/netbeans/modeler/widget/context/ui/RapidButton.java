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
package org.netbeans.modeler.widget.context.ui;

import java.awt.Color;
import javax.swing.UIManager;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 *
 *
 */
public class RapidButton extends Widget {

    private static final Border HOVER_BORDER = BorderFactory.createLineBorder(1, Color.LIGHT_GRAY);
    private static final Border NO_HOVER_BORDER = BorderFactory.createEmptyBorder(1);
    private ContextPaletteButtonModel descriptor = null;
    private INodeWidget associatedWidget = null;

    public RapidButton(INodeWidget target, ContextPaletteButtonModel descriptor) {
        super(target.getScene());
        associatedWidget = target;

        IModelerScene scene = target.getModelerScene();

        this.descriptor = descriptor;

        WidgetAction[] actions = descriptor.getWidgetActions();
        for (WidgetAction curAction : actions) {
            getActions().addAction(curAction);
        }

        getActions().addAction(getScene().createWidgetHoverAction());

        ImageWidget image = new ImageWidget((Scene) scene, descriptor.getImage());
//        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addChild(image);

        setBorder(NO_HOVER_BORDER);
        setBackground(UIManager.getColor("List.selectionBackground"));

    }

    public INodeWidget getAssociatedWidget() {
        return associatedWidget;
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);
        if (!previousState.isHovered() && state.isHovered()) {
            setOpaque(true);
        }
        if (previousState.isHovered() && !state.isHovered()) {
            setOpaque(false);
        }
    }
}
