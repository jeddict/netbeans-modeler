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
package org.netbeans.modeler.config.document;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.netbeans.modeler.util.Util;
import org.netbeans.modeler.widget.node.image.svg.SvgImage;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.w3c.dom.svg.SVGDocument;
//import org.w3c.dom.svg.SVGDocument;

/**
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelerDocument implements IModelerDocument {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private Class specification;
    @XmlAttribute
    private Class widget;
    @XmlAttribute
    private String definition;
    @XmlAttribute
    private FlowDimensionType flowDimension;
    @XmlAttribute
    private String documentModel;
    @XmlElement(name = "container-document")
    private String documentPath;

//    @XmlTransient
//    private SVGDocument document;
    @XmlTransient
    private Image image;
    @XmlTransient
    private SvgImage svgImage;
    @XmlElement(name = "shape-design")
    private StandardShapeDesign shapeDesign;
    @XmlElement
    private BoundsConstraint bounds;

    @Override
    public void init() {
        String extension = getDocumentPath().substring(getDocumentPath().lastIndexOf(".") + 1, getDocumentPath().length());
        if ("svg".equalsIgnoreCase(extension)) {

            SvgImage svgImage = Util.loadSvgImage(getDocumentPath());
            this.svgImage = svgImage;
        } else if ("gif".equalsIgnoreCase(extension)
                || "png".equalsIgnoreCase(extension)
                || "jpg".equalsIgnoreCase(extension)
                || "jpeg".equalsIgnoreCase(extension)) {
            image = Util.loadImage(getDocumentPath());
        }

    }

    @Override
    public SVGDocument generateDocument() {
        SVGDocument document = (SVGDocument) this.getDocument().cloneNode(true);
        return document;
    }

//     public Image getImage(double width , double height) {
//        Image imageObj = null;
//         try {
//            SvgImage svgImage = new SvgImage(this.getDocument());
//            imageObj =  svgImage.getImage(width, height);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//         return imageObj;
//    }
    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the documentPath
     */
    @Override
    public String getDocumentPath() {
        return documentPath;
    }

    /**
     * @return the documentPath
     */
    @Override
    public String getImagePath() {
        return FileUtil.getConfigRoot().getPath() + File.pathSeparatorChar + documentPath;
    }

    @Override
    public String getImageFileName() {
        String documentPath_Tmp;
        documentPath_Tmp = this.documentPath.replace("\\", "/");
        int start = documentPath_Tmp.lastIndexOf("/") + 1;
        int end = documentPath_Tmp.length();//imagePath.lastIndexOf(".");
        end = start < end ? end : documentPath_Tmp.length();
        return documentPath_Tmp.substring(start, end);
    }

    /**
     * @param documentPath the documentPath to set
     */
    @Override
    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

//    /**
//     * @return the document
//     */
//    public SVGDocument getDocument() {
//        return document;
//    }
//
//    /**
//     * @param document the document to set
//     */
//    public void setDocument(SVGDocument document) {
//        this.document = document;
//    }
    /**
     * @return the image
     */
    @Override
    public Image getImage() {
        Image image = null;
        try {
            if (svgImage != null) {
                image = svgImage.getImage(bounds.getWidth().getValue(), bounds.getHeight().getValue());
            } else {
                image = this.image;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return image;
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(getImage());
    }

    /**
     * @param image the image to set
     */
//    @Override
//    public void setImage(Image image) {
//        this.image = image;
//    }
    /**
     * @return the flowDimension
     */
    @Override
    public FlowDimensionType getFlowDimension() {
        return flowDimension;
    }

    /**
     * @param flowDimension the flowDimension to set
     */
    @Override
    public void setFlowDimension(FlowDimensionType flowDimension) {
        this.flowDimension = flowDimension;
    }

    /**
     * @return the documentModelType
     */
    @Override
    public String getDocumentModel() {
        return documentModel;
    }

    /**
     * @param documentModelType the documentModelType to set
     */
    @Override
    public void setDocumentModel(String documentModel) {
        this.documentModel = documentModel;
    }

    /**
     * @return the bounds
     */
    @Override
    public BoundsConstraint getBounds() {
        return bounds;
    }

    /**
     * @param bounds the bounds to set
     */
    @Override
    public void setBounds(BoundsConstraint bounds) {
        this.bounds = bounds;
    }

    /**
     * @return the shapeDesign
     */
    @Override
    public StandardShapeDesign getDocumentShapeDesign() {
        return shapeDesign;
    }

    /**
     * @param shapeDesign the shapeDesign to set
     */
    @Override
    public void setDocumentShapeDesign(StandardShapeDesign shapeDesign) {
        this.shapeDesign = shapeDesign;
    }

    /**
     * @return the document
     */
//  BUG : org.w3c.dom.svg.SVGDocument JAXB can't handle interface ; add JAB_API from Netbeans Module Platform
    @Override
    public SVGDocument getDocument() {
        return svgImage.getSvgDocument();
//        return document;
    }

    /**
     * @param document the document to set
     */
//    @Override
//    public void setDocument(SVGDocument document) {
//        this.document = document;
//    }
    /**
     * @return the specification
     */
    @Override
    public Class getSpecification() {
        return specification;
    }

    /**
     * @param specification the specification to set
     */
    @Override
    public void setSpecification(Class specification) {
        this.specification = specification;
    }

    /**
     * @return the definition
     */
    @Override
    public String getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    @Override
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * @return the widget
     */
    @Override
    public Class getWidget() {
        return widget;
    }

    /**
     * @param widget the widget to set
     */
    @Override
    public void setWidget(Class widget) {
        this.widget = widget;
    }
}
