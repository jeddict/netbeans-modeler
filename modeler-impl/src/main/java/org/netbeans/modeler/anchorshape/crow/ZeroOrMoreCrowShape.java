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

import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * @author Shiwani Gupta
 */
public class ZeroOrMoreCrowShape extends CrowAnchorShape {

    public ZeroOrMoreCrowShape(int size) {
        super(size);
        int degrees = 300;
        double radians = Math.PI * degrees / 180.0;
        double cos = Math.cos(radians / 2.0);
        double sin = -size * Math.sqrt(1 - cos * cos);

        float centerX = 0.0f + size;
        float centerY = 0.0f;

        path.moveTo(centerX, centerY);
        path.lineTo(centerX - size, -sin);
        path.moveTo(centerX, centerY);
        path.lineTo(centerX - size, sin);
        path.moveTo(centerX, centerY);
        path.lineTo(centerX - size, 0.0f);
    }

    public void paint(Graphics2D graphics, boolean source) {
        super.paint(graphics, source);
        Stroke previousStroke = graphics.getStroke();
        graphics.setStroke(STROKE);
        int radius = (int) (size * 0.87);//equals to circle of ZeroOrOneCrowShape
        graphics.drawOval(size, -radius / 2, radius, radius);
        graphics.setStroke(previousStroke);
    }
}
