package com.parser;

import com.base.Tuple;

/**
 * Created by NyLP on 7/12/17.
 */

public interface IParser<T>
{
    IParserExp<T> parse(String expr);
}
