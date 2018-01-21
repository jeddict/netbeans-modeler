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
package org.netbeans.modeler.provider.connection;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.widget.IEndFlowNodeWidget;
import org.netbeans.modeler.specification.model.document.widget.IStartFlowNodeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.AbstractNodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;

/**
 *
 *
 */
public class SequenceFlowConnectionProvider implements ConnectProvider {

    private Scene scene;

    public SequenceFlowConnectionProvider(Scene scene) {
        this.scene = scene;
    }

    @Override
    public boolean isSourceWidget(Widget source) {
        return source != null && source instanceof AbstractNodeWidget ? true : false;
    }

    @Override
    public ConnectorState isTargetWidget(Widget src, Widget trg) {
        if (trg instanceof IStartFlowNodeWidget || src instanceof IEndFlowNodeWidget) {
            return ConnectorState.REJECT;
        } else {
            return src != trg && trg instanceof AbstractNodeWidget ? ConnectorState.ACCEPT : ConnectorState.REJECT;
        }
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene arg0) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene arg0, Point arg1) {
        return null;
    }

    @Override
    public void createConnection(Widget source, Widget target) {
        EdgeWidgetInfo edge = new EdgeWidgetInfo();
        edge.setSource(((NodeWidget) source).getNodeWidgetInfo().getId());
        edge.setTarget(((NodeWidget) target).getNodeWidgetInfo().getId());

        ((GraphScene) scene).addEdge(edge);
        ((GraphScene) scene).setEdgeSource(edge, ((NodeWidget) source).getNodeWidgetInfo());
        ((GraphScene) scene).setEdgeTarget(edge, ((NodeWidget) target).getNodeWidgetInfo());
    }
}
