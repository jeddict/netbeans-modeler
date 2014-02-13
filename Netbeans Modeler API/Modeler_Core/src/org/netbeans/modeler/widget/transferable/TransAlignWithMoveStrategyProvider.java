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
package org.netbeans.modeler.widget.transferable;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.scene.AbstractModelerScene;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IModelerSubScene;
import org.netbeans.modeler.widget.context.ContextPaletteManager;

/**
 *
 */
public class TransAlignWithMoveStrategyProvider extends TransAlignWithSupport implements MoveStrategy, MoveProvider {

    private boolean outerBounds;
    private static int eventID = 0;
    private LayerWidget interactionLayer = null;
    private LayerWidget mainLayer = null;
    private ArrayList< MovingWidgetDetails> movingWidgets = null;
    private Point original;
    private boolean moveWidgetInitialized;

    public TransAlignWithMoveStrategyProvider(AlignWithWidgetCollector collector,
            LayerWidget interractionLayer,
            LayerWidget widgetLayer,
            AlignWithMoveDecorator decorator,
            boolean outerBounds) {
        super(collector, interractionLayer, decorator);
        this.outerBounds = outerBounds;
        this.interactionLayer = interractionLayer;
        this.mainLayer = widgetLayer;
        moveWidgetInitialized = false;
    }

    public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {

        if (movingWidgets.size() > 1) {
            return suggestedLocation;
        }

        Point widgetLocation = widget.getLocation();
        Rectangle widgetBounds = outerBounds ? widget.getBounds() : widget.getClientArea();
        Rectangle bounds = widget.convertLocalToScene(widgetBounds);
        bounds.translate(suggestedLocation.x - widgetLocation.x, suggestedLocation.y - widgetLocation.y);
        Insets insets = widget.getBorder().getInsets();
        if (!outerBounds) {
            suggestedLocation.x += insets.left;
            suggestedLocation.y += insets.top;
        }

        Widget parent = widget.getParentWidget();

        Point point = super.locationSuggested(widget, bounds, suggestedLocation, true, true, true, true);
        if (!outerBounds) {
            point.x -= insets.left;
            point.y -= insets.top;
        }

        Point localPt = parent.convertSceneToLocal(point);
        return localPt;
    }

    public void movementStarted(Widget widget) {
        show();

        Scene scene = widget.getScene();
        ContextPaletteManager manager = ((AbstractModelerScene) scene).getContextPaletteManager();
        if (manager != null) {
            manager.cancelPalette();
        }
        moveWidgetInitialized = false;
    }

    public void movementFinished(Widget widget) {
        hide();

        Scene scene = widget.getScene();
        if (movingWidgets != null) {
            for (MovingWidgetDetails details : movingWidgets) {
                Widget curWidget = details.getWidget();
                Point location = curWidget.getLocation();

                MoveDropTargetDropEvent dropEvent = new MoveDropTargetDropEvent(curWidget, location);
                WidgetAction.WidgetDropTargetDropEvent event = new WidgetAction.WidgetDropTargetDropEvent(++eventID, dropEvent);

                if (processLocationOperator(scene, event, location) == false) {
                    finishedOverScene(details, scene);
                }

//                if (details.getOwner() instanceof SubProcessWidget)
//                {
//                    SubProcessWidget container = (SubProcessWidget) details.getOwner();
//                    container.firePropertyChange(SubProcessWidget.CHILDREN_CHANGED, null, null);
//
//                }
                // If a widgets new owner is the same as its original owner
                // then make sure that the widget has the same index as it
                // had before the move began.
                for (MovingWidgetDetails curDetail : movingWidgets) {
                    curDetail.updateIndexIfRequired();
                }
            }

            movingWidgets.clear();
            movingWidgets = null;
        }

//        ContextPaletteManager manager = scene.getLookup().lookup(ContextPaletteManager.class);
//        if(manager != null)
//        {
//            manager.selectionChanged(null);
//        }
        original = null;
    }

    public Point getOriginalLocation(Widget widget) {

        original = widget.getPreferredLocation();
        initializeMovingWidgets(widget.getScene(), widget);

        lastPoint = original;
        if (lastPoint != null) {
            original = widget.getParentWidget().convertLocalToScene(lastPoint);
        }
        return original;
    }

    protected Point convertLocationToScene(Widget widget) {
        Point retVal = widget.getLocation();

        Widget curWidget = widget.getParentWidget();
        while (curWidget != null) {
            retVal.x += curWidget.getLocation().x;
            retVal.y += curWidget.getLocation().y;

            curWidget = curWidget.getParentWidget();
        }

        return retVal;
    }

    Point lastPoint = null;

    public void setNewLocation(Widget widget, Point location) {

        if (location != null && original != null) {
            // Determine if the new location of the widget has actually moved.
            //
            // Originally we used the dx and dy variables to determine if the
            // node moved.  However, the dx and dy values are based off the
            // original position of the widget.  In this case the "original"
            // position of the widget is defined as the location before the move
            // started.  Therefore if the widget is moved back to the exact
            // coordinate as the origional location the widget will not be
            // moved.  This is not that big of a deal when using the mouse to
            // move because it is not very likely that the exact coordinate will
            // occur in a single move.  However when using the keyboard to move
            // nodes this is very likey to happen, especially on the SQD where
            // the lifeline node can only be moved left and right.

            int dx = location.x - widget.getPreferredLocation().x;
            int dy = location.y - widget.getPreferredLocation().y;
            if (dx != 0 || dy != 0) {
                if (movingWidgets == null) {
                    //in case if this class is used only as provider and strategy isn't used
                    initializeMovingWidgets(widget.getScene(), widget);
                }

                // The dx is calcuated using a start location of when the move
                // first started.  However for connection points we do not have
                // the connection point values from before the move started.
                //
                // Therefore use the reference widget to determine the dx.
                lastPoint = location;
                adjustControlPoints(getMovingWidgetList(), dx, dy);
                for (MovingWidgetDetails details : movingWidgets) {
                    Point point = details.getWidget().getPreferredLocation();
                    if (point == null) {
                        point = details.getWidget().getLocation();
                    }

                    Point newPt = new Point(point.x + dx, point.y + dy);
                    if (details.getWidget() instanceof ConnectionWidget) {
                        // Do nothing because the adjustControlPoints will
                        // take care of this situation.
                    } else {
                        //REMOVE_PRE
                        // I do not understand what is different between the
                        // composite container and the other nodes.  However
                        // the position is always relative to the contianers
                        // original owner.
                        //
                        // Since we are at the end of the release cycle I am
                        // not wanting to do anything big here.  So special
                        // case the composite nodes.
                        if (details.getWidget() instanceof IModelerSubScene) {
                            newPt = details.getOwner().convertLocalToScene(newPt);
                        }
                        details.getWidget().setPreferredLocation(newPt);
                    }
                }

            }
        }
    }

    protected ArrayList<MovingWidgetDetails> getMovingDetails() {
        return movingWidgets;
    }

    protected List< Widget> getMovingWidgetList() {
        ArrayList< Widget> retVal = new ArrayList< Widget>();

        for (MovingWidgetDetails details : movingWidgets) {
            retVal.add(details.getWidget());
        }

        return retVal;
    }

    /**
     * Adjust the control points of all selected connection widgets attached to
     * a node.
     *
     * @param widgets The list of widgets to update.
     * @param dx The distance in the x direction.
     * @param dy The distance in the y direction.
     */
    public static void adjustControlPoints(List< Widget> widgets,
            int dx, int dy) {
        // Since child nodes are not part of the widgets (since they are moved
        // when the parent widget is moved), we need to first make sure
        // that we get not only the selected set of widgets, but also the
        // edges attached to all child nodes.  This is mostly for containers.
        List< ConnectionWidget> connections = includeAllConnections(widgets);

        ArrayList< Object> alreadyProcessed = new ArrayList< Object>();

        for (ConnectionWidget connection : connections) {
            GraphScene scene = (GraphScene) connection.getScene();
            Object data = scene.findObject(connection);

            if (alreadyProcessed.contains(data) == false) {
                if ((connection.getState().isSelected() == true)
                        || (widgets.contains(connection) == true)) {
                    List<Point> points = connection.getControlPoints();
                    for (int index = 1; index < points.size() - 1; index++) {
                        Point pt = points.get(index);
                        pt.x += dx;
                        pt.y += dy;
                    }
                }

                // Each node also needs to be revalidated so that the anchor
                // gets a chance to update the end point
                Anchor sourceAnchor = connection.getSourceAnchor();
                if (sourceAnchor != null) {
                    sourceAnchor.getRelatedWidget().revalidate();
                }

                Anchor targetAnchor = connection.getTargetAnchor();
                if (targetAnchor != null) {
                    targetAnchor.getRelatedWidget().revalidate();
                }

                alreadyProcessed.add(data);
            }
        }
    }

    private static List<ConnectionWidget> includeAllConnections(List<Widget> widgets) {
        ArrayList< ConnectionWidget> retVal = new ArrayList<ConnectionWidget>();

        for (Widget widget : widgets) {
            IModelerScene scene = (IModelerScene) widget.getScene();
            List< ConnectionWidget> connections = buildListOfConnections(scene, widget);
            if ((connections != null) && (connections.size() > 0)) {
                retVal.addAll(connections);
            }
        }

        return retVal;
    }

    private static List<ConnectionWidget> buildListOfConnections(IModelerScene scene,
            Widget widget) {
        ArrayList< ConnectionWidget> retVal = new ArrayList<ConnectionWidget>();

        // First get the edges for the passed in widget.  If the data object
        // does not represent a node the method findNodeEdges will throw an
        // assertion.  Therefore check if it is a node first.
        Object data = scene.findObject(widget);
        if ((data != null) && (scene.isNode(data) == true)) {
            // If you ask for the object of a widget it will get the object
            // of the first parent that has an associated object.  Therefore
            // we need to first make sure that the object is associated with the
            // widget in question.  Otherwise we will end up with a lot of
            // duplicates.
            if (widget.equals(scene.findWidget(data)) == true) {
                Collection edges = null;
                if (scene instanceof GraphScene) {
                    edges = ((GraphScene) scene).findNodeEdges(data, true, true);
                }
//                else {
//                    edges = ((GraphPinScene) scene).findPinEdges(data, true, true); // BUG : Development Required
//                }
                if ((edges != null) && (edges.size() > 0)) {
                    for (Object curEdge : edges) {
                        ConnectionWidget connection = (ConnectionWidget) scene.findWidget(curEdge);
                        retVal.add(connection);
                    }
                }
            }
        }

        // Second get the edges for all of the children.
        for (Widget child : widget.getChildren()) {

            List<ConnectionWidget> childConns = buildListOfConnections(scene, child);
            if ((childConns != null) && (childConns.size() > 0)) {
                retVal.addAll(childConns);
            }
        }
        return retVal;
    }

    private boolean checkIfAccepted(Widget widget,
            WidgetAction.WidgetDropTargetDropEvent event,
            Point pt) {
        boolean retVal = false;

        if (widget != null) {
            for (Widget child : widget.getChildren()) {
                Rectangle bounds = child.getBounds();
                if (bounds.contains(pt)) {

                    List<Widget> children = child.getChildren();
                    for (Widget curChild : children) {
                        if (curChild.isVisible() == true) {
                            Point childLoc = curChild.getLocation();
                            Point testPoint = new Point(pt.x - childLoc.x,
                                    pt.y - childLoc.y);
                            retVal = checkIfAccepted(curChild, event, testPoint);

                            if (retVal == true) {
                                break;
                            }
                        }
                    }

                    if (retVal == false) {
                        retVal = sendEvents(child, event, pt);
                    }
                }
            }
        }

        return retVal;
    }

    private void finishedOverScene(MovingWidgetDetails details, Scene scene) {
        Widget widget = details.getWidget();
        List<Widget> children = interactionLayer.getChildren();
        if (children != null && children.contains(widget)) {
            interactionLayer.removeChild(widget);
            mainLayer.addChild(widget);
        }

//        Lookup lookup = scene.getLookup();
//        if (lookup != null)
//        {
//            // Find out who currently owns the widgets metamodel element
//            // If a new namespace is about to own the metamodel element, then
//            // remove the model element from the curent namespace.
//            INamedElement element = null;
//            if (scene instanceof ObjectScene)
//            {
//                ObjectScene objScene = (ObjectScene) scene;
//
//                Object data = objScene.findObject(widget);
//                if (data instanceof IPresentationElement)
//                {
//                    IPresentationElement presentation = (IPresentationElement) data;
//                    if(presentation.getFirstSubject() instanceof INamedElement)element = (INamedElement) presentation.getFirstSubject();
//                }
//
//                // Handle the namespace change.  The container widget will
//                // handle adding a child to the containers namespace.
//                //
//                // We have to have to handle the case of when the widget
//                // is moved from a container to the scene.
//                if (details.getOwner() instanceof ContainerWidget && element!=null)
//                {
//                    IDiagram diagram = lookup.lookup(IDiagram.class);
//                    if (diagram != null)
//                    {
//                        INamespace space = diagram.getNamespace();
//                        INamespace curSpace = element.getOwningPackage();
//
//                        if (space.equals(curSpace) == false)
//                        {
//                            curSpace.removeOwnedElement(element);
//                            space.addOwnedElement(element);
//                        }
//                    }
//                }
//            }
//        }
    }

    private void initializeMovingWidgets(Scene scene, Widget widget) {
        if (movingWidgets != null) {
            // We should never be here;
            movingWidgets.clear();
            movingWidgets = null;
        }
        movingWidgets = new ArrayList< MovingWidgetDetails>();

        if (scene instanceof GraphScene) {
            GraphScene gscene = (GraphScene) scene;
            Object object = gscene.findObject(widget);
            if (gscene.isNode(object)) {
                Set< ?> selected = gscene.getSelectedObjects();
                for (Object o : selected) {
                    if ((gscene.isNode(o)) && (isOwnerSelected(o, selected, gscene) == false)) {
                        Widget w = gscene.findWidget(o);
                        if (w != null) {
                            Point pt = w.getPreferredLocation();
                            Widget owner = w.getParentWidget();
                            if (owner != null) {
                                pt = owner.convertLocalToScene(pt);
                                w.setPreferredLocation(pt);
                            }

                            MovingWidgetDetails details = new MovingWidgetDetails(w, owner, pt);
                            movingWidgets.add(details);

                            for (ConnectionWidget c : NBModelerUtil.getAllContainedEdges(w)) {
                                movingWidgets.add(new MovingWidgetDetails(c,
                                        c.getParentWidget(), c.getParentWidget().convertLocalToScene(c.getFirstControlPoint())));
                            }

                            if (details.getOwner() != null) {
                                details.getOwner().removeChild(w);
                            }
                            interactionLayer.addChild(w);
                        }
                    }
                }
                //need to sort in order to return back properly without indexOutOfBounds
                Collections.sort(movingWidgets, new Comparator<MovingWidgetDetails>() {
                    public int compare(TransAlignWithMoveStrategyProvider.MovingWidgetDetails o1, TransAlignWithMoveStrategyProvider.MovingWidgetDetails o2) {
                        return o1.getOriginalIndex() - o2.getOriginalIndex();
                    }
                });
            } else {
                MovingWidgetDetails details = new MovingWidgetDetails(widget, widget.getParentWidget(), widget.getPreferredLocation());

                movingWidgets.add(details);
            }
        }
        moveWidgetInitialized = true;
    }

    public boolean isMovementInitialized() {
        return moveWidgetInitialized;
    }

    private boolean isOwnerSelected(Object o,
            Set<?> selected,
            GraphScene gscene) {
        boolean retVal = false;

        Widget widget = gscene.findWidget(o);
        if (widget != null) {
            Widget parent = widget.getParentWidget();
            Object parentObj = gscene.findObject(parent);

            if (parentObj != null) {
                if (selected.contains(parentObj) == true) {
                    retVal = true;
                } else {
                    retVal = isOwnerSelected(parentObj, selected, gscene);
                }
            }
        }

        return retVal;
    }

    private boolean processLocationOperator(Widget widget,
            WidgetAction.WidgetDropTargetDropEvent event,
            Point cursorSceneLocation) {
        Scene scene = widget.getScene();
        Point location = scene.getLocation();
        return processLocationOperator2(scene, event, new Point(cursorSceneLocation.x + location.x, cursorSceneLocation.y + location.y));
    }

    private boolean processLocationOperator2(Widget widget,
            WidgetAction.WidgetDropTargetDropEvent event,
            Point point) {
        boolean retVal = false;

        if (!widget.isVisible()) {
            return false;
        }

        Point location = widget.getLocation();
        point.translate(-location.x, -location.y);

        Rectangle bounds = widget.getBounds();
        if (bounds.contains(point)) {
            List<Widget> children = widget.getChildren();
            Widget[] childrenArray = children.toArray(new Widget[children.size()]);

            for (int i = childrenArray.length - 1; i >= 0; i--) {
                if (processLocationOperator2(childrenArray[i], event, point) == true) {
                    retVal = true;
                    break;
                }
            }

            if ((retVal == false) && (widget.isHitAt(point) == true)) {
                retVal = sendEvents(widget, event, point);
            }
        }

        point.translate(location.x, location.y);
        return retVal;
    }

    private boolean sendEvents(Widget target,
            WidgetAction.WidgetDropTargetDropEvent event,
            Point pt) {
        boolean retVal = false;

        if (target != null) {
            if (sendEvents(target.getActions(), target, event) == false) {
                String tool = target.getScene().getActiveTool();
                retVal = sendEvents(target.getActions(tool), target, event);
            } else {
                retVal = true;
            }
        }

        return retVal;
    }

    private boolean sendEvents(WidgetAction.Chain actions,
            Widget target,
            WidgetAction.WidgetDropTargetDropEvent event) {
        boolean retVal = false;

        if (actions != null) {
            for (WidgetAction action : actions.getActions()) {
                if (action.drop(target, event) == WidgetAction.State.CONSUMED) {
                    retVal = true;
                    break;
                }
            }
        }

        return retVal;
    }

    private class MovingWidgetDetails {

        private Widget widget = null;
        private Widget owner = null;
        private Point originalLocation = null;
        private int originalIndex = -1;

        public MovingWidgetDetails(Widget widget,
                Widget owner,
                Point location) {
            this.widget = widget;
            this.owner = owner;
            this.originalLocation = location;

            if (owner != null) {
                originalIndex = owner.getChildren().indexOf(widget);
            }
        }

        public Point getOriginalLocation() {
            return originalLocation;
        }

        public Widget getOwner() {
            return owner;
        }

        public Widget getWidget() {
            return widget;
        }

        public int getOriginalIndex() {
            return originalIndex;
        }

        public void updateIndexIfRequired() {
            if (owner.equals(widget.getParentWidget())) {
                owner.removeChild(widget);
                //Adjust the original index.
                owner.addChild(--originalIndex < 0 ? 0 : originalIndex, widget);
            }
        }
    }
}
