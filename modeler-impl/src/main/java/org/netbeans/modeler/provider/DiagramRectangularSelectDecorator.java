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
package org.netbeans.modeler.provider;

import java.awt.Color;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.widget.context.ContextPaletteManager;

/**
 *
 *
 */
public class DiagramRectangularSelectDecorator implements RectangularSelectDecorator {

    private Scene scene;
    private String tool;

    public DiagramRectangularSelectDecorator(Scene scene, String tool) {
        this.scene = scene;
        this.tool = tool;
    }

    @Override
    public Widget createSelectionWidget() {

        ContextPaletteManager palette = scene.getLookup().lookup(ContextPaletteManager.class);
        if (palette != null) {
            palette.cancelPalette();
        }

        Widget widget = new Widget(scene);
        if (tool.equals(DesignerTools.SELECT)) {
            widget.setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
        } else {
            widget.setBorder(BorderFactory.createLineBorder(1, Color.BLACK));
        }
        return widget;
    }
}
