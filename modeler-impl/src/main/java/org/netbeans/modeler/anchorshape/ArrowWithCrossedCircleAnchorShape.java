/**
 * Copyright 2013-2022 Gaurav Gupta
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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import org.netbeans.api.visual.anchor.AnchorShape;

/**
 *
 */
public class ArrowWithCrossedCircleAnchorShape implements AnchorShape {

    public static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private int arrowSize;
    private int circleSize;
    private double cutDistance;
    private GeneralPath generalPath;

    /**
     * Creates a triangular anchor shape.
     *
     * @param arrowSize the size of triangle
     * @param circleSize diameter of circle
     * @param cutDistance the cut distance
     */
    public ArrowWithCrossedCircleAnchorShape(int arrowSize, int circleSize, double cutDistance) {
        this.arrowSize = arrowSize;
        this.cutDistance = cutDistance;
        this.circleSize = circleSize;
        float side = arrowSize;

        generalPath = new GeneralPath();
        generalPath.moveTo(side, -side / 2);
        generalPath.lineTo(0, 0);
        generalPath.lineTo(side, side / 2);
    }

    @Override
    public boolean isLineOriented() {
        return true;
    }

    @Override
    public int getRadius() {
        return (int) Math.ceil(1.5f * arrowSize);
    }

    @Override
    public double getCutDistance() {
        return cutDistance;
    }

    @Override
    public void paint(Graphics2D graphics, boolean source) {
        Stroke stroke = graphics.getStroke();
        Color color = graphics.getColor();
        graphics.setStroke(STROKE);
        graphics.draw(generalPath);
        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawOval(-circleSize / 2, -circleSize / 2, circleSize, circleSize);
        graphics.drawLine(circleSize / 2, circleSize / 2, -circleSize / 2, -circleSize / 2);
        graphics.setStroke(stroke);
        graphics.setColor(color);
    }
}
