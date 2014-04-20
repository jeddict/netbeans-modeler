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

import java.util.ArrayList;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.IWidget;

/**
 * The ContextPaletteModel interface specifies the methods that the Context
 * Palette uses to interrogate the palette data.
 * <p>
 * A context palette can have a static location on the left side of the model
 * element, or it can follow the mouse as long as the mouse is over the
 * associated model element.
 *
 *
 */
public interface ContextPaletteModel {

    /**
     * The FOLLOWMODE enumeration is used to specify the location type of the
     * palette.
     */
    enum FOLLOWMODE {

        /**
         * The palette should have a static location on the side of the
         * associated model element.
         */
        NONE,
        /**
         * The palette should follow the mouse cursor as the user move the
         * cursor vertically.
         */
        VERTICAL_ONLY,
        /**
         * The palette should follow the mouse cursor as the user moves the
         * cursor vertically and horizontally. Typically the palette will be
         * displayed on the left or right side of the associated model element,
         * but the side that palette will be displayed on will be based on the
         * location of the cursor.
         */
        VERTICAL_AND_HORIZONTAL

    };

    /**
     * Retreive the context palette button models that should be used to
     * populate the palettes contents.
     *
     * @return The buttom models used to populate the palette.
     */
    public ArrayList< ContextPaletteButtonModel> getChildren();

    /**
     * The Widget that is associated with the context palette.
     *
     * @return the associated widget.
     */
    public IWidget getContext();

//    public boolean isList(int index);
    /**
     * Retrieves how the palette should behave as the mouse is moved.
     *
     * @return the mouse behavior.
     */
    public FOLLOWMODE getFollowMouseMode();
}
