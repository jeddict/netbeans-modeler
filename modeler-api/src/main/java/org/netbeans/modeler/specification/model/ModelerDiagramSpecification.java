/**
 * Copyright 2013-2022 Gaurav Gupta
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

import org.netbeans.modeler.config.document.ModelerDocumentFactory;
import org.netbeans.modeler.config.document.ModelerDocumentFactoryRepository;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.config.element.ElementConfigFactoryRepository;
import org.netbeans.modeler.config.palette.IPaletteConfig;
import org.netbeans.modeler.config.palette.PaletteConfigRepository;
import org.netbeans.modeler.core.IExceptionHandler;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.specification.export.IExportManager;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;

public class ModelerDiagramSpecification {

    private DiagramModel diagramModel;
    private IModelerUtil modelerUtil;

    private IModelerScene modelerScene;
    private IModelerDiagramEngine modelerDiagramEngine;

    private IDefinitionElement definitionElement;

    private IExportManager exportManager;
    private IRelationValidator relationValidator;

    private IExceptionHandler exceptionHandler;
    
    private IPaletteConfig paletteConfig;
    private ElementConfigFactory elementConfigFactory;
    private ModelerDocumentFactory modelerDocumentFactory;

    public void createElementConfig(String vendorId, String resourcePath) {
        elementConfigFactory = ElementConfigFactoryRepository.createElementConfigFactory(vendorId, resourcePath);
    }

    public void createModelerDocumentConfig(String vendorId, String resourcePath) {
        modelerDocumentFactory = ModelerDocumentFactoryRepository.createModelerDocumentFactory(vendorId, resourcePath);
    }

    public void createPaletteConfig(String vendorId, String diagramModelId, String resourcePath) {
        paletteConfig = PaletteConfigRepository.createPaletteConfig(vendorId, diagramModelId, resourcePath);//.addPaletteConfig(modelerFileClass,resource);
    }



    /**
     * @return the diagramModelPaletteConfig
     */
    public IPaletteConfig getPaletteConfig() {
        return paletteConfig;
    }

    /**
     * @return the elementConfigFactory
     */
    public ElementConfigFactory getElementConfigFactory() {
        return elementConfigFactory;
    }

    /**
     * @return the modelerDocumentFactory
     */
    public ModelerDocumentFactory getModelerDocumentFactory() {
        return modelerDocumentFactory;
    }


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
     * @return the modelerUtil
     */
    public IModelerUtil getModelerUtil() {
        return modelerUtil;
    }

    /**
     * @param modelerUtil the modelerUtil to set
     */
    public void setModelerUtil(IModelerUtil modelerUtil) {
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

    /**
     * @return the exportManager
     */
    public IExportManager getExportManager() {
        return exportManager;
    }

    /**
     * @param exportManager the exportManager to set
     */
    public void setExportManager(IExportManager exportManager) {
        this.exportManager = exportManager;
    }

    /**
     * @return the exceptionHandler
     */
    public IExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * @param exceptionHandler the exceptionHandler to set
     */
    public void setExceptionHandler(IExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

}
