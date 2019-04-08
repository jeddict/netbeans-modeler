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
package org.netbeans.modeler.widget.node.vmd.internal;

import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.pin.IPinSeperatorWidget;

public class PinSeperatorWidget extends LabelWidget implements IPinSeperatorWidget {

    public PinSeperatorWidget(Scene scene) {
        super(scene);
    }

    public PinSeperatorWidget(Scene scene, String label) {
        super(scene, label);
    }

    @Override
    public ContextPaletteModel getContextPaletteModel() {
        return null;
    }

    @Override
    public IModelerScene getModelerScene() {
        return (IModelerScene) this.getScene();
    }

    @Override
    public PopupMenuProvider getPopupMenuProvider() {
        return null;
    }

}
