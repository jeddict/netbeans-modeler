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
package org.netbeans.modeler.resource.toolbar;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

public class ImageUtil {

    public final static String EXT_PNG = ".png"; // NOI18N
    public final static String EXT_GIF = ".gif"; // NOI18N

    private ImageUtil() {
    }
    private static ImageUtil self = null;

    public static ImageUtil getInstance() {
        if (self == null) {
            self = new ImageUtil();
        }

        return self;
    }

    public FileInputStream getImageFileInputStream(String imageName) {
        try {
            java.net.URL url = getClass().getResource(imageName);

            if (url == null) {
                throw new ImageUtil.ImageFileNotFoundException(
                        "Image file \"" + imageName);
            }

            return new java.io.FileInputStream(url.getPath());
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    public File getImageFile(String imageName) {
        return InstalledFileLocator.getDefault().locate(imageName, "org.netbeans", false); // NOI18N
    }

    public Icon getIcon(Class clazz, String imageName) {
        URL url = clazz.getResource(imageName);
        if (url == null) {
            throw new ImageFileNotFoundException("Image file \"" + imageName); // NOI18N
        }

        return new ImageIcon(url);
    }

    public Icon getIcon(String imageName) {
        return getIcon(getClass(), imageName);
    }

    public Image getImage(Class clazz, String imageName) {
        return iconToImage(getIcon(clazz, imageName));
    }

    public Image getImage(String imageName) {
        return iconToImage(getIcon(imageName));
    }

    static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    public Icon getIcon(String imageName, boolean isExtSpecified) {
        if (isExtSpecified) {
            return getIcon(imageName);
        } else {
            Icon icon = getIcon(imageName + EXT_PNG);

            if (icon == null) {
                icon = getIcon(imageName + EXT_GIF);
            }

            return icon;
        }
    }

    class ImageFileNotFoundException extends RuntimeException {

        public ImageFileNotFoundException() {
            super();
        }

        public ImageFileNotFoundException(String message) {
            super(message);
        }

    }
}
