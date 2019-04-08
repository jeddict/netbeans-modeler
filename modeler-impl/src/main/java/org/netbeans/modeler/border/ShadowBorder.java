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
package org.netbeans.modeler.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.border.Border;

/**
 * Border with a drop shadow.
 */
public class ShadowBorder implements Border, Serializable {

    private static final long serialVersionUID = 6854989457150641240L;

    private Insets insets;
    private Color bg;
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    private Color color5;

//    public static ShadowBorder sharedInstance = new ShadowBorder();
    public ShadowBorder(Color bg, int thickness, Color color1, Color color2, Color color3, Color color4, Color color5) {
        this.bg = bg;
        insets = new Insets(0, 0, thickness, thickness);
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
    }

    public ShadowBorder(Color bg, int top_thickness, int right_thickness, int bottom_thickness, int left_thickness, Color color1, Color color2, Color color3, Color color4, Color color5) {
        this.bg = bg;
        insets = new Insets(top_thickness, right_thickness, bottom_thickness, left_thickness);
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

        Rectangle bounds = new Rectangle(x, y, w, h);
        Graphics2D g2 = (Graphics2D) g;
//        drawGradient(g2, bounds, color1, color2, 0f, 0.3f);
//        drawGradient(g2, bounds, color2, color3, 0.3f, 0.764f);
//        drawGradient(g2, bounds, color3, color4, 0.764f, 0.927f);
//        drawGradient(g2, bounds, color4, color5, 0.927f, 1f);

        drawGradient(g2, bounds, color1, color2, 0f, 0.125f);
        drawGradient(g2, bounds, color2, color3, 0.125f, 0.250f);
        drawGradient(g2, bounds, color3, color4, 0.250f, 0.375f);
        drawGradient(g2, bounds, color4, color5, 0.375f, 0.620f);
//
        if (bg != null) {
            Color mid = bg.darker();
            Color edge = average(mid, bg);
            Color ledge = average(edge.brighter(), edge);

            g.setColor(bg);
            g.drawLine(0, h - 2, w, h - 2);
            g.drawLine(0, h - 1, w, h - 1);
            g.drawLine(w - 2, 0, w - 2, h);
            g.drawLine(w - 1, 0, w - 1, h);

            // draw the drop-shadow
            g.setColor(mid);
            g.drawLine(1, h - 2, w - 2, h - 2);
            g.drawLine(w - 2, 1, w - 2, h - 2);

            g.setColor(edge);
            g.drawLine(0, 0, 0, h - 1);
            g.drawLine(0, 0, w - 1, 0);
            g.drawLine(2, h - 1, w - 2, h - 1);
            g.drawLine(w - 1, 2, w - 1, h - 2);

            g.setColor(ledge);
            g.drawLine(3, h, w, h);
            g.drawLine(w, 3, w, h);
        }
    }

    private void drawGradient(Graphics2D gr, Rectangle bounds, Color color1, Color color2, float y1, float y2) {
        y1 = bounds.y + y1 * bounds.height;
        y2 = bounds.y + y2 * bounds.height;
        gr.setPaint(new GradientPaint(bounds.x, y1, color1, bounds.x, y2, color2));
        gr.fill(new Rectangle.Float(bounds.x, y1, bounds.x + bounds.width, y2));

    }

    private static Color average(Color c1, Color c2) {
        int red = c1.getRed() + (c2.getRed() - c1.getRed()) / 2;
        int green = c1.getGreen() + (c2.getGreen() - c1.getGreen()) / 2;
        int blue = c1.getBlue() + (c2.getBlue() - c1.getBlue()) / 2;
        return new Color(red, green, blue);
    }

//    public static ShadowBorder getSharedInstance() {
//        return sharedInstance;
//    }
}
