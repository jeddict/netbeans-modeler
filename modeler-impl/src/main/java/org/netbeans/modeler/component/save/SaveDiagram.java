/**
 * Copyright 2013-2019 Gaurav Gupta
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
package org.netbeans.modeler.component.save;

import java.io.IOException;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.openide.cookies.SaveCookie;

public class SaveDiagram implements SaveCookie {

    private final ModelerFile file;

    public SaveDiagram(ModelerFile file) {
        this.file = file;
    }

    @Override
    public synchronized void save() throws IOException {
        NBModelerUtil.saveModelerFile(file);
        file.getModelerPanelTopComponent().changePersistenceState(true);
        file.getModelerFileDataObject().setDirty(false, SaveDiagram.this);
    }
}
