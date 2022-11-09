package com.th3rdwave.safeareacontext

import com.facebook.jni.HybridData
import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.soloader.SoLoader

/**
 * Created by lylaut on 2022/11/04
 */
class RNSafeAreaComponentsRegistry(componentFactory: com.facebook.react.fabric.ComponentFactory) {
    companion object {
        init {
            SoLoader.loadLibrary("rnsafeareacontext_modules")
        }

        @JvmStatic
        fun register(componentFactory: com.facebook.react.fabric.ComponentFactory): RNSafeAreaComponentsRegistry {
            return RNSafeAreaComponentsRegistry(componentFactory)
        }
    }

    @DoNotStrip
    private var mHybridData: HybridData? = null

    @DoNotStrip
    private external fun initHybrid(componentFactory: com.facebook.react.fabric.ComponentFactory): HybridData?

    init {
        mHybridData = initHybrid(componentFactory)
    }
}
