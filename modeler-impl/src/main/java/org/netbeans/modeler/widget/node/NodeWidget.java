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
package org.netbeans.modeler.widget.node;

//import org.netbeans.modeler.widget.INodeWidget;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.border.ResizeBorder;
import org.netbeans.modeler.border.RoundResizeBorder;
import org.netbeans.modeler.config.document.BoundsConstraint;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.core.exception.SVGAttributeNotFoundException;
import org.netbeans.modeler.core.scene.ModelerScene;
import org.netbeans.modeler.label.BasicLabelManager;
import org.netbeans.modeler.label.LabelManager;
import org.netbeans.modeler.locale.I18n;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.properties.view.manager.IPropertyManager;
import org.netbeans.modeler.properties.view.manager.VisualPropertyViewManager;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.specification.model.document.widget.IModelerSubScene;
import org.netbeans.modeler.svg.SVGDocument;
import org.netbeans.modeler.svg.SvgNodeWidget;
import org.netbeans.modeler.svg.SvgNodeWidgetFactory;
import org.netbeans.modeler.widget.context.action.SceneConnectProvider;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.properties.generic.ElementPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.NodeOperation;
import org.openide.util.Lookup;

public abstract class NodeWidget<S extends IModelerScene> extends AbstractNodeWidget implements INNodeWidget {

    public static final int WIDGET_BORDER_PADDING = 4;
    private static final ResizeProvider.ControlPoint[] CIRCLE_RESIZE_BORDER_DISABLE_POINT = new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.TOP_CENTER, ResizeProvider.ControlPoint.CENTER_LEFT};
    private static final ResizeProvider.ControlPoint[] CIRCLE_RESIZE_BORDER_ENABLE_POINT = new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.BOTTOM_CENTER, ResizeProvider.ControlPoint.CENTER_RIGHT};
    public static final ResizeBorder CIRCLE_RESIZE_BORDER = new RoundResizeBorder(WIDGET_BORDER_PADDING, new Color(242, 132, 0), CIRCLE_RESIZE_BORDER_ENABLE_POINT, CIRCLE_RESIZE_BORDER_DISABLE_POINT, false, 10000, 10000, false, new Color(242, 132, 0));
    private static final ResizeProvider.ControlPoint[] RECTANGLE_RESIZE_BORDER_DISABLE_POINT = new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.BOTTOM_LEFT, ResizeProvider.ControlPoint.TOP_LEFT, ResizeProvider.ControlPoint.TOP_RIGHT, ResizeProvider.ControlPoint.TOP_CENTER, ResizeProvider.ControlPoint.CENTER_LEFT};
    private static final ResizeProvider.ControlPoint[] RECTANGLE_RESIZE_BORDER_ENABLE_POINT = new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.BOTTOM_CENTER, ResizeProvider.ControlPoint.BOTTOM_RIGHT, ResizeProvider.ControlPoint.CENTER_RIGHT};
    public static final ResizeBorder RECTANGLE_RESIZE_BORDER = new RoundResizeBorder(WIDGET_BORDER_PADDING, new Color(242, 132, 0), RECTANGLE_RESIZE_BORDER_ENABLE_POINT, RECTANGLE_RESIZE_BORDER_DISABLE_POINT, false, 0, 0, true, new Color(242, 132, 0));
    private ResizeBorder widgetBorder;

    private NodeWidgetStatus status;
    private final NodeWidgetInfo nodeWidgetInfo;
    private boolean activeStatus = true;
    private boolean anchorState = false;
    private static final Float HOVER_BORDER_WIDTH = 0.2F;
    private final Map<String, PropertyChangeListener> propertyChangeHandlers = new HashMap<>();
    private LabelManager labelManager;

    public NodeWidget(S scene, NodeWidgetInfo nodeWidgetInfo) {
        super(scene, nodeWidgetInfo.getNodeDesign());
        this.setModelerScene(scene);

        this.nodeWidgetInfo = nodeWidgetInfo;

        IModelerDocument modelerDocument = nodeWidgetInfo.getModelerDocument();
        Dimension dimension = new Dimension((int) modelerDocument.getBounds().getWidth().getValue(), (int) modelerDocument.getBounds().getHeight().getValue());
        nodeWidgetInfo.setDimension(dimension);

        SvgNodeWidgetFactory factory = Lookup.getDefault().lookup(SvgNodeWidgetFactory.class);
        SvgNodeWidget svgNodeWidget = factory.create(
                scene,
                this,
                modelerDocument.generateDocument(),
                new Dimension(
                        (int) modelerDocument.getBounds().getWidth().getValue(),
                        (int) modelerDocument.getBounds().getHeight().getValue()
                )
        );
        this.setNodeImageWidget(svgNodeWidget);

        updateNodeWidgetDesign(new ShapeDesign(modelerDocument.getDocumentShapeDesign()));

        setRangeConstraint();

        setOpaque(false);
        setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 1)); // use vertical layout

        setChildConstraint(getNodeImageWidget(), 1);
        setCheckClipping(true);

        this.setWidgetBorder(getNodeBorder());

    }

    @Override
    public void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener) {
        this.propertyChangeHandlers.put(id, propertyChangeListener);
    }

    @Override
    public void removePropertyChangeListener(String id) {
        propertyChangeHandlers.remove(id);
    }

    @Override
    public Map<String, PropertyChangeListener> getPropertyChangeListeners() {
        return propertyChangeHandlers;
    }
    private final Map<String, PropertyVisibilityHandler> propertyVisibilityHandlers = new HashMap<String, PropertyVisibilityHandler>();

    @Override
    public void addPropertyVisibilityHandler(String id, PropertyVisibilityHandler propertyVisibilityHandler) {
        this.propertyVisibilityHandlers.put(id, propertyVisibilityHandler);
    }

    @Override
    public void removePropertyVisibilityHandler(String id) {
        propertyVisibilityHandlers.remove(id);
    }

    @Override
    public Map<String, PropertyVisibilityHandler> getPropertyVisibilityHandlers() {
        return propertyVisibilityHandlers;
    }

    public void setRangeConstraint() {
        IModelerDocument modelerDocument = nodeWidgetInfo.getModelerDocument();
        this.setMaximumSize(new Dimension((int) modelerDocument.getBounds().getWidth().getMax(), (int) modelerDocument.getBounds().getHeight().getMax()));
        this.setMinimumSize(new Dimension((int) modelerDocument.getBounds().getWidth().getMin(), (int) modelerDocument.getBounds().getHeight().getMin()));
    }

    public Dimension manageMinRangeConstraint(int x, int y) {
        BoundsConstraint bound = getNodeWidgetInfo().getModelerDocument().getBounds();
        x = x > (int) bound.getWidth().getMin() ? x : (int) bound.getWidth().getMin();
        y = y > (int) bound.getHeight().getMin() ? y : (int) bound.getHeight().getMin();
        return new Dimension(x, y);

    }

    public Dimension manageMaxRangeConstraint(int x, int y) {
        BoundsConstraint bound = getNodeWidgetInfo().getModelerDocument().getBounds();
        x = x < (int) bound.getWidth().getMax() ? x : (int) bound.getWidth().getMax();
        y = y < (int) bound.getHeight().getMax() ? y : (int) bound.getHeight().getMax();
        return new Dimension(x, y);
    }

    @Override
    public void setLabel(String label) {
        if (labelManager == null) {
            labelManager = new BasicLabelManager(this, label);
        } else {
            getLabelManager().setLabel(label);
        }
    }

    @Override
    public String getLabel() {
        if (labelManager == null) {
            labelManager = new BasicLabelManager(this, "");
        }
        return getLabelManager().getLabel();
    }

    @Override
    public void hideLabel() {
        if (getLabelManager() != null) {
            getLabelManager().hideLabel();
        }
    }

    @Override
    public void showLabel() {
        if (getLabelManager() != null) {
            getLabelManager().showLabel();
        }
    }

    public LabelManager getLabelManager() {
        return labelManager;
    }

    @Override
    public NodeWidgetInfo getNodeWidgetInfo() {
        return nodeWidgetInfo;
    }

//    @Override
//    protected Rectangle calculateClientArea () {
//        //System.out.println("calculateClientArea ....");
//        return super.calculateClientArea ();
//    }
//
//
//    @Override
//    protected void paintWidget () {
//          //System.out.println("paintWidget ....");
//        super.paintWidget();
//    }
    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState newState) {
        super.notifyStateChanged(previousState, newState);
        if (this.getNodeWidgetInfo() != null) {
            if (this instanceof IModelerSubScene) {
                this.bringToBack();
            }
            if (newState.isSelected()) {
                getModelerScene().manageLayerWidget();
                showResizeBorder();
            } else if (newState.isHovered()) {
                hoverWidget();
            } else {
                unhoverWidget();
                hideResizeBorder();
            }
        }

    }

    @Override
    public void showResizeBorder() {
        ResizeBorder border = getNodeBorder();
        if (border != null) {
            this.setBorder(border);
            setWidgetBorder(border);
        }
    }

    @Override
    public void hideResizeBorder() {
        this.setBorder(DEFAULT_BORDER);
    }

    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuItemList = new LinkedList<>();
        JMenuItem visualProperty = new JMenuItem(I18n.getString("Customize"));
        visualProperty.setIcon(ImageUtil.getInstance().getIcon("customize.png"));
        visualProperty.addActionListener(e -> {
            AbstractNode node1 = new VisualPropertyViewManager((IBaseElementWidget) NodeWidget.this);
            NodeOperation.getDefault().showProperties(node1);
            NodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });

        JMenuItem delete = new JMenuItem("Delete");
        delete.setIcon(ImageUtil.getInstance().getIcon("delete.png"));
        delete.addActionListener(e -> {
            NodeWidget.this.remove(true);
            NodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });

        JMenu position = new JMenu("Position");
        position.setIcon(ImageUtil.getInstance().getIcon("position.png"));

        JMenuItem itemFront = new JMenuItem("Bring to Front");
        itemFront.setIcon(ImageUtil.getInstance().getIcon("front.png"));
        itemFront.addActionListener(e -> {
            NodeWidget.this.bringToFront();
            NodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });
        position.add(itemFront);

        JMenuItem itemBack = new JMenuItem("Send to Back");
        itemBack.setIcon(ImageUtil.getInstance().getIcon("back.png"));
        itemBack.addActionListener(e -> {
            NodeWidget.this.bringToBack();
            NodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });
        position.add(itemBack);

        JMenuItem baseProperty = new JMenuItem("Properties");
        baseProperty.setIcon(ImageUtil.getInstance().getIcon("properties.gif"));
        baseProperty.addActionListener(e -> {
            NodeWidget.this.showProperties();
            NodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });

        menuItemList.add(visualProperty);
        menuItemList.add(null);
        menuItemList.add(position);
        menuItemList.add(delete);
        menuItemList.add(null);
        menuItemList.add(baseProperty);
        return menuItemList;
    }

    @Override
    public PopupMenuProvider getPopupMenuProvider() {
        final PopupMenuProvider popupMenuProvider;
        final JPopupMenu popupMenu; //PopupMenu used to give some funcionality to the widget

        popupMenu = new JPopupMenu();

        List<JMenuItem> menuItemList = getPopupMenuItemList();
        for (JMenuItem menuItem : menuItemList) {
            if (menuItem == null) {
                popupMenu.addSeparator();
            } else {
                popupMenu.add(menuItem);
            }
        }
        popupMenuProvider = (final Widget widget, final Point location1) -> popupMenu;

        return popupMenuProvider;
    }

    private BasePropertyViewManager node;

    @Override
    public void exploreProperties() {
        if (node == null) {
            node = new BasePropertyViewManager((IBaseElementWidget) this);
        }
        org.netbeans.modeler.properties.util.PropertyUtil.exploreProperties(node, this.getLabel(), propertyVisibilityHandlers);
    }
    
    public IPropertyManager getPropertyManager(){
        return node;
    }
    

    @Override
    public void refreshProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.refreshProperties(node, this.getLabel(), propertyVisibilityHandlers);
    }

    @Override
    public void showProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.showProperties(node, this.getLabel(), propertyVisibilityHandlers);
    }

    void hoverWidget() {
        hoverWidget(0);
    }

    public void hoverWidget(int padding) {
        increaseBorderWidth(HOVER_BORDER_WIDTH + padding);
    }

    void unhoverWidget() {
        unhoverWidget(0);
    }

    public void unhoverWidget(int padding) {
        decreaseBorderWidth(HOVER_BORDER_WIDTH + padding);
    }
    private Color outerElementStartBackgroundColor;
    private Float outerElementStartOffset;
    private Color outerElementEndBackgroundColor;
    private Float outerElementEndOffset;
    private Color outerElementBorderColor;
    private Float outerElementBorderWidth;
    private Color innerElementStartBackgroundColor;
    private Float innerElementStartOffset;
    private Color innerElementEndBackgroundColor;
    private Float innerElementEndOffset;
    private Color innerElementBorderColor;
    private Float innerElementBorderWidth;

    @Override
    public ElementPropertySet createVisualOuterPropertiesSet(ElementPropertySet elementPropertySet) throws NoSuchMethodException, NoSuchFieldException {
        elementPropertySet.put("OUTER_PROP", new ElementPropertySupport(this, Color.class, "outerElementStartBackgroundColor", "Start Background Color", "The Start Background Color of the Outer Element."));
        elementPropertySet.put("OUTER_PROP", new ElementPropertySupport(this, Color.class, "outerElementEndBackgroundColor", "End Background Color", "The End Background Color of the Outer Element."));
        elementPropertySet.put("OUTER_PROP", new ElementPropertySupport(this, Color.class, "outerElementBorderColor", "Outer Border Color", "The Border Color of the Outer Element."));
        elementPropertySet.put("OUTER_PROP", new ElementPropertySupport(this, Float.class, "outerElementBorderWidth", "Outer Border Width", "The Border Width of the Outer Element."));
        return elementPropertySet;
    }

    @Override
    public ElementPropertySet createVisualInnerPropertiesSet(ElementPropertySet elementPropertySet) throws NoSuchMethodException, NoSuchFieldException {
        elementPropertySet.put("INNER_PROP", new ElementPropertySupport(this, Color.class, "innerElementStartBackgroundColor", "Start Background Color", "The Start Background Color of the Inner Element."));
        elementPropertySet.put("INNER_PROP", new ElementPropertySupport(this, Color.class, "innerElementEndBackgroundColor", "End Background Color", "The End Background Color of the Inner Element."));
        elementPropertySet.put("INNER_PROP", new ElementPropertySupport(this, Color.class, "innerElementBorderColor", "Inner Border Color", "The Border Color of the Inner Element."));
        elementPropertySet.put("INNER_PROP", new ElementPropertySupport(this, Float.class, "innerElementBorderWidth", "Inner Border Width", "The Border Width of the Inner Element."));
        return elementPropertySet;
    }

    /*----------------------------Outer-------------------------------------*/
    public Color getOuterElementStartBackgroundColor() {
        return outerElementStartBackgroundColor;
    }

    public void setOuterElementStartBackgroundColor(Color outerElementStartBackgroundColor) {
        this.outerElementStartBackgroundColor = outerElementStartBackgroundColor;
        setElementValue("outer-start-grad", "style", "stop-color:" + getColorString(outerElementStartBackgroundColor) + ";stop-opacity:1;");
    }

    public Color getOuterElementEndBackgroundColor() {
        return outerElementEndBackgroundColor;
    }

    public void setOuterElementEndBackgroundColor(Color outerElementEndBackgroundColor) {
        this.outerElementEndBackgroundColor = outerElementEndBackgroundColor;
        setElementValue("outer-end-grad", "style", "stop-color:" + getColorString(outerElementEndBackgroundColor) + ";stop-opacity:1;");

    }

    /**
     * @return the outerElementStartOffset
     */
    public Float getOuterElementStartOffset() {
        return outerElementStartOffset;
    }

    /**
     * @param outerElementStartOffset the outerElementStartOffset to set
     */
    public void setOuterElementStartOffset(Float outerElementStartOffset) {
        this.outerElementStartOffset = outerElementStartOffset;
        setElementValue("outer-start-grad", "offset", outerElementStartOffset + "%");
    }

    /**
     * @return the outerElementEndOffset
     */
    public Float getOuterElementEndOffset() {
        return outerElementEndOffset;
    }

    /**
     * @param outerElementEndOffset the outerElementEndOffset to set
     */
    public void setOuterElementEndOffset(Float outerElementEndOffset) {
        this.outerElementEndOffset = outerElementEndOffset;
        setElementValue("outer-end-grad", "offset", outerElementEndOffset + "%");
    }

    /**
     * @return the outerElementBorderWidth
     */
    public Float getOuterElementBorderWidth() {
        return outerElementBorderWidth;
    }

    /**
     * @param outerElementBorderWidth the outerElementBorderWidth to set
     */
    public void setOuterElementBorderWidth(Float outerElementBorderWidth) {
        this.outerElementBorderWidth = outerElementBorderWidth;
        setElementValue("outer", "stroke-width", outerElementBorderWidth.toString());
    }

    /**
     * @return the outerElementBorderColor
     */
    public Color getOuterElementBorderColor() {
        return outerElementBorderColor;
    }

    /**
     * @param outerElementBorderColor the outerElementBorderColor to set
     */
    public void setOuterElementBorderColor(Color outerElementBorderColor) {
        this.outerElementBorderColor = outerElementBorderColor;
        setElementValue("outer", "stroke", getColorString(outerElementBorderColor));
    }

    void changeBorderColor(Color color) {
        //setElementValue("outer", "stroke", getColorString(color));//setOuterElementBorderColor is not used because orignal value changed
    }

    void defaultBorderColor() {
        //  setElementValue("outer", "stroke", getColorString(outerElementBorderColor));//setOuterElementBorderColor is not used because orignal value changed
    }

    public void increaseBorderWidth(float inc) {
        if (getOuterElementBorderWidth() != null) {
            Float total = getOuterElementBorderWidth() + inc;
            setElementValue("outer", "stroke-width", total.toString());//setOuterElementBorderWidth is not used because orignal value changed
        }
    }

    public void decreaseBorderWidth(float dec) {
        if (getOuterElementBorderWidth() != null) {
            Float total = getOuterElementBorderWidth() - dec;
            setElementValue("outer", "stroke-width", total.toString());//setOuterElementBorderWidth is not used because orignal value changed
        }
    }

    void glowWidget() {
//        setElementValue("outer", "filter", "url(#blur-effect)");
//        setElementValue("inner", "filter", "url(#blur-effect)");
    }

    void unglowWidget() {
//        setElementValue("outer", "filter", "");
//        setElementValue("inner", "filter", "");
    }

    /*----------------------------Inner-------------------------------------*/
    public Color getInnerElementStartBackgroundColor() {
        return innerElementStartBackgroundColor;
    }

    public void setInnerElementStartBackgroundColor(Color innerElementStartBackgroundColor) {
        this.innerElementStartBackgroundColor = innerElementStartBackgroundColor;
        setElementValue("inner-start-grad", "style", "stop-color:" + getColorString(innerElementStartBackgroundColor) + ";stop-opacity:1;");
    }

    public Color getInnerElementEndBackgroundColor() {
        return innerElementEndBackgroundColor;
    }

    public void setInnerElementEndBackgroundColor(Color innerElementEndBackgroundColor) {
        this.innerElementEndBackgroundColor = innerElementEndBackgroundColor;
        setElementValue("inner-end-grad", "style", "stop-color:" + getColorString(innerElementEndBackgroundColor) + ";stop-opacity:1;");

    }

    /**
     * @return the innerElementStartOffset
     */
    public Float getInnerElementStartOffset() {
        return innerElementStartOffset;
    }

    /**
     * @param innerElementStartOffset the innerElementStartOffset to set
     */
    public void setInnerElementStartOffset(Float innerElementStartOffset) {
        this.innerElementStartOffset = innerElementStartOffset;
        setElementValue("inner-start-grad", "offset", innerElementStartOffset + "%");
    }

    /**
     * @return the innerElementEndOffset
     */
    public Float getInnerElementEndOffset() {
        return innerElementEndOffset;
    }

    /**
     * @param innerElementEndOffset the innerElementEndOffset to set
     */
    public void setInnerElementEndOffset(Float innerElementEndOffset) {
        this.innerElementEndOffset = innerElementEndOffset;
        setElementValue("inner-end-grad", "offset", innerElementEndOffset + "%");
    }

    /**
     * @return the innerElementBorderWidth
     */
    public Float getInnerElementBorderWidth() {
        return innerElementBorderWidth;
    }

    /**
     * @param innerElementBorderWidth the innerElementBorderWidth to set
     */
    public void setInnerElementBorderWidth(Float innerElementBorderWidth) {
        this.innerElementBorderWidth = innerElementBorderWidth;
        setElementValue("inner", "stroke-width", innerElementBorderWidth.toString());
    }

    /**
     * @return the innerElementBorderColor
     */
    public Color getInnerElementBorderColor() {
        return innerElementBorderColor;
    }

    /**
     * @param innerElementBorderColor the innerElementBorderColor to set
     */
    public void setInnerElementBorderColor(Color innerElementBorderColor) {
        this.innerElementBorderColor = innerElementBorderColor;
        setElementValue("inner", "stroke", getColorString(innerElementBorderColor));
    }

    String getColorString(Color color) {
        if (color != null) {
            return "RGB(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
        } else {
            return "RGB(155,155,155)";
        }

    }

    public void setElementValue(String id, String attribute, String value) {
        SVGDocument svgDocument = this.getNodeImageWidget().getSvgDocument();
        setElementValue(svgDocument, id, attribute, value);
    }

    public void setElementTextValue(String value) {
        SVGDocument svgDocument = this.getNodeImageWidget().getSvgDocument();
        setElementTextValue(svgDocument, value);
    }

    public String getElementValue(String id, String attribute) {
        SVGDocument svgDocument = this.getNodeImageWidget().getSvgDocument();
        return getElementValue(svgDocument, id, attribute);
    }

    public String getElementValue(String attribute) {//For Root Element Attr
        SVGDocument svgDocument = this.getNodeImageWidget().getSvgDocument();
        return svgDocument.getRootElement().getAttribute(attribute);
    }

    public void setElementValue(SVGDocument svgDocument, String id, String attribute, String value, boolean lazy) {
        if (svgDocument.getElementById(id) != null) {
            if (svgDocument.getElementById(id).hasAttribute(attribute)) {
                svgDocument.getElementById(id).setAttribute(attribute, value);
            } else {
                System.out.println("Document[" + this.getNodeWidgetInfo().getModelerDocument().getDocumentPath() + "] Not Found id : " + id + " attribute : " + attribute);
            }

        }
        if (!lazy) {
            reloadSVGDocument();
        }
    }

    public void setElementValue(SVGDocument svgDocument, String id, String attribute, String value) {
        setElementValue(svgDocument, id, attribute, value, false);
    }

    public void setElementTextValue(SVGDocument svgDocument, String value) {
        String textLocation = svgDocument.getRootElement().getAttribute("TextLocation");
        if ("INSIDE".equalsIgnoreCase(textLocation)) {
            String textLocationId = svgDocument.getRootElement().getAttribute("TextLocationId");
            if (textLocationId == null || textLocationId.isEmpty()) {
                throw new SVGAttributeNotFoundException("ROOT", "TextLocationId");
            } else {
                if (svgDocument.getElementById(textLocationId) != null) {
                    svgDocument.getElementById(textLocationId).setTextContent(value);
                }
            }
//            this.hideLabel();
            reloadSVGDocument();
        }
//        else {
//            if (value != null && !value.trim().isEmpty()) {
//                this.setLabel(value);
//                this.showLabel();
//            } else {
//                this.setLabel("");
//                this.hideLabel();
//            }
//        }

    }

    public String getElementValue(SVGDocument svgDocument, String id, String attribute) {
        if (svgDocument.getElementById(id) != null) {
            return svgDocument.getElementById(id).getAttribute(attribute);
        }
        return null;
    }

    public void reloadSVGDocument() {
        if (this.isActiveStatus()) {
            this.getNodeImageWidget().setSVGDocument(this.getNodeImageWidget().getSvgDocument());
        }
    }

    /*--------------------------------------------------------------------------*/
    /**
     * @return the activeStatus
     */
    @Override
    public boolean isActiveStatus() {
        return activeStatus;
    }

    /**
     * @param activeStatus the activeStatus to set
     */
    @Override
    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    /**
     * @return the status
     */
    public NodeWidgetStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    @Override
    public void setStatus(NodeWidgetStatus status) {

        if (status == NodeWidgetStatus.INVALID & this.getStatus() != NodeWidgetStatus.INVALID) {
            this.getModelerScene().setInvalidNodeWidget(this);
            changeBorderColor(new Color(223, 35, 0));
            hoverWidget();
            glowWidget();
        } else if (status == NodeWidgetStatus.VALID & this.getStatus() != NodeWidgetStatus.VALID) {
            this.getModelerScene().setValidNodeWidget(this);
            changeBorderColor(new Color(81, 141, 4));
            hoverWidget();
        } else if (status == NodeWidgetStatus.ACCEPT & this.getStatus() != NodeWidgetStatus.ACCEPT) {
            changeBorderColor(new Color(0, 162, 232));
            hoverWidget();
        } else if (NodeWidgetStatus.NONE == status & this.getStatus() != NodeWidgetStatus.NONE) {
            if (this.getModelerScene().getValidNodeWidget() == this) {
                this.getModelerScene().setValidNodeWidget(null);
                defaultBorderColor();
                unhoverWidget();
            } else if (this.getModelerScene().getInvalidNodeWidget() == this) {
                this.getModelerScene().setInvalidNodeWidget(null);
                defaultBorderColor();
                unhoverWidget();
                unglowWidget();
            } else {
                defaultBorderColor();
                unhoverWidget();
            }
        } else {
            this.setBorder(DEFAULT_BORDER);
        }
        this.status = status;
    }

    /**
     * @return the widgetBorder
     */
    @Override
    public ResizeBorder getWidgetBorder() {
        return widgetBorder;
    }

    /**
     * @param widgetBorder the widgetBorder to set
     */
    @Override
    public void setWidgetBorder(ResizeBorder widgetBorder) {
        this.widgetBorder = widgetBorder;
    }

    /**
     * @return the widgetInnerArea
     */
    @Override
    public Rectangle getWidgetInnerArea() {
        return widgetBorder.getWidgetArea();
    }

    @Override
    public Dimension getWidgetInnerDimension() {
        return widgetBorder.getWidgetArea().getSize();
    }

    @Override
    public boolean isAnchorEnable() {
        return anchorState;
    }

    @Override
    public void setAnchorState(boolean state) {
        this.anchorState = state;
    }

    @Override
    public Rectangle getSceneViewBound() {
        return this.convertLocalToScene(this.getPreferredBounds());
    }

    @Override
    public Point getSceneViewLocation() {
        return this.convertLocalToScene(this.getPreferredLocation());
    }

    @Override
    // to achieve resize cursors and move cursor
    protected Cursor getCursorAt(Point location) {
        Border border = this.getBorder();
        if (!(border instanceof ResizeBorder)) {
            return getCursor();
        }

        Rectangle bounds = getBounds();
        Insets insets = border.getInsets();
        int thickness = insets.bottom;

//        Rectangle topLeft = new Rectangle(bounds.x, bounds.y, thickness, thickness);
//        Rectangle topRight = new Rectangle(bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
//        Rectangle bottomLeft = new Rectangle(bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
        Rectangle bottomRight = new Rectangle(bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);

        Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);

//        Rectangle topCenter = new Rectangle(center.x - thickness / 2, bounds.y, thickness, thickness);
        Rectangle bottomCenter = new Rectangle(center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
//        Rectangle leftCenter = new Rectangle(bounds.x, center.y - thickness / 2, thickness, thickness);
        Rectangle rightCenter = new Rectangle(bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);

        Rectangle[] rects = new Rectangle[]{
            // topLeft,
            //topRight,
            //bottomLeft,
            bottomRight,
            //topCenter,
            bottomCenter,
            //leftCenter,
            rightCenter
        };

        Cursor[] cursors = new Cursor[]{
            //            Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR),
            //            Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR),
            //            Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR),
            Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR),
            //            Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR),
            Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),
            //            Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR),
            Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)
        };
        for (int i = 0; i < rects.length; i++) {
            if (rects[i].contains(location)) {
                return cursors[i];
            }
        }
//        if (getState().isSelected() && scene.getActiveTool().equals(DesignerTools.SELECT))
//            return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        return getCursor();
    }

    @Override
    public boolean remove() {
        return remove(false);
    }

    @Override
    public boolean remove(boolean notification) {
        if (notification) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(String.format("are you sure you want to delete %s ?", this.getLabel()), String.format("Delete ", this.getLabel()), NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
                removeNode();
                return true;
            }
        } else {
            removeNode();
            return true;
        }
        return false;
    }

    private void removeNode() {
        if (!locked) {
            IModelerScene scene = this.getModelerScene();
            this.setLabel("");
            this.hideLabel();
//        this.removeFromParent();
//            ((IBaseElementWidget) this).destroy();
            if (((IFlowNodeWidget) this).getFlowElementsContainer() instanceof IModelerSubScene) {
                IModelerSubScene modelerSubScene = (IModelerSubScene) ((IFlowNodeWidget) this).getFlowElementsContainer();
                modelerSubScene.deleteBaseElement((IBaseElementWidget) this);
            } else {
                scene.deleteBaseElement((IBaseElementWidget) this);
            }
            scene.deleteNodeWidget(this);
            scene.getModelerPanelTopComponent().changePersistenceState(false);
            cleanReference();
        }
    }

    private boolean locked = false;

    /**
     * @return the exist
     */
    @Override
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public INodeWidget addSiblingWidget(NodeWidgetInfo newNodeWidgetInfo, int xPadding, int yPadding, boolean connect, boolean horizontal) {
        NodeWidget nodeWidget = this;
        Rectangle sceneBound = nodeWidget.getSceneViewBound();
        INodeWidget new_nodewidget = nodeWidget.getModelerScene().createNodeWidget(newNodeWidgetInfo);
        Rectangle new_rec = new_nodewidget.getSceneViewBound();
        Point point;
        if (horizontal) {
            point = new Point((int) (sceneBound.getX() + sceneBound.getWidth() + xPadding
                    + (sceneBound.getWidth() - new_rec.getWidth()) / 2),
                    (int) (sceneBound.getY() + yPadding
                    + (sceneBound.getHeight() - new_rec.getHeight()) / 2));

        } else {
            point = new Point((int) (sceneBound.getX() + xPadding
                    + (sceneBound.getWidth() - new_rec.getWidth()) / 2),
                    (int) (sceneBound.getY() + sceneBound.getHeight() + yPadding
                    + (sceneBound.getHeight() - new_rec.getHeight()) / 2));
        }
        new_nodewidget.setPreferredLocation(point);

        if (connect) {
            SceneConnectProvider connectProvider = new SceneConnectProvider(null, null);
            connectProvider.createConnection((ModelerScene) nodeWidget.getModelerScene(), nodeWidget, (NodeWidget) new_nodewidget);
        }
        nodeWidget.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        return new_nodewidget;
    }

    private S scene;

    /**
     * @return the scene
     */
    @Override
    public S getModelerScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setModelerScene(S scene) {
        this.scene = scene;
    }
    
    @Override
    public void cleanReference() {
        if (this.getPropertyManager() != null) {
            this.getPropertyManager().getElementPropertySet().clearGroups();//clear ElementSupportGroup
        }
        for (Widget childWidget : this.getChildren()) {
            if (childWidget instanceof IPinWidget) {
                ((IPinWidget) childWidget).cleanReference();
            }
        }
        this.getModelerScene().getModelerFile().getModelerDiagramEngine().clearNodeWidgetAction(this);
        this.getLabelManager().getLabelConnectionWidget().clearActions();
    }
    
    public void updateNodeWidgetDesign(ShapeDesign shapeDesign){
        
    }
}
