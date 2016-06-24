package org.logstash.ackedqueue;

import java.util.HashMap;
import java.util.Map;

public class VolatilePageHandler extends PageHandler {
    private Map<Long, Page> livePages;

    public VolatilePageHandler(int pageSize) {
        super(pageSize);

        // TODO: ajust when meta will be persisted & retrieved
        this.meta = new VolatileMetadata();
        this.meta.setPageSize(this.pageSize);
        this.meta.setHeadPageIndex(0);
        this.meta.setHeadPageOffset(0);
        this.meta.setUnackedTailPageIndex(0);
        this.meta.setUnusedTailPageIndex(0);

        this.livePages = new HashMap<>();
    }

    // pages opening/caching strategy
    // @param index the page index to retrieve
    protected Page page(long index) {
        // TODO: adjust implementation for correct live pages handling
        // TODO: extract page caching in a separate class?

        Page p = this.livePages.get(index);
        if (p != null) {
            return p;
        }

        p = new MemoryPage(this.pageSize, index);
        this.livePages.put(index, p);
        return p;
    }
}
