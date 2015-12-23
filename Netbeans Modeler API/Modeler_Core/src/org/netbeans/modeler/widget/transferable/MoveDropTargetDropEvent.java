package org.netbeans.modeler.widget.transferable;

import org.netbeans.modeler.widget.transferable.MoveWidgetTransferable;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import org.netbeans.api.visual.widget.Widget;

public class MoveDropTargetDropEvent extends DropTargetDropEvent {

    private MoveWidgetTransferable widgetTransferable = null;

    public MoveDropTargetDropEvent(Widget dropWidget, Point pt) {
        super((new DropTarget()).getDropTargetContext(), pt, 0, 0);
        widgetTransferable = new MoveWidgetTransferable(dropWidget);
    }

    @Override
    public Transferable getTransferable() {
        return new Transferable() {

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{MoveWidgetTransferable.FLAVOR};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return MoveWidgetTransferable.FLAVOR.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor) == true) {
                    return widgetTransferable;
                }
                throw new UnsupportedFlavorException(flavor);
            }
        };
    }
}
