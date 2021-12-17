package com.imooc.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewUtils {

    /**
     * 注入Activity
     * @param activity
     */
    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }
    /**
     * 注入View
     * @param view
     */
    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }
    /**
     * 注入View
     * @param view
     */
    public static void inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }

    /**
     * 兼容以上三个方法 object --> 反射要执行的类
     * @param finder
     * @param object
     */
    private static void inject(ViewFinder finder,Object object){
        injectField(finder, object);
        injectEvent(finder, object);
    }

    /**
     * 事件注入
     * @param finder
     * @param object
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        //1.获取添加注解的类属性
        Class<?> aClass = object.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            //2.遍历查找添加了OnClick 注解的方法
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick!=null){
                int[] viewIds = onClick.value();
                for (int viewId : viewIds) {
                    // 3. 找到对应资源id的view
                    View targetView = finder.findViewById(viewId);
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) !=null ;
                    if (targetView!=null){
                        // 4. view.setOnClickListener
                        targetView.setOnClickListener(new DeclaredOnClickListener(object,method,isCheckNet));
                    }
                }
            }
        }
    }
    private static class DeclaredOnClickListener implements View.OnClickListener {

        private Object object;
        private Method method;
        private boolean isCheckNet;

        public DeclaredOnClickListener(Object object, Method method, boolean isCheckNet) {
            this.object = object;
            this.method = method;
            this.isCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View view) {
            // 如果没有网络则弹出提示，不往下执行
            if (isCheckNet){
                if (!networkAvaliable(view.getContext())){
                    Toast.makeText(view.getContext(),"网络没有连接", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // 绑定注入事件
            try{
                // 所有方法都可以 包括私有和公有
                method.setAccessible(true);
                // 5.反射执行方法
                method.invoke(object,view);
            }catch (Exception e){
                e.printStackTrace();
                try{
                    method.invoke(object,null);
                }catch (Exception el){
                    el.printStackTrace();
                }
            }
        }
    }
    /**
     * 属性注入
     * @param finder
     * @param object
     */
    private static void injectField(ViewFinder finder, Object object) {
        //1.获取添加注解的类属性
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            //2.遍历查找对应的资源id
            ViewById annotation = declaredField.getAnnotation(ViewById.class);
            if (annotation!=null){
                int viewId = annotation.value();
                // 3. 找到对应资源id的view
                View targetView = finder.findViewById(viewId);
                // 能够注入所有修饰符 private public
                declaredField.setAccessible(true);
                //4.动态注入找到的View
                try{
                    declaredField.set(object,targetView);
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean networkAvaliable(Context context){
        try{
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo!=null && activeNetworkInfo.isConnected()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
