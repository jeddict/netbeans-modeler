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
package org.netbeans.modeler.core;

import org.netbeans.api.project.FileOwnerQuery;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.file.IModelerFileDataObject;
import org.netbeans.modeler.specification.model.ModelerDiagramSpecification;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.specification.version.SoftwareVersion;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modules.websvc.saas.codegen.java.support.SourceGroupSupport;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.NotifyDescriptor;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

public class ModelerFile {

    private String id;
    private String name;
    private String tooltip;
    private String extension;
    private String path;
    private boolean loaded = false;

    private Image icon;
    private IModelerFileDataObject modelerFileDataObject;
    private ModelerDiagramSpecification modelerSpecificationDiagramModel;

    private Map<String, Object> attributes = new HashMap<>();
    private ModelerFile parentFile;
    private Set<ModelerFile> childrenFile = new HashSet<>();

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
        if(this.getModelerFileDataObject() == null){
            return null;
        }
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
        if(modelerFileDataObject == null){
            return;
        }
        this.modelerFileDataObject = modelerFileDataObject;
        FileObject fileObject = modelerFileDataObject.getPrimaryFile();
        this.setName(fileObject.getName());
        this.setExtension(fileObject.getExt());
        this.setIcon(modelerFileDataObject.getIcon());
    }

    private Project project;
    public Project getProject() {
        if(project==null){
        project = FileOwnerQuery.getOwner(modelerFileDataObject.getPrimaryFile());
        }
        return project;
    }
  
    private SourceGroup sourceGroup;
    public SourceGroup getSourceGroup() {
        if (sourceGroup == null) {
            sourceGroup = SourceGroupSupport.findSourceGroupForFile(getProject(), getModelerFileDataObject().getPrimaryFile());
        }
        return sourceGroup;
    }

    /**
     * @return the modelerUtil
     */
    public IModelerUtil getModelerUtil() {
        return this.getModelerDiagramModel().getModelerUtil();
    }
    
        /**
     * @return the modelerSpecificationDiagramModel
     */
    public ModelerDiagramSpecification getModelerDiagramModel() {
        if(modelerSpecificationDiagramModel==null){
            modelerSpecificationDiagramModel = new ModelerDiagramSpecification();
        }
        return modelerSpecificationDiagramModel;
    }

    /**
     * @param modelerSpecificationDiagramModel the
     * modelerSpecificationDiagramModel to set
     */
    public void setModelerDiagramModel(ModelerDiagramSpecification modelerSpecificationDiagramModel) {
        this.modelerSpecificationDiagramModel = modelerSpecificationDiagramModel;
    }

    /**
     * @return the modelerScene
     */
    public IModelerScene getModelerScene() {
        return this.getModelerDiagramModel().getModelerScene();
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
        return this.getModelerDiagramModel().getModelerDiagramEngine();
    }

    /**
     * @return the definitionElement
     */
    public IDefinitionElement getDefinitionElement() {
        return this.getModelerDiagramModel().getDefinitionElement();
    }

    public void handleException(Throwable throwable) {
        if (getModelerDiagramModel().getExceptionHandler() != null) {
            getModelerDiagramModel().getExceptionHandler().handle(throwable, this);
        } else {
            throwable.printStackTrace();
        }
    }

    public void save() {
        if (this.getModelerFileDataObject() != null) {
            SaveCookie cookie = this.getModelerFileDataObject().getCookie(SaveCookie.class);
            try {
                if (cookie != null) {
                    cookie.save();
                }
            } catch (IOException e) {
                ErrorManager.getDefault().notify(e);
            }
        }
    }
    
    public void close(){
        getModelerPanelTopComponent().close();
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
            d = new NotifyDescriptor.Confirmation("are you sure you want to delete these Elements ?", "Delete Elements", NotifyDescriptor.OK_CANCEL_OPTION);
        } else if (nodeWidgets.size() + edgeWidgets.size() == 1) {
            if (nodeWidgets.size() == 1) {
                d = new NotifyDescriptor.Confirmation(String.format("are you sure you want to delete %s ?", nodeWidgets.get(0).getLabel()), String.format("Delete ", nodeWidgets.get(0).getLabel()), NotifyDescriptor.OK_CANCEL_OPTION);
            } else {
                d = new NotifyDescriptor.Confirmation("are you sure you want to delete this Element ?", "Delete Element", NotifyDescriptor.OK_CANCEL_OPTION);
            }

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

    /**
     * @return the parentFile
     */
    public ModelerFile getParentFile() {
        return parentFile;
    }

    /**
     * @param parentFile the parentFile to set
     */
    public void setParentFile(ModelerFile parentFile) {
        this.parentFile = parentFile;
    }

    /**
     * @return the childrenFile
     */
    public Set<ModelerFile> getChildrenFile() {
        return childrenFile;
    }

    /**
     * @param childrenFile the childrenFile to set
     */
    public void setChildrenFile(Set<ModelerFile> childrenFile) {
        this.childrenFile = childrenFile;
    }

    /**
     * @param id
     * @return the childrenFile
     */
    public Optional<ModelerFile> getChildrenFile(String id) {
        return childrenFile.stream().filter(c -> id.equals(c.getId())).findFirst();
    }

    /**
     * @param file the childrenFile to add
     */
    public void addChildrenFile(ModelerFile file) {
        this.childrenFile.add(file);
    }

    /**
     * @param file the childrenFile to add
     */
    public void removeChildrenFile(ModelerFile file) {
        this.childrenFile.remove(file);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelerFile other = (ModelerFile) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the load
     */
    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        if (loaded) {
            throw new IllegalStateException("Modeler File already loaded");
        }
        this.loaded = true;
    }

    public void unload() {
        this.loaded = false;
    }

    public String getContent() {
        return getModelerUtil().getContent(this);
    }

    public String getFileContent() {
        try {
            return new String(this.getFileObject().asBytes(), Charset.defaultCharset());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    public SoftwareVersion getCurrentVersion() {
        return this.getModelerDiagramModel().getDiagramModel().getVersion();
    }

    
    public SoftwareVersion getArchitectureVersion() {
        return this.getModelerDiagramModel().getDiagramModel().getArchitectureVersion();
    }

}
