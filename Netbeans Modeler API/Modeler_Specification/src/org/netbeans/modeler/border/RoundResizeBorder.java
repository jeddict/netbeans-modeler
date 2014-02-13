package org.netbeans.modeler.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import org.netbeans.api.visual.action.ResizeProvider;

/**
 *
 *
 */
public class RoundResizeBorder implements ResizeBorder {

    private static final BasicStroke STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{6.0f, 3.0f}, 0.0f);
    public static final BasicStroke DEFAULT_DASH = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_MITER, new float[]{10, 10}, 0);

    private int thickness;
    private Color color;
    private boolean outer;
    private ArrayList<ResizeProvider.ControlPoint> enablePoints;
    private ArrayList<ResizeProvider.ControlPoint> disablePoints;

    private Rectangle widgetArea;

    private int arcWidth;
    private int arcHeight;
    private boolean cornerPoint;
    private int insetWidth;
    private int insetHeight;
    private Paint fillColor;
    private Paint drawColor;
    private Stroke stroke;

    /*
     * @param thickness the thickness of the border
     * @param color the border color
     * @param points points which will be filled
     * @param outer if true, then the rectangle encapsulate the squares too; if false, then the rectangle encapsulates the widget client area only
     */
    public RoundResizeBorder(int thickness, Color color, ResizeProvider.ControlPoint[] enablePoints, ResizeProvider.ControlPoint[] disablePoints, boolean outer, int arcWidth, int arcHeight, boolean cornerPoint, int insetWidth, int insetHeight, Paint fillColor, Paint drawColor, Stroke stroke) {
        this.thickness = thickness;
        this.color = color;
        this.outer = outer;
        this.enablePoints = new ArrayList<ResizeProvider.ControlPoint>();
        if (enablePoints != null) {
            this.enablePoints.addAll(Arrays.asList(enablePoints));
        }
        this.disablePoints = new ArrayList<ResizeProvider.ControlPoint>();
        if (disablePoints != null) {
            this.disablePoints.addAll(Arrays.asList(disablePoints));
        }

        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.cornerPoint = cornerPoint;
        this.insetWidth = insetWidth;
        this.insetHeight = insetHeight;
        this.fillColor = fillColor;
        this.drawColor = drawColor != null ? drawColor : Color.BLACK;
        this.stroke = stroke;

    }

    public RoundResizeBorder(int thickness, Color color, ResizeProvider.ControlPoint[] enablePoints, ResizeProvider.ControlPoint[] disablePoints, boolean outer, int arcWidth, int arcHeight, boolean cornerPoint, Paint drawColor) {
        this(thickness, color, enablePoints, disablePoints, outer, arcWidth, arcHeight, cornerPoint, 0, 0, null, drawColor, null);
    }

    public RoundResizeBorder(int thickness, Color color, boolean outer, int arcWidth, int arcHeight, boolean cornerPoint, Paint drawColor) {
        this(thickness, color, new ResizeProvider.ControlPoint[]{}, new ResizeProvider.ControlPoint[]{}, outer, arcWidth, arcHeight, cornerPoint, 0, 0, null, drawColor, null);
    }

    public Insets getInsets() {
        return new Insets(thickness, thickness, thickness, thickness);//new Insets (thickness, thickness, thickness, thickness);
    }

    public boolean isOuter() {
        return outer;
    }

    public void paint(Graphics2D gr, Rectangle bounds) {
//  thickness=2;
        gr.setColor(color);
        //troke stroke = gr.getStroke ();
        gr.setStroke(STROKE);
        if (outer) {
            gr.draw(new RoundRectangle2D.Double(bounds.x + 0.5, bounds.y + 0.5, bounds.width - 1.0, bounds.height - 1.0, arcWidth, arcHeight));
        } else {
            gr.draw(new RoundRectangle2D.Double(bounds.x + thickness + 0.5,
                    bounds.y + thickness + 0.5,
                    bounds.width - thickness - thickness - 1.0,
                    bounds.height - thickness - thickness - 1.0, arcWidth, arcHeight));
            widgetArea = new Rectangle((int) (bounds.x + thickness + 0.5),
                    (int) (bounds.y + thickness + 0.5),
                    (int) (bounds.width - thickness - thickness - 1.0),
                    (int) (bounds.height - thickness - thickness - 1.0));
        }
        //gr.setStroke (stroke);

        Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);

        if (cornerPoint) {
            //TBD, may it have sense for perfomance to have 8 boolean vars
            if (enablePoints.contains(ResizeProvider.ControlPoint.TOP_LEFT)) {
                gr.fillRect(bounds.x, bounds.y, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.TOP_LEFT)) {
                gr.drawRect(bounds.x, bounds.y, thickness, thickness);
            }

            if (enablePoints.contains(ResizeProvider.ControlPoint.TOP_RIGHT)) {
                gr.fillRect(bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.TOP_RIGHT)) {
                gr.drawRect(bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
            }
            if (enablePoints.contains(ResizeProvider.ControlPoint.BOTTOM_LEFT)) {
                gr.fillRect(bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.BOTTOM_LEFT)) {
                gr.drawRect(bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
            }
            if (enablePoints.contains(ResizeProvider.ControlPoint.BOTTOM_RIGHT)) {
                gr.fillRect(bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.BOTTOM_RIGHT)) {
                gr.drawRect(bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);
            }
        }

        if (bounds.width >= thickness * 5) {
            if (enablePoints.contains(ResizeProvider.ControlPoint.TOP_CENTER)) {
                gr.fillRect(center.x - thickness / 2, bounds.y, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.TOP_CENTER)) {
                gr.drawRect(center.x - thickness / 2, bounds.y, thickness, thickness);
            }
            if (enablePoints.contains(ResizeProvider.ControlPoint.BOTTOM_CENTER)) {
                gr.fillRect(center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.BOTTOM_CENTER)) {
                gr.drawRect(center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
            }
        }
        if (bounds.height >= thickness * 5) {
            if (enablePoints.contains(ResizeProvider.ControlPoint.CENTER_LEFT)) {
                gr.fillRect(bounds.x, center.y - thickness / 2, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.CENTER_LEFT)) {
                gr.drawRect(bounds.x, center.y - thickness / 2, thickness, thickness);
            }
            if (enablePoints.contains(ResizeProvider.ControlPoint.CENTER_RIGHT)) {
                gr.fillRect(bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);
            } else if (disablePoints.contains(ResizeProvider.ControlPoint.CENTER_RIGHT)) {
                gr.drawRect(bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);
            }
        }

    }

    public boolean isOpaque() {
        return outer;
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

    /**
     * @return the widgetArea
     */
    public Rectangle getWidgetArea() {
        return widgetArea;
    }

    /**
     * @param widgetArea the widgetArea to set
     */
    public void setWidgetArea(Rectangle widgetArea) {
        this.widgetArea = widgetArea;
    }

}
