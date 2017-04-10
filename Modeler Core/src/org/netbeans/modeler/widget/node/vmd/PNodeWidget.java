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
package org.netbeans.modeler.widget.node.vmd;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.border.ResizeBorder;
import org.netbeans.modeler.border.RoundResizeBorder;
import org.netbeans.modeler.config.document.BoundsConstraint;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import static org.netbeans.modeler.core.engine.ModelerDiagramEngine.cleanActions;
import org.netbeans.modeler.label.LabelInplaceEditor;
import org.netbeans.modeler.label.inplace.InplaceEditorAction;
import org.netbeans.modeler.label.inplace.TextFieldInplaceEditorProvider;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.properties.view.manager.IPropertyManager;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IPModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.specification.model.document.widget.IModelerSubScene;
import org.netbeans.modeler.widget.node.NodeWidgetStatus;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.node.vmd.internal.AbstractPNodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.netbeans.modeler.widget.transferable.cp.WidgetTransferable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;

/**
 *
 *
 * @param <S> IModelerScene implementation
 */
public abstract class PNodeWidget<S extends IModelerScene> extends AbstractPNodeWidget {

    /**
     * internalPinWidgetInfo is the pin widget that is not visible , helps to connect other pin directly with the node
     */
    private PinWidgetInfo internalPinWidgetInfo;
    public static final int WIDGET_BORDER_PADDING = 4;
    private static final ResizeProvider.ControlPoint[] RECTANGLE_RESIZE_BORDER_DISABLE_POINT = new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.BOTTOM_LEFT, ResizeProvider.ControlPoint.TOP_LEFT, ResizeProvider.ControlPoint.TOP_RIGHT, ResizeProvider.ControlPoint.TOP_CENTER, ResizeProvider.ControlPoint.CENTER_LEFT};
    private static final ResizeProvider.ControlPoint[] RECTANGLE_RESIZE_BORDER_ENABLE_POINT = new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.BOTTOM_CENTER, ResizeProvider.ControlPoint.BOTTOM_RIGHT, ResizeProvider.ControlPoint.CENTER_RIGHT};
    public static final ResizeBorder RECTANGLE_RESIZE_BORDER = new RoundResizeBorder(WIDGET_BORDER_PADDING, new Color(242, 132, 0), RECTANGLE_RESIZE_BORDER_ENABLE_POINT, RECTANGLE_RESIZE_BORDER_DISABLE_POINT, false, 0, 0, true, new Color(242, 132, 0));
    private ResizeBorder widgetBorder;
    private NodeWidgetStatus status;
    private final NodeWidgetInfo nodeWidgetInfo;
    private boolean activeStatus = true;
    private boolean highlightStatus = false;
    private boolean anchorState = false;
    private final Map<String, PropertyChangeListener> propertyChangeHandlers = new HashMap<>();

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
    private final Map<String, PropertyVisibilityHandler> propertyVisibilityHandlers = new HashMap<>();

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

    public PNodeWidget(S scene, NodeWidgetInfo nodeWidgetInfo) {
        super((Scene) scene, ((IPModelerScene) scene).getColorScheme(), nodeWidgetInfo.getNodeDesign());
        this.scene = scene;
        this.nodeWidgetInfo = nodeWidgetInfo;
        setAnchorGap(0);

        WidgetAction editAction = new InplaceEditorAction<>(new TextFieldInplaceEditorProvider(new LabelInplaceEditor((Widget) this), null));
        getNodeNameWidget().getActions().addAction(editAction);
        getHeader().getActions().addAction(scene.createObjectHoverAction());

        setWidgetBorder(this.getModelerScene().getModelerFile().getModelerUtil().getNodeBorder(this));
    }

    // Alternative to AbstractPModelerScene createPinWidget
    @Override
    public IPinWidget createPinWidget(PinWidgetInfo pinWidgetInfo) {
        return ((IPModelerScene) this.getModelerScene()).createPinWidget(nodeWidgetInfo, pinWidgetInfo);
    }

    @Override
    public void deletePinWidget(IPinWidget pinWidget) {
        ((IPModelerScene) this.getModelerScene()).deletePinWidget(pinWidget);
    }

    @Override
    public void setLabel(String label) {
        setNodeName(label);
    }

    @Override
    public String getLabel() {
        return getNodeName();
    }

    @Override
    public void hideLabel() {
//         getLabelManager().hideLabel();
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showLabel() {
//        getLabelManager().showLabel();
//        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public NodeWidgetInfo getNodeWidgetInfo() {
        return nodeWidgetInfo;
    }

//    @Override
//    public void notifyStateChanged(ObjectState previousState, ObjectState newState) {
//        super.notifyStateChanged(previousState, newState);
//        if (this.getNodeWidgetInfo() != null) {
//            if (this instanceof IModelerSubScene) {
//                this.bringToBack();
//            }
//            if (newState.isSelected()) {
//                getModelerScene().manageLayerWidget();
//                showResizeBorder();
//            } else if (newState.isHovered()) {
//                hoverWidget();
//            } else {
//                unhoverWidget();
//                hideResizeBorder();
//            }
//        }
//
//    }
    @Override
    public void showResizeBorder() {
        Border border = this.getModelerScene().getModelerFile().getModelerUtil().getNodeBorder(this);
        if (border != null) {
            this.setBorder(border);
        }
    }

    @Override
    public void hideResizeBorder() {
        this.setBorder(DEFAULT_BORDER);
    }

    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuItemList = new LinkedList<>();
//        JMenuItem visualProperty = new JMenuItem(I18n.getString("Customize"));
//        visualProperty.setIcon(ImageUtil.getInstance().getIcon("customize.png"));
//        visualProperty.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                AbstractNode node = new VisualPropertyViewManager((IBaseElementWidget) PNodeWidget.this);
//                NodeOperation.getDefault().showProperties(node);
//                PNodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
//
//            }
//        });

        JMenuItem delete = new JMenuItem("Delete");
        delete.setIcon(ImageUtil.getInstance().getIcon("delete.png"));
        delete.addActionListener((ActionEvent e) -> {
            PNodeWidget.this.remove(true);
            PNodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });

        
        menuItemList.add(delete);
        menuItemList.add(null);
        menuItemList.add(getCopyMenu());
        menuItemList.add(getPasteMenu());
        menuItemList.add(getPropertyMenu());
        return menuItemList;
    }
    
    protected JMenuItem getCopyMenu() {
        JMenuItem copyProperty = new JMenuItem("Copy");
        copyProperty.addActionListener(e -> {
            WidgetTransferable.copy(PNodeWidget.this);
        });
        return copyProperty;
    }

    protected JMenuItem getPasteMenu() {
        JMenuItem pasteProperty = new JMenuItem("Paste");
        pasteProperty.addActionListener(e -> {
            WidgetTransferable.paste((IBaseElementWidget)PNodeWidget.this);
        });
        return pasteProperty;
    }
    
    protected JMenuItem getPropertyMenu(){
        JMenuItem baseProperty = new JMenuItem("Properties");
        baseProperty.setIcon(ImageUtil.getInstance().getIcon("properties.gif"));
        baseProperty.addActionListener((ActionEvent e) -> {
            PNodeWidget.this.showProperties();
            PNodeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });
        return baseProperty;
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
        org.netbeans.modeler.properties.util.PropertyUtil.exploreProperties(node, this.getNodeName(), propertyVisibilityHandlers);
    }
    
    public IPropertyManager getPropertyManager(){
        return node;
    }

    @Override
    public void refreshProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.refreshProperties(node, this.getNodeName(), propertyVisibilityHandlers);
    }

    @Override
    public void showProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.showProperties(node, this.getNodeName(), propertyVisibilityHandlers);
    }


    /*----------------------------------------*/
    String getColorString(Color color) {
        if (color != null) {
            return "RGB(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
        } else {
            return "RGB(155,155,155)";
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

//        if (status == NodeWidgetStatus.INVALID & this.getStatus() != NodeWidgetStatus.INVALID) {
//            this.getModelerScene().setInvalidNodeWidget(this);
////            changeBorderColor(new Color(223, 35, 0));
//        } else if (status == NodeWidgetStatus.VALID & this.getStatus() != NodeWidgetStatus.VALID) {
//            this.getModelerScene().setValidNodeWidget(this);
////               changeBorderColor(new Color(81, 141, 4));
//        } else if (status == NodeWidgetStatus.ACCEPT & this.getStatus() != NodeWidgetStatus.ACCEPT) {
////            changeBorderColor(new Color(0, 162, 232));
//        } else if (NodeWidgetStatus.NONE == status & this.getStatus() != NodeWidgetStatus.NONE) {
//            if (this.getModelerScene().getValidNodeWidget() == this) {
//                this.getModelerScene().setValidNodeWidget(null);
////                defaultBorderColor();
//            } else if (this.getModelerScene().getInvalidNodeWidget() == this) {
//                this.getModelerScene().setInvalidNodeWidget(null);
////                defaultBorderColor();
//            } else {
////                defaultBorderColor();
//            }
//        } else {
////            this.setBorder(DEFAULT_BORDER);
//        }
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
        return this.convertLocalToScene(this.getBounds());
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
            this.setLabel("");
            this.hideLabel();
            if (((IFlowNodeWidget) this).getFlowElementsContainer() instanceof IModelerSubScene) {
                IModelerSubScene modelerSubScene = (IModelerSubScene) ((IFlowNodeWidget) this).getFlowElementsContainer();
                modelerSubScene.deleteBaseElement((IBaseElementWidget) this);
            } else {
                scene.deleteBaseElement((IBaseElementWidget) this);
            }
            List<Widget> children = new ArrayList<>(this.getChildren());//PinWidget children lost after NodeWidget deleteNodeWidget method called
            scene.deleteNodeWidget(this);
            scene.getModelerPanelTopComponent().changePersistenceState(false);
            cleanReference(children);
        }else {
            System.out.println("");
        }
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
    public ElementPropertySet createVisualInnerPropertiesSet(ElementPropertySet elementPropertySet) throws NoSuchMethodException, NoSuchFieldException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ElementPropertySet createVisualOuterPropertiesSet(ElementPropertySet elementPropertySet) throws NoSuchMethodException, NoSuchFieldException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the internalPinWidget
     */
    @Override
    public PinWidgetInfo getInternalPinWidgetInfo() {
        return internalPinWidgetInfo;
    }

    /**
     * @param internalPinWidgetInfo the internalPinWidget to set
     */
    @Override
    public void setInternalPinWidgetInfo(PinWidgetInfo internalPinWidgetInfo) {
        this.internalPinWidgetInfo = internalPinWidgetInfo;
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

    private int anchorGap;

    @Override
    public int getAnchorGap() {
        return anchorGap;
    }

    @Override
    public void setAnchorGap(int anchorGap) {
        this.anchorGap = anchorGap;
    }

    /**
     * @return the highlightStatus
     */
    @Override
    public boolean isHighlightStatus() {
        return highlightStatus;
    }

    /**
     * @param highlightStatus the highlightStatus to set
     */
    @Override
    public void setHighlightStatus(boolean highlightStatus) {
        this.highlightStatus = highlightStatus;
    }
    
    @Override
    public void cleanReference() {
         cleanReference(this.getChildren());
    }
    
    
    private void cleanReference(List<Widget> children) { //PinWidget children lost after NodeWidget remove method called
        if (this.getPropertyManager() != null) {
            this.getPropertyManager().getElementPropertySet().clearGroups();//clear ElementSupportGroup
        }
        for (Widget childWidget : children) {
            if (childWidget instanceof IPinWidget) {
                ((IPinWidget) childWidget).cleanReference();
            }
        }
        this.getModelerScene().getModelerFile().getModelerDiagramEngine().clearNodeWidgetAction(this);
        cleanActions(getNodeNameWidget().getActions());
        cleanActions(getHeader().getActions());
        cleanActions(getMinimizeButton().getActions());
        cleanActions(getImageWidget().getActions());

    }
    
      public boolean isValidPinWidget(SubCategoryNodeConfig subCategoryInfo){
        return true;
    }
}
