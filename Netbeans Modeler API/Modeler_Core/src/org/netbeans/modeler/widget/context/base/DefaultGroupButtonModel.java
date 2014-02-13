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
import java.awt.Image;
import java.util.ArrayList;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.modeler.core.scene.ModelerScene;

public class DefaultGroupButtonModel extends DefaultPaletteButtonModel {

    private ArrayList< ContextPaletteButtonModel> buttonList
            = new ArrayList< ContextPaletteButtonModel>();

    public DefaultGroupButtonModel() {
    }

    public void add(ContextPaletteButtonModel desc) {
        buttonList.add(desc);
    }

    public ContextPaletteButtonModel getActiveDescription() {
        return this;//buttonList.get(0);
    }

    ///////////////////////////////////////////////////////////////
    // RapidButtonDescription Overrides
    public boolean isGroup() {
        return true;
    }

    public WidgetAction[] createActions(ModelerScene scene) {
//        ContextPaletteButtonModel activeDesc = getActiveDescription();
//        return activeDesc.createActions(scene);
        return null;
    }

    public ArrayList<ContextPaletteButtonModel> getChildren() {
        return buttonList;
    }

    ///////////////////////////////////////////////////////////////
    // Data Accessors
    public Image getImage() {
        Image retVal = super.getImage();

//        ContextPaletteButtonModel active = getActiveDescription();
//        if (active != null) {
//            retVal = active.getImage();
//        }
        return retVal;
    }

//    public void setImage(Image image)
//    {
//        ContextPaletteButtonModel active = getActiveDescription();
//        if(active != null)
//        {
//            active.setImage(image);
//        }
//    }
    public String getName() {
        String retVal = super.getName();

//        ContextPaletteButtonModel active = getActiveDescription();
//        if (active != null) {
//            retVal = active.getName();
//        }
        return retVal;
    }

//    public void setName(String name)
//    {
//        ContextPaletteButtonModel active = getActiveDescription();
//        if(active != null)
//        {
//            active.setName(name);
//        }
//    }
//    public String getConnectionType()
//    {
//        String retVal = null;
//
//        RapidButtonDescription active = getActiveDescription();
//        if(active != null)
//        {
//            retVal = active.getConnectionType();
//        }
//        return retVal;
//    }
}
