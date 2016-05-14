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
package org.netbeans.modeler.widget.node;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ResourceTable;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.Widget.Dependency;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.openide.util.Lookup;

public interface IWidget extends Accessible, Lookup.Provider {

    /**
     * Adds a child widget as the last one.
     *
     * @param child the child widget to be added
     */
    void addChild(Widget child);

    /**
     * Adds a child widget as the last one.
     *
     * @param child the child widget to be added
     * @param constraint the constraint assigned to the child widget
     */
    void addChild(Widget child, Object constraint);

    /**
     * Adds a child at a specified index
     *
     * @param index the index (the child is added before the one that is not the
     * index place)
     * @param child the child widget
     */
    void addChild(int index, Widget child);

    /**
     * Adds a child at a specified index
     *
     * @param index the index (the child is added before the one that is not the
     * index place)
     * @param child the child widget
     * @param constraint the constraint assigned to the child widget
     */
    void addChild(int index, Widget child, Object constraint);

    /**
     * Adds all children in a specified list.
     *
     * @param children the list of children widgets
     */
    void addChildren(List<? extends Widget> children);

    /**
     * Adds a dependency listener which is notified when the widget placement or
     * boundary is going to be changed or similar thing happens to its parent
     * widget.
     *
     * @param dependency the dependency listener
     */
    void addDependency(Widget.Dependency dependency);

    /**
     * Brings the widget to the back. Means: the widget becomes the first child
     * in the list of children of the parent widget.
     */
    void bringToBack();

    /**
     * Brings the widget to the front. Means: the widget becomes the last child
     * in the list of children of the parent widget.
     */
    void bringToFront();

    /**
     * Converts a location in the local coordination system to the scene
     * coordination system.
     *
     * @param localLocation the local location
     * @return the scene location
     */
    Point convertLocalToScene(Point localLocation);

    /**
     * Converts a rectangle in the local coordination system to the scene
     * coordination system.
     *
     * @param localRectangle the local rectangle
     * @return the scene rectangle
     */
    @NonNull
    Rectangle convertLocalToScene(@NonNull Rectangle localRectangle);

    /**
     * Converts a location in the scene coordination system to the local
     * coordination system.
     *
     * @param sceneLocation the scene location
     * @return the local location
     */
    Point convertSceneToLocal(Point sceneLocation);

    /**
     * Converts a rectangle in the scene coordination system to the local
     * coordination system.
     *
     * @param sceneRectangle the scene rectangle
     * @return the local rectangle
     */
    Rectangle convertSceneToLocal(Rectangle sceneRectangle);

    /**
     * Creates and returns an action chain for a specified tool.
     *
     * @param tool the tool
     * @return the action chain
     */
    WidgetAction.Chain createActions(String tool);

    /**
     * Returns whether a specified object is the same as the widget.
     *
     * @param object the object
     * @return true if the object reference is the same as the widget
     */
    @Override
    boolean equals(Object object);

    /**
     * Returns an accessible context of the widget.
     *
     * @return the accessible context
     */
    @Override
    AccessibleContext getAccessibleContext();

    /**
     * Returns a default action chain.
     *
     * @return the default action chain.
     */
    WidgetAction.Chain getActions();

    /**
     * Returns already created action chain for a specified tool.
     *
     * @param tool the tool
     * @return the action chain; null, if no chain for the tool exists
     */
    WidgetAction.Chain getActions(String tool);

    /**
     * Returns the widget background paint.
     *
     * @return the background paint
     */
    Paint getBackground();

    /**
     * Returns the border of the widget.
     *
     * @return the border
     */
    Border getBorder();

    /**
     * Returns the resolved bounds of the widget. The bounds are specified
     * relatively to the location of the widget.
     * <p>
     * The location is resolved/set by calling <code>resolveBounds</code> method
     * which should be called from <code>Layout</code> interface implementation
     * only. Therefore the corrent value is available only after the scene is
     * validated ( <code>SceneListener.sceneValidated</code> method). Before
     * validation a previous/obsolete or <code>null</code> value could be
     * returned. See <strong>Layout</strong>
     * section in documentation.
     *
     * @return the bounds in local coordination system
     */
    @CheckForNull
    Rectangle getBounds();

    /**
     * Returns constraint assigned to a specified child widget.
     *
     * @param child the child widget
     * @return the constraint
     */
    Object getChildConstraint(Widget child);

    /**
     * Returns a list of children widgets.
     *
     * @return the list of children widgets
     */
    List<Widget> getChildren();

    /**
     * Returns a client area of the widget.
     *
     * @return the client area
     */
    Rectangle getClientArea();

    /**
     * Returns a mouse cursor for the widget.
     *
     * @return the mouse cursor
     */
    Cursor getCursor();

    /**
     * Returns a collection of registered dependencies.
     *
     * @return the unmodifiable collection of dependencies
     * @since 2.6
     */
    Collection<Dependency> getDependencies();

    /**
     * Returns the font assigned to the widget. If not set yet, then it returns
     * the font of its parent widget.
     *
     * @return the font
     */
    Font getFont();

    /**
     * Returns the widget foreground color.
     *
     * @return the foreground color
     */
    Color getForeground();

    /**
     * Returns the layout of the widget.
     *
     * @return the layout
     */
    Layout getLayout();

    /**
     * Returns the resolved location of the widget. The location is specified
     * relatively to the location of the parent widget.
     * <p>
     * The location is resolved/set by calling <code>resolveBounds</code> method
     * which should be called from <code>Layout</code> interface implementation
     * only. Therefore the corrent value is available only after the scene is
     * validated ( <code>SceneListener.sceneValidated</code> method). Before
     * validation a previous/obsolete or <code>[0,0]</code> value could be
     * returned. See <strong>Layout</strong>
     * section in documentation.
     *
     * @return the location in the local coordination system of the parent
     * widget
     */
    Point getLocation();

    /**
     * Returns a lookup of the widget.
     *
     * @return the lookup
     */
    @Override
    Lookup getLookup();

    /**
     * Returns a maximum size of the widget.
     *
     * @return the maximum size; if null, then no maximum size are set.
     */
    Dimension getMaximumSize();

    /**
     * Returns a minimum size of the widget.
     *
     * @return the minimum size; if null, then no minumum size are set.
     */
    Dimension getMinimumSize();

    /**
     * Returns a parent widget.
     *
     * @return the parent widget
     */
    Widget getParentWidget();//Widget

    /**
     * Returns a preferred bounds relatively to the location of the widget. If
     * no preferred bounds are set, then it returns a preferred bounds that are
     * calculated from the calculateClientArea method of this widget and
     * location and bounds of the children widgets. This calculated bounds are
     * processed by the minimum and maximum bounds too.
     * <p>
     * This method can be called after child widgets are layed out which is
     * assured in method calls of the <code>Layout</code> interface
     * implementation. If preferred bounds are set (check it using
     * <code>isPreferredBoundsSet</code> method), you can call this method at
     * any time.
     *
     * @return the preferred bounds
     */
    Rectangle getPreferredBounds();

    /**
     * Returns a preferred location of the widget.
     *
     * @return the preferred location; if null, then no preferred location is
     * set
     */
    Point getPreferredLocation();

    /**
     * Returns a preferred size of the widget.
     *
     * @return the preferred size; if null, then no preferred size are set.
     */
    Dimension getPreferredSize();

    /**
     * Retreives the widgets resource table. If the widgets resource table is
     * not set then the widgets parent resource table it retrieved.
     *
     * @return The resource table.
     */
    ResourceTable getResourceTable();

    /**
     * Returns a scene where the widget is assigned
     *
     * @return the scene
     */
    Scene getScene();

    /**
     * Returns a state of the widget.
     *
     * @return the widget state
     */
    ObjectState getState();

    /**
     * Returns a tool-tip text of the widget.
     *
     * @return the tool-tip text
     */
    String getToolTipText();

    /**
     * Returns the object hash code.
     *
     * @return the object hash code
     */
    @Override
    int hashCode();

    /**
     * Returns whether clipping is used in the widget.
     *
     * @return true, if the check clipping is used
     */
    boolean isCheckClipping();

    /**
     * Returns whether the widget is enabled. If the widget is disabled then any
     * event is processed by assigned actions.
     *
     * @return true if the widget is enabled.
     */
    boolean isEnabled();

    /**
     * Called to whether a particular location in local coordination system is
     * controlled (otionally also painted) by the widget.
     *
     * @param localLocation the local location
     * @return true, if the location belong to the widget
     */
    boolean isHitAt(Point localLocation);

    /**
     * Returns whether the widget is opaque.
     *
     * @return true, if the widget is opaque
     */
    boolean isOpaque();

    /**
     * Returns whether a preferred bounds are set.
     *
     * @return true, if preferred bounds are set
     */
    boolean isPreferredBoundsSet();

    /**
     * Returns true if the widget is validated (is not scheduled to
     * revalidation).
     *
     * @return true, if is validated
     */
    boolean isValidated();

    /**
     * Returns whether the widget is visible.
     *
     * @return true if the widget is visible
     */
    boolean isVisible();

    /**
     * Paints the widget with its children widget into the Graphics2D instance
     * acquired from Scene.getGraphics method.
     */
    void paint();

    /**
     * Removes a child widget.
     *
     * @param child the child widget
     */
    void removeChild(Widget child);

    /**
     * Removes all children widgets.
     */
    void removeChildren();

    /**
     * Removes all children widget that are in a specified list.
     *
     * @param widgets the list of children widgets to be removed
     */
    void removeChildren(List<Widget> widgets);

    /**
     * Removes a dependency listener.
     *
     * @param dependency the dependency listener
     */
    void removeDependency(Widget.Dependency dependency);

    /**
     * Removes the widget from its parent.
     */
    void removeFromParent();

    /**
     * Schedules the widget for repainting.
     */
    // NOTE - has to be called before a change is set into the widget when the change immediatelly affects calculation of the local/scene location/boundary (means any property used in convertLocalToScene) because repaint/revalidate needs to calculate old scene boundaries
    void repaint();

    /**
     * Sets resolved location and bounds of the widget This method is usually
     * called from implementations of <code>Layout</code> interface.
     *
     * @param location the resolved location; if null then [0,0] point is used
     * instead
     * @param bounds the resolved bounds; if null then the preferred bounds are
     * used instead
     */
    void resolveBounds(Point location, Rectangle bounds);

    /**
     * Schedules the widget to repaint or revalidation.
     *
     * @param repaintOnly if true, then the widget is scheduled for repainting
     * only; if false, then widget is scheduled for revalidation (the
     * Scene.validate method has to be called after all changes to invoke
     * validation)
     */
    // NOTE - has to be called before a change is set into the widget when the change affects the local/scene location/boundary because repaint/revalidate needs to calculate old scene boundaries
    void revalidate(boolean repaintOnly);

    /**
     * Schedules the widget for revalidation. The Scene.validate method has to
     * be called after all changes to invoke validation. In some cases it is
     * invoked automatically.
     */
    // NOTE - has to be called before a change is set into the widget when the change affects the local/scene location/boundary because repaint/revalidate needs to calculate old scene boundaries
    void revalidate();

    /**
     * Sets a accessible context of the widget.
     *
     * @param accessibleContext the accessible context
     */
    void setAccessibleContext(AccessibleContext accessibleContext);

    /**
     * Sets the widget background paint.
     *
     * @param background the background paint
     */
    void setBackground(Paint background);

    /**
     * Sets the widget background color to be based on a resource property.
     *
     * @param property the background property name
     */
    void setBackgroundFromResource(String property);

    /**
     * Sets the border of the widget.
     *
     * @param border the border
     */
    void setBorder(Border border);

    /**
     * Sets the Swing layout as the border of the widget.
     *
     * @param swingBorder the Swing border
     */
    void setBorder(javax.swing.border.Border swingBorder);

    /**
     * Sets a clipping for the widget.
     *
     * @param checkClipping if true, then the clipping is used
     */
    void setCheckClipping(boolean checkClipping);

    /**
     * Assigns a constraint to a child widget.
     *
     * @param child the child widget
     * @param constraint the constraint
     */
    void setChildConstraint(Widget child, Object constraint);

    /**
     * Sets a cursor for the widget.
     *
     * @param cursor the mouse cursor; if null, the cursor is unset
     */
    void setCursor(Cursor cursor);

    /**
     * Sets whether the widget is enabled. If the widget is disabled then any
     * event is processed by assigned actions.
     *
     * @param enabled if true, then the widget is enabled
     */
    void setEnabled(boolean enabled);

    /**
     * Sets the widget font.
     *
     * @param font the font; if null, then widget unassignes its font.
     */
    void setFont(Font font);

    /**
     * Sets the widget background color to be based on a resource property.
     *
     * @param property the foreground property name
     */
    void setFontFromResource(String property);

    /**
     * Sets the widget foreground color.
     *
     * @param foreground the foreground color
     */
    void setForeground(Color foreground);

    /**
     * Sets the widget foreground color to be based on a resource property.
     *
     * @param property the foreground property name
     */
    void setForegroundFromResource(String property);

    /**
     * Sets the layout of the widget.
     *
     * @param layout the layout
     */
    void setLayout(Layout layout);

    /**
     * Sets a maximum size of the widget
     *
     * @param maximumSize the maximum size; if null, then maximum size are
     * unset.
     */
    void setMaximumSize(Dimension maximumSize);

    /**
     * Sets a minumum size of the widget
     *
     * @param minimumSize the minimum size; if null, then minimum size are
     * unset.
     */
    void setMinimumSize(Dimension minimumSize);

    /**
     * Sets the widget opacity.
     *
     * @param opaque if true, then the widget is opaque
     */
    void setOpaque(boolean opaque);

    /**
     * Sets a preferred bounds that are specified relatively to the location of
     * the widget.
     *
     * @param preferredBounds the preferred bounds; if null, then the preferred
     * bounds are unset
     */
    void setPreferredBounds(Rectangle preferredBounds);

    /**
     * Sets a preferred location of the widget.
     *
     * @param preferredLocation the preferred location; if null, then the
     * preferred location is unset
     */
    void setPreferredLocation(Point preferredLocation);

    /**
     * Sets a preferred size of the widget
     *
     * @param preferredSize the preferred size; if null, then preferred size are
     * unset.
     */
    void setPreferredSize(Dimension preferredSize);

    /**
     * Sets the resource table.
     *
     * @param table The widgets resource table.
     */
    void setResourceTable(ResourceTable table);

    /**
     * Sets a state of the widget.
     *
     * @param state the widget state
     */
    void setState(ObjectState state);

    /**
     * Sets a tool-tip of the widget.
     *
     * @param toolTipText the tool tip text
     */
    void setToolTipText(String toolTipText);

    /**
     * Sets whether the widget is visible.
     *
     * @param visible if true, then the widget is visible
     */
    void setVisible(boolean visible);

    // custom added
    ContextPaletteModel getContextPaletteModel();//moved from INodeWidget to cover all Widget

    IModelerScene getModelerScene();

    PopupMenuProvider getPopupMenuProvider();
    
}
