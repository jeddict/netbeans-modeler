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
package org.netbeans.modeler.widget.edge.vmd;

import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.vmd.internal.PColorScheme;
import org.netbeans.modeler.widget.node.vmd.internal.PFactory;

public class PEdgeWidget extends EdgeWidget {

    private PColorScheme scheme;

    public PEdgeWidget(IModelerScene scene, EdgeWidgetInfo edge) {
        this(scene, edge, PFactory.getNetBeans60Scheme());
    }

    public PEdgeWidget(IModelerScene scene, EdgeWidgetInfo edge, PColorScheme scheme) {
        super(scene, edge);
        this.scheme = scheme;
        scheme.installUI(this);
        setState(ObjectState.createNormal());
    }

    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
//        super.notifyStateChanged(previousState, state);
        scheme.updateUI(this, previousState, state);

    }
}
