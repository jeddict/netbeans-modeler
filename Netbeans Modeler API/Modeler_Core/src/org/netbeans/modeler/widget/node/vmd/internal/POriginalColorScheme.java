/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
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
import org.netbeans.api.visual.widget.Scene;
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
    public IPinSeperatorWidget createPinCategoryWidget(IPNodeWidget widget, String categoryDisplayName) {
        Scene scene = widget.getScene();
        IPinSeperatorWidget label = new PinSeperatorWidget(scene, categoryDisplayName);
        installUI(label);
        return label;
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
