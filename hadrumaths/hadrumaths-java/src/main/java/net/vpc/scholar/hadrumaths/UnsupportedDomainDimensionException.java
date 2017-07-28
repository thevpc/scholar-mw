package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 8/24/14.
 */
public class UnsupportedDomainDimensionException extends RuntimeException{
    public UnsupportedDomainDimensionException() {
        super("Unsupported domain dimension. Should be 1, 2 or 3");
    }

    public UnsupportedDomainDimensionException(int d) {
        super("Unsupported domain dimension "+d+". Should be 1, 2 or 3");
    }
}
