package com.afterpay.frauddetector.mapper;

import org.mapstruct.MapperConfig;

@MapperConfig
public interface BaseMapper<S, T> {

    T mapSourceToTarget(S source);
}
