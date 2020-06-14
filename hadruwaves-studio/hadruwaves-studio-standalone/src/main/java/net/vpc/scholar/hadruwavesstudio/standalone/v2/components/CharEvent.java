package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import java.awt.*;

public class CharEvent extends Event {
    public CharEvent(Object target, CharCommand arg) {
        super(target, 0, arg);
    }
    public CharCommand getCharCommand(){
        return (CharCommand) arg;
    }
}
