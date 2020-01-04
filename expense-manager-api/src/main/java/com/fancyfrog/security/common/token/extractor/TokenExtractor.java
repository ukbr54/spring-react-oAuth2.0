package com.fancyfrog.security.common.token.extractor;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */
public interface TokenExtractor {

    String extract(String header);
}
