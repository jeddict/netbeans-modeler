/**
 * Copyright 2013-2019 Gaurav Gupta
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
package org.netbeans.modeler.label.multiline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.modeler.border.ResizeBorderbk;

public class MultilineLabelWidget extends LabelWidget {

    public static final Color BORDER_HILIGHTED_COLOR = new Color(0xFFA400);
    public static ResizeBorderbk NON_RESIZABLE_BORDER = new ResizeBorderbk(5, Color.BLACK, new ResizeProvider.ControlPoint[]{});

    public MultilineLabelWidget(Scene scene) {
        this(scene, null);
    }

    public MultilineLabelWidget(Scene scene, String label) {
        super(scene, label);
    }

    /**
     * Calculates a client area for the label.
     *
     * @return the client area
     */
    @Override
    protected Rectangle calculateClientArea() {
        if (getLabel() == null) {
            return super.calculateClientArea();
        }

        Rectangle rectangle;

        Graphics2D gr = getGraphics();
        FontMetrics fontMetrics = gr.getFontMetrics(getFont());

        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;

        String[] lines = getLabel().split("\n");
        for (int index = 0; index < lines.length; index++) {
            String line = lines[index];
            Rectangle2D stringBounds = fontMetrics.getStringBounds(line, gr);

            if (index == 0) {
                x = stringBounds.getX();
                y = stringBounds.getY();
                width = stringBounds.getWidth();
            } else {
                if (stringBounds.getX() < x) {
                    x = stringBounds.getX();
                }

                if (stringBounds.getY() < y) {
                    y = stringBounds.getY();
                }

                if (stringBounds.getWidth() > width) {
                    width = stringBounds.getWidth();
                }
            }

            height += stringBounds.getHeight();
        }
        rectangle = roundRectangle(new Rectangle2D.Double(x, y, width, height));

        switch (getOrientation()) {
            case NORMAL:
                return rectangle;
            case ROTATE_90:
                return new Rectangle(rectangle.y, -rectangle.x - rectangle.width, rectangle.height, rectangle.width);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    protected void paintWidget() {
        Graphics2D g = getGraphics();

        g.setFont(getFont());
        FontMetrics fontMetrics = g.getFontMetrics(getFont());

        String txt = getLabel();
        if (txt != null && txt.length() > 0) {
            String[] lines0 = getLabel().split("\n");
            ArrayList<LineDetails> lineDeltails = new ArrayList<LineDetails>();
            for (String lineTxt : lines0) {
                if (lineTxt.length() == 0) {
                    lineTxt = " ";//draw one space if empty line
                }
                LineDetails lines = breakupLinesInNoBreakLinedTxt(lineTxt, g, fontMetrics);
                lineDeltails.add(lines);
            }
            LineDetails lines = new LineDetails();
            if (lineDeltails.size() > 0) {
                for (LineDetails lineDeltail : lineDeltails) {
                    for (int j = 0; j < lineDeltail.getNumberOfLines(); j++) {
                        lines.addLine(lineDeltail.getLine(j), lineDeltail.getHeight(j));
                    }
                }
                // save the current color
                Color currentColor = g.getColor();
                g.setColor(this.getForeground());

                int x;
                int y = (getSize().height - lines.getNumberOfLines() * fontMetrics.getHeight()) / 2;

                for (int index = 0; index < lines.getNumberOfLines(); index++) {
                    String line = lines.getLine(index);
                    if (line == null) {
                        line = new String();
                    }
                    switch (getAlignment()) {
                        case LEFT:
                            x = 0;
                            break;
                        case RIGHT:
                            x = getSize().width - fontMetrics.stringWidth(line);
                            break;
                        case CENTER:
                        default:
                            x = (getSize().width - fontMetrics.stringWidth(line)) / 2;
                    }
                    g.drawString(line, x, y);

                    y += lines.getHeight(index);
                }

                // reset to the original color
                if (!g.getColor().equals(currentColor)) {
                    g.setColor(currentColor);
                }
            }
        }
    }

    protected LineDetails breakupLinesInNoBreakLinedTxt(String text, Graphics2D g, FontMetrics metrics) {
        LineDetails retVal = null;
        if (text == null || text.length() == 0) {
            return retVal;
        }

        retVal = new LineDetails();

        int width = getClientArea().width;
        StringBuilder label = new StringBuilder(text);

        int index = 0;
        int startLine = 0;
        int previousEnd = 0;
        String previousLine = null;
        double previousHeight = 0;

        while (index >= 0) {

            index = findEndOfNextWord(label, index);
            if (index == -1) {
                retVal.addLine(previousLine, previousHeight);
                break;
            }

            String line = label.substring(startLine, index);
            Rectangle2D strBounds = metrics.getStringBounds(line, g);

            if (strBounds.getWidth() <= width) {
                previousLine = line;
                previousHeight = strBounds.getHeight();
                previousEnd = index;
                index++;
            } else // the width of the line is longer than the width of the Widget's client area.
            {
                if (previousLine != null) {
                    retVal.addLine(previousLine, previousHeight);
                    previousLine = null;  //Already added; hence reset
                } else {
                    String subStr = null;
                    //int len = label.length();
                    int startIndx = startLine;
                    int endIndx = index;
                    while (startIndx < endIndx) {
                        subStr = label.substring(startIndx, endIndx);
                        strBounds = metrics.getStringBounds(subStr, g);
                        if (strBounds.getWidth() < width) {
                            retVal.addLine(subStr, strBounds.getHeight());
                            // reset the subString' s start and end indices
                            startIndx = endIndx;
                            endIndx = index + 1;
                        }
                        endIndx--;
                    }
                    previousEnd = index;
                }

                // Find the next non-whitespace, start from the previous end
                // point, since that was the end of the line.
                index = findFirstNonWhitespace(label, previousEnd);
                startLine = index;
            }
        }

        return retVal;
    }

    /**
     *
     * @param label
     * @param start
     * @return
     */
    protected int findFirstNonWhitespace(StringBuilder label, int start) {
        int retVal = -1;
        for (int index = start; index < label.length(); index++) {
            int codePointAt = label.codePointAt(index);
            if (!Character.isWhitespace(codePointAt)) {
                retVal = index;
                break;
            } else if (codePointAt == '\n') {
                retVal = index;
                break;
            }
        }

        return retVal;
    }

    /**
     * find end of word using whitespace separation(space, linebreak etc)
     * consist from one whitespace symbol at least.
     *
     * @param label
     * @param start
     * @return
     */
    protected int findEndOfNextWord(StringBuilder label, int start) {
        int retVal = -1;

        if ((start >= 0) && (start < label.length())) {
            retVal = label.length();
            for (int index = start + 1; index < label.length(); index++) {
                int codePoint = label.codePointAt(index);
                if (Character.isWhitespace(codePoint))//currently split only on whitespace, may be consider to split on any not letter-digit
                {
                    retVal = index;
                    break;
                }
            }
        }

        return retVal;
    }

    /**
     * Rounds Rectangle2D to Rectangle.
     *
     * @param rectangle the rectangle2D
     * @return the rectangle
     */
    public Rectangle roundRectangle(Rectangle2D rectangle) {
        return rectangle.getBounds();
    }

    protected Dimension getSize() {
        Insets labelInsets = getBorder().getInsets();
        int width = getBounds().width - labelInsets.left - labelInsets.right;
        int height = getBounds().height - labelInsets.top - labelInsets.bottom;

        return new Dimension(width, height);
    }

    protected class LineDetails {

        private ArrayList< String> lines = new ArrayList< String>();
        private ArrayList< Float> heights = new ArrayList< Float>();

        public final void addLine(String line, double height) {
            lines.add(line);
            heights.add((float) height);
        }

        public final String getLine(int index) {
            return lines.get(index);
        }

        public final float getHeight(int index) {
            return heights.get(index);
        }

        public int getNumberOfLines() {
            return lines.size();
        }
    }
}
