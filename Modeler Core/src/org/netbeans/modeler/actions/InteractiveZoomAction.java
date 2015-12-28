package org.netbeans.modeler.actions;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 *
 */
public class InteractiveZoomAction extends WidgetAction.Adapter {

    private Scene scene;
    private Point lastLocation;
    private double zoomMultiplier;

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {

            scene = widget.getScene();
            lastLocation = event.getPoint();
            lastLocation = widget.getScene().convertSceneToView(lastLocation);

            return State.CONSUMED;
        }
        return State.REJECTED;
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent event) {

        Point newLocation = event.getPoint();
        newLocation = widget.getScene().convertSceneToView(newLocation);

        int amount = lastLocation.y - newLocation.y;

//        //System.out.println("lastLocation.y : " + lastLocation.y);
//        //System.out.println("newLocation.y : " + newLocation.y);
//        //System.out.println("amount : " + amount);
        double zoom = scene.getZoomFactor();
        zoomMultiplier = 1 + Math.abs(amount) / (zoom * 100) / 5;
        if (amount > 0 && zoom < (double) ZoomManager.MAX_ZOOM_PERCENT / 100) {
            zoom *= zoomMultiplier;
            zoom = Math.min(zoom, (double) ZoomManager.MAX_ZOOM_PERCENT / 100);
        } else if (amount < 0 && zoom > (double) ZoomManager.MIN_ZOOM_PERCENT / 100) {
            zoom /= zoomMultiplier;
            zoom = Math.max(zoom, (double) ZoomManager.MIN_ZOOM_PERCENT / 100);
        }
        if (zoom != scene.getZoomFactor()) {
            scene.setZoomFactor(zoom);
        }

        return State.REJECTED;
    }

    @Override
    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        if (scene != widget.getScene()) {
            return State.REJECTED;
        }

        Point newLocation = event.getPoint();
        newLocation = widget.getScene().convertSceneToView(newLocation);

        int amount = lastLocation.y - newLocation.y;

        double zoom = scene.getZoomFactor();
        zoomMultiplier = 1 + Math.abs(amount) / (zoom * 100) / 5;
        if (amount > 0 && zoom < (double) ZoomManager.MAX_ZOOM_PERCENT / 100) {
            zoom *= zoomMultiplier;
            zoom = Math.min(zoom, (double) ZoomManager.MAX_ZOOM_PERCENT / 100);
        } else if (amount < 0 && zoom > (double) ZoomManager.MIN_ZOOM_PERCENT / 100) {
            zoom /= zoomMultiplier;
            zoom = Math.max(zoom, (double) ZoomManager.MIN_ZOOM_PERCENT / 100);
        }

        if (zoom != scene.getZoomFactor()) {
            scene.setZoomFactor(zoom);
        }

        lastLocation = newLocation;
        return State.CONSUMED;
    }
}
