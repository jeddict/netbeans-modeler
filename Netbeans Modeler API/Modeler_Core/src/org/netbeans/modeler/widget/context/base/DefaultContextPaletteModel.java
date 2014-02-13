/** Copyright [2014] Gaurav Gupta
   *
   *Licensed under the Apache License, Version 2.0 (the "License");
   *you may not use this file except in compliance with the License.
   *You may obtain a copy of the License at
   *
   *    http://www.apache.org/licenses/LICENSE-2.0
   *
   *Unless required by applicable law or agreed to in writing, software
   *distributed under the License is distributed on an "AS IS" BASIS,
   *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   *See the License for the specific language governing permissions and
   *limitations under the License.
   */
 package org.netbeans.modeler.widget.context.base;

import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import java.util.ArrayList;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 *
 * 
 */
public class DefaultContextPaletteModel implements ContextPaletteModel
{
    private ArrayList < ContextPaletteButtonModel > descriptions = 
            new ArrayList < ContextPaletteButtonModel >();
    private INodeWidget context = null;
    
    private FOLLOWMODE followMouse = FOLLOWMODE.NONE;
    
    
    public DefaultContextPaletteModel(INodeWidget widget)
    {
        setContext(widget);
    }
    
    public DefaultContextPaletteModel(INodeWidget widget, 
                                      FOLLOWMODE mode)
    {
        this(widget);
        followMouse = mode;
    }
    
    public void addDescriptor(ContextPaletteButtonModel desc)
    {
        descriptions.add(desc);
    }

    public void initialize(String path)
    {
//        
//        ArrayList < ContextPaletteItem > retVal = 
//                        new ArrayList <ContextPaletteItem>();
//        FileObject fo = FileUtil.getConfigFile(path);
//        DataFolder df = fo != null ? DataFolder.findFolder(fo) : null;
//        if (df != null)
//        {
//            try
//            {
//                df.setSortMode(DataFolder.SortMode.NONE);
//                DataObject[] dObjs = df.getChildren();
//                for(DataObject curDObj : dObjs)
//                {
//                    ContextPaletteItem item = curDObj.getLookup().lookup(ContextPaletteItem.class);
////                        FileObject descriptorFO = curDObj.getPrimaryFile();
//                    if(item != null)
//                    {
//                        addDescriptor(createButton(item, curDObj));
//                    }
//                    else if(curDObj.getPrimaryFile().isFolder() == true)
//                    {
//                        DefaultGroupButtonModel group = new DefaultGroupButtonModel();
//                        DataFolder folder = (DataFolder)curDObj;
//                        for(DataObject groupObj : folder.getChildren())
//                        {
//                           ContextPaletteItem groupItem = groupObj.getLookup().lookup(ContextPaletteItem.class);
//                           if(groupItem != null)
//                           {
//                               group.add(createButton(groupItem, groupObj));
//                           }
//                        }
//                        addDescriptor(group);
//                    }
//                }
//            }
//            catch(IOException e)
//            {
//
//            }
//        }
    }
    
    public FOLLOWMODE getFollowMouseMode()
    {
        return followMouse;
    }
    
//    protected ContextPaletteButtonModel createButton(ContextPaletteItem item,
//                                                     DataObject dObj)
//    {
//        FileObject fo = dObj.getPrimaryFile();
//        ContextPaletteButtonModel retVal = (ContextPaletteButtonModel) fo.getAttribute("model");
//        if(retVal == null)
//        {
//            if(dObj instanceof DataShadow)
//            {
//                DataObject original = ((DataShadow)dObj).getOriginal();
//                FileObject originalFO = original.getPrimaryFile();
//                retVal = (ContextPaletteButtonModel) originalFO.getAttribute("model");
//            }
//        }
//        
//        if(retVal == null)
//        {
//            retVal = new DefaultPaletteButtonModel(item.getSmallIcon(), 
//                                                item.getTooltip());
//        }
//        else
//        {
//            retVal.setImage(item.getSmallIcon());
//            retVal.setTooltip(item.getTooltip());
//        }
//        
//        retVal.initialize(dObj);
//        retVal.setPaletteModel(this);
//        return retVal;
//    }
    
//    protected ArrayList < ContextPaletteItem > getInstanceFromFilesSystem(String path)
//    {
//        ArrayList < ContextPaletteItem > retVal = 
//                        new ArrayList <ContextPaletteItem>();
//        FileObject fo = FileUtil.getConfigFile(path);
//        DataFolder df = fo != null ? DataFolder.findFolder(fo) : null;
//        if (df != null)
//        {
//            DataObject[] dObjs = df.getChildren();
//            for(DataObject curDObj : dObjs)
//            {
//                ContextPaletteItem item = curDObj.getLookup().lookup(ContextPaletteItem.class);
//                if(item != null)
//                {
//                    retVal.add(item);
//                }
//            }
//        }
//        return retVal;
//    }
    
    ///////////////////////////////////////////////////////////////
    // ContextPaletteModel Implementation
    
    public ArrayList < ContextPaletteButtonModel > getChildren()
    {
        return descriptions;
    }
    
    public INodeWidget getContext()
    {
        return context;
    }
    ///////////////////////////////////////////////////////////////
    // Data Access    

    public void setContext(INodeWidget context)
    {
        this.context = context;
    }
}
