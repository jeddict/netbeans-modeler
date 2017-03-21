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

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.border.Border;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.border.ShadowBorder;
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
public class PDarkColorScheme implements IColorScheme {

    private final Color SCENE_BACKGROUND;
    private final org.netbeans.api.visual.border.Border OPAQUE_BORDER;
    private final Image BUTTON_E;
    private final Image BUTTON_C;
    private final Color COLOR1;
    private final Color COLOR2;
    private final Color COLOR3;
    private final Color COLOR4;
    private final Color COLOR5;

    protected final Color WIDGET_BORDER_COLOR;
    private final Color WIDGET_SELECT_BORDER_COLOR;
    private final Color WIDGET_HOVER_BORDER_COLOR;

    private final Color WIDGET_HOVER_BACKGROUND;
    private final Color WIDGET_SELECT_BACKGROUND;
    private final Color WIDGET_BACKGROUND;

    private final Color WIDGET_HOVER_LBACKGROUND;
    private final Color WIDGET_SELECT_LBACKGROUND;
    private final Color WIDGET_LBACKGROUND;

    private final Border WIDGET_BORDER;
    private final Border WIDGET_SELECT_BORDER;
    private final Border WIDGET_HOVER_BORDER;
    
    private final Color WIDGET_TEXT_COLOR;

    private final Color EDGE_WIDGET_COLOR;
    private final Color EDGE_WIDGET_SELECT_COLOR;
    private final Color EDGE_WIDGET_HOVER_COLOR;

    private final Color PIN_WIDGET_BACKGROUND;
    private final Color PIN_WIDGET_HOVER_BACKGROUND;
    private final Color PIN_WIDGET_SELECT_BACKGROUND;
    private final Color PIN_WIDGET_HOVER_LBACKGROUND;
    private final Color PIN_WIDGET_SELECT_LBACKGROUND;

    private final Color PIN_WIDGET_TEXT_COLOR;
    private final Color PIN_WIDGET_HOVER_TEXT_COLOR;
    private final org.netbeans.api.visual.border.Border PIN_WIDGET_SELECT_BORDER;

    private final Color PIN_SEPERATOR_WIDGET_BACKGROUND;
    private final Color PIN_SEPERATOR_WIDGET_FOREGROUND;

    private static PDarkColorScheme instance;

    public static PDarkColorScheme getInstance() {
        if (instance == null) {
            synchronized (PDarkColorScheme.class) {
                if (instance == null) {
                    instance = new PDarkColorScheme();
                }
            }
        }
        return instance;
    }

    private PDarkColorScheme() {
        SCENE_BACKGROUND = new Color(40,40,40);
        OPAQUE_BORDER = BorderFactory.createOpaqueBorder(2, 8, 2, 8);
        BUTTON_E = ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/metro/expand.png");
        BUTTON_C = ImageUtilities.loadImage("org/netbeans/modeler/widget/theme/metro/collapse.png");
        COLOR1 = new Color(221, 235, 246);
        COLOR2 = new Color(255, 255, 255);
        COLOR3 = new Color(214, 235, 255);
        COLOR4 = new Color(255, 255, 255);
        COLOR5 = new Color(241, 249, 253);

        WIDGET_BORDER_COLOR = new Color(168, 168, 168);
        WIDGET_SELECT_BORDER_COLOR = new Color(94, 94, 94);
        WIDGET_HOVER_BORDER_COLOR = new Color(139, 139, 139);

        WIDGET_HOVER_BACKGROUND = new Color(66, 66, 66);
        WIDGET_SELECT_BACKGROUND = new Color(77, 77, 77);
        WIDGET_BACKGROUND = new Color(99, 99, 99);

        WIDGET_HOVER_LBACKGROUND = new Color(55, 55, 55);
        WIDGET_SELECT_LBACKGROUND = new Color(44, 44, 44);
        WIDGET_LBACKGROUND = new Color(66, 66, 66);

        WIDGET_BORDER = new ShadowBorder(new Color(155, 155, 155), 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        WIDGET_SELECT_BORDER = new ShadowBorder(new Color(130, 130, 130), 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        WIDGET_HOVER_BORDER = new ShadowBorder(new Color(100, 100, 100), 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);

        WIDGET_TEXT_COLOR = new Color(230, 230, 230);
        EDGE_WIDGET_COLOR = new Color(144, 144, 144);
        EDGE_WIDGET_SELECT_COLOR = new Color(111, 111, 111);
        EDGE_WIDGET_HOVER_COLOR = new Color(122, 122, 122);

        PIN_WIDGET_BACKGROUND = new Color(180, 180, 180);
        PIN_WIDGET_HOVER_BACKGROUND = new Color(180, 180, 180);
        PIN_WIDGET_SELECT_BACKGROUND = new Color(175, 175, 175);
        PIN_WIDGET_HOVER_LBACKGROUND = new Color(175, 175, 175);
        PIN_WIDGET_SELECT_LBACKGROUND = new Color(180, 180, 180);

        PIN_WIDGET_TEXT_COLOR = new Color(40 ,40, 40);
        PIN_WIDGET_HOVER_TEXT_COLOR = new Color(70, 70, 70);
        PIN_WIDGET_SELECT_BORDER = BorderFactory.createCompositeBorder(BorderFactory.createLineBorder(0, 1, 0, 1, WIDGET_BORDER_COLOR), BorderFactory.createLineBorder(2, 3, 2, 3, WIDGET_HOVER_BORDER_COLOR));

        PIN_SEPERATOR_WIDGET_BACKGROUND = new Color(133, 133, 133);
        PIN_SEPERATOR_WIDGET_FOREGROUND = Color.WHITE;
    }

    @Override
    public void installUI(IPNodeWidget widget) {
        widget.setBorder(WIDGET_BORDER);
        Widget header = widget.getHeader();
        Rectangle bound = widget.getHeader().getBounds();
        if (bound != null) {
            GradientPaint gp = new GradientPaint(bound.x + bound.width / 2, bound.y, WIDGET_LBACKGROUND, bound.x + bound.width / 2, bound.y + bound.height, WIDGET_BACKGROUND);
            header.setBackground(gp);
        }
        header.setBorder(OPAQUE_BORDER);
        widget.getHeader().setOpaque(true);
        widget.getNodeNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():WIDGET_TEXT_COLOR);
        widget.getNodeNameWidget().setFont(widget.getScene().getDefaultFont().deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
        Widget pinsSeparator = widget.getPinsSeparator();
        pinsSeparator.setForeground(PIN_SEPERATOR_WIDGET_BACKGROUND);
        widget.getMinimizeButton().setImage(this.getMinimizeWidgetImage(widget));
    }

    @Override
    public void updateUI(IPNodeWidget widget, ObjectState previousState, ObjectState state) {
        if (!previousState.isSelected() && state.isSelected()) {
            widget.bringToFront();
        }
        Rectangle bound = widget.getHeader().getBounds();
        if (bound != null) {
            if (state.isHovered()) {
                GradientPaint gp = new GradientPaint(bound.x + bound.width / 2, bound.y, WIDGET_HOVER_LBACKGROUND, bound.x + bound.width / 2, bound.y + bound.height, WIDGET_HOVER_BACKGROUND);
                widget.getHeader().setBackground(gp);
                widget.setBorder(WIDGET_HOVER_BORDER);
            } else if (state.isSelected() || state.isFocused()) {
                GradientPaint gp = new GradientPaint(bound.x, bound.y, WIDGET_SELECT_LBACKGROUND, bound.width, bound.height, WIDGET_SELECT_BACKGROUND);
                widget.getHeader().setBackground(gp);
                widget.setBorder(WIDGET_SELECT_BORDER);
            } else {
                GradientPaint gp = new GradientPaint(bound.x + bound.width / 2, bound.y, WIDGET_LBACKGROUND, bound.x + bound.width / 2, bound.y + bound.height, WIDGET_BACKGROUND);
                widget.getHeader().setBackground(gp);
                widget.setBorder(WIDGET_BORDER);
            }
        }

    }

    @Override
    public void installUI(IPEdgeWidget widget) {
        widget.setPaintControlPoints(true);
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
            widget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
            widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
            widget.setControlPointCutDistance(0);
        } else if (state.isHovered()) {
            widget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
            widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
            widget.setControlPointCutDistance(0);
        } else {

            widget.setControlPointShape(PointShape.NONE);
            widget.setEndPointShape(PointShape.NONE);
            widget.setControlPointCutDistance(5);
        }
    }

    @Override
    public void installUI(IPinWidget widget) {
        widget.setBorder(OPAQUE_BORDER);
        widget.setBackground(PIN_WIDGET_BACKGROUND);
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
        return widget.isMinimized() ? BUTTON_E : BUTTON_C;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void highlightUI(IEdgeWidget widget) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void highlightUI(IPinWidget widget) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
