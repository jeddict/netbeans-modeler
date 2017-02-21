/**
 * Copyright [2013] Gaurav Gupta
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;

/**
 * DefaultPModelerScene is the default implementation of PModelerScene. You
 * should implement your own modeler scene implementation if you want to modify
 * the storage structure of flowElements widget (e.g divide flowElements widget
 * into multiple collection , as in BPMN module Conversation and Artifacts
 * element are stored in different collection)
 *
 * @author Gaurav Gupta
 * @param <E>
 * @param <R>
 */
public abstract class DefaultPModelerScene<E extends IRootElement> extends PModelerScene<E> {

    private Map<String, IFlowElementWidget> flowElements = new LinkedHashMap<>();

    @Override
    public void onConnection() {
    }
    
    @Override
    public IBaseElementWidget getBaseElement(String id) {
        return flowElements.get(id);
    }

    @Override
    public List<IBaseElementWidget> getBaseElements() {
        return new ArrayList<>(flowElements.values());
    }

    @Override
    public void removeBaseElement(IBaseElementWidget baseElementWidget) {
        if (baseElementWidget instanceof IFlowElementWidget) {
            this.flowElements.remove(baseElementWidget.getId());
        }
    }

    @Override
    public void addBaseElement(IBaseElementWidget baseElementWidget) {
        if (baseElementWidget instanceof IFlowElementWidget) {
            this.flowElements.put(baseElementWidget.getId(), (IFlowElementWidget) baseElementWidget);
        }
    }

    @Override
    public void createPropertySet(ElementPropertySet set) {
        set.createPropertySet(this, this.getBaseElementSpec(), getPropertyChangeListeners());
    }

    @Override
    public void createVisualPropertySet(ElementPropertySet elementPropertySet) {

    }

}
