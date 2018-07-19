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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 *
 */
public class ClosestSegment {

    private ArrayList<Line2D.Double> areaSegments = new ArrayList<Line2D.Double>();
    private Point2D.Double closestPoint = new Point2D.Double(-1, -1);
    private Point2D.Double bestPoint = null;
    private ArrayList<Point2D.Double> closestPointList = new ArrayList<Point2D.Double>();

    @Override
    public String toString() {
        return "ClosestSegment{" + "areaSegments=" + areaSegments + ", closestPoint=" + closestPoint + ", bestPoint=" + bestPoint + ", closestPointList=" + closestPointList + '}';
    }

    /**
     * @return the areaSegments
     */
    public ArrayList<Line2D.Double> getAreaSegments() {
        return areaSegments;
    }

    /**
     * @param areaSegments the areaSegments to set
     */
    public void setAreaSegments(ArrayList<Line2D.Double> areaSegments) {
        this.areaSegments = areaSegments;
    }

    /**
     * @return the closestPoint
     */
    public Point2D.Double getClosestPoint() {
        return closestPoint;
    }

    /**
     * @param closestPoint the closestPoint to set
     */
    public void setClosestPoint(Point2D.Double closestPoint) {
        this.closestPoint = closestPoint;
    }

    /**
     * @return the bestPoint
     */
    public Point2D.Double getBestPoint() {
        return bestPoint;
    }

    /**
     * @param bestPoint the bestPoint to set
     */
    public void setBestPoint(Point2D.Double bestPoint) {
        this.bestPoint = bestPoint;
    }

    /**
     * @return the closestPointList
     */
    public ArrayList<Point2D.Double> getClosestPointList() {
        return closestPointList;
    }

    /**
     * @param closestPointList the closestPointList to set
     */
    public void setClosestPointList(ArrayList<Point2D.Double> closestPointList) {
        this.closestPointList = closestPointList;
    }
}
