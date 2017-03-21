/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.modeler.widget.design;

import java.awt.Color;

/**
 *
 * @author jGauravGupta
 */
public interface ITextDesign {

    /**
     * @return the color
     */
    Color getColor();

    /**
     * @return the size
     */
    float getSize();

    /**
     * @return the style
     */
    int getStyle();

    /**
     * @param color the color to set
     */
    void setColor(Color color);

    /**
     * @param size the size to set
     */
    void setSize(float size);

    /**
     * @param style the style to set
     */
    void setStyle(int style);

    public boolean isChanged();

    public void reset();

    public int getDefaultStyle();

    public Color getDefaultColor();

    public float getDefaultSize();

}
