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
package org.netbeans.modeler.widget.context.base;

import java.util.ArrayList;
import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;

public class DefaultGroupButtonModel extends DefaultPaletteButtonModel {

    private final ArrayList< ContextPaletteButtonModel> buttonList = new ArrayList<>();

    public DefaultGroupButtonModel() {
    }

    public ContextPaletteButtonModel getActiveDescription() {
        return this;//buttonList.get(0);
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public ArrayList<ContextPaletteButtonModel> getChildren() {
        return buttonList;
    }

    public void add(ContextPaletteButtonModel desc) {
        buttonList.add(desc);
    }

}
