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
package org.netbeans.modeler.label.multiline;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.modeler.label.Customizable;
import org.netbeans.modeler.label.ResourceType;
import org.netbeans.modeler.label.ResourceValue;
import org.netbeans.modeler.widget.node.NodeWidget;

/**
 *
 *
 */
public class ModelerMultilineLabelWidget extends MultilineLabelWidget
        implements Customizable {

    private NodeWidget nodeWidget;
    private String propId;
    private String propDisplayName;
    private ResourceType[] customizableResTypes = new ResourceType[]{
        ResourceType.FONT,
        ResourceType.FOREGROUND,
        ResourceType.BACKGROUND};

    public ModelerMultilineLabelWidget(Scene scene,
            String propertyID, String propDisplayName) {
        super(scene);
        init(propertyID, propDisplayName);
    }

    public ModelerMultilineLabelWidget(Scene scene, String label,
            String propertyID, String propDisplayName) {
        super(scene, label);
        init(propertyID, propDisplayName);
    }

    private void init(String propertyID, String displayName) {
        setForeground(null);
        setBackground(null);
        propId = propertyID;
        ResourceValue.initResources(propertyID, this);
        propDisplayName = displayName;
    }

//    public void save(EdgeWriter edgeWriter) {
//        edgeWriter.setPEID(PersistenceUtil.getPEID(this));
//        edgeWriter.setVisible(this.isVisible());
//        edgeWriter.setLocation(this.getLocation());
//        edgeWriter.setSize(this.getBounds().getSize());
//        edgeWriter.setPresentation("");
//
//        edgeWriter.beginGraphNode();
//        edgeWriter.endGraphNode();
//    }
//
//    public void load(EdgeInfo edgeReader) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    public void remove() {
        super.removeFromParent();
    }

    public void refresh(boolean resizetocontent) {
    }

    @Override
    public String getDisplayName() {
        return propDisplayName;
    }

    @Override
    public String getID() {
        return propId;
    }

    @Override
    public void update() {
        ResourceValue.initResources(propId, this);
    }

    @Override
    public ResourceType[] getCustomizableResourceTypes() {
        return customizableResTypes;
    }

    @Override
    public void setCustomizableResourceTypes(ResourceType[] resTypes) {
        customizableResTypes = resTypes;
    }

    @Override
    protected Rectangle calculateClientArea() {
        if (getLabel() == null) {
            return super.calculateClientArea();
        }

        Rectangle rectangle;
//        if (isUseGlyphVector() == true)
//        {
//            assureGlyphVector();
//            rectangle = GeomUtil.roundRectangle(cacheGlyphVector.getVisualBounds());
//            rectangle.grow(1, 1); // WORKAROUND - even text antialiasing is included into the boundary
//        }
//        else
        {
            Graphics2D gr = getGraphics();
            FontMetrics fontMetrics = gr.getFontMetrics(getFont());
            Dimension parentSize = getParentPrefSize();

//            Rectangle2D union = new Rectangle2D.Double();
            double x = 0;
            double y = 0;
            double width = 0;
            double height = 0;
//"retertrehffgdg d xcxb \n xcbv xv x \\n xcvs xc".split("\n")
            String[] lines = getLabel().split("\n");
            for (int index = 0; index < lines.length; index++) {
                String line = lines[index];
                Rectangle2D stringBounds = fontMetrics.getStringBounds(line, gr);

                if (index == 0) {
                    x = stringBounds.getX();
                    y = stringBounds.getY();
                    width = stringBounds.getWidth();
                    if (parentSize != null && (parentSize.width < width)) {
                        width = parentSize.width;
                    }
                } else {
                    if (stringBounds.getX() < x) {
                        x = stringBounds.getX();
                    }

                    if (stringBounds.getY() < y) {
                        y = stringBounds.getY();
                    }

                    if (stringBounds.getWidth() > width) {
                        width = stringBounds.getWidth();
                    }
                    if (parentSize != null && (parentSize.width < width)) {
                        width = parentSize.width;
                    }
                }

                height += stringBounds.getHeight();
                if (parentSize != null && (parentSize.height < height)) {
                    height = parentSize.height;
                }

            }
            rectangle = roundRectangle(new Rectangle2D.Double(x, y, width, height));

        }

        switch (getOrientation()) {
            case NORMAL:
                return rectangle;
            case ROTATE_90:
                return new Rectangle(rectangle.y, -rectangle.x - rectangle.width, rectangle.height, rectangle.width);
            default:
                throw new IllegalStateException();
        }
    }

    private Dimension getParentPrefSize() {
        NodeWidget widget = getParent();
        if (widget != null && widget.getPreferredSize() != null) {
            return widget.getPreferredSize();
        }
        return null;
    }

    /**
     * @return the nodeWidget
     */
    public NodeWidget getParent() {
        return nodeWidget;
    }

    /**
     * @param nodeWidget the nodeWidget to set
     */
    public void setParent(NodeWidget nodeWidget) {
        this.nodeWidget = nodeWidget;
    }
}
