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
package org.netbeans.modeler.widget.properties.customattr;

import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.openide.nodes.PropertySupport;

/**
 *
 *
 */
public class CustomAttributeSupport extends PropertySupport {

    private PropertyEditor editor = null;
    private IBaseElement baseElement = null;
    private ModelerFile modelerFile;

    public CustomAttributeSupport(ModelerFile modelerFile, IBaseElement baseElement, String displayName, String description) {
        super(displayName, Map.class, displayName, description, true, true);
        setValue("canEditAsText", Boolean.FALSE);
        this.modelerFile = modelerFile;
        this.baseElement = baseElement;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return baseElement.getCustomAttributes();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        baseElement.setCustomAttributes((Map<String, String>) val);
        modelerFile.getModelerPanelTopComponent().changePersistenceState(false);
    }

    @Override
    public PropertyEditor getPropertyEditor() {

        if (editor == null) {
            editor = new CustomAttributeEditorSupport();
        }
        return editor;
    }

}
