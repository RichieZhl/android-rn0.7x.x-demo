package com.richie.rn70;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lylaut on 2022/09/24
 */
public class RnActivity extends ReactActivity {

    private static final ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
            1,
            2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            Executors.defaultThreadFactory()
    );

    public interface CreateRNInterface {
        void createRnIntent(Intent intent);
    }

    public static String md5(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 创建一个MD5算法对象
            md.update(raw.getBytes()); // 给算法对象加载待加密的原始数据
            byte[] encryContext = md.digest(); // 调用digest方法完成哈希计算
            StringBuilder buf = new StringBuilder();
            for (byte b : encryContext) {
                buf.append(String.format("%02x", b));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class Data {
        String mJSBundleFile;
        Map<String, Object> params;

        public Data(String mJSBundleFile, Map<String, Object> params) {
            this.mJSBundleFile = mJSBundleFile;
            this.params = params;
        }
    }

    public static void buildRn(final Context packageContext, final String urlString, final Map<String, Object> params, final CreateRNInterface createRNInterface) {
        try {
            if (urlString.startsWith("assets://")) {
                mHashMap.put(urlString, new Data(urlString, params));
                createRNInterface.createRnIntent(newRn(packageContext, urlString));
                return;
            }
            final URL url = new URL(urlString);
            if (url.getPort() == 8081) {
                mHashMap.put(urlString, new Data(null, params));
                createRNInterface.createRnIntent(newRn(packageContext, urlString));
                return;
            }

            mThreadPoolExecutor.execute(() -> {
                String absolutePath = packageContext.getApplicationContext().getFilesDir().getAbsolutePath();
                File rnBundleDir = new File(absolutePath, "rnBundle");
                if ((!rnBundleDir.exists() || !rnBundleDir.isDirectory()) && !rnBundleDir.mkdir()) {
                    return;
                }

                String filename = md5(urlString);
                if (filename == null) {
                    return;
                }

                String fileLocalPath = absolutePath + "/rnBundle/" + filename + ".bundle";
                File file = new File(fileLocalPath);
                if (file.exists()) {
                    mHashMap.put(urlString, new Data(fileLocalPath, params));
                    createRNInterface.createRnIntent(newRn(packageContext, urlString));
                    return;
                }

                HttpURLConnection conn = null;
                InputStream inputStream = null;
                try {
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setRequestMethod("GET");

                    inputStream = conn.getInputStream();
                    int responseCode = conn.getResponseCode();
                    if (responseCode >= 200 && responseCode <= 204) {
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        byte[] buffer = new byte[4096];
                        int len;
                        while((len = inputStream.read(buffer)) != -1) {
                            raf.write(buffer,0, len);
                        }
                        raf.close();

                        inputStream.close();
                        inputStream = null;
                        conn.disconnect();
                        conn = null;

                        mHashMap.put(urlString, new Data(fileLocalPath, params));
                        createRNInterface.createRnIntent(newRn(packageContext, urlString));
                    }
                } catch (IOException e) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Intent newRn(Context packageContext, String url) {
        Intent it = new Intent(packageContext, RnActivity.class);
        it.putExtra("url", url);
        return it;
    }

    public static void cleanCaches(Context context) {
        String absolutePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        File rnBundleDir = new File(absolutePath, "rnBundle");
        if (rnBundleDir.exists() && rnBundleDir.isDirectory()) {
            deleteFile(rnBundleDir);
        }
    }

    private static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile != null && childFile.length > 0) {
                    for (File f : childFile) {
                        deleteFile(f);
                    }
                }
                file.delete();
            }
        }
    }

    private final static ConcurrentHashMap<String, Data> mHashMap = new ConcurrentHashMap<>();

    private interface HandleParams {
        void handleParamsCallback(Bundle bundle);
    }

    private MainActivityDelegate delegate;

    private Data mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        mData = mHashMap.remove(url);
        assert mData != null;
        delegate.setJsBundleFile(mData.mJSBundleFile);
        super.onCreate(savedInstanceState);
    }

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "AwesomeProject";
    }

    /**
     * Returns the instance of the {@link ReactActivityDelegate}. There the RootView is created and
     * you can specify the renderer you wish to use - the new renderer (Fabric) or the old renderer
     * (Paper).
     */
    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        delegate = new MainActivityDelegate(this, getMainComponentName(), bundle -> {
            for (Map.Entry<String, Object> entry : mData.params.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    bundle.putString(entry.getKey(), (String) value);
                } else if (value instanceof Boolean) {
                    bundle.putBoolean(entry.getKey(), (Boolean) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(entry.getKey(), (Integer) value);
                } else if (value instanceof Long) {
                    bundle.putLong(entry.getKey(), (Long) value);
                } else if (value instanceof Float) {
                    bundle.putFloat(entry.getKey(), (Float) value);
                } else if (value instanceof Double) {
                    bundle.putDouble(entry.getKey(), (Double) value);
                }
            }
        });
        return delegate;
    }

    public static class MainActivityDelegate extends ReactActivityDelegate {

        private ReactNativeHost reactNativeHost;

        private final HandleParams handleParams;

        public MainActivityDelegate(ReactActivity activity, String mainComponentName, HandleParams handleParams) {
            super(activity, mainComponentName);
            this.handleParams = handleParams;
        }

        public MainActivityDelegate(ReactActivity activity, String mainComponentName, String jsBundleFile, HandleParams handleParams) {
            super(activity, mainComponentName);
            this.handleParams = handleParams;
            if (jsBundleFile != null && jsBundleFile.length() > 0) {
                this.reactNativeHost = new MainApplicationReactNativeHost(jsBundleFile);
            }
        }

        public void setJsBundleFile(String jsBundleFile) {
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
            handleParams.handleParamsCallback(bundle);
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
