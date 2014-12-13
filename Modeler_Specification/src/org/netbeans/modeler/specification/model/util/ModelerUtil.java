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
package org.netbeans.modeler.specification.model.util;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.modeler.border.ResizeBorder;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

public interface ModelerUtil {

    public void loadModelerFile(ModelerFile file);

    public void saveModelerFile(ModelerFile file);

    public INodeWidget updateNodeWidgetDesign(ShapeDesign shapeDesign, INodeWidget nodeWidget);

    public Anchor getAnchor(INodeWidget nodeWidget);

    public IEdgeWidget attachEdgeWidget(IModelerScene scene, EdgeWidgetInfo widgetInfo);

    public INodeWidget attachNodeWidget(IModelerScene scene, NodeWidgetInfo widgetInfo);

    public String getEdgeType(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, String connectionContextToolId);

    public ResizeBorder getNodeBorder(INodeWidget nodeWidget);

    public void transformNode(IFlowNodeWidget flowNodeWidget, IModelerDocument document);

    public void attachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget sourceNodeWidget);

    public void attachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget targetNodeWidget);
}
