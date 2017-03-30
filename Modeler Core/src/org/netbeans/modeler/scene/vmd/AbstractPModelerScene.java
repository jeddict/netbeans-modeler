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
package org.netbeans.modeler.scene.vmd;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.actions.EventListener;
import org.netbeans.modeler.actions.IEventListener;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.properties.view.manager.IPropertyManager;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IPModelerScene;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.widget.context.ContextPaletteManager;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.context.SwingPaletteManager;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.IPEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.IWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.node.vmd.PNodeWidget;
import org.netbeans.modeler.widget.pin.IPinSeperatorWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.PinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 *
 *
 */
public abstract class AbstractPModelerScene<E extends IRootElement> extends GraphPinScene<NodeWidgetInfo, EdgeWidgetInfo, PinWidgetInfo> implements IPModelerScene<E> {

    private static final Logger LOG = Logger.getLogger(AbstractPModelerScene.class.getName());

    private LayerWidget backgroundLayer; //  rectangular selection
    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;//interactive actions like ConnectAction or AlignWithMoveAction
    private LayerWidget boundaryWidgetLayer;
    private LayerWidget labelLayer;
    private Router router = null;
    private JComponent satelliteView;
    private IModelerPanel modelerPanel;
    private ModelerFile modelerFile;
    // Data
    private ContextPaletteManager paletteManager = null;
    // Lookup Data
    private InstanceContent lookupContent = new InstanceContent();
    private ArrayList< IFlowElementWidget> lockedSelected = new ArrayList< IFlowElementWidget>();
    private INodeWidget validNodeWidget;
    private INodeWidget invalidNodeWidget;
    private boolean alignSupport = true;

    public AbstractPModelerScene() {
        setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_CHILDREN);
        backgroundLayer = new LayerWidget(this);
        mainLayer = new LayerWidget(this);
        connectionLayer = new LayerWidget(this);
        interractionLayer = new LayerWidget(this);
        boundaryWidgetLayer = new LayerWidget(this);
        labelLayer = new LayerWidget(this);
        addChild(backgroundLayer);
        addChild(mainLayer);
        addChild(interractionLayer);
        addChild(connectionLayer);
        addChild(boundaryWidgetLayer);
        addChild(labelLayer);

        boundaryWidgetLayer.bringToFront();
        connectionLayer.bringToFront();
        labelLayer.bringToFront();

        router = RouterFactory.createOrthogonalSearchRouter(mainLayer, connectionLayer);//RouterFactory.createFreeRouter();
        satelliteView = this.createSatelliteView();
        setActiveTool(DesignerTools.SELECT);
    }

    @Override
    public void init() {
        this.setContextPaletteManager(new SwingPaletteManager((IModelerScene) this));

        IColorScheme scheme = this.getColorScheme();
        scheme.installUI(this);
    }
    private final Map<String, PropertyChangeListener> propertyChangeHandlers = new HashMap<String, PropertyChangeListener>();

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

    /**
     * Called by the addNode method before the node is registered to acquire a
     * widget that is going to represent the node in the scene. The method is
     * responsible for creating the widget, adding it into the scene and
     * returning it from the method.
     *
     * @param node the node that is going to be added
     * @return the widget representing the node; null, if the node is non-visual
     */
    @Override
    protected Widget attachNodeWidget(NodeWidgetInfo widgetInfo) {
        Widget widget = (Widget) this.getModelerFile().getModelerUtil().attachNodeWidget(this, widgetInfo);
        this.getMainLayer().addChild(widget);
        this.setFocusedWidget(widget);
        this.validateComponent();//#60
        if (widget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) widget).setId(widgetInfo.getId());
            this.createBaseElement((IBaseElementWidget) widget);
        }

        return widget;
    }

    /**
     * Called by the addEdge method before the edge is registered to acquire a
     * widget that is going to represent the edge in the scene. The method is
     * responsible for creating the widget, adding it into the scene and
     * returning it from the method.
     *
     * @param edge the edge that is going to be added
     * @return the widget representing the edge; null, if the edge is non-visual
     */
    @Override
    protected Widget attachEdgeWidget(EdgeWidgetInfo widgetInfo) {
        IEdgeWidget edgeWidget = this.getModelerFile().getModelerUtil().attachEdgeWidget(this, widgetInfo);
        this.getConnectionLayer().addChild((Widget) edgeWidget);
        edgeWidget.setEndPointShape(PointShape.SQUARE_FILLED_SMALL);
        edgeWidget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
        edgeWidget.setRouter(getRouter());
        this.setFocusedWidget((Widget) edgeWidget);
        this.validateComponent();//#60
        if (edgeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) edgeWidget).setId(widgetInfo.getId());
            this.createBaseElement((IBaseElementWidget) edgeWidget);
        }
        return (Widget) edgeWidget;
    }

    /**
     * Called by the addPin method before the pin is registered to acquire a
     * widget that is going to represent the pin in the scene. The method is
     * responsible for creating the widget, adding it into the scene and
     * returning it from the method.
     *
     * @param nodeWidgetInfo
     * @param pinWidgetInfo
     *
     * @return the widget representing the pin; null, if the pin is non-visual
     */
    @Override
    protected Widget attachPinWidget(NodeWidgetInfo nodeWidgetInfo, PinWidgetInfo pinWidgetInfo) {
        INodeWidget nodeWidget = (INodeWidget) findWidget(nodeWidgetInfo);

        if (pinWidgetInfo.getDocumentId().equals("INTERNAL")) {
            return null;
        }
        PinWidget pinWidget = (PinWidget) this.getModelerFile().getPModelerUtil().attachPinWidget(this, nodeWidget, pinWidgetInfo);
        if (pinWidget == null) {
            return null;
        }
        if (pinWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) pinWidget).setId(pinWidgetInfo.getId());
            ((PNodeWidget) nodeWidget).attachPinWidget(pinWidget);
        }

//        this.setFocusedWidget(pinWidget);
//        this.validate();
        return pinWidget;
    }

    /**
     * Called by the setEdgeSource method to notify about the changing the edge
     * source in the graph model. The method is responsible for attaching a new
     * source pin to the edge in the visual representation.
     * <p>
     * Usually it is implemented as:
     * <pre>
     * Widget sourcePinWidget = findWidget (sourcePin);
     * Anchor sourceAnchor = AnchorFactory.createRectangularAnchor (sourcePinWidget)
     * ConnectionWidget edgeWidget = (ConnectionWidget) findWidget (edge);
     * edgeWidget.setSourceAnchor (sourceAnchor);
     * </pre>
     *
     * @param edge the edge which source is changed in graph model
     * @param oldSourcePin the old source pin
     * @param sourcePin the new source pin
     */
    @Override
    protected void attachEdgeSourceAnchor(EdgeWidgetInfo edgeWidgetInfo, PinWidgetInfo oldSourcePin, PinWidgetInfo sourcePinInfo) {
        IEdgeWidget edgeWidget = (IEdgeWidget) findWidget(edgeWidgetInfo);
        if (sourcePinInfo == null) {//called in case of edge remove
            edgeWidget.setSourceAnchor(null);
            return;
        }
        IPinWidget sourcePinWidget = (IPinWidget) findWidget(sourcePinInfo);
        if (sourcePinWidget != null) {
            NBModelerUtil.attachEdgeSourceAnchor((IModelerScene) this, edgeWidget, sourcePinWidget);
        } else {
            INodeWidget sourceNodeWidget = (INodeWidget) findWidget(getPinNode(sourcePinInfo));
            NBModelerUtil.attachEdgeSourceAnchor((IModelerScene) this, edgeWidget, sourceNodeWidget);
        }
    }

    /**
     * Called by the setEdgeTarget method to notify about the changing the edge
     * target in the graph model. The method is responsible for attaching a new
     * target pin to the edge in the visual representation.
     * <p>
     * Usually it is implemented as:
     * <pre>
     * Widget targetPinWidget = findWidget (targetPin);
     * Anchor targetAnchor = AnchorFactory.createRectangularAnchor (targetPinWidget)
     * ConnectionWidget edgeWidget = (ConnectionWidget) findWidget (edge);
     * edgeWidget.setTargetAnchor (targetAnchor);
     * </pre>
     *
     * @param edge the edge which target is changed in graph model
     * @param oldTargetPin the old target pin
     * @param targetPin the new target pin
     */
    @Override
    protected void attachEdgeTargetAnchor(EdgeWidgetInfo edgeWidgetInfo, PinWidgetInfo oldTargetPin, PinWidgetInfo targetPinInfo) {
        IEdgeWidget edgeWidget = (IEdgeWidget) findWidget(edgeWidgetInfo);
        if (targetPinInfo == null) {//called in case of edge remove
            edgeWidget.setTargetAnchor(null);
            return;
        }
        IPinWidget targetPinWidget = (IPinWidget) findWidget(targetPinInfo);
        if (targetPinWidget != null) {
            NBModelerUtil.attachEdgeTargetAnchor((IModelerScene) this, edgeWidget, targetPinWidget);
        } else {
            INodeWidget targetNodeWidget = (INodeWidget) findWidget(getPinNode(targetPinInfo));
            NBModelerUtil.attachEdgeTargetAnchor((IModelerScene) this, edgeWidget, targetNodeWidget);
        }
        if (edgeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) edgeWidget).init();
        }
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        if (state.isSelected()) {
            this.getContextPaletteManager().cancelPalette();
        }
    }

    @Override
    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    @Override
    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

    @Override
    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    @Override
    public void paintChildren() {
        if(closed){
            return;
        }
        Object anti = getGraphics().getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Object textAnti = getGraphics().getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);

        getGraphics().setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        getGraphics().setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintChildren();
        getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, anti);
        getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAnti);
    }

    @Override
    public JComponent getSatelliteView() {
        return satelliteView;
    }

    public void setContextPaletteManager(ContextPaletteManager manager) {
        if (paletteManager != null) {
            removeFromLookup(paletteManager);
        }

        paletteManager = manager;
        if (paletteManager != null) {
            addToLookup(paletteManager);
        }
    }

    @Override
    public ContextPaletteManager getContextPaletteManager() {
        return paletteManager;
    }

    private void addToLookup(Object item) {
        lookupContent.add(item);
    }

    protected void removeFromLookup(Object item) {
        lookupContent.remove(item);
    }

    /**
     * @param interactionLayer the interactionLayer to set
     */
    public void setInteractionLayer(LayerWidget interactionLayer) {
        this.interractionLayer = interactionLayer;
    }

    /**
     * @param mainLayer the mainLayer to set
     */
    public void setMainLayer(LayerWidget mainLayer) {
        this.mainLayer = mainLayer;
    }

    /**
     * @param connectionLayer the connectionLayer to set
     */
    public void setConnectionLayer(LayerWidget connectionLayer) {
        this.connectionLayer = connectionLayer;
    }

    /**
     * @return the topComponent
     */
    @Override
    public IModelerPanel getModelerPanelTopComponent() {
        return modelerPanel;
    }

    /**
     * @param topComponent the topComponent to set
     */
    @Override
    public void setModelerPanelTopComponent(IModelerPanel topComponent) {
        this.modelerPanel = topComponent;
    }

    /**
     * Retrieves all of the nodes and edges that are in a specified area. The
     * area is specified in screen coordinates.
     *
     * @param sceneSelection The area that must contain the nodes and edges.
     * @param containedOnly Only select nodes and edges that are fully
     * contained.
     *
     * @return The nodes and edges in the specified area.
     */
    public Set<IFlowElementWidget> getGraphObjectInRectangle(Rectangle sceneSelection,
            boolean intersectNodes,
            boolean intersectEdges) {
        int w = sceneSelection.width;
        int h = sceneSelection.height;
        Rectangle rect = new Rectangle(w >= 0 ? 0 : w, h >= 0 ? 0 : h, w >= 0 ? w : -w, h >= 0 ? h : -h);
        rect.translate(sceneSelection.x, sceneSelection.y);

        HashSet<IFlowElementWidget> set = new HashSet<IFlowElementWidget>();
        Set<?> objects = getObjects();
        for (Object object : objects) {
            boolean isEdge = isEdge(object);
            boolean isNode = isNode(object);
            if ((isEdge == false) && (isNode == false)) {
                continue;
            }

            Widget widget = findWidget(object);

            if (widget == null) {
                continue;
            }

            if (((isNode == true) && (intersectNodes == false))
                    || ((isEdge == true) && (intersectEdges == false))) {
                // The node or edge must be entirely contained.
                Rectangle widgetRect = widget.convertLocalToScene(widget.getBounds());
                if (rect.contains(widgetRect) && (object instanceof IFlowElementWidget)) {
                    set.add((IFlowElementWidget) object);
                }
            } else {
                // The node or edge can intersect the rectangle.
                if (widget instanceof ConnectionWidget) {
                    ConnectionWidget conn = (ConnectionWidget) widget;
                    java.util.List<Point> points = conn.getControlPoints();
                    for (int i = points.size() - 2; i >= 0; i--) {
                        Point p1 = widget.convertLocalToScene(points.get(i));
                        Point p2 = widget.convertLocalToScene(points.get(i + 1));
                        if (new Line2D.Float(p1, p2).intersects(rect)) {
                            set.add((IFlowElementWidget) object);
                        }
                    }
                } else {
                    Rectangle widgetRect = widget.convertLocalToScene(widget.getBounds());
                    if (rect.intersects(widgetRect)) {
                        set.add((IFlowElementWidget) object);
                    }
                }
        }
        }

        return set;
    }

    /**
     * @return the backgroundLayer
     */
    @Override
    public LayerWidget getBackgroundLayer() {
        return backgroundLayer;
    }

    /**
     * @param backgroundLayer the backgroundLayer to set
     */
    public void setBackgroundLayer(LayerWidget backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
    }

    public void addLockedSelected(IFlowElementWidget element) {
        lockedSelected.add(element);
    }

    public void removeLockSelected(IFlowElementWidget element) {
        lockedSelected.remove(element);
    }

    public List< IFlowElementWidget> getLockedSelected() {
        return Collections.unmodifiableList(lockedSelected);
    }

    public void clearLockedSelected() {
        lockedSelected.clear();
    }

    /**
     * @return the router
     */
    @Override
    public Router getRouter() {
        return router;
    }

    /**
     * @param router the router to set
     */
    public void setRouter(Router router) {
        this.router = router;
    }

    private void reinstallColorScheme() {
        IColorScheme scheme = this.getColorScheme();
        scheme.installUI(this);
        for (Widget widget : this.getChildren()) {
            reinstallColorScheme(widget, scheme);
        }
    }

    public void reinstallColorScheme(IWidget widget){
        reinstallColorScheme((Widget)widget, this.getColorScheme());
    }
    
    private void reinstallColorScheme(Widget widget, IColorScheme scheme) {
        if (widget instanceof IPNodeWidget) {//PNodeAnchor implements skipped
            IPNodeWidget nodeWidget = (IPNodeWidget) widget;
            scheme.installUI(nodeWidget);
            scheme.updateUI(nodeWidget, nodeWidget.getState(), nodeWidget.getState());
            if (nodeWidget.isHighlightStatus()) {
                scheme.highlightUI(nodeWidget);
            }
            nodeWidget.setColorScheme(scheme);

            Iterator<Entry<String, IPinSeperatorWidget>> itr = nodeWidget.getPinCategoryWidgets().entrySet().iterator();
            while (itr.hasNext()) {
                Entry<String, IPinSeperatorWidget> entry = itr.next();
                scheme.installUI(entry.getValue());
            }

            for (Widget widget_TMP : nodeWidget.getChildren()) {
                reinstallColorScheme(widget_TMP, scheme);
            }
        } else if (widget instanceof IPEdgeWidget) {
            IPEdgeWidget edgeWidget = (IPEdgeWidget) widget;
            scheme.installUI(edgeWidget);
            scheme.updateUI(edgeWidget, edgeWidget.getState(), edgeWidget.getState());
            if (edgeWidget.isHighlightStatus()) {
                scheme.highlightUI(edgeWidget);
            }
            edgeWidget.setColorScheme(scheme);
        } else if (widget instanceof IPinWidget) {
            IPinWidget pinWidget = (IPinWidget) widget;
            scheme.installUI(pinWidget);
            scheme.updateUI(pinWidget, pinWidget.getState(), pinWidget.getState());
            if (pinWidget.isHighlightStatus()) {
                scheme.highlightUI(pinWidget);
            }
            pinWidget.setColorScheme(scheme);
        } else {
            for (Widget widget_TMP : widget.getChildren()) {
                reinstallColorScheme(widget_TMP, scheme);
            }
        }
    }

    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuItemList = new LinkedList<>();

        JMenu themeMenu = new JMenu("Theme");
        ButtonGroup thmemeGroup = new javax.swing.ButtonGroup();

        for (final Entry<String, Class<? extends IColorScheme>> scheme : this.getColorSchemes().entrySet()) {
            JRadioButtonMenuItem schemeMenu = new JRadioButtonMenuItem(scheme.getKey());
            schemeMenu.addActionListener((ActionEvent e) -> {
                AbstractPModelerScene.this.setColorScheme(scheme.getValue());
                reinstallColorScheme();
                AbstractPModelerScene.this.getModelerPanelTopComponent().changePersistenceState(false);
            });
            themeMenu.add(schemeMenu);
            thmemeGroup.add(schemeMenu);
//            if (this.getColorScheme().getId().equals(scheme.getId())) {
//                schemeMenu.setSelected(true);
//            }
        }
        menuItemList.add(themeMenu);

        JMenu container = new JMenu("Container");
        menuItemList.add(container);
        
        JMenuItem rerouteMenu = new JMenuItem("Re-Route");
        rerouteMenu.addActionListener((ActionEvent e) -> {
            int option = JOptionPane.showConfirmDialog(WindowManager.getDefault().getMainWindow(), "Are you want to re-route the diagram ?", "Re-Route Diagram", JOptionPane.YES_NO_OPTION);
            if (option == javax.swing.JOptionPane.OK_OPTION) {
                AbstractPModelerScene.this.autoLayout();
            }
        });
        container.add(rerouteMenu);

        JMenuItem routeMenu = new JMenu("Route types");
//        position.setIcon(ImageUtil.getInstance().getIcon("position.png"));
        final JRadioButtonMenuItem freeRoute = new JRadioButtonMenuItem("Free Design");
        final JRadioButtonMenuItem autoRoute = new JRadioButtonMenuItem("Auto Route");

//        freeRoute.setIcon(ImageUtil.getInstance().getIcon("front.png"));
        freeRoute.addActionListener((ActionEvent e) -> {
            freeRoute.setSelected(true);
            autoRoute.setSelected(false);
            router = RouterFactory.createFreeRouter();
            for (Widget widget : AbstractPModelerScene.this.connectionLayer.getChildren()) {
                if (widget instanceof ConnectionWidget) {
                    ((ConnectionWidget) widget).setRouter(router);
                }
            }
        });
        routeMenu.add(freeRoute);

//        autoRoute.setIcon(ImageUtil.getInstance().getIcon("back.png"));
        autoRoute.addActionListener((ActionEvent e) -> {
            freeRoute.setSelected(false);
            autoRoute.setSelected(true);
            router = RouterFactory.createOrthogonalSearchRouter(mainLayer, connectionLayer);
            for (Widget widget : AbstractPModelerScene.this.connectionLayer.getChildren()) {
                if (widget instanceof ConnectionWidget) {
                    ((ConnectionWidget) widget).setRouter(router);
                }
            }
        });
        routeMenu.add(autoRoute);

        ButtonGroup routeGroup = new javax.swing.ButtonGroup();
        routeGroup.add(freeRoute);
        routeGroup.add(autoRoute);

        if (router.getClass().getSimpleName().equals("FreeRouter")) {
            freeRoute.setSelected(true);
            autoRoute.setSelected(false);
        } else if (router.getClass().getSimpleName().equals("OrthogonalSearchRouter")) {
            freeRoute.setSelected(false);
            autoRoute.setSelected(true);
        }

        container.add(routeMenu);

        JCheckBoxMenuItem alignMenu = new JCheckBoxMenuItem("Align Support");
        alignMenu.addActionListener((ActionEvent e) -> {
            if (AbstractPModelerScene.this.isAlignSupport()) {
                setAlignSupport(false);
            } else {
                setAlignSupport(true);
            }
        });
        alignMenu.setSelected(true);

        container.add(alignMenu);

        JMenuItem propsMenu = new JMenuItem("Properties");
        propsMenu.setIcon(ImageUtil.getInstance().getIcon("properties.gif"));
        propsMenu.addActionListener((ActionEvent e) -> {
            AbstractPModelerScene.this.showProperties();
        });

        menuItemList.add(propsMenu);

        return menuItemList;
    }

    @Override
    public PopupMenuProvider getPopupMenuProvider() {
        JPopupMenu popupMenu = new JPopupMenu();
        List<JMenuItem> menuItemList = getPopupMenuItemList();
        menuItemList.forEach(menuItem -> {
            if (menuItem == null) {
                popupMenu.addSeparator();
            } else {
                popupMenu.add(menuItem);
            }
        });
       return (widget, location) -> popupMenu;
    }

    private BasePropertyViewManager node;

    @Override
    public void exploreProperties() {
        if (node == null) {
            node = new BasePropertyViewManager((IBaseElementWidget) this);
        }
        org.netbeans.modeler.properties.util.PropertyUtil.exploreProperties(node, this.getName(), propertyVisibilityHandlers);
    }
    
    public IPropertyManager getPropertyManager(){
        return node;
    }

    @Override
    public void refreshProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.refreshProperties(node, this.getName(), propertyVisibilityHandlers);
    }

    @Override
    public void showProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.showProperties(node, this.getName(), propertyVisibilityHandlers);
    }

    /**
     * @return the validNodeWidget
     */
    @Override
    public INodeWidget getValidNodeWidget() {
        return validNodeWidget;
    }

    /**
     * @param validNodeWidget the validNodeWidget to set
     */
    @Override
    public void setValidNodeWidget(INodeWidget validNodeWidget) {
        this.validNodeWidget = validNodeWidget;
    }

    /**
     * @return the invalidNodeWidget
     */
    @Override
    public INodeWidget getInvalidNodeWidget() {
        return invalidNodeWidget;
    }

    /**
     * @param invalidNodeWidget the invalidNodeWidget to set
     */
    @Override
    public void setInvalidNodeWidget(INodeWidget invalidNodeWidget) {
        this.invalidNodeWidget = invalidNodeWidget;
    }

    /**
     * @return the boundaryWidgetLayer
     */
    @Override
    public LayerWidget getBoundaryWidgetLayer() {
        return boundaryWidgetLayer;
    }

    /**
     * @param boundaryWidgetLayer the boundaryWidgetLayer to set
     */
    public void setBoundaryWidgetLayer(LayerWidget boundaryWidgetLayer) {
        this.boundaryWidgetLayer = boundaryWidgetLayer;
    }

    public void manageWidgetBorder() {
    }

    @Override
    public void manageLayerWidget() {

        getInterractionLayer().bringToBack();
        getBoundaryWidgetLayer().bringToFront();
        getMainLayer().bringToFront();
        getConnectionLayer().bringToFront();
        getBoundaryWidgetLayer().bringToFront();
        getLabelLayer().bringToFront();
    }

    /**
     * @return the modelerFile
     */
    @Override
    public ModelerFile getModelerFile() {
        return modelerFile;
    }

    /**
     * @param modelerFile the modelerFile to set
     */
    @Override
    public void setModelerFile(ModelerFile modelerFile) {
        this.modelerFile = modelerFile;
    }

    @Override
    public INodeWidget createNodeWidget(NodeWidgetInfo node) {
        IPNodeWidget nodeWidget = (IPNodeWidget) this.addNode(node);
        nodeWidget.setPreferredLocation(this.convertLocalToScene(node.getLocation()));
        if (!node.isExist()) {
            this.revalidate();
            this.validateComponent();
        }
        if (nodeWidget instanceof IBaseElementWidget) {
            if (node.getBaseElementSpec() != null) {
                ((IBaseElementWidget) nodeWidget).setBaseElementSpec(node.getBaseElementSpec());
            }
            ((IBaseElementWidget) nodeWidget).init();
        }
        nodeWidget.getModelerScene().getModelerFile().getModelerDiagramEngine().setNodeWidgetAction(nodeWidget);

        PinWidgetInfo pinWidgetInfo = new PinWidgetInfo(NBModelerUtil.getAutoGeneratedStringId(), "INTERNAL");
        nodeWidget.setInternalPinWidgetInfo(pinWidgetInfo);
        createPinWidget(nodeWidget.getNodeWidgetInfo(), pinWidgetInfo);

        return nodeWidget;
    }

    @Override
    public IEdgeWidget createEdgeWidget(EdgeWidgetInfo edge) {
        IEdgeWidget edgeWidget = (IEdgeWidget) this.addEdge(edge);
        if (edgeWidget instanceof IBaseElementWidget) {
            if (edge.getBaseElementSpec() != null) {
                ((IBaseElementWidget) edgeWidget).setBaseElementSpec(edge.getBaseElementSpec());
            }
            if (edgeWidget instanceof IFlowEdgeWidget) {
                ((IFlowEdgeWidget) edgeWidget).setName(edge.getName());
            }
//            ((IBaseElementWidget) edgeWidget).init(); //move to target anchor
        }
        edgeWidget.getModelerScene().getModelerFile().getModelerDiagramEngine().setEdgeWidgetAction(edgeWidget);

        return edgeWidget;
    }

    @Override
    public IPinWidget createPinWidget(NodeWidgetInfo node, PinWidgetInfo pin) {
        IPinWidget pinWidget = (IPinWidget) this.addPin(node, pin);

        if (pinWidget instanceof IBaseElementWidget) {
            if (pin.getBaseElementSpec() != null) {
                ((IBaseElementWidget) pinWidget).setBaseElementSpec(pin.getBaseElementSpec());
            }
            ((IBaseElementWidget) pinWidget).init();
        }
        if (pinWidget != null) {
            pinWidget.getModelerScene().getModelerFile().getModelerDiagramEngine().setPinWidgetAction(pinWidget);
        }

        return pinWidget;
    }

    @Override
    public void deleteNodeWidget(INodeWidget nodeWidget) {
        if (this.isNode(nodeWidget.getNodeWidgetInfo())) {
            this.removeNode(nodeWidget.getNodeWidgetInfo());
            if (nodeWidget instanceof IBaseElementWidget) {
                ((IBaseElementWidget) nodeWidget).destroy();
            }
        } else {
            LOG.warning("node not found");
        }
    }

    @Override
    public void deleteEdgeWidget(IEdgeWidget edgeWidget) {
        if (this.isEdge(edgeWidget.getEdgeWidgetInfo())) {
            this.removeEdge(edgeWidget.getEdgeWidgetInfo());
            if (edgeWidget instanceof IBaseElementWidget) {
                ((IBaseElementWidget) edgeWidget).destroy();
            }
        } else {
            LOG.warning("edge not found");
        }
    }

    @Override
    public void deletePinWidget(IPinWidget pinWidget) {
        if (this.isPin(pinWidget.getPinWidgetInfo())) {
            this.removePin(pinWidget.getPinWidgetInfo());
            if (pinWidget instanceof IBaseElementWidget) {
                ((IBaseElementWidget) pinWidget).destroy();
            }
        } else {
            LOG.warning("pin not found");
        }
       
    }

    @Override
    public void setEdgeWidgetSource(EdgeWidgetInfo edge, PinWidgetInfo pin) {
        this.setEdgeSource(edge, pin);
    }

    @Override
    public void setEdgeWidgetTarget(EdgeWidgetInfo edge, PinWidgetInfo pin) {

        this.setEdgeTarget(edge, pin);
    }

    /**
     * @return the alignSupport
     */
    @Override
    public boolean isAlignSupport() {
        return alignSupport;
    }

    /**
     * @param alignSupport the alignSupport to set
     */
    @Override
    public void setAlignSupport(boolean alignSupport) {
        this.alignSupport = alignSupport;
    }

    /**
     * @return the labelLayer
     */
    @Override
    public LayerWidget getLabelLayer() {
        return labelLayer;
    }

    /**
     * @param labelLayer the labelLayer to set
     */
    @Override
    public void setLabelLayer(LayerWidget labelLayer) {
        this.labelLayer = labelLayer;
    }

    @Override
    public Anchor getPinAnchor(IPinWidget pin) {
        if (pin == null) {
            return null;//nodeWidget.getNodeAnchor();
        }
        IPNodeWidget nodeWidget = pin.getPNodeWidget();
        Anchor anchor;
        anchor = AnchorFactory.createDirectionalAnchor((Widget) pin, AnchorFactory.DirectionalAnchorKind.HORIZONTAL, 8);
        anchor = nodeWidget.createAnchorPin(anchor);
        return anchor;
    }

    @Override
    public void autoLayout() {
        SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(this, new GridGraphLayout<NodeWidgetInfo, EdgeWidgetInfo>().setChecker(true));
        sceneLayout.invokeLayout();
    }

    @Override
    public ContextPaletteModel getContextPaletteModel() {
        return null;
    }

    public void validateComponent() {
        if(!isSceneGenerating()){
            long st = new Date().getTime();
            this.validate();
            System.out.println("validateComponent Total time : " + (new Date().getTime() - st) + " ms");
        }
    }
    private boolean sceneGeneration; //to improve the effciency and thread-safety in parrallel processing of ui generation (scene.validate() called at the end instead of after each element creation)

    public void startSceneGeneration() {
        sceneGeneration = true;
    }

    public void commitSceneGeneration() {
        sceneGeneration = false;
        RequestProcessor.getDefault().post(() -> {
            validateComponent();
        });
    }

    public boolean isSceneGenerating() {
        return sceneGeneration;
    }

    @Override
    public IModelerScene getModelerScene() {
        return this;
    }

    @Override
    public JComponent createView() {
        super.createView();
        addKeyboardActions();
        return getView();
    }

    /**
     * Register Keyboard Event //TODO same need to do in AbstractModelerScene
     */
    public void addKeyboardActions() {
        getView().setFocusable(true);
        getActions().addAction(new MouseClickAction(getView()));
        getEventListener().registerEvent(getView(), modelerFile);
    }

    protected IEventListener getEventListener() {
        return new EventListener();
    }
    
    private boolean closed = false;
    @Override
     public void cleanReference(){
         satelliteView.removeNotify();
           modelerFile.getModelerDiagramEngine().clearModelerSceneAction();
           setContextPaletteManager(null);//remove from lookup
           getView().getActionMap().clear();
           getView().getInputMap().clear();
           
        if (getPropertyManager() != null) {
            getPropertyManager().getElementPropertySet().clearGroups();//clear ElementSupportGroup
        }
        
        closed = true;
    }
}

/**
 * When the scene is clicked, sets the the focus to scene's component.
 */
class MouseClickAction extends org.netbeans.api.visual.action.WidgetAction.Adapter {

    private final Component component;

    public MouseClickAction(Component aComponent) {
        component = aComponent;
    }

    @Override
    public State mouseClicked(Widget widget, WidgetMouseEvent event) {
        System.out.println("setting focus to " + component);
        component.requestFocus();
        return State.REJECTED;
    }   
}
