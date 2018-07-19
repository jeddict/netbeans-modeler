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
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.IPEdgeWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.pin.IPinSeperatorWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.openide.util.ImageUtilities;

public class PNBColorScheme implements IColorScheme {

    private final Color COLOR_NORMAL;
    private final Color COLOR_HIGHLIGHTED;
    public final Color COLOR60_SELECT;
    public final Color COLOR60_HOVER;
    public final Color COLOR60_HOVER_BACKGROUND;

    private final Border BORDER60;
    private final Border BORDER60_SELECT;
    private final Border BORDER60_HOVER;

    private final Border BORDER60_PIN_SELECT;

    private final Color BORDER_CATEGORY_BACKGROUND;
    private final Border BORDER_PIN;
    private final Color COLOR1;
    private final Color COLOR2;
    private final Color COLOR3;
    private final Color COLOR4;
    private final Color COLOR5;
    private static PNBColorScheme instance;

    public static PNBColorScheme getInstance() {
        if (instance == null) {
            synchronized (PNBColorScheme.class) {
                if (instance == null) {
                    instance = new PNBColorScheme();
                }
            }
        }
        return instance;
    }

    private PNBColorScheme() {
        COLOR_NORMAL = new Color(0xBACDF0);//new Color(160, 180, 210);
        COLOR_HIGHLIGHTED = new Color(0x5B67B0);
        COLOR60_SELECT = new Color(0xFF8500);
        COLOR60_HOVER = new Color(0x5B67B0);
        COLOR60_HOVER_BACKGROUND = new Color(0xB0C3E1);

        COLOR1 = new Color(221, 235, 246);
        COLOR2 = new Color(255, 255, 255);
        COLOR3 = new Color(214, 235, 255);
        COLOR4 = new Color(241, 249, 253);
        COLOR5 = new Color(255, 255, 255);

        BORDER60 = PFactory.createPNodeBorder(COLOR_NORMAL, 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        BORDER60_SELECT = PFactory.createPNodeBorder(COLOR60_SELECT, 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);
        BORDER60_HOVER = PFactory.createPNodeBorder(COLOR60_HOVER, 2, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);

        BORDER60_PIN_SELECT = BorderFactory.createCompositeBorder(BorderFactory.createLineBorder(0, 1, 0, 1, COLOR60_SELECT), BorderFactory.createLineBorder(2, 7, 2, 7, COLOR60_SELECT));

        BORDER_CATEGORY_BACKGROUND = new Color(0xCDDDF8);
        BORDER_PIN = BorderFactory.createOpaqueBorder(2, 8, 2, 8);

    }

    @Override
    public void installUI(IPNodeWidget widget) {
        widget.setBorder(BORDER60);

        Widget header = widget.getHeader();
        header.setBackground(COLOR60_HOVER_BACKGROUND);
        header.setBorder(BORDER_PIN);
        widget.getNodeNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():Color.BLACK);
        Font font = widget.getNodeNameWidget().getFont()!=null?widget.getNodeNameWidget().getFont():widget.getScene().getDefaultFont();
        widget.getNodeNameWidget().setFont(font.deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
        
        Widget pinsSeparator = widget.getPinsSeparator();
        pinsSeparator.setForeground(BORDER_CATEGORY_BACKGROUND);

        widget.getMinimizeButton().setImage(this.getMinimizeWidgetImage(widget));
    }

    @Override
    public void updateUI(IPNodeWidget widget, ObjectState previousState, ObjectState state) {
        if (!previousState.isSelected() && state.isSelected()) {
            widget.bringToFront();
        }

        boolean hover = state.isHovered() || state.isFocused();
        widget.getHeader().setOpaque(hover);

        if (state.isSelected()) {
            widget.setBorder(BORDER60_SELECT);
        } else if (state.isHovered() || state.isFocused()) {
            widget.setBorder(BORDER60_HOVER);
        } else {
            widget.setBorder(BORDER60);
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
            widget.setForeground(COLOR60_SELECT);
        } else if (state.isHighlighted()) {
            widget.setForeground(COLOR_HIGHLIGHTED);
        } else if (state.isHovered() || state.isFocused()) {
            widget.setForeground(COLOR60_HOVER);
        } else {
            widget.setForeground(COLOR_NORMAL);
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
        widget.setBackground(COLOR60_HOVER_BACKGROUND);
        widget.getPinNameWidget().setForeground(widget.getTextDesign().getColor()!=null?widget.getTextDesign().getColor():Color.BLACK);
        Font font = widget.getPinNameWidget().getFont()!=null?widget.getPinNameWidget().getFont():widget.getScene().getDefaultFont();
        widget.getPinNameWidget().setFont(font.deriveFont(widget.getTextDesign().getStyle(), widget.getTextDesign().getSize()));
    
    }

    @Override
    public void updateUI(IPinWidget widget, ObjectState previousState, ObjectState state) {
        widget.setOpaque(state.isHovered() || state.isFocused());
        if (state.isSelected()) {
            widget.setBorder(BORDER60_PIN_SELECT);
        } else {
            widget.setBorder(BORDER_PIN);
        }
    }

    @Override
    public boolean isNodeMinimizeButtonOnRight(IPNodeWidget widget) {
        return true;
    }

//    @Override
//    public Image getMinimizeWidgetImage(AbstractPNodeWidget widget) {
//        return widget.isMinimized()
//                ? ImageUtilities.loadImage("org/netbeans/modeler/widget/resource/cf_plus.gif") // NOI18N
//                : ImageUtilities.loadImage("org/netbeans/modeler/widget/resource/cf_minus.gif"); // NOI18N
//    }
    @Override
    public Image getMinimizeWidgetImage(IPNodeWidget widget) {
        return widget.isMinimized()
                ? ImageUtilities.loadImage("org/netbeans/modules/visual/resources/vmd-expand-60.png") // NOI18N
                : ImageUtilities.loadImage("org/netbeans/modules/visual/resources/vmd-collapse-60.png"); // NOI18N
    }

    @Override
    public void installUI(IPinSeperatorWidget label) {
        label.setOpaque(true);
        label.setBackground(BORDER_CATEGORY_BACKGROUND);
        label.setForeground(Color.GRAY);
        Font fontPinCategory = label.getScene().getDefaultFont().deriveFont(10.0f);
        label.setFont(fontPinCategory);
        label.setAlignment(LabelWidget.Alignment.CENTER);
        label.setCheckClipping(true);
    }

    @Override
    public void installUI(IModelerScene scene) {
        scene.setBackground(Color.WHITE);
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
