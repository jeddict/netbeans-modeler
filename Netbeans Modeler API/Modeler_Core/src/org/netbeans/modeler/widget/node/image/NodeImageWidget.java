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
package org.netbeans.modeler.widget.node.image;

import java.awt.Dimension;
import java.util.Iterator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.modeler.anchors.CustomCircularAnchor;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.image.svg.SvgWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 *
 */
public class NodeImageWidget extends SvgWidget {

    private IModelerScene scene;
    private INodeWidget nodeWidget;

    public NodeImageWidget(IModelerScene scene, INodeWidget nodeWidget, SVGDocument doc, Dimension dimension) {
        super(scene, doc, dimension);
        this.scene = scene;
        this.nodeWidget = nodeWidget;
    }

    /**
     * @return the scene
     */
    public IModelerScene getModelerScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setModelerScene(IModelerScene scene) {
        this.scene = scene;
    }

    public void updateWidget(double width, double height, double dimX, double dimY) {
        NodeWidgetInfo nodeWidgetInfo = nodeWidget.getNodeWidgetInfo();
        NodeImageWidget imageWidget = this;

        nodeWidgetInfo.setDimension(new Dimension((int) width, (int) height));
        imageWidget.setDimension(new Dimension((int) width, (int) height));

        if (nodeWidgetInfo.getModelerDocument().getDocumentModel().equals("EVENT")) {//Major Bug : Based on BPN event dependecy for circle shape :  getDocumentModel() == DocumentModelType.EVENT
            Iterator<? extends IFlowEdgeWidget> itr = ((IFlowNodeWidget) nodeWidget).getIncommingFlowEdgeWidget().iterator();
            while (itr.hasNext()) {
                EdgeWidget sequenceFlowWidget = (EdgeWidget) itr.next();
                Anchor targetAnchor;
                targetAnchor = new CustomCircularAnchor(imageWidget.getParentNodeWidget());//,, (int) width / 2
                sequenceFlowWidget.setTargetAnchor(targetAnchor);
            }
            itr = ((IFlowNodeWidget) nodeWidget).getOutgoingFlowEdgeWidget().iterator();
            while (itr.hasNext()) {
                EdgeWidget sequenceFlowWidget = (EdgeWidget) itr.next();
                Anchor targetAnchor;
                targetAnchor = new CustomCircularAnchor(imageWidget.getParentNodeWidget());//, (int) width / 2
                sequenceFlowWidget.setSourceAnchor(targetAnchor);
            }
        }
    }

    /**
     * @return the nodeWidget
     */
    public INodeWidget getParentNodeWidget() {
        return nodeWidget;
    }
}
