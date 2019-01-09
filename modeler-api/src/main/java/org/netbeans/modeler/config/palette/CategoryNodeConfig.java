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
package org.netbeans.modeler.config.palette;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.netbeans.modeler.svg.SvgImage;
import org.netbeans.modeler.util.Util;
import org.openide.util.Exceptions;

@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryNodeConfig {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String icon;
    @XmlTransient
    private Image paletteImage;
    @XmlTransient
    private SvgImage svgPaletteImage;

    @XmlElement(name = "subcategory-node")
    private List<SubCategoryNodeConfig> subCategoryNodeConfigs = new ArrayList<SubCategoryNodeConfig>();

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {

        if (icon != null) {
            String extension = icon.substring(icon.lastIndexOf(".") + 1, icon.length());
            if ("svg".equalsIgnoreCase(extension)) {

                svgPaletteImage = Util.loadSvgImage(icon);
            } else if ("gif".equalsIgnoreCase(extension)
                    || "png".equalsIgnoreCase(extension)
                    || "jpg".equalsIgnoreCase(extension)
                    || "jpeg".equalsIgnoreCase(extension)) {
                paletteImage = Util.loadImage(icon);
            }
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
     * @return the subCategoryNodeConfigs
     */
    public List<SubCategoryNodeConfig> getSubCategoryNodeConfigs() {
        return subCategoryNodeConfigs;
    }

    /**
     * @param subCategoryNodeConfigs the subCategoryNodeConfigs to set
     */
    public void setSubCategoryNodeConfigs(List<SubCategoryNodeConfig> subCategoryNodeConfigs) {
        this.subCategoryNodeConfigs = subCategoryNodeConfigs;
    }

    public Image getIcon() {
        Image img = null;
        try {
            if (svgPaletteImage != null) {
                img = svgPaletteImage.getImage(32, 32);
            } else {
                img = paletteImage;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return img;
    }

    public Image getIcon(int width, int height) {
        Image img = null;
        try {
            if (svgPaletteImage != null) {
                img = svgPaletteImage.getImage(width, height);
            } else {
                img = paletteImage;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return img;
    }

}
