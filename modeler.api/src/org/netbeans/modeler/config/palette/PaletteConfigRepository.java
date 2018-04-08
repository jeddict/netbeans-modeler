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
package org.netbeans.modeler.config.palette;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.netbeans.modeler.config.document.ModelerDocumentFactory;
import org.netbeans.modeler.config.document.ModelerDocumentFactoryRepository;
import org.netbeans.modeler.core.exception.InvalidPaletteConfigException;
import org.netbeans.modeler.util.Util;
import org.openide.util.Exceptions;

public class PaletteConfigRepository {

    private static final Map<String, IPaletteConfig> paletteConfigList = new HashMap<String, IPaletteConfig>();

    public static IPaletteConfig createPaletteConfig(String vendorId, String diagramModelId, String resourcePath) {
        IPaletteConfig paletteConfig = paletteConfigList.get(vendorId + File.pathSeparator + diagramModelId);
        if (paletteConfig == null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(PaletteConfig.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                paletteConfig = (IPaletteConfig) jaxbUnmarshaller.unmarshal(Util.loadResource(resourcePath));
                if (paletteConfig == null) {
                    throw new InvalidPaletteConfigException("No PaletteConfig found for Vendor : " + vendorId + " DiagramModel : " + diagramModelId + " Resource : " + resourcePath);
                }
                paletteConfigList.put(vendorId + File.pathSeparator + diagramModelId, paletteConfig);
                ModelerDocumentFactory modelerDocumentFactory = ModelerDocumentFactoryRepository.getModelerDocumentFactory(vendorId);
                for (CategoryNodeConfig categoryNodeConfig : paletteConfig.getCategoryNodeConfigs()) {
                    for (SubCategoryNodeConfig subCategoryNodeConfig : categoryNodeConfig.getSubCategoryNodeConfigs()) {
                        subCategoryNodeConfig.setModelerDocument(modelerDocumentFactory.getModelerDocument(subCategoryNodeConfig.getId()));
                        subCategoryNodeConfig.init();
                    }
                }
            } catch (JAXBException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return paletteConfig;
    }

    public static IPaletteConfig getPaletteConfig(String vendorId, String diagramModelId) {//Class<? extends IModelerFileDataObject> modelerFileClass) {
        IPaletteConfig paletteConfig = paletteConfigList.get(vendorId + File.pathSeparator + diagramModelId);
        if (paletteConfig == null) {
            throw new InvalidPaletteConfigException("No DiagramModelConfig PaletteConfig DiagramModel : " + diagramModelId);
        }
        return paletteConfig;
    }

}
