package com.parser;

/**
 * Created by NyLP on 7/12/17.
 */

interface IParserExp<T>
{
    T eval();
}

public interface IParser<T>
{
    IParserExp<T> parse(String expr);
}
