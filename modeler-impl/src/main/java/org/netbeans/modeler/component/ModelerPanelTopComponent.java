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
package org.netbeans.modeler.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.component.save.SaveDiagram;
import org.netbeans.modeler.component.save.ui.SaveNotifierYesNo;
import org.netbeans.modeler.core.ModelerCore;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.palette.PaletteSupport;
import org.netbeans.modeler.specification.model.DiagramModel;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.transferable.cp.WidgetTransferable;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.netbeans.spi.palette.PaletteController;
import org.openide.NotifyDescriptor;
import org.openide.awt.Toolbar;
import org.openide.cookies.SaveCookie;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallbackSystemAction;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.netbeans.core.windows.view.ui.NbSheet;
import org.openide.actions.FindAction;

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
    private Toolbar editorToolbar;
    private JScrollPane scrollPane;
    private SaveDiagram saveCookies;

    @Override
    public void init(ModelerFile modelerFile) {
        saveCookies = new SaveDiagram(modelerFile);
        this.modelerFile = modelerFile;
        modelerScene = modelerFile.getModelerDiagramModel().getModelerScene();
        this.setName(modelerFile.getName());
        this.setIcon(modelerFile.getIcon());
        this.setToolTipText(modelerFile.getTooltip());
        setFocusable(true);
        initComponents();
        setupActionMap(getActionMap());
        initLookup();
    }

    private InstanceContent lookupContent = new InstanceContent();
    private Lookup lookup = null;
    private Lookup exploreLookup;
    private PaletteController paletteController;

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            Lookup[] content = {super.getLookup(), new AbstractLookup(lookupContent)};
            lookup = new ProxyLookup(content);
        }
        return lookup;
    }

    private void initLookup() {
        explorerManager = new ExplorerManager();
        lookupContent.add(exploreLookup = ExplorerUtils.createLookup(explorerManager, getActionMap())); //getActionMap() => setupActionMap(getActionMap()) to apply custom action key // it is commented because KeyAdapter functionality is added for key listener
        if (!modelerFile.getModelerDiagramModel().getPaletteConfig().getCategoryNodeConfigs().isEmpty()) {
            lookupContent.add(paletteController = PaletteSupport.createPalette(modelerFile));
        }
        lookupContent.add(modelerFile.getModelerScene());
        if (modelerFile.getModelerFileDataObject() != null) {
            lookupContent.add(modelerFile.getModelerFileDataObject());
        }
        lookupContent.add(getNavigatorCookie());
    }

    private void cleanLookup() {
        lookupContent.remove(exploreLookup);
//        if (!modelerFile.getVendorSpecification().getPaletteConfig().getCategoryNodeConfigs().isEmpty()) {
//            lookupContent.remove(paletteController);
//        }
        lookupContent.remove(modelerFile.getModelerScene());
        if(modelerFile.getModelerFileDataObject()!=null){
            lookupContent.remove(modelerFile.getModelerFileDataObject());
        }
        lookupContent.remove(getNavigatorCookie());

        navigatorCookie = null;
        exploreLookup = null;
        paletteController = null;
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
            if(ModelerPanelTopComponent.this.getModelerFile().getModelerFileDataObject()==null){
                return null;
            }
            return ModelerPanelTopComponent.this.getModelerFile().getModelerFileDataObject().getPrimaryFile().getMIMEType();
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }
 
    private ActionMap setupActionMap(javax.swing.ActionMap map) {
        CallbackSystemAction a = SystemAction.get(FindAction.class);
        map.put(a.getActionMapKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelerFile.getModelerDiagramEngine().searchWidget();
            }
        });
        map.put(DefaultEditorKit.copyAction, new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                WidgetTransferable.copy(modelerScene);
            }
        });
        map.put(DefaultEditorKit.pasteAction, new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                Object[] selectedObjects = modelerScene.getSelectedObjects().toArray();
                if (selectedObjects.length == 1) {
                    Widget selectedWidget = modelerScene.findWidget(selectedObjects[0]);
                    if (selectedWidget instanceof IBaseElementWidget) {
                        WidgetTransferable.paste((IBaseElementWidget) selectedWidget);
                    }
                } else {
                    WidgetTransferable.paste((IBaseElementWidget) modelerScene);
                }
            }
        });
        return map;
    }

    @Override
    public void initializeToolBar() {
        SwingUtilities.invokeLater(() -> {
            modelerFile.getModelerDiagramEngine().buildToolBar(editorToolbar);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
        scrollPane = new javax.swing.JScrollPane();
        add(scrollPane, java.awt.BorderLayout.CENTER);
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUnitIncrement(5);
        InputMap im = vertical.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("DOWN"), "positiveUnitIncrement");
        im.put(KeyStroke.getKeyStroke("UP"), "negativeUnitIncrement");

        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
        horizontal.setUnitIncrement(5);
        im = horizontal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("RIGHT"), "positiveUnitIncrement");
        im.put(KeyStroke.getKeyStroke("LEFT"), "negativeUnitIncrement");

        editorToolbar = new Toolbar("Diagram Toolbar", false);
        add(editorToolbar, BorderLayout.NORTH);

        if (modelerScene.getView() == null) {
            scrollPane.setViewportView(modelerScene.createView());
        } else {
            scrollPane.setViewportView(modelerScene.getView());
        }
    }

    @Override
    public void componentOpened() {
        super.componentOpened();      
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        cleanReference();
    }

    private void cleanReference() {
        if (this.getModelerFile() != null) {
            ModelerCore.removeModelerFile(this.getModelerFile());
        }
        SwingUtilities.invokeLater(() -> {
            modelerScene.cleanReference();

            for (KeyListener keyListener : this.getKeyListeners()) {
                this.removeKeyListener(keyListener);
            }
            modelerFile.getModelerDiagramEngine().cleanToolBar(editorToolbar);
            cleanLookup();
            if (modelerFile.getModelerFileDataObject() != null) {
                modelerFile.getModelerFileDataObject().removeSaveCookie();
            }
            modelerFile.setModelerDiagramModel(null);
            modelerScene.getBaseElements().clear();
            modelerScene.setBaseElementSpec(null);
            System.gc();
        });
    }

    @Override
    public void componentShowing() { //this function is added to handle multiple topcompoent for single file
        if (modelerFile.getModelerFileDataObject() != null) {
            if (persistenceState == Boolean.FALSE) {
                modelerFile.getModelerFileDataObject().addSaveCookie(saveCookies);
            } else {
                modelerFile.getModelerFileDataObject().removeSaveCookie();
            }
        }
    }

    /**
     * @return the modelerFile
     */
    @Override
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
        SwingUtilities.invokeLater(ModelerPanelTopComponent.this::close);
    }

    @Override
    public boolean canClose() {
        boolean safeToClose = true;

        if (isForceClose()) {
            return true;
        }
        if (modelerFile.getModelerFileDataObject() == null 
                || modelerFile.getModelerFileDataObject().getCookie(SaveCookie.class) == null 
                || modelerFile.getModelerFileDataObject().getCookie(SaveCookie.class) != this.saveCookies) {
            modelerScene.destroy();
            if (modelerFile.getParentFile() != null) {
                modelerFile.getParentFile().removeChildrenFile(modelerFile);
            }
            return true;
        }
        //prompt to save before close
        switch (saveDiagram()) {
            case RESULT_YES:
                modelerFile.save();
                break;

            case RESULT_NO:
                if(modelerFile.getModelerFileDataObject()!=null)
                    modelerFile.getModelerFileDataObject().setDirty(false, saveCookies);
                break;

            case RESULT_CANCEL:
                safeToClose = false;
                break;
        }

        if (safeToClose) {
            modelerScene.destroy();
            if (modelerFile.getParentFile() != null) {
                modelerFile.getParentFile().removeChildrenFile(modelerFile);
            }
        }

        return safeToClose;
    }

    private void setDiagramDisplayName(final String name) {
        SwingUtilities.invokeLater(() -> {
            setDisplayName(name);
        });
    }

    private int saveDiagram() {
        DiagramModel diagram = this.getModelerFile().getModelerDiagramModel().getDiagramModel();
        String title = "Save Diagram"; // NOI18N
        int result;

        Object response = SaveNotifierYesNo.getDefault().displayNotifier(
                title, diagram.getName(), this.getModelerFile().getName());

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
        if (modelerFile.getModelerFileDataObject() == null
                || persistenceState == state) {
            return;
        }

        String diagramName = modelerFile.getName();
        String displayName;
        persistenceState = state;
        if (persistenceState == Boolean.FALSE) {
            displayName = /*"<b>" + */ diagramName + SPACE_STAR/* +"</b>"*/;
            modelerFile.getModelerFileDataObject().addSaveCookie(saveCookies);
        } else {
            displayName = diagramName;
            modelerFile.getModelerFileDataObject().removeSaveCookie();
        }

        this.setDiagramDisplayName(displayName);
    }

    @Override
    public boolean isPersistenceState() {
        return persistenceState;
    }
    
    private static void handlePropertyPanelEvent() {
        TopComponent.getRegistry().addPropertyChangeListener(evt -> {
            String property = evt.getPropertyName();
            Object oldValue = evt.getOldValue();
            Object newValue = evt.getNewValue();
            if (property.equals(TopComponent.Registry.PROP_ACTIVATED)) {
                if((oldValue instanceof TopComponent.Cloneable)//MultiViewCloneableTopComponent 
                        && newValue instanceof ModelerPanelTopComponent) {
                    openPropertyPanel();
                } else if((oldValue instanceof ModelerPanelTopComponent) 
                        && newValue instanceof ModelerPanelTopComponent) {
                    //ignore
                } else if(oldValue instanceof ModelerPanelTopComponent
                        && newValue instanceof TopComponent.Cloneable) {
                    closePropertyPanel();
                }
            } else if (property.equals(TopComponent.Registry.PROP_TC_OPENED)) {
                if(newValue instanceof ModelerPanelTopComponent) {
                    openPropertyPanel();
                }
            } else if (property.equals(TopComponent.Registry.PROP_TC_CLOSED)) {
                if(newValue instanceof ModelerPanelTopComponent) {
                    closePropertyPanel();
                }
            }
        });
    }
    
        
    private static void openPropertyPanel() {
        TopComponent propertyWindow = NbSheet.findDefault();
        try {
            if (!propertyWindow.isOpened()) {
                propertyWindow.open();
                propertyWindow.requestActive();
            }
        } catch (Exception ex) {
            // ignore
        }
    }
    
    private static void closePropertyPanel() {
        NbSheet propertyWindow = NbSheet.findDefault();
        if (propertyWindow.isOpened()) {
            propertyWindow.close();
        }
    }
    
    static {
        handlePropertyPanelEvent();
    }

}
