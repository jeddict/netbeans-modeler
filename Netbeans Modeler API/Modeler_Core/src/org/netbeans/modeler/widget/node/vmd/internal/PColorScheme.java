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
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.Image;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.edge.vmd.PEdgeWidget;

/**
 * Fork to implement custom functionality
 */
public abstract class PColorScheme {

    /**
     * Creates a new vmd color scheme.
     *
     * @since 2.5
     */
    protected PColorScheme() {
    }

    /**
     * Called to install UI to a node widget.
     *
     * @param widget the node widget
     * @since 2.5
     */
    public abstract void installUI(AbstractPNodeWidget widget);

    /**
     * Called to update UI of a node widget. Called from
     * AbstractPNodeWidget.notifyStateChanged method.
     *
     * @param widget the node widget
     * @param previousState the previous state
     * @param state the new state
     * @since 2.5
     */
    public abstract void updateUI(AbstractPNodeWidget widget, ObjectState previousState, ObjectState state);

    /**
     * Returns whether the node minimize button is on the right side of the node
     * header.
     *
     * @param widget the node widget
     * @return true, if the button is on the right side; false, if the button is
     * on the left side
     * @since 2.5
     */
    public abstract boolean isNodeMinimizeButtonOnRight(AbstractPNodeWidget widget);

    /**
     * Returns an minimize-widget image for a specific node widget.
     *
     * @param widget the node widget
     * @return the minimize-widget image
     * @since 2.5
     */
    public abstract Image getMinimizeWidgetImage(AbstractPNodeWidget widget);

    /**
     * Called to create a pin-category widget.
     *
     * @param widget the node widget
     * @param categoryDisplayName the category display name
     * @return the pin-category widget
     * @since 2.5
     */
    public abstract Widget createPinCategoryWidget(AbstractPNodeWidget widget, String categoryDisplayName);

    /**
     * Called to install UI to a connection widget.
     *
     * @param widget the connection widget
     * @since 2.5
     */
    public abstract void installUI(PEdgeWidget widget);

    /**
     * Called to update UI of a connection widget. Called from
     * PConnectionWidget.notifyStateChanged method.
     *
     * @param widget the connection widget
     * @param previousState the previous state
     * @param state the new state
     * @since 2.5
     */
    public abstract void updateUI(PEdgeWidget widget, ObjectState previousState, ObjectState state);

    /**
     * Called to install UI to a pin widget.
     *
     * @param widget the pin widget
     * @since 2.5
     */
    public abstract void installUI(AbstractPinWidget widget);

    /**
     * Called to update UI of a pin widget. Called from
     * PinWidget.notifyStateChanged method.
     *
     * @param widget the pin widget
     * @param previousState the previous state
     * @param state the new state
     * @since 2.5
     */
    public abstract void updateUI(AbstractPinWidget widget, ObjectState previousState, ObjectState state);

    /**
     * Returns a gap size of a node-anchor from a node-widget.
     *
     * @param anchor the node anchor
     * @return the gap size
     * @since 2.5
     */
    public abstract int getNodeAnchorGap(PNodeAnchor anchor);
}
