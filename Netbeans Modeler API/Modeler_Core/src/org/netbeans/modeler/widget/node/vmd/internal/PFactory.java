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
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.Color;
import org.netbeans.api.visual.border.Border;
import org.netbeans.modeler.specification.model.document.IColorScheme;

/**
 * Used as a factory class for objects defined in P visualization style.
 *
 * @author Gaurav Gupta
 */
public final class PFactory {

    private static final IColorScheme SCHEME_ORIGINAL = new POriginalColorScheme();
    private static final IColorScheme SCHEME_NB60 = new PNBColorScheme();
    private static final IColorScheme SCHEME_METRO = new PMetroColorScheme();
    private static final IColorScheme SCHEME_MAC = new PMacColorScheme();
    private static final IColorScheme SCHEME_WOOD = new PWoodColorScheme();

    private PFactory() {
    }

    /**
     * Creates the original vmd color scheme. Used by default.
     *
     * @return the color scheme
     * @since 2.5
     */
    public static IColorScheme getOriginalScheme() {
        return SCHEME_ORIGINAL;
    }

    /**
     * Creates the NetBeans 6.0 vmd color scheme.
     *
     * @return the color scheme
     * @since 2.5
     */
    public static IColorScheme getNetBeans60Scheme() {
        return SCHEME_NB60;
    }

    public static IColorScheme getMetroScheme() {
        return SCHEME_METRO;
    }

    public static IColorScheme getMacScheme() {
        return SCHEME_MAC;
    }

    public static IColorScheme getWoodScheme() {
        return SCHEME_WOOD;
    }

    /**
     * Creates a border used by P node.
     *
     * @return the P node border
     */
    public static Border createPNodeBorder() {
        return POriginalColorScheme.BORDER_NODE;
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
