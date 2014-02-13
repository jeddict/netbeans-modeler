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
package org.netbeans.modeler.provider;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.openide.windows.TopComponent;

/**
 *
 *
 */
public class ModelerSceneSelectProvider implements SelectProvider {

    @Override
    public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
        return false;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
        boolean retVal;
        if (widget instanceof Scene) {
            retVal = true;
        } else {
            retVal = ((IModelerScene) widget.getScene()).findObject(widget) != null;
        }
        return retVal;
    }

    @Override
    public void select(Widget widget, Point localLocation, boolean invertSelection) {
        IModelerScene modelerScene = (IModelerScene) widget;
        NBModelerUtil.hideContextPalette(modelerScene);
        modelerScene.exploreProperties();

    }
}
