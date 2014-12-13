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

import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import java.util.ArrayList;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IWidget;

/**
 *
 *
 */
public class DefaultContextPaletteModel implements ContextPaletteModel {

    private ArrayList< ContextPaletteButtonModel> descriptions
            = new ArrayList< ContextPaletteButtonModel>();
    private IWidget context = null;

    private FOLLOWMODE followMouse = FOLLOWMODE.NONE;

    public DefaultContextPaletteModel(IWidget widget) {
        setContext(widget);
    }

    public DefaultContextPaletteModel(IWidget widget,
            FOLLOWMODE mode) {
        this(widget);
        followMouse = mode;
    }

    public void addDescriptor(ContextPaletteButtonModel desc) {
        descriptions.add(desc);
    }

    public FOLLOWMODE getFollowMouseMode() {
        return followMouse;
    }

    // ContextPaletteModel Implementation
    public ArrayList< ContextPaletteButtonModel> getChildren() {
        return descriptions;
    }

    public IWidget getContext() {
        return context;
    }
    ///////////////////////////////////////////////////////////////
    // Data Access

    public void setContext(IWidget context) {
        this.context = context;
    }
}
