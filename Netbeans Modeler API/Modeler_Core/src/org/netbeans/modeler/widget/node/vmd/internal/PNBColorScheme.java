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
import org.netbeans.api.visual.anchor.PointShape;
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
        widget.getNodeNameWidget().setForeground(Color.BLACK);
        widget.getNodeNameWidget().setFont(widget.getScene().getDefaultFont().deriveFont(Font.BOLD, 12));

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
        } else if (state.isHovered()) {
            widget.setBorder(BORDER60_HOVER);
        } else if (state.isFocused()) {
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
        widget.getPinNameWidget().setForeground(Color.BLACK);
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
