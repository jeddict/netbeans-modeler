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
package org.netbeans.modeler.widget.edge;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.label.BasicLabelManager;
import org.netbeans.modeler.label.LabelManager;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.util.ModelerUtil;
import org.netbeans.modeler.specification.model.util.NModelerUtil;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOperation;
import org.openide.util.Exceptions;

/**
 *
 *
 */
public abstract class EdgeWidget extends ConnectionWidget implements IEdgeWidget {

    private IModelerScene scene;
    private LabelManager labelManager;
    private EdgeWidgetInfo edgeWidgetInfo;
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

    //private static WidgetAction deleteAction = new KeyEventLoggerAction();
//    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    public EdgeWidget(IModelerScene scene, EdgeWidgetInfo edge) {
        super((Scene) scene);
        this.setModelerScene(scene);

        this.edgeWidgetInfo = edge;

        setPaintControlPoints(true);
        setControlPointShape(PointShape.SQUARE_FILLED_SMALL);
        setRouter(scene.getRouter());
        setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        setCursor(Cursor.getDefaultCursor());//default
        setControlPointsCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        this.getModelerScene().getModelerFile().getModelerDiagramEngine().setEdgeWidgetAction(this);

        //smoothness
        setControlPointCutDistance(2);
        setForeground(new Color(120, 120, 120));

    }

    @Override
    public void createVisualPropertySet(ElementPropertySet elementPropertySet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLabel(String label) {
        getLabelManager().setLabel(label);
    }

    @Override
    public void hideLabel() {
        getLabelManager().hideLabel();
    }

    @Override
    public void showLabel() {
        getLabelManager().showLabel();
    }

    public LabelManager getLabelManager() {
        if (labelManager == null) {
            labelManager = new BasicLabelManager(this, "");
        }
        return labelManager;
    }

    @Override
    public EdgeWidgetInfo getEdgeWidgetInfo() {
        return edgeWidgetInfo;
    }

    /**
     * @return the scene
     */
    @Override
    public IModelerScene getModelerScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    @Override
    public void setModelerScene(IModelerScene scene) {
        this.scene = scene;
    }

    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuItemList = new LinkedList<JMenuItem>();
        JMenuItem delete = new JMenuItem("Delete");
        delete.setIcon(ImageUtil.getInstance().getIcon("delete.png"));
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EdgeWidget.this.remove(true);
                scene.getModelerPanelTopComponent().changePersistenceState(false);
            }
        });

        JMenuItem propsMenu = new JMenuItem("Properties");
        propsMenu.setIcon(ImageUtil.getInstance().getIcon("properties.gif"));
        propsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EdgeWidget.this.showProperties();
                EdgeWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
            }
        });

        menuItemList.add(delete);
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

   
    private BasePropertyViewManager node;
    @Override
    public void exploreProperties() {
        if(node==null){
            node = new BasePropertyViewManager((IBaseElementWidget) this);
        }
        org.netbeans.modeler.properties.util.PropertyUtil.exploreProperties(node,  this.getLabelManager() == null ? "" : this.getLabelManager().getLabel(), propertyVisibilityHandlers);
    }
    
    @Override
    public void refreshProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.refreshProperties(node,  this.getLabelManager() == null ? "" : this.getLabelManager().getLabel(), propertyVisibilityHandlers);
    }
    
    @Override
    public void showProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.showProperties(node,  this.getLabelManager() == null ? "" : this.getLabelManager().getLabel(), propertyVisibilityHandlers);
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState newState) {
        super.notifyStateChanged(previousState, newState);
    }

    @Override
    public void manageControlPoint() {
        int buffer = 10;
        EdgeWidget edge = this;
        java.util.List<Point> points = edge.getControlPoints();
        java.util.List<Point> new_points = new LinkedList<Point>();
        if (!points.isEmpty()) {
            new_points.add(points.get(0));
            for (int i = 1; i < points.size(); i++) {
                Point point_A = points.get(i - 1);
                Point point_B = points.get(i);

//               if (i == points.size() - 1) {    //this code is commented to resolve bug : when edge is aligned at last corner of node then edge control point is outside of node widget
//                   NodeWidget nodeWidget = (NodeWidget) flowEdge.getTargetFlowNodeWidget();
//                   ModelerScene modelerScene = (ModelerScene) nodeWidget.getModelerScene();
//                   Point point_Cal = Util.getComputedPoint(point_A, point_B, buffer);
//                   Point location = modelerScene.convertSceneToLocal(nodeWidget.getLocation());
//                   Rectangle bound = new Rectangle(location.x, location.y, (int) nodeWidget.getBounds().getWidth(), (int) nodeWidget.getBounds().getHeight());
//                   if (bound.contains(point_Cal)) {
//                       new_points.add(point_Cal);
//                   } else {
//                       new_points.add(point_B);
//                   }
//               } else {
                new_points.add(NBModelerUtil.getComputedPoint(point_A, point_B, buffer));
//               }

            }
            edge.setControlPoints(new_points, true);
        }
    }
    /*   public void manageControlPoint(){
     int buffer = 10;


     EdgeWidget edge = (EdgeWidget)this;
     java.util.List<Point> points = edge.getControlPoints();
     java.util.List<Point> new_points = new LinkedList<Point>();
     new_points.add(points.get(0));
     for(int i=1;i<points.size()-1;i++ ){
     Point point_A = points.get(i-1);
     Point point_B = points.get(i);
     int x = point_B.x , y =point_B.y;
     if(Math.abs(point_A.x - point_B.x) < buffer){
     x = point_A.x;
     }
     if(Math.abs(point_A.y - point_B.y) < buffer){
     y = point_A.y;
     }

     new_points.add(new Point(x,y));
     }
     new_points.add(points.get(points.size()-1));

     edge.setControlPoints(new_points, true);

     }*/
//    public void manageControlPoint(int index) {
//        int buffer = 10;
//        EdgeWidget edge = (EdgeWidget) this;
//        java.util.List<Point> points = edge.getControlPoints();
//
//        Point point_A = points.get(index - 1);
//        Point point_B = points.get(index);
//        int x = point_B.x, y = point_B.y;
//        if (Math.abs(point_A.x - point_B.x) < buffer) {
//            x = point_A.x;
//        }
//        if (Math.abs(point_A.y - point_B.y) < buffer) {
//            y = point_A.y;
//        }
//        points.set(index, new Point(x, y));
//        edge.setControlPoints(points, true);
//    }
//
//    public Point evalControlPoint(int index, Point suggestedLocation) {
//        int buffer = 10;
//        EdgeWidget edge = (EdgeWidget) this;
//        java.util.List<Point> points = new LinkedList<Point>(edge.getControlPoints());
//        Point point_A = points.get(index - 1);
//        Point point_B = points.get(index);
//        int x = point_B.x, y = point_B.y;
//        if (Math.abs(point_A.x - point_B.x) < buffer) {
//            x = point_A.x;
//        }
//        if (Math.abs(point_A.y - point_B.y) < buffer) {
//            y = point_A.y;
//        }
//
//        return new Point(x, y);
//    }
//
//

    public boolean remove() {
        return remove(false);
    }

    public boolean remove(boolean notification) {
        if (notification) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation("are you sure you want to delete this Edge?", "Delete Edge", NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
                removeEdge();
                return true;
            }
        } else {
            removeEdge();
            return true;
        }
        return false;
    }

    private void removeEdge() {
        if (!locked) {
//            this.setLabel("");
            this.hideLabel();

            ModelerUtil modelerUtil = this.getModelerScene().getModelerFile().getModelerUtil();
            if (modelerUtil instanceof NModelerUtil) {
                NModelerUtil nModelerUtil = (NModelerUtil) modelerUtil;
                nModelerUtil.dettachEdgeSourceAnchor(scene, this, (INodeWidget) this.getSourceAnchor().getRelatedWidget().getParentWidget());
                nModelerUtil.dettachEdgeTargetAnchor(scene, this, (INodeWidget) this.getTargetAnchor().getRelatedWidget().getParentWidget());
            }
            scene.deleteBaseElement((IBaseElementWidget) this);
            scene.deleteEdgeWidget(this);
            this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        }
    }

    private boolean locked = false;

    /**
     * @return the exist
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
