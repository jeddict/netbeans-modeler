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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import org.netbeans.modeler.config.palette.IPaletteConfig;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 *
 */
public class PaletteSupport {

    private static final Map<IPaletteConfig, PaletteController> paletteConfigs = new HashMap<>();
    
    public static PaletteController createPalette(ModelerFile file) {
        IPaletteConfig config = file.getModelerDiagramModel().getPaletteConfig();
        if (paletteConfigs.get(config) == null) {
            AbstractNode paletteRoot = new AbstractNode(new CategoryChildren(file));
            paletteRoot.setName(file.getName());
            PaletteController controller = PaletteFactory.createPalette(paletteRoot, new ModelerPaletteActions(), null, new ModelerPaletteItemDragAndDropHandler());
            paletteConfigs.put(config, controller);
        }
        return paletteConfigs.get(config);
    }

    private static class ModelerPaletteActions extends PaletteActions {

        @Override
        public Action[] getImportActions() {
            return null;
        }

        @Override
        public Action[] getCustomPaletteActions() {
            return null;
        }

        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action getPreferredAction(Lookup lookup) {
            return null;
        }

    }

    private static class ModelerPaletteItemDragAndDropHandler extends DragAndDropHandler {

        @Override
        public void customize(ExTransferable exTransferable, Lookup lookup) {
            final Node node = lookup.lookup(Node.class);
            exTransferable.put(new ExTransferable.Single(DataFlavor.imageFlavor) {
                @Override
                protected Object getData() throws IOException, UnsupportedFlavorException {
                    return node;
                }
            });
        }

    }

}
