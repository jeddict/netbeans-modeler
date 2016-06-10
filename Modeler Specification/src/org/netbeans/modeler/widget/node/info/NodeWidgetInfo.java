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
package org.netbeans.modeler.widget.node.info;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;

public class NodeWidgetInfo implements Cloneable {

    private String id;
    private String name;
    private Image image;
    private Point location;
    private Dimension dimension;
    private SubCategoryNodeConfig subCategoryNodeConfig;
    private IModelerDocument modelerDocument;
    private IBaseElement baseElementSpec;
    private Boolean exist = false;//to Load Element

    public NodeWidgetInfo(String id, SubCategoryNodeConfig subCategoryNodeConfig, Point location) {
        this.id = id;
        this.location = location;
        this.subCategoryNodeConfig = subCategoryNodeConfig;
        this.modelerDocument = subCategoryNodeConfig.getModelerDocument();
        this.image = subCategoryNodeConfig.getModelerDocument().getImage();

    }
    
     public NodeWidgetInfo(String id, IModelerDocument modelerDocument, Point location) {
        this.id = id;
        this.location = location;
        this.modelerDocument = modelerDocument;
        this.image = modelerDocument.getImage();
    }

    private NodeWidgetInfo() {
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

//    /**
//     * @return the imagePath
//     */
//    public String getImagePath() {
//        return imagePath;
//    }
//
//    /**
//     * @param imagePath the imagePath to set
//     */
//    public void setImagePath(String imagePath) {
//        this.imagePath = imagePath;
//    }
    /**
     * @return the modelerDocument
     */
    public IModelerDocument getModelerDocument() {
        return modelerDocument;
    }

    /**
     * @return the dimension
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * @param dimension the dimension to set
     */
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        NodeWidgetInfo cloned = (NodeWidgetInfo) super.clone();
        cloned.setBaseElementSpec(null);
//	cloned.setBaseElementSpec((IBaseElement)cloned.getBaseElementSpec().clone());
        return cloned;
    }

//  @Override
    public NodeWidgetInfo cloneNodeWidgetInfo() {
        NodeWidgetInfo cloned = new NodeWidgetInfo();
        cloned.setId(id);
        cloned.setLocation(location);
        cloned.setDimension(dimension);
        return cloned;
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
     * @return the subCategoryNodeConfig
     */
    public SubCategoryNodeConfig getSubCategoryNodeConfig() {
        return subCategoryNodeConfig;
    }

    /**
     * @param subCategoryNodeConfig the subCategoryNodeConfig to set
     */
    public void setSubCategoryNodeConfig(SubCategoryNodeConfig subCategoryNodeConfig) {
        this.subCategoryNodeConfig = subCategoryNodeConfig;
    }
}
