package com.github.serkanalgl.hydro.raindrop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by serkanalgul on 4.07.2018.
 */
public final class Raindrop {

    private static final Logger logger = LoggerFactory.getLogger(Raindrop.class);

    public RaindropClient client(RaindropPartnerConfig config) {
        return new RaindropClientImpl(config);
    }

    public RaindropServer server(RaindropPartnerConfig config) {
        return new RaindropServerImpl(config);
    }

    private void validate(RaindropPartnerConfig config) {
        if (config == null) throw new IllegalArgumentException("RaindropPartnerConfig parameter can not be null");
    }
}
