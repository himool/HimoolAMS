package com.pyramid.witmat.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.pyramid.witmat.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EnvironmentUtil {
    // 缓存根目录
    public static final String BOOKS_STORAGE = "rubbish";
    public static final String BOOKS_STORAGE_PICTURE = "show/picture";
    public static final String BOOKS_COVER = "covers";
    // 下载文件存储路径
    public static final String DOWNLOAD_FILE = "file";
    // 日志文件路径
    public static final String BOOK_LOG = "logcat";
    // 插件路径
    public static final String PLUGIN = "plugin";

    public static MessageHashMap MESSAGEHASHMAP = new MessageHashMap();

    /*
     *
     */
    private static File checkDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return null;
            }
        }
        return file;
    }

    public static int initExtendsStorage(Context context) {

        int sdcardState = getSdcardState();

        // 如果是正在检测SDCard ，则等待20秒，如果20秒后还未得到结果，则按失败处理
        if (sdcardState == -2) {
            for (int i = 0; i < 10 && sdcardState != 0; i++) {
                try {
                    Thread.sleep(2000);
                    sdcardState = getSdcardState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (sdcardState != 0) {
            return sdcardState;
        }

        ArrayList<String> paths = new ArrayList<String>();
        paths.add(getMainFilePath());
        paths.add(getBooksPath());
        for (int i = 0; i < paths.size(); i++) {
            File fi = checkDir(paths.get(i));
            if (fi == null) {
                return -(9 + i);
            }
        }

        return 0;
    }

    public static int getSdcardState() {
        String states[] = {Environment.MEDIA_MOUNTED, // 对象是否存在并具有读/写权限
                Environment.MEDIA_BAD_REMOVAL, // SDCard 被卸载前己被移除
                Environment.MEDIA_CHECKING, // 对象正在磁盘检查。
                Environment.MEDIA_MOUNTED_READ_ONLY, // 对象权限为只读
                Environment.MEDIA_NOFS, // 对象为空白或正在使用不受支持的文件系统
                Environment.MEDIA_REMOVED, // 不存在 SDCard
                Environment.MEDIA_SHARED, // SDCard 未安装 ，并通过 USB 大容量存储共享
                Environment.MEDIA_UNMOUNTABLE, // SDCard 不可被安装 如果 SDCard
                // 是存在但不可以被安装
                Environment.MEDIA_UNMOUNTED // SDCard 已卸掉如果 SDCard 是存在但是没有被安装
        };
        String str = Environment.getExternalStorageState();
        for (int i = 0; i < states.length; i++) {
            if (str.equalsIgnoreCase(states[i])) {
                return -i;
            }
        }
        return 0;
    }

    //静态方法不会创建对象的
    public static String getBooksPath() {
        String path = getMainFilePath() + File.separator + BOOKS_STORAGE;
        return path;
    }

    public static String getRootPath() {
        String path = getMainFilePath();
        return path;
    }

    public static void deleteFolder(final String novelId, Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    FileUtils.deleteFolder(EnvironmentUtil.getBooksPath() + File.separator + novelId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    //
    public static String getDownloadPath(Context context) {
        String path = getMainFilePath() + File.separator + DOWNLOAD_FILE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    //
    public static String getLogcatPath(Context context) {
        String path = getMainFilePath() + File.separator + BOOK_LOG;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getPluginPath() {
        String path = getMainFilePath() + File.separator + PLUGIN;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getBooksPictures() {
        String path = getMainFilePath() + File.separator
                + BOOKS_STORAGE_PICTURE;
        return path;

    }

    public static String getBookCovers() {
        String path = getMainFilePath() + File.separator
                + BOOKS_COVER;
        return path;

    }

    public static String getMainFilePath() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + fileNameFromAppPage();
        return path;
    }

    public static String getExternalSD() {
        String path = Environment.getExternalStorageDirectory().getPath();
        return path;
    }

    public static class MessageHashMap extends HashMap<Integer, String> {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public MessageHashMap() {
            this.put(0, "SDCard是否存在并具有读/写权限");
            this.put(-1, "SDCard 被卸载前己被移除");
            this.put(-2, "SDCard 正在磁盘检查");
            this.put(-3, "SDCard 权限为只读");
            this.put(-4, "SDCard 为空白或正在使用不受支持的文件系统");
            this.put(-5, "SDCard 不存在");
            this.put(-6, "SDCard 未安装 ，并通过 USB 大容量存储共享");
            this.put(-7, "SDCard 不可被安装 如果 SDCard 是存在但不可以被安装");
            this.put(-8, "SDCard 已卸掉或SDCard存在但是没有被安装");
            this.put(-9, "创建存贮主目录失败");
            this.put(-10, "创建图片目录失败");
            this.put(-11, "创建音频目录失败");
        }
    }

//	/**
//	 * 获取手机IMEI
//	 */
//	public static String getImei(Context ctx) {
//		TelephonyManager tm = (TelephonyManager) ctx
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		String imei = tm.getDeviceId();
//		return imei;
//	}

    /**
     * 获取 移动终端设备id号
     *
     * @param context :上下文文本对象
     * @return id 移动终端设备id号
     */

    public static long getAppInstallTime(Activity context) {
        PackageManager packageManager = context.getPackageManager();
        long firstInstallTime = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            firstInstallTime = packageInfo.firstInstallTime;//应用第一次安装的时间

        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstInstallTime;
    }
    /**
     * 获得当前应用包名
     **/

    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null && infos.size() > 0)
            for (ActivityManager.RunningAppProcessInfo info : infos) {
                if (info.pid == pid)//得到当前应用
                    return info.processName;//返回包名
            }
        return "";
    }

    //获取到包名最后一个单词作为文件名
    public static String fileNameFromAppPage() {
        String name = getAppProcessName(App.getInstance().getApplicationContext());
        String[] names = name.split("\\.");
        if (names.length > 0) {
            return names[names.length - 1];
        }
        return "ebook";
    }

    public static String fileNameFromAppPage(Context context) {
        String name = getAppProcessName(App.getInstance().getApplicationContext());
        String[] names = name.split("\\.");
        if (names.length > 0) {
            return names[names.length - 1];
        }
        return "ebook";
    }

    /**
     * 获取应用的版本名称（用于显示给用户时使用）
     * 使用 x.yy.mmdd 格式, 如 1.12.0906
     *
     * @param context
     * @return
     */
    public static String getSoftVersion(Context context) {
        String version = "1.0";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取软件的版本号（版本对比升级，使用此值）
     *
     * @param context
     * @return
     */
    public static int getSoftVersionCode(Context context) {
        int version = 1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public static String getAssetTxt(Context context, String fileName) {
        String result = "";
        byte[] buffer = new byte[0];
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isInstanll(Context context, String packageName) {
        final PackageManager pack = context.getPackageManager();
        List<PackageInfo> pinfo = pack.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
                if (pn.contains(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 调用系统安装应用
     */
    public static boolean install(Context context, File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }


    /**
     * 前往设置网络
     *
     * @param context
     */
    public static void toSetNet(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 当前手机是否有可用网络 (所有网络类型)
     *
     * @param context
     * @return
     */
    public static boolean isNetConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        if (info.isAvailable()) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 将下载下来的图片缓存到sd卡里
     *
     * @param bitmap
     * @param file
     */
    public static boolean addBitmapToSDCardCache(Bitmap bitmap, File file) {
        boolean success = false;
        byte[] bytes = bitmapToBytes(bitmap, 100);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 将bitmap转换成bytes
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, int quality) {
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;

        bm = big(bm, newWidth, scaleWidth * height);

        return bm;
    }

    public static Bitmap big(Bitmap b, float x, float y) {
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = (float) x / w;//要强制转换，不转换我的在这总是死掉。
        float sy = (float) y / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }


}
