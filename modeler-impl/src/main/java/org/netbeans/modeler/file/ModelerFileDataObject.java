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
package org.netbeans.modeler.file;

import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;

public abstract class ModelerFileDataObject extends MultiDataObject implements IModelerFileDataObject {

    public ModelerFileDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    public void addSaveCookie(SaveCookie saveDiagram) {

        setModified(true);
        Node.Cookie saveCookie = getCookie(SaveCookie.class);
        if (saveCookie == null) {
            CookieSet cookies = getCookieSet();
            cookies.add(saveDiagram);
        }
    }

    @Override
    public void removeSaveCookie() {
        setModified(false);
        Node.Cookie saveCookie = getCookie(SaveCookie.class);
        if (saveCookie != null) {
            CookieSet cookies = getCookieSet();
            cookies.remove(saveCookie);
        }
    }

    @Override
    public void setDirty(boolean modified, SaveCookie saveDiagram) {
        if (isModified() == modified) {
            return;
        }

        if (modified) {
            addSaveCookie(saveDiagram);
        } else {
            removeSaveCookie();
        }
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        return super.getCookie(type);
    }
}
