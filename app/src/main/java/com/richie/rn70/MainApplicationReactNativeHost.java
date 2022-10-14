package com.richie.rn70;

import com.facebook.react.ReactPackage;
import com.facebook.newarchitecture.BaseReactNativeHost;

import java.util.List;

/**
 * A {@link ReactNativeHost} that helps you load everything needed for the New Architecture, both
 * TurboModule delegates and the Fabric Renderer.
 *
 * <p>Please note that this class is used ONLY if you opt-in for the New Architecture (see the
 * `newArchEnabled` property). Is ignored otherwise.
 */
public class MainApplicationReactNativeHost extends BaseReactNativeHost {
  public MainApplicationReactNativeHost(String jsBundleFile) {
    super(jsBundleFile);
  }

  protected void addReactPackage(final List<ReactPackage> packages) {
    packages.add(new RnPackage());
  }

  protected boolean jsEngineAsHermesEnabled() {
    return true;
  }
}
