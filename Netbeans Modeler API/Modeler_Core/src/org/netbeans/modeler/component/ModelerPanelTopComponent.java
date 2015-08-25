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
package org.netbeans.modeler.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.modeler.component.save.SaveDiagram;
import org.netbeans.modeler.component.save.ui.SaveNotifierYesNo;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerCore;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.palette.PaletteSupport;
import org.netbeans.modeler.specification.Vendor;
import org.netbeans.modeler.specification.model.DiagramModel;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.NotifyDescriptor;
import org.openide.awt.Toolbar;
import org.openide.cookies.SaveCookie;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
        preferredID = "ModelerPanelTopComponent",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ModelerPanelAction",
        preferredID = "ModelerPanelTopComponent")
@Messages({
    "CTL_ModelerPanelAction=ModelerPanel",
    "CTL_ModelerPanelTopComponent=ModelerPanel Window",
    "HINT_ModelerPanelTopComponent=This is a ModelerPanel window"
})
public class ModelerPanelTopComponent extends TopComponent implements ExplorerManager.Provider, IModelerPanel {

    private ExplorerManager explorerManager;
    private ModelerFile modelerFile;
    private IModelerScene modelerScene;
    private final Toolbar editorToolbar;
    private SaveDiagram saveCookies;

    public ModelerPanelTopComponent() {
        initComponents();
        editorToolbar = new Toolbar("Diagram Toolbar", false);
        add(editorToolbar, BorderLayout.NORTH);
    }

    @Override
    public void init(ModelerFile file) {
        saveCookies = new SaveDiagram(file);
        this.modelerFile = file;
        this.modelerScene = file.getVendorSpecification().getModelerDiagramModel().getModelerScene();
        this.setName(modelerFile.getName());
        this.setIcon(modelerFile.getIcon());
        this.setToolTipText(modelerFile.getTooltip());
        setFocusable(true);
        addKeyListener(new ModelerKeyAdapter(file));

        initializeToolBar();
        if (modelerScene.getView() == null) {
            scrollPane.setViewportView(modelerScene.createView());
        } else {
            scrollPane.setViewportView(modelerScene.getView());
        }

        TopComponent propertiesComponent = WindowManager.getDefault().findTopComponent("properties");
        if (!propertiesComponent.isOpened()) {
            propertiesComponent.open();
        }

        explorerManager = new ExplorerManager();

        initLookup();
//        associateLookup(getLookup());
    }
    private InstanceContent lookupContent = new InstanceContent();
    private Lookup lookup = null;

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            Lookup superLookup = super.getLookup();

            Lookup[] content = {superLookup, new AbstractLookup(lookupContent)};
            lookup = new ProxyLookup(content);
        }

        return lookup;
    }

    private void initLookup() {
//        paletteController = getAssociatedPalette();
        lookupContent.add(ExplorerUtils.createLookup(explorerManager, getActionMap())); //getActionMap() => setupActionMap(getActionMap()) to apply custom action key // it is commented because KeyAdapter functionality is added for key listener
        lookupContent.add(PaletteSupport.createPalette(modelerFile));
        lookupContent.add(modelerFile.getModelerScene());
        lookupContent.add(modelerFile.getModelerFileDataObject());
        lookupContent.add(getNavigatorCookie());
//        lookupContent.add(editorToolbar);
//        lookupContent.add(zoomManager);
    }
    private NavigatorHint navigatorCookie = null;

    private NavigatorHint getNavigatorCookie() {
        if (navigatorCookie == null) {
            navigatorCookie = new NavigatorHint();
        }
        return navigatorCookie;
    }

    /**
     * @return the forceClose
     */
    private boolean isForceClose() {
        return forceClose;
    }

    public class NavigatorHint implements NavigatorLookupHint, Node.Cookie {

        @Override
        public String getContentType() {
            return ModelerPanelTopComponent.this.getModelerFile().getModelerFileDataObject().getPrimaryFile().getMIMEType();
        }
    }

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    private ActionMap setupActionMap(javax.swing.ActionMap map) {
//        this.getInputMap(WHEN_IN_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('a'),
//                "doNothing");
//        this.getActionMap().put("doNothing",
//                doNothing);
        map.put(DefaultEditorKit.copyAction, new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ev
            ) {
                System.out.println("copyAction");

            }

        });
        map.put(DefaultEditorKit.cutAction, new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                System.out.println("cutAction");
            }

        });
        map.put(DefaultEditorKit.pasteAction, new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ev
            ) {
                System.out.println("pasteAction");

            }

        });
        map.put("delete", new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ev
            ) {
                System.out.println("Deleted Acion");

            }

        });
        return map;
    }

    private void initializeToolBar() {
        //DiagramEngine engine = scene.getEngine();
        IModelerDiagramEngine engine = getModelerScene().getModelerFile().getModelerDiagramEngine();

        if (engine != null) {
            engine.buildToolBar(editorToolbar);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>
    // Variables declaration - do not modify
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration

    @Override
    public void componentOpened() {
        super.componentOpened();
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        if (this.getModelerFile() != null) {
            ModelerCore.removeModelerFile(this.getModelerFile().getPath());
        }
    }

    @Override
    public void componentShowing() { //this function is added to handle multiple topcompoent for single file
        if (persistenceState == Boolean.FALSE) {
            modelerFile.getModelerFileDataObject().addSaveCookie(saveCookies);
        } else {
            modelerFile.getModelerFileDataObject().removeSaveCookie();
        }
    }

    /**
     * @return the modelerScene
     */
    public IModelerScene getModelerScene() {
        return modelerScene;
    }

    /**
     * @return the modelerFile
     */
    public ModelerFile getModelerFile() {
        return modelerFile;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }
    private static final int RESULT_CANCEL = 0;
    private static final int RESULT_NO = 1;
    private static final int RESULT_YES = 2;

    private boolean forceClose = false;

    @Override
    public final void forceClose() {
        forceClose = true;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ModelerPanelTopComponent.this.close();
            }
        });
    }

    @Override
    public boolean canClose() {
        boolean safeToClose = true;

        if (isForceClose()) {
            return true;
        }

        if (modelerFile == null || modelerFile.getModelerFileDataObject().getCookie(SaveCookie.class) == null || modelerFile.getModelerFileDataObject().getCookie(SaveCookie.class) != this.saveCookies) {
            this.getModelerScene().destroy();
            return true;
        }
        //prompt to save before close
        switch (saveDiagram()) {
            case RESULT_YES:
                modelerFile.save();
                break;

            case RESULT_NO:
                modelerFile.getModelerFileDataObject().setDirty(false, saveCookies);
                break;

            case RESULT_CANCEL:
                safeToClose = false;
                break;
        }

        if (safeToClose) {
            this.getModelerScene().destroy();
        }

        return safeToClose;
    }

    private void setDiagramDisplayName(final String name) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setDisplayName(name);
            }
        });
    }

    private int saveDiagram() {
        Vendor vendor = this.getModelerFile().getVendorSpecification().getVendor();
        DiagramModel diagram = this.getModelerFile().getVendorSpecification().getModelerDiagramModel().getDiagramModel();
        String title = "Save " + vendor.getDisplayName() + " " + diagram.getName() + " Diagram"; // NOI18N

        int result = RESULT_CANCEL;

        Object response = SaveNotifierYesNo.getDefault().displayNotifier(
                title, // NOI18N
                vendor.getName() + " " + diagram.getName(), // NOI18N
                this.getModelerFile().getName());

        if (response == SaveNotifierYesNo.SAVE_ALWAYS_OPTION) {
            result = RESULT_YES;
        } else if (response == NotifyDescriptor.YES_OPTION) {
            result = RESULT_YES;
        } else if (response == NotifyDescriptor.NO_OPTION) {
            result = RESULT_NO;
        } else // cancel or closed (x button)
        {
            result = RESULT_CANCEL;
        }

        return result;
    }
    private boolean persistenceState = true;
    private final static String SPACE_STAR = " *";

    @Override
    public void changePersistenceState(boolean state) {
        if (persistenceState == state) {
            return;
        }

        String diagramName = modelerFile.getName();
        String displName = "";
        persistenceState = state;
        if (persistenceState == Boolean.FALSE) {
            displName = /*"<b>" + */ diagramName + SPACE_STAR/* +"</b>"*/;
            modelerFile.getModelerFileDataObject().addSaveCookie(saveCookies);
        } else {
            displName = diagramName;
            modelerFile.getModelerFileDataObject().removeSaveCookie();
        }

        this.setDiagramDisplayName(displName);
    }

    @Override
    public boolean isPersistenceState() {
        return persistenceState;
    }

}
