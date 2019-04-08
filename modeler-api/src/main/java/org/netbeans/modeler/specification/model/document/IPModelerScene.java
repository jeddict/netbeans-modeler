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
package org.netbeans.modeler.specification.model.document;

import java.util.Map;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;

public interface IPModelerScene<E extends IRootElement> extends IModelerScene<E> {

    IPinWidget createPinWidget(NodeWidgetInfo node, PinWidgetInfo pin);

    void setEdgeWidgetSource(EdgeWidgetInfo edge, PinWidgetInfo pin);

    void setEdgeWidgetTarget(EdgeWidgetInfo edge, PinWidgetInfo pin);

    void deletePinWidget(IPinWidget pinWidget);

    Anchor getPinAnchor(IPinWidget pin);
    
    IColorScheme getColorScheme();

    IColorScheme getColorScheme(String defaultTheme);

    void setColorScheme(Class<? extends IColorScheme> scheme);

    Map<String, Class<? extends IColorScheme>> getColorSchemes();

}
