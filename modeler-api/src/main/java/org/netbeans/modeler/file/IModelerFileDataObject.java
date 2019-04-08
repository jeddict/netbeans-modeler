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
package org.netbeans.modeler.file;

import java.awt.Image;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;

public interface IModelerFileDataObject {

    FileObject getPrimaryFile();

    Image getIcon();

    void addSaveCookie(SaveCookie saveCookie);

    void removeSaveCookie();

    void setDirty(boolean modified, SaveCookie saveCookie);

    <T extends Node.Cookie> T getCookie(Class<T> type);
}
