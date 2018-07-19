/**
 * Copyright 2013-2018 Gaurav Gupta
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

import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.List;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

public class NEntityPropertyEditorSupport extends PropertyEditorSupport implements ExPropertyEditor {

    NAttributeEntity attributeEntity;

    public NEntityPropertyEditorSupport(NAttributeEntity attributeEntity) {
        this.attributeEntity = attributeEntity;
    }

    @Override
    public String getAsText() {
        Object val = getValue();
        if (val == null) {
            return attributeEntity.getCountDisplay()[0];
        }

        int len = attributeEntity.getTableDataListener().getCount();
        switch (len) {
            case 0:
                return attributeEntity.getCountDisplay()[0];
            case 1:
                return attributeEntity.getCountDisplay()[1];
            default:
                return len + " " + attributeEntity.getCountDisplay()[2];
        }

    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public java.awt.Component getCustomEditor() {
//        Object val = getValue();this.
        return new NEntityEditor(attributeEntity, this, env); // NOI18N
    }

//   private boolean customEd=true;
    private PropertyEnv env;

    // bugfix# 9219 added attachEnv() method checking if the user canWrite in text box
    @Override
    public void attachEnv(PropertyEnv env) {
        FeatureDescriptor desc = env.getFeatureDescriptor();
        this.env = env;
    }
}
