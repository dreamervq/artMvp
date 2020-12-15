/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.art.base;

/**
 * ================================================
 * Created by JessYan on 2018/7/27 15:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class Platform {
    public static final boolean DEPENDENCY_AUTO_LAYOUT;
    public static final boolean DEPENDENCY_SUPPORT_DESIGN;
    public static final boolean DEPENDENCY_GLIDE;
    public static final boolean DEPENDENCY_ANDROID_EVENTBUS;
    public static final boolean DEPENDENCY_EVENTBUS;
    public static final String WEIBO = "weibo";
    public static final String WECHAT = "wechat";
    public static final String WECHAT_TIMELINE = "wechat_timeline";
    public static final String QQ = "qq";
    public static final String QZONE = "qzone";
    public static final String COPY = "copy";
    public static final String HAIBAO = "haibao";

    public static final String KEY_WECHAT = "wxaa51cfa4437f4881";
    public static final String SECRET_WECHAT = "58908c8fe21015b0bade4cae7cc2e39a";

    public static final String KEY_WEIBO = "277889805";
    public static final String SECRET_WEIBO = "7a535076b0d562460ab0338d6175b3ad";

    public static final String KEY_QQ = "101903015";
    public static final String SECRET_QQ = "f95eacdc1cffaf6e855c3ededd8d2091";


    static {
        DEPENDENCY_AUTO_LAYOUT = findClassByClassName("com.zhy.autolayout.AutoLayoutInfo");
        DEPENDENCY_SUPPORT_DESIGN = findClassByClassName("android.support.design.widget.Snackbar");
        DEPENDENCY_GLIDE = findClassByClassName("com.bumptech.glide.Glide");
        DEPENDENCY_ANDROID_EVENTBUS = findClassByClassName("org.simple.eventbus.EventBus");
        DEPENDENCY_EVENTBUS = findClassByClassName("org.greenrobot.eventbus.EventBus");
    }

    private static boolean findClassByClassName(String className) {
        boolean hasDependency;
        try {
            Class.forName(className);
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }
        return hasDependency;
    }
}
