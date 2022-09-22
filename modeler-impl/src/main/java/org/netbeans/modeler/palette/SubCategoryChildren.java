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

import java.util.ArrayList;
import java.util.List;
import org.netbeans.modeler.config.palette.CategoryNodeConfig;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

public class SubCategoryChildren extends Index.ArrayChildren {

    private CategoryNodeConfig category;

    public SubCategoryChildren(CategoryNodeConfig Category) {
        this.category = Category;
    }

    @Override
    protected java.util.List<Node> initCollection() {
        List<Node> childrenNodes = new ArrayList<Node>(category.getSubCategoryNodeConfigs().size());
        for (SubCategoryNodeConfig subCategoryNodeConfig : category.getSubCategoryNodeConfigs()) {
            if (subCategoryNodeConfig.isVisible()) {
                childrenNodes.add(new SubCategoryNode(subCategoryNodeConfig));
            }
        }
        return childrenNodes;
    }
}
