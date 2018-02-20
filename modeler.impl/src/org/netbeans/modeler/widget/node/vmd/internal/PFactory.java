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
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.visual.border.Border;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.openide.util.Exceptions;

/**
 * Used as a factory class for objects defined in P visualization style.
 *
 * @author Gaurav Gupta
 */
public final class PFactory {

    private static final Map<Class<? extends IColorScheme>, IColorScheme> SCHEME = new HashMap<>();

    public static IColorScheme getColorScheme(Class<? extends IColorScheme> colorSchemeClass) {
        IColorScheme scheme = SCHEME.get(colorSchemeClass);
        if (scheme == null) {
            try {
                scheme = (IColorScheme) colorSchemeClass.getMethod("getInstance").invoke(null);
                SCHEME.put(colorSchemeClass, scheme);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return scheme;
    }

    private PFactory() {
    }

    public static Class<? extends IColorScheme> getOriginalScheme() {
        return POriginalColorScheme.class;
    }

    public static Class<? extends IColorScheme> getNetBeans60Scheme() {
        return PNBColorScheme.class;
    }

    public static Class<? extends IColorScheme> getMetroScheme() {
        return PMetroColorScheme.class;
    }

    public static Class<? extends IColorScheme> getMacScheme() {
        return PMacColorScheme.class;
    }
    
    public static Class<? extends IColorScheme> getDarkScheme() {
        return PDarkColorScheme.class;
    }
    
    public static Class<? extends IColorScheme> getLightScheme() {
        return PLightColorScheme.class;
    }

    public static Class<? extends IColorScheme> getWoodScheme() {
        return PWoodColorScheme.class;
    }

    /**
     * Creates a border used by P node with a specific colors.
     *
     * @return the P node border
     * @param borderColor the border color
     * @param borderThickness the border thickness
     * @param color1 1. color of gradient background
     * @param color2 2. color of gradient background
     * @param color3 3. color of gradient background
     * @param color4 4. color of gradient background
     * @param color5 5. color of gradient background
     * @since 2.5
     */
    public static Border createPNodeBorder(Color borderColor, int borderThickness, Color color1, Color color2, Color color3, Color color4, Color color5) {
        return new org.netbeans.modeler.border.PNodeBorder(borderColor, borderThickness, color1, color2, color3, color4, color5);
    }

}
