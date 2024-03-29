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
package org.netbeans.modeler.navigator;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.openide.windows.TopComponent;

/**
 *
 *
 */
public class DiagramNavigatorContent extends JPanel implements PropertyChangeListener {

    private JComponent currentView = null;

    /**
     * Creates new form DiagramNavigatorPanel
     */
    public DiagramNavigatorContent() {
        initComponents();
    }

    public void navigate(final JComponent satelliteView) {
        SwingUtilities.invokeLater(() -> {
            if (satelliteView != null) {
                if (currentView != null) {
                    remove(currentView);
                }
                add(satelliteView, BorderLayout.CENTER);
                currentView = satelliteView;
                invalidate();
                validate();
            }
        });
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if (property.equals(TopComponent.Registry.PROP_ACTIVATED)) {
            TopComponent view = (TopComponent) evt.getNewValue();
            if (view != null)//may be null on events like slide out of Project tree etc
            {
                IModelerScene scene = view.getLookup().lookup(IModelerScene.class);
                if (scene != null) {
                    navigate(scene.getSatelliteView());
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
