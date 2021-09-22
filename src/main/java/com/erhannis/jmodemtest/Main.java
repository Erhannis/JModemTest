/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.jmodemtest;

import com.erhannis.mathnstuff.MeMath;
import com.erhannis.mathnstuff.MeUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import jmodem.InputSampleStream;
import jmodem.OutputSampleStream;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.MathArrays;

/**
 *
 * @author erhannis
 */
public class Main {
    
    public static void main(String[] args) throws IOException {
        play(MeUtils.incrementalStream(1024*8, os -> {
            try {
                InputStream is = record();
                final int L = 256*4;
                byte[] b = new byte[L*2];
                final double[] zeros = new double[L];
                double[][] d = new double[2][L];
                while (true) {
                    is.read(b);
                    byteArrayToDoubleArray(b, d[0]);
                    System.arraycopy(zeros, 0, d[1], 0, zeros.length);
                    FastFourierTransformer.transformInPlace(d,  DftNormalization.STANDARD, TransformType.FORWARD);
                    final double F = 0.99;
                    for (int i = 80; i < 90; i++) {
                        d[0][i] *= F;
                        d[1][i] *= F;
                        d[0][L-1-i] *= F;
                        d[1][L-1-i] *= F;
                    }
                    FastFourierTransformer.transformInPlace(d, DftNormalization.STANDARD, TransformType.INVERSE);
                    doubleArrayToByteArray(d[0], b);
                    os.write(b);
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
        
        if (1==1) return;
        
        byte[] bytes = "This is a test of the automated garbage collection chutes and ladders of every shape and size and colors of the rainbows of platinum raining from the every face and bringing the beautiful sun to life and liberty and the pursuit of greedy pots of cereal".getBytes();
//        byte[] bytes = new byte[100];
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = 0b00000001;
//        }
        final int SYMBOL_LEN = 2;
        InputStream is = MeUtils.incrementalStream(o -> {
            try {
//                while (true) {
//                    byte[] buffer = new byte[64];
//                    double step = Math.PI / buffer.length;
//                    double angle = Math.PI * 2;
//                    int i = buffer.length;
//                    while (i > 0) {
//                        double sine = Math.sin(angle);
//                        int sample = (int) Math.round(sine * 32767);
//                        buffer[--i] = (byte) (sample >> 8);
//                        buffer[--i] = (byte) sample;
//                        angle -= step;
//                    }
//                    o.write(buffer);
//                }

//                byte[] buffer = new byte[2*3*SYMBOL_LEN*8];                
//                for (byte b : bytes) {
//                    int k = 0;
//                    for (int i = 0; i < 8; i++) {
//                        if ((b & (1<<i)) > 0) {
//                            for (int j = 0; j < SYMBOL_LEN; j++) {
//                                buffer[k++] = (byte)0x00;
//                                buffer[k++] = (byte)0x80;
//                            }
//                            for (int j = 0; j < SYMBOL_LEN*2; j++) {
//                                buffer[k++] = (byte)0x00;
//                                buffer[k++] = (byte)0x00;
//                            }
//                        } else {
//                            for (int j = 0; j < SYMBOL_LEN*2; j++) {
//                                buffer[k++] = (byte)0x00;
//                                buffer[k++] = (byte)0x80;
//                            }
//                            for (int j = 0; j < SYMBOL_LEN; j++) {
//                                buffer[k++] = (byte)0x00;
//                                buffer[k++] = (byte)0x00;
//                            }
//                        }
//                    }
//                    o.write(buffer);
//                }

//                byte[] buffer = new byte[1000];
//                for (int i = 0; i < 100; i++) {
//                    if (i % 2 == 0) {
//                        buffer[0] = (byte)0x00;
//                        buffer[1] = (byte)0x80;
//                        buffer[2] = (byte)0x00;
//                        buffer[3] = (byte)0x80;
//                    } else {
//                        buffer[0] = (byte)0xFF;
//                        buffer[1] = (byte)0x7F;
//                        buffer[2] = (byte)0xFF;
//                        buffer[3] = (byte)0x7F;
//                    }
//                    o.write(buffer);
//                }

                byte[] buffer = new byte[200];
                for (int i = 0; i < 100; i++) {
                    if (i % 3 == 0) {
                        for (int j = 0; j < buffer.length/2; j++) {
                            buffer[j*2+0] = (byte)0x00;
                            buffer[j*2+1] = (byte)0x80;
                        }
                    } else {
                        for (int j = 0; j < buffer.length/2; j++) {
                            buffer[j*2+0] = (byte)0xFF;
                            buffer[j*2+1] = (byte)0x7F;
                        }
                    }
                    o.write(buffer);
                }
                
//                jmodem.Main.send(new ByteArrayInputStream("This is a test of the automated garbage collection chutes and ladders of every shape and size and colors of the rainbows of platinum raining from the every face and bringing the beautiful sun to life and liberty and the pursuit of greedy pots of cereal".getBytes()), new OutputSampleStream() {
//                    double max = 0;
//                    double min = 0;
//
//                    @Override
//                    public void write(double sample) throws IOException {
//                        int s = (int)(((sample+1) / 2)*0xFFFF);
//                        int h = (s >> 8);
//                        int l = (s & 0xFF);
//                        System.out.println(sample + " -> " + s + " -> " + h + " : " + l);
//                        o.write(l);
//                        o.write(h);
//                    }
//                });

            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        play(is);

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

    private static final Object LINE_LOCK = new Object();
    
    // https://stackoverflow.com/a/32891220/513038
    public static void play(InputStream is) {
        try {
            // select audio format parameters
            AudioFormat af = new AudioFormat(8000, 16, 1, true, false);
            SourceDataLine line;
            synchronized (LINE_LOCK) {
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
                line = (SourceDataLine) AudioSystem.getLine(info);

                // generate some PCM data (a sine wave for simplicity)

                // prepare audio output
                line.open(af, 4096);
                line.start();
            }

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

    // https://stackoverflow.com/a/32891220/513038
    public static InputStream record() {
        return MeUtils.incrementalStream(1024*16, os -> {
        try {
            // select audio format parameters
            AudioFormat af = new AudioFormat(8000, 16, 1, true, false);
            TargetDataLine line;
            synchronized (LINE_LOCK) {
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
                line = (TargetDataLine) AudioSystem.getLine(info);

                // generate some PCM data (a sine wave for simplicity)

                // prepare audio output
                line.open(af, 4096);
                line.start();
            }
            
            byte[] buf = new byte[256];
            int wrote = 0;
            while ((wrote = line.read(buf, 0, buf.length)) >= 0) {
                os.write(buf, 0, wrote);
            }

            // shut down audio
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        });
    }
    
    public static double[][] deconvolve(double[][] combined, double[][] blip) {
        FastFourierTransformer.transformInPlace(combined, DftNormalization.STANDARD, TransformType.FORWARD);
        FastFourierTransformer.transformInPlace(blip, DftNormalization.STANDARD, TransformType.FORWARD);
        double[][] result = new double[2][0];
        result[0] = MathArrays.ebeDivide(combined[0], blip[0]);
        result[1] = MathArrays.ebeDivide(combined[1], blip[1]);
        FastFourierTransformer.transformInPlace(result, DftNormalization.STANDARD, TransformType.INVERSE);
        return result;
    }
    
    //TODO WARNING - These stop being right if certain audio format params change
    
    public static double bytesToDouble(byte a, byte b) {
        return ((a << 8) + b) / 32767.0;
    }

    public static double[] byteArrayToDoubleArray(byte[] b) {
        double[] result = new double[b.length/2];
        byteArrayToDoubleArray(b, result);
        return result;
    }
    
    public static void byteArrayToDoubleArray(byte[] b, double[] d) {
        for (int i = 0; i < d.length; i++) {
            d[i] = ((b[i*2] << 8) + b[i*2+1]) / 32767.0;
        }
    }
    
    public static byte[] doubleToBytes(double d) {
        int sample = (int) (d * 32767);
        return new byte[]{(byte) (sample >> 8), (byte) sample};
    }
    
    public static byte[] doubleArrayToByteArray(double[] d) {
        byte[] result = new byte[d.length*2];
        doubleArrayToByteArray(d, result);
        return result;
    }
    
    public static void doubleArrayToByteArray(double[] d, byte[] b) {
        for (int i = 0; i < d.length; i++) {
            int sample = (int) (d[i] * 32767);
            b[i*2+0] = (byte) (sample >> 8);
            b[i*2+1] = (byte) sample;
        }
    }
    
    public static byte doubleToBA(double d) {
        return (byte)(((int) (d * 32767)) >> 8);
    }
    
    public static byte doubleToBB(double d) {
        return (byte)((int) (d * 32767));
    }
}
