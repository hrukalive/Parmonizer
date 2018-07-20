package com.parser;

import com.base.chord.Chord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordSymbolParser implements IParser<Chord.Builder> {
    @Override
    public ChordSymbolExp parse(String expr) {
        /**
         * maj 1 3 5
         * maj7 1 3 (5) 7
         * maj9 1 3 (5) 7 9
         * maj#11 1 3 (5) (7) #11 (9,13..)
         * maj13 1 3 (5) 7 (9) (#11) 13
         * 2 add2 add9 1 3 (5) 9
         * 6 add6 add13 1 3 (5) 6
         * 69 1 3 (5) 6 9
         * maj7#5 1 3 #5 7
         * maj7b6 1 3 (5) (7) (9) (b13), (11)
         *
         * min 1 b3 5
         * min7 1 b3 (5) b7
         * minM7 1 b3 (5) 7 (9, b13, 13)
         * min6 1 b3 (5) 6
         * min9 1 b3 (5) b7 9
         * min11 1 b3 (5) b7 (9) 11
         * min13 1 b3 (5) b7 (9) 13
         * min7b5 1 b3 b5 b7
         * min7b6 1 b3 (5) b6
         *
         * dim 1 b3 b5
         * dim7 1 b3 b5 bb7
         * aug 1 3 #5
         *
         * 7 1 3 (5) b7
         * 9 1 3 (5) b7 9
         * 13 1 3 (5) b7 (9) 13
         * 7b9 1 3 (5) b7 b9 (#9,b5,6..)
         * 7#9 1 3 (5) b7 #9
         * 7#11 1 3 (5) b7 #11 (9,13..)
         * 7alt 1 3 b7 (b9) (b5,b6,#9..)
         * 7sus4 1 4 (5) b7
         * 11sus 1 4 (5) b7 (9) 11
         *
         * sus4 1 4 (5)
         * sus2 1 2 (5)
         * b9sus 1 4 (5) b9
         * 5 1 5
         */
        Pattern pattern_quality = Pattern.compile("([A-G](b|#)?)(5|(69|6|add6|add13|2|add2|add9|maj(7#5|7b6|7|9|13|#4|#11)?)|min(7b5|7b6|7|Maj7|6|9|11|13)?|(dim7|dim|aug)|(7b9|7#9|7#4|7#11|7sus4|7alt|7|9|13|11sus)|(sus4|sus2|b9sus))");


        String[] test = new String("Cmaj\n" +
                "C#maj7\n" +
                "Dbmaj9\n" +
                "Dmaj13\n" +
                "D#2\n" +
                "Ebadd2\n" +
                "Eadd9\n" +
                "D#6\n" +
                "Ebadd6\n" +
                "Eadd13\n" +
                "F69\n" +
                "F#maj#11\n" +
                "Gbmaj#4\n" +
                "Gmaj7#5\n" +
                "G#maj7b6\n" +
                "Abmin\n" +
                "Amin7\n" +
                "A#minMaj7\n" +
                "Bbmin6\n" +
                "Bmin9\n" +
                "B#min11\n" +
                "Cbmin13\n" +
                "Cmin7b5\n" +
                "C#dim\n" +
                "Dbdim7\n" +
                "Dbaug\n" +
                "D5\n" +
                "D#7\n" +
                "Eb7b9\n" +
                "E7#9\n" +
                "E#7#4\n" +
                "Fb7#11\n" +
                "F7sus4\n" +
                "F#7alt\n" +
                "Gb9\n" +
                "G11sus\n" +
                "G#13\n" +
                "Absus4\n" +
                "Asus2\n" +
                "A#b9sus").split("\n");

        for (String str : test) {
            Matcher matcher_quality = pattern_quality.matcher(str);
            if (matcher_quality.find()) {
                System.out.println(matcher_quality.group(1) + matcher_quality.group(3));
            }
        }
        return null;
    }
}
