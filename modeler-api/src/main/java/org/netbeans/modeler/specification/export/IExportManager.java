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
package org.netbeans.modeler.specification.export;

import java.io.File;
import java.util.List;
import org.netbeans.modeler.specification.model.document.IModelerScene;

public interface IExportManager<S extends IModelerScene> {

    List<FileType> getExportType();

    void export(S scene, FileType format, File file);

    class FileType {

        private String extension;
        private String name;

        public FileType(String extension, String name) {
            this.extension = extension;
            this.name = name;
        }

        public FileType() {
        }

        /**
         * @return the extension
         */
        public String getExtension() {
            return extension;
        }

        /**
         * @param extension the extension to set
         */
        public void setExtension(String extension) {
            this.extension = extension;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }
}
