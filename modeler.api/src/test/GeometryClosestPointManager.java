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
package test;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 *
 */
public class GeometryClosestPointManager {

    public static ClosestSegment getClosetsPoint(Shape s, Point2D.Double insidePoint) {

        ClosestSegment cs = new ClosestSegment();

        Area area = new Area();
        area.add(new Area(s));
        // Note: we're storing double[] and not Point2D.Double
        ArrayList<double[]> areaPoints = new ArrayList<double[]>();
        double[] coords = new double[6];

        for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {

            // Because the Area is composed of straight lines
            int type = pi.currentSegment(coords);
            // We record a double array of {segment type, x coord, y coord}
            double[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
        }

        double[] start = new double[3]; // To record where each polygon starts
        for (int i = 0; i < areaPoints.size(); i++) {
            // If we're not on the last point, return a line from this point to the next
            double[] currentElement = areaPoints.get(i);

            // We need a default value in case we've reached the end of the ArrayList
            double[] nextElement = {-1, -1, -1};
            if (i < areaPoints.size() - 1) {
                nextElement = areaPoints.get(i + 1);
            }

            // Make the lines
            if (currentElement[0] == PathIterator.SEG_MOVETO) {
                start = currentElement; // Record where the polygon started to close it later
            }

            if (nextElement[0] == PathIterator.SEG_LINETO) {
                cs.getAreaSegments().add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                nextElement[1], nextElement[2]
                        )
                );
            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
                cs.getAreaSegments().add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                start[1], start[2]
                        )
                );
            }
        }

        // Calculate the nearest point on the edge
        for (Line2D.Double line : cs.getAreaSegments()) {

            // From: http://stackoverflow.com/questions/6176227
            double u
                    = ((insidePoint.getX() - line.x1) * (line.x2 - line.x1) + (insidePoint.getY() - line.y1) * (line.y2 - line.y1))
                    / ((line.x2 - line.x1) * (line.x2 - line.x1) + (line.y2 - line.y1) * (line.y2 - line.y1));

            double xu = line.x1 + u * (line.x2 - line.x1);
            double yu = line.y1 + u * (line.y2 - line.y1);

            if (u < 0) {
                cs.getClosestPoint().setLocation(line.getP1());
            } else if (u > 1) {
                cs.getClosestPoint().setLocation(line.getP2());
            } else {
                cs.getClosestPoint().setLocation(xu, yu);
            }

            if (!Double.isNaN(cs.getClosestPoint().getX()) && !Double.isNaN(cs.getClosestPoint().getY())) {
                cs.getClosestPointList().add((Point2D.Double) cs.getClosestPoint().clone());

                if (cs.getBestPoint() == null) {
                    cs.setBestPoint(new Point2D.Double(-1, -1));
                    cs.getBestPoint().setLocation(cs.getClosestPoint());
                } else {
                    if (cs.getClosestPoint().distance(insidePoint) < cs.getBestPoint().distance(insidePoint)) {
                        cs.getBestPoint().setLocation(cs.getClosestPoint());
                    }
                }
            }

        }

        ////System.out.println("cscscs : " + cs);
        return cs;
    }

}
