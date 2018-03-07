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
package org.netbeans.modeler.navigator;

import java.beans.PropertyChangeEvent;
import org.netbeans.modeler.component.ModelerPanelTopComponent;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import static org.openide.windows.TopComponent.Registry.PROP_ACTIVATED_NODES;
import org.openide.windows.WindowManager;

public abstract class ModelerNavigatorPanel implements NavigatorPanel {

    private DiagramNavigatorContent navigator;
    private final Lookup.Template< IModelerScene> template = new Lookup.Template<>(IModelerScene.class);

    @Override
    public String getDisplayName() {
        return null;//NbBundle.getMessage(DiagramNavigatorPanel.class, "Navigator_DisplayName");
    }
//

    @Override
    public String getDisplayHint() {
        return null;//NbBundle.getMessage(DiagramNavigatorPanel.class, "Navigator_Hint");
    }

    @Override
    public DiagramNavigatorContent getComponent() {
        if (navigator == null) {
            navigator = new DiagramNavigatorContent();
        }
        return navigator;
    }

    private TopComponent getTopComponent() {
        TopComponent tc = WindowManager.getDefault().getRegistry().getActivated();
        if (tc instanceof ModelerPanelTopComponent) {
            return tc;
        }
        return null;
    }

    @Override
    public void panelActivated(Lookup context) {
        TopComponent.getRegistry().addPropertyChangeListener(getComponent());
        TopComponent tc = getTopComponent();
        if (tc != null) {
            getComponent().propertyChange(new PropertyChangeEvent(this, PROP_ACTIVATED_NODES, false, true));
        }
    }

    @Override
    public void panelDeactivated() {
        TopComponent.getRegistry().removePropertyChangeListener(getComponent());
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

}
