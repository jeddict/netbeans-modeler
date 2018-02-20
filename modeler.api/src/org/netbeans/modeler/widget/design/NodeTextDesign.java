/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.modeler.widget.design;

import java.awt.Color;
import java.awt.Font;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author jGauravGupta
 */
@XmlJavaTypeAdapter(value = NodeTextDesignValidator.class)
public class NodeTextDesign extends TextDesign {

    private static final int NODE_DEFAULT_STYLE = Font.BOLD;

    public NodeTextDesign() {
        style = NODE_DEFAULT_STYLE;
    }

    public NodeTextDesign(int style, float size) {
        super(style, size);
    }

    public NodeTextDesign(int style, float size, Color color) {
        super(style, size, color);
    }

    @Override
    public boolean isChanged() {
        return style != NODE_DEFAULT_STYLE
                || size != DEFAULT_SIZE
                || color != null;
    }

    @Override
    public void reset() {
        style = NODE_DEFAULT_STYLE;
        size = DEFAULT_SIZE;
        color = null;
    }
    
    @Override
    public int getDefaultStyle() {
        return NODE_DEFAULT_STYLE;
    }

}
