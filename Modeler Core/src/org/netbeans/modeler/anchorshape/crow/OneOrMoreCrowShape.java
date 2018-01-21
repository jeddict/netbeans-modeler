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

/**
 * @author Shiwani Gupta
 */
public class OneOrMoreCrowShape extends CrowAnchorShape {

    public OneOrMoreCrowShape(int size) {
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
        path.moveTo(centerX, centerY);
        path.lineTo(centerX, -sin);
        path.moveTo(centerX, centerY);
        path.lineTo(centerX, sin);

    }

}
