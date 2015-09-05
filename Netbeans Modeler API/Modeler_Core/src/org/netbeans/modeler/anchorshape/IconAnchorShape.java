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
package org.netbeans.modeler.anchorshape;

import java.awt.Graphics2D;
import java.awt.Image;
import org.netbeans.api.visual.anchor.AnchorShape;

public class IconAnchorShape implements AnchorShape {

    private Image image;
    private boolean lineOriented;
    private int radius;
    private int x, y;

    public IconAnchorShape(Image image, boolean lineOriented) {
        this(image, lineOriented, image.getWidth(null), image.getHeight(null));
    }
  public IconAnchorShape(Image image) {
        this(image, false, image.getWidth(null), image.getHeight(null));
    }

    public IconAnchorShape(Image image, boolean lineOriented, int x, int y) {
        this.x = x;
        this.y = y;
        this.lineOriented = lineOriented;
        assert image != null;
        this.image = image;
        radius = Math.max(x, y);
        this.x = -(x / 4);
        this.y = -(y / 2);
    }

    public boolean isLineOriented() {
        return lineOriented;
    }

    public int getRadius() {
        return radius;
    }

    public double getCutDistance() {
        return 0.0;
    }

    public void paint(Graphics2D graphics, boolean source) {
        graphics.drawImage(getImage(), x, y, null);
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

}
