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
package org.netbeans.modeler.specification.model.document.property;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import org.netbeans.modeler.config.element.ElementConfig;
import org.netbeans.modeler.config.element.Group;
import org.netbeans.modeler.core.ModelerFile;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;

public class ElementPropertySet {

    private ModelerFile modelerFile;
    private ElementConfig elementConfig;
    private Sheet sheet;
    private Map<String, Sheet.Set> set = new LinkedHashMap<String, Sheet.Set>();

    public ElementPropertySet(ModelerFile modelerFile, Sheet sheet) {
        this.modelerFile = modelerFile;
        this.sheet = sheet;
        this.elementConfig = modelerFile.getVendorSpecification().getElementConfigFactory().getElementConfig();;

    }

    public void createGroup(String id) {
        set.put(id, sheet.createPropertiesSet());
        set.get(id).setName(id);// Sheet.Set : name is required work as key [otherwise set is replaced]
    }

    public synchronized Node.Property<?> put(String id, Node.Property<?> p) {

        if (set.get(id) == null) {
            Group group = elementConfig.getGroup(id);
            if (group == null) {
                throw new RuntimeException("Group Id : " + id + " not found in element config for Element : " + p.getName());
            }
            createGroup(id);
            setGroupDisplayName(id, group.getName());
        }
        return set.get(id).put(p);

    }

    public void setGroupDisplayName(String id, String displayName) {
        set.get(id).setDisplayName(displayName);
    }

    /**
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * @return the set
     */
    public Sheet.Set getGroup(String id) {
        return set.get(id);
    }

    public LinkedList<Sheet.Set> getGroups() {
        return new LinkedList<Sheet.Set>(set.values());
    }

    /**
     * @return the modelerFile
     */
    public ModelerFile getModelerFile() {
        return modelerFile;
    }

    /**
     * @param modelerFile the modelerFile to set
     */
    public void setModelerFile(ModelerFile modelerFile) {
        this.modelerFile = modelerFile;
    }

}
