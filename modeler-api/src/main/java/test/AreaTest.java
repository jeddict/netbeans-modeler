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
package test;

/**
 *
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;

public class AreaTest extends JFrame {

    private static final long serialVersionUID = -2221432546854106311L;

    Area area = new Area();
    ArrayList<Line2D.Double> areaSegments = new ArrayList<Line2D.Double>();
    Point2D.Double insidePoint = new Point2D.Double();
    Point2D.Double closestPoint = new Point2D.Double(-1, -1);
    Point2D.Double bestPoint = new Point2D.Double(-1, -1);
    ArrayList<Point2D.Double> closestPointList = new ArrayList<Point2D.Double>();

    AreaTest() {
        Path2D.Double triangle = new Path2D.Double();

        triangle.moveTo(50, 50);
        triangle.lineTo(300, 400);
        triangle.lineTo(10, 200);
        triangle.closePath();
        area.add(new Area(triangle));
        triangle.reset();

        insidePoint.setLocation(350, 150);

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
                areaSegments.add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                nextElement[1], nextElement[2]
                        )
                );
            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
                areaSegments.add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                start[1], start[2]
                        )
                );
            }
        }

        // Calculate the nearest point on the edge
        for (Line2D.Double line : areaSegments) {

            // From: http://stackoverflow.com/questions/6176227
            double u
                    = ((insidePoint.getX() - line.x1) * (line.x2 - line.x1) + (insidePoint.getY() - line.y1) * (line.y2 - line.y1))
                    / ((line.x2 - line.x1) * (line.x2 - line.x1) + (line.y2 - line.y1) * (line.y2 - line.y1));

            double xu = line.x1 + u * (line.x2 - line.x1);
            double yu = line.y1 + u * (line.y2 - line.y1);

            if (u < 0) {
                closestPoint.setLocation(line.getP1());
            } else if (u > 1) {
                closestPoint.setLocation(line.getP2());
            } else {
                closestPoint.setLocation(xu, yu);
            }

            closestPointList.add((Point2D.Double) closestPoint.clone());

            if (closestPoint.distance(insidePoint) < bestPoint.distance(insidePoint)) {
                bestPoint.setLocation(closestPoint);
            }
        }

        setSize(new Dimension(500, 500));
        setLocationRelativeTo(null); // To center the JFrame on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        // Fill the area
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.lightGray);
        g2d.fill(area);

        // Draw the border line by line
        g.setColor(Color.black);
        for (Line2D.Double line : areaSegments) {
            g2d.draw(line);
        }

        // Draw the inside point
        g.setColor(Color.red);
        g2d.fill(
                new Ellipse2D.Double(
                        insidePoint.getX() - 3,
                        insidePoint.getY() - 3,
                        6,
                        6
                )
        );

        // Draw the other close points
        for (Point2D.Double point : closestPointList) {
            g.setColor(Color.black);
            g2d.fill(
                    new Ellipse2D.Double(
                            point.getX() - 3,
                            point.getY() - 3,
                            6,
                            6
                    )
            );
        }

        // Draw the outside point
        g.setColor(Color.green);
        g2d.fill(
                new Ellipse2D.Double(
                        bestPoint.getX() - 3,
                        bestPoint.getY() - 3,
                        6,
                        6
                )
        );
    }

    public static void main(String[] args) {
        new AreaTest();
    }
}
