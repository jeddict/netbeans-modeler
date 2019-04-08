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

import java.awt.Color;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.netbeans.modeler.shape.adapter.ColorAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Border {

    @XmlAttribute
    @XmlJavaTypeAdapter(ColorAdapter.class)
    @JsonbTypeAdapter(ColorAdapter.class)
    private Color color;
    @XmlAttribute
    private Float width;

    public Border(Color color, Float width) {
        this.color = color;
        this.width = width;
    }

    public Border() {
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the width
     */
    public Float getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Float width) {
        this.width = width;
    }

}
