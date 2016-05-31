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
package org.netbeans.modeler.properties.embedded;

public class GenericEmbedded {

    private Boolean readOnly = false;

    public GenericEmbedded(String name, String displayName, String shortDescription, GenericEmbeddedEditor customDialog) {
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
        this.entityEditor = customDialog;
    }
    private String name;
    private String displayName;
    private String shortDescription;
    private GenericEmbeddedEditor entityEditor;
    private EmbeddedDataListener dataListener;

    public GenericEmbedded(String name, String displayName, String shortDescription) {
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
    }

    public GenericEmbedded() {
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
     * @return the entityEditor
     */
    public GenericEmbeddedEditor getEntityEditor() {
        return entityEditor;
    }

    /**
     * @param entityEditor the entityEditor to set
     */
    public void setEntityEditor(GenericEmbeddedEditor entityEditor) {
        this.entityEditor = entityEditor;
        if(entityEditor!=null && !entityEditor.isLoaded()){
            entityEditor.init();
            entityEditor.setLoaded();
        }
    }

    /**
     * @return the dataListener
     */
    public EmbeddedDataListener getDataListener() {
        return dataListener;
    }

    /**
     * @param dataListener the dataListener to set
     */
    public void setDataListener(EmbeddedDataListener dataListener) {
        this.dataListener = dataListener;
    }
}
