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
package org.netbeans.modeler.tool.writer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.core.scene.ModelerScene;
import org.netbeans.modeler.specification.export.ExportType;
import org.netbeans.modeler.specification.model.document.IModelerScene;

public class DiagramImageWriter {

    public static final int CUSTOM_SIZE = 0;
    public static final int FIT_IN_WINDOW = 1;
    public static final int CURRENT_ZOOM_LEVEL = 2;
    public static final int ACTUAL_SIZE = 3;

    public static void write(IModelerScene scene,
            ExportType format,
            ImageOutputStream fo,
            boolean visibleAreaOnly,
            int zoomType,
            boolean selectedOnly,
            int quality,
            int width,
            int height) {
        // unselect widgest so that the exported image does not have resize border
        HashSet selected = new HashSet();
        selected.addAll(scene.getSelectedObjects());
        scene.userSelectionSuggested(Collections.emptySet(), false);

        double scale = scene.getZoomFactor();

        Rectangle sceneRec = scene.getClientArea();
        Rectangle viewRect = scene.getView().getVisibleRect();

        BufferedImage bufferedImage;
        Graphics2D g;

        int imageWidth = sceneRec.width;
        int imageHeight = sceneRec.height;

        switch (zoomType) {
            case CUSTOM_SIZE:
                imageWidth = width;
                imageHeight = height;
                scale = Math.min((double) width / (double) sceneRec.width,
                        (double) height / (double) sceneRec.height);
                break;
            case FIT_IN_WINDOW:
                scale = Math.min((double) viewRect.width / (double) sceneRec.width,
                        (double) viewRect.height / (double) sceneRec.height);
                imageWidth = (int) ((double) sceneRec.width * scale);
                imageHeight = (int) ((double) sceneRec.height * scale);
                break;
            case CURRENT_ZOOM_LEVEL:
                imageWidth = (int) (sceneRec.width * scene.getZoomFactor());
                imageHeight = (int) (sceneRec.height * scene.getZoomFactor());
                break;
            case ACTUAL_SIZE:
                imageWidth = sceneRec.width;
                imageHeight = sceneRec.height;
                scale = 1.0;
                break;
        }

        bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        g = bufferedImage.createGraphics();
        g.setPaint(scene.getBackground());
        g.fillRect(0, 0, imageWidth, imageHeight);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.scale(scale, scale);

        if (selectedOnly) {
            Area area = new Area();
            for (Object o : selected) {
                Widget w = scene.findWidget(o);
                if (w != null) {
                    Rectangle rec = w.convertLocalToScene(w.getClientArea());
                    rec.translate(-sceneRec.x, -sceneRec.y);
                    area.add(new Area(rec));
                }
            }
            g.clip(area);
            scene.paint(g);
            if (visibleAreaOnly) {
                bufferedImage = bufferedImage.getSubimage(viewRect.x, viewRect.y,
                        viewRect.width, viewRect.height);
            } else {
                if (area.getBounds().width > 0 && area.getBounds().height > 0) {
                    bufferedImage = bufferedImage.getSubimage((int) (area.getBounds().x * scale), (int) (area.getBounds().y * scale),
                            (int) (area.getBounds().width * scale), (int) (area.getBounds().height * scale));
                }
            }
        } else {
            scene.paint(g);

            if (visibleAreaOnly && imageWidth >= viewRect.width && imageHeight >= viewRect.height) {
                bufferedImage = bufferedImage.getSubimage(viewRect.x, viewRect.y,
                        viewRect.width, viewRect.height);
            }
        }

        // now restore the selected objects 
        scene.userSelectionSuggested(selected, false);

        try {
            if (ExportType.jpg == format) {
                Iterator iter = ImageIO.getImageWritersByFormatName(format.getName());
                ImageWriter writer = (ImageWriter) iter.next();

                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality((float) quality / (float) 100);
                writer.setOutput(fo);
                IIOImage image = new IIOImage(bufferedImage, null, null);
                writer.write(null, image, iwp);

                writer.dispose();
            } else {
                ImageIO.write(bufferedImage, format.getName(), fo);
            }
        } catch (IOException e) {
            Logger.getLogger("DiagramImageWriter").log(Level.SEVERE, null, e); //NOI18N
        } finally {
            try {
                fo.flush();
                fo.close();
            } catch (IOException e) {
                Logger.getLogger("DiagramImageWriter").log(Level.SEVERE, null, e); //NOI18N
            }
        }
    }

    public static void write(ModelerScene scene,
            ImageOutputStream fo,
            double scale) {
        int width = (int) (scene.getClientArea().width * scale);
        int height = (int) (scene.getClientArea().height * scale);
        write(scene, ExportType.png, fo, false, CUSTOM_SIZE, false, 100, width, height);
    }

}
