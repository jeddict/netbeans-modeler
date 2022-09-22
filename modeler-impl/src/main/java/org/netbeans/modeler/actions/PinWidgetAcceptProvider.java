/**
 * Copyright 2013-2022 Gaurav Gupta
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
package org.netbeans.modeler.actions;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.util.List;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.action.WidgetDropListener;
import org.netbeans.modeler.config.document.FlowDimensionType;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import static org.netbeans.modeler.core.NBModelerUtil.drawImageOnNodeWidget;
import org.netbeans.modeler.label.ILabelConnectionWidget;
import org.netbeans.modeler.specification.model.document.IContainerElement;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.widget.IBounadryFlowNodeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IModelerSubScene;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;

/**
 *
 *
 */
public class PinWidgetAcceptProvider extends CustomAcceptProvider {

    public PinWidgetAcceptProvider(IModelerScene scene) {
        super(scene);
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
        ConnectorState retVal = ConnectorState.ACCEPT;
        if (isWidgetMove(transferable)) {
            Widget[] target = new Widget[]{getWidget(transferable)};
            for (Widget curWidget : target) {
                if (curWidget instanceof NodeWidget) {
                    NodeWidget nodeWidget = (NodeWidget) curWidget;
                    if (nodeWidget.getNodeWidgetInfo().getModelerDocument().getFlowDimension() == FlowDimensionType.BOUNDARY) {
                        retVal = ConnectorState.REJECT;
                        return retVal;
                    }
                }
            }
        } else if (isPaletteItem(transferable)) {
            SubCategoryNodeConfig subCategoryInfo = getSubCategory(transferable);
            Image image = subCategoryInfo.getImage();
            drawImageOnNodeWidget(image, point, scene, widget);

            if (subCategoryInfo.getModelerDocument().getFlowDimension() == FlowDimensionType.BOUNDARY
                    || INodeWidget.class.isAssignableFrom(subCategoryInfo.getModelerDocument().getWidget())//INodeWidget item skipped
                    || !((IPNodeWidget) widget).isValidPinWidget(subCategoryInfo)) {
                return ConnectorState.REJECT;
            }
        } else {
            List<WidgetDropListener> listeners = (List<WidgetDropListener>) scene.getWidgetDropListener();
            for (WidgetDropListener listener : listeners) {
                if (!listener.isDroppable(widget, point, transferable, scene)) {
                    return ConnectorState.REJECT;
                }
            }
        }

        return retVal;
    }

    @Override
    public void accept(Widget nodeWidget, Point point, Transferable transferable) {
        try {
            if (isWidgetMove(transferable)) {
                boolean convertLocation = false;
                Widget[] target;
                try {
                    target = new Widget[]{getWidget(transferable)};
                    convertLocation = true;
                } catch (Exception e) {
                    target = new Widget[0];
                }

                for (Widget curWidget : target) {

                    if (curWidget instanceof IBounadryFlowNodeWidget
                            || curWidget instanceof ILabelConnectionWidget) {
                        continue;
                    }
                    if (curWidget.getParentWidget() != null) {
                        curWidget.getParentWidget().removeChild(curWidget);
                    }

                    Point curPt = curWidget.getPreferredLocation();
                    if (curPt == null) {
                        curPt = curWidget.getLocation();
                    }
                    scene.addChild(curWidget);
                    if (convertLocation == true) {
                        curWidget.setPreferredLocation(scene.convertSceneToLocal(curPt));
                    }

                    IFlowElementWidget newNodeWidget = (IFlowElementWidget) curWidget;
                    /*Manage Widget and Widget Specification Start */
                    if (newNodeWidget.getFlowElementsContainer() instanceof IModelerSubScene) {
                        IModelerSubScene subProcessWidget = (IModelerSubScene) newNodeWidget.getFlowElementsContainer();

                        subProcessWidget.removeBaseElementElement(newNodeWidget);
                        scene.addBaseElement(newNodeWidget);

                        IRootElement rootElementSpec = (IRootElement) scene.getBaseElementSpec();
                        IContainerElement subProcessSpec = (IContainerElement) subProcessWidget.getBaseElementSpec();
                        IBaseElement baseElementSpec = newNodeWidget.getBaseElementSpec();

                        subProcessSpec.removeBaseElement(baseElementSpec);
                        rootElementSpec.addBaseElement(baseElementSpec);
                    }

                    /*Manage Widget and Widget Specification End */
                    newNodeWidget.setFlowElementsContainer(nodeWidget);

                }
            } else if (isPaletteItem(transferable)) {
                SubCategoryNodeConfig subCategoryInfo = getSubCategory(transferable);
                boolean status = true;
                if (subCategoryInfo.getModelerDocument().getFlowDimension() == FlowDimensionType.BOUNDARY) {
                    status = false;
                }
                if (status) {
                    ((IPNodeWidget) nodeWidget).createPinWidget(subCategoryInfo);
                    scene.getModelerPanelTopComponent().changePersistenceState(false);
                }
            } else {
                List<WidgetDropListener> listeners = (List<WidgetDropListener>) scene.getWidgetDropListener();
                listeners.forEach(listener -> listener.drop(nodeWidget, point, transferable, scene));
            }

        } catch (Throwable t) {
            scene.getModelerFile().handleException(t);
        }
    }

}
