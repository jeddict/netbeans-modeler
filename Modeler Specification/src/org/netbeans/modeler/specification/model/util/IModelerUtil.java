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

import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.modeler.border.ResizeBorder;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.exception.ProcessInterruptedException;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

public interface IModelerUtil<S extends IModelerScene> {

    void init();

    void loadModelerFile(ModelerFile file) throws ProcessInterruptedException;

    void saveModelerFile(ModelerFile file);
      
    List<IBaseElement> clone(List<IBaseElement> elements);
    
    void loadBaseElement(IBaseElementWidget parentConatiner, Map<IBaseElement,Rectangle> elements);

    public String getContent(ModelerFile file);

    INodeWidget updateNodeWidgetDesign(ShapeDesign shapeDesign, INodeWidget nodeWidget);

    Anchor getAnchor(INodeWidget nodeWidget);

    IEdgeWidget attachEdgeWidget(S scene, EdgeWidgetInfo widgetInfo);

    INodeWidget attachNodeWidget(S scene, NodeWidgetInfo widgetInfo);

    String getEdgeType(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, String connectionContextToolId);

    ResizeBorder getNodeBorder(INodeWidget nodeWidget);

    void transformNode(IFlowNodeWidget flowNodeWidget, IModelerDocument document);

    void attachEdgeSourceAnchor(S scene, IEdgeWidget edgeWidget, INodeWidget sourceNodeWidget);

    void attachEdgeTargetAnchor(S scene, IEdgeWidget edgeWidget, INodeWidget targetNodeWidget);
}
