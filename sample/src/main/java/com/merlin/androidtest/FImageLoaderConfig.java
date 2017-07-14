package com.merlin.androidtest;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.anbetter.log.MLog;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.fresco.helper.config.BitmapMemoryCacheParamsSupplier;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * User: Merlin
 * Date: 2017-2-28
 * Desc: 图片库的配置文件
 */

public class FImageLoaderConfig {

    private static final String IMAGE_PIPLINE_CACHE_DIA = "image_cache";

    private static final String IMAGE_PIPLINE_SMALL_CACHE_DIA = "small_image_cache";

    private static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

    private static final int MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE = 5 * ByteConstants.MB;

    private static ImagePipelineConfig mImagePipelineConfig;


    public static ImagePipelineConfig getImagePipelineConfig(final Context context){
        if (mImagePipelineConfig == null){

            File fileCacheDir = context.getApplicationContext().getCacheDir();

            DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(fileCacheDir)
                    .setBaseDirectoryName(IMAGE_PIPLINE_CACHE_DIA)
                    .build();

            DiskCacheConfig smallDistCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryName(IMAGE_PIPLINE_SMALL_CACHE_DIA)
                    .setBaseDirectoryPath(fileCacheDir)
                    .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE)
                    .build();

            // 当内存紧张时采取的措施
            MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
            memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
                @Override
                public void trim(MemoryTrimType trimType) {
                    final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                    MLog.i("Fresco onCreate suggestedTrimRatio = " + suggestedTrimRatio);

                    if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                            ) {
                        // 清除内存缓存
                        Fresco.getImagePipeline().clearMemoryCaches();
                    }
                }
            });

            FLog.setMinimumLoggingLevel(FLog.VERBOSE);

            mImagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context,new OkHttpClient().newBuilder().build())
                    .setBitmapsConfig(Bitmap.Config.RGB_565)
                    .setDownsampleEnabled(true)
                    .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                    .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier((ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)))
                    .setMainDiskCacheConfig(mainDiskCacheConfig)
                    .setSmallImageDiskCacheConfig(smallDistCacheConfig)
                    .build();
        }

        return mImagePipelineConfig;
    }
}
