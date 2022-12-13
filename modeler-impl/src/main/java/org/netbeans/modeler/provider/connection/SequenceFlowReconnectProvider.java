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
package org.netbeans.modeler.provider.connection;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.scene.AbstractModelerScene;
import org.netbeans.modeler.scene.vmd.AbstractPModelerScene;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.svg.SvgNodeWidget;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

public class SequenceFlowReconnectProvider implements ReconnectProvider {

    private EdgeWidgetInfo edge;
    private Object originalNode;
    private Object replacementNode;//NodeWidgetInfo
    private final IModelerScene scene;

    public SequenceFlowReconnectProvider(IModelerScene scene) {
        this.scene = scene;
    }

    @Override
    public boolean isSourceReconnectable(ConnectionWidget connectionWidget) {
        Object object = scene.findObject(connectionWidget);
        if (scene instanceof AbstractModelerScene) {
            edge = ((AbstractModelerScene) scene).isEdge(object) ? (EdgeWidgetInfo) object : null;
            originalNode = edge != null ? ((AbstractModelerScene) scene).getEdgeSource(edge) : null;
        } else if (scene instanceof AbstractPModelerScene) {
            edge = ((AbstractPModelerScene) scene).isEdge(object) ? (EdgeWidgetInfo) object : null;
            originalNode = edge != null ? ((AbstractPModelerScene) scene).getEdgeSource(edge) : null;
        }
        return originalNode != null;
    }

    @Override
    public boolean isTargetReconnectable(ConnectionWidget connectionWidget) {
        Object object = scene.findObject(connectionWidget);
        if (scene instanceof AbstractModelerScene) {
            edge = ((AbstractModelerScene) scene).isEdge(object) ? (EdgeWidgetInfo) object : null;
            originalNode = edge != null ? ((AbstractModelerScene) scene).getEdgeTarget(edge) : null;
        } else if (scene instanceof AbstractPModelerScene) {
            edge = ((AbstractPModelerScene) scene).isEdge(object) ? (EdgeWidgetInfo) object : null;
            originalNode = edge != null ? ((AbstractPModelerScene) scene).getEdgeTarget(edge) : null;
        }
        return originalNode != null;
    }

    @Override
    public void reconnectingStarted(ConnectionWidget connectionWidget, boolean reconnectingSource) {
    }

    @Override
    public void reconnectingFinished(ConnectionWidget connectionWidget, boolean reconnectingSource) {
    }

    @Override
    public ConnectorState isReplacementWidget(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
        Object object = scene.findObject(replacementWidget);
        replacementNode = scene.isNode(object) ? (NodeWidgetInfo) object : null;
        if (replacementNode != null) {
            return ConnectorState.ACCEPT;
        }
        return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomReplacementWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveReplacementWidget(Scene scene, Point sceneLocation) {
        return null;
    }

    @Override
    public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
        EdgeWidget edgeWidget = (EdgeWidget) connectionWidget;
        NodeWidget nodeWidget = (NodeWidget) ((SvgNodeWidget) replacementWidget).getParentNodeWidget();
        if (replacementWidget == null) {
            ((IEdgeWidget) edgeWidget).remove(true);
        } else if (reconnectingSource) {
            edgeWidget.dettachEdgeSourceAnchor(((IFlowEdgeWidget) edgeWidget).getSourceWidget());
            edgeWidget.attachEdgeSourceAnchor(nodeWidget);
        } else {
            edgeWidget.dettachEdgeTargetAnchor(((IFlowEdgeWidget) edgeWidget).getTargetWidget());
            edgeWidget.attachEdgeTargetAnchor(nodeWidget);
        }
        scene.validate();
    }
}
