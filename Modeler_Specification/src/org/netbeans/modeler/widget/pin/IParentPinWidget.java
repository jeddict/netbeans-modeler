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
package org.netbeans.modeler.widget.pin;

import java.awt.Image;
import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.IWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.nodes.AbstractNode;
/* Is is orignal visual widget api interface */

public interface IParentPinWidget extends IWidget {


    /* Abstract PinWidget */
    /**
     * Creates a horizontally oriented anchor similar to
     * VMDNodeWidget.createAnchorPin
     *
     * @return the anchor
     */
    Anchor createAnchor();

    /**
     * Returns a pin name.
     *
     * @return the pin name
     */
    String getPinName();

    /**
     * Returns a pin name widget.
     *
     * @return the pin name widget
     */
    Widget getPinNameWidget();

    /**
     * Sets pin glyphs.
     *
     * @param glyphs the list of images
     */
    void setGlyphs(List<Image> glyphs);

    /**
     * Sets a pin name.
     *
     * @param name the pin name
     */
    void setPinName(String name);

    /**
     * Sets all pin properties at once.
     *
     * @param name the pin name
     * @param glyphs the pin glyphs
     */
    void setProperties(String name, List<Image> glyphs);

}
