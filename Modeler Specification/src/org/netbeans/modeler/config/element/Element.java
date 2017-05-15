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
package org.netbeans.modeler.config.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 *
 */
@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Element {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String category;
    @XmlAttribute(name = "class")
    private Class<?> classType;
    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<Attribute> attributes = new ArrayList<>();
    @XmlElement
    private String visible;

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);;
    }

    public Attribute getAttribute(String id) {
        Attribute attribute = null;
        for (Attribute attribute_TMP : this.attributes) {
            if (attribute_TMP.getId().equals(id)) {
                attribute = attribute_TMP;
                break;
            }
        }
        return attribute;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the classType
     */
    public Class<?> getClassType() {
        return classType;
    }

    /**
     * @param classType the classType to set
     */
    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
        /**
     * @return the visible
     */
    public String getVisible() {
        return visible;
    }

    @XmlTransient
    private Serializable visibilityExpression;


    /**
     * @param visible the visible to set
     */
    public void setVisible(String visible) {
        this.visible = visible;
    }

}
