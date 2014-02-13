/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.Color;
import org.netbeans.api.visual.border.Border;

/**
 * Used as a factory class for objects defined in P visualization style.
 *
 * @author David Kaspar
 */
public final class PFactory {

    private static PColorScheme SCHEME_ORIGINAL = new POriginalColorScheme();
    private static PColorScheme SCHEME_NB60 = new PNBColorScheme();

    private PFactory() {
    }

    /**
     * Creates the original vmd color scheme. Used by default.
     *
     * @return the color scheme
     * @since 2.5
     */
    public static PColorScheme getOriginalScheme() {
        return SCHEME_ORIGINAL;
    }

    /**
     * Creates the NetBeans 6.0 vmd color scheme.
     *
     * @return the color scheme
     * @since 2.5
     */
    public static PColorScheme getNetBeans60Scheme() {
        return SCHEME_NB60;
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
        return new PNodeBorder(borderColor, borderThickness, color1, color2, color3, color4, color5);
    }

}
