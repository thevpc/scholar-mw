/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1;

import net.vpc.lib.pheromone.application.Application;
import net.vpc.lib.pheromone.application.DefaultApplicationRenderer;
import net.vpc.lib.pheromone.application.DefaultApplicationRendererComponentsFactory;

/**
 *
 * @author vpc
 */
public class TMWLabApplicationRenderer extends DefaultApplicationRenderer {

    public TMWLabApplicationRenderer(Application app) {
        super(app);
        setComponentsFactory(new DefaultApplicationRendererComponentsFactory(this));
        addComponentsConfigurator(new TMWLabApplicationRendererConfigurator(this));
    }

}
