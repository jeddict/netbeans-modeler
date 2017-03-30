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
package org.netbeans.modeler.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ModelerCore {

    private static Map<String, ModelerFile> modelerFiles = new HashMap<String, ModelerFile>();

    /**
     * @return the ModelerFiles
     */
    public static Map<String, ModelerFile> getModelerFiles() {
        return ModelerCore.modelerFiles;
    }

    /**
     * @return the ModelerFile
     */
    public static ModelerFile getModelerFile(String key) {
        return ModelerCore.modelerFiles.get(key);
    }

    /**
     * @param modelerFiles the modelerFiles to set
     */
    public static void setModelerFiles(Map<String, ModelerFile> modelerFiles) {
        ModelerCore.modelerFiles = modelerFiles;
    }

    public static void addModelerFile(String key, ModelerFile modelerFile) {
        ModelerCore.modelerFiles.put(key, modelerFile);
    }

    public static void removeModelerFile(String key) {
        ModelerCore.modelerFiles.remove(key);
    }
    
    public static void removeModelerFile(ModelerFile modelerFile) {
        ModelerCore.modelerFiles.remove(modelerFile.getPath());
    }

}
