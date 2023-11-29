package xaeroplus.util;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Suppliers;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import wdl.WDL;
import xaeroplus.XaeroPlus;
import xaeroplus.settings.XaeroPlusSettingRegistry;
import xaeroplus.util.highlights.HighlightAtChunkPos;
import xaeroplus.util.highlights.RegionRenderPos;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static xaeroplus.util.ChunkUtils.loadHighlightChunksAtRegion;

public class WDLHelper {
    private static int wdlColor = ColorHelper.getColor(0, 255, 0, 100);
    // getting the set of saved chunks is expensive. This supplier acts as a cache to speed things up.
    private static final Supplier<Set<Long>> getChunkSupplier = Suppliers.memoizeWithExpiration(WDLHelper::getSavedChunks, 500, TimeUnit.MILLISECONDS);
    private static boolean hasLoggedFail = false;
    private static boolean checkedWdlPresent = false;
    private static boolean isWdlPresent = false;

    public static boolean isWdlPresent() {
        if (!checkedWdlPresent) {
            checkedWdlPresent = true;
            try {
                Class.forName(WDL.class.getName());
                Set<ChunkPos> savedChunks = WDL.getInstance().savedChunks;
                WDL.getInstance().getChunkList();
                isWdlPresent = true;
            } catch (final Throwable e) {
                if (!hasLoggedFail) {
                    XaeroPlus.LOGGER.error("WDL mod not present or has unsupported version, WDL features will be disabled.");
                    hasLoggedFail = true;
                }
                isWdlPresent = false;
            }
        }
        return isWdlPresent;
    }

    public static boolean isDownloading() {
        try {
            return WDL.downloading;
        } catch (final Throwable e) {
            return false;
        }
    }

    public static Set<Long> getSavedChunksWithCache() {
        return getChunkSupplier.get();
    }

    private static Set<Long> getSavedChunks() {
        try {
            final HashSet<Long> set = new HashSet<>();
            set.addAll(WDL.getInstance().getChunkList().stream().map(Chunk::getPos).map(ChunkUtils::chunkPosToLong).collect(Collectors.toList()));
            set.addAll(WDL.getInstance().savedChunks.stream().map(ChunkUtils::chunkPosToLong).collect(Collectors.toList()));
            return set;
        } catch (final Throwable e) {
            if (!hasLoggedFail) {
                XaeroPlus.LOGGER.error("Error: Failed getting WDL chunks", e);
                hasLoggedFail = true;
            }
        }
        return Collections.emptySet();
    }

    public static int getWdlColor() {
        return wdlColor;
    }

    private static final AsyncLoadingCache<RegionRenderPos, List<HighlightAtChunkPos>> regionRenderCache = Caffeine.newBuilder()
            .expireAfterWrite(3000, TimeUnit.MILLISECONDS)
            .refreshAfterWrite(500, TimeUnit.MILLISECONDS)
            .executor(Globals.cacheRefreshExecutorService)
            .buildAsync(key -> loadHighlightChunksAtRegion(key.leafRegionX, key.leafRegionZ, key.level,
                    (chunkPos) -> getSavedChunksWithCache().contains(chunkPos)).call());
    public static List<HighlightAtChunkPos> getSavedChunksInRegion(final int leafRegionX, final int leafRegionZ, final int level) {
        final RegionRenderPos regionRenderPos = new RegionRenderPos(leafRegionX, leafRegionZ, level);
        try {
            CompletableFuture<List<HighlightAtChunkPos>> future = regionRenderCache.get(regionRenderPos);
            if (future.isDone()) {
                return future.get();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            XaeroPlus.LOGGER.error("Error handling WDL region lookup", e);
        }
        return Collections.emptyList();
    }

    public static void setRgbColor(final int color) {
        wdlColor = ColorHelper.getColorWithAlpha(color, (int) XaeroPlusSettingRegistry.wdlAlphaSetting.getValue());
    }

    public static void setAlpha(float a) {
        wdlColor = ColorHelper.getColorWithAlpha(wdlColor, (int) a);
    }
}
