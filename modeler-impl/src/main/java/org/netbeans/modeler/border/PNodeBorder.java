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
package org.netbeans.modeler.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import org.netbeans.api.visual.border.Border;

/**
 * @author David Kaspar
 */
public class PNodeBorder implements Border {

    private Color colorBorder;
    private Insets insets;
    private Stroke stroke;
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    private Color color5;

    public PNodeBorder(Color colorBorder, int thickness, Color color1, Color color2, Color color3, Color color4, Color color5) {
        this.colorBorder = colorBorder;
        this.insets = new Insets(thickness, thickness, thickness, thickness);
        this.stroke = new BasicStroke(thickness);
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
    }

    @Override
    public Insets getInsets() {
        return insets;
    }

    @Override
    public void paint(Graphics2D gr, Rectangle bounds) {
        Shape previousClip = gr.getClip();
        gr.clip(new RoundRectangle2D.Float(bounds.x, bounds.y, bounds.width, bounds.height, 4, 4));

        drawGradient(gr, bounds, color1, color2, 0f, 0.3f);
        drawGradient(gr, bounds, color2, color3, 0.3f, 0.764f);
        drawGradient(gr, bounds, color3, color4, 0.764f, 0.927f);
        drawGradient(gr, bounds, color4, color5, 0.927f, 1f);

        gr.setColor(colorBorder);
        Stroke previousStroke = gr.getStroke();
        gr.setStroke(stroke);
        gr.draw(new RoundRectangle2D.Float(bounds.x + 0.5f, bounds.y + 0.5f, bounds.width - 1, bounds.height - 1, 4, 4));
        gr.setStroke(previousStroke);

        gr.setClip(previousClip);
    }

    private void drawGradient(Graphics2D gr, Rectangle bounds, Color color1, Color color2, float y1, float y2) {
        y1 = bounds.y + y1 * bounds.height;
        y2 = bounds.y + y2 * bounds.height;
        gr.setPaint(new GradientPaint(bounds.x, y1, color1, bounds.x, y2, color2));
        gr.fill(new Rectangle.Float(bounds.x, y1, bounds.x + bounds.width, y2));
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

}
