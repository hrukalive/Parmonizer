package com.parser;

import com.base.Progression;

/**
 * Created by NyLP on 7/14/17.
 */

public class ProgressionExp implements IParserExp<Progression>
{
    private Progression progression = null;
    public ProgressionExp(Progression progression)
    {
        this.progression = progression;
    }
    @Override public Progression eval()
    {
        return progression;
    }
    @Override public String remaining()
    {
        return "";
    }
}
