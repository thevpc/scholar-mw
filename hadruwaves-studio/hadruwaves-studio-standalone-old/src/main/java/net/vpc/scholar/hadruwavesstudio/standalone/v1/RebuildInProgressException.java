package net.thevpc.scholar.hadruwavesstudio.standalone.v1;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 13 juin 2004
 * Time: 21:54:07
 * To change this template use File | Settings | File Templates.
 */
public class RebuildInProgressException extends RuntimeException {
    public RebuildInProgressException() {
        super("le calcul des coefficient J est en cours");
    }
}
