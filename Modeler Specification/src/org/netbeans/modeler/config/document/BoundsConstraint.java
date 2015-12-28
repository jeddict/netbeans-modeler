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
package org.netbeans.modeler.config.document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundsConstraint {

    @XmlElement
    private SizeConstraint width;
    @XmlElement
    private SizeConstraint height;

    public BoundsConstraint() {
    }

    public BoundsConstraint(SizeConstraint width, SizeConstraint height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @return the height
     */
    public SizeConstraint getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(SizeConstraint height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public SizeConstraint getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(SizeConstraint width) {
        this.width = width;
    }
}
