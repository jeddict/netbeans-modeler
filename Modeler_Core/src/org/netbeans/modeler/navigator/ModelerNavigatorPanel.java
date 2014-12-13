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
package org.netbeans.modeler.navigator;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import org.netbeans.modeler.component.ModelerPanelTopComponent;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 *
 */
public abstract class ModelerNavigatorPanel implements NavigatorPanel {

    private DiagramNavigatorContent navigator = null;
    private Lookup.Template< IModelerScene> template
            = new Lookup.Template< IModelerScene>(IModelerScene.class);
//    private Lookup.Result<IModelerScene> result;

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
        getComponent();
        TopComponent.getRegistry().addPropertyChangeListener(navigator);
        TopComponent tc = getTopComponent();
        if (tc != null) {
//            result = tc.getLookup().lookup(template);
//            result.addLookupListener(this);
//            Collection c = result.allInstances();
//            resultChanged(null);
            navigator.propertyChange(new PropertyChangeEvent(this,
                    TopComponent.getRegistry().PROP_ACTIVATED_NODES, false, true));

        }
    }

    @Override
    public void panelDeactivated() {
        TopComponent.getRegistry().removePropertyChangeListener(navigator);

//        if (result != null) {
//            result.removeLookupListener(this);
//            result = null;
//        }
    }

    @Override
    public Lookup getLookup() {
//        TopComponent tc = TopComponent.getRegistry().getActivated()
//        return null;
        return Lookup.EMPTY;
    }

//    @Override
//    public void resultChanged(LookupEvent ev) {
//        Collection selected = result.allInstances();
//        if (selected.size() == 1) {
//            IModelerScene scene = (IModelerScene) selected.iterator().next();
//            navigator.navigate(scene.getSatelliteView());
//        }
//    }
}
