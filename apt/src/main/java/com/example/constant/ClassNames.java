package com.example.constant;

import com.squareup.javapoet.ClassName;

/**
 * Created by apple on 16-11-19.
 */
public interface ClassNames {
    ClassName VIEW_HOLDER = ClassName.get("android.support.v7.widget.RecyclerView", "ViewHolder");
    ClassName VIEW = ClassName.get("android.view", "View");
    ClassName VIEW_GROUP = ClassName.get("android.view", "ViewGroup");
    ClassName CONTEXT = ClassName.get("android.content", "Context");
    ClassName ADAPTER = ClassName.get("android.support.v7.widget.RecyclerView", "Adapter");
}
