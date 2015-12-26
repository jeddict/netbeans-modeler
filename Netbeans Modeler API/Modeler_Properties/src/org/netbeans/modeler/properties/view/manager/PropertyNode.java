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
package org.netbeans.modeler.properties.view.manager;

import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

public abstract class PropertyNode<S extends IModelerScene> extends AbstractNode {

    private S modelerScene;

    public PropertyNode(S modelerScene, Children children, Lookup lookup) {
        super(children, lookup);
        this.modelerScene = modelerScene;
    }

    public PropertyNode(S modelerScene, Children children) {
        super(children);
        this.modelerScene = modelerScene;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        ElementPropertySet elementPropertySet = new ElementPropertySet(getModelerScene().getModelerFile(), sheet);

        createPropertySet(elementPropertySet);

        for (Sheet.Set set : elementPropertySet.getGroups()) {
            elementPropertySet.getSheet().put(set);
        }

        //PropertyUtil.getNode(this);
        //non persistence strategy here insteadof NetbeansModeler Widget AbstractNode persist and runtime hidden and count validation
        //here when node created then count is also evaluated
        for (Sheet.Set set : elementPropertySet.getGroups()) {
            for (Node.Property property : set.getProperties()) {
                if (property.getClass() == NEntityPropertySupport.class) {
                    NEntityPropertySupport attributeProperty = (NEntityPropertySupport) property;
                    attributeProperty.getAttributeEntity().getTableDataListener().initCount();
                }
            }
        }

        return sheet;
    }

    public abstract void createPropertySet(ElementPropertySet elementPropertySet);

    /**
     * @return the modelerScene
     */
    public S getModelerScene() {
        return modelerScene;
    }
}
