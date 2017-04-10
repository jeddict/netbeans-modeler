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
package org.netbeans.modeler.widget.pin.info;

import java.awt.Image;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.widget.design.ITextDesign;
import org.netbeans.modeler.widget.info.WidgetInfo;

public class PinWidgetInfo implements WidgetInfo {

    private String id;
    private String name;
    private Image image;
    private String documentId; // inplaceof ImodelerDocument to identify
    private IBaseElement baseElementSpec;
    private Boolean exist = false;
    private ITextDesign textDesign;

    public PinWidgetInfo(String id,IBaseElement baseElementSpec, String documentId) {
        this.id = id;
        this.baseElementSpec=baseElementSpec;
        this.documentId = documentId;
    }

    public PinWidgetInfo(String id, String documentId) {
        this.id = id;
        this.documentId = documentId;
    }

    public PinWidgetInfo(String id,IBaseElement baseElementSpec) {
        this.id = id;
        this.baseElementSpec=baseElementSpec;
    }

    private PinWidgetInfo() {
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
     * @return the baseElementSpec
     */
    public IBaseElement getBaseElementSpec() {
        return baseElementSpec;
    }

    /**
     * @param baseElementSpec the baseElementSpec to set
     */
    public void setBaseElementSpec(IBaseElement baseElementSpec) {
        this.baseElementSpec = baseElementSpec;
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
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the textDesign
     */
    public ITextDesign getTextDesign() {
        return textDesign;
    }

    /**
     * @param textDesign the textDesign to set
     */
    public void setTextDesign(ITextDesign textDesign) {
        this.textDesign = textDesign;
    }
}
