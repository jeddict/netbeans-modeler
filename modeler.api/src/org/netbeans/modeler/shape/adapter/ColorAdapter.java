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
package org.netbeans.modeler.shape.adapter;

import java.awt.Color;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorAdapter extends XmlAdapter<String, Color> {

    @Override
    public Color unmarshal(String colorString) {
        Color color;
        String[] colors = colorString.substring(4, colorString.length() - 1).split(",");//rgb\s*\(\s*([0-9]+)\s*,\s*([0-9]+)\s*,\s*([0-9]+)\s*\)\s*
        color = new Color(
                Integer.parseInt(colors[0].trim()),
                Integer.parseInt(colors[1].trim()),
                Integer.parseInt(colors[2].trim()));
        return color;
    }

    @Override
    public String marshal(Color color) {
        String colorString = null;
        if (color != null) {
            colorString = "RGB(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
        }
        return colorString;
    }
}
