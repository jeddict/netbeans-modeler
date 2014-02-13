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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import org.netbeans.modeler.core.ModelerFile;
import org.openide.explorer.propertysheet.PropertyEnv;

public abstract class GenericEmbeddedEditor<T> extends javax.swing.JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private PropertyEditor editor;
    private GenericEmbedded entity;
    private ModelerFile modelerFile;

    public GenericEmbeddedEditor() {

        init();
    }

    public abstract void init();

    public abstract T getValue();

    public abstract void setValue(T val);

//    public GenericEntityEditor(GenericEntity entity, PropertyEditor editor, PropertyEnv env) {
//        this.env = env;
//        this.editor = editor;
//        this.entity = entity;
//        this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
//        this.env.addPropertyChangeListener(this);
//    }
    @Override //Save Button Clicked
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            entity.getDataListener().setData(this.getValue());
            getModelerFile().getModelerPanelTopComponent().changePersistenceState(false);

        }
    }

    /**
     * @return the env
     */
    public PropertyEnv getEnv() {
        return env;
    }

    /**
     * @param env the env to set
     */
    public void setEnv(PropertyEnv env) {
        this.env = env;
        this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        this.env.addPropertyChangeListener(this);
    }

    /**
     * @return the editor
     */
    public PropertyEditor getEditor() {
        return editor;
    }

    /**
     * @param editor the editor to set
     */
    public void setEditor(PropertyEditor editor) {
        this.editor = editor;
    }

    /**
     * @return the entity
     */
    public GenericEmbedded getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(GenericEmbedded entity) {
        this.entity = entity;
    }

    /**
     * @return the modelerFile
     */
    public ModelerFile getModelerFile() {
        return modelerFile;
    }

    /**
     * @param modelerFile the modelerFile to set
     */
    public void setModelerFile(ModelerFile modelerFile) {
        this.modelerFile = modelerFile;
    }
}
