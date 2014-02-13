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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import org.netbeans.modeler.core.ModelerFile;
import org.openide.nodes.PropertySupport;

/**
 *
 *
 */
public class EmbeddedPropertySupport extends PropertySupport {

    PropertyEditor editor = null;
    private ModelerFile modelerFile;
    private GenericEmbedded entity;

    public EmbeddedPropertySupport(ModelerFile modelerFile, GenericEmbedded attributeEntity) {
        super(attributeEntity.getName(), Map.class, attributeEntity.getDisplayName(), attributeEntity.getShortDescription(), true, !attributeEntity.isReadOnly());
        this.modelerFile = modelerFile;
        setValue("canEditAsText", Boolean.FALSE);
        this.entity = attributeEntity;
        if (entity.getDataListener() != null) {
            entity.getDataListener().init();
        }

    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return entity.getDataListener().getData();
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
            editor = new EmbeddedPropertyEditorSupport(modelerFile, entity);
        }
        return editor;
    }
}
