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
package org.netbeans.modeler.config.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 *
 */
@XmlRootElement(name = "element-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ElementConfig implements IElementConfig {
//    @XmlAttribute
//    String id;

    @XmlElementWrapper(name = "elements")
    @XmlElement(name = "element")
    private List<Element> elements = new ArrayList<>();
    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private List<Group> groups = new ArrayList<>();
    
    /**
     * @return the elements
     */
    @Override
    public List<Element> getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    @Override
    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public void addElement(Element element) {
        this.elements.add(element);
    }

    /**
     * @return the groups
     */
    @Override
    public List<Group> getGroups() {
        return groups;
    }

    @XmlTransient
    private Map<String, Group> groupsCache;
    public Group getGroup(String id) {
        if(groupsCache == null){
            groupsCache = groups.stream().collect(toMap(Group::getId, identity()));
        }
        
        return groupsCache.get(id);
    }

    /**
     * @param groups the groups to set
     */
    @Override
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public void addGroup(Group group) {
        this.groups.add(group);
    }
}
