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
package org.netbeans.modeler.widget.pin;

import java.util.Map;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.AbstractNode;

public interface IPinWidget extends IParentPinWidget {

    public PinWidgetInfo getPinWidgetInfo();

    IModelerScene getModelerScene();

    PopupMenuProvider getPopupMenuProvider();

    void showProperties();

    void exploreProperties();

    AbstractNode getNode();

    void setNode(AbstractNode node);

    boolean remove();

    boolean remove(boolean notification);

    public boolean isActiveStatus();

    public void setActiveStatus(boolean activeStatus);

    void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(String id);

    Map<String, PropertyChangeListener> getPropertyChangeListeners();

    void addPropertyVisibilityHandler(String id, PropertyVisibilityHandler propertyVisibilityHandler);

    void removePropertyVisibilityHandler(String id);

    Map<String, PropertyVisibilityHandler> getPropertyVisibilityHandlers();

    void setLabel(String label);

    String getLabel();

    IPNodeWidget getPNodeWidget();

    void setPNodeWidget(IPNodeWidget nodeWidget);

    boolean isLocked();

    void setLocked(boolean locked);

    public IColorScheme getColorScheme();

    public void setColorScheme(IColorScheme colorScheme);

    boolean isHighlightStatus();

    void setHighlightStatus(boolean highlightStatus);

}
