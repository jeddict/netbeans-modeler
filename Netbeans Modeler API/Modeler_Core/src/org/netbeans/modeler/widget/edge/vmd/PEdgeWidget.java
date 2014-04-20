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
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IPModelerScene;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.edge.IPEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;

public abstract class PEdgeWidget extends EdgeWidget implements IPEdgeWidget {

    private IColorScheme colorScheme;
    private boolean highlightStatus = false;

    public PEdgeWidget(IModelerScene scene, EdgeWidgetInfo edge) {
        this(scene, edge, ((IPModelerScene) scene).getColorScheme());
    }

    public PEdgeWidget(IModelerScene scene, EdgeWidgetInfo edge, IColorScheme colorScheme) {
        super(scene, edge);
        setAnchorGap(0);
        this.colorScheme = colorScheme;
        colorScheme.installUI(this);
        setState(ObjectState.createNormal());
    }

    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
//        super.notifyStateChanged(previousState, state);
        if (!highlightStatus) {
            colorScheme.updateUI(this, previousState, state);
        }

    }

    /**
     * @return the colorScheme
     */
    public IColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * @param colorScheme the colorScheme to set
     */
    public void setColorScheme(IColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }
    private int anchorGap;

    public int getAnchorGap() {
        return anchorGap;
    }

    public void setAnchorGap(int anchorGap) {
        this.anchorGap = anchorGap;
    }

    /**
     * @return the highlightStatus
     */
    public boolean isHighlightStatus() {
        return highlightStatus;
    }

    /**
     * @param highlightStatus the highlightStatus to set
     */
    public void setHighlightStatus(boolean highlightStatus) {
        this.highlightStatus = highlightStatus;
    }
}
