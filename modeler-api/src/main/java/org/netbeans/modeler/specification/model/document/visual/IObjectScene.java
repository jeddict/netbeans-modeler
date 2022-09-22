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

import java.util.List;
import java.util.Set;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;

public interface IObjectScene extends IScene {

    /**
     * Adds a mapping between an object and a widget. Note that it does not add
     * the widget into the scene automatically - it has to be done manually
     * before this method is called.
     *
     * @param object the model object; the object must not be a Widget
     * @param widgets the scene widgets; if it is empty or it is a single null
     * value then the object is non-visual and does not have any widget
     * assigned; otherwise the widgets cannot contain null values
     */
    void addObject(Object object, Widget... widgets);

    /**
     * Adds object scene listener for specified object scene event types.
     *
     * @param listener the object scene listener
     * @param types the object scene event types
     */
    void addObjectSceneListener(ObjectSceneListener listener, ObjectSceneEventType... types);

    /**
     * Returns a object-oriented hover action.
     *
     * @return the object-oriented hover action
     */
    WidgetAction createObjectHoverAction();

    /**
     * Creates a object-oriented select action.
     *
     * @return the object-oriented select action
     */
    WidgetAction createSelectAction();

    /**
     * Returns an object which is assigned to a widget. If the widget is not
     * mapped to any object then the method recursively searches for an object
     * of the parent widget.
     *
     * @param widget the widget
     * @return the mapped object; null if no object is assigned to a widget or
     * any of its parent widgets
     */
    Object findObject(Widget widget);

    /**
     * Returns an instance of stored object. It searches for an instance of an
     * object stored internally in the class using "equals" method on an object.
     *
     * @param object the object that is equals (observed by calling the "equals"
     * method on the instances stored in the class); the object must not be a
     * Widget
     * @return the stored instance of the object
     */
    Object findStoredObject(Object object);

    /**
     * Returns the widget that is mapped to a specified object.
     *
     * @param object the object; must not be a Widget
     * @return the widget from the registered mapping; null if the object is
     * non-visual or no mapping is registered
     */
    Widget findWidget(Object object);

    /**
     * Returns a list of all widgets that are mapped to a specified object.
     *
     * @param object the object; must not be a Widget
     * @return the list of all widgets from the registered mapping; empty list
     * if the object is non-visual; null if no mapping is registered
     */
    List<Widget> findWidgets(Object object);

    /**
     * Returns a focused object. There could be only one focused object at
     * maximum at the same time.
     *
     * @return the focused object; null if no object is focused
     */
    Object getFocusedObject();

    /**
     * Returns a set of highlighted objects.
     *
     * @return the set of highlighted objects
     */
    Set<?> getHighlightedObjects();

    /**
     * Returns a hovered object. There could be only one hovered object at
     * maximum at the same time.
     *
     * @return the hovered object; null if no object is hovered
     */
    Object getHoveredObject();

    /**
     * This method returns an identity code. It should be unique for each object
     * in the scene. The identity code is a Comparable and could be used for
     * sorting. The method implementation should be fast.
     *
     * @param object the object
     * @return the identity code of the object; null, if the object is null
     */
    Comparable getIdentityCode(Object object);

    /**
     * Returns an object-state of a specified object.
     *
     * @param object the object
     * @return the object-state of the specified object; null if the object is
     * not registered
     */
    ObjectState getObjectState(Object object);

    /**
     * Returns a set of objects with registered mapping.
     *
     * @return the set of register objects
     */
    Set<?> getObjects();

    /**
     * Returns a set of selected objects.
     *
     * @return the set of selected objects
     */
    Set<?> getSelectedObjects();

    /**
     * Returns whether a specified object is registered.
     *
     * @param object the object to be checked
     * @return true if the object is register; false if the object is not
     * registered
     */
    boolean isObject(Object object);

    /**
     * Removes a mapping for an object. Note that it does not remove the widget
     * from the scene automatically - it has to be done manually after this
     * method is called.
     *
     * @param object the object for which the mapping is removed
     */
    void removeObject(Object object);

    /**
     * Removes object scene listener for specified object scene event types.
     *
     * @param listener the object scene listener
     * @param types the object scene event types
     */
    void removeObjectSceneListener(ObjectSceneListener listener, ObjectSceneEventType... types);

    /**
     * Sets a focused object.
     *
     * @param focusedObject the focused object; if null, then the scene does not
     * have focused object
     */
    void setFocusedObject(Object focusedObject);

    /**
     * Sets a set of highlighted objects.
     *
     * @param highlightedObjects the set of highlighted objects
     */
    void setHighlightedObjects(Set<?> highlightedObjects);

    /**
     * Sets a hovered object.
     *
     * @param hoveredObject the hovered object; if null, then the scene does not
     * have hovered object
     */
    void setHoveredObject(Object hoveredObject);

    /**
     * Sets a set of selected objects.
     *
     * @param selectedObjects the set of selected objects
     */
    void setSelectedObjects(Set<?> selectedObjects);

    /**
     * Set by actions for setting selected objects invoked by an user.
     *
     * @param suggestedSelectedObjects the selected objects suggested by an user
     * @param invertSelection the invert selection is specified by an user
     */
    void userSelectionSuggested(Set<?> suggestedSelectedObjects, boolean invertSelection);
}
