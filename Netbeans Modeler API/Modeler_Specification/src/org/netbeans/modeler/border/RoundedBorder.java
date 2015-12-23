package org.netbeans.modeler.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import org.netbeans.api.visual.border.Border;

/**
 *
 */
public class RoundedBorder implements Border {

    public static final BasicStroke DEFAULT_DASH = new BasicStroke(1,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
            BasicStroke.JOIN_MITER, new float[]{10, 10}, 0);
    private int arcWidth;
    private int arcHeight;
    private int insetWidth;
    private int insetHeight;
    private Paint fillColor;
    private Paint drawColor;
    private Stroke stroke;

    /**
     * Creates a rounded rectangle border with specified arc width and height
     * using the specified color
     *
     * @param arcWidth the width of the arc of this rounded rectangle border.
     * @param arcHeight the height of the arc of this rounded rectangle border.
     * @param drawColor the color to draw the border. If null, Black color is
     * used by default.
     */
    public RoundedBorder(int arcWidth, int arcHeight, Paint drawColor) {
        this(arcWidth, arcHeight, 0, 0, null, drawColor, null);
    }

    /**
     * Creates a rounded rectangle border with specified arc width and height
     * using the specified color and stroke
     *
     * @param arcWidth the width of the arc of this rounded rectangle border.
     * @param arcHeight the height of the arc of this rounded rectangle border.
     * @param drawColor the color to draw the border. If null, Black color is
     * used by default.
     * @param stroke the stroke to draw the border. If null, the current stroke
     * is used.
     */
    public RoundedBorder(int arcWidth, int arcHeight, Paint drawColor, Stroke stroke) {
        this(arcWidth, arcHeight, 0, 0, null, drawColor, stroke);
    }

    /**
     * Creates a rounded rectangle border with specified arc width and height
     * using the specified fill color, draw color and stroke
     *
     * @param arcWidth the width of the arc of this rounded rectangle border.
     * @param arcHeight the height of the arc of this rounded rectangle border.
     * @param fillColor the color to fill the object; if null, the object is not
     * filled.
     * @param drawColor the color to draw the border. If null, Black color is
     * used by default.
     * @param stroke the stroke to draw the border. If null, the current stroke
     * is used.
     */
    public RoundedBorder(int arcWidth, int arcHeight, Paint fillColor, Paint drawColor, Stroke stroke) {
        this(arcWidth, arcHeight, 0, 0, fillColor, drawColor, stroke);
    }

    /**
     * Creates a rounded rectangle border with specified attributes and the
     * current stroke
     *
     * @param arcWidth the width of the arc of this rounded rectangle border.
     * @param arcHeight the height of the arc of this rounded rectangle border.
     * @param insetWidth
     * @param insetHeight
     * @param fillColor the color to fill the object; if null, the object is not
     * filled.
     * @param drawColor the color to draw the border. If not set, Black color is
     * used by default.
     */
    public RoundedBorder(int arcWidth, int arcHeight, int insetWidth, int insetHeight, Paint fillColor, Paint drawColor) {
        this(arcWidth, arcHeight, insetWidth, insetHeight, fillColor, drawColor, null);
    }

    /**
     * Creates a rounded rectangle border with specified attributes
     *
     * @param arcWidth the width of the arc of this rounded rectangle border.
     * @param arcHeight the height of the arc of this rounded rectangle border.
     * @param insetWidth
     * @param insetHeight
     * @param fillColor the color to fill the object; if null, the object is not
     * filled.
     * @param drawColor the color to draw the border. If not set, Black color is
     * used by default.
     * @param stroke the stroke to draw the border. If null, the current stroke
     * is used.
     */
    public RoundedBorder(int arcWidth, int arcHeight, int insetWidth, int insetHeight, Paint fillColor, Paint drawColor, Stroke stroke) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.insetWidth = insetWidth;
        this.insetHeight = insetHeight;
        this.fillColor = fillColor;
        this.drawColor = drawColor != null ? drawColor : Color.BLACK;
        this.stroke = stroke;
    }

    @Override
    public Insets getInsets() {
        return new Insets(insetHeight, insetWidth, insetHeight, insetWidth);
    }

    public void setFillColor(Paint val) {
        this.fillColor = val;
    }

    public Paint getFillColor() {
        return this.fillColor;
    }

    public void paintBackground(Graphics2D gr, Rectangle bounds) {
        Paint previousPaint = gr.getPaint();
        Insets insets = this.getInsets();
        if (fillColor != null) {
            gr.setPaint(fillColor);
            gr.fill(new RoundRectangle2D.Float(
                    bounds.x + insets.left,
                    bounds.y + insets.top,
                    bounds.width - insets.left - insets.right,
                    bounds.height - insets.top - insets.bottom,
                    arcWidth, arcHeight));

            if (previousPaint != gr.getPaint()) {
                gr.setPaint(previousPaint);
            }
        }
    }

    @Override
    public void paint(Graphics2D gr, Rectangle bounds) {
        // paint the border
        Paint previousPaint = gr.getPaint();
        Stroke previousStroke = gr.getStroke();
        if (drawColor != null) {
            gr.setPaint(drawColor);
            if (stroke != null) {
                gr.setStroke(stroke);
            }
            gr.draw(new RoundRectangle2D.Float(
                    bounds.x + 0.5f, bounds.y + 0.5f,
                    bounds.width - 1, bounds.height - 1,
                    arcWidth, arcHeight));
        }
        // pain the back ground
        paintBackground(gr, bounds);

        // reset to the previous paint and stroke
        if (previousPaint != gr.getPaint()) {
            gr.setPaint(previousPaint);
        }

        if (previousStroke != gr.getStroke()) {
            gr.setStroke(previousStroke);
        }

    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
