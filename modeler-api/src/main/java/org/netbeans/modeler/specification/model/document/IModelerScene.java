/**
 * Copyright 2013-2018 Gaurav Gupta
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

import java.util.List;
import javax.swing.JComponent;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.modeler.action.WidgetDropListener;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.visual.IObjectScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.context.ContextPaletteManager;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.property.IPropertyWidget;

public interface IModelerScene<E extends IRootElement> extends IBaseElementWidget<E>, IPropertyWidget, IObjectScene {

    String getName();

    void setName(String name);

    /**
     * To move INodeWidget from IModelerSubScene to IModelerScene
     *
     * @param baseElementWidget
     */
    void addBaseElement(IBaseElementWidget baseElementWidget);

    /**
     * To move INodeWidget from IModelerScene to IModelerSubScene
     *
     * @param baseElementWidget
     */
    void removeBaseElement(IBaseElementWidget baseElementWidget);

    /**
     * To create new INodeWidget in IModelerScene
     *
     * @param baseElementWidget
     */
    void createBaseElement(IBaseElementWidget baseElementWidget);

    /**
     * To delete INodeWidget in IModelerScene
     *
     * @param baseElementWidget
     */
    void deleteBaseElement(IBaseElementWidget baseElementWidget);

    List<IBaseElementWidget> getBaseElements();

    IBaseElementWidget getBaseElement(String id);

    ModelerFile getModelerFile();

    void setModelerFile(ModelerFile modelerFile);

    ContextPaletteManager getContextPaletteManager();

    IModelerPanel getModelerPanelTopComponent();

    void setModelerPanelTopComponent(IModelerPanel topComponent);

    LayerWidget getConnectionLayer();

    LayerWidget getInterractionLayer();

    LayerWidget getMainLayer();

    LayerWidget getBackgroundLayer();

    void manageLayerWidget();

    Router getRouter();

    INodeWidget getValidNodeWidget();

    void setValidNodeWidget(INodeWidget validNodeWidget);

    INodeWidget getInvalidNodeWidget();

    void setInvalidNodeWidget(INodeWidget invalidNodeWidget);

    INodeWidget createNodeWidget(NodeWidgetInfo node);

    IEdgeWidget createEdgeWidget(EdgeWidgetInfo edge);

    void deleteNodeWidget(INodeWidget nodeWidget);

    void deleteEdgeWidget(IEdgeWidget edgeWidget);

    boolean isAlignSupport();

    void setAlignSupport(boolean alignSupport);

    LayerWidget getLabelLayer();

    void setLabelLayer(LayerWidget labelLayer);

    JComponent getSatelliteView();

    /* GraphScene & GraphPinScene */
    boolean isNode(Object object);

    boolean isEdge(Object object);

    void autoLayout();

    void cleanReference();

    boolean isSceneGenerating();

    List<WidgetDropListener> getWidgetDropListener();
}
