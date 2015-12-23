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
package org.netbeans.modeler.shape;

import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.netbeans.modeler.shape.adapter.ColorAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class GradientPaint {

    @XmlAttribute(name = "start-color")
    @XmlJavaTypeAdapter(ColorAdapter.class)
    private Color startColor;
    @XmlAttribute(name = "start-offset")
    private Float startOffset;
    @XmlAttribute(name = "end-color")
    @XmlJavaTypeAdapter(ColorAdapter.class)
    private Color endColor;
    @XmlAttribute(name = "end-offset")
    private Float endOffset;

    public GradientPaint(Color startColor, Float startOffset, Color endColor, Float endOffset) {
        this.startColor = startColor;
        this.startOffset = startOffset;
        this.endColor = endColor;
        this.endOffset = endOffset;
    }

    public GradientPaint() {
    }

    /**
     * @return the startColor
     */
    public Color getStartColor() {
        return startColor;
    }

    /**
     * @param startColor the startColor to set
     */
    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    /**
     * @return the startOffset
     */
    public Float getStartOffset() {
        return startOffset;
    }

    /**
     * @param startOffset the startOffset to set
     */
    public void setStartOffset(Float startOffset) {
        this.startOffset = startOffset;
    }

    /**
     * @return the endColor
     */
    public Color getEndColor() {
        return endColor;
    }

    /**
     * @param endColor the endColor to set
     */
    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    /**
     * @return the endOffset
     */
    public Float getEndOffset() {
        return endOffset;
    }

    /**
     * @param endOffset the endOffset to set
     */
    public void setEndOffset(Float endOffset) {
        this.endOffset = endOffset;
    }

}
