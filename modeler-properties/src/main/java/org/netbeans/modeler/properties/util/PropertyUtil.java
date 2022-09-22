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
package org.netbeans.modeler.properties.util;

import java.beans.PropertyVetoException;
import java.util.Map;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOperation;
import org.openide.util.Exceptions;

public class PropertyUtil {

    public static void exploreProperties(BasePropertyViewManager node, String displayName, Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
        node.setDisplayName(displayName);
        node.reloadSheet(propertyVisibilityHandlerList);
        IModelerPanel modelerPanel = node.getModelerScene().getModelerPanelTopComponent();
        if (modelerPanel.getExplorerManager().getRootContext() != node) {
            modelerPanel.getExplorerManager().setRootContext(node);
            try {
                modelerPanel.getExplorerManager().setSelectedNodes(new Node[]{node});
            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            }
            modelerPanel.setActivatedNodes(new Node[]{node});
        }
    }

    public static void refreshProperties(BasePropertyViewManager node, String displayName, Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
        node.setDisplayName(displayName);
        node.reloadSheet(propertyVisibilityHandlerList);
        IModelerPanel modelerPanel = node.getModelerScene().getModelerPanelTopComponent();
        modelerPanel.getExplorerManager().setRootContext(node);
        try {
            modelerPanel.getExplorerManager().setSelectedNodes(new Node[]{node});
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
        modelerPanel.setActivatedNodes(new Node[]{node});
    }

    public static void showProperties(BasePropertyViewManager node, String displayName, Map<String, PropertyVisibilityHandler> propertyVisibilityHandlerList) {
        node.setDisplayName(displayName);
        node.reloadSheet(propertyVisibilityHandlerList);
        NodeOperation.getDefault().showProperties(node);
    }

}
