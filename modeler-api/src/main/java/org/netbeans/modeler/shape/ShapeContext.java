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
package org.netbeans.modeler.shape;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ShapeContext {

    private GradientPaint background;
    private Border border;

    public ShapeContext(GradientPaint background, Border border) {
        this.background = background;
        this.border = border;
    }

    public ShapeContext() {
    }

    /**
     * @return the background
     */
    public GradientPaint getBackground() {
        return background;
    }

    /**
     * @param background the background to set
     */
    public void setBackground(GradientPaint background) {
        this.background = background;
    }

    /**
     * @return the border
     */
    public Border getBorder() {
        return border;
    }

    /**
     * @param border the border to set
     */
    public void setBorder(Border border) {
        this.border = border;
    }
}
