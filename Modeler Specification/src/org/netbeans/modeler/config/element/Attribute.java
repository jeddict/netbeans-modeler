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
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.mvel2.MVEL;

/**
 *
 *
 */
@XmlRootElement(name = "attribute")
@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String groupId;
    @XmlAttribute(name = "class") //e.g java.lang.String, enum etc
    private Class<?> classType;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String condition;
    @XmlElement(name = "display-name")
    private String displayName;
    @XmlElement(name = "description")
    private String shortDescription;

    @XmlAttribute
    private Boolean exist = true;
    @XmlAttribute(name = "read-only")
    private Boolean readOnly = false;
    @XmlAttribute
    private String value;
    @XmlElement
    private String visible;
    @XmlElement(name = "change-listener")
    private String onChangeEvent;
    @XmlAttribute(name = "refresh-onchange")
    private Boolean refreshOnChange = false;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
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
     * @return the exist
     */
    public Boolean isExist() {
        return exist;
    }

    /**
     * @param exist the exist to set
     */
    public void setExist(Boolean exist) {
        this.exist = exist;
    }

    /**
     * @return the readOnly
     */
    public Boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldGetter() {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the visible
     */
    public String getVisible() {
        return visible;
    }

    @XmlTransient
    private Serializable visibilityExpression;

    public Serializable getVisibilityExpression() {
        if (visibilityExpression == null) {
            if (getElement().getVisible() != null && visible != null) {
                visibilityExpression = MVEL.compileExpression(getElement().getVisible().replaceAll("this", "_this") + " && " + getVisible().replaceAll("this", "_this"));
            } else if (getElement().getVisible() == null && visible != null) {
                visibilityExpression = MVEL.compileExpression(getVisible().replaceAll("this", "_this"));
            } else if (getElement().getVisible() != null && visible == null) {
                visibilityExpression = MVEL.compileExpression(getElement().getVisible().replaceAll("this", "_this"));
            } 
        }
        return visibilityExpression;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(String visible) {
        this.visible = visible;
    }

    /**
     * @return the onChangeEvent
     */
    public String getOnChangeEvent() {
        return onChangeEvent;
    }

    /**
     * @param onChangeEvent the onChangeEvent to set
     */
    public void setOnChangeEvent(String onChangeEvent) {
        this.onChangeEvent = onChangeEvent;
    }

    @XmlTransient
    private Serializable onChangeListenerExpression;

    public Serializable getChangeListenerExpression() {
        if (onChangeListenerExpression == null && onChangeEvent != null) {
            onChangeListenerExpression = MVEL.compileExpression(getOnChangeEvent().replaceAll("this", "_this"));
        }
        return onChangeListenerExpression;
    }

    /**
     * @return the refreshOnChange
     */
    public Boolean isRefreshOnChange() {
        return refreshOnChange;
    }

    /**
     * @param refreshOnChange the refreshOnChange to set
     */
    public void setRefreshOnChange(Boolean refreshOnChange) {
        this.refreshOnChange = refreshOnChange;
    }
    
    @XmlTransient
    private Element element;

    void afterUnmarshal(Unmarshaller u, Object parent) {
        setElement((Element) parent);
    }

    /**
     * @return the element
     */
    private Element getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    private void setElement(Element element) {
        this.element = element;
    }

}
