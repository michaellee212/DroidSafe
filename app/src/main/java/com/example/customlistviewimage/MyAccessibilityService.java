package com.example.customlistviewimage;

import android.accessibilityservice.AccessibilityService;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService{
    private CategoryDataSource categoryHelp;
    private long timeStart = 0;
    private long timeEnd = 0;
    private boolean isBlock;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventID = event.getEventType();
        //System.out.println(eventID);
        switch(eventID)
        {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                timeStart = System.currentTimeMillis();
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                timeEnd = System.currentTimeMillis();
                if(timeEnd-timeStart > 1000) {
                    AccessibilityNodeInfo info = event.getSource();
                    parseInfo(info);
                }

                break;
        }
    }
    public void parseInfo(AccessibilityNodeInfo info){
        categoryHelp = new CategoryDataSource(this);
        if(info == null) {
            return;
        }
        if(info.getText() != null && info.getText().length() > 0) {
            System.out.println(info.getText().toString());
            isBlock = categoryHelp.isBlock(info.getText().toString());
            for (int i = 0; i < info.getChildCount(); i++) {
                AccessibilityNodeInfo child = info.getChild(i);
                parseInfo(child);
                child.recycle();
            }
        }
    }
      /*

       //.... find out if the url is blocked
       public void comparison(int action, string URL){

    }*/

    @Override
    public void onInterrupt() {

        System.out.println("onInterrupt");
    }


}
