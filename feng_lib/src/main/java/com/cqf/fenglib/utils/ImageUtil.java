package com.cqf.fenglib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.LruCache;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>200) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 左下角添加水印（多行，图标 + 文字）
     * 参考资料：
     * Android 对Canvas的translate方法总结    https://blog.csdn.net/u013681739/article/details/49588549
     * @param photo
     */
    public static Bitmap addWaterMark(Context context, Bitmap photo, List<String> textList, List<Integer> iconIdList, boolean isShowIcon) {
        Bitmap newBitmap = photo;
        try {
            if (null == photo) {
                return null;
            }

            int srcWidth = photo.getWidth();
            int srcHeight = photo.getHeight();

            //Resources resources = context.getResources();
            //float scale = resources.getDisplayMetrics().density;
            int unitHeight = srcHeight > srcWidth ? srcWidth/30 : srcHeight / 25;
            int padding = unitHeight;
            int marginLeft = padding;
            int marginBottom = padding;
            int textSize = unitHeight;

            //创建一个bitmap
            if (!newBitmap.isMutable()) {
                newBitmap = copy(photo);
            }
            //将该图片作为画布
            Canvas canvas = new Canvas(newBitmap);

            // 设置画笔
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
            textPaint.setTextSize(textSize);// 字体大小
            textPaint.setTypeface(Typeface.DEFAULT);// 采用默认的宽度
            textPaint.setColor(Color.WHITE);// 采用的颜色v

            Rect bounds = new Rect();
            String gText = "hello world!";
            textPaint.getTextBounds(gText, 0, gText.length(), bounds);

            int iconWidth = bounds.height();//图片宽度
            int maxTextWidth = srcWidth - padding*3 - iconWidth;//最大文字宽度

            for (int i = textList.size() - 1 ; i >=0 ; i--){
                String text = textList.get(i);
                int iconId = iconIdList.get(i);

                canvas.save();//锁画布(为了保存之前的画布状态)

                //文字处理
                StaticLayout layout = new StaticLayout(text, textPaint, maxTextWidth, Layout.Alignment.ALIGN_NORMAL,
                        1.0f, 0.0f, true); // 确定换行
                //在画布上绘制水印图片
                if (isShowIcon){
                    Bitmap watermark= BitmapFactory.decodeResource(context.getResources(),iconId);
                    int iconHeight = iconWidth * ((watermark.getHeight()*1000)/watermark.getWidth())/1000;//维持图片宽高比例，也可以简单粗暴 iconHeight = iconWidth;
                    //图片相对文字位置居中
                    RectF rectF=new RectF(marginLeft,srcHeight - marginBottom - layout.getHeight()/2 - iconHeight/2
                            ,marginLeft + iconWidth,srcHeight - marginBottom - layout.getHeight()/2 + iconHeight/2);
                    canvas.drawBitmap(watermark,null,rectF,null);//限定图片显示范围
                }

                //绘制文字
                canvas.translate(isShowIcon ? marginLeft + iconWidth + padding : marginLeft, srcHeight - layout.getHeight() - marginBottom); // 设定画布位置
                layout.draw(canvas); // 绘制水印

                //marginBottom 更新
                marginBottom = marginBottom + (padding + layout.getHeight());

                canvas.restore();//把当前画布返回（调整）到上一个save()状态之前
            }

            // 保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            // 存储
            canvas.restore();

        } catch (Exception e) {
            e.printStackTrace();
            return newBitmap;
        }
        return newBitmap;
    }
    /**
     * 根据原位图生成一个新的位图，并将原位图所占空间释放
     *
     * @param srcBmp 原位图
     * @return 新位图
     */
    public static Bitmap copy(Bitmap srcBmp) {
        Bitmap destBmp = null;
        try {

            // 创建一个临时文件
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "temppic/tmp.txt");
            if (file.exists()) {// 临时文件 ， 用一次删一次
                file.delete();
            }
            file.getParentFile().mkdirs();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, width * height * 4);
            // 将位图信息写进buffer
            srcBmp.copyPixelsToBuffer(map);
            // 释放原位图占用的空间
            srcBmp.recycle();
            // 创建一个新的位图
            destBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            map.position(0);
            // 从临时缓冲中拷贝位图信息
            destBmp.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();
            file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
            destBmp = null;
            return srcBmp;
        }
        return destBmp;
    }

    //设置水印图片在左上角
    public static Bitmap createWaterMaskLeftTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark, 
                dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                                int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.save();
        // 存储
        canvas.restore();
        return newb;
    }

    //设置水印图片在右下角
    public static Bitmap createWaterMaskRightBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, 
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight), 
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    //设置水印图片到右上角
    public static Bitmap createWaterMaskRightTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingTop) {
        return createWaterMaskBitmap( src, watermark, 
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight), 
                dp2px(context, paddingTop));
    }

    //设置水印图片到左下角
    public static Bitmap createWaterMaskLeftBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft), 
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    //设置水印图片到中间
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark, 
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    //给图片添加文字到左上角
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
                                           int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                dp2px(context, paddingLeft),  
                dp2px(context, paddingTop) + bounds.height());
    }

    //绘制文字到右下角
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text,
                                               int size, int color, int paddingRight, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight), 
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    //绘制文字到右上方
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text,
                                            int size, int color, int paddingRight, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight), 
                dp2px(context, paddingTop) + bounds.height());
    }

    //绘制文字到左下方
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                dp2px(context, paddingLeft),  
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }
    //绘制文字list到左下方
    public static Bitmap drawTextsToLeftBottom(Context context, Bitmap bitmap, String[] texts,
                                               int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        for (int i = 0; i < texts.length; i++) {

            paint.getTextBounds(texts[i], 0, texts[i].length(), bounds);
            bitmap=drawTextToBitmap(context, bitmap, texts[i], paint, bounds,
                    dp2px(context, paddingLeft),
                    bitmap.getHeight() - dp2px(context, paddingBottom)-dp2px(context, size+10)*i);
        }
        return bitmap;
    }

    //绘制文字到中间
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
                                          int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                (bitmap.getWidth() - bounds.width()) / 2,  
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    //缩放图片
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    //dip转pix
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String save(Bitmap mBitmap, String parentFilePath, String fileName, boolean clearBlank, int blank) throws IOException {

        File parentFile = new File(parentFilePath);
        if (!parentFile.exists()) {//不存在创建
            parentFile.mkdirs();
            MyUtils.showMyLog(parentFile.getPath());
        }
        Bitmap bitmap = mBitmap;
        //BitmapUtil.createScaledBitmapByHeight(srcBitmap, 300);//  压缩图片
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank);
        }
        File file = new File(parentFilePath, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return file.getPath();
    }

    /**
     * 逐行扫描 清楚边界空白。
     * @param bp
     * @param blank 边距留多少个像素
     */
    private static Bitmap clearBlank(Bitmap bp, int blank) {
        int mBgColor = Color.TRANSPARENT;
        int HEIGHT = bp.getHeight();
        int WIDTH = bp.getWidth();
        int top = 0, left = 0, right = 0, bottom = 0;
        int[] pixs = new int[WIDTH];
        boolean isStop;
        //扫描上边距不等于背景颜色的第一个点
        for (int y = 0; y < HEIGHT; y++) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //扫描下边距不等于背景颜色的第一个点
        for (int y = HEIGHT - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        pixs = new int[HEIGHT];
        //扫描左边距不等于背景颜色的第一个点
        for (int x = 0; x < WIDTH; x++) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //扫描右边距不等于背景颜色的第一个点
        for (int x = WIDTH - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        //计算加上保留空白距离之后的图像大小
        left = left - blank > 0 ? left - blank : 0;
        top = top - blank > 0 ? top - blank : 0;
        right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
        bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    //view转bitmap
    public static Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    //获取scrollview的截屏
    public static Bitmap scrollViewToBitmap(NestedScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static Bitmap viewToBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }

    /**
     * @param rawBitmap 原来的Bitmap
     * @param myRow       切成几行
     * @param column    切成几列
     */
    public static ArrayList<Bitmap> splitImage(Bitmap rawBitmap, int myRow, int column){
        int rawBitmapWidth=rawBitmap.getWidth();
        int rawBitmapHeight=rawBitmap.getHeight();
//        int row=rawBitmapHeight/rawBitmapWidth;//切成正方形。。剩余分割图片rawBitmapHeight%rawBitmapWidth
        if (rawBitmap.getByteCount()/1024/1024<1){
            ArrayList<Bitmap> partImagesArrayList=new ArrayList<>();

            return partImagesArrayList;
        }
        int row=myRow;
        ArrayList<Bitmap> partImagesArrayList=new ArrayList<Bitmap>(row*column);
        MyUtils.showMyLog("rawBitmapWidth="+rawBitmapWidth+",rawBitmapHeight="+rawBitmapHeight);
        int perPartWidth=rawBitmapWidth/column;
        int perPartHeight=rawBitmapHeight/(row);
        int endPartHeight=rawBitmapHeight%(row);
        MyUtils.showMyLog("perPartWidth="+perPartWidth+",perPartHeight="+perPartHeight+
                ",endPartHeight="+perPartHeight);
        Bitmap perBitmap=null;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {//列
                int x=j*perPartWidth;
                int y=i*perPartHeight;
                MyUtils.showMyLog("i="+i+",j="+j+",x="+x+",y="+y);
                perBitmap= Bitmap.createBitmap(rawBitmap, x, y, perPartWidth, perPartHeight);
                partImagesArrayList.add(perBitmap);
                if (i==row-1&&endPartHeight!=0){
                    perBitmap= Bitmap.createBitmap(rawBitmap, x, y+perPartHeight, perPartWidth, endPartHeight);
                    partImagesArrayList.add(perBitmap);
                }
            }
        }
        MyUtils.showMyLog("size="+partImagesArrayList.size());
        return partImagesArrayList;
    }
}