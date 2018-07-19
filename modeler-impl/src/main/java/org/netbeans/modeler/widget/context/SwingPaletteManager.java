/**
 * Copyright 2013-2018 Gaurav Gupta
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
package org.netbeans.modeler.widget.context;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetMouseEvent;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.context.ui.ContextPalette;
import org.netbeans.modeler.widget.node.INNodeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.IWidget;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;

/**
 *
 *
 */
public class SwingPaletteManager implements ContextPaletteManager {

    public static final int SPACE_FROM_WIDGET = 5;

    private IModelerScene scene = null;
    private JComponent decoratorLayer = null;
    private ContextPalette paletteWidget = null;
    private FollowCursorAction followAction = new FollowCursorAction();
    private FollowCursorLeftRightAction followLRAction = new FollowCursorLeftRightAction();

    //if palette is cancelled and reappears it's good to put to the same position (have sense for follow case)
    private IWidget cancelledWidget;

    public SwingPaletteManager(IModelerScene scene) {
        this(scene, null);

        if (scene.getView() == null) {
            setDecoratorLayer(scene.createView());
        } else {
            setDecoratorLayer(scene.getView());
        }
    }

    public SwingPaletteManager(IModelerScene scene, JComponent layer) {
        setScene(scene);
        setDecoratorLayer(layer);
    }

    ///////////////////////////////////////////////////////////////
    // ContextPaletteManager implementation
    /**
     * Changes the palette to represent the select widget. If more than one
     * widget is selected, or no widgets are selected then the palette is
     * removed.
     *
     * @param scenePoint Specifies the location to place the palette. If null is
     * specified the best location if determined.
     */
    @Override
    public void selectionChanged(IWidget selectedWidget, Point scenePoint) {
        selectionChanged(selectedWidget, scenePoint, false);
    }

    /**
     * Changes the palette to represent the select widget. If more than one
     * widget is selected, or no widgets are selected then the palette is
     * removed.
     *
     * @param scenePoint Specifies the location to place the palette. If null is
     * specified the best location if determined.
     * @param forceShow If true the palette will be show even if more than one
     * widget is selected.
     */
    protected void selectionChanged(IWidget widget, Point scenePoint, boolean forceShow) {
        //  //System.out.println("INSIDE selectionChanged(Widget selectedWidget,Point scenePoint, boolean forceShow)");
        // Clear the previous palette
        cancelPalette();

        IWidget selectedWidget = widget;

        cancelledWidget = selectedWidget;

        if (selectedWidget != null) {
            showPaletteFor(selectedWidget);

            ContextPaletteModel.FOLLOWMODE follow = ContextPaletteModel.FOLLOWMODE.NONE;
            if (paletteWidget != null) {
                follow = paletteWidget.getModel().getFollowMouseMode();
            }

            if (follow != ContextPaletteModel.FOLLOWMODE.NONE) {
                String activeTool = getScene().getActiveTool();
                WidgetAction.Chain actions = selectedWidget.createActions(activeTool);

                if (follow == ContextPaletteModel.FOLLOWMODE.VERTICAL_ONLY) {
                    actions.addAction(followAction);
                } else if (follow == ContextPaletteModel.FOLLOWMODE.VERTICAL_AND_HORIZONTAL) {
                    actions.addAction(followLRAction);
                }

                if (scenePoint != null && paletteWidget != null) {
                    //TBD avoid duplicate code here and in FollowAction
                    Point viewPt = scene.convertSceneToView(scenePoint);
                    viewPt = SwingUtilities.convertPoint(getScene().getView(), viewPt, getDecoratorLayer());

                    // The palette is going to follow the cursor vertical position.
                    // We may want to change where the horizontal position is located.
                    Point newPt = new Point(paletteWidget.getX(), viewPt.y);
                    paletteWidget.setLocation(newPt);
                }
            }
        }

    }

    @Override
    public void cancelPalette() {
        if (paletteWidget != null) {
            getDecoratorLayer().remove(paletteWidget);
            getDecoratorLayer().repaint();
            paletteWidget = null;

            if (cancelledWidget != null)//remove from widget to which actions was assigned
            {
                WidgetAction.Chain actionChain = cancelledWidget.getActions(getScene().getActiveTool());
                if (actionChain != null) {
                    actionChain.removeAction(followAction);
                    actionChain.removeAction(followLRAction);
                }
            }
            cancelledWidget = null;
        }
    }

    @Override
    public ContextPaletteModel getModel() {
        ContextPaletteModel retVal = null;

        if (paletteWidget != null) {
            retVal = paletteWidget.getModel();
        }

        return retVal;
    }

    /**
     * Request that the context palette recieve input focus.
     */
    @Override
    public void requestFocus() {
        if (paletteWidget != null) {
            paletteWidget.requestFocusInWindow();
        } else {
            selectionChanged(null, null, true);//gg2 first param widget
            if (paletteWidget != null) {
                paletteWidget.requestFocusInWindow();
            }
        }

        // Need to make sure that the attached Widget is visible.
        if (paletteWidget != null) {

        }
    }

    ///////////////////////////////////////////////////////////////
    // Helper Methods
    protected void showPaletteFor(IWidget widget) {
        if (widget != null) {
            // Lookup lookup = widget.getLookup();
            //ContextPaletteModel model = lookup.lookup(ContextPaletteModel.class);

            ContextPaletteModel model = widget.getContextPaletteModel();

            if (model != null) {
                // For some reason when you use tab key, and the new widget is
                // off the screen the palette is not being removed off the screen
                // correctly, therefore the current paletteWidget gets orphaned.
                //
                // I think that this is because the selectionChanged method is
                // being called twice (however the selectionChanged method also
                // calls cancelPalettte so it should be removed).  So, instead
                // requiring others to manage the events, the palette manager
                // should make sure that things are cleaned up correctly.
                if (paletteWidget != null) {
                    paletteWidget.getParent().remove(paletteWidget);
                }

                paletteWidget = new ContextPalette(model);
                paletteWidget.revalidate();

                Point location = getPaletteLocation(widget, paletteWidget);

                Dimension size = paletteWidget.getPreferredSize();
                paletteWidget.setBounds(location.x, location.y, size.width, size.height);

                getDecoratorLayer().add(paletteWidget);
                getDecoratorLayer().revalidate();
            }
        }
    }

    /**
     * The preferred side is the right side of the widget. If there is not
     * enough space, the palette should be on the left side.
     *
     * @param widget The widget that will be decorated.
     * @param palette
     * @return
     */
    protected Point getPaletteLocation(IWidget widget, ContextPalette palette) {

        int xPos = 0;
        int yPos = 0;
//        Rectangle clientArea = widget.getScene().getClientArea();
        Rectangle visibleRect = widget.getScene().getView().getVisibleRect();
        if (widget != null) {
            Dimension collapsedDim = palette.getPreferredSize();

            // The 10 accounts for the top, and bottom values of the empty border.
            int height = collapsedDim.height - 10;
            Point location;
            if (widget instanceof IPinWidget) {
                IPinWidget pinWidget = (IPinWidget) widget;
                Point parentLocation = pinWidget.getPNodeWidget().getPreferredLocation();
                Point currentLocation = widget.getLocation(); // pinwidget location is not set manually
                location = new Point(parentLocation.x + currentLocation.x, parentLocation.y + currentLocation.y);
            } else if (widget instanceof INodeWidget) {
                location = widget.getPreferredLocation();
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            if (location != null) {
                Widget parentWidget = widget.getParentWidget();
                if (widget instanceof INodeWidget) {
                    if (parentWidget != null) {
                        location = parentWidget.convertLocalToScene(location);
                    }
                }
                Rectangle actual = widget.getClientArea();

                Point viewLocation = scene.convertSceneToView(location);
                Rectangle viewActual = scene.convertSceneToView(actual);

                xPos = viewLocation.x + viewActual.width + SPACE_FROM_WIDGET;

                /*Start :  for inner widget */
                Rectangle inner;
                if (widget instanceof INNodeWidget) {
                    inner = ((NodeWidget) widget).getNodeImageWidget().getPreferredBounds();
                } else if (widget instanceof IPNodeWidget) {
                    inner = widget.getPreferredBounds();
                } else if (widget instanceof IPinWidget) {
                    IPinWidget pinWidget = (IPinWidget) widget;
                    inner = pinWidget.getBounds();
                } else {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                Rectangle outer = widget.getClientArea();
                double difX = (outer.getWidth() - inner.getWidth()) / 2;
                xPos = xPos - (int) difX;
                if (widget instanceof IPinWidget) { //remove padding space
                    IPinWidget pinWidget = (IPinWidget) widget;
                    xPos = xPos - (int) pinWidget.getLocation().getX() / 2;
                }

                // Center the palette on the widget.
//                int yCenter = viewLocation.y + (viewActual.height / 2);
                int yCenter = viewLocation.y + (inner.height / 2);
                yPos = yCenter - (height / 2);
                /*End :  for inner widget */

                // Issue Fix #5852 Start
                if (yCenter - (height / 2) < visibleRect.y) {
                    yPos = (int) visibleRect.y;
                } else if (yCenter + (height / 2) > visibleRect.y + visibleRect.getHeight()) {
                    yPos = (int) visibleRect.y + (int) visibleRect.getHeight() - (int) collapsedDim.getHeight();
                }
                // Issue Fix #5852 End

                JComponent view = getScene().getView();

                int expandedWidth = palette.getExpandedWidth();
                Rectangle viewableRec = view.getVisibleRect();
                int rightViewBounds = viewableRec.x + viewableRec.width;
                if (rightViewBounds < (xPos + expandedWidth)) {
                    xPos = viewLocation.x + (int) difX - SPACE_FROM_WIDGET - collapsedDim.width;
                    palette.setDirection(PaletteDirection.LEFT);
                }
            }
        }

//
//if(clientArea.getY() < )
//
        return new Point(xPos, yPos);
    }

    /**
     * The preferred side is the right side of the widget. If there is not
     * enough space, do nothing becausae jump have no sense
     *
     * @param widget The widget that will be decorated. #param left if paletter
     * should be on left side (inner)
     */
    protected Point getPaletteLocationLR(Widget widget,
            ContextPalette palette, boolean left) {
        Dimension collapsedDim = palette.getPreferredSize();

        Point location = widget.getPreferredLocation();
        location = widget.getParentWidget().convertLocalToScene(location);

        Rectangle actual = widget.getClientArea();

        Point viewLocaton = scene.convertSceneToView(location);
        Rectangle viewActual = scene.convertSceneToView(actual);

        // Center the palette on the widget.
        int xPos = 0;
        if (!left) {
            xPos = viewLocaton.x + viewActual.width + SPACE_FROM_WIDGET;
        } else {
            xPos = viewLocaton.x + SPACE_FROM_WIDGET;//inner left side
        }
        int yPos = viewLocaton.y + (viewActual.height / 2) - (collapsedDim.height / 2);

        //JComponent view = getScene().getView();
        //int expandedWidth = palette.getExpandedWidth();
        /*if(view.getWidth() < xPos + expandedWidth)
         {
         xPos = viewLocaton.x - SPACE_FROM_WIDGET - collapsedDim.width;
         palette.setDirection(PaletteDirection.LEFT);
         }*/
        return new Point(xPos, yPos);
    }

    ///////////////////////////////////////////////////////////////
    // Data Access
    public JComponent getDecoratorLayer() {
        JComponent retVal = decoratorLayer;

        if (retVal == null) {
            if (scene.getView() == null) {
                retVal = scene.createView();
            } else {
                retVal = scene.getView();
            }
        }

        return retVal;
    }

    protected void setDecoratorLayer(JComponent layer) {
        decoratorLayer = layer;
    }

    public IModelerScene getScene() {
        return scene;
    }

    public void setScene(IModelerScene scene) {
        this.scene = scene;
    }

//    private Widget getFirstNode(DesignerScene scene, List selectedObjects)
//    {
//        Widget retVal = null;
//
//        for(Object curObject : selectedObjects)
//        {
//            if(scene.isNode(curObject) == true)
//            {
//                retVal = scene.findWidget(curObject);
//                break;
//            }
//        }
//
//        return retVal;
//    }
    public class FollowCursorAction extends WidgetAction.Adapter {

        @Override
        public State mouseMoved(Widget widget, WidgetMouseEvent event) {
            if (paletteWidget != null && paletteWidget.isVisible() && widget.getState().isSelected()) {
                Point scenePt = widget.convertLocalToScene(event.getPoint());
                Point viewPt = scene.convertSceneToView(scenePt);
                viewPt = SwingUtilities.convertPoint(getScene().getView(), viewPt, getDecoratorLayer());

                // The palette is going to follow the cursor vertical position.
                // We may want to change where the horizontal position is located.
                Point newPt = new Point(paletteWidget.getX(), viewPt.y - 6);
                paletteWidget.setLocation(newPt);
            }

            return State.REJECTED;
        }

    }

    public class FollowCursorLeftRightAction extends WidgetAction.Adapter {

        @Override
        public State mouseMoved(Widget widget, WidgetMouseEvent event) {
            if (paletteWidget != null && paletteWidget.isVisible() && widget.getState().isSelected()) {
                Rectangle bounds = widget.getBounds();
                boolean left = event.getPoint().x < (bounds.x + bounds.width / 2);

                Point scenePt = widget.convertLocalToScene(event.getPoint());
                Point viewPt = scene.convertSceneToView(scenePt);
                viewPt = SwingUtilities.convertPoint(getScene().getView(), viewPt, getDecoratorLayer());

                // The palette is going to follow the cursor vertical position.
                // We may want to change where the horizontal position is located.
                Point newPt = getPaletteLocationLR(widget, paletteWidget, left);
                newPt.y = viewPt.y - 6;
                //Point newPt = new Point(paletteWidget.getX(), viewPt.y);
                if (!paletteWidget.getBounds().contains(viewPt)) {
                    paletteWidget.setLocation(newPt);
                }
            }

            return State.REJECTED;
        }

    }

}
