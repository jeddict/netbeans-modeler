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
package org.netbeans.modeler.widget.context.action;

import java.awt.Point;
import java.util.ArrayList;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * The extended connect provider adds the ability to create connection between
 * specified points of widgets
 *
 *
 */
public interface ExConnectWithLocationProvider extends ExConnectProvider {

    /**
     * check if conection is possible between specified points of widgets
     *
     * @param sourceWidget
     * @param targetWidget
     * @param sourcePoint
     * @param targetPoint
     * @return
     */
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget, Point sourcePoint, Point targetPoint);

    /**
     * handle if it possible to start link from point on source widget
     *
     * @param sourceWidget
     * @param sourcePoint
     * @return
     */
    public boolean isSourceWidget(Widget sourceWidget, Point sourcePoint);

    /**
     *
     * @param scene
     * @param sourcePoint
     * @return
     */
    public Widget createTargetWidget(Scene scene, Point sourcePoint);

    /**
     * Create connection between specified points of source and target widgets
     *
     * @param sourceWidget
     * @param targetWidget
     * @param startingPoint
     * @param finishPoint
     * @return some feedback based on usage
     */
    public ArrayList<ConnectionWidget> createConnection(Widget sourceWidget, Widget targetWidget, Point startingPoint, Point finishPoint);
}
