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
package org.netbeans.modeler.properties.nentity;

import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.PropertySupport;

/**
 *
 *
 */
public class NEntityPropertySupport extends PropertySupport {

    private PropertyEditor editor = null;
    private ModelerFile modelerFile;
    private NAttributeEntity attributeEntity;
    private PropertyVisibilityHandler propertyVisibilityHandler;

    public NEntityPropertySupport(ModelerFile modelerFile, NAttributeEntity attributeEntity) {
        super(attributeEntity.getName(), Map.class, attributeEntity.getDisplayName(), attributeEntity.getShortDescription(), true, true);
        this.modelerFile = modelerFile;
        setValue("canEditAsText", Boolean.FALSE);
        this.attributeEntity = attributeEntity;
        if (attributeEntity.getTableDataListener() != null) {
            attributeEntity.getTableDataListener().initData();
        }
    }

    public NEntityPropertySupport(ModelerFile modelerFile, NAttributeEntity attributeEntity, PropertyVisibilityHandler propertyVisibilityHandler) {
        this(modelerFile, attributeEntity);
        this.propertyVisibilityHandler = propertyVisibilityHandler;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return getAttributeEntity().getTableDataListener().getData();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            getAttributeEntity().getTableDataListener().setData((List<Object[]>) val);
            getAttributeEntity().getTableDataListener().initCount();
            modelerFile.getModelerPanelTopComponent().changePersistenceState(false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public PropertyEditor getPropertyEditor() {

        if (editor == null) {
            editor = new NEntityPropertyEditorSupport(getAttributeEntity());
        }
        return editor;
    }

    /**
     * @return the attributeEntity
     */
    public NAttributeEntity getAttributeEntity() {
        return attributeEntity;
    }

    /**
     * @return the propertyVisibilityHandler
     */
    public PropertyVisibilityHandler getPropertyVisibilityHandler() {
        return propertyVisibilityHandler;
    }

    /**
     * @param propertyVisibilityHandler the propertyVisibilityHandler to set
     */
    public void setPropertyVisibilityHandler(PropertyVisibilityHandler propertyVisibilityHandler) {
        this.propertyVisibilityHandler = propertyVisibilityHandler;
    }

}
