/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.jmodemtest;

import com.erhannis.mathnstuff.MeUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import jmodem.InputSampleStream;
import jmodem.OutputSampleStream;

/**
 *
 * @author erhannis
 */
public class Main {

    public static void main(String[] args) throws IOException {
        InputStream is = MeUtils.incrementalStream(o -> {
            try {
                while (true) {
                    byte[] buffer = new byte[64];
                    double step = Math.PI / buffer.length;
                    double angle = Math.PI * 2;
                    int i = buffer.length;
                    while (i > 0) {
                        double sine = Math.sin(angle);
                        int sample = (int) Math.round(sine * 32767);
                        buffer[--i] = (byte) (sample >> 8);
                        buffer[--i] = (byte) sample;
                        angle -= step;
                    }
                    o.write(buffer);
                }
            } catch (IOException ex) {
                Logger.getLogger(RawAudioPlay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        RawAudioPlay.play(is);
        
//        jmodem.Main.send(new ByteArrayInputStream("This is a test".getBytes()), new OutputSampleStream() {
//            double max = 0;
//            double min = 0;
//            @Override
//            public void write(double sample) throws IOException {
//                if (sample > max) {
//                    max = sample;
//                    System.out.println(sample+"");
//                }
//                if (sample < min) {
//                    min = sample;
//                    System.out.println(sample+"");
//                }
//            }
//        });
        
        
//        try {
//            Clip clip = AudioSystem.getClip();
//            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
//                    Main.class.getResourceAsStream("/path/to/sounds/" + url));
//            clip.open(inputStream);
//            clip.start();
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
    }
}
