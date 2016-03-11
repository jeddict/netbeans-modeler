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
package org.netbeans.modeler.core.engine;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.actions.CustomAcceptAction;
import org.netbeans.modeler.actions.CyclePinFocusAction;
import org.netbeans.modeler.actions.CyclePinFocusProvider;
import org.netbeans.modeler.actions.EdgeDeleteAction;
import org.netbeans.modeler.actions.InteractiveZoomAction;
import org.netbeans.modeler.actions.LockSelectionAction;
import org.netbeans.modeler.actions.NodeDeleteAction;
import org.netbeans.modeler.actions.PanAction;
import org.netbeans.modeler.actions.ZoomManager;
import org.netbeans.modeler.actions.ZoomManager.ZoomEvent;
import org.netbeans.modeler.actions.export.ExportAction;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.provider.EdgeWidgetSelectProvider;
import org.netbeans.modeler.provider.ModelerSceneSelectProvider;
import org.netbeans.modeler.provider.NodeWidgetSelectProvider;
import org.netbeans.modeler.provider.PinWidgetSelectProvider;
import org.netbeans.modeler.provider.connection.SequenceFlowReconnectProvider;
import org.netbeans.modeler.provider.connection.controlpoint.FreeMoveControlPointProvider;
import org.netbeans.modeler.provider.connection.controlpoint.MoveControlPointAction;
import org.netbeans.modeler.provider.node.move.AlignStrategyProvider;
import org.netbeans.modeler.provider.node.move.MoveAction;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.tool.DiagramSelectToolAction;
import org.netbeans.modeler.widget.context.ContextPaletteManager;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.vmd.PEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.netbeans.modeler.actions.PinDeleteAction;

public class ModelerDiagramEngine implements IModelerDiagramEngine {

    protected ModelerFile file;
    private static final WidgetAction NODE_DELETE_ACTION = new NodeDeleteAction();
    private static final WidgetAction EDGE_DELETE_ACTION = new EdgeDeleteAction();
    private static final WidgetAction PIN_DELETE_ACTION = new PinDeleteAction();

    public static AlignStrategyProvider ALIGNSTRATEGY_PROVIDER = null;
    protected static final MoveProvider MOVE_PROVIDER_DEFAULT = new MoveProvider() {
        private boolean locationChanged = false;
        private Point original;

        @Override
        public void movementStarted(Widget widget) {
            INodeWidget nodeWidget = (INodeWidget) widget;
            NBModelerUtil.hideContextPalette(nodeWidget.getModelerScene());
            locationChanged = false;
        }

        @Override
        public void movementFinished(Widget widget) {
            INodeWidget nodeWidget = (INodeWidget) widget;
            NBModelerUtil.showContextPalette(nodeWidget.getModelerScene(), nodeWidget);
            if (locationChanged) {
                ((IModelerScene) widget.getScene()).getModelerPanelTopComponent().changePersistenceState(false);
            }
            locationChanged = false;
        }

        @Override
        public Point getOriginalLocation(Widget widget) {
            original = widget.getPreferredLocation();
            return original;
        }

        @Override
        public void setNewLocation(Widget widget, Point location) {
            widget.setPreferredLocation(location);
            if (original != null) {
                locationChanged = true;
            }
        }
    };

    @Override
    public void setNodeWidgetAction(final INodeWidget nodeWidget) {
        WidgetAction selectAction = ActionFactory.createSelectAction(new NodeWidgetSelectProvider(nodeWidget.getModelerScene()));
        WidgetAction moveAction = new MoveAction(nodeWidget, null, MOVE_PROVIDER_DEFAULT, ALIGNSTRATEGY_PROVIDER, ALIGNSTRATEGY_PROVIDER);
        WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(nodeWidget.getPopupMenuProvider());
        WidgetAction snapMoveAction = ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(5, 5), null);
        WidgetAction.Chain selectActionTool = nodeWidget.createActions(DesignerTools.SELECT);
        selectActionTool.addAction(NODE_DELETE_ACTION);
        selectActionTool.addAction(selectAction);
        selectActionTool.addAction(moveAction);
        selectActionTool.addAction(nodeWidget.getModelerScene().createWidgetHoverAction());
        selectActionTool.addAction(popupMenuAction);
        selectActionTool.addAction(snapMoveAction);
    }

    @Override
    public void init(ModelerFile file) {
        this.setFile(file);
        ALIGNSTRATEGY_PROVIDER = new AlignStrategyProvider(file.getModelerScene());
    }

    @Override
    public void setModelerSceneAction() {
        file.getModelerScene().getActions().addAction(ActionFactory.createWheelPanAction());
        file.getModelerScene().getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.5));
        WidgetAction acceptAction = ActionFactory.createAcceptAction(new CustomAcceptAction(file.getModelerScene()));
        WidgetAction.Chain selectTool = file.getModelerScene().createActions(DesignerTools.SELECT);
        selectTool.addAction(new LockSelectionAction());//12  sec
        selectTool.addAction(ActionFactory.createSelectAction(new ModelerSceneSelectProvider(), true));
        selectTool.addAction(ActionFactory.createRectangularSelectAction((ObjectScene) file.getModelerScene(), file.getModelerScene().getBackgroundLayer()));//2 sec
        selectTool.addAction(ActionFactory.createZoomAction());
        selectTool.addAction(file.getModelerScene().createWidgetHoverAction());//2 sec
        selectTool.addAction(acceptAction);
        selectTool.addAction(ActionFactory.createPopupMenuAction(file.getModelerScene().getPopupMenuProvider())); //2190 seec
        selectTool.addAction(new WidgetAction.Adapter() {
            @Override
            public WidgetAction.State mouseMoved(Widget widget, WidgetAction.WidgetMouseEvent event) {
//                if (positionLabel != null) {
//                    Point point = widget.convertLocalToScene(event.getPoint());
//                    positionLabel.setText("[" + point.x + "," + point.y + "]");
//                }
                return WidgetAction.State.REJECTED;
            }
        });
        WidgetAction.Chain panTool = file.getModelerScene().createActions(DesignerTools.PAN);
        panTool.addAction(new PanAction());
        panTool.addAction(ActionFactory.createZoomAction());
        WidgetAction.Chain interactiveZoomTool = file.getModelerScene().createActions(DesignerTools.INTERACTIVE_ZOOM);
        interactiveZoomTool.addAction(new InteractiveZoomAction());
        interactiveZoomTool.addAction(ActionFactory.createZoomAction());
        WidgetAction.Chain contextPalette = file.getModelerScene().createActions(DesignerTools.CONTEXT_PALETTE);
        contextPalette.addAction(acceptAction);

    }
    public static final PinWidgetSelectProvider PIN_WIDGET_SELECT_PROVIDER = new PinWidgetSelectProvider();

    @Override
    public void setPinWidgetAction(final IPinWidget pinWidget) {
        WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(pinWidget.getPopupMenuProvider());
        WidgetAction.Chain selectActionTool = pinWidget.createActions(DesignerTools.SELECT);

        selectActionTool.addAction(PIN_DELETE_ACTION);
        selectActionTool.addAction(ActionFactory.createSelectAction(PIN_WIDGET_SELECT_PROVIDER, true));//(getScene().createSelectAction());
        selectActionTool.addAction(file.getModelerScene().createObjectHoverAction());
        selectActionTool.addAction(popupMenuAction);
        WidgetAction cycleAction = new CyclePinFocusAction(new CyclePinFocusProvider());
        selectActionTool.addAction(cycleAction);
    }

    @Override
    public void setEdgeWidgetAction(IEdgeWidget edgeWidget) {
        WidgetAction.Chain actions = edgeWidget.getActions();

        if (edgeWidget instanceof PEdgeWidget) {
            actions.addAction(EDGE_DELETE_ACTION);
            actions.addAction(ActionFactory.createAddRemoveControlPointAction());
            actions.addAction(ActionFactory.createMoveControlPointAction(ActionFactory.createFreeMoveControlPointProvider(), ConnectionWidget.RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED));
//
//            actions.addAction(new MoveControlPointAction(new FreeMoveControlPointProvider(), null)); // Working
            actions.addAction(file.getModelerScene().createWidgetHoverAction());
            actions.addAction(ActionFactory.createSelectAction(new EdgeWidgetSelectProvider(edgeWidget.getModelerScene())));
            actions.addAction(ActionFactory.createReconnectAction(ActionFactory.createDefaultReconnectDecorator(), new SequenceFlowReconnectProvider(file.getModelerScene())));
            actions.addAction(ActionFactory.createPopupMenuAction(edgeWidget.getPopupMenuProvider()));

        } else {
            actions.addAction(EDGE_DELETE_ACTION);
            actions.addAction(ActionFactory.createAddRemoveControlPointAction());
            actions.addAction(new MoveControlPointAction(new FreeMoveControlPointProvider(), null)); // Working
            actions.addAction(file.getModelerScene().createWidgetHoverAction());
            actions.addAction(ActionFactory.createSelectAction(new EdgeWidgetSelectProvider(edgeWidget.getModelerScene())));
            actions.addAction(ActionFactory.createReconnectAction(ActionFactory.createDefaultReconnectDecorator(), new SequenceFlowReconnectProvider(file.getModelerScene())));
            actions.addAction(ActionFactory.createPopupMenuAction(edgeWidget.getPopupMenuProvider()));

        }
    }
    public JLabel positionLabel;

    /**
     *
     * @param bar
     */
    @Override
    public void buildToolBar(JToolBar bar) {
        buildSaveDocTool(bar);
        buildExportDocTool(bar);
        buildSatelliteTool(bar);
        bar.add(new JToolBar.Separator());
        buildSelectTool(bar);
        bar.add(new JToolBar.Separator());
        buildZoomTool(bar);
        bar.add(new JToolBar.Separator());

//        positionLabel = new JLabel();
//        bar.add(positionLabel);
//        bar.add(new JToolBar.Separator());
    }

    protected void buildExportDocTool(JToolBar bar) {
        JButton exportImageButton = new JButton(new ExportAction(file.getModelerScene())); // 1150 ms
        bar.add(exportImageButton);
    }

    protected void buildSaveDocTool(JToolBar bar) {
        JButton saveButton = new JButton(ImageUtil.getInstance().getIcon("save-doc.png"));
        saveButton.setToolTipText("Save");
        bar.add(saveButton);
        saveButton.addActionListener((ActionEvent e) -> {
            file.save();
        });
    }

    protected void buildSatelliteTool(JToolBar bar) {
        JButton satelliteViewButton = new JButton(ImageUtil.getInstance().getIcon("satelliteView.png"));
        bar.add(satelliteViewButton);
        satelliteViewButton.addActionListener((ActionEvent e) -> {
            JPopupMenu popup = new JPopupMenu();
            popup.setLayout(new BorderLayout());
            JComponent satelliteView = file.getModelerScene().createSatelliteView();
            popup.add(satelliteView, BorderLayout.CENTER);
            popup.show(satelliteViewButton, (satelliteViewButton.getSize().width - satelliteView.getPreferredSize().width) / 2, satelliteViewButton.getSize().height);
        });
    }

    protected void buildZoomTool(JToolBar bar) {
        ZoomManager zoomManager = new ZoomManager((Scene) file.getModelerScene()); //4 sec
        zoomManager.addZoomListener((ZoomEvent event) -> {
            ContextPaletteManager manager = file.getModelerScene().getContextPaletteManager();
            if (manager != null) {
                manager.cancelPalette();
                manager.selectionChanged(null, null);
            }
        });
        zoomManager.addToolbarActions(bar);
    }

    protected void buildSelectTool(JToolBar bar) {

        ButtonGroup selectToolBtnGroup = new ButtonGroup();

        JToggleButton selectToolButton = new JToggleButton(
                new DiagramSelectToolAction(file.getModelerScene(), DesignerTools.SELECT,
                        ImageUtil.getInstance().getIcon("selection-arrow.png"), "SelectToolAction",
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR),
                        KeyStroke.getKeyStroke("ctrl alt shift S"),
                        KeyStroke.getKeyStroke("meta ctrl shift S")));
        selectToolButton.setName(DesignerTools.SELECT);  // need a name to later identify the button
        DesignerTools.mapToolToButton.put(DesignerTools.SELECT, selectToolButton);

        JToggleButton handToolButton = new JToggleButton(
                new DiagramSelectToolAction(file.getModelerScene(),
                        DesignerTools.PAN, ImageUtil.getInstance().getIcon("pan.png"), "HandToolAction",
                        //NbBundle.getMessage(DiagramSelectToolAction.class, "LBL_HandToolAction"),
                        Utilities.createCustomCursor(file.getModelerScene().getView(),
                                ImageUtilities.icon2Image(ImageUtil.getInstance().getIcon("pan-open-hand.gif")), "PanOpenedHand"),
                        KeyStroke.getKeyStroke("ctrl alt shift N"),
                        KeyStroke.getKeyStroke("meta ctrl shift N")));
        handToolButton.setName(DesignerTools.PAN);

        JToggleButton interactiveZoomButton = new JToggleButton(
                new DiagramSelectToolAction(file.getModelerScene(),
                        DesignerTools.INTERACTIVE_ZOOM, ImageUtil.getInstance().getIcon("interactive-zoom.png"), "InteractiveZoomAction",
                        Utilities.createCustomCursor(file.getModelerScene().getView(),
                                ImageUtilities.icon2Image(ImageUtil.getInstance().getIcon("interactive-zoom.gif")), "InteractiveZoom"),
                        KeyStroke.getKeyStroke("ctrl alt shift I"),
                        KeyStroke.getKeyStroke("meta ctrl shift I")));
        interactiveZoomButton.setName(DesignerTools.INTERACTIVE_ZOOM);

        selectToolBtnGroup.add(selectToolButton);
        selectToolBtnGroup.add(handToolButton);
        selectToolBtnGroup.add(interactiveZoomButton);

        selectToolButton.setSelected(true);

        bar.add(selectToolButton);
        bar.add(handToolButton);
        bar.add(interactiveZoomButton);
    }

    /**
     * @return the file
     */
    public ModelerFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(ModelerFile file) {
        this.file = file;
    }
}
