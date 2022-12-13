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
package org.netbeans.modeler.specification.model.document.visual;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.animator.SceneAnimator;
import org.netbeans.api.visual.laf.InputBindings;
import org.netbeans.api.visual.laf.LookFeel;
import org.netbeans.api.visual.widget.BirdViewController;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.ResourceTable;
import org.netbeans.api.visual.widget.Scene.SceneListener;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.IWidget;

public interface IScene extends IWidget {

    /**
     * Registers a scene listener.
     *
     * @param listener the scene listener
     */
    void addSceneListener(SceneListener listener);

    /**
     * Converts a location in the scene coordination system to the view
     * coordination system.
     *
     * @param sceneLocation the scene location
     * @return the view location
     */
    Point convertSceneToView(Point sceneLocation);

    /**
     * Converts a rectangle in the scene coordination system to the view
     * coordination system.
     *
     * @param sceneRectangle the scene rectangle
     * @return the view rectangle
     */
    Rectangle convertSceneToView(Rectangle sceneRectangle);

    /**
     * Converts a location in the view coordination system to the scene
     * coordination system.
     *
     * @param viewLocation the view location
     * @return the scene location
     */
    Point convertViewToScene(Point viewLocation);

    /**
     * Creates a bird view with specific zoom factor.
     *
     * @return the bird view controller
     * @since 2.7
     */
    BirdViewController createBirdView();

    /**
     * Creates a satellite view.
     *
     * @return the satellite view
     */
    JComponent createSatelliteView();

    /**
     * Creates a view. This method could be called once only. Call the getView
     * method for getting created instance of a view.
     *
     * @return the created view
     */
    JComponent createView();

    /**
     * Creates a widget-specific hover action.
     *
     * @return the widget-specific hover action
     */
    WidgetAction createWidgetHoverAction();

    /**
     * Returns an active tool of the scene.
     *
     * @return the active tool; if null, then only default action chain of
     * widgets will be used
     */
    String getActiveTool();

    /**
     * Returns a default font of the scene.
     *
     * @return the default font
     */
    Font getDefaultFont();

    /**
     * Returns a focused widget of the scene.
     *
     * @return the focused widget; null if no widget is focused
     */
    Widget getFocusedWidget();

    /**
     * Returns an instance of Graphics2D which is used for calculating
     * boundaries and rendering all widgets in the scene.
     *
     * @return the instance of Graphics2D
     */
    Graphics2D getGraphics();

    /**
     * Returns input bindings of the scene.
     *
     * @return the input bindings
     * @since 2.4
     */
    InputBindings getInputBindings();

    /**
     * Returns a key events processing type of the scene.
     *
     * @return the processing type for key events
     */
    EventProcessingType getKeyEventProcessingType();

    /**
     * Returns a look'n'feel of the scene.
     *
     * @return the look'n'feel
     */
    LookFeel getLookFeel();

    /**
     * Returns maximum bounds of the scene.
     *
     * @return the maximum bounds
     */
    Rectangle getMaximumBounds();

    /**
     * Returns a prior actions. These actions are executed before any other
     * action in the scene. If any of these actions consumes an event that the
     * event processsing is stopped. Action locking is ignored.
     *
     * @return the prior actions
     */
    WidgetAction.Chain getPriorActions();

    @Override
    ResourceTable getResourceTable();

    /**
     * Returns a scene animator of the scene.
     *
     * @return the scene animator
     */
    SceneAnimator getSceneAnimator();

    /**
     * Returns an instance of created view
     *
     * @return the instance of created view; null if no view is created yet
     */
    JComponent getView();

    /**
     * Returns a zoom factor.
     *
     * @return the zoom factor
     */
    double getZoomFactor();

    /**
     * Returns whether the whole scene is validated and there is no widget or
     * region that has to be revalidated.
     *
     * @return true, if the whole scene is validated
     */
    @Override
    boolean isValidated();

    /**
     * Paints the whole scene into the graphics instance. The method calls
     * validate before rendering.
     *
     * @param graphics the Graphics2D instance where the scene is going to be
     * painted
     */
    void paint(Graphics2D graphics);

    /**
     * Unregisters a scene listener.
     *
     * @param listener the scene listener
     */
    void removeSceneListener(SceneListener listener);

    /**
     * Sets an active tool.
     *
     * @param activeTool the active tool; if null, then the active tool is unset
     * and only default action chain of widgets will be used
     */
    void setActiveTool(String activeTool);

    /**
     * Sets a focused widget of the scene.
     *
     * @param focusedWidget the focused widget; if null, then the scene itself
     * is taken as the focused widget
     */
    void setFocusedWidget(Widget focusedWidget);

    /**
     * Sets a key events processing type of the scene.
     *
     * @param keyEventProcessingType the processing type for key events
     */
    void setKeyEventProcessingType(EventProcessingType keyEventProcessingType);

    /**
     * Sets a look'n'feel of the scene. This method does affect current state of
     * the scene - already created components will not be refreshed.
     *
     * @param lookFeel the look'n'feel
     */
    void setLookFeel(LookFeel lookFeel);

    /**
     * Sets maximum bounds of the scene.
     *
     * @param maximumBounds the non-null maximum bounds
     */
    void setMaximumBounds(Rectangle maximumBounds);

    @Override
    void setResourceTable(ResourceTable table);

    /**
     * Sets a zoom factor for the scene.
     *
     * @param zoomFactor the zoom factor
     */
    void setZoomFactor(double zoomFactor);

    /**
     * This method invokes Scene.validate method with a specific Graphics2D
     * instance. This is useful for rendering a scene off-screen without
     * creating and showning the main scene view. See
     * test.view.OffscreenRenderingTest example for usages.
     * <p>
     * Note: Do not call this method unless you know the consequences. The scene
     * is going to be validated using the specified Graphics2D instance even
     * after the method call therefore it may break scene layout when your main
     * scene view is finally created and shown.
     *
     * @param graphics the graphics instance used for validation
     * @since 2.7
     */
    void validate(Graphics2D graphics);

    /**
     * Validates all widget in the whole scene. The validation is done
     * repeatively until there is no invalid widget in the scene after
     * validating process. It also schedules invalid regions in the view for
     * repainting.
     */
    @SuppressWarnings(value = "unchecked")
    void validate();
}
