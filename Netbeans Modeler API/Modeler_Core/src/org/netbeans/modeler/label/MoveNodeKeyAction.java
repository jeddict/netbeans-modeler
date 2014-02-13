/** Copyright [2014] Gaurav Gupta
   *
   *Licensed under the Apache License, Version 2.0 (the "License");
   *you may not use this file except in compliance with the License.
   *You may obtain a copy of the License at
   *
   *    http://www.apache.org/licenses/LICENSE-2.0
   *
   *Unless required by applicable law or agreed to in writing, software
   *distributed under the License is distributed on an "AS IS" BASIS,
   *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   *See the License for the specific language governing permissions and
   *limitations under the License.
   */
 package org.netbeans.modeler.label;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Set;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Utilities;

/**
 *
 * 
 */
public class MoveNodeKeyAction extends WidgetAction.LockedAdapter
{

    private MoveStrategy strategy;
    private MoveProvider provider;
    private Widget movingWidget = null;
    private Point dragSceneLocation = null;
    private Point originalSceneLocation = null;
    private Point initialWidgetLocation = null;
    
    // Debugging Properties
    private boolean debugging = false;
    private Widget debugMarker = null;

    public MoveNodeKeyAction(MoveStrategy strategy, MoveProvider provider)
    {
        this.strategy = strategy;
        this.provider = provider;
    }

    protected boolean isLocked()
    {
        return movingWidget != null;
    }

    public State keyPressed (Widget widget, WidgetKeyEvent event)
    {
        if (isLocked())
        {
            boolean state = move(event);
            return state ? State.createLocked(widget, this) : State.REJECTED;
        }

        boolean controlKeyPressed = event.isControlDown();
        if(Utilities.isMac() == true)
        {
            controlKeyPressed = event.isMetaDown();
        }
            
        if((controlKeyPressed == true) && 
          ((event.getKeyCode() == KeyEvent.VK_UP) ||
           (event.getKeyCode() == KeyEvent.VK_DOWN) ||
           (event.getKeyCode() == KeyEvent.VK_LEFT) ||
           (event.getKeyCode() == KeyEvent.VK_RIGHT)))
        {
            movingWidget = getNodeWidget(widget);
            if(movingWidget != null)
            {
                initialWidgetLocation = getWidgetLocation(movingWidget);

                originalSceneLocation = provider.getOriginalLocation(movingWidget);
                if (originalSceneLocation == null)
                {
                    originalSceneLocation = new Point();
                }

                dragSceneLocation = originalSceneLocation;
                provider.movementStarted(movingWidget);
                
                move(event);
                return State.createLocked(widget, this);
            }
        }
        
        return State.REJECTED;
    }

    public State keyReleased (Widget widget, WidgetKeyEvent event)
    {   
        State retVal = State.REJECTED;
        
        if(movingWidget != null)
        {
            boolean controlKeyPressed = event.isControlDown();
            if(Utilities.isMac() == true)
            {
                controlKeyPressed = event.isMetaDown();
            }
            
            if((controlKeyPressed == true) && 
              ((event.getKeyCode() == KeyEvent.VK_UP) ||
               (event.getKeyCode() == KeyEvent.VK_DOWN) ||
               (event.getKeyCode() == KeyEvent.VK_LEFT) ||
               (event.getKeyCode() == KeyEvent.VK_RIGHT)))
            {
                // This is a repeated event.  Therfore there is nothing to do.
                retVal = State.createLocked(widget, this);
            }
            else
            {
                provider.movementFinished(movingWidget);
                
                movingWidget = null;
                dragSceneLocation = null;
                originalSceneLocation = null;
                initialWidgetLocation = null;

                retVal = State.CONSUMED;

                if(debugMarker != null)
                {
                    debugMarker.removeFromParent();
                    debugMarker = null;
                }
            }
        }
        
        return retVal;
    }

    /**
     * Checks if an parent widget is also selected.  If the parent widget is
     * also selected the parent will be returned.
     *
     * @param movingWidget The widget that is being checked.
     * @return The correct widget that should be the moves target widget.
     */
    private Widget getTopmostWidget(Widget movingWidget)
    {
        Widget retVal = null;

        if(movingWidget != null)
        {
            if (movingWidget.getScene() instanceof ObjectScene)
            {
                ObjectScene scene = (ObjectScene) movingWidget.getScene();
                Set selected = scene.getSelectedObjects();
                Object data = scene.findObject(movingWidget);
                if(movingWidget == scene.findWidget(data))
                {
                    if((selected != null) && (selected.contains(data) == true))
                    {
                        retVal = movingWidget;
                    }
                }
            }
            else
            {
                if(movingWidget.getState().isSelected() == true)
                {
                    retVal = movingWidget;
                }
            }

            if(movingWidget.getParentWidget() != null)
            {
                Widget newWidget = getTopmostWidget(movingWidget.getParentWidget());
                if(newWidget != null)
                {
                    retVal = newWidget;
                }
            }
        }
        return retVal;
    }

    private boolean move(WidgetKeyEvent event)
    {
        boolean state = false;

        if (movingWidget != null)
        {
            Point newWidgetLocation = getNewLocation(movingWidget, event);

            if (initialWidgetLocation != null && initialWidgetLocation.equals(newWidgetLocation))
            {
                state = true;
            }
            else
            {
                state = move(movingWidget, newWidgetLocation);
            }
        }

        return state;
    }
    
    private Point getNewLocation (Widget widget, WidgetKeyEvent event)
    {
        Point location = new Point(0, 0);
        if(event.getKeyCode() == KeyEvent.VK_UP)
        {
            location.y -= 10;
        }
        else if(event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            location.y += 10;
        }
        else if(event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            location.x -= 10;
        }
        else if(event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            location.x += 10;

        }
        
        return location;
    }

    /**
     * A connection widget can not be moved.  Therefore if the focused widget 
     * is a connection widget get one of the ends and base the movement on the
     * nodes.  A node widget will only returned if one of the nodes are selected.
     * 
     * @param widget The target of the key event.
     * @return The node widget.  If no node widget is selected, then null is
     *         returned.
     */
    private Widget getNodeWidget(Widget widget) 
    {
        Widget retVal = widget;
        
        if (widget instanceof ConnectionWidget) 
        {
            retVal = null;
            
            ConnectionWidget connection = (ConnectionWidget) widget;
            Widget source = connection.getSourceAnchor().getRelatedWidget();
            if((source != null) && (source.getState().isSelected() == true))
            {
                retVal = source;
            }
            else
            {
                Widget target = connection.getTargetAnchor().getRelatedWidget();
                if((target != null) && (target.getState().isSelected() == true))
                {
                    retVal = target;
                }
            }
            
            if(retVal == null)
            {
                if (widget.getScene() instanceof GraphScene)
                {
                    GraphScene scene = (GraphScene) widget.getScene();
                    for(Object select : scene.getSelectedObjects())
                    {
                        if(scene.isNode(select) == true)
                        {
                            retVal = scene.findWidget(select);
                            break;
                        }
                        else
                        {
                            //we have an edge label selected here..
                            retVal = scene.findWidget(select);
                            if (retVal instanceof ConnectionWidget)
                            {
                                // The only widget that is selected is an edge.
                                retVal = null;
                            }
                        }
                    }
                }
            }
        }
        
        return getTopmostWidget(retVal);
    }

    private Point getWidgetLocation(Widget widget)
    {

        Point retVal = widget.getPreferredLocation();
        if (retVal == null)
        {
            retVal = widget.getLocation();
        }

        return retVal;
    }

    private boolean move(Widget widget, Point newLocation)
    {
        if ((movingWidget != widget) || (widget == null))
        {
            return false;
        }
        initialWidgetLocation = null;
        
        if(widget.getPreferredLocation() == null)
        {
            return false;
        }
        Point sceneLocation = widget.getParentWidget().convertLocalToScene(widget.getPreferredLocation());
        
        if(debugging == true)
        {
            if(debugMarker == null)
            {
                debugMarker = new Widget(widget.getScene());
                debugMarker.setPreferredSize(new java.awt.Dimension(10, 10));
                debugMarker.setBackground(java.awt.Color.BLUE);
                debugMarker.setOpaque(true);
                debugMarker.setBorder(BorderFactory.createLineBorder());
            }

            // Make sure that the widget has not switched parents.
            if(debugMarker.getParentWidget() != null)
            {
                debugMarker.removeFromParent();
            }
            widget.getParentWidget().addChild(debugMarker);
            debugMarker.setPreferredLocation(new Point(sceneLocation.x - 5, sceneLocation.y - 5));
        }

        newLocation.x += sceneLocation.x;
        newLocation.y += sceneLocation.y;
        Point location = newLocation;
        provider.setNewLocation(widget, strategy.locationSuggested(widget, originalSceneLocation, location));
        return true;
    }
}
