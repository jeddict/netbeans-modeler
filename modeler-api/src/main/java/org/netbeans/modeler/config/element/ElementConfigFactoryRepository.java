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
package org.netbeans.modeler.config.element;

import java.util.HashMap;
import java.util.Map;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.netbeans.modeler.util.Util;
import org.openide.util.Exceptions;

public class ElementConfigFactoryRepository {

    private static final Map<String, ElementConfigFactory> elementConfigFactories = new HashMap<String, ElementConfigFactory>();

    public static ElementConfigFactory createElementConfigFactory(String vendorId, String resourcePath) {
        if (elementConfigFactories.get(vendorId) == null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ElementConfig.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                ElementConfig elementConfig = (ElementConfig) jaxbUnmarshaller.unmarshal(Util.loadResource(resourcePath));
                ElementConfigFactory elementConfigFactory = new ElementConfigFactory(elementConfig);
                if (elementConfig != null) {
                    elementConfigFactories.put(vendorId, elementConfigFactory);
                }
            } catch (JAXBException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return elementConfigFactories.get(vendorId);
    }

    public static ElementConfigFactory getElementConfigFactory(String vendorId) {
        return elementConfigFactories.get(vendorId);
    }

}
