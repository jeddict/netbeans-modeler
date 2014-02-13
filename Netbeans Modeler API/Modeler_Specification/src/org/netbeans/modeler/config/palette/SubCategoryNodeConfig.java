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
package org.netbeans.modeler.config.palette;

import java.awt.Image;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.config.document.PaletteDocument;
import org.netbeans.modeler.util.Util;

@XmlAccessorType(XmlAccessType.FIELD)
public class SubCategoryNodeConfig {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String name;
    @XmlTransient
    private Image image;
    @XmlTransient
    private IModelerDocument modelerDocument;

    @XmlElement(name = "palette-document")
    private PaletteDocument paletteDocument;

    public void init() {

        String path = getPaletteDocument().getPath();
        String extension = path.substring(path.lastIndexOf(".") + 1, path.length());
        if ("svg".equalsIgnoreCase(extension)) {
            this.getPaletteDocument().setSvgPaletteImage(Util.loadSvgImage(path));
        } else if ("gif".equalsIgnoreCase(extension)
                || "png".equalsIgnoreCase(extension)
                || "jpg".equalsIgnoreCase(extension)
                || "jpeg".equalsIgnoreCase(extension)) {
            this.getPaletteDocument().setPaletteImage(Util.loadImage(path));
        }

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
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the modelerDocument
     */
    public IModelerDocument getModelerDocument() {

        return modelerDocument;
    }

    /**
     * @param modelerDocument the modelerDocument to set
     */
    public void setModelerDocument(IModelerDocument modelerDocument) {
        this.modelerDocument = modelerDocument;
    }

    /**
     * @return the paletteDocument
     */
    public PaletteDocument getPaletteDocument() {
        return paletteDocument;
    }

    /**
     * @param paletteDocument the paletteDocument to set
     */
    public void setPaletteDocument(PaletteDocument paletteDocument) {
        this.paletteDocument = paletteDocument;
    }
}
