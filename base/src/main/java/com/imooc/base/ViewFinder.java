package com.imooc.base;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * 通过资源ID查找对因资源
 */
public class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }
    public ViewFinder(View view) {
        this.mView = view;
    }
    public View findViewById(int viewId){
        return mActivity!=null?mActivity.findViewById(viewId):mView.findViewById(viewId);
    }
}
