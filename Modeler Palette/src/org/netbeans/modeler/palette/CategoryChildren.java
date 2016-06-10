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
package org.netbeans.modeler.palette;

import java.util.List;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.config.palette.CategoryNodeConfig;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

public class CategoryChildren extends Children.Keys<CategoryNodeConfig> {

    List<CategoryNodeConfig> categoryNodeConfigs;

    public CategoryChildren(ModelerFile file) {
        categoryNodeConfigs = file.getModelerDiagramModel().getPaletteConfig().getCategoryNodeConfigs();//PaletteConfigFactory.getCategoryNodeConfig(file.getModelerFileDataObject().getClass());
    }

    @Override
    protected Node[] createNodes(CategoryNodeConfig key) {
        return new Node[]{new CategoryNode(key)};
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(categoryNodeConfigs);
    }
}
