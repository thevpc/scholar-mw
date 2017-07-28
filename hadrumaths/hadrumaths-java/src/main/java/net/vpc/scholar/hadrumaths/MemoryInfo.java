package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/24/17.
 */
public final class MemoryInfo {
    private final long maxMemory;
    private final long totalMemory;
    private final long freeMemory;

    public MemoryInfo() {
        Runtime rt = Runtime.getRuntime();
        maxMemory = rt.maxMemory();
        totalMemory = rt.totalMemory();
        freeMemory = rt.freeMemory();
    }

//    public MemoryInfo(long maxMemory, long totalMemory, long freeMemory) {
//        this.maxMemory = maxMemory;
//        this.totalMemory = totalMemory;
//        this.freeMemory = freeMemory;
//    }

    public MemoryUsage diff(MemoryInfo other) {
        if (other == null) {
            return new MemoryUsage(
                    maxMemory,
                    totalMemory,
                    freeMemory,
                    inUseMemory()
            );
        }
        return new MemoryUsage(
                maxMemory,
                totalMemory,
                freeMemory - other.freeMemory,
                inUseMemory() - other.inUseMemory()
        );
    }

    public long maxFreeMemory() {
        return maxMemory - (totalMemory - freeMemory);
    }

    public long inUseMemory() {
        return totalMemory - freeMemory;
    }

    public long maxMemory() {
        return maxMemory;
    }

    public long totalMemory() {
        return totalMemory;
    }

    public long freeMemory() {
        return freeMemory;
    }


    public String toString() {
        return
                "free : " + Maths.formatMemory(freeMemory()) + " ; "
                        + "total : " + Maths.formatMemory(totalMemory()) + " ; "
                        + "max : " + Maths.formatMemory(maxMemory()) + " ; "
                        + "inUse : " + Maths.formatMemory(inUseMemory()) + " ; "
                        + "max Free : " + Maths.formatMemory(maxFreeMemory())
                ;
    }
}
