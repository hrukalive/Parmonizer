package com.nylp;

import com.base.interval.Interval;
import com.base.Note;
import com.base.Progression;
import com.base.progression.VoiceConfig;
import com.base.realization.ChordVoicing;
import com.base.scale.Scale;
import com.parser.ChordSymbolParser;
import com.utils.VoiceLeadingPlayer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        ChordSymbolParser parser = new ChordSymbolParser();
        parser.parse("");
        ArrayList<Interval> intervalSteps = new ArrayList<>();
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.m2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.m2);
        Scale scale = new Scale(Note.parse("C"), intervalSteps);
//        System.out.println(scale.getMode(0).chord(0, 3, 7));
//        System.out.println(scale.getMode(0).negativeChord(4, 3, 7));
//        ArrayList<VoiceConfig> vs = new ArrayList<>();
//        vs.add(new VoiceConfig(Note.parse("E2"), Note.parse("E4"), scale.getMode(0).chord(0, 3, 3).getBass(), null, 50));
//        vs.add(new VoiceConfig(Note.parse("B2"), Note.parse("A4"), null, null, 20));
//        vs.add(new VoiceConfig(Note.parse("F3"), Note.parse("E5"), null, null, 30));
//        vs.add(new VoiceConfig(Note.parse("C4"), Note.parse("C6"), null, null, 0));
//        ChordVoicing voicing1 = new ChordVoicing(scale.getMode(0).chord(0, 3, 3), vs);
//        ArrayList<VoiceConfig> vs2 = new ArrayList<>();
//        vs2.add(new VoiceConfig(Note.parse("E2"), Note.parse("E4"), scale.getMode(0).chord(3, 3, 4).getBass(), null, 50));
//        vs2.add(new VoiceConfig(Note.parse("B2"), Note.parse("A4"), null, null, 20));
//        vs2.add(new VoiceConfig(Note.parse("F3"), Note.parse("E5"), null, null, 30));
//        vs2.add(new VoiceConfig(Note.parse("C4"), Note.parse("C6"), null, null, 0));
//        ChordVoicing voicing2 = new ChordVoicing(scale.getMode(0).chord(3, 3, 4), vs2);
//        ArrayList<VoiceConfig> vs3 = new ArrayList<>();
//        vs3.add(new VoiceConfig(Note.parse("E2"), Note.parse("E4"), scale.getMode(0).chord(4, 3, 4).getBass(), null, 50));
//        vs3.add(new VoiceConfig(Note.parse("B2"), Note.parse("A4"), null, null, 20));
//        vs3.add(new VoiceConfig(Note.parse("F3"), Note.parse("E5"), null, null, 30));
//        vs3.add(new VoiceConfig(Note.parse("C4"), Note.parse("C6"), null, null, 0));
//        ChordVoicing voicing3 = new ChordVoicing(scale.getMode(0).chord(4, 3, 4), vs3);
//        ChordVoicing voicing4 = new ChordVoicing(scale.getMode(0).chord(0, 3, 3), vs);

//        ArrayList<ChordVoicing> chords = new ArrayList<>();
//
//        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());
//        chords.add(new ChordExpParser().parse("[G,*Bb,D][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3)][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:4=D][i:]").eval());
//        chords.add(new ChordExpParser().parse("[F,*Ab,C][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());
//        chords.add(new ChordExpParser().parse("[*Ab,C,F#][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1\\>m2;3/>m2][b:][r:F;T(500);F][o:F;F;F][u:100;200;300][f:4=C][i:]").eval());
//        chords.add(new ChordExpParser().parse("[*G,B,D][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:4=B][i:]").eval());
//        chords.add(new ChordExpParser().parse("[G,B,D,*F][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3);4\\>m2;4\\>M2][b:][r:T(50);F;T(200);F][o:F;F;T(500);F][u:500;200;300][f:4=D][i:]").eval());
//        chords.add(new ChordExpParser().parse("[C,*Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=Eb][i:]").eval());
//        chords.add(new ChordExpParser().parse("[B,*D,F,Ab][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1/>m2;4\\>m2;(4\\>M2)][b:][r:F;F;F;F][o:F;F;F;F][u:100;200;300][f:4=B][i:]").eval());
//        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());
//        chords.add(new ChordExpParser().parse("[D,*F,Ab,C][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:4\\>m2;(4\\>M2)][b:][r:T(500);T(50);T(1000);F][o:F;F;F;F][u:100;200;300][f:4=C][i:]").eval());
//        chords.add(new ChordExpParser().parse("[C,Eb,*G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1\\>m2;1\\>M2;2\\>m2;2\\>M2][b:][r:T(500);T(500);T(50)][o:F;F;F][u:100;200;300][f:4=C][i:1=G3]").eval());
//        chords.add(new ChordExpParser().parse("[*G,B,D][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3)][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:4=B][i:1=G2]").eval());
//        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:1=C3]").eval());
//
//        Progression prog = new Progression();
//        prog.addHarmony(voicing1);
//        prog.addHarmony(voicing2);
//        prog.addHarmony(voicing3);
//        prog.addHarmony(voicing4);
//        for (ChordVoicing chord : chords)
//            prog.addHarmony(chord);
//        prog.yield();
//        System.out.println(prog.getPieces().size() + " realizations generated");
//        for (int i = 0; i < 4; i++)
//        {
//            System.out.println(prog.getPieces().get(i) + " loss: " + prog.getPieces().get(i).getLoss());
//            VoiceLeadingPlayer.play(prog.getPieces().get(i));
//        }
    }
}
