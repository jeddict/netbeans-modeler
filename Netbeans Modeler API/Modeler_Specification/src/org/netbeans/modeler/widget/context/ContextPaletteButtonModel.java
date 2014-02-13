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
package org.netbeans.modeler.widget.context;

import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import org.netbeans.api.visual.action.WidgetAction;

/**
 * The ContextPaletteButtonModel interface specifies the data used to build the
 * buttons in the context palette. A palette button can be a group of buttons.
 * It is up to the palette implementation of how to display a group of actions.
 *
 *
 */
public interface ContextPaletteButtonModel {

    /**
     * Gives the ContextPaletteButtonModel the chance to initialize the buttons
     * details from a NetBeans DataObject
     *
     * @param fo the data object.
     */
    //  void initialize(DataObject fo);
    /**
     * Retrieves the actions that should be executed when the users presses the
     * button.
     *
     * @param scene The scene that contains the palettes associates widget.
     * @return The actions that need to be executed.
     */
//     WidgetAction[] createConnectActions(IModelerScene scene);
//
//     MouseListener addWidgetRemoveAction(IModelerScene scene);
//       MouseListener addWidgetReplaceAction(Scene scene );
    WidgetAction[] getWidgetActions();

    void setWidgetActions(WidgetAction[] actions);

    MouseListener getMouseListener();

    MouseMotionListener getMouseMotionListener();

    void setMouseListener(MouseListener mouseListener);

    void setMouseMotionListener(MouseMotionListener mouseMotionListener);

    /**
     * Checks if the palette button is a group of associated buttons.
     *
     * @return true if the button is a true, false otherwise.
     */
    boolean isGroup();

    /**
     * If the button is a group button, then getChildren will return the button
     * models that are part of the group.
     *
     * @return
     */
    ArrayList<ContextPaletteButtonModel> getChildren();

    /**
     * The image that represents the button.
     *
     * @return the image associated with the button.
     */
    Image getImage();

    /**
     * The buttons name.
     *
     * @return the name.
     */
    String getName();

    /**
     * The buttons tooltip.
     *
     * @return tooltip.
     */
    String getTooltip();

    /**
     * Sets the image for the button.
     *
     * @param image the image
     */
    void setImage(Image image);

    /**
     * Sets the buttons tooltip.
     *
     * @param tooltip the tooltip.
     */
    void setTooltip(String tooltip);

    /**
     * Sets the ContextPaletteModel that is owns the button model.
     *
     * @param model The owner.
     * @see ContextPaletteModel
     */
    void setPaletteModel(ContextPaletteModel model);

    /**
     * Retrieves the ContextPaletteModel that owns the button model.
     *
     * @return the owner.
     * @param ContextPaletteModel
     */
    ContextPaletteModel getPaletteModel();

    RelationshipFactory getFactory();

    void setFactory(RelationshipFactory factory);

    /**
     * @return the contextActionType
     */
    ContextActionType getContextActionType();

    /**
     * @param contextActionType the contextActionType to set
     */
    void setContextActionType(ContextActionType contextActionType);

    /**
     * @return the id
     */
    String getId();

    /**
     * @param id the id to set
     */
    void setId(String id);
}
