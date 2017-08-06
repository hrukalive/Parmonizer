package com.parser;

/**
 * Created by NyLP on 7/14/17.
 */

public interface IParserExp<T>
{
    T eval();
    String remaining();
}
