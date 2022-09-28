package com.richie.rn70;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.*;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import com.facebook.react.uimanager.ViewManager;

import java.util.*;

/**
 * Created by lylaut on 2022/09/28
 */
public class RnPackage implements ReactPackage {

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactApplicationContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new RnModule(reactApplicationContext));

        return modules;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }



    private static class RnModule extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {

        public RnModule(ReactApplicationContext context) {
            super(context);
        }

        @NonNull
        @Override
        public String getName() {
            return "extra";
        }

        @ReactMethod
        public void close() {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                currentActivity.finish();
            }
        }

        @ReactMethod
        public void test(ReadableMap data, Promise promise) {
            HashMap<String, Object> stringObjectHashMap = data.toHashMap();
            System.out.println(stringObjectHashMap);

            WritableMap map = RnPromiseUtil.convertPromiseSuccessResponseData(1, "success", Collections.singletonMap("granted", true));
            promise.resolve(map);
        }
    }
}
