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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import javax.swing.JComponent;
import org.netbeans.modeler.core.ModelerFile;
import org.openide.explorer.propertysheet.PropertyEnv;

public abstract class GenericEmbeddedEditor<T> extends javax.swing.JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private PropertyEditor editor;
    private GenericEmbedded entity;
    private ModelerFile modelerFile;
    private boolean loaded = false;

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
//            T obj = this.getValue();
//            if (obj == null) {
//                this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
//            } else {
//                entity.getDataListener().setData(obj);
//                getModelerFile().getModelerPanelTopComponent().changePersistenceState(false);
//            }

            try {
                entity.getDataListener().setData(this.getValue());
                getModelerFile().getModelerPanelTopComponent().changePersistenceState(false);
            } catch (IllegalStateException ise) {
                this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            }

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

//    @Override
//    public int getWidth() {
//        if (this.getParent() != null) {
//            Container dialog = (Container) this.getParent().getParent().getParent().getParent();
//            return dialog.getWidth();
//        }
//        return 0;
//    }
//
//    @Override
//    public int getHeight() {
//        if (this.getParent() != null) {
//            Container dialog = (Container) this.getParent().getParent().getParent().getParent();
//            return dialog.getHeight();
//        }
//        return 0;
//    }
//    @Override
//    public void setSize(int width, int height) {
//        if (this.getParent() != null) {
//            Container dialog = (Container) this.getParent().getParent().getParent().getParent();//NBDialog
//            dialog.setSize(width, height);
//
//            Toolkit toolkit = Toolkit.getDefaultToolkit();
//            Dimension screenSize = toolkit.getScreenSize();
//            int x = (screenSize.width - this.getWidth()) / 2;
//            int y = (screenSize.height - this.getHeight()) / 2;
//            dialog.setLocation(x, y);
//
//        }
//    }
    protected void setEnablePanel(JComponent layerPane, boolean status) {
        for (Component com : layerPane.getComponents()) {
            if (com instanceof javax.swing.JLayeredPane) {
                javax.swing.JLayeredPane layerPane_Child = (javax.swing.JLayeredPane) com;
                setEnablePanel(layerPane_Child, status);
            } else if (com instanceof javax.swing.JScrollPane) {
                javax.swing.JScrollPane layerPane_Child = (javax.swing.JScrollPane) com;
                setEnablePanel(layerPane_Child, status);
            } else if (com instanceof javax.swing.JViewport) {
                javax.swing.JViewport layerPane_Child = (javax.swing.JViewport) com;
                setEnablePanel(layerPane_Child, status);

            } else {
                com.setEnabled(status);
            }
        }
        layerPane.setEnabled(status);

    }

    /**
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * @param loaded the loaded to set
     */
    public void setLoaded() {
        this.loaded = true;
    }

}
