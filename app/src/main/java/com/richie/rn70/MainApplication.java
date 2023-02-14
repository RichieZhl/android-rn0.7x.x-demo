package com.richie.rn70;

import android.app.Application;
import android.content.Context;
import com.facebook.react.*;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.soloader.SoLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by lylaut on 2022/09/22
 */
public class MainApplication extends Application implements ReactApplication {

    private final ReactNativeHost mReactNativeHost =
            new DefaultReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    packages.add(new RnPackage());
                    return packages;
                }

                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }

                @Override
                protected boolean isNewArchEnabled() {
                    return true;
                }

                @Override
                protected Boolean isHermesEnabled() {
                    return true;
                }
            };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // If you opted-in for the New Architecture, we enable the TurboModule system
        SoLoader.init(this, /* native exopackage */ false);
        DefaultNewArchitectureEntryPoint.load();
        // TODO support metro split bundle
//        getReactNativeHost().getReactInstanceManager().createReactContextInBackground();
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
