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
package org.netbeans.modeler.specification;

import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.config.element.ElementConfigFactoryRepository;
import org.netbeans.modeler.config.document.ModelerDocumentFactory;
import org.netbeans.modeler.config.document.ModelerDocumentFactoryRepository;
import org.netbeans.modeler.config.palette.IPaletteConfig;
import org.netbeans.modeler.config.palette.PaletteConfigRepository;
import org.netbeans.modeler.specification.model.ModelerDiagramSpecification;

public class ModelerVendorSpecification {

    private Vendor vendor;
    private ModelerDiagramSpecification modelerSpecificationDiagramModel;

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
     * @return the modelerSpecificationDiagramModel
     */
    public ModelerDiagramSpecification getModelerDiagramModel() {
        return modelerSpecificationDiagramModel;
    }

    /**
     * @param modelerSpecificationDiagramModel the
     * modelerSpecificationDiagramModel to set
     */
    public void setModelerSpecificationDiagramModel(ModelerDiagramSpecification modelerSpecificationDiagramModel) {
        this.modelerSpecificationDiagramModel = modelerSpecificationDiagramModel;
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
     * @return the vendor
     */
    public Vendor getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

//    /**
//     * @return the paletteConfig
//     */
//    public IPaletteConfig getPaletteConfig() {
//        return paletteConfig;
//    }
//
//    /**
//     * @param paletteConfig the paletteConfig to set
//     */
//    public void setPaletteConfig(IPaletteConfig paletteConfig) {
//        this.paletteConfig = paletteConfig;
//    }
    /**
     * @return the paletteConfigFactory
     */
//    public IPaletteConfigFactory getPaletteConfigFactory() {
//        return paletteConfigFactory;
//    }
}
