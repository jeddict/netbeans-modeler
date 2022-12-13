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
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author jGauravGupta
 */
class ColorAdapter extends XmlAdapter<String, Color> {

    @Override
    public Color unmarshal(String s) {
        return Color.decode(s);
    }

    @Override
    public String marshal(Color c) {
        String rgb = Integer.toHexString(c.getRGB());
        return "#" + rgb.substring(2, rgb.length());
    }
}
