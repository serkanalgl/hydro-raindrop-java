package com.github.serkanalgl.hydro.raindrop;

import com.github.serkanalgl.hydro.raindrop.model.Authenticate;
import com.github.serkanalgl.hydro.raindrop.model.Challenge;
import com.github.serkanalgl.hydro.raindrop.model.Whitelist;

/**
 * Created by serkanalgul on 4.07.2018.
 */
public interface RaindropServer extends RaindropShared {

    Whitelist whitelist(String address) throws RaindropException;

    Challenge challenge(String hydroAddressId) throws RaindropException;

    Authenticate authenticate(String hydroAddressId) throws RaindropException;

}
