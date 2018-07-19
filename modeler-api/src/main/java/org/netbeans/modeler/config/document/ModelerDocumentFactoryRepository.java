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
package org.netbeans.modeler.config.document;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.netbeans.modeler.util.Util;
import org.openide.util.Exceptions;

public class ModelerDocumentFactoryRepository {

    private static final Map<String, ModelerDocumentFactory> modelerDocumentFactories = new HashMap<String, ModelerDocumentFactory>();

    public static ModelerDocumentFactory createModelerDocumentFactory(String vendorId, String resourcePath) {
        if (modelerDocumentFactories.get(vendorId) == null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ModelerDocumentConfig.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                ModelerDocumentConfig documentConfig = (ModelerDocumentConfig) jaxbUnmarshaller.unmarshal(Util.loadResource(resourcePath));
                ModelerDocumentFactory modelerDocumentFactory = new ModelerDocumentFactory(documentConfig);
                if (documentConfig != null) {
                    modelerDocumentFactories.put(vendorId, modelerDocumentFactory);
                }
            } catch (JAXBException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return modelerDocumentFactories.get(vendorId);
    }

    public static ModelerDocumentFactory getModelerDocumentFactory(String vendorId) {
        return modelerDocumentFactories.get(vendorId);
    }

}
