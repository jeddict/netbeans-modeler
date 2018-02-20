/**
 * Copyright 2013-2018 Gaurav Gupta
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

import java.util.List;

public interface IElementConfig {

    void addElement(Element element);

    /**
     * @return the elements
     */
    List<Element> getElements();

    /**
     * @param elements the elements to set
     */
    void setElements(List<Element> elements);

    /**
     * @return the groups
     */
    public List<Group> getGroups();

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<Group> groups);

    public void addGroup(Group group);

}
