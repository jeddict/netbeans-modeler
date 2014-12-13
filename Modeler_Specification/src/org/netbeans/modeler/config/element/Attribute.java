/** Copyright [2014] Gaurav Gupta
   *
   *Licensed under the Apache License, Version 2.0 (the "License");
   *you may not use this file except in compliance with the License.
   *You may obtain a copy of the License at
   *
   *    http://www.apache.org/licenses/LICENSE-2.0
   *
   *Unless required by applicable law or agreed to in writing, software
   *distributed under the License is distributed on an "AS IS" BASIS,
   *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   *See the License for the specific language governing permissions and
   *limitations under the License.
   */
 package org.netbeans.modeler.config.element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
     @XmlAttribute(name="class")
    private Class<?> classType;
    @XmlAttribute
    private String name;  
    @XmlAttribute
    private String condition;
    @XmlElement(name="display-name")
    private String displayName;  
    @XmlElement(name="description")
    private String shortDescription;
    
    @XmlAttribute
    private Boolean exist = true;
    @XmlAttribute(name="read-only")
    private Boolean readOnly = false;
     @XmlAttribute
    private String value;
    
    
    
    

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

   
    public String getFieldGetter()
{
    return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
}
    
    
    
// public static String findGetterName(Class clazz, String name) throws IntrospectionException, NoSuchFieldException, NoSuchMethodException {
//    Method getter = findGetter(clazz, name);
//    if (getter == null) throw new NoSuchMethodException(clazz+" has no "+name+" getter");
//    return getter.getName();
//}
//
//public static Method findGetter(Class clazz, String name) throws IntrospectionException, NoSuchFieldException {
//    BeanInfo info = Introspector.getBeanInfo(clazz);
//    for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
//        if (name.equals(pd.getName())) return pd.getReadMethod();
//    throw new NoSuchFieldException(clazz+" has no field "+name);
//}
//
//public static String findSetterName(Class clazz, String name) throws IntrospectionException, NoSuchFieldException, NoSuchMethodException {
//    Method setter = findSetter(clazz, name);
//    if (setter == null) throw new NoSuchMethodException(clazz+" has no "+name+" setter");
//    return setter.getName();
//}
//
//public static Method findSetter(Class clazz, String name) throws IntrospectionException, NoSuchFieldException {
//    BeanInfo info = Introspector.getBeanInfo(clazz);
//    for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
//        if (name.equals(pd.getName())) return pd.getWriteMethod();
//    throw new NoSuchFieldException(clazz+" has no field "+name);
//}   

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
    
    
}
