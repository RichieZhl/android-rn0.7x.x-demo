package com.richie.rn70;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import java.util.Date;

/**
 * Created by lylaut on 2022/11/11
 */
public class RnMetroSplitActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, PermissionAwareActivity {

    // 根视图
    private FrameLayout rootView;

    // 子包路径
    public static final String BUNDLE_EXTRA_KEY = "bundle";

    private MainReactActivityDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rn_boot);

        rootView = this.findViewById(R.id.rn_root_fl);

        String bundle = "assets://business.android.bundle";
        loadBundle(bundle);
    }

    protected String getMainComponentName() {
        return "AwesomeProject";
    }

    private void loadBundle(String localBundlePath) {
        mDelegate = new MainReactActivityDelegate(this, rootView, getMainComponentName(), localBundlePath, true, new Bundle());
        mDelegate.onCreate();
        mDelegate.getReactInstanceManager().addReactInstanceEventListener(
                reactContext -> {
                    final long tickEnd = new Date().getTime();
//                    System.out.println(String.format("rn runtime cost time:%.3f", (tickEnd - tickStart) / 1000.0));
                });
    }

    @Override
    public void onBackPressed() {
        if (this.mDelegate == null || !mDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mDelegate != null) {
            this.mDelegate.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mDelegate != null) {
            this.mDelegate.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mDelegate != null) {
            this.mDelegate.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mDelegate != null) {
            this.mDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mDelegate == null) {
            return super.onKeyDown(keyCode, event);
        }
        return this.mDelegate.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mDelegate == null) {
            return super.onKeyUp(keyCode, event);
        }
        return this.mDelegate.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (this.mDelegate == null) {
            return super.onKeyLongPress(keyCode, event);
        }
        return this.mDelegate.onKeyLongPress(keyCode, event) || super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (this.mDelegate == null || !this.mDelegate.onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        if (this.mDelegate == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                super.requestPermissions(permissions, requestCode);
            }
            return;
        }
        this.mDelegate.requestPermissions(permissions, requestCode, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (this.mDelegate == null) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        this.mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (this.mDelegate != null) {
            this.mDelegate.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mDelegate != null) {
            this.mDelegate.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        finish();
    }
}
