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
package org.netbeans.modeler.widget.node;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.modeler.border.ResizeBorder;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.nodes.AbstractNode;
//import org.netbeans.modules.visual.border.ResizeBorder;

public interface INodeWidget extends IWidget {

    public static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(0);

    IModelerScene getModelerScene();

//    void setModelerScene(IModelerScene scene);
    public void setStatus(NodeWidgetStatus status);

    ElementPropertySet createVisualInnerPropertiesSet(ElementPropertySet elementPropertySet) throws NoSuchMethodException, NoSuchFieldException;

    ElementPropertySet createVisualOuterPropertiesSet(ElementPropertySet elementPropertySet) throws NoSuchMethodException, NoSuchFieldException;

    public NodeWidgetInfo getNodeWidgetInfo();

    public Rectangle getWidgetInnerArea();

    public Dimension getWidgetInnerDimension();

    public ResizeBorder getWidgetBorder();

    public void setWidgetBorder(ResizeBorder widgetBorder);

    PopupMenuProvider getPopupMenuProvider();

    void showProperties();

    void exploreProperties();

    void showResizeBorder();

    void hideResizeBorder();

    public boolean isAnchorEnable();

    public void setAnchorState(boolean state);

    ContextPaletteModel getContextPaletteModel();

    AbstractNode getNode();

    void setNode(AbstractNode node);

    Rectangle getSceneViewBound();

    void setLabel(String label);

    String getLabel();

    void hideLabel();

    void showLabel();

//    LabelManager getLabelManager();
    void remove();

    void remove(boolean notification);

    public boolean isActiveStatus();

    public void setActiveStatus(boolean activeStatus);

    void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(String id);

    Map<String, PropertyChangeListener> getPropertyChangeListeners();

    boolean isLocked();

    void setLocked(boolean locked);

}
