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
import java.awt.Font;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author jGauravGupta
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlJavaTypeAdapter(value = TextDesignValidator.class)
public abstract class TextDesign implements ITextDesign {

    protected static final int DEFAULT_SIZE = 12;
    protected static final int DEFAULT_STYLE = Font.PLAIN;
    
    @XmlAttribute(name="st")
    protected int style = DEFAULT_STYLE;
    
    @XmlAttribute(name="si")
    protected float size = DEFAULT_SIZE;
    
    @XmlAttribute(name="cl")
    @XmlJavaTypeAdapter(ColorAdapter.class)
    protected Color color;

    public TextDesign() {
    }
    
    public TextDesign(int style, float size) {
        this.style = style;
        this.size = size;
    }
    
    public TextDesign(int style, float size, Color color) {
        this.style = style;
        this.size = size;
        this.color = color;
    }
    
    /**
     * @return the style
     */
    @Override
    public int getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    @Override
    public void setStyle(int style) {
        this.style = style;
    }

    /**
     * @return the size
     */
    @Override
    public float getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    @Override
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * @return the color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }
    
    public boolean isChanged() {
        return style != DEFAULT_STYLE
                || size != DEFAULT_SIZE
                || color != null;
    }

    public void reset() {
        style = DEFAULT_STYLE;
        size = DEFAULT_SIZE;
        color = null;
    }

    public int getDefaultStyle() {
        return DEFAULT_STYLE;
    }

    public Color getDefaultColor() {
        return null;
    }

    public float getDefaultSize() {
        return DEFAULT_SIZE;
    }

}
