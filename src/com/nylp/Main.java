package com.nylp;

import com.base.chord.Chord;
import com.base.Progression;
import com.base.realization.ChordVoicing;
import com.parser.ChordExpParser;
import com.utils.VoiceLeadingPlayer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        ArrayList<ChordVoicing> chords = new ArrayList<>();

        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());
        chords.add(new ChordExpParser().parse("[G,*Bb,D][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3)][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:4=D][i:]").eval());
        chords.add(new ChordExpParser().parse("[F,*Ab,C][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());
        chords.add(new ChordExpParser().parse("[*Ab,C,F#][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1\\>m2;3/>m2][b:][r:F;T(500);F][o:F;F;F][u:100;200;300][f:4=C][i:]").eval());
        chords.add(new ChordExpParser().parse("[*G,B,D][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:4=B][i:]").eval());
        chords.add(new ChordExpParser().parse("[G,B,D,*F][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3);4\\>m2;4\\>M2][b:][r:T(50);F;T(200);F][o:F;F;T(500);F][u:500;200;300][f:4=D][i:]").eval());
        chords.add(new ChordExpParser().parse("[C,*Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=Eb][i:]").eval());
        chords.add(new ChordExpParser().parse("[B,*D,F,Ab][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1/>m2;4\\>m2;(4\\>M2)][b:][r:F;F;F;F][o:F;F;F;F][u:100;200;300][f:4=B][i:]").eval());
        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());
        chords.add(new ChordExpParser().parse("[D,*F,Ab,C][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:4\\>m2;(4\\>M2)][b:][r:T(500);T(50);T(1000);F][o:F;F;F;F][u:100;200;300][f:4=C][i:]").eval());
        chords.add(new ChordExpParser().parse("[C,Eb,*G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1\\>m2;1\\>M2;2\\>m2;2\\>M2][b:][r:T(500);T(500);T(50)][o:F;F;F][u:100;200;300][f:4=C][i:1=G3]").eval());
        chords.add(new ChordExpParser().parse("[*G,B,D][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3)][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:4=B][i:1=G2]").eval());
        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:1=C3]").eval());
        
        Progression prog = new Progression();
        for (ChordVoicing chord : chords)
            prog.addHarmony(chord);
        prog.yield();
        System.out.println(prog.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++)
        {
            System.out.println(prog.getPieces().get(i) + " loss: " + prog.getPieces().get(i).getLoss());
            VoiceLeadingPlayer.play(prog.getPieces().get(i));
        }
    }
}
