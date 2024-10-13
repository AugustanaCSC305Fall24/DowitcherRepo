package org.example;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    public static Clip audioClip;

    // for the string use "/dit.wav" for dit and "/dah.wav" for dah
    public static void playDitOrDah(String file) {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();  //find a mixer that can play the audio clip
        Clip audioClip = null;
        for (Mixer.Info info : mixerInfo) {
            Mixer mixer = AudioSystem.getMixer(info);
            DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);

            if (mixer.isLineSupported(dataInfo)) {
                try {
                    audioClip = (Clip) mixer.getLine(dataInfo);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            URL soundURL = Sound.class.getResource(file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            audioClip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return;
        }
        audioClip.start();
        do {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (audioClip.isActive());
        audioClip.close();
    }

}
