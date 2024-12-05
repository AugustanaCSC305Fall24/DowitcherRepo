package org.example.utility;

import org.example.data.User;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sound {

    private static int ditDuration = 75;
    private static int ditFrequency = 600;
    private static int dahDuration = 225;
    private static int dahFrequency = 600;

    private static SourceDataLine staticSourceLine; // **New field for persistent sound line**
    private static Thread audioThread;  // Track the current audio thread
    public static AtomicBoolean isAudioPlaying = new AtomicBoolean(false);  // Track audio playback state

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

    public static void playAudio(String morseCode) {
        // Convert the input Morse code string to a character array
        char[] messageArray = morseCode.toCharArray();

        // Stop the audio if it's already playing
        if (isAudioPlaying.get() && audioThread != null && audioThread.isAlive()) {
            audioThread.interrupt();  // Interrupt the current playback thread
            try {
                audioThread.join();  // Wait for the thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // If no sound is requested, just return
        if (morseCode.isEmpty()) {
            isAudioPlaying.set(false); // Ensure the audio is marked as not playing
            return;
        }

        // Create and start a new thread for playing audio
        audioThread = new Thread(() -> {
            isAudioPlaying.set(true);  // Mark audio as playing

            for (char c : messageArray) {
                try {
                    if (Thread.currentThread().isInterrupted()) {
                        break;  // Exit the loop if interrupted
                    }

                    if (c == '-') {
                        // Play dah
                        Sound.playDah();
                    } else if (c == '.') {
                        // Play dit
                        Sound.playDit();
                    } else if (c == ' ') {
                        // Pause between letters/words
                        Thread.sleep(User.getCwSpeed());
                    }
                } catch (LineUnavailableException | InterruptedException e) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;  // Exit the loop if interrupted
                    }
                    throw new RuntimeException(e);
                }
            }

            isAudioPlaying.set(false);  // Mark audio as finished
        });

        // Start the thread
        audioThread.start();
    }
}

