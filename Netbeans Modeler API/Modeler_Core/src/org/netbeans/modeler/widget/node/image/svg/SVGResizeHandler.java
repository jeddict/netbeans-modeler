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
package org.netbeans.modeler.widget.node.image.svg;

import org.w3c.dom.svg.SVGDocument;

public interface SVGResizeHandler {

    void resizingStarted(SVGDocument svgDocument, ResizeType resizeType, Double documentWidth, Double documentHeight, Double scaleX, Double scaleY);

    void resizing(SVGDocument svgDocument, ResizeType resizeType, Double documentWidth, Double documentHeight, Double scaleX, Double scaleY);

    void resizingFinished(SVGDocument svgDocument, ResizeType resizeType, Double documentWidth, Double documentHeight, Double scaleX, Double scaleY);
}
