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
package org.netbeans.modeler.label;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.netbeans.api.visual.widget.ResourceTable;
import org.netbeans.api.visual.widget.Widget;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 *
 */
public class ResourceValue {

    public static String FGCOLOR = "fgcolor";
    public static String BGCOLOR = "bgcolor";
    public static String FONT = "font";
    private Font font;
    private Color fgcolor;
    private Color bgcolor;
    private boolean dirty = false;
    public static Preferences prefs;
    public static ResourceTable systemTable = new ResourceTable();

    static final Logger err = Logger.getLogger(ResourceValue.class.getName());

    static {
        prefs = NbPreferences.forModule(ResourceValue.class);
        try {
            if (prefs.keys().length == 0) {
                load();
            } else {
                loadResourceTable(prefs, systemTable);
            }
        } catch (Exception e) {
            err.log(Level.SEVERE, "error loading drawing area preferences");
        }
    }

    public ResourceValue(Font font, Color fgcolor, Color bgcolor) {

        this.font = font;
        this.fgcolor = fgcolor;
        this.bgcolor = bgcolor;

    }

    public void save(String path) {
        save(path, systemTable);
    }

    public void save(String path, ResourceTable table) {
        if (!dirty) {
            return;
        }
        String prefix = path + ".";

        table.addProperty(prefix + FGCOLOR, fgcolor);
        table.addProperty(prefix + BGCOLOR, bgcolor);
        table.addProperty(prefix + FONT, font);

        setDirty(false);
    }

    public static String fontToString(Font f) {
        if (f == null) {
            return "";
        }
        String strStyle;

        if (f.isBold()) {
            strStyle = f.isItalic() ? "bolditalic" : "bold";
        } else {
            strStyle = f.isItalic() ? "italic" : "plain";
        }
        return f.getName() + "-" + strStyle + "-" + f.getSize();
    }

    public void setFGColor(Color fg) {
        fgcolor = fg;
        setDirty(true);
    }

    public void setBGColor(Color bg) {
        bgcolor = bg;
        setDirty(true);
    }

    public void setFont(Font f) {
        font = f;
        setDirty(true);
    }

    public Font getFont() {
        return font;
    }

    public Color getBGColor() {
        return bgcolor;
    }

    public Color getFGColor() {
        return fgcolor;
    }

    public void setDirty(boolean value) {
        dirty = value;
    }

    public static ResourceValue getResources(String path, ResourceTable table) {
        path = path + ".";

        Color bgcolor = null;
        Color fgcolor = null;
        Font font = null;

        Object bgValue = table.getProperty(path + BGCOLOR);
        if (bgValue instanceof Color) {
            bgcolor = (Color) bgValue;
        }

        Object fgValue = table.getProperty(path + FGCOLOR);
        if (fgValue instanceof Color) {
            fgcolor = (Color) fgValue;
        }

        Object fontValue = table.getProperty(path + FONT);
        if (fontValue instanceof Font) {
            font = (Font) fontValue;
        }

        return new ResourceValue(font, fgcolor, bgcolor);
    }

    public static void initResources(String path, Widget widget) {
        path = path + ".";

        widget.setForegroundFromResource(path + FGCOLOR);
        widget.setBackgroundFromResource(path + BGCOLOR);
        widget.setFontFromResource(path + FONT);

    }

    public static void load() {
        load(getSystemResourceTable());
    }

    public static void load(ResourceTable table) {
        try {
            prefs.clear();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
        String path = "Options/data";

        FileObject fo = FileUtil.getConfigFile(path);
        if (fo != null) {
            try {
                Properties p = new Properties();
                p.load(fo.getInputStream());
                Set<Object> keys = p.keySet();
                for (Object key : keys) {

                    prefs.put(key.toString(), p.get(key).toString());
                }

                try {
                    prefs.flush();
                } catch (BackingStoreException ex) {
                    Exceptions.printStackTrace(ex);
                }

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        loadResourceTable(prefs, table);
    }

    public static void save() {
        try {
            prefs.clear();

            for (String name : systemTable.getLocalPropertyNames()) {
                Object c = systemTable.getProperty(name);

                String value = c.toString();
                if (c instanceof Color) {
                    value = String.valueOf(((Color) c).getRGB());
                } else if (c instanceof Font) {
                    value = fontToString((Font) c);
                }

                prefs.put(name, value);
            }

            prefs.flush();
            prefs.sync();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static ResourceTable createChildResourceTable() {
        return new ResourceTable(systemTable);
    }

    public static ResourceTable getSystemResourceTable() {
        return systemTable;
    }

    protected static void loadResourceTable(Preferences props, ResourceTable table) {
        try {
            table.clear();

            for (String key : props.keys()) {
                String value = props.get(key, null);

                if (key.endsWith(BGCOLOR) == true) {
                    table.addProperty(key, Color.decode(value));
                } else if (key.endsWith(FGCOLOR) == true) {
                    table.addProperty(key, Color.decode(value));
                } else if (key.endsWith(FONT) == true) {
                    if (value.startsWith("inherited") == true) {
                        table.addProperty(key, value);
                    } else {
                        table.addProperty(key, Font.decode(value));
                    }
                }
            }
        } catch (BackingStoreException e) {
            err.log(Level.SEVERE, "error loading drawing area preferences");
        }
    }
}
