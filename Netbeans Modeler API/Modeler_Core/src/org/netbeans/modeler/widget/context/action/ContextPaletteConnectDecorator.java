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
package org.netbeans.modeler.widget.context.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 *
 *
 */
public class ContextPaletteConnectDecorator implements ConnectDecorator {

    public ContextPaletteConnectDecorator() {
    }

    public ConnectionWidget createConnectionWidget(Scene scene) {
        ConnectionWidget widget = new ConnectionWidget(scene);
        BasicStroke DEFAULT_DASH = new BasicStroke(1.2f,
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                BasicStroke.JOIN_MITER, new float[]{4, 4}, 0);
        widget.setStroke(DEFAULT_DASH);
//        widget.setForeground(new Color(242, 132, 0));

//        widget.setTargetAnchorShape(AnchorShape.NONE);
        return widget;
    }

    public Anchor createSourceAnchor(Widget sourceWidget) {
        return NBModelerUtil.getAnchor((INodeWidget) sourceWidget);
    }

    public Anchor createTargetAnchor(Widget targetWidget) {
        return NBModelerUtil.getAnchor((INodeWidget) targetWidget);
    }

    public Anchor createFloatAnchor(Point location) {
        return AnchorFactory.createFixedAnchor(location);
    }
}
