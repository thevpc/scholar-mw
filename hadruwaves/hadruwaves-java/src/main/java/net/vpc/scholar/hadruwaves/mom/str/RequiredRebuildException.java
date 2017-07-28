package net.vpc.scholar.hadruwaves.mom.str;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 13 juin 2004
 * Time: 21:54:07
 * To change this template use File | Settings | File Templates.
 */
public class RequiredRebuildException extends RuntimeException {
    public RequiredRebuildException() {
        super("Vous devez relancer le calcul des coefficient J");
    }
}
