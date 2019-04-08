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
package org.netbeans.modeler.core;

import javax.swing.JToolBar;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;

public interface IModelerDiagramEngine {

    void buildToolBar(JToolBar bar);
    void cleanToolBar(JToolBar bar);

    void init(ModelerFile file);

    void setNodeWidgetAction(INodeWidget nodeWidget);
    void clearNodeWidgetAction(INodeWidget nodeWidget);

    void setEdgeWidgetAction(IEdgeWidget edgeWidget);
    void clearEdgeWidgetAction(IEdgeWidget edgeWidget);

    void setPinWidgetAction(IPinWidget pinWidget);
    void clearPinWidgetAction(IPinWidget pinWidget);

    void setModelerSceneAction();
    void clearModelerSceneAction();
    
    IZoomManager getZoomManager();
    
    void searchWidget();
    void moveToWidget(INodeWidget widget);

}
