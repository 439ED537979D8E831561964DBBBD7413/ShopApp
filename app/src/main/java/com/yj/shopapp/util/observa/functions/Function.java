package com.yj.shopapp.util.observa.functions;

/**
 * Created on 16/6/4.下午10:21.
 *
 * @author bobomee
 */
public interface Function<Result, Param> {

  Result function(Param... data);
}
