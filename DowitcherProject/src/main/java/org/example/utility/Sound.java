package org.example.utility;

import javafx.fxml.FXML;
import org.example.data.User;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sound {

    private static int ditDuration = 75;
    private static int ditFrequency = 600;
    private static int dahDuration = 225;
    private static int dahFrequency = 600;

    private static SourceDataLine staticSourceLine; // **New field for persistent sound line**

    private Boolean isPaused = true;
    private int pauseIndex;
    private Thread audioThread;
    private volatile boolean stopAudio = false;

    public static void playDit() throws LineUnavailableException {
        playTone(ditFrequency,ditDuration);
    }

    public static void playDit(int frequency) throws LineUnavailableException {
        playTone(frequency,ditDuration);
    }

    public static void playDah() throws LineUnavailableException {
        playTone(dahFrequency,dahDuration);
    }

    public static void playDah(int frequency) throws LineUnavailableException {
        playTone(frequency,dahDuration);
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

    public static void playStraitTone(int frequency, boolean isPlaying) throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(44100,8,1,true,false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(audioFormat);

        FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (float) ((range * (User.getVolume() / 100.0f)) + volumeControl.getMinimum());
        volumeControl.setValue(gain);

        sourceDataLine.start();

        byte[] data = new byte[44100];
        while (isPlaying){
            for(int i = 0; i < data.length; i++){
                double angle = i / (44100.0/frequency) * 2.0 * Math.PI;
                data[i] = (byte) (Math.sin(angle) * 127 +128);
            }
        }

        sourceDataLine.write(data,0, data.length);
        sourceDataLine.drain();
        sourceDataLine.close();
    }

    public static void staticSound(double sliderVolume, boolean isPlaying) throws LineUnavailableException {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);

        sourceDataLine.open(format);
        sourceDataLine.start();
        adjustVolumeOfStatic(sliderVolume);

        byte[] data = new byte[1024];
        while(isPlaying) {
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (Math.random() * 256 - 128);
            }

            sourceDataLine.write(data, 0, data.length);
        }
        sourceDataLine.drain();
        sourceDataLine.close();
    }

    // *** New: Start static playback ***
    public static void startStaticSound(double sliderVolume) throws LineUnavailableException {
        if (staticSourceLine != null && staticSourceLine.isOpen()) {
            return; // Avoid reopening if already playing
        }
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        staticSourceLine = (SourceDataLine) AudioSystem.getLine(info);

        staticSourceLine.open(format);
        adjustVolumeOfStatic(sliderVolume); // Set initial volume
        staticSourceLine.start();
    }

    // *** New: Stop static playback ***
    public static void stopStaticSound() {
        if (staticSourceLine != null) {
            staticSourceLine.stop();
            staticSourceLine.flush();
            staticSourceLine.close();
        }
    }

    public static void adjustVolumeOfStatic( double sliderVolume) throws LineUnavailableException {
        if (staticSourceLine == null || !staticSourceLine.isOpen()) {
            return; // Avoid errors if staticSourceLine is not initialized
        }
        FloatControl volumeControl = (FloatControl) staticSourceLine.getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (float) ((range * (sliderVolume / 100.0f)) + volumeControl.getMinimum());
        volumeControl.setValue(gain);
    }

    // If audio is paused, plays audio and changes button to say pause
    // If audio is playing, pauses audio and changes button to say play
    public void playPauseAudio(String cwAudio) throws InterruptedException {
        char[] messageArray = cwAudio.toCharArray();

        if (isPaused){
            isPaused = false;
            stopAudio = false;
            playAudio(pauseIndex,messageArray);
        } else {
            isPaused = true;
        }
    }

    //play audio method for how the audio plays and saves the index where the message is paused so it picks up where it left off
    public void playAudio(int index, char[] messageArray) throws InterruptedException {
        stopAudioPlayback();
        pauseIndex = 0;
        isPaused = false;

        audioThread = new Thread(() -> {
            for (int i = index; i < messageArray.length; i++) {
                if (isPaused || stopAudio) {
                    pauseIndex = i;
                    break;
                }
                if (messageArray[i] == '-') {
                    try {
                        playDah();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                } else if (messageArray[i] == '.') {
                    try {
                        playDit();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                } else if (messageArray[i] == ' ') {
                    try {
                        Thread.sleep(User.getCwSpeed());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        audioThread.start();
    }

    public void stopAudioPlayback() throws InterruptedException {
        if (audioThread != null && audioThread.isAlive()) {
            stopAudio = true;
            audioThread.join();
            stopAudio = false;
        }
    }

    // Restarts the audio that is playing
    public void restartAudio() throws InterruptedException {
        stopAudioPlayback();

        pauseIndex = 0;
        isPaused = true;
        stopAudio = false;
    }

}

