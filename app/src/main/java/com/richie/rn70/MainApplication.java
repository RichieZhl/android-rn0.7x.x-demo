package com.richie.rn70;

import android.app.Application;
import android.content.Context;
import com.facebook.react.*;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.soloader.SoLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by lylaut on 2022/09/22
 */
public class MainApplication extends Application implements ReactApplication {

    private ReactNativeHost mNewArchitectureNativeHost;

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (mNewArchitectureNativeHost == null) {
            mNewArchitectureNativeHost = new MainApplicationReactNativeHost(null);
        }
        return mNewArchitectureNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplicationReactNativeHost.setApplication(this);
        // If you opted-in for the New Architecture, we enable the TurboModule system
        ReactFeatureFlags.useTurboModules = true;
        SoLoader.init(this, /* native exopackage */ false);
        initializeFlipper();
    }

    private void initializeFlipper() {
        if (BuildConfig.DEBUG) {
            try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
                Class<?> aClass = Class.forName("com.richie.ReactNativeFlipper");
                aClass
                        .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
                        .invoke(null, this, getReactNativeHost().getReactInstanceManager());
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
