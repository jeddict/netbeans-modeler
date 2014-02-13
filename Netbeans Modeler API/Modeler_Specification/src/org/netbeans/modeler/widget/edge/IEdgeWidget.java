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
package org.netbeans.modeler.widget.edge;

import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.openide.nodes.AbstractNode;

public interface IEdgeWidget extends IConnectionWidget {

    void createVisualPropertySet(ElementPropertySet elementPropertySet);

    void exploreProperties();

    EdgeWidgetInfo getEdgeWidgetInfo();

    IModelerScene getModelerScene();

    AbstractNode getNode();

    PopupMenuProvider getPopupMenuProvider();

    void hideLabel();

    void manageControlPoint();

    void setLabel(String label);

    void setModelerScene(IModelerScene scene);

    void setNode(AbstractNode node);

    void showLabel();

    void showProperties();

    void remove();

    void remove(boolean notification);

    boolean isLocked();

    void setLocked(boolean locked);

}
