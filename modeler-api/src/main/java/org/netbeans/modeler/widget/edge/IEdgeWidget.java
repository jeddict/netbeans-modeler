/**
 * Copyright 2013-2019 Gaurav Gupta
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
package org.netbeans.modeler.widget.edge;

import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;
import org.netbeans.modeler.widget.property.IPropertyWidget;

public interface IEdgeWidget<S extends IModelerScene> extends IConnectionWidget, IPropertyWidget {

    void createVisualPropertySet(ElementPropertySet elementPropertySet);

    EdgeWidgetInfo getEdgeWidgetInfo();

    void hideLabel();

    void manageControlPoint();

    void setLabel(String label);

    void setModelerScene(S scene);

    void showLabel();

    boolean remove();

    boolean remove(boolean notification);

    boolean isLocked();

    void setLocked(boolean locked);

    void cleanReference();

    PinWidgetInfo getSourcePinWidget(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget);

    PinWidgetInfo getSourcePinWidget(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, IPinWidget sourcePinWidget);

    PinWidgetInfo getTargetPinWidget(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget);

    PinWidgetInfo getTargetPinWidget(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, IPinWidget targetPinWidget);

    void dettachEdgeSourceAnchor(IWidget sourcePinWidget);

    void dettachEdgeTargetAnchor(IWidget targetPinWidget);

    void attachEdgeSourceAnchor(IPinWidget sourcePinWidget);

    void attachEdgeTargetAnchor(IPinWidget targetPinWidget);

    void attachEdgeSourceAnchor(INodeWidget sourceNodeWidget);

    void attachEdgeTargetAnchor(INodeWidget targetNodeWidget);
}
