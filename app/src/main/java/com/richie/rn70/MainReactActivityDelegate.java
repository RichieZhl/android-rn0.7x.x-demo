package com.richie.rn70;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.*;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.react.modules.core.PermissionListener;

import java.util.List;

/**
 * Created by lylaut on 2022/11/11
 */
public class MainReactActivityDelegate {
    @Nullable
    private final Activity mActivity;
    @Nullable
    private final String mMainComponentName;
    @Nullable
    private PermissionListener mPermissionListener;
    @Nullable
    private Callback mPermissionsCallback;
    private ReactDelegate mReactDelegate;

    private ReactNativeHost reactNativeHost;

    private final ViewGroup mRootView;

    private final Bundle mBundle;

    private final String mJSBundleFile;

    private final boolean mSplit;

    public MainReactActivityDelegate(Activity activity, ViewGroup rootView, @Nullable String mainComponentName, @Nullable String jsBundleFile, boolean split, @Nullable Bundle bundle) {
        this.mActivity = activity;
        this.mRootView = rootView;
        this.mMainComponentName = mainComponentName;
        this.mBundle = bundle;
        this.mJSBundleFile = jsBundleFile;
        this.mSplit = split;

        if (!mSplit && jsBundleFile != null && jsBundleFile.length() > 0) {
            reactNativeHost = new DefaultReactNativeHost(activity.getApplication()) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    packages.add(new RnPackage());
                    return packages;
                }
            };
        }
    }

    @NonNull
    private Bundle composeLaunchOptions() {
        Bundle composedLaunchOptions = this.mBundle;
        if (this.isConcurrentRootEnabled()) {
            if (composedLaunchOptions == null) {
                composedLaunchOptions = new Bundle();
            }

            composedLaunchOptions.putBoolean("concurrentRoot", true);
        }

        return composedLaunchOptions;
    }

    public ReactRootView createRootView() {
        return new ReactRootView(this.getContext());
    }

    public ReactNativeHost getReactNativeHost() {
        if (reactNativeHost != null) {
            return reactNativeHost;
        }
        return ((ReactApplication)this.getPlainActivity().getApplication()).getReactNativeHost();
    }

    public ReactInstanceManager getReactInstanceManager() {
        return this.mReactDelegate.getReactInstanceManager();
    }

    public String getMainComponentName() {
        return this.mMainComponentName;
    }

    public void onCreate() {
        String mainComponentName = this.getMainComponentName();
        Bundle launchOptions = this.composeLaunchOptions();
        this.mReactDelegate = new ReactDelegate(this.getPlainActivity(), this.getReactNativeHost(), mainComponentName, launchOptions) {
            public ReactRootView createRootView() {
                return MainReactActivityDelegate.this.createRootView();
            }
        };
        if (mainComponentName != null) {
            if (mSplit) {
                CatalystInstance catalystInstance = getReactInstanceManager().getCurrentReactContext().getCatalystInstance();
                if (mJSBundleFile.startsWith("assets://")) {
                    catalystInstance.loadScriptFromAssets(getContext().getAssets(), mJSBundleFile, false);
                } else {
                    catalystInstance.loadScriptFromFile(mJSBundleFile, mJSBundleFile, false);
                }
            }
            this.loadApp(mainComponentName);
        }
    }

    public void loadApp(String appKey) {
        this.mReactDelegate.loadApp(appKey);
        this.mRootView.addView(this.mReactDelegate.getReactRootView());
    }

    public void onPause() {
        this.mReactDelegate.onHostPause();
    }

    public void onResume() {
        this.mReactDelegate.onHostResume();
        if (this.mPermissionsCallback != null) {
            this.mPermissionsCallback.invoke(new Object[0]);
            this.mPermissionsCallback = null;
        }

    }

    public void onDestroy() {
        this.mReactDelegate.onHostDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mReactDelegate.onActivityResult(requestCode, resultCode, data, true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.getReactNativeHost().hasInstance() && this.getReactNativeHost().getUseDeveloperSupport() && keyCode == 90) {
            event.startTracking();
            return true;
        } else {
            return false;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.mReactDelegate.shouldShowDevMenuOrReload(keyCode, event);
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (this.getReactNativeHost().hasInstance() && this.getReactNativeHost().getUseDeveloperSupport() && keyCode == 90) {
            this.getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        } else {
            return false;
        }
    }

    public boolean onBackPressed() {
        return this.mReactDelegate.onBackPressed();
    }

    public boolean onNewIntent(Intent intent) {
        if (this.getReactNativeHost().hasInstance()) {
            this.getReactNativeHost().getReactInstanceManager().onNewIntent(intent);
            return true;
        } else {
            return false;
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.getReactNativeHost().hasInstance()) {
            this.getReactNativeHost().getReactInstanceManager().onWindowFocusChange(hasFocus);
        }

    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getReactNativeHost().hasInstance()) {
            this.getReactInstanceManager().onConfigurationChanged(this.getContext(), newConfig);
        }

    }

    @TargetApi(23)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        this.mPermissionListener = listener;
        this.getPlainActivity().requestPermissions(permissions, requestCode);
    }

    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        this.mPermissionsCallback = new Callback() {
            public void invoke(Object... args) {
                if (MainReactActivityDelegate.this.mPermissionListener != null && MainReactActivityDelegate.this.mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                    MainReactActivityDelegate.this.mPermissionListener = null;
                }

            }
        };
    }

    public Context getContext() {
        return Assertions.assertNotNull(this.mActivity);
    }

    public Activity getPlainActivity() {
        return (Activity)this.getContext();
    }

    public boolean isConcurrentRootEnabled() {
        return true;
    }
}
