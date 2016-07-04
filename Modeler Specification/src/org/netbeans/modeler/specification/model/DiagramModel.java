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

import org.netbeans.modeler.specification.version.SoftwareVersion;

public class DiagramModel {

    private String id;
    private String name;
    private SoftwareVersion version;
    private SoftwareVersion architectureVersion;

    public DiagramModel(String id, String name, SoftwareVersion version, SoftwareVersion architectureVersion) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.architectureVersion = architectureVersion;
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
     * @return the version
     */
    public SoftwareVersion getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(SoftwareVersion version) {
        this.version = version;
    }

    /**
     * @return the architectureVersion
     */
    public SoftwareVersion getArchitectureVersion() {
        return architectureVersion;
    }

    /**
     * @param architectureVersion the architectureVersion to set
     */
    public void setArchitectureVersion(SoftwareVersion architectureVersion) {
        this.architectureVersion = architectureVersion;
    }
}
