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
package org.netbeans.modeler.scene;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphScene;
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
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.INModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.widget.context.ContextPaletteManager;
import org.netbeans.modeler.widget.context.SwingPaletteManager;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOperation;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;

/**
 *
 *
 */
public abstract class AbstractModelerScene extends GraphScene<NodeWidgetInfo, EdgeWidgetInfo> implements INModelerScene {

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
    private IModelerDiagramEngine modelerDiagramEngine;
    // Data
    private ContextPaletteManager paletteManager = null;
    // Lookup Data
    private InstanceContent lookupContent = new InstanceContent();
//    private AbstractLookup lookup = new AbstractLookup(lookupContent);
    private ArrayList< IFlowElementWidget> lockedSelected = new ArrayList< IFlowElementWidget>();
    private INodeWidget validNodeWidget;
    private INodeWidget invalidNodeWidget;
    private boolean alignSupport = true;

    //  private UltraSimpleIdGenerator idGenerator;
    //private List<NodeWidget> nodeWidgets = new ArrayList<NodeWidget>();
    public AbstractModelerScene() {

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

        router = RouterFactory.createFreeRouter();
//          router = RouterFactory.createOrthogonalSearchRouter(mainLayer);

        satelliteView = this.createSatelliteView();

        //  idGenerator = new UltraSimpleIdGenerator();
        setActiveTool(DesignerTools.SELECT);
// Zoom Action
//        getActions ().addAction (ActionFactory.createZoomAction (1.05, true));
//        getActions ().addAction (ActionFactory.createPanAction ());
        //multi move rectangular selection of widgets
        // getActions ().addAction (ActionFactory.createRectangularSelectAction (this, backgroundLayer));

//     getPriorActions().addAction(new WidgetAction.Adapter () {
//            @Override
//            public State mousePressed(Widget widget, WidgetMouseEvent event) {
//                getView().requestFocus();
//                return State.REJECTED;
//            }
//        });
        this.setContextPaletteManager(new SwingPaletteManager((IModelerScene) this));

    }

    public void init(ModelerFile file) {
        this.setModelerFile(file);
        this.modelerPanel = file.getModelerPanelTopComponent();
        this.modelerDiagramEngine = file.getModelerDiagramEngine();
        modelerDiagramEngine.setModelerSceneAction();
    }

    @Override
    protected void attachEdgeSourceAnchor(EdgeWidgetInfo edgeWidgetInfo, NodeWidgetInfo oldSourceNode, NodeWidgetInfo sourceNodeInfo) {
        IEdgeWidget edgeWidget = (IEdgeWidget) findWidget(edgeWidgetInfo);
        INodeWidget sourceNodeWidget = (INodeWidget) findWidget(sourceNodeInfo);
        NBModelerUtil.attachEdgeSourceAnchor((IModelerScene) this, edgeWidget, sourceNodeWidget); // TBF_CODE (ModelerScene)
    }

    @Override
    protected void attachEdgeTargetAnchor(EdgeWidgetInfo edgeWidgetInfo, NodeWidgetInfo oldTargetNode, NodeWidgetInfo targetNodeInfo) {
        IEdgeWidget edgeWidget = (IEdgeWidget) findWidget(edgeWidgetInfo);
        INodeWidget targetNodeWidget = (INodeWidget) findWidget(targetNodeInfo);
        NBModelerUtil.attachEdgeTargetAnchor((IModelerScene) this, edgeWidget, targetNodeWidget);
        if (edgeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) edgeWidget).init();
        }
    }

    @Override
    protected Widget attachNodeWidget(NodeWidgetInfo widgetInfo) {
        Widget widget = (Widget) this.getModelerFile().getModelerUtil().attachNodeWidget(this, widgetInfo);
        this.getMainLayer().addChild(widget);
        this.setFocusedWidget(widget);
        this.validate();
        if (widget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) widget).setId(widgetInfo.getId());
            this.createBaseElement((IBaseElementWidget) widget);
        }
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(EdgeWidgetInfo widgetInfo) {
        IEdgeWidget edgeWidget = this.getModelerFile().getModelerUtil().attachEdgeWidget(this, widgetInfo);
        this.getConnectionLayer().addChild((Widget) edgeWidget);
        edgeWidget.setEndPointShape(PointShape.SQUARE_FILLED_SMALL);
        edgeWidget.setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
        edgeWidget.setRouter(getRouter());
        this.setFocusedWidget((Widget) edgeWidget);
        this.validate();
        if (edgeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) edgeWidget).setId(widgetInfo.getId());
            this.createBaseElement((IBaseElementWidget) edgeWidget);
        }

        return (Widget) edgeWidget;

    }

    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        if (state.isSelected()) {
            this.getContextPaletteManager().cancelPalette();
        }

//      //System.out.println("state.isSelected() : " + state.isSelected());
//      //System.out.println("state.isFocused() : " + state.isFocused());
//      //System.out.println("state.isHighlighted() : " + state.isHighlighted());
//      //System.out.println("state.isHovered() : " + state.isHovered());
//
//
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

//    public void loadWidgetsFromXML() {
//        JFileChooser chooser = new JFileChooser ();
//        chooser.setDialogTitle ("Load Scene From XML");
//        chooser.setDialogType (JFileChooser.OPEN_DIALOG);
//        chooser.setMultiSelectionEnabled (false);
//        chooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
//        chooser.setFileFilter (new FileFilter() {
//            public boolean accept (File file) {
//                if (file.isDirectory ())
//                return true;
//                return file.getName ().toLowerCase ().endsWith (".xml"); // NOI18N
//            }
//            public String getDescription () {
//                return "Extensible Markup Language (.xml)"; // NOI18N
//            }
//        });
//        if (chooser.showSaveDialog (new JFrame()) != JFileChooser.APPROVE_OPTION)
//            return;
//
//        File file = chooser.getSelectedFile ();
//
//        WidgetsXML wxml = new WidgetsXML(file);
//        wxml.prepareToLoad();
//
//        ArrayList<NodeWidgetInfo> myNodes = wxml.getMyNodes();
//        for(int i=0; i<myNodes.size(); i++) {
//            NodeWidgetInfo node = myNodes.get(i);
//            Widget w = Scene.this.addNode(node);
//            getSceneAnimator().animatePreferredLocation(w, w.convertLocalToScene(node.getLocation()));
//        }
//
//        ArrayList<SequenceWidgetInfo> myEdges = wxml.getMyEdges();
//        NodeWidgetInfo source = null;
//        NodeWidgetInfo target = null;
//        for(int i=0; i<myEdges.size(); i++) {
//            SequenceWidgetInfo edge = myEdges.get(i);
//
//            for(int j=0; j<myNodes.size(); j++) {
//                NodeWidgetInfo n = myNodes.get(j);
//
//                if(n.getId().equals(edge.getSource()))
//                    source = n;
//                if(n.getId().equals(edge.getTarget()))
//                    target = n;
//            }
//
//            addEdge(edge);
//            setEdgeSource(edge, source);
//            setEdgeTarget(edge, target);
//        }
//    }
    @Override
    public void paintChildren() {
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

    public JComponent getSatelliteView() {
        return satelliteView;
    }

    public void setContextPaletteManager(ContextPaletteManager manager) {
        if (paletteManager != null) {
            removeFromLookup(paletteManager);
        }

        paletteManager = manager;
        addToLookup(paletteManager);
    }

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
    public IModelerPanel getModelerPanelTopComponent() {
        return modelerPanel;
    }

    /**
     * @param topComponent the topComponent to set
     */
    public void setModelerPanelTopComponent(IModelerPanel topComponent) {
        this.modelerPanel = topComponent;
    }

//    /**
//     * @return the idGenerator
//     */
//    public UltraSimpleIdGenerator getIdGenerator() {
//        return idGenerator;
//    }
//
//    /**
//     * @param idGenerator the idGenerator to set
//     */
//    public void setIdGenerator(UltraSimpleIdGenerator idGenerator) {
//        this.idGenerator = idGenerator;
//    }
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
        //boolean entirely = sceneSelection.width > 0;
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

//    /**
//     * @return the nodeWidgets
//     */
//    public List<NodeWidget> getNodeWidgets() {
//        return nodeWidgets;
//    }
//
//    /**
//     * @param nodeWidgets the nodeWidgets to set
//     */
//    public void setNodeWidgets(List<NodeWidget> nodeWidgets) {
//        this.nodeWidgets = nodeWidgets;
//    }
//
//      public void addNodeWidget(NodeWidget nodeWidget) {
//        this.nodeWidgets.add(nodeWidget);
//    }
    /**
     * @return the engine
     */
    public IModelerDiagramEngine getModelerDiagramEngine() {
        return modelerDiagramEngine;
    }

//    /**
//     * @return the name
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     * @param name the name to set
//     */
//    public void setName(String name) {
//        this.name = name;
//    }
//
    /**
     * @return the router
     */
    public Router getRouter() {
        return router;
    }

    /**
     * @param router the router to set
     */
    public void setRouter(Router router) {
        this.router = router;
    }

    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuItemList = new LinkedList<JMenuItem>();

        JMenu routeMenu = new JMenu("Router");
//        position.setIcon(ImageUtil.getInstance().getIcon("position.png"));
        final JRadioButtonMenuItem freeRoute = new JRadioButtonMenuItem("Free Design");
        final JRadioButtonMenuItem autoRoute = new JRadioButtonMenuItem("Auto Route");

//        freeRoute.setIcon(ImageUtil.getInstance().getIcon("front.png"));
        freeRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                freeRoute.setSelected(true);
                autoRoute.setSelected(false);
                router = RouterFactory.createFreeRouter();
                for (Widget widget : AbstractModelerScene.this.connectionLayer.getChildren()) {
                    if (widget instanceof ConnectionWidget) {
                        ((ConnectionWidget) widget).setRouter(router);
                    }
                }
            }
        });
        routeMenu.add(freeRoute);

//        autoRoute.setIcon(ImageUtil.getInstance().getIcon("back.png"));
        autoRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                freeRoute.setSelected(false);
                autoRoute.setSelected(true);
                router = RouterFactory.createOrthogonalSearchRouter(mainLayer);
                for (Widget widget : AbstractModelerScene.this.connectionLayer.getChildren()) {
                    if (widget instanceof ConnectionWidget) {
                        ((ConnectionWidget) widget).setRouter(router);
                    }
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

        menuItemList.add(routeMenu);

        JCheckBoxMenuItem alignMenu = new JCheckBoxMenuItem("Align Support");
        alignMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (AbstractModelerScene.this.isAlignSupport()) {
                    setAlignSupport(false);
                } else {
                    setAlignSupport(true);
                }
            }
        });
        alignMenu.setSelected(true);

        menuItemList.add(alignMenu);

        JMenuItem propsMenu = new JMenuItem("Properties");
        propsMenu.setIcon(ImageUtil.getInstance().getIcon("properties.gif"));
        propsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractModelerScene.this.showProperties();
                AbstractModelerScene.this.getModelerPanelTopComponent().changePersistenceState(false);
            }
        });

        menuItemList.add(propsMenu);

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
        popupMenuProvider = new PopupMenuProvider() {
            @Override
            public JPopupMenu getPopupMenu(final Widget widget, final Point location) {
                return popupMenu;
            }
        };

        return popupMenuProvider;
    }

    public void showProperties() {
        NodeOperation.getDefault().showProperties(getNode());
    }

    public void exploreProperties() {
        AbstractNode currentNode = getNode();
        if (modelerPanel.getExplorerManager().getRootContext() != currentNode) {
            modelerPanel.getExplorerManager().setRootContext(currentNode);
            try {
                modelerPanel.getExplorerManager().setSelectedNodes(
                        new Node[]{currentNode});
            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            }

            modelerPanel.setActivatedNodes(new Node[]{currentNode});
        }
    }
    private AbstractNode node;

    public AbstractNode getNode() {
        if (node == null) {
            node = new BasePropertyViewManager((IBaseElementWidget) this);
        }
        BasePropertyViewManager baseNode = (BasePropertyViewManager) node;
        for (Node.PropertySet propertySet : baseNode.getPropertySets()) {
            for (Node.Property property : propertySet.getProperties()) {
                if (property.getClass() == NEntityPropertySupport.class) {
                    NEntityPropertySupport attributeProperty = (NEntityPropertySupport) property;
                    attributeProperty.getAttributeEntity().getTableDataListener().initCount();
                }
            }
        }
        return node;
    }

    public void setNode(AbstractNode node) {
        this.node = node;
    }

    /**
     * @return the validNodeWidget
     */
    public INodeWidget getValidNodeWidget() {
        return validNodeWidget;
    }

    /**
     * @param validNodeWidget the validNodeWidget to set
     */
    public void setValidNodeWidget(INodeWidget validNodeWidget) {
        this.validNodeWidget = validNodeWidget;
    }

    /**
     * @return the invalidNodeWidget
     */
    public INodeWidget getInvalidNodeWidget() {
        return invalidNodeWidget;
    }

    /**
     * @param invalidNodeWidget the invalidNodeWidget to set
     */
    public void setInvalidNodeWidget(INodeWidget invalidNodeWidget) {
        this.invalidNodeWidget = invalidNodeWidget;
    }

    /**
     * @return the boundaryWidgetLayer
     */
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
        INodeWidget nodeWidget = (INodeWidget) this.addNode(node);
        if (nodeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) nodeWidget).init();
        }
        return nodeWidget;
    }

    @Override
    public IEdgeWidget createEdgeWidget(EdgeWidgetInfo edge) {
        IEdgeWidget edgeWidget = (IEdgeWidget) this.addEdge(edge);
        if (edgeWidget instanceof IBaseElementWidget) {
            if (edgeWidget instanceof IFlowEdgeWidget) {
                ((IFlowEdgeWidget) edgeWidget).setName(edge.getName());
            }
//            ((IBaseElementWidget) edgeWidget).init();
        }
        return edgeWidget;
    }

    @Override
    public void deleteNodeWidget(INodeWidget nodeWidget) {

        this.removeNode(nodeWidget.getNodeWidgetInfo());
        if (nodeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) nodeWidget).destroy();
        }
    }

    @Override
    public void deleteEdgeWidget(IEdgeWidget edgeWidget) {

        this.removeEdge(edgeWidget.getEdgeWidgetInfo());
        if (edgeWidget instanceof IBaseElementWidget) {
            ((IBaseElementWidget) edgeWidget).destroy();
        }
    }

    @Override
    public void setEdgeWidgetSource(EdgeWidgetInfo edge, NodeWidgetInfo node) {
        this.setEdgeSource(edge, node);
    }

    @Override
    public void setEdgeWidgetTarget(EdgeWidgetInfo edge, NodeWidgetInfo node) {
        this.setEdgeTarget(edge, node);
    }

    /**
     * @return the alignSupport
     */
    public boolean isAlignSupport() {
        return alignSupport;
    }

    /**
     * @param alignSupport the alignSupport to set
     */
    public void setAlignSupport(boolean alignSupport) {
        this.alignSupport = alignSupport;
    }

    /**
     * @return the labelLayer
     */
    public LayerWidget getLabelLayer() {
        return labelLayer;
    }

    /**
     * @param labelLayer the labelLayer to set
     */
    public void setLabelLayer(LayerWidget labelLayer) {
        this.labelLayer = labelLayer;
    }

    public void autoLayout() {
        SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(this, new GridGraphLayout<NodeWidgetInfo, EdgeWidgetInfo>().setChecker(true));
        sceneLayout.invokeLayout();
    }
}
