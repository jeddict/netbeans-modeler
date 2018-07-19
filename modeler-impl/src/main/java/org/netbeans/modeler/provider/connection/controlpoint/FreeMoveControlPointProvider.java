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
package org.netbeans.modeler.provider.connection.controlpoint;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.modeler.specification.model.document.widget.IArtifactEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;

public final class FreeMoveControlPointProvider implements MoveControlPointProvider {

    @Override
    public List<Point> locationSuggested(ConnectionWidget connectionWidget, int index, Point suggestedLocation) {
        IEdgeWidget edgeWidget = (IEdgeWidget) connectionWidget;

//        IModelerScene modelerScene = (IModelerScene)connectionWidget.getScene();
//        modelerScene.setAnchorState(true); //enable Auto Distance Adjust by Anchor
        List<Point> controlPoints = connectionWidget.getControlPoints();
        int cpSize = controlPoints.size() - 1;
        ArrayList<Point> list = new ArrayList<Point>(controlPoints);
        if (index <= 0 || index >= cpSize) {
            return null;
        }
        if (index == 1) {
            INodeWidget sourceNodeWidget = null;
            if (edgeWidget instanceof IFlowEdgeWidget) {
                sourceNodeWidget = (INodeWidget) ((IFlowEdgeWidget) edgeWidget).getSourceWidget();
            } else if (edgeWidget instanceof IArtifactEdgeWidget) {
                IBaseElementWidget elementWidget = ((IArtifactEdgeWidget) edgeWidget).getSourceElementWidget();
                if (elementWidget instanceof INodeWidget) {
                    sourceNodeWidget = (INodeWidget) elementWidget;
                }
            }
            if (sourceNodeWidget != null) {
                sourceNodeWidget.setAnchorState(true);
                list.set(0, connectionWidget.getSourceAnchor().compute(connectionWidget.getSourceAnchorEntry()).getAnchorSceneLocation());
                sourceNodeWidget.setAnchorState(false);
            }
        }
        if (index == cpSize - 1) {
            INodeWidget targetNodeWidget = null;
            if (edgeWidget instanceof IFlowEdgeWidget) {
                targetNodeWidget = (INodeWidget) ((IFlowEdgeWidget) edgeWidget).getTargetWidget();
            } else if (edgeWidget instanceof IArtifactEdgeWidget) {
                IBaseElementWidget elementWidget = ((IArtifactEdgeWidget) edgeWidget).getTargetElementWidget();
                if (elementWidget instanceof INodeWidget) {
                    targetNodeWidget = (INodeWidget) elementWidget;
                }
            }
            if (targetNodeWidget != null) {
                targetNodeWidget.setAnchorState(true);
                list.set(cpSize, connectionWidget.getTargetAnchor().compute(connectionWidget.getTargetAnchorEntry()).getAnchorSceneLocation());
                targetNodeWidget.setAnchorState(false);
            }
        }

        list.set(index, suggestedLocation);

        edgeWidget.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);

        return list;
    }
}
