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
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import javax.swing.border.Border;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.border.ShadowBorder;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.IPEdgeWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.pin.IPinSeperatorWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.openide.util.ImageUtilities;

/**
 * @author Gaurav Gupta
 */
public class PMacColorScheme implements IColorScheme {

    private final TexturePaint SCENE_BACKGROUND;
    private final org.netbeans.api.visual.border.Border OPAQUE_BORDER;
    private final Image BUTTON_G;
    private final Image BUTTON_Y;
    private final Image BUTTON_W;
    private final Color COLOR1;
    private final Color COLOR2;
    private final Color COLOR3;
    private final Color COLOR4;
    private final Color COLOR5;

    private final Color WIDGET_BORDER_COLOR;
    private final Color WIDGET_SELECT_BORDER_COLOR;
    private final Color WIDGET_HOVER_BORDER_COLOR;

    private final TexturePaint WIDGET_HOVER_BACKGROUND;
    private final TexturePaint WIDGET_SELECT_BACKGROUND;
    private final TexturePaint WIDGET_BACKGROUND;

    private final Border WIDGET_BORDER;
    private final Border WIDGET_SELECT_BORDER;
    private final Border WIDGET_HOVER_BORDER;
// private final Border WIDGET_HIGHT_BORDER ;

    private final Color EDGE_WIDGET_SELECT_COLOR;
    private final Color EDGE_WIDGET_HOVER_COLOR;
    private final Color EDGE_WIDGET_COLOR;

    private final Color PIN_WIDGET_BACKGROUND;
    private final Color PIN_WIDGET_LBACKGROUND;
    private final Color PIN_WIDGET_HOVER_BACKGROUND;
    private final Color PIN_WIDGET_HOVER_LBACKGROUND;
    private final Color PIN_WIDGET_SELECT_BACKGROUND;
    private final Color PIN_WIDGET_SELECT_LBACKGROUND;

    private final Color PIN_WIDGET_HOVER_TEXT_COLOR;
    private final Color PIN_WIDGET_TEXT_COLOR;
    private final org.netbeans.api.visual.border.Border PIN_WIDGET_SELECT_BORDER;

    private final Color PIN_SEPERATOR_WIDGET_BACKGROUND;
    private final Color PIN_SEPERATOR_WIDGET_FOREGROUND;
    private static PMacColorScheme instance;

    public static PMacColorScheme getInstance() {
        if (instance == null) {
            synchronized (PMacColorScheme.class) {
                if (instance == null) {
                    instance = new PMacColorScheme();
                }
            }
        }
        return instance;
    }

    private PMacColorScheme() {
        SCENE_BACKGROUND = new TexturePaint(ImageUtil.toBufferedImage(ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/BACKGROUND.jpg")), new Rectangle(0, 0, 3812, 2362));
        OPAQUE_BORDER = BorderFactory.createOpaqueBorder(2, 8, 2, 8);
        BUTTON_G = ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/BUTTON_G.png");
        BUTTON_Y = ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/BUTTON_Y.png");
        BUTTON_W = ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/BUTTON_W.png");
        COLOR1 = new Color(249, 249, 249);
        COLOR2 = new Color(237, 237, 237);
        COLOR3 = new Color(227, 227, 227);
        COLOR4 = new Color(249, 249, 249);
        COLOR5 = new Color(237, 237, 237);

        WIDGET_BORDER_COLOR = new Color(245, 245, 245);
        WIDGET_SELECT_BORDER_COLOR = new Color(180, 180, 180);
        WIDGET_HOVER_BORDER_COLOR = new Color(200, 200, 200);

        WIDGET_HOVER_BACKGROUND = new TexturePaint(ImageUtil.toBufferedImage(ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/MAC_TOOLBAR_LIGHT.png")), new Rectangle(0, 0, 658, 61));
        WIDGET_SELECT_BACKGROUND = new TexturePaint(ImageUtil.toBufferedImage(ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/MAC_TOOLBAR_DARK.png")), new Rectangle(0, 0, 658, 37));
        WIDGET_BACKGROUND = new TexturePaint(ImageUtil.toBufferedImage(ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/mac/MAC_TOOLBAR_AVG.png")), new Rectangle(0, 0, 660, 48));

        WIDGET_BORDER = new ShadowBorder(WIDGET_BORDER_COLOR, 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        WIDGET_SELECT_BORDER = new ShadowBorder(WIDGET_SELECT_BORDER_COLOR, 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        WIDGET_HOVER_BORDER = new ShadowBorder(WIDGET_HOVER_BORDER_COLOR, 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);

        EDGE_WIDGET_SELECT_COLOR = new Color(235, 235, 235);
        EDGE_WIDGET_HOVER_COLOR = new Color(255, 255, 255);
        EDGE_WIDGET_COLOR = new Color(250, 250, 250);

        PIN_WIDGET_BACKGROUND = new Color(120, 138, 176);
        PIN_WIDGET_LBACKGROUND = new Color(138, 154, 190);
        PIN_WIDGET_HOVER_BACKGROUND = new Color(120, 138, 176);
        PIN_WIDGET_HOVER_LBACKGROUND = new Color(138, 154, 190);
        PIN_WIDGET_SELECT_BACKGROUND = new Color(110, 128, 166);
        PIN_WIDGET_SELECT_LBACKGROUND = new Color(128, 144, 180);

        PIN_WIDGET_HOVER_TEXT_COLOR = Color.WHITE;
        PIN_WIDGET_TEXT_COLOR = new Color(34, 34, 34);
        PIN_WIDGET_SELECT_BORDER = BorderFactory.createCompositeBorder(BorderFactory.createLineBorder(0, 1, 0, 1, WIDGET_BORDER_COLOR), BorderFactory.createLineBorder(2, 3, 2, 3, WIDGET_HOVER_BORDER_COLOR));

        PIN_SEPERATOR_WIDGET_BACKGROUND = new Color(200, 200, 200);
        PIN_SEPERATOR_WIDGET_FOREGROUND = new Color(34, 34, 34);
    }

    @Override
    public void installUI(IPNodeWidget widget) {
        widget.setBorder(WIDGET_BORDER);
        Widget header = widget.getHeader();
        header.setBackground(WIDGET_BACKGROUND);
        header.setBorder(OPAQUE_BORDER);
        header.setOpaque(true);
        widget.getNodeNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():Color.WHITE);
        widget.getNodeNameWidget().setFont(widget.getScene().getDefaultFont().deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
        Widget pinsSeparator = widget.getPinsSeparator();
        pinsSeparator.setForeground(PIN_SEPERATOR_WIDGET_BACKGROUND);
        widget.getMinimizeButton().setImage(BUTTON_W);
    }

    @Override
    public void updateUI(IPNodeWidget widget, ObjectState previousState, ObjectState state) {
        if (!previousState.isSelected() && state.isSelected()) {
            widget.bringToFront();
        }
       
        if (state.isSelected() || state.isFocused()) {
            widget.getHeader().setBackground(WIDGET_SELECT_BACKGROUND);
            widget.setBorder(WIDGET_SELECT_BORDER);
            widget.getMinimizeButton().setImage(this.getMinimizeWidgetImage(widget));
        } else if (state.isHovered()) {
            widget.getHeader().setBackground(WIDGET_HOVER_BACKGROUND);
            widget.setBorder(WIDGET_HOVER_BORDER);
            widget.getMinimizeButton().setImage(this.getMinimizeWidgetImage(widget));
        } else {
            widget.getHeader().setBackground(WIDGET_BACKGROUND);
            widget.setBorder(WIDGET_BORDER);
            widget.getMinimizeButton().setImage(BUTTON_W);
        } 
    }

    @Override
    public void installUI(IPEdgeWidget widget) {
        widget.setPaintControlPoints(true);
        widget.setForeground(EDGE_WIDGET_COLOR);
    }

    @Override
    public void updateUI(IPEdgeWidget widget, ObjectState previousState, ObjectState state) {
        if (state.isSelected()) {
            widget.setForeground(EDGE_WIDGET_SELECT_COLOR);
        } else if (state.isHovered() || state.isFocused()) {
            widget.setForeground(EDGE_WIDGET_HOVER_COLOR);
        } else {
            widget.setForeground(EDGE_WIDGET_COLOR);
        }
        if (state.isSelected()) {
            widget.setStroke(new BasicStroke(1.5f));
            widget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
            widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
            widget.setControlPointCutDistance(0);
        } else if (state.isHovered()) {
            widget.setStroke(new BasicStroke(1.2f));
            widget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
            widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
            widget.setControlPointCutDistance(0);
        } else {
            widget.setStroke(new BasicStroke(1f));
            widget.setControlPointShape(PointShape.NONE);
            widget.setEndPointShape(PointShape.NONE);
            widget.setControlPointCutDistance(5);
        }
    }

    @Override
    public void installUI(IPinWidget widget) {
        widget.setBorder(OPAQUE_BORDER);
        Rectangle bound = widget.getBounds();
        if (bound != null) {
            GradientPaint gp = new GradientPaint(bound.x, bound.y, PIN_WIDGET_BACKGROUND, bound.x, bound.height, PIN_WIDGET_LBACKGROUND);
            widget.setBackground(gp);
        }
        widget.getPinNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():PIN_WIDGET_TEXT_COLOR);
        widget.getPinNameWidget().setFont(widget.getScene().getDefaultFont().deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
    }

    @Override
    public void updateUI(IPinWidget widget, ObjectState previousState, ObjectState state) {
        widget.setOpaque(state.isHovered() || state.isFocused());

        Rectangle bound = widget.getBounds();
        if (bound != null) {
            if (state.isFocused() || state.isSelected()) {
                GradientPaint gp = new GradientPaint(bound.x, bound.y, PIN_WIDGET_SELECT_BACKGROUND, bound.x, bound.height, PIN_WIDGET_SELECT_LBACKGROUND);
                widget.setBackground(gp);
            } else {
                GradientPaint gp = new GradientPaint(bound.x, bound.y, PIN_WIDGET_HOVER_BACKGROUND, bound.x, bound.height, PIN_WIDGET_HOVER_LBACKGROUND);
                widget.setBackground(gp);
            }
        }
        
        if (widget.getTextDesign().getColor() == null) {
            if (state.isHovered() || state.isSelected()) {
                widget.getPinNameWidget().setForeground(PIN_WIDGET_HOVER_TEXT_COLOR);
            } else {
                widget.getPinNameWidget().setForeground(PIN_WIDGET_TEXT_COLOR);
            }
        } else {
            widget.getPinNameWidget().setForeground(widget.getTextDesign().getColor());
        }
        
        if (state.isSelected()) {
            widget.setBorder(PIN_WIDGET_SELECT_BORDER);
        } else {
            widget.setBorder(OPAQUE_BORDER);
        }
    }

    @Override
    public boolean isNodeMinimizeButtonOnRight(IPNodeWidget widget) {
        return true;
    }

    @Override
    public Image getMinimizeWidgetImage(IPNodeWidget widget) {
        return widget.isMinimized() ? BUTTON_G : BUTTON_Y;
    }
  
    @Override
    public IPinSeperatorWidget createPinCategoryWidget(IPNodeWidget widget, String categoryDisplayName) {
        Scene scene = widget.getScene();
        IPinSeperatorWidget label = new PinSeperatorWidget(scene, categoryDisplayName);
        installUI(label);
        return label;
    }

    @Override
    public void installUI(IPinSeperatorWidget label) {
        label.setOpaque(true);
        label.setBackground(PIN_SEPERATOR_WIDGET_BACKGROUND);
        label.setForeground(PIN_SEPERATOR_WIDGET_FOREGROUND);
        Font fontPinCategory = label.getScene().getDefaultFont().deriveFont(9.5f);
        label.setFont(fontPinCategory);
        label.setAlignment(LabelWidget.Alignment.CENTER);
        label.setCheckClipping(true);
    }

    @Override
    public void installUI(IModelerScene scene) {
        scene.setBackground(SCENE_BACKGROUND);
    }

    @Override
    public void highlightUI(IPNodeWidget widget) {
        widget.setBorder(PFactory.createPNodeBorder(new Color(60, 60, 70), 3, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5));
    }

    @Override
    public void highlightUI(IEdgeWidget widget) {
        widget.setStroke(new BasicStroke(1.9f));
        widget.setForeground(new Color(100, 100, 110));
    }

    @Override
    public void highlightUI(IPinWidget widget) {
        widget.setBorder(BorderFactory.createCompositeBorder(BorderFactory.createLineBorder(0, 1, 0, 1, new Color(60, 60, 70)), BorderFactory.createLineBorder(3, 4, 3, 4, new Color(60, 60, 70))));
    }
}
