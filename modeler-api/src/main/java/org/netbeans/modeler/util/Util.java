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
package org.netbeans.modeler.util;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.netbeans.modeler.svg.SvgImage;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.xml.sax.SAXException;

public class Util<E> {

    private static volatile Object currentLoader;
    private static Lookup.Result<ClassLoader> loaderQuery = null;
    private static boolean noLoaderWarned = false;
    private static final Logger ERR = Logger.getLogger(ImageUtilities.class.getName());

    private static ClassLoader getLoader() {
        Object is = currentLoader;
        if (is instanceof ClassLoader) {
            return (ClassLoader) is;
        }

        currentLoader = Thread.currentThread();

        if (loaderQuery == null) {
            loaderQuery = Lookup.getDefault().lookup(new Lookup.Template<ClassLoader>(ClassLoader.class));
            loaderQuery.addLookupListener(
                    new LookupListener() {
                @Override
                public void resultChanged(LookupEvent ev) {
                    ERR.fine("Loader cleared"); // NOI18N
                    currentLoader = null;
                }
            });
        }

        Iterator it = loaderQuery.allInstances().iterator();
        if (it.hasNext()) {
            ClassLoader toReturn = (ClassLoader) it.next();
            if (currentLoader == Thread.currentThread()) {
                currentLoader = toReturn;
            }
            ERR.fine("Loader computed: " + currentLoader); // NOI18N
            return toReturn;
        } else {
            if (!noLoaderWarned) {
                noLoaderWarned = true;
                ERR.warning(
                        "No ClassLoader instance found in " + Lookup.getDefault() // NOI18N
                );
            }
            return null;
        }
    }

    public static SvgImage loadSvgImage(String resource) {
        SvgImage svgImage = null;
        try {
            svgImage = Lookup.getDefault().lookup(SvgImage.class);
            svgImage.loadStream(loadResource(resource));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return svgImage;
    }

    public static InputStream loadResource(String resource) {
        InputStream inputStream = null;
        try {
            String n;
            if (resource.startsWith("/")) { // NOI18N
                n = resource.substring(1);
            } else {
                n = resource;
            }

            java.net.URL url = Util.getLoader().getResource(n);
            inputStream = url.openStream();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return inputStream;
    }

    public static Image loadImage(String resource) {
        return ImageUtilities.loadImage(resource);
    }

    /**
     * Returns a copy of the object, or null if the object cannot be serialized.
     * @param original Original object to be cloned
     * @return The new cloned Object
     */
    public static <E> E cloneObject(E original) {
        E clone = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {

            // Write the object out to a byte array
            out.writeObject(original);
            out.flush();

            // Make an input stream from the byte array and read a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            clone = (E)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
