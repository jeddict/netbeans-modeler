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
package org.netbeans.modeler.label;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.border.RoundResizeBorder;
import org.netbeans.modeler.label.inplace.InplaceEditorAction;
import org.netbeans.modeler.label.inplace.TextPaneInplaceEditorProvider;
import org.netbeans.modeler.label.multiline.MultilineEditableCompartmentWidget;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;

/**
 * The AbstractLabelManger provides a basic implementation of the label manager.
 * This implementation will control how to display the labels on a connection.
 * It is up to the subclasses to specify how to create and initialize the
 * labels.
 *
 *
 */
public abstract class AbstractLabelManager implements LabelManager {

    private final static BasicStroke ALIGN_STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{6.0f, 3.0f}, 0.0f);
    private final static Border NON_SELECTED_BORDER = BorderFactory.createOpaqueBorder(1, 1, 1, 1);
    private final static Border SELECTED_BORDER = BorderFactory.createLineBorder(1, new Color(0xFFA400));
    private Widget connector = null;
    LabelWidget label;
    private ILabelConnectionWidget labelConnectionWidget;
    private EdgeLabelMoveSupport labelMoveSupport;

    /**
     * Creates an AbstractLabelManager and associates it to a connection widget.
     *
     * @param widget
     */
    public AbstractLabelManager(Widget widget, String label) {
        connector = widget;
        init(label);
    }

    @Override
    public void hideLabel() {
        label.setVisible(false);
    }

    @Override
    public boolean isVisible() {
        return label.isVisible();
    }

    @Override
    public void showLabel() {
        label.setVisible(true);
    }

    @Override
    public void setLabel(String name) {
        label.setLabel(name);
        Rectangle label_Bound = label.getPreferredBounds();
        Rectangle labelCon_Bound = labelConnectionWidget.getPreferredBounds();
        Rectangle bound = new Rectangle((int) labelCon_Bound.getX(), (int) labelCon_Bound.getY(), (int) label_Bound.getWidth(), (int) label_Bound.getHeight());
        labelConnectionWidget.setPreferredBounds(bound);
    }

    @Override
    public String getLabel() {
        return label.getLabel();
    }

    @Override
    public LabelWidget getLabelWidget() {
        return label;
    }

    public void init(final String name) {
        ObjectScene scene = (ObjectScene) getConnector().getScene();
//        label = createLabel(name);

        labelConnectionWidget = new LabelConnectionWidget(scene, name);

        labelMoveSupport = new EdgeLabelMoveSupport(connector);

        //Action for LabelWidget
        WidgetAction.Chain chain = getLabelConnectionWidget().createActions(DesignerTools.SELECT);
        chain.addAction(scene.createSelectAction());
        chain.addAction(ActionFactory.createMoveAction(labelMoveSupport, labelMoveSupport));
        chain.addAction(new MoveNodeKeyAction(labelMoveSupport, labelMoveSupport));
        chain.addAction(new WidgetAction.Adapter() {
            @Override
            public WidgetAction.State keyPressed(Widget widget,
                    WidgetAction.WidgetKeyEvent event) {
                WidgetAction.State retVal = WidgetAction.State.REJECTED;

                if (widget.getState().isSelected() && (event.getKeyCode() == KeyEvent.VK_DELETE || event.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
                    hideLabel();
                    retVal = WidgetAction.State.CONSUMED;
                }

                return retVal;
            }
        });

        label.setVisible(true);

        if (connector instanceof IEdgeWidget) {
            IEdgeWidget edgeWidget = (IEdgeWidget) connector;
            edgeWidget.addChild((Widget) getLabelConnectionWidget());
            edgeWidget.setConstraint((Widget) getLabelConnectionWidget(),
                    getDefaultAlignment(name, LabelType.EDGE),
                    0.5f);

        } else if (connector instanceof INodeWidget) {
            INodeWidget nodeWidget = (INodeWidget) connector;
            nodeWidget.getModelerScene().getLabelLayer().addChild((Widget) getLabelConnectionWidget());
        }

        scene.validate();

        setDefaultPosition();

    }

    @Override
    public void setDefaultPosition() {
        ObjectScene scene = (ObjectScene) getConnector().getScene();
        if (connector instanceof IEdgeWidget) {
            labelMoveSupport.setAnchorLocation(0.5f);
            /* LabelType.Source/Target
             ((ConnectionWidget) connector).setConstraint(child,
             getDefaultAlignment(name, type),
             getAlignmentDistance(type));
             labelMoveSupport.setAnchorLocation(getAlignmentPrecent(type));
             */

        } else if (connector instanceof INodeWidget) {
            INodeWidget nodeWidget = (INodeWidget) connector;
            Rectangle rec = nodeWidget.getSceneViewBound();
            Point point = new Point((int) (rec.getX()), (int) (rec.getY() + rec.getHeight() + 5));
            Rectangle new_rec = getLabelConnectionWidget().getPreferredBounds();
            point = new Point((int) (point.getX() + (rec.getWidth() - new_rec.getWidth()) / 2), (int) (point.getY()));
            getLabelConnectionWidget().setPreferredLocation(point);
        }
        scene.validate();
    }

    /**
     * Specifies the alignment along the edge.
     *
     * @param name the name of the label.
     * @param type the type of the label
     * @return the alignment of the label.
     */
    protected abstract LayoutFactory.ConnectionWidgetLayoutAlignment getDefaultAlignment(String name,
            LabelType type);

    /**
     * Specifies where to place the label on the connection.
     *
     * @param type the type of label.
     * @return the location of the label.
     */
    protected float getAlignmentPrecent(LabelType type) {
        float retVal = 0.5f;

        if (type == LabelType.SOURCE) {
            retVal = 0f;
        } else if (type == LabelType.TARGET) {
            retVal = 1f;
        }

        return retVal;
    }

    /**
     * Specifies where to place the label on the connection.
     *
     * @param type the type of label.
     * @return the location of the label.
     */
    protected int getAlignmentDistance(LabelType type) {
        int retVal = 0;

        if (type == LabelType.SOURCE) {
            retVal = 5;
        } else if (type == LabelType.TARGET) {
            retVal = -5;
        }

        return retVal;
    }

    /**
     * Retreives the associated connection.
     *
     * @return the connection
     */
    protected Widget getConnector() {
        return connector;
    }

    /**
     * Retreives the associated scene.
     *
     * @return the scene.
     */
    protected Scene getScene() {
        return connector.getScene();
    }

    /**
     * The createLineWidget method is used to create a line widget that can be
     * used to connect a label to the associated connnection widget.
     *
     * @param scene The scene that will own the line widget.
     * @return The line widget.
     */
    private ConnectionWidget createLineWidget(Scene scene) {
        ConnectionWidget widget = new ConnectionWidget(scene);
        widget.setStroke(ALIGN_STROKE);
        widget.setForeground(new Color(242, 132, 0));//Color.GRAY);
        return widget;
    }

    /**
     * @return the labelConnectionWidget
     */
    @Override
    public ILabelConnectionWidget getLabelConnectionWidget() {
        return labelConnectionWidget;
    }

    /**
     * The ConnectionLabelWidget provides some basic features for all label
     * widgets. For example the connection widget will has the ability to
     * highlight when selected.
     */
    private class LabelConnectionWidget extends Widget
            implements ILabelConnectionWidget, PropertyChangeListener {

        LabelWidget labelWidget;

//        private Color previousColor = Color.BLACK;
        public LabelConnectionWidget(Scene scene, String name) {
            super(scene);
//            labelWidget = new LabelWidget(scene, name);

            labelWidget = new MultilineEditableCompartmentWidget(scene, name, null,
                    this, "getResourcePath()", name);
            labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
            label = labelWidget;
            WidgetAction action = new InplaceEditorAction<JScrollPane>(new TextPaneInplaceEditorProvider(new LabelInplaceEditor(connector), null));
            WidgetAction.Chain actions = labelWidget.createActions(DesignerTools.SELECT);
            actions.addAction(action);
            actions.addAction(new WidgetAction.Adapter() {
                @Override
                public WidgetAction.State keyPressed(Widget widget,
                        WidgetAction.WidgetKeyEvent event) {
                    WidgetAction.State retVal = WidgetAction.State.REJECTED;

                    if (widget.getState().isSelected() && (event.getKeyCode() == KeyEvent.VK_DELETE)) {
                        AbstractLabelManager.this.setLabel("");
                        AbstractLabelManager.this.hideLabel();
                        retVal = WidgetAction.State.CONSUMED;
                    }

                    return retVal;
                }
            });

            setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 1)); // use vertical layout

            setBorder(NON_SELECTED_BORDER);
//            labelWidget = (LabelWidget) label;
            addChild(label);
//            setLayout(LayoutFactory.createVerticalFlowLayout());

            WidgetAction.Chain selectActionTool = this.createActions(DesignerTools.SELECT);
            selectActionTool.addAction(this.getScene().createWidgetHoverAction());
            selectActionTool.addAction(ActionFactory.createSelectAction(new SelectProvider() {
                @Override
                public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
                    return false;
                }

                @Override
                public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
                    return true;
                }

                @Override
                public void select(Widget widget, Point point, boolean bln) {
                    selectAction();
                }
            }));

        }

        private void selectAction() {
            if (connector instanceof INodeWidget) {
                ((INodeWidget) connector).showResizeBorder();
            }
            setOpaque(true);
//            previousColor = getForeground();

//            setBackground(UIManager.getColor("List.selectionBackground"));
//            setForeground(UIManager.getColor("List.selectionForeground"));
            RoundResizeBorder roundResizeBorder = new RoundResizeBorder(0, new Color(242, 132, 0), new ResizeProvider.ControlPoint[]{}, new ResizeProvider.ControlPoint[]{}, false, 0, 0, true, new Color(242, 132, 0));
            setBorder(roundResizeBorder);
        }

        @Override
        protected void notifyStateChanged(ObjectState previousState, ObjectState state) {

//            System.out.println("is" + state.isSelected());
//            System.out.println("ih" + state.isHovered());
            if ((previousState.isSelected() == false) && (state.isSelected() == true)) {
                // Going from not selected to selected.
                // Need to remove the background and changed the font back to the
                // standard color.
                selectAction();

            } else {
                // Going from selected to not selected
                setOpaque(false);
//                setForeground(previousColor);
                setBorder(NodeWidget.DEFAULT_BORDER);

                if (connector instanceof INodeWidget && !connector.getState().isSelected()) {
                    ((INodeWidget) connector).hideResizeBorder();
                }
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // Since this is a wrapper widget, we will simply forward the
            // event to the child widget.
            Widget child = getChildren().get(0);
            if (child instanceof PropertyChangeListener) {
                PropertyChangeListener listener = (PropertyChangeListener) child;
                listener.propertyChange(event);
            }

        }

        @Override
        public LabelWidget getLabelWidget() {
            return label;
        }

        @Override
        public ContextPaletteModel getContextPaletteModel() {
            return null;
        }
    }

    private class EdgeLabelMoveSupport implements MoveStrategy, MoveProvider {

        private Widget connectorWidget; //INODE,IEDGE
        private Point origLoc;
        private ConnectionWidget lineWidget;
        private float anchorLocation = 0.5f;

        public EdgeLabelMoveSupport(Widget connectorWidget) {
            this.connectorWidget = connectorWidget;
        }

        @Override
        public void movementStarted(Widget widget) {
            show(widget);
        }

        @Override
        public void movementFinished(Widget widget) {
            hide();

        }

        @Override
        public Point getOriginalLocation(Widget widget) {
            origLoc = widget.getPreferredLocation();
            return origLoc;
        }

        @Override
        public void setNewLocation(Widget widget, Point location) {
//            connectLineWidget(location);
            widget.setPreferredLocation(location);
        }

        @Override
        public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
            Point labelLocation = widget.getLocation();
            Rectangle widgetBounds = widget.getBounds();
            Rectangle labelBounds = widget.convertLocalToScene(widgetBounds);

            Rectangle nodeBounds = connectorWidget.getBounds();
            nodeBounds = connectorWidget.convertLocalToScene(nodeBounds);
            nodeBounds.getCenterX();
            labelBounds.translate(suggestedLocation.x - labelLocation.x, suggestedLocation.y - labelLocation.y);

            return suggestedLocation;
        }

        public void show(Widget label) {
            Widget owner = connector.getParentWidget();
            if (owner != null) {
                if (lineWidget == null) {
                    lineWidget = createLineWidget(connector.getScene());
                    if (connector instanceof ConnectionWidget) {
                        lineWidget.setSourceAnchor(new ConnectionAnchor(((ConnectionWidget) connector), anchorLocation));
                    } else if (connector instanceof INodeWidget) {
                        INodeWidget nodeWidget = (INodeWidget) connector;
                        lineWidget.setSourceAnchor(nodeWidget.getModelerScene().getModelerFile().getModelerUtil().getAnchor(nodeWidget));
                    }

                    lineWidget.setTargetAnchor(new ShapeUniqueAnchor(label, false));
                }
                owner.addChild(lineWidget);
            }
        }

        public void hide() {
            if (lineWidget != null) {
                lineWidget.removeFromParent();
                lineWidget = null;
            }
        }

        private void setAnchorLocation(float location) {
            anchorLocation = location;
        }
    }
}
