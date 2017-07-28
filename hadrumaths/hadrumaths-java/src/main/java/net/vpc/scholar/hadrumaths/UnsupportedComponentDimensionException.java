package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 8/24/14.
 */
public class UnsupportedComponentDimensionException extends RuntimeException{
    public UnsupportedComponentDimensionException() {
        super("Unsupported component dimension. Should be 1, 2 or 3");
    }

    public UnsupportedComponentDimensionException(ComponentDimension d) {
        super("Unsupported component dimension "+d+". Should be 1, 2 or 3");
    }

    public UnsupportedComponentDimensionException(int d) {
        super("Unsupported component dimension "+d+". Should be 1, 2 or 3");
    }
}
