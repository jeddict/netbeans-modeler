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
package org.netbeans.modeler.config.document;

import java.awt.Image;
import java.io.IOException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import org.netbeans.modeler.svg.SvgImage;
import org.openide.util.Exceptions;

@XmlAccessorType(XmlAccessType.FIELD)
public class PaletteDocument {

    @XmlAttribute
    private double width;
    @XmlAttribute
    private double height;
//    @XmlValue
    @XmlAttribute(name = "icon")
    private String path;
//    @XmlTransient
//    private Image paletteIcon;
//    @XmlTransient
//    private Image paletteSmallIcon; // for widget menuItem
    @XmlTransient
    private Image paletteImage;
    @XmlTransient
    private SvgImage svgPaletteImage;

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the paletteImage
     */
    public Image getPaletteIcon() {
        Image img = null;
        try {
            if (svgPaletteImage != null) {
                img = svgPaletteImage.getImage(
                        this.getWidth(),
                        this.getHeight());
            } else {
                img = paletteImage;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return img;
    }

    /**
     * @return the paletteSmallIcon
     */
    public Image getPaletteSmallIcon() {
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

    public Image getPaletteSmallIcon(int width, int height) {
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
//
//      public Image getSmallRefIcon() {
//        Image img = null;
//        try {
//            img = svgPaletteImage.getSvgDocument().getElementById("o")..getImage(32, 32);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return img;
//    }
//

    /**
     * @return the svgPaletteImage
     */
    public SvgImage getSvgPaletteImage() {
        return svgPaletteImage;
    }

    /**
     * @param svgPaletteImage the svgPaletteImage to set
     */
    public void setSvgPaletteImage(SvgImage svgPaletteImage) {
        this.svgPaletteImage = svgPaletteImage;
    }

    /**
     * @return the paletteImage
     */
    public Image getPaletteImage() {
        return paletteImage;
    }

    /**
     * @param paletteImage the paletteImage to set
     */
    public void setPaletteImage(Image paletteImage) {
        this.paletteImage = paletteImage;
    }
}
