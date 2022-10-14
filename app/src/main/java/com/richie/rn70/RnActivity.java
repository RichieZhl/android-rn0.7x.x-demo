package com.richie.rn70;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;

/**
 * Created by lylaut on 2022/09/24
 */
public class RnActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "AwesomeProject";
    }

    protected String getJSBundleFile() {
//        return "assets://index.bundle";
        return null;
    }

    /**
     * Returns the instance of the {@link ReactActivityDelegate}. There the RootView is created and
     * you can specify the renderer you wish to use - the new renderer (Fabric) or the old renderer
     * (Paper).
     */
    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new MainActivityDelegate(this, getMainComponentName(), getJSBundleFile());
    }

    public static class MainActivityDelegate extends ReactActivityDelegate {

        private ReactNativeHost reactNativeHost;

        public MainActivityDelegate(ReactActivity activity, String mainComponentName, String jsBundleFile) {
            super(activity, mainComponentName);
            if (jsBundleFile != null && jsBundleFile.length() > 0) {
                this.reactNativeHost = new MainApplicationReactNativeHost(jsBundleFile);
            }
        }

        @Override
        public ReactNativeHost getReactNativeHost() {
            if (reactNativeHost != null) {
                return reactNativeHost;
            }
            return MainApplicationReactNativeHost.getApplicationReactNativeHost();
        }

        @Nullable
        @Override
        protected Bundle getLaunchOptions() {
            Bundle bundle = new Bundle();
            bundle.putFloat("custom", 1.1f);
            return bundle;
        }

        @Override
        protected ReactRootView createRootView() {
            ReactRootView reactRootView = new ReactRootView(getContext());
            // If you opted-in for the New Architecture, we enable the Fabric Renderer.
            reactRootView.setIsFabric(true);
            return reactRootView;
        }

        @Override
        protected boolean isConcurrentRootEnabled() {
            // If you opted-in for the New Architecture, we enable Concurrent Root (i.e. React 18).
            // More on this on https://reactjs.org/blog/2022/03/29/react-v18.html
            return true;
        }
    }
}
