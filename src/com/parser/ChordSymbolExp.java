package com.parser;

import com.base.chord.Chord;

public class ChordSymbolExp implements IParserExp<Chord.Builder> {
    private final Chord.Builder builder;
    public ChordSymbolExp(Chord.Builder builder) {
        this.builder = builder;
    }
    @Override public Chord.Builder eval()
    {
        return builder;
    }
    @Override public String remaining()
    {
        return "";
    }
}
