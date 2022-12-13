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
package org.netbeans.modeler.config.document;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 *
 */
@XmlRootElement(name = "document-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelerDocumentConfig {

    @XmlElementWrapper(name = "documents")
    @XmlElement(name = "document")
    private List<ModelerDocument> documents = new ArrayList<ModelerDocument>();

    /**
     * @return the elements
     */
    public List<? extends IModelerDocument> getModelerDocuments() {
        return documents;
    }

    /**
     * @param documents the elements to set
     */
    public void setModelerDocuments(List<ModelerDocument> documents) {
        this.documents = documents;
    }

    public void addModelerDocument(ModelerDocument document) {
        this.documents.add(document);
    }
}
