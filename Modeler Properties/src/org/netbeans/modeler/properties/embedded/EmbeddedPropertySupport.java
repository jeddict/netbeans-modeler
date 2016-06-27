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
package org.netbeans.modeler.properties.embedded;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.mvel2.MVEL;
import org.netbeans.modeler.config.element.ModelerSheetProperty;
import org.netbeans.modeler.core.ModelerFile;
import static org.netbeans.modeler.specification.model.document.property.PropertySetUtil.createPropertyVisibilityHandler;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.PropertySupport;

/**
 *
 *
 */
public class EmbeddedPropertySupport extends PropertySupport implements ModelerSheetProperty{

    PropertyEditor editor = null;
    private ModelerFile modelerFile;
    private GenericEmbedded entity;
    private PropertyVisibilityHandler propertyVisibilityHandler;
    
    public EmbeddedPropertySupport(ModelerFile modelerFile, GenericEmbedded attributeEntity, PropertyVisibilityHandler propertyVisibilityHandler) {
        super(attributeEntity.getName(), Map.class, attributeEntity.getDisplayName(), attributeEntity.getShortDescription(), true, !attributeEntity.isReadOnly());
        this.modelerFile = modelerFile;
        setValue("canEditAsText", Boolean.FALSE);
        this.entity = attributeEntity;
        if (entity.getDataListener() != null) {
            entity.getDataListener().init();
        }
        this.propertyVisibilityHandler = propertyVisibilityHandler;

    }

    public EmbeddedPropertySupport(ModelerFile modelerFile, GenericEmbedded attributeEntity) {
        this(modelerFile, attributeEntity, null);
    }

    public EmbeddedPropertySupport(ModelerFile modelerFile, GenericEmbedded attributeEntity, String visible, Object object) {
        super(attributeEntity.getName(), Map.class, attributeEntity.getDisplayName(), attributeEntity.getShortDescription(), true, !attributeEntity.isReadOnly());
        this.modelerFile = modelerFile;
        setValue("canEditAsText", Boolean.FALSE);
        this.entity = attributeEntity;
        if (entity.getDataListener() != null) {
            entity.getDataListener().init();
        }
        Serializable visibilityExpression = MVEL.compileExpression(visible);
        this.propertyVisibilityHandler = createPropertyVisibilityHandler(modelerFile, object, visibilityExpression);
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return getEntity().getDataListener().getData();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
//            entity.getDataListener().setData(val);
// ignore panel will do this job
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public PropertyEditor getPropertyEditor() {

        if (editor == null) {
            editor = new EmbeddedPropertyEditorSupport(modelerFile, getEntity());
        }
        return editor;
    }

    /**
     * @return the entity
     */
    public GenericEmbedded getEntity() {
        return entity;
    }

    /**
     * @return the propertyVisibilityHandler
     */
    public PropertyVisibilityHandler getPropertyVisibilityHandler() {
        return propertyVisibilityHandler;
    }

    

    @Override
    public String getBefore() {
        if(entity!=null){
            return entity.getBefore();
        }
        return null;
    }

    @Override
    public String getAfter() {
        if(entity!=null){
            return entity.getAfter();
        }
        return null;
    }

}
