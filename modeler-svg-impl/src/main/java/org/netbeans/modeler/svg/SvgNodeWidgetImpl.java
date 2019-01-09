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
package org.netbeans.modeler.svg;

import java.util.Iterator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.modeler.anchors.CustomCircularAnchor;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
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
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.w3c.dom.Element;

public class SvgNodeWidgetImpl extends SvgNodeWidget {

    private IModelerScene scene;
    private final INodeWidget nodeWidget;
        
    private SVGResizeHandler resizeHandler;
    private GraphicsNode initialGraphicsNode;
    private GraphicsNode graphicsNode;
    private BridgeContext ctx;
    private Element outlineElement;
    private Shape outlineShape;
    private final Element rootElement;
    private final int width;
    private final int height;
    private ResizeType resizeType = ResizeType.ALL;
    private AffineTransform transform;
    private final Dimension preDimension;
    private Dimension dimension;
    private double scaleX = 1, scaleY = 1;
    private SVGDocument svgDocument;
    private boolean locked = false; // on resize start time[true] & update GraphicsNode , on resize finish time[false];


    public SvgNodeWidgetImpl(IModelerScene scene, INodeWidget nodeWidget, SVGDocument doc, Dimension dimension) {
        super((Scene) scene);

        rootElement = doc.getRootElement();
        width = new Integer(rootElement.getAttribute("width"));
        height = new Integer(rootElement.getAttribute("height"));

        this.preDimension = new Dimension(width, height);
        this.dimension = dimension;
        scaleX = dimension.getWidth() / preDimension.getWidth();
        scaleY = dimension.getHeight() / preDimension.getHeight();
        setSVGDocument(doc);
        this.scene = scene;
        this.nodeWidget = nodeWidget;
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
    @Override
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
    @Override
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

    /**
     * @return the scene
     */
    public IModelerScene getModelerScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setModelerScene(IModelerScene scene) {
        this.scene = scene;
    }

    public void updateWidget(double width, double height, double dimX, double dimY) {
        NodeWidgetInfo nodeWidgetInfo = nodeWidget.getNodeWidgetInfo();
        SvgNodeWidgetImpl imageWidget = this;

        nodeWidgetInfo.setDimension(new Dimension((int) width, (int) height));
        imageWidget.setDimension(new Dimension((int) width, (int) height));

        // TODO
        if ("EVENT".equals(nodeWidgetInfo.getModelerDocument().getDocumentModel())) {//Major Bug : Based on BPN event dependecy for circle shape :  getDocumentModel() == DocumentModelType.EVENT
            Iterator<? extends IFlowEdgeWidget> itr = ((IFlowNodeWidget) nodeWidget).getIncommingFlowEdgeWidget().iterator();
            while (itr.hasNext()) {
                EdgeWidget sequenceFlowWidget = (EdgeWidget) itr.next();
                Anchor targetAnchor;
                targetAnchor = new CustomCircularAnchor(imageWidget.getParentNodeWidget());//,, (int) width / 2
                sequenceFlowWidget.setTargetAnchor(targetAnchor);
            }
            itr = ((IFlowNodeWidget) nodeWidget).getOutgoingFlowEdgeWidget().iterator();
            while (itr.hasNext()) {
                EdgeWidget sequenceFlowWidget = (EdgeWidget) itr.next();
                Anchor targetAnchor;
                targetAnchor = new CustomCircularAnchor(imageWidget.getParentNodeWidget());//, (int) width / 2
                sequenceFlowWidget.setSourceAnchor(targetAnchor);
            }
        }
    }

    /**
     * @return the nodeWidget
     */
    public INodeWidget getParentNodeWidget() {
        return nodeWidget;
    }
}
