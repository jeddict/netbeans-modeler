/**
 * Copyright [2016] Gaurav Gupta
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
package org.netbeans.modeler.anchorshape.crow;

import org.netbeans.api.visual.anchor.AnchorShape;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * @author Shiwani Gupta
 */
public class CrowAnchorShape implements AnchorShape {

    protected static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    protected GeneralPath path = new GeneralPath();
    protected int size;

    public CrowAnchorShape(int size) {
        this.size = size;
    }

    public boolean isLineOriented() {
        return true;
    }

    public int getRadius() {
        return size + 1;
    }

    public double getCutDistance() {
        return 0;
    }

    public void paint(Graphics2D graphics, boolean source) {
        Stroke previousStroke = graphics.getStroke();
        graphics.setStroke(STROKE);
        graphics.draw(path);
        graphics.setStroke(previousStroke);
    }

}
