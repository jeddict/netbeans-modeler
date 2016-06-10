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
package org.netbeans.modeler.widget.context.base;

import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.function.Supplier;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.modeler.widget.context.ContextActionType;
import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.context.NodeInitializer;
import org.netbeans.modeler.widget.context.RelationshipFactory;

/**
 *
 *
 */
public class DefaultPaletteButtonModel implements ContextPaletteButtonModel {

    private String id;
    private Image image = null;
    private String name = "";
    private ContextActionType contextActionType;
    private String connectionType = "";
    private String defaultTargetType = "";//Class,Interface
    private String tooltip = "";
    private RelationshipFactory factory = null;//GeneralizationFactory.java,AssociationFactory.java
    private ContextPaletteModel paletteModel = null;
    private Supplier<Boolean> visibiltyCheck;

    public DefaultPaletteButtonModel() {
    }

    public DefaultPaletteButtonModel(Image image, String tooltip) {
        this.image = image;
        this.tooltip = tooltip;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public ArrayList<ContextPaletteButtonModel> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void setPaletteModel(ContextPaletteModel model) {
        paletteModel = model;
    }

    @Override
    public ContextPaletteModel getPaletteModel() {
        return paletteModel;
    }

    ///////////////////////////////////////////////////////////////
    // Data Accessors
    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getDefaultTargetType() {
        return defaultTargetType;
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public RelationshipFactory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(RelationshipFactory factory) {
        this.factory = factory;
    }

    /**
     * @return the contextActionType
     */
    @Override
    public ContextActionType getContextActionType() {
        return contextActionType;
    }

    /**
     * @param contextActionType the contextActionType to set
     */
    @Override
    public void setContextActionType(ContextActionType contextActionType) {
        this.contextActionType = contextActionType;
    }
    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;

    /**
     * @param mouseListener the mouseListener to set
     */
    @Override
    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    /**
     * @param mouseMotionListener the mouseMotionListener to set
     */
    @Override
    public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListener = mouseMotionListener;
    }

    /**
     * @return the mouseListener
     */
    @Override
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    /**
     * @return the mouseMotionListener
     */
    @Override
    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }
    private WidgetAction[] widgetActions;

    @Override
    public WidgetAction[] getWidgetActions() {
        return widgetActions;
    }

    @Override
    public void setWidgetActions(WidgetAction[] actions) {
        this.widgetActions = actions;
    }

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

    
    @Override
    public boolean isVisible() {
        if(visibiltyCheck!=null){
            return visibiltyCheck.get();
        }
        return true;
    }

    @Override
    public void checkVisibility(Supplier<Boolean> visibiltyCheck) {
        this.visibiltyCheck=visibiltyCheck;
    }
}
