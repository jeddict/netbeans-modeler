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
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.anchor.PointShapeFactory;
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

/**
 * This class specifies look and feel of vmd widgets. There are predefined
 * schemes in PFactory class.
 *
 * @author David Kaspar
 */
public class POriginalColorScheme implements IColorScheme {

    private final Color COLOR_NORMAL;
    private final Color COLOR_HOVERED;
    private final Color COLOR_SELECTED;
    private final Color COLOR_HIGHLIGHTED;

    private final Color COLOR1;
    private final Color COLOR2;
    private final Color COLOR3;
    private final Color COLOR4;
    private final Color COLOR5;

    private final Border BORDER_NODE;

    private final Color BORDER_CATEGORY_BACKGROUND;
    private final Border BORDER_MINIMIZE;
    private final Border BORDER_PIN;
    private final Border BORDER_PIN_HOVERED;

    private final PointShape POINT_SHAPE_IMAGE;

    private static POriginalColorScheme instance;

    public static POriginalColorScheme getInstance() {
        if (instance == null) {
            synchronized (POriginalColorScheme.class) {
                if (instance == null) {
                    instance = new POriginalColorScheme();
                }
            }
        }
        return instance;
    }

    private POriginalColorScheme() {
        COLOR_NORMAL = new Color(0xBACDF0);
        COLOR_HOVERED = Color.BLACK;
        COLOR_SELECTED = new Color(0x748CC0);
        COLOR_HIGHLIGHTED = new Color(0x316AC5);

        COLOR1 = new Color(221, 235, 246);
        COLOR2 = new Color(255, 255, 255);
        COLOR3 = new Color(214, 235, 255);
        COLOR4 = new Color(241, 249, 253);
        COLOR5 = new Color(255, 255, 255);

        BORDER_NODE = PFactory.createPNodeBorder(COLOR_NORMAL, 1, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5);

        BORDER_CATEGORY_BACKGROUND = new Color(0xCDDDF8);
        BORDER_MINIMIZE = BorderFactory.createRoundedBorder(2, 2, null, COLOR_NORMAL);
        BORDER_PIN = BorderFactory.createOpaqueBorder(2, 8, 2, 8);
        BORDER_PIN_HOVERED = BorderFactory.createLineBorder(2, 8, 2, 8, Color.BLACK);

        POINT_SHAPE_IMAGE = PointShapeFactory.createImagePointShape(ImageUtilities.loadImage("org/netbeans/modules/visual/resources/vmd-pin.png")); // NOI18N

    }

    @Override
    public void installUI(IPNodeWidget widget) {
        widget.setBorder(BORDER_NODE);
        widget.setOpaque(false);

        Widget header = widget.getHeader();
        header.setBorder(BORDER_PIN);
        header.setBackground(COLOR_SELECTED);
        header.setOpaque(false);

        Widget minimize = widget.getMinimizeButton();
        minimize.setBorder(BORDER_MINIMIZE);

        Widget pinsSeparator = widget.getPinsSeparator();
        pinsSeparator.setForeground(BORDER_CATEGORY_BACKGROUND);
        widget.getMinimizeButton().setImage(this.getMinimizeWidgetImage(widget));
    }

    @Override
    public void updateUI(IPNodeWidget widget, ObjectState previousState, ObjectState state) {
        if (!previousState.isSelected() && state.isSelected()) {
            widget.bringToFront();
        } else if (!previousState.isHovered() && state.isHovered()) {
            widget.bringToFront();
        }

        Widget header = widget.getHeader();
        header.setOpaque(state.isSelected());
        header.setBorder(state.isFocused() || state.isHovered() ? BORDER_PIN_HOVERED : BORDER_PIN);
    }

    @Override
    public boolean isNodeMinimizeButtonOnRight(IPNodeWidget widget) {
        return false;
    }

    @Override
    public Image getMinimizeWidgetImage(IPNodeWidget widget) {
        return widget.isMinimized()
                ? ImageUtilities.loadImage("org/netbeans/modules/visual/resources/vmd-expand.png") // NOI18N
                : ImageUtilities.loadImage("org/netbeans/modules/visual/resources/vmd-collapse.png"); // NOI18N
    }

    @Override
    public void installUI(IPEdgeWidget widget) {
        widget.setSourceAnchorShape(AnchorShape.NONE);
        widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        widget.setPaintControlPoints(true);
    }

    @Override
    public void updateUI(IPEdgeWidget widget, ObjectState previousState, ObjectState state) {
        if (state.isHovered()) {
            widget.setForeground(COLOR_HOVERED);
        } else if (state.isSelected()) {
            widget.setForeground(COLOR_SELECTED);
        } else if (state.isHighlighted()) {
            widget.setForeground(COLOR_HIGHLIGHTED);
        } else if (state.isFocused()) {
            widget.setForeground(COLOR_HOVERED);
        } else {
            widget.setForeground(COLOR_NORMAL);
        }

        if (state.isSelected()) {
            widget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
            widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        } else {
            widget.setControlPointShape(PointShape.NONE);
            widget.setEndPointShape(POINT_SHAPE_IMAGE);
        }
    }

    @Override
    public void installUI(IPinWidget widget) {
        widget.setBorder(BORDER_PIN);
        widget.setBackground(COLOR_SELECTED);
        widget.setOpaque(false);

    }

    @Override
    public void updateUI(IPinWidget widget, ObjectState previousState, ObjectState state) {
        widget.setOpaque(state.isSelected());
        widget.setBorder(state.isFocused() || state.isHovered() ? BORDER_PIN_HOVERED : BORDER_PIN);
//        LookFeel lookFeel = getScene ().getLookFeel ();
//        setBorder (BorderFactory.createCompositeBorder (BorderFactory.createEmptyBorder (8, 2), lookFeel.getMiniBorder (state)));
//        setForeground (lookFeel.getForeground (state));
    }

    public int getNodeAnchorGap(Anchor anchor) {
        return 8;
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
