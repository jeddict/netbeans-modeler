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
package org.netbeans.modeler.core.scene.vmd;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.modeler.scene.vmd.AbstractPModelerScene;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;

public abstract class PModelerScene extends AbstractPModelerScene {

    private IRootElement rootElementSpec;
    protected String id;
    protected String name;
    protected String documentation;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /**
     * @return the rootElementSpec
     */
    @Override
    public IRootElement getRootElementSpec() {
        return rootElementSpec;
    }

    /**
     * @param rootElementSpec the rootElementSpec to set
     */
    @Override
    public void setRootElementSpec(IRootElement rootElementSpec) {
        this.rootElementSpec = rootElementSpec;
    }

    @Override
    public void setBaseElementSpec(IBaseElement baseElementSpec) {
        setRootElementSpec((IRootElement) baseElementSpec);
//          throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IBaseElement getBaseElementSpec() {
        return rootElementSpec;
    }

    private Map<String, PropertyChangeListener> propertyChangeHandlers = new HashMap<String, PropertyChangeListener>();

    public void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener) {
        this.propertyChangeHandlers.put(id, propertyChangeListener);
    }

    public void removePropertyChangeListener(String id) {
        propertyChangeHandlers.remove(id);
    }

    public Map<String, PropertyChangeListener> getPropertyChangeListeners() {
        return propertyChangeHandlers;
    }

}
