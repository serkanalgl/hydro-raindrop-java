package com.github.serkanalgl.hydro.raindrop;

import com.github.serkanalgl.hydro.raindrop.model.TransactionStatus;

/**
 * Created by serkanalgul on 8.07.2018.
 */
public interface RaindropShared {
    TransactionStatus transactionStatus(String transactionHash) throws RaindropException;
}
