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

import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import org.netbeans.modeler.core.ModelerFile;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

public class EmbeddedPropertyEditorSupport extends PropertyEditorSupport implements ExPropertyEditor {

    private GenericEmbedded entity;
    private ModelerFile modelerFile;

    public EmbeddedPropertyEditorSupport(ModelerFile modelerFile, GenericEmbedded entity) {
        this.modelerFile = modelerFile;
        this.entity = entity;
//        entity.getEntityEditor().init();
    }

    @Override
    public String getAsText() {
        return entity.getDataListener().getDisplay();
    }

    @Override
    public boolean supportsCustomEditor() {
        return entity.getEntityEditor() != null ? true : false;
    }

    @Override
    public java.awt.Component getCustomEditor() {
//        Object val = getValue();this.
        if (entity.getEntityEditor() != null) {
            entity.getEntityEditor().setEntity(entity);
            entity.getEntityEditor().setEnv(env);
            entity.getEntityEditor().setEditor(this);
            entity.getEntityEditor().setModelerFile(modelerFile);
            entity.getEntityEditor().setValue(entity.getDataListener().getData());
            return entity.getEntityEditor();//new CustomNAttributeEditor(attributeEntity, this, env); // NOI18N
        } else {
            return null;
        }
    }

//   private boolean customEd=true;
//    private PropertyEnv env;
//
//    // bugfix# 9219 added attachEnv() method checking if the user canWrite in text box
//    public void attachEnv(PropertyEnv env) {
//
//        FeatureDescriptor desc = env.getFeatureDescriptor();
//        if (desc instanceof Node.Property) {
//            Node.Property prop = (Node.Property) desc;
//            //enh 29294 - support one-line editor & suppression of custom
//            //editor
//            //instructions = (String) prop.getValue ("instructions"); //NOI18N
//            //oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
////            customEd = !Boolean.TRUE.equals (prop.getValue ("suppressCustomEditor")); //NOI18N
//        }
//        this.env = env;
//    }
    private PropertyEnv env;

    @Override
    public void attachEnv(PropertyEnv pe) {
        FeatureDescriptor desc = pe.getFeatureDescriptor();
        this.env = pe;
    }
}
