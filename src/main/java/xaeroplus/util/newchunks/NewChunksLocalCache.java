package xaeroplus.util.newchunks;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import xaeroplus.util.highlights.ChunkHighlightLocalCache;
import xaeroplus.util.highlights.HighlightAtChunkPos;

import java.util.Collections;
import java.util.List;

import static xaeroplus.util.ChunkUtils.getActualDimension;

public class NewChunksLocalCache implements NewChunksCache {
    private final ChunkHighlightLocalCache delegate = new ChunkHighlightLocalCache();

    @Override
    public void addNewChunk(final int x, final int z) {
        delegate.addHighlight(x, z);
    }

    @Override
    public boolean isNewChunk(final int chunkPosX, final int chunkPosZ, final RegistryKey<World> dimensionId) {
        if (dimensionId != getActualDimension()) return false;
        return delegate.isHighlighted(chunkPosX, chunkPosZ);
    }

    @Override
    public List<HighlightAtChunkPos> getNewChunksInRegion(final int leafRegionX, final int leafRegionZ, final int level, final RegistryKey<World> dimension) {
        if (dimension != getActualDimension()) return Collections.emptyList();
        return delegate.getHighlightsInRegion(leafRegionX, leafRegionZ, level);
    }

    @Override
    public void handleWorldChange() {
        delegate.handleWorldChange();
    }

    @Override
    public void handleTick() {
        delegate.handleTick();
    }

    @Override
    public void onEnable() {
        delegate.onEnable();
    }

    @Override
    public void onDisable() {
        delegate.onDisable();
    }

    @Override
    public Long2LongMap getNewChunksState() {
        return delegate.getHighlightsState();
    }

    @Override
    public void loadPreviousState(final Long2LongMap state) {
        delegate.loadPreviousState(state);
    }
}
