/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.jmodemtest;

import com.erhannis.mathnstuff.MeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

// https://stackoverflow.com/a/32891220/513038
public class RawAudioPlay {

    public static void play(InputStream is) {
        try {
            // select audio format parameters
            AudioFormat af = new AudioFormat(24000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            // generate some PCM data (a sine wave for simplicity)

            // prepare audio output
            line.open(af, 4096);
            line.start();

            byte[] buf = new byte[256];
            int wrote = 0;
            while ((wrote = is.read(buf)) >= 0) {
                line.write(buf, 0, wrote);
            }

            // shut down audio
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
