/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modeler.anchorshape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import org.netbeans.api.visual.anchor.AnchorShape;

/**
 * @author Trey Spiva
 */
public class DiamondAnchorShape implements AnchorShape {

    public static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private final int size;
    private final boolean filled;
    private final double cutDistance;
    private final GeneralPath generalPath;
    private final Color color;

    /**
     * Creates a triangular anchor shape.
     *
     * @param size the size of triangle
     * @param color the color of triangle
     * @param filled if true, then the triangle is filled
     * @param cutDistance the cut distance
     */
    public DiamondAnchorShape(int size, Color color, boolean filled, double cutDistance) {
        this.size = size;
        this.filled = filled;
        this.cutDistance = cutDistance;
        this.color = color;
        float side = size * 0.3f;
        float half = size / 2;

        generalPath = new GeneralPath();
        generalPath.moveTo(0.0f, 0.0f);
        generalPath.lineTo(half, -side);
        generalPath.lineTo(size, 0);
        generalPath.lineTo(half, side);
        generalPath.lineTo(0.0f, 0.0f);
    }

    @Override
    public boolean isLineOriented() {
        return true;
    }

    @Override
    public int getRadius() {
        return (int) Math.ceil(1.5f * size);
    }

    @Override
    public double getCutDistance() {
        return cutDistance;
    }

    @Override
    public void paint(Graphics2D graphics, boolean source) {
        graphics.setColor(color);
        if (filled) {
            graphics.fill(generalPath);
        } else {
            Stroke stroke = graphics.getStroke();
            graphics.setStroke(STROKE);
            graphics.draw(generalPath);
            graphics.setStroke(stroke);
        }
//        int side = (int)(size * 0.3f);
//        int half = size / 2;
//
//        graphics.drawLine(0, 0, half, -side);
//        graphics.drawLine(half, -side, size, 0);
//        graphics.drawLine(size, 0, half, side);
//        graphics.drawLine(half, side, 0, 0);
    }
}
