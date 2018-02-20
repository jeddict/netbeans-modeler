/**
 * Copyright 2013-2018 Gaurav Gupta
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
public class PWoodColorScheme implements IColorScheme {

    private final Color CONST;

    private final Color WIDGET_BORDER_COLOR;
    private final Color WIDGET_SELECT_BORDER_COLOR;
    private final Color WIDGET_HOVER_BORDER_COLOR;

    private final Color COLOR_HIGHLIGHTED;

    private final Color WIDGET_HOVER_BACKGROUND;
    private final Color WIDGET_SELECT_BACKGROUND;
    private final Color WIDGET_BACKGROUND;

    private final Color WIDGET_HOVER_LBACKGROUND;
    private final Color WIDGET_SELECT_LBACKGROUND;
    private final Color WIDGET_LBACKGROUND;

    private final Color PIN_WIDGET_HOVER_BACKGROUND;
    private final Color PIN_WIDGET_SELECT_BACKGROUND;

    private final Color PIN_WIDGET_TEXT;

    private final Color COLOR1;
    private final Color COLOR2;
    private final Color COLOR3;
    private final Color COLOR4;
    private final Color COLOR5;

    private final Color PIN_SEPERATOR_WIDGET_BACKGROUND;
    private final Color PIN_SEPERATOR_WIDGET_FOREGROUND;
    private final org.netbeans.api.visual.border.Border BORDER_PIN;

    private final Border WIDGET_BORDER;
    private final Border WIDGET_SELECT_BORDER;
    private final Border WIDGET_HOVER_BORDER;

    private final org.netbeans.api.visual.border.Border PIN_WIDGET_SELECT_BORDER;
    private static PWoodColorScheme instance;

    public static PWoodColorScheme getInstance() {
        if (instance == null) {
            synchronized (PWoodColorScheme.class) {
                if (instance == null) {
                    instance = new PWoodColorScheme();
                }
            }
        }
        return instance;
    }

    private PWoodColorScheme() {
        CONST = new Color(98, 168, 217);

        WIDGET_BORDER_COLOR = new Color(63, 54, 49);//DEFAULT BORDER
        WIDGET_SELECT_BORDER_COLOR = new Color(50, 54, 39);//SELECTED BORDER
        WIDGET_HOVER_BORDER_COLOR = new Color(87, 74, 68);//HOVER BORDER

        COLOR_HIGHLIGHTED = new Color(222, 0, 0);//(46,139,204);

        WIDGET_HOVER_BACKGROUND = new Color(87, 74, 68);//HOVER BackGround//(98, 168, 217);
        WIDGET_SELECT_BACKGROUND = new Color(50, 54, 39);//HOVER BackGround//(98, 168, 217);
        WIDGET_BACKGROUND = new Color(63, 54, 49);// BackGround//(98, 168, 217);

        WIDGET_HOVER_LBACKGROUND = new Color(92, 79, 71);//HOVER BackGround//(98, 168, 217);
        WIDGET_SELECT_LBACKGROUND = new Color(60, 51, 47);//HOVER BackGround//(98, 168, 217);
        WIDGET_LBACKGROUND = new Color(79, 68, 62);// BackGround//(98, 168, 217);

        PIN_WIDGET_HOVER_BACKGROUND = new Color(218, 148, 81);//HOVER BackGround//(98, 168, 217);
        PIN_WIDGET_SELECT_BACKGROUND = new Color(236, 170, 110);// BackGround//(98, 168, 217);

        PIN_WIDGET_TEXT = new Color(187, 175, 160);

        COLOR1 = new Color(89, 73, 64);
        COLOR2 = new Color(79, 63, 54);
        COLOR3 = new Color(109, 93, 84);
        COLOR4 = new Color(89, 73, 64);
        COLOR5 = new Color(109, 93, 84);

        PIN_SEPERATOR_WIDGET_BACKGROUND = new Color(50, 52, 38);//(94, 156, 210);
        PIN_SEPERATOR_WIDGET_FOREGROUND = Color.WHITE;
        BORDER_PIN = BorderFactory.createOpaqueBorder(2, 8, 2, 8);

        WIDGET_BORDER = new ShadowBorder(new Color(255, 255, 255), 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        WIDGET_SELECT_BORDER = new ShadowBorder(new Color(230, 230, 230), 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        WIDGET_HOVER_BORDER = new ShadowBorder(new Color(200, 200, 200), 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);

        PIN_WIDGET_SELECT_BORDER = BorderFactory.createCompositeBorder(BorderFactory.createLineBorder(0, 1, 0, 1, WIDGET_BORDER_COLOR), BorderFactory.createLineBorder(2, 3, 2, 3, WIDGET_HOVER_BORDER_COLOR));

    }

    @Override
    public void installUI(IPNodeWidget widget) {
        widget.setBorder(WIDGET_BORDER);

        Widget header = widget.getHeader();
        header.setBackground(WIDGET_HOVER_BACKGROUND);
        header.setBorder(BORDER_PIN);
        widget.getHeader().setOpaque(true);
        widget.getNodeNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():Color.WHITE);
        Font font = widget.getNodeNameWidget().getFont()!=null?widget.getNodeNameWidget().getFont():widget.getScene().getDefaultFont();
        widget.getNodeNameWidget().setFont(font.deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
        Widget pinsSeparator = widget.getPinsSeparator();
        pinsSeparator.setForeground(PIN_SEPERATOR_WIDGET_BACKGROUND);

        widget.getMinimizeButton().setImage(this.getMinimizeWidgetImage(widget));
    }

    @Override
    public void updateUI(IPNodeWidget widget, ObjectState previousState, ObjectState state) {
        if (!previousState.isSelected() && state.isSelected()) {
            widget.bringToFront();
        }

//        boolean hover = state.isHovered() || state.isFocused();
//        widget.getHeader().setOpaque(!hover);
        Rectangle bound = widget.getHeader().getBounds();
        if (bound != null) {
            if (state.isHovered()) {
                GradientPaint gp = new GradientPaint(bound.x + bound.width / 2, bound.y, WIDGET_HOVER_BACKGROUND, bound.x + bound.width / 2, bound.y + bound.height, WIDGET_HOVER_BACKGROUND);
                widget.getHeader().setBackground(gp);
            } else if (state.isSelected() || state.isFocused()) {
                GradientPaint gp = new GradientPaint(bound.x, bound.y, WIDGET_SELECT_LBACKGROUND, bound.width, bound.height, WIDGET_SELECT_BACKGROUND);
                widget.getHeader().setBackground(gp);
            } else {
                GradientPaint gp = new GradientPaint(bound.x + bound.width / 2, bound.y, WIDGET_LBACKGROUND, bound.x + bound.width / 2, bound.y + bound.height, WIDGET_BACKGROUND);
                widget.getHeader().setBackground(gp);
            }
        }

        if (state.isSelected()) {
            widget.setBorder(WIDGET_SELECT_BORDER);
        } else if (state.isHovered()) {
            widget.setBorder(WIDGET_HOVER_BORDER);
        } else if (state.isFocused()) {
            widget.setBorder(WIDGET_HOVER_BORDER);
        } else {
            widget.setBorder(WIDGET_BORDER);
        }
    }

    @Override
    public void installUI(IPEdgeWidget widget) {
//        widget.setSourceAnchorShape(AnchorShape.NONE);
//        widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        widget.setPaintControlPoints(true);
    }

    @Override
    public void updateUI(IPEdgeWidget widget, ObjectState previousState, ObjectState state) {
        if (state.isSelected()) {
            widget.setForeground(WIDGET_SELECT_BORDER_COLOR);
        } else if (state.isHighlighted()) {
            widget.setForeground(COLOR_HIGHLIGHTED);
        } else if (state.isHovered() || state.isFocused()) {
            widget.setForeground(WIDGET_HOVER_BORDER_COLOR);
        } else {
            widget.setForeground(WIDGET_BORDER_COLOR);
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
        widget.setBorder(BORDER_PIN);
        widget.setBackground(PIN_WIDGET_HOVER_BACKGROUND);
        widget.getPinNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():WIDGET_HOVER_BACKGROUND);
        Font font = widget.getPinNameWidget().getFont()!=null?widget.getPinNameWidget().getFont():widget.getScene().getDefaultFont();
        widget.getPinNameWidget().setFont(font.deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
    
    }

    @Override
    public void updateUI(IPinWidget widget, ObjectState previousState, ObjectState state) {
        widget.setOpaque(state.isHovered() || state.isFocused());

        Rectangle bound = widget.getBounds();
        if (bound != null) {
            if (state.isFocused() || state.isSelected()) {
                GradientPaint gp = new GradientPaint(bound.x, bound.y, PIN_WIDGET_HOVER_BACKGROUND, bound.x, bound.height, PIN_WIDGET_SELECT_BACKGROUND);
                widget.setBackground(gp);
            } else {
                GradientPaint gp = new GradientPaint(bound.x, bound.y, PIN_WIDGET_SELECT_BACKGROUND, bound.x, bound.height, PIN_WIDGET_HOVER_BACKGROUND);
                widget.setBackground(gp);
            }
        }

        if (widget.getTextDesign().getColor() == null) {
            if (state.isHovered() || state.isSelected()) {
                widget.getPinNameWidget().setForeground(Color.WHITE);
//            widget.getPinNameWidget().setFont(widget.getScene().getDefaultFont().deriveFont(Font.BOLD));
            } else {
                widget.getPinNameWidget().setForeground(PIN_WIDGET_TEXT);
//            widget.getPinNameWidget().setFont(widget.getScene().getDefaultFont().deriveFont(Font.PLAIN));
            }
        } else {
            widget.getPinNameWidget().setForeground(widget.getTextDesign().getColor());
        }
        
        if (state.isSelected()) {
            widget.setBorder(PIN_WIDGET_SELECT_BORDER);
        } else {
            widget.setBorder(BORDER_PIN);
        }
    }

    @Override
    public boolean isNodeMinimizeButtonOnRight(IPNodeWidget widget) {
        return true;
    }

//    @Override
//    public Image getMinimizeWidgetImage(IPNodeWidget widget) {
//        return widget.isMinimized()
//                ? ImageUtilities.loadImage("org/netbeans/modeler/widget/resource/cf_plus.gif") // NOI18N
//                : ImageUtilities.loadImage("org/netbeans/modeler/widget/resource/cf_minus.gif"); // NOI18N
//    }
    @Override
    public Image getMinimizeWidgetImage(IPNodeWidget widget) {
        return widget.isMinimized()
                ? ImageUtilities.loadImage("org/netbeans/modeler/widget/node/vmd/internal/resource/ex1/expand.png") // NOI18N
                : ImageUtilities.loadImage("org/netbeans/modeler/widget/node/vmd/internal/resource/ex1/collapse.png"); // NOI18N
    }

    @Override
    public void installUI(IPinSeperatorWidget label) {
        label.setOpaque(true);
        label.setBackground(PIN_SEPERATOR_WIDGET_BACKGROUND);
        label.setForeground(PIN_SEPERATOR_WIDGET_FOREGROUND);
//        if (changeFont) {
        Font fontPinCategory = label.getScene().getDefaultFont().deriveFont(9.5f);
        label.setFont(fontPinCategory);
//        }
        label.setAlignment(LabelWidget.Alignment.CENTER);
        label.setCheckClipping(true);
    }

    @Override
    public void installUI(IModelerScene widget) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
