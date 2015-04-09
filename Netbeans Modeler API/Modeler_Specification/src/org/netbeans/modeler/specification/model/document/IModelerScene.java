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
package org.netbeans.modeler.specification.model.document;

import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.visual.IObjectScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.context.ContextPaletteManager;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.AbstractNode;

public interface IModelerScene extends IBaseElementWidget, IObjectScene {

    public String getName();

    public void setName(String name);

    public IRootElement getRootElementSpec();

    public void setRootElementSpec(IRootElement rootElementSpec);

    /**
     * To move INodeWidget from IModelerSubScene to IModelerScene
     *
     * @param baseElementWidget
     */
    public void addBaseElement(IBaseElementWidget baseElementWidget);

    /**
     * To move INodeWidget from IModelerScene to IModelerSubScene
     *
     * @param baseElementWidget
     */
    public void removeBaseElement(IBaseElementWidget baseElementWidget);

    /**
     * To create INodeWidget in IModelerScene
     *
     * @param baseElementWidget
     */
    public void createBaseElement(IBaseElementWidget baseElementWidget);

    /**
     * To delete INodeWidget in IModelerScene
     *
     * @param baseElementWidget
     */
    public void deleteBaseElement(IBaseElementWidget baseElementWidget);

    public IBaseElementWidget findBaseElement(String id);

    public List<IBaseElementWidget> getBaseElements();

    public IBaseElementWidget getBaseElement(String id);

  
    /* Abstract Modeler Scene */
    public ModelerFile getModelerFile();

    public void setModelerFile(ModelerFile modelerFile);

    public ContextPaletteManager getContextPaletteManager();

    public IModelerPanel getModelerPanelTopComponent();

    public void setModelerPanelTopComponent(IModelerPanel topComponent);

    public LayerWidget getConnectionLayer();

    public LayerWidget getInterractionLayer();

    public LayerWidget getMainLayer();

    public LayerWidget getBackgroundLayer();

    public LayerWidget getBoundaryWidgetLayer();

    PopupMenuProvider getPopupMenuProvider();

    public void manageLayerWidget();

    public Router getRouter();

    public INodeWidget getValidNodeWidget();

    public void setValidNodeWidget(INodeWidget validNodeWidget);

    public INodeWidget getInvalidNodeWidget();

    public void setInvalidNodeWidget(INodeWidget invalidNodeWidget);

    public INodeWidget createNodeWidget(NodeWidgetInfo node);

    public IEdgeWidget createEdgeWidget(EdgeWidgetInfo edge);

    void deleteNodeWidget(INodeWidget nodeWidget);

    void deleteEdgeWidget(IEdgeWidget edgeWidget);

    public boolean isAlignSupport();

    public void setAlignSupport(boolean alignSupport);



//    AbstractNode getNode();
//
//    void setNode(AbstractNode node);

    LayerWidget getLabelLayer();

    void setLabelLayer(LayerWidget labelLayer);

    JComponent getSatelliteView();

    /* GraphScene & GraphPinScene */
    boolean isNode(Object object);

    public boolean isEdge(Object object);

    void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(String id);

    Map<String, PropertyChangeListener> getPropertyChangeListeners();

    void addPropertyVisibilityHandler(String id, PropertyVisibilityHandler propertyVisibilityHandler);

    void removePropertyVisibilityHandler(String id);

    Map<String, PropertyVisibilityHandler> getPropertyVisibilityHandlers();

    public void autoLayout();

    public void setModelerDiagramEngine(IModelerDiagramEngine modelerDiagramEngine);

    
        //custom added
    void showProperties();

    void exploreProperties();

    void refreshProperties();
}
