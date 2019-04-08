/**
 * Copyright 2013-2019 Gaurav Gupta
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
package org.netbeans.modeler.properties.customattr;

import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Map;
import org.netbeans.modeler.locale.I18n;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.nodes.Node;

public class CustomAttributeEditorSupport extends PropertyEditorSupport implements ExPropertyEditor {
//    public boolean isEditable(){
//        return false;
//    }

    @Override
    public String getAsText() {
        Object val = getValue();
        if (val == null) {
            return "ModelerElement.Attribute.NoPropertiesSet";
        }

        if (val instanceof List) {
            int len = ((List) val).size();
            switch (len) {
                case 0:
                    return I18n.getString(CustomAttributeEditorSupport.class, "ModelerElement.Attribute.NoPropertiesSet");
                case 1:
                    return I18n.getString(CustomAttributeEditorSupport.class, "ModelerElement.Attribute.OnePropertiesSet");
                default:
                    return I18n.getString(CustomAttributeEditorSupport.class, "ModelerElement.Attribute.PropertiesSet", len);
            }
        } else if (val instanceof Map) {
            int len = ((Map) val).size();
            switch (len) {
                case 0:
                    return I18n.getString(CustomAttributeEditorSupport.class, "ModelerElement.Attribute.NoPropertiesSet");
                case 1:
                    return I18n.getString(CustomAttributeEditorSupport.class, "ModelerElement.Attribute.OnePropertiesSet");
                default:
                    return I18n.getString(CustomAttributeEditorSupport.class, "ModelerElement.Attribute.PropertiesSet", len);
            }
        }
        return "";
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public java.awt.Component getCustomEditor() {
        Object val = getValue();
        return new CustomAttributeEditor(val, this, env); // NOI18N
    }

//   private boolean customEd=true;
    private PropertyEnv env;

    // bugfix# 9219 added attachEnv() method checking if the user canWrite in text box
    @Override
    public void attachEnv(PropertyEnv env) {

        FeatureDescriptor desc = env.getFeatureDescriptor();
        if (desc instanceof Node.Property) {
            Node.Property prop = (Node.Property) desc;
            //enh 29294 - support one-line editor & suppression of custom
            //editor
            //instructions = (String) prop.getValue ("instructions"); //NOI18N
            //oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
//            customEd = !Boolean.TRUE.equals (prop.getValue ("suppressCustomEditor")); //NOI18N
        }
        this.env = env;
    }
}
