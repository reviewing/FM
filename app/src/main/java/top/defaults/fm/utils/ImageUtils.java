package top.defaults.fm.utils;

import android.content.Context;
import android.net.Uri;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * @author duanhong
 * @version 1.0, 9/9/16 10:58 AM
 */
public class ImageUtils {
    public static void initializeFresco(final Context c) {
        final int M = 1024 * 1024;
        Supplier<MemoryCacheParams> memoryCacheParamsSupplier = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return new MemoryCacheParams(40 * M, 100, 10 * M, 50, 20 * M);
            }
        };
        Supplier<File> basePathSupplier = new Supplier<File>() {
            @Override
            public File get() {
                return c.getCacheDir();
            }
        };
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(c)
                .setBaseDirectoryPathSupplier(basePathSupplier)
                .setBaseDirectoryName("images")
                .setMaxCacheSize(100 * M)
                .setMaxCacheSizeOnLowDiskSpace(10 * M)
                .setMaxCacheSizeOnVeryLowDiskSpace(0).build();
        DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(c)
                .setBaseDirectoryPathSupplier(basePathSupplier)
                .setBaseDirectoryName("thumbs")
                .setMaxCacheSize(30 * M)
                .setMaxCacheSizeOnLowDiskSpace(10 * M)
                .setMaxCacheSizeOnVeryLowDiskSpace(0).build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(c)
                .setBitmapMemoryCacheParamsSupplier(memoryCacheParamsSupplier)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setSmallImageDiskCacheConfig(smallDiskCacheConfig)
                .build();
        Fresco.initialize(c, config);
    }

    /**
     * Resize image with the specified uri.
     *
     * @param image  The view
     * @param uri    The url for the image
     * @param resize resize to in pixel.
     */
    public static void setImageUri(SimpleDraweeView image, Uri uri, int resize) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(resize, resize))
                .build();
        resize(image, request);
    }

    /*
     *resize image with the specified uri.
     */
    private static void resize(SimpleDraweeView image, ImageRequest request) {
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(image.getController())
                .setImageRequest(request)
                .build();
        image.setController(controller);
    }
}
