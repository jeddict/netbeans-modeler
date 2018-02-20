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
package org.netbeans.modeler.widget.node;

import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.model.StateModel;
import org.netbeans.api.visual.vmd.VMDMinimizeAbility;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public interface IParentPNodeWidget extends StateModel.Listener, VMDMinimizeAbility {

    /**
     * Attaches a pin widget to the node widget.
     *
     * @param widget the pin widget
     */
    void attachPinWidget(Widget widget);

    /**
     * Collapses the widget.
     */
    @Override
    void collapseWidget();

    /**
     * Creates an extended pin anchor with an ability of reconnecting to the
     * node anchor when the node is minimized.
     *
     * @param anchor the original pin anchor from which the extended anchor is
     * created
     * @return the extended pin anchor, the returned anchor is cached and
     * returns a single extended pin anchor instance of each original pin anchor
     */
    Anchor createAnchorPin(Anchor anchor);

    /**
     * Expands the widget.
     */
    @Override
    void expandWidget();

    /**
     * Returns a header widget.
     *
     * @return the header widget
     */
    Widget getHeader();

    /**
     * Returns a minimize button widget.
     *
     * @return the miminize button widget
     */
    ImageWidget getMinimizeButton();

    /**
     * Returns a node anchor.
     *
     * @return the node anchor
     */
    Anchor getNodeAnchor();

    /**
     * Returns a node name.
     *
     * @return the node name
     */
    String getNodeName();

    /**
     * Returns a node name widget.
     *
     * @return the node name widget
     */
    LabelWidget getNodeNameWidget();

    /**
     * Returns a pins separator.
     *
     * @return the pins separator
     */
    Widget getPinsSeparator();

    /**
     * Check the minimized state.
     *
     * @return true, if minimized
     */
    boolean isMinimized();

    /**
     * Set the minimized state. This method will show/hide child widgets of this
     * Widget and switches anchors between node and pin widgets.
     *
     * @param minimized if true, then the widget is going to be minimized
     */
    void setMinimized(boolean minimized);

    /**
     * Sets a node name.
     *
     * @param nodeName the node name
     */
    void setNodeName(String nodeName);

    /**
     * Sets a node type (secondary name).
     *
     * @param nodeType the node type
     */
    void setNodeType(String nodeType);

    /**
     * Sorts and assigns pins into categories.
     *
     * @param pinsCategories the map of category name as key and a list of pin
     * widgets as value
     */
    void sortPins(Map<String, List<Widget>> pinsCategories);

    /**
     * Called when a minimized state is changed. This method will show/hide
     * child widgets of this Widget and switches anchors between node and pin
     * widgets.
     */
    @Override
    void stateChanged();

    /**
     * Toggles the minimized state. This method will show/hide child widgets of
     * this Widget and switches anchors between node and pin widgets.
     */
    void toggleMinimized();
}
