/**
 * Copyright 2013-2022 Gaurav Gupta
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
import javax.swing.ImageIcon;
import org.netbeans.modeler.svg.SVGDocument;

public interface IModelerDocument {

    SVGDocument generateDocument();

    /**
     * @return the model
     */
    String getDocumentModel();

    /**
     * @return the shapeDesign
     */
    StandardShapeDesign getDocumentShapeDesign();

    /**
     * @return the bounds
     */
    BoundsConstraint getBounds();

    /**
     * @return the definition
     */
    String getDefinition();

    /**
     * @return the document
     */
    //  BUG : org.w3c.dom.svg.SVGDocument JAXB can't handle interface ; add JAB_API from Netbeans Module Platform
    SVGDocument getDocument();

    /**
     * @return the documentPath
     */
    String getDocumentPath();

    /**
     * @return the flowDimension
     */
    FlowDimensionType getFlowDimension();

    ImageIcon getIcon();

    /**
     * @return the id
     */
    String getId();

    /**
     * @return the image
     */
    Image getImage();

    String getImageFileName();

    /**
     * @return the documentPath
     */
    String getImagePath();

    /**
     * @return the specification
     */
    Class getSpecification();

    /**
     * @return the widget
     */
    Class getWidget();

    void init();

    /**
     * @param model the model to set
     */
    void setDocumentModel(String model);

    /**
     * @param shapeDesign the shapeDesign to set
     */
    void setDocumentShapeDesign(StandardShapeDesign shapeDesign);

    /**
     * @param bounds the bounds to set
     */
    void setBounds(BoundsConstraint bounds);

    /**
     * @param definition the definition to set
     */
    void setDefinition(String definition);

    /**
     * @param documentPath the documentPath to set
     */
    void setDocumentPath(String documentPath);

    /**
     * @param flowDimension the flowDimension to set
     */
    void setFlowDimension(FlowDimensionType flowDimension);

    /**
     * @param id the id to set
     */
    void setId(String id);

    /**
     * @param specification the specification to set
     */
    void setSpecification(Class specification);

    /**
     * @param widget the widget to set
     */
    void setWidget(Class widget);

}
