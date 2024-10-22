package org.example;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;



public class Sound {

    private static int ditDuration = 100;
    private static int ditFrequency = 800;
    private static int dahDuration = 250;
    private static int dahFrequency = 800;

    public static void playDit() throws LineUnavailableException {
        playTone(ditFrequency,ditDuration);
    }

    public static void playDah() throws LineUnavailableException {
        playTone(dahFrequency,dahDuration);
    }

    public static void playTone(int frequency, int duration) throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(44100,8,1,true,false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(audioFormat);

        FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (float) ((range * (User.getVolume() / 100.0f)) + volumeControl.getMinimum());
        volumeControl.setValue(gain);

        sourceDataLine.start();

        byte[] data = new byte[duration * 44100 /1000];

        for(int i = 0; i < data.length; i++){
            double angle = i / (44100.0/frequency) * 2.0 * Math.PI;
            data[i] = (byte) (Math.sin(angle) * 127 +128);
        }
        sourceDataLine.write(data,0, data.length);
        sourceDataLine.drain();
        sourceDataLine.close();
    }

}
