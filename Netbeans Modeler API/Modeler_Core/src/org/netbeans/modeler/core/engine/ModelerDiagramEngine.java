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
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.actions.CustomAcceptAction;
import org.netbeans.modeler.actions.InteractiveZoomAction;
import org.netbeans.modeler.actions.LockSelectionAction;
import org.netbeans.modeler.actions.PanAction;
import org.netbeans.modeler.actions.ZoomManager;
import org.netbeans.modeler.actions.ZoomManager.ZoomEvent;
import org.netbeans.modeler.actions.export.image.ExportImageAction;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.label.LabelInplaceEditor;
import org.netbeans.modeler.provider.EdgeWidgetSelectProvider;
import org.netbeans.modeler.provider.ModelerSceneSelectProvider;
import org.netbeans.modeler.provider.PinWidgetSelectProvider;
import org.netbeans.modeler.provider.connection.SequenceFlowReconnectProvider;
import org.netbeans.modeler.provider.connection.controlpoint.FreeMoveControlPointProvider;
import org.netbeans.modeler.provider.connection.controlpoint.MoveControlPointAction;
import org.netbeans.modeler.provider.node.move.AlignStrategyProvider;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.tool.DiagramSelectToolAction;
import org.netbeans.modeler.widget.context.ContextPaletteManager;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.vmd.PEdgeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.PinWidget;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

public abstract class ModelerDiagramEngine implements IModelerDiagramEngine {

    private IModelerScene scene;
    private ModelerFile file;
    private ZoomManager zoomManager;
    public static AlignStrategyProvider alignStrategyProvider = null;

    @Override
    public void init(ModelerFile file) {
        this.setFile(file);
        this.scene = file.getVendorSpecification().getModelerDiagramModel().getModelerScene();
        alignStrategyProvider = new AlignStrategyProvider(scene);
    }

    @Override
    public void setModelerSceneAction() {

        zoomManager = new ZoomManager((Scene) scene);
        zoomManager.addZoomListener(new ZoomManager.ZoomListener() {
            @Override
            public void zoomChanged(ZoomEvent event) {
                ContextPaletteManager manager = scene.getContextPaletteManager();
                if (manager != null) {
                    // Make sure that the palette is correctly placed for the
                    // zoom level.
                    manager.cancelPalette();
                    manager.selectionChanged(null, null);
                }
            }
        });

        //INamespace diagramNamespace = scene.getDiagram().getNamespaceForCreatedElements();
//        AcceptProvider provider = new SceneAcceptProvider(diagramNamespace, false);
//        WidgetAction acceptAction = new DiagramSceneAcceptAction(provider);
        WidgetAction acceptAction = ActionFactory.createAcceptAction(new CustomAcceptAction(scene));

        WidgetAction.Chain selectTool = scene.createActions(DesignerTools.SELECT);
        selectTool.addAction(new LockSelectionAction());

//        InputMap inputMap = new InputMap();
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "myAction");
//
//        ActionMap actionMap = new ActionMap();
//        actionMap.put("myAction", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "My Action has been invoked");
//            }
//        });
//
//        selectTool.addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
        selectTool.addAction(ActionFactory.createSelectAction(new ModelerSceneSelectProvider(), true));
        selectTool.addAction(ActionFactory.createRectangularSelectAction((ObjectScene) scene, scene.getBackgroundLayer()));
        selectTool.addAction(ActionFactory.createZoomAction());
        selectTool.addAction(scene.createWidgetHoverAction());
        selectTool.addAction(acceptAction);
        selectTool.addAction(ActionFactory.createPopupMenuAction(scene.getPopupMenuProvider()));
        //   selectTool.addAction(ActionFactory.createCycleFocusAction(new CycleObjectSceneSelectProvider()));
        selectTool.addAction(new WidgetAction.Adapter() {
            public WidgetAction.State mouseMoved(Widget widget, WidgetAction.WidgetMouseEvent event) {
                Point point = widget.convertLocalToScene(event.getPoint());
                positionLabel.setText("[" + point.x + "," + point.y + "]");
                return WidgetAction.State.REJECTED;
            }
//             public WidgetAction.State mousePressed(Widget widget, WidgetAction.WidgetMouseEvent event) {
//                 //System.out.println("DENgine mousePressed");
//                 if (scene.getContextPaletteManager() != null) {
//                     scene.getContextPaletteManager().cancelPalette();
//                 }
//                return WidgetAction.State.REJECTED;
//            }
        });

        WidgetAction.Chain panTool = scene.createActions(DesignerTools.PAN);
        panTool.addAction(new PanAction());
        panTool.addAction(ActionFactory.createZoomAction());
        // panTool.addAction(ActionFactory.createPopupMenuAction(menuProvider));

        // WidgetAction.Chain marqueeZoomTool = scene.createActions(DesignerTools.MARQUEE_ZOOM);
        //  marqueeZoomTool.addAction(ActionFactory.createZoomAction());
        //  marqueeZoomTool.addAction(scene.createMarqueeSelectAction());
        // marqueeZoomTool.addAction(ActionFactory.createPopupMenuAction(menuProvider));
        WidgetAction.Chain interactiveZoomTool = scene.createActions(DesignerTools.INTERACTIVE_ZOOM);
        interactiveZoomTool.addAction(new InteractiveZoomAction());
        interactiveZoomTool.addAction(ActionFactory.createZoomAction());
        //   interactiveZoomTool.addAction(ActionFactory.createPopupMenuAction(menuProvider));

        WidgetAction.Chain contextPalette = scene.createActions(DesignerTools.CONTEXT_PALETTE);
        contextPalette.addAction(acceptAction);
    }
    private static final PinWidgetSelectProvider pinWidgetSelectProvider = new PinWidgetSelectProvider();

    public void setPinWidgetAction(final IPinWidget pinWidget) {
//        WidgetAction doubleClickAction = new DoubleClickAction(new DoubleClickProvider() {
//            @Override
//            public void onDoubleClick(Widget widget, Point point, boolean bln) {
//                pinWidget.showProperties();
//                pinWidget.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
//            }
//        });
//        WidgetAction selectAction = ActionFactory.createSelectAction(new NodeWidgetSelectProvider(pinWidget.getModelerScene()));
        WidgetAction editAction = ActionFactory.createInplaceEditorAction(new LabelInplaceEditor((Widget) pinWidget));

        WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(pinWidget.getPopupMenuProvider());

        WidgetAction.Chain selectActionTool = pinWidget.createActions(DesignerTools.SELECT);
        selectActionTool.addAction(ActionFactory.createSelectAction(pinWidgetSelectProvider, true));//(getScene().createSelectAction());
        selectActionTool.addAction(getScene().createObjectHoverAction());
        selectActionTool.addAction(popupMenuAction);
        ((PinWidget) pinWidget).getPinNameWidget().getActions().addAction(editAction);
    }

    public void setEdgeWidgetAction(IEdgeWidget edgeWidget) {
        WidgetAction.Chain actions = edgeWidget.getActions();

        if (edgeWidget instanceof PEdgeWidget) {
            actions.addAction(ActionFactory.createAddRemoveControlPointAction());
            actions.addAction(ActionFactory.createMoveControlPointAction(ActionFactory.createFreeMoveControlPointProvider(), ConnectionWidget.RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED));
//
//            actions.addAction(new MoveControlPointAction(new FreeMoveControlPointProvider(), null)); // Working
            actions.addAction(scene.createWidgetHoverAction());
            actions.addAction(ActionFactory.createSelectAction(new EdgeWidgetSelectProvider(edgeWidget.getModelerScene())));
            actions.addAction(ActionFactory.createReconnectAction(ActionFactory.createDefaultReconnectDecorator(), new SequenceFlowReconnectProvider(scene)));
            actions.addAction(ActionFactory.createPopupMenuAction(edgeWidget.getPopupMenuProvider()));

        } else {
            actions.addAction(ActionFactory.createAddRemoveControlPointAction());
            actions.addAction(new MoveControlPointAction(new FreeMoveControlPointProvider(), null)); // Working
            actions.addAction(scene.createWidgetHoverAction());
            actions.addAction(ActionFactory.createSelectAction(new EdgeWidgetSelectProvider(edgeWidget.getModelerScene())));
            actions.addAction(ActionFactory.createReconnectAction(ActionFactory.createDefaultReconnectDecorator(), new SequenceFlowReconnectProvider(scene)));
            actions.addAction(ActionFactory.createPopupMenuAction(edgeWidget.getPopupMenuProvider()));

        }

//       getActions().addAction(ActionFactory.createFreeMoveControlPointAction());
        //  getActions ().addAction (ActionFactory.createMoveControlPointAction (ActionFactory.createFreeMoveControlPointProvider (), ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY));
//        getActions().addAction(((GraphScene) scene).createSelectAction());
        //ActionFactory.createReconnectAction (new SceneReconnectProvider ());
    }
    public JLabel positionLabel;

    /**
     *
     * @param bar
     */
    @Override
    public void buildToolBar(JToolBar bar) {
        final JButton saveButton = new JButton(ImageUtil.getInstance().getIcon("save-doc.png"));
        saveButton.setToolTipText("Save Modeler File");
        bar.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scene.getModelerFile().save();
            }
        });

        JButton exportImageButton = new JButton(new ExportImageAction(scene));
        bar.add(exportImageButton);

        final JButton satelliteViewButton = new JButton(ImageUtil.getInstance().getIcon("satelliteView.png"));
        bar.add(satelliteViewButton);
        satelliteViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setLayout(new BorderLayout());
                JComponent satelliteView = scene.createSatelliteView();
                popup.add(satelliteView, BorderLayout.CENTER);
                popup.show(satelliteViewButton, (satelliteViewButton.getSize().width - satelliteView.getPreferredSize().width) / 2, satelliteViewButton.getSize().height);
            }
        });

        bar.add(new JToolBar.Separator());

        ButtonGroup selectToolBtnGroup = new ButtonGroup();

        JToggleButton selectToolButton = new JToggleButton(
                new DiagramSelectToolAction(scene, DesignerTools.SELECT,
                ImageUtil.getInstance().getIcon("selection-arrow.png"), "SelectToolAction",
                Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR),
                KeyStroke.getKeyStroke("ctrl alt shift S"),
                KeyStroke.getKeyStroke("meta ctrl shift S")));
        selectToolButton.setName(DesignerTools.SELECT);  // need a name to later identify the button
        DesignerTools.mapToolToButton.put(DesignerTools.SELECT, selectToolButton);

        JToggleButton handToolButton = new JToggleButton(
                new DiagramSelectToolAction(scene,
                DesignerTools.PAN, ImageUtil.getInstance().getIcon("pan.png"), "HandToolAction",
                //NbBundle.getMessage(DiagramSelectToolAction.class, "LBL_HandToolAction"),
                Utilities.createCustomCursor(scene.getView(),
                ImageUtilities.icon2Image(ImageUtil.getInstance().getIcon("pan-open-hand.gif")), "PanOpenedHand"),
                KeyStroke.getKeyStroke("ctrl alt shift N"),
                KeyStroke.getKeyStroke("meta ctrl shift N")));
        handToolButton.setName(DesignerTools.PAN);

        JToggleButton interactiveZoomButton = new JToggleButton(
                new DiagramSelectToolAction(scene,
                DesignerTools.INTERACTIVE_ZOOM, ImageUtil.getInstance().getIcon("interactive-zoom.png"), "InteractiveZoomAction",
                Utilities.createCustomCursor(scene.getView(),
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

        bar.add(new JToolBar.Separator());
        zoomManager.addToolbarActions(bar);
        bar.add(new JToolBar.Separator());

        positionLabel = new JLabel();
        bar.add(positionLabel);
        bar.add(new JToolBar.Separator());

    }

    @Override
    public IModelerScene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    @Override
    public void setScene(IModelerScene scene) {
        this.scene = scene;
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
