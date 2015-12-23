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
package org.netbeans.modeler.properties.nentity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Column {

    private String name;
    private Integer width = -1;
    private boolean editable;
    private boolean hidden = false;
    private boolean autoIncrement = false;
    private String autoIncrementSufix = "";
    private Class clazz;
    private List<? extends Object> values = new LinkedList();

    public Column(String name, boolean editable, boolean hidden, Class clazz) {
        this(name, editable, clazz);
        this.hidden = hidden;
    }

    public Column(String name, boolean editable, boolean hidden, Class clazz, Integer width) {
        this(name, editable, hidden, clazz);
        this.width = width;

    }

    public Column(String name, boolean editable, Class clazz) {
        this.name = name;
        this.editable = editable;
        this.clazz = clazz;
    }

    public Column(String name, boolean editable, Class clazz, boolean autoIncrement, String autoIncrementSufix) {
        this.name = name;
        this.editable = editable;
        this.clazz = clazz;
        this.autoIncrement = autoIncrement;
        this.autoIncrementSufix = autoIncrementSufix;
    }

    public Column(String name, boolean editable, Class clazz, Integer width) {
        this(name, editable, clazz);
        this.width = width;
    }

    public Column(String name, boolean editable, Class clazz, List<? extends Object> values) {
        this.name = name;
        this.editable = editable;
        this.clazz = clazz;
        this.values = values;
    }

    public Column(String name, boolean editable, Class clazz, Object[] values) {
        this.name = name;
        this.editable = editable;
        this.clazz = clazz;
        this.values = Arrays.asList(values);
    }

    public Column(String name, boolean editable, Class clazz, Integer width, Object[] values) {
        this(name, editable, clazz, values);
        this.width = width;
    }

    public Column() {
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
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the clazz
     */
    public Class getClassType() {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClassType(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the values
     */
    public List<? extends Object> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<? extends Object> values) {
        this.values = values;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the autoIncrement
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * @param autoIncrement the autoIncrement to set
     */
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * @return the autoIncrementSufix
     */
    public String getAutoIncrementSufix() {
        return autoIncrementSufix;
    }

    /**
     * @param autoIncrementSufix the autoIncrementSufix to set
     */
    public void setAutoIncrementSufix(String autoIncrementSufix) {
        this.autoIncrementSufix = autoIncrementSufix;
    }
}
