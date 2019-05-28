package com.cqf.fenglib.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cqf.fenglib.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Binga on 6/20/2018.
 */

public class MyUtils{

    private static final int BUFFER_SIZE = 1024 * 1024;//1M Byte
    public static Locale newLocale=new Locale("zh");
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static boolean isShow = false;
    private final static int SPACE_TIME = 300;//2次点击的间隔时间，单位ms
    private static long lastClickTime;

    public static Toast toast;
    public static boolean isFastDoubleClick() {
        long currentTime = SystemClock.elapsedRealtime();
        boolean isClick;

        if (currentTime - lastClickTime > SPACE_TIME) {
            isClick = false;
        } else {
            isClick = true;
        }
        showMyLog("isFastClick:"+isClick);
        lastClickTime = currentTime;
        return isClick;
    }

    public static void initLocaleLanguage(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
        } else {
            configuration.locale = newLocale;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());//更新配置
    }
    //吐司
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    //长吐司
    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    //打印长日志
    public static void showLargeLog(String tag, String content) {
        if (content.length() > 4000) {
            String show = content.substring(0, 4000);
            Log.v(tag, show);
            if (content.length() - 4000 > 4000) {
                String partLog = content.substring(4000, content.length());
                showLargeLog(tag, partLog);
            } else {
                String partLog = content.substring(4000, content.length());
                showLargeLog(tag, partLog);
            }
        } else {
            Log.v(tag, content);
        }
    }

    //px转换成dp
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //dp转换成px
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //解压缩一个文件 zipFile    压缩文件 folderPath 解压缩的目标目录
    public static boolean upZipFile(File zipFile, String folderPath) throws IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            InputStream is = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "UTF-8");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream os = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        }
        return true;
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        int versionCode = 0;

        //拿到管理类之类这样的代码
        /*
         * SMSManager.getDefaul()
		 * windowManager 可以拿到和屏幕相关的信息
		 * TelephoneManager 电话相关的
		 * NotificationManager 拿到和通知相关的
		 * PackageManager 和包相关的
		 */

        PackageManager packageManager = context.getPackageManager();
        // 拿到包的一些信息
        try {
            // 标记写一个0代表所有信息都要，javabean对象
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            // 拿到版本号和版本名称
            versionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    //获取版本名称
    public static String getVersionName(Context context) {
        String versionName = null;

        PackageManager packageManager = context.getPackageManager();
        // 拿到包的一些信息
        try {
            // 标记写一个0代表所有信息都要
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);


            // 拿到版本号和版本名称
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }

    //验证手机格式
    public static boolean isPhoneNum(String mobiles) {
        String telRegex = "[1][1345678]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    public static boolean isPwd(String pwd) {
        String pwdRegex = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,20}$";
        if (isEmpty(pwd)) {
            return false;
        } else {
            return pwd.matches(pwdRegex);
        }
    }

    /*public static void setWheelStyle(WheelView wheelView){
        wheelView.setWheelSize(3);
        wheelView.setWheelAdapter(new com.wx.wheelview.adapter.ArrayWheelAdapter(wheelView.getContext()));

        WheelView.WheelViewStyle style=new WheelView.WheelViewStyle();
        style.selectedTextColor= Color.BLACK;
        style.textColor=Color.GRAY;
        wheelView.setStyle(style);
    }*/
    //验证手机并弹吐司
    public static boolean checkPhone(Context context, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            MyUtils.showToast(context, "手机号不能为空");
        } else if (!MyUtils.isPhoneNum(phoneNum)) {
            MyUtils.showToast(context, "手机号格式不正确");
        } else {
            return true;
        }
        return false;
    }

    //判断是不是一个合法的电子邮件地址
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    //判断是否有网络连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    //判断是否有网络连接
    public static boolean checkNetWithToast(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                if (!networkInfo.isAvailable()) {
                    MyUtils.showToast(context, "网络连接异常");
                    return networkInfo.isAvailable();
                }
        }
        MyUtils.showToast(context, "网络连接异常");
        return false;
        /*ConnectivityManager connectivityManager = (ConnectivityManager) YcWater.YcContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //新版本调用方法获取网络状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getStatus().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            //否则调用旧版本方法
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getStatus() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        MyUtils.showToast(context,"网络连接异常");
        return false;*/
    }

    //光标居右
    public static void setCusorToRight(EditText editText) {
        editText.setSelection(editText.getText().toString().length());
    }

    //密码明文模式监听
    public static void setPwdListener(final EditText editText, final ImageView view, final int showId, final int hideId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = (isShow ? false : true);
                if (!isShow) {
                    view.setImageResource(showId);
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    view.setImageResource(hideId);
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                setCusorToRight(editText);
            }
        });
    }

    /**
     * Android5.1以上的处理
     */
    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(statusColor);
        }
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void showMyLog(String msg) {
        Log.v(Config.TAG, msg);
    }

    //启动相册
    public static void startPhoto(Activity activity) {

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        Log.d("tag", "startPhoto: " + componentName);
        if (componentName != null) {//防止启动意图时app崩溃
            activity.startActivityForResult(intent, 1);
        }
    }

    //启动相机
    public static void startCamera(Activity activity) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
                File.separator + "user_icon.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//intent隐式调用启动拍照界面
        intent.putExtra("return-data", false);//该属性设置为false表示拍照后不会将数据返回到onResluet方法中（建议设置为false，这样获取的图片会比较清晰）
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//该属性设置的是拍照后图片保存的位置
        //防止app启动意图时崩溃
        if (componentName != null) {
            activity.startActivityForResult(intent, 3);
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 根据图片的Uri获取图片的绝对路径(适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String[] ids = wholeID.split(":");
        String id = null;
        if (ids == null) {
            return null;
        }
        if (ids.length > 1) {
            id = ids[1];
        } else {
            id = ids[0];
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    public static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    public static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    public static void hideSoftInput(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static double divde(double value1, double value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        //默认保留两位会有错误，这里设置保留小数点后4位
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getCurrentYMD() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
    }

    public static String getCurrentYMDHMS() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA).format(new Date());
    }

    public static String getPreviousMonthYMD() {
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String endDate = sdf.format(today);//当前日期
        //获取三十天前日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(today);
        theCa.add(theCa.DATE, -30);//最后一个数字30可改，30天的意思
        Date start = theCa.getTime();
        String startDate = sdf.format(start);//三十天之前日期
        return startDate;
    }

    //判断是手机还是平板
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    //获取设备唯一号
    public static String getMacAddress(Context context) {

        String macAddress = null;
        WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());

        if (!wifiManager.isWifiEnabled()) {
            //必须先打开，才能获取到MAC地址
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
        }
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    //请求权限，一次请求多个
    public static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity,
                    permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
                break;
            }
        }
        //需要通过onRequestPermissionsResult处理回调操作
        /*@Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == 1) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PERMISSION_GRANTED) {
                        Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }*/
    }
    public static String replaceChar(String oldStr,String replace,int start,int end){
        return oldStr.replace(oldStr.substring(start,end),replace);
    }


    /*public static void smartSuccess(SmartRefreshLayout smart, BaseQuickAdapter adapter, int pageNo, int pageSize, int currentSize){

        if (pageNo == 1) {//下拉刷新
            smart.finishRefresh();
            if (currentSize < pageSize) {//加载完毕
                smart.setEnableLoadMore(false);
            }else {
                smart.setEnableLoadMore(true);
            }
        } else {//上拉加载更多
            smart.finishLoadMore();
            if (currentSize < pageSize) {//加载完毕
                smart.setEnableLoadMore(false);
            }else {
                smart.setEnableLoadMore(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
    public static void smartError(SmartRefreshLayout smart, BaseQuickAdapter adapter,int pageNo){
        if (pageNo == 1) {
            smart.finishRefresh(false);
        } else {
            smart.finishLoadMore(false);
        }
        adapter.notifyDataSetChanged();
    }
*/
    /**
     * 匹配金额是否符合要求（99999999.99）
     */
    public static boolean isMoney(String money) {
        String regex = "(^[1-9][0-9]{0,7}(\\.[0-9]{0,2})?)|(^0(\\.[0-9]{0,2})?)";
        return isMatches(money, regex);
    }

    public static boolean isPercent(String percent) {
        String regex = "(^[1-9][0-9]{0,1}(\\.[0-9]{0,2})?)|(^0(\\.[0-9]{0,2})?)";
        return isMatches(percent, regex);
    }

    /**
     * 字符串是否符合正则表达式的规则
     */
    private static boolean isMatches(String text, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher m = pattern.matcher(text);
        return m.matches();
    }

    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}
