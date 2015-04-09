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
package org.netbeans.modeler.specification.model;

import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;
import org.netbeans.modeler.specification.model.document.IDiagramElement;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.util.ModelerUtil;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;

public abstract class ModelerSpecificationDiagramModel {

    private DiagramModel diagramModel;
    private ModelerUtil modelerUtil;

    private IModelerScene modelerScene;
    private IModelerPanel modelerPanel;
    private IModelerDiagramEngine modelerDiagramEngine;

    private IDefinitionElement definitionElement;
    private IDiagramElement diagramElement;
    private IRootElement rootElement;

    private IRelationValidator relationValidator;

    public abstract void init(ModelerFile modelerFile);//Load Default Value Like Definition , RootElement , Design , will be raplaced if XML Content exist

    /**
     * @return the modelerScene
     */
    public IModelerScene getModelerScene() {
        return modelerScene;
    }

    /**
     * @param modelerScene the modelerScene to set
     */
    public void setModelerScene(IModelerScene modelerScene) {
        this.modelerScene = modelerScene;
    }

    /**
     * @return the ModelerPanelTopComponent
     */
    public IModelerPanel getModelerPanelTopComponent() {
        return modelerPanel;
    }

    /**
     * @param modelerPanel the ModelerPanelTopComponent to set
     */
    public void setModelerPanelTopComponent(IModelerPanel modelerPanel) {
        this.modelerPanel = modelerPanel;
    }

    /**
     * @return the modelerDiagramEngine
     */
    public IModelerDiagramEngine getModelerDiagramEngine() {
        return modelerDiagramEngine;
    }

    /**
     * @param modelerDiagramEngine the modelerDiagramEngine to set
     */
    public void setModelerDiagramEngine(IModelerDiagramEngine modelerDiagramEngine) {
        this.modelerDiagramEngine = modelerDiagramEngine;
    }

    /**
     * @return the definitionElement
     */
    public IDefinitionElement getDefinitionElement() {
        return definitionElement;
    }

    /**
     * @param definitionElement the definitionElement to set
     */
    public void setDefinitionElement(IDefinitionElement definitionElement) {
        this.definitionElement = definitionElement;
    }

    /**
     * @return the diagramElement
     */
    public IDiagramElement getDiagramElement() {
        return diagramElement;
    }

    /**
     * @param diagramElement the diagramElement to set
     */
    public void setDiagramElement(IDiagramElement diagramElement) {
        this.diagramElement = diagramElement;
    }

    /**
     * @return the rootElement
     */
    public IRootElement getRootElement() {
        return rootElement;
    }

    /**
     * @param rootElement the rootElement to set
     */
    public void setRootElement(IRootElement rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * @return the modelerUtil
     */
    public ModelerUtil getModelerUtil() {
        return modelerUtil;
    }

    /**
     * @param modelerUtil the modelerUtil to set
     */
    public void setModelerUtil(ModelerUtil modelerUtil) {
        this.modelerUtil = modelerUtil;
    }

    /**
     * @return the relationValidator
     */
    public IRelationValidator getRelationValidator() {
        return relationValidator;
    }

    /**
     * @param relationValidator the relationValidator to set
     */
    public void setRelationValidator(IRelationValidator relationValidator) {
        this.relationValidator = relationValidator;
    }

    /**
     * @return the diagramModel
     */
    public DiagramModel getDiagramModel() {
        return diagramModel;
    }

    /**
     * @param diagramModel the diagramModel to set
     */
    public void setDiagramModel(DiagramModel diagramModel) {
        this.diagramModel = diagramModel;
    }

}
