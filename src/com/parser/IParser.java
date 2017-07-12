package com.parser;

/**
 * Created by NyLP on 7/12/17.
 */

interface ParserExp<T>
{
    T eval();
}

public interface IParser<T>
{
    ParserExp<T> parse(String expr);
}
