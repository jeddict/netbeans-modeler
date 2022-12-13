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
package org.netbeans.modeler.palette;

import java.awt.Image;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

public class SubCategoryNode extends AbstractNode {

    private SubCategoryNodeConfig subCategoryInfo;

    public SubCategoryNode(SubCategoryNodeConfig info) {
        super(Children.LEAF, Lookups.fixed(new Object[]{info}));
        this.subCategoryInfo = info;
        fireIconChange();
        String name = subCategoryInfo.getName();
        setName(name);
        this.setDisplayName(name);
        this.setValue("SubCategoryInfo", subCategoryInfo);
        subCategoryInfo.setImage(subCategoryInfo.getModelerDocument().getImage());
    }

    @Override
    public Image getIcon(int type) {
        return subCategoryInfo.getPaletteDocument().getPaletteIcon();

    }

    /**
     * @return the subCategoryInfo
     */
    public SubCategoryNodeConfig getSubCategoryInfo() {
        return subCategoryInfo;
    }
}
