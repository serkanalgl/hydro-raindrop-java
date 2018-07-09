package com.github.serkanalgl.hydro.raindrop;

import com.github.serkanalgl.hydro.raindrop.model.BaseResponse;
import com.github.serkanalgl.hydro.raindrop.model.VerifySignature;

/**
 * Created by serkanalgul on 4.07.2018.
 */
public interface RaindropClient extends RaindropShared {

    BaseResponse registerUser(String hydroId) throws RaindropException;

    BaseResponse deleteUser(String hydroId) throws RaindropException;

    VerifySignature verifySignature(String hydroId, Integer message) throws RaindropException;

    Integer generateMessage() throws RaindropException;


}
