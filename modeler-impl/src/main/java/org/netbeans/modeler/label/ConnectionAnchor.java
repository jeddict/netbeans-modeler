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

import java.awt.Point;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.ConnectionWidget;

/**
 *
 *
 */
public class ConnectionAnchor extends Anchor {

    private double locationRatio = 0.5;

    public ConnectionAnchor(ConnectionWidget connection) {
        this(connection, 0.5);
    }

    public ConnectionAnchor(ConnectionWidget connection, double location) {
        super(connection);
        locationRatio = location;
    }

    /**
     * Returns a scene location of a related widget.
     *
     * @return the scene location; null if no related widget is assigned
     */
    @Override
    public Point getRelatedSceneLocation() {
        return getLocationPoint(locationRatio);
    }

    @Override
    public Anchor.Result compute(Entry entry) {

        Point center = getLocationPoint(locationRatio);
        return new Anchor.Result(center, Anchor.DIRECTION_ANY);
    }

    protected Point getLocationPoint(double location) {
        ConnectionWidget connectionWidget = (ConnectionWidget) getRelatedWidget();

        java.util.List<Point> controlPoints = connectionWidget.getControlPoints();
        boolean empty = controlPoints == null || controlPoints.size() <= 0;

        double totalDistance = 0.0;
        double[] distances = new double[empty ? 0 : controlPoints.size() - 1];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = totalDistance += GeomUtil.distanceSq(controlPoints.get(i), controlPoints.get(i + 1));
        }

        // For now I always want the center and the left and right sides of the
        // connectoin widget.
        double lineDistance = totalDistance * location;

        return getLinePointDistance(distances, (int) lineDistance, controlPoints);
    }

    private Point getLinePointDistance(double[] distances,
            int lineDistance,
            java.util.List<Point> controlPoints) {
        Point retVal = new Point();

        if (distances.length > 0) {
            int index = distances.length - 1;
            for (int i = 0; i < distances.length; i++) {
                if (lineDistance < distances[i]) {
                    index = i;
                    break;
                }
            }

            double segmentStartDistance = index > 0 ? distances[index - 1] : 0;
            double segmentLength = distances[index] - segmentStartDistance;
            double segmentDistance = lineDistance - segmentStartDistance;

            if (segmentLength == 0.0) {
                retVal = controlPoints.get(index);
            } else {
                Point p1 = controlPoints.get(index);
                Point p2 = controlPoints.get(index + 1);

                double segmentFactor = segmentDistance / segmentLength;

                retVal = new Point((int) (p1.x + (p2.x - p1.x) * segmentFactor),
                        (int) (p1.y + (p2.y - p1.y) * segmentFactor));
            }
        }

        return retVal;
    }
}
