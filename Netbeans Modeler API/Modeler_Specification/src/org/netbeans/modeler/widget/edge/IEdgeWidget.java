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

import java.util.Map;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.AbstractNode;

public interface IEdgeWidget<S extends IModelerScene> extends IConnectionWidget {

    void createVisualPropertySet(ElementPropertySet elementPropertySet);

    EdgeWidgetInfo getEdgeWidgetInfo();

    S getModelerScene();

//    AbstractNode getNode();
    PopupMenuProvider getPopupMenuProvider();

    void hideLabel();

    void manageControlPoint();

    void setLabel(String label);

    void setModelerScene(S scene);

//    void setNode(AbstractNode node);
    void showLabel();

    boolean remove();

    boolean remove(boolean notification);

    boolean isLocked();

    void setLocked(boolean locked);

    void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(String id);

    Map<String, PropertyChangeListener> getPropertyChangeListeners();

    void addPropertyVisibilityHandler(String id, PropertyVisibilityHandler propertyVisibilityHandler);

    void removePropertyVisibilityHandler(String id);

    Map<String, PropertyVisibilityHandler> getPropertyVisibilityHandlers();

}
