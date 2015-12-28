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
package org.netbeans.modeler.core;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.file.IModelerFileDataObject;
import org.netbeans.modeler.specification.ModelerVendorSpecification;
import org.netbeans.modeler.specification.model.ModelerDiagramSpecification;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.specification.model.util.NModelerUtil;
import org.netbeans.modeler.specification.model.util.PModelerUtil;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.NotifyDescriptor;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 *
 */
public class ModelerFile {

    private String id;
    private String name;
    private String tooltip;
    private String extension;
    private String path;
    private Image icon;
    private IModelerFileDataObject modelerFileDataObject;
    private ModelerVendorSpecification modelerVendorSpecification;
    private Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the icon
     */
    public Image getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public FileObject getFileObject() {
        return this.getModelerFileDataObject().getPrimaryFile();
    }

    public File getFile() {
        return FileUtil.toFile(this.getModelerFileDataObject().getPrimaryFile());
    }

    /**
     * @return the modelerFileDataObject
     */
    public IModelerFileDataObject getModelerFileDataObject() {
        return modelerFileDataObject;
    }

    /**
     * @param modelerFileDataObject the modelerFileDataObject to set
     */
    public void setModelerFileDataObject(IModelerFileDataObject modelerFileDataObject) {
        this.modelerFileDataObject = modelerFileDataObject;
        FileObject fileObject = modelerFileDataObject.getPrimaryFile();
        this.setName(fileObject.getName());
        this.setExtension(fileObject.getExt());
        this.setIcon(modelerFileDataObject.getIcon());
    }

    /**
     * @return the modelerVendorSpecification
     */
    public ModelerVendorSpecification getVendorSpecification() {
        if(modelerVendorSpecification == null){
            modelerVendorSpecification = new ModelerVendorSpecification();
            modelerVendorSpecification.setModelerSpecificationDiagramModel(new ModelerDiagramSpecification());
        }
        return modelerVendorSpecification;
    }

    /**
     * @param modelerVendorSpecification the modelerVendorSpecification to set
     */
    public void setModelerVendorSpecification(ModelerVendorSpecification modelerVendorSpecification) {
        this.modelerVendorSpecification = modelerVendorSpecification;
    }

    /**
     * @return the modelerUtil
     */
    public IModelerUtil getModelerUtil() {
        return this.getVendorSpecification().getModelerDiagramModel().getModelerUtil();
    }

    /**
     * These two method are for convenience *
     */
    public NModelerUtil getNModelerUtil() {
        return (NModelerUtil) this.getVendorSpecification().getModelerDiagramModel().getModelerUtil();
    }

    public PModelerUtil getPModelerUtil() {
        return (PModelerUtil) this.getVendorSpecification().getModelerDiagramModel().getModelerUtil();
    }

    /**
     * @return the modelerScene
     */
    public IModelerScene getModelerScene() {
        return this.getVendorSpecification().getModelerDiagramModel().getModelerScene();
    }

    /**
     * @return the ModelerPanelTopComponent
     */
    public IModelerPanel getModelerPanelTopComponent() {
        return this.getModelerScene().getModelerPanelTopComponent();
    }

    /**
     * @return the modelerDiagramEngine
     */
    public IModelerDiagramEngine getModelerDiagramEngine() {
        return this.getVendorSpecification().getModelerDiagramModel().getModelerDiagramEngine();
    }

    /**
     * @return the definitionElement
     */
    public IDefinitionElement getDefinitionElement() {
        return this.getVendorSpecification().getModelerDiagramModel().getDefinitionElement();
    }


    public ModelerDiagramSpecification getModelerDiagramModel() {
        return this.getVendorSpecification().getModelerDiagramModel();
    }

    public void save() {
        SaveCookie cookie = this.getModelerFileDataObject().getCookie(SaveCookie.class);
        try {
            if (cookie != null) {
                cookie.save();
            }
        } catch (IOException e) {
            ErrorManager.getDefault().notify(e);
        }
    }

    public void deleteSelectedElements() {
        IModelerScene scene = this.getModelerScene();

        List<INodeWidget> nodeWidgets = new ArrayList<INodeWidget>();
        for (Object o : scene.getSelectedObjects()) {
            if (scene.isNode(o)) {
                Widget w = scene.findWidget(o);
                if (w instanceof INodeWidget) {
                    INodeWidget nodeWidget = (INodeWidget) w;
                    nodeWidgets.add(nodeWidget);
                }
            }
        }
        List<IEdgeWidget> edgeWidgets = new ArrayList<IEdgeWidget>();
        for (Object o : scene.getSelectedObjects()) {
            if (o instanceof EdgeWidgetInfo) {
                Widget w = scene.findWidget(o);
                if (w instanceof IEdgeWidget) {
                    IEdgeWidget edgeWidget = (IEdgeWidget) w;
                    edgeWidgets.add(edgeWidget);
                }
            }
        }
        NotifyDescriptor d = null;
        if (nodeWidgets.size() + edgeWidgets.size() > 1) {
            d = new NotifyDescriptor.Confirmation("are you sure you want to delete these Elements?", "Delete Elements", NotifyDescriptor.OK_CANCEL_OPTION);
        } else if (nodeWidgets.size() + edgeWidgets.size() == 1) {
            d = new NotifyDescriptor.Confirmation("are you sure you want to delete this Element?", "Delete Element", NotifyDescriptor.OK_CANCEL_OPTION);
        }
        if (nodeWidgets.size() + edgeWidgets.size() >= 1) {
            if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
                // Issue Fix #5869 Start
                /**
                 * #5869 FIX fixed NullPointerException during delete process
                 *
                 * @author Juraj Balaz <georgeeb@java.net>
                 * @since Thu, 17 Apr 2014 10:39:05 +0000
                 */
                for (IEdgeWidget edgeWidget : new CopyOnWriteArrayList<IEdgeWidget>(edgeWidgets)) {
                    if (edgeWidget.getModelerScene().isEdge(edgeWidget.getEdgeWidgetInfo())) {
                        edgeWidget.remove();
                    }
                }
                for (INodeWidget nodeWidget : new CopyOnWriteArrayList<INodeWidget>(nodeWidgets)) {
                    if (nodeWidget.getModelerScene().isNode(nodeWidget.getNodeWidgetInfo())) {
                        nodeWidget.remove();
                    }
                }
                // Issue Fix #5869 Start

                scene.validate();
            }
        }
    }

    /**
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public void removeAttribute(String key, Object value) {
        this.attributes.remove(key);
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
