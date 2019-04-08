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
package org.netbeans.modeler.anchorshape;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.netbeans.api.visual.anchor.AnchorShape;

public class CircleAnchorShape implements AnchorShape {

    private Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private int circleSize;

    public CircleAnchorShape(int circleSize) {
        super();
        this.circleSize = circleSize;
    }

    public CircleAnchorShape(int circleSize, Stroke stroke) {
        super();
        this.circleSize = circleSize;
        this.stroke = stroke;
    }

    @Override
    public boolean isLineOriented() {
        return true;
    }

    @Override
    public int getRadius() {
        return (int) Math.ceil(circleSize);
    }

    @Override
    public double getCutDistance() {
        return circleSize / 2;
    }

    @Override
    public void paint(Graphics2D graphics, boolean source) {
        graphics.setStroke(stroke);
        graphics.drawOval(-circleSize / 2, -circleSize / 2, circleSize, circleSize);

    }
}
