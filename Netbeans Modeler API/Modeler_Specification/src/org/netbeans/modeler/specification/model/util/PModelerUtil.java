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

import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;

public interface PModelerUtil<S extends IModelerScene> extends IModelerUtil<S> {

    public IPinWidget attachPinWidget(S scene, INodeWidget nodeWidget, PinWidgetInfo widgetInfo);

    public void dettachEdgeSourceAnchor(S scene, IEdgeWidget edgeWidget, IPinWidget sourcePinWidget);

    public void dettachEdgeTargetAnchor(S scene, IEdgeWidget edgeWidget, IPinWidget targetPinWidget);

    public void attachEdgeSourceAnchor(S scene, IEdgeWidget edgeWidget, IPinWidget sourcePinWidget);

    public void attachEdgeTargetAnchor(S scene, IEdgeWidget edgeWidget, IPinWidget targetPinWidget);

    PinWidgetInfo getEdgeSourcePinWidget(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, IEdgeWidget edgeWidget);

    PinWidgetInfo getEdgeTargetPinWidget(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, IEdgeWidget edgeWidget);

}
