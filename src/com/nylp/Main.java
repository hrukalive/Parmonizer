package com.nylp;

import com.base.Chord;
import com.base.Progression;
import com.parser.ChordExpParser;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        ArrayList<Chord> chords = new ArrayList<>();
        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:2=Eb;3=C][i:4=Ab5]").eval());
        chords.add(new ChordExpParser().parse("[*Eb,G,Bb][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(200);T(50);T(200)][o:F;F;F][u:100;200;300][f:][i:]").eval());
        chords.add(new ChordExpParser().parse("[Ab,C,*Eb][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(100);T(200)][o:F;F;F][u:100;200;300][f:][i:]").eval());
        chords.add(new ChordExpParser().parse("[*F,Ab,C,Eb][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200);F][o:F;F;T(500);F][u:500;200;300][f:][i:]").eval());
        chords.add(new ChordExpParser().parse("[*G,B,D,F][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3);4\\>m2;4\\>M2][b:][r:T(50);F;T(200);F][o:F;F;T(500);F][u:500;200;300][f:4=G][i:]").eval());
        chords.add(new ChordExpParser().parse("[*Ab,C,Eb][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(100);T(200)][o:F;F;F][u:100;200;300][f:][i:]").eval());
        chords.add(new ChordExpParser().parse("[D,*F,Ab][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(500);T(50);T(1000)][o:F;F;F][u:100;200;300][f:4=F][i:]").eval());
        chords.add(new ChordExpParser().parse("[C,Eb,*G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:1\\>m2;1\\>M2;2\\>m2;2\\>M2][b:][r:T(500);T(500);T(50)][o:F;F;F][u:100;200;300][f:4=Eb][i:]").eval());
        chords.add(new ChordExpParser().parse("[*G,B,D;F][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3)][b:][r:T(50);F;T(200)][o:F;F;T(500)][u:500;200;300][f:][i:]").eval());
        chords.add(new ChordExpParser().parse("[*C,Eb,G][4][E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300][f:4=C][i:]").eval());

//        chords.add(new ChordExpParser().build("[*C,E,G][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300;400][f:5=C][i:]").eval());
//        chords.add(new ChordExpParser().build("[*E,G,B][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(200);T(50);T(200)][o:F;F;F][u:100;200;300;400][f:5=B][i:]").eval());
//        chords.add(new ChordExpParser().build("[*A,C,E][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(100);T(200)][o:F;F;F][u:100;200;300;400][f:5=A][i:]").eval());
//        chords.add(new ChordExpParser().build("[C,*E,G][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300;400][f:5=G][i:]").eval());
//        chords.add(new ChordExpParser().build("[Ab,C,*Eb][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(50);T(200)][o:F;F;F][u:100;200;300;400][f:][i:]").eval());
//        chords.add(new ChordExpParser().build("[D,*F,Ab,C][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(500);T(50);T(1000);F][o:F;F;T(500);F][u:100;200;300;400][f:][i:]").eval());
//        chords.add(new ChordExpParser().build("[*F#,A,C,Eb][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:4/>P1;4/>m2;4/>M2;4\\>m2;4\\>M2][t:2/>m2;(2\\>M2);3\\>m2;4\\>m2][b:][r:F;F;T(200);F][o:F;F;T(500);F][u:500;200;300;400][f:][i:]").eval());
//        chords.add(new ChordExpParser().build("[D,*F#,A,C][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3);4\\>m2;4\\>M2][b:][r:T(50);F;T(200);F][o:F;F;T(500);F][u:500;200;300;400][f:][i:]").eval());
//        chords.add(new ChordExpParser().build("[*G,B,D,F][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:2/>m2;(2\\>M3);4\\>m2;4\\>M2][b:][r:T(50);F;T(200);F][o:F;F;T(500);F][u:500;200;300;400][f:][i:]").eval());
//        chords.add(new ChordExpParser().build("[*C,E,G][5][C1:C4;E2:E4;B2:A4;F3:E5;C4:C6][p:][t:][b:][r:T(50);T(500);T(200)][o:F;F;T(500)][u:100;200;300;400][f:5=C][i:]").eval());
        
        Progression prog = new Progression();
        for (Chord chord : chords)
            prog.addHarmony(chord);
        prog.yield();
        System.out.println(prog.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++)
        {
            System.out.println(prog.getPieces().get(i) + " loss: " + prog.getPieces().get(i).getLoss());
            prog.getPieces().get(i).play();
        }
    }
}
