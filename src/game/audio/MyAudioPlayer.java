package game.audio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MyAudioPlayer {
    private final Map<String, AudioInputStream> streamMap = new HashMap<String, AudioInputStream>();
    private Clip audioClip;

    public MyAudioPlayer(){
        try {
            streamMap.put("background",AudioSystem.getAudioInputStream(getClass().getResource("/music/bg.wav")));
            streamMap.put("getCoin",AudioSystem.getAudioInputStream(getClass().getResource("/music/Coin_1.wav")));
            streamMap.put("landing",AudioSystem.getAudioInputStream(getClass().getResource("/music/Landing.wav")));
            streamMap.put("death",AudioSystem.getAudioInputStream(getClass().getResource("/music/Death.wav")));
            streamMap.put("start",AudioSystem.getAudioInputStream(getClass().getResource("/music/Start.wav")));
            streamMap.put("win",AudioSystem.getAudioInputStream(getClass().getResource("/music/Win.wav")));
            streamMap.put("spikesAttack",AudioSystem.getAudioInputStream(getClass().getResource("/music/Spikesinwalls_attack.wav")));
        } catch (UnsupportedAudioFileException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setFile(String fileName) {
        try {
            AudioInputStream s = streamMap.get(fileName);
            audioClip = AudioSystem.getClip();
            audioClip.open(s);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void playMusic() {
        audioClip.start();
        audioClip.setFramePosition(0);
    }
    public void loopMuisc() {
        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stopMusic() {
        audioClip.stop();
    }
}
