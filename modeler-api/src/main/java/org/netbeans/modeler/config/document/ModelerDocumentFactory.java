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
import java.util.Iterator;
import java.util.Map.Entry;
import org.netbeans.modeler.core.exception.ModelerException;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;

/**
 *
 *
 */
public class ModelerDocumentFactory {

    private HashMap<String, IModelerDocument> modelerDocuments = new HashMap<String, IModelerDocument>();
    private ModelerDocumentConfig modelerDocumentConfig;

    public ModelerDocumentFactory(ModelerDocumentConfig modelerDocumentConfig) {
        this.modelerDocumentConfig = modelerDocumentConfig;
        for (IModelerDocument modelerDocument : modelerDocumentConfig.getModelerDocuments()) {
            try {
                modelerDocuments.put(modelerDocument.getId(), modelerDocument);
                modelerDocument.init();
            } catch (Exception ex) {
                System.out.println("Error in ModelerDocuments : " + modelerDocument.getId() + " Error : " + ex);
            }
        }
    }

    /**
     * @return the modelerDocuments
     */
    public HashMap<String, IModelerDocument> getModelerDocuments() {
        return modelerDocuments;
    }

    /**
     * @return the modelerDocuments
     */
    public IModelerDocument getModelerDocument(String id) {
        return modelerDocuments.get(id);
    }

    /**
     * @param modelerDocuments the modelerDocuments to set
     */
    public void setModelerDocuments(HashMap<String, IModelerDocument> modelerDocuments) {
        modelerDocuments = modelerDocuments;
    }

    public IModelerDocument getModelerDocument(IBaseElement specification) throws ModelerException {
        return getModelerDocument(specification, null);
    }

    public IModelerDocument getModelerDocument(IBaseElement specification, String definition) throws ModelerException {
        Iterator<Entry<String, IModelerDocument>> modelerDocumentsItr = modelerDocuments.entrySet().iterator();
        while (modelerDocumentsItr.hasNext()) {
            Entry<String, IModelerDocument> entry = modelerDocumentsItr.next();
            IModelerDocument document = entry.getValue();
//            if (document.getSpecification() == null) {
//                throw new ModelerException("No Specification [" + specification.getClass() + "] defined in DocumentConfig");
//            }
            if (definition != null) {
                if (document.getSpecification() == specification.getClass() && definition.equals(document.getDefinition())) {
                    return document;
                }
            } else {
                if (document.getSpecification() == specification.getClass() & document.getDefinition() == null) {
                    return document;
                }
            }
        }
        if (definition != null) {
            throw new ModelerException("No Document found with Specification [" + specification.getClass() + "] , Definition [" + definition + "] in DocumentConfig");
        } else {
            throw new ModelerException("No Document found with Specification [" + specification.getClass() + "] in DocumentConfig");
        }

    }

    public IModelerDocument getModelerDocument(Class<? extends IFlowNodeWidget> widget) throws ModelerException {
        Iterator<Entry<String, IModelerDocument>> modelerDocumentsItr = modelerDocuments.entrySet().iterator();
        while (modelerDocumentsItr.hasNext()) {
            Entry<String, IModelerDocument> entry = modelerDocumentsItr.next();
            IModelerDocument document = entry.getValue();
//            if (document.getWidget() == null) {
//                throw new ModelerException("No Widget [" + widget.getClass() + "] defined in DocumentConfig");
//            }
            if (document.getWidget() == widget) {
                return document;
            }

        }
        throw new ModelerException("No Document found with Widget [" + widget.getClass() + "] in DocumentConfig");
    }

    /**
     * @return the modelerDocumentConfig
     */
    public ModelerDocumentConfig getModelerDocumentConfig() {
        return modelerDocumentConfig;
    }
}
