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
package org.netbeans.modeler.widget.node.image.svg;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class SvgWidget extends Widget {

    private SVGResizeHandler resizeHandler;
    private GraphicsNode initialGraphicsNode;
    private GraphicsNode graphicsNode;
    BridgeContext ctx;
    Element outlineElement;
    private Shape outlineShape;
    Element rootElement;
    int width, height;
    private ResizeType resizeType = ResizeType.ALL;
    private AffineTransform transform;
    private Dimension preDimension;
    private Dimension dimension;
    private double scaleX = 1, scaleY = 1;
    private SVGDocument svgDocument;
    private boolean locked = false; // on resize start time[true] & update GraphicsNode , on resize finish time[false];

//    public SvgWidget(Scene scene, File file) throws IOException {
//        super(scene);
//        setSvgUri(file.toURL().toString());
//    }
    public SvgWidget(IModelerScene scene, SVGDocument doc, Dimension dimension) {
        super((Scene) scene);

        rootElement = doc.getRootElement();
        width = new Integer(rootElement.getAttribute("width"));
        height = new Integer(rootElement.getAttribute("height"));

        this.preDimension = new Dimension(width, height);
        this.dimension = dimension;
        scaleX = dimension.getWidth() / preDimension.getWidth();
        scaleY = dimension.getHeight() / preDimension.getHeight();
        setSVGDocument(doc);

//        setInitialGraphicsNode(graphicsNode);
    }

    public SvgWidget(IModelerScene scene) {
        super((Scene) scene);
    }

    public void setSVGDocument(SVGDocument doc) {
        svgDocument = doc;
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        GVTBuilder builder = new GVTBuilder();
        setGraphicsNode(builder.build(ctx, getSvgDocument()));

        outlineElement = doc.getElementById("OUTLINE");
        if (outlineElement != null) {
            outlineShape = ((org.apache.batik.gvt.ShapeNode) ctx.getGraphicsNode(outlineElement)).getShape();
        }

        if ("OUTER".equals(doc.getRootElement().getAttribute("resizeType"))) {
            setResizeType(ResizeType.OUTER);
        }

        revalidate();
    }

    @Override
    protected Rectangle calculateClientArea() {
        return getSVGClientArea();
    }

    public Rectangle getSVGClientArea() {
        Rectangle rec = getGraphicsNode().getBounds().getBounds();
        rec.setSize(dimension);
        return rec;
    }

    @Override
    protected void paintWidget() {
        if (locked) {
            Dimension borderInnerDimension = ((NodeWidget) this.getParentWidget()).getWidgetInnerDimension();//fail > 1
            this.setDimension(borderInnerDimension);
        }
        transform = new AffineTransform();
        transform.setToScale(getScaleX(), getScaleY());
        if (getResizeType() == ResizeType.ALL) {
            getGraphicsNode().setTransform(getTransform());
        }
        if (null != getGraphicsNode()) {
            Graphics2D g = getGraphics();
            getGraphicsNode().paint(g);
        }
    }

    /**
     * @return the dimension
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * @param dimension the dimension to set
     */
    public void setDimension(Dimension dimension) {
//tmpDimension=this.dimension;
        this.dimension = dimension;
        setScaleX(dimension.getWidth() / preDimension.getWidth());
        setScaleY(dimension.getHeight() / preDimension.getHeight());

        if (this.getResizeHandler() != null && getResizeType() == ResizeType.OUTER) {
            this.getResizeHandler().resizing(svgDocument, getResizeType(), Integer.valueOf(width).doubleValue(), Integer.valueOf(height).doubleValue(), getScaleX(), getScaleY());
            if (outlineElement != null) {
                outlineShape = ((org.apache.batik.gvt.ShapeNode) ctx.getGraphicsNode(outlineElement)).getShape();
            }
        }
//        paintWidget();
    }

    public void resizingFinished() {
        locked = false;
        if (this.getResizeHandler() != null && getResizeType() == ResizeType.OUTER) {
            this.getResizeHandler().resizingFinished(svgDocument, getResizeType(), Integer.valueOf(width).doubleValue(), Integer.valueOf(height).doubleValue(), getScaleX(), getScaleY());
        }
    }

    public void resizingStarted() {
        locked = true;
        if (this.getResizeHandler() != null && getResizeType() == ResizeType.OUTER) {
            this.getResizeHandler().resizingStarted(svgDocument, getResizeType(), Integer.valueOf(width).doubleValue(), Integer.valueOf(height).doubleValue(), getScaleX(), getScaleY());
        }
    }

    /**
     * @return the svgDocument
     */
    public SVGDocument getSvgDocument() {
        return svgDocument;
    }

    /**
     * @return the outlineShape
     */
    public Shape getOutlineShape() {
        return outlineShape;
    }

    /**
     * @return the transform
     */
    public AffineTransform getTransform() {
        return transform;
    }

    /**
     * @return the resizeHandler
     */
    public SVGResizeHandler getResizeHandler() {
        return resizeHandler;
    }

    /**
     * @param resizeHandler the resizeHandler to set
     */
    public void setResizeHandler(SVGResizeHandler resizeHandler) {
        this.resizeHandler = resizeHandler;
    }

    /**
     * @return the resizeType
     */
    public ResizeType getResizeType() {
        return resizeType;
    }

    /**
     * @param resizeType the resizeType to set
     */
    public void setResizeType(ResizeType resizeType) {
        this.resizeType = resizeType;
    }

    /**
     * @return the scaleX
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * @param scaleX the scaleX to set
     */
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * @return the scaleY
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * @param scaleY the scaleY to set
     */
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    /**
     * @return the graphicsNode
     */
    public GraphicsNode getGraphicsNode() {
        return graphicsNode;
    }

    /**
     * @param graphicsNode the graphicsNode to set
     */
    public void setGraphicsNode(GraphicsNode graphicsNode) {
        this.graphicsNode = graphicsNode;
    }

    /**
     * @return the initialGraphicsNode
     */
    public GraphicsNode getInitialGraphicsNode() {
        return initialGraphicsNode;
    }

    /**
     * @param initialGraphicsNode the initialGraphicsNode to set
     */
    public void setInitialGraphicsNode(GraphicsNode initialGraphicsNode) {
        this.initialGraphicsNode = initialGraphicsNode;
    }
}
