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
package org.netbeans.modeler.tool.writer;

import java.io.File;
import org.netbeans.modeler.specification.export.IExportManager;
import org.netbeans.modeler.specification.export.IExportManager.FileType;
import org.netbeans.modeler.specification.model.document.IModelerScene;

public class DocumentWriter {

    public static void write(IModelerScene scene,
            FileType format,
            File file) {
        IExportManager exportManager = scene.getModelerFile().getVendorSpecification().getModelerDiagramModel().getExportManager();
        exportManager.export(scene, format, file);

    }

}
