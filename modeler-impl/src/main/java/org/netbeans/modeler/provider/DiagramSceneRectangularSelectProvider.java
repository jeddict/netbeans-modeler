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
package org.netbeans.modeler.provider;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Set;
import org.netbeans.api.visual.action.RectangularSelectProvider;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.modeler.scene.AbstractModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;

/**
 *
 *
 */
public class DiagramSceneRectangularSelectProvider implements RectangularSelectProvider {

    private ObjectScene scene;

    public DiagramSceneRectangularSelectProvider(ObjectScene scene) {
        this.scene = scene;
    }

    @Override
    public void performSelection(Rectangle sceneSelection) {

        if (scene instanceof AbstractModelerScene) {
            AbstractModelerScene designerScene = (AbstractModelerScene) scene;
            Set< IFlowElementWidget> set
                    = designerScene.getGraphObjectInRectangle(sceneSelection, true, true);

            Iterator<IFlowElementWidget> iterator = set.iterator();
            scene.setFocusedObject(iterator.hasNext() ? iterator.next() : null);
            scene.userSelectionSuggested(set, false);
        }

    }

}
