/**
 * Copyright [2015] Gaurav Gupta
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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.modeler.widget.node.IWidgetStateHandler.StateType;

/**
 *
 * @author Gaurav Gupta
 */
public class AdvanceImageWidget extends ImageWidget {

    private StateType stateType;

    public AdvanceImageWidget(Scene scene, Image image) {
        super(scene, image);
    }

    public AdvanceImageWidget(Scene scene) {
        super(scene);
    }

    private final ImageObserver observer = (Image img, int infoflags, int x, int y, int width1, int height1) -> {
        setImage(getImage());
        getScene().validate();
        return (infoflags & (ImageObserver.ABORT | ImageObserver.ERROR)) == 0;
    };

    /**
     * Paints the image widget.
     */
    @Override
    protected void paintWidget() {
        if (getImage() == null) {
            return;
        }
        Graphics2D gr = getGraphics();
        if (getImage() != null) {
            if (getStateType() != null) {
                gr.drawImage(getImage(), 0, 0, null);
                if (stateType == StateType.ERROR) {
                    gr.drawImage(stateType.getIcon(), 0, getImage().getHeight(null) - stateType.getIcon().getHeight(null), observer);
                } else {
                    gr.drawImage(stateType.getIcon(), 0, 0, observer);
                }
            } else {
                gr.drawImage(getImage(), 0, 0, observer);

            }
        }
    }

    /**
     * @return the errorState
     */
    public StateType getStateType() {
        return stateType;
    }

    /**
     * @param state the errorState to set
     */
    public void setStateType(StateType state) {
        this.stateType = state;
        revalidate();
    }

}
