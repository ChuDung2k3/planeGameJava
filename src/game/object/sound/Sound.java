package game.object.sound;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
public class Sound {
    private final URL shoot;
    private final URL collide;
    private final URL destroy;
    private final URL cuckoo;

    public Sound() {
        this.shoot = this.getClass().getClassLoader().getResource("game/object/sound/shot.wav");
        this.collide = this.getClass().getClassLoader().getResource("game/object/sound/collide.wav");
        this.destroy = this.getClass().getClassLoader().getResource("game/object/sound/destroy.wav");
        this.cuckoo = this.getClass().getClassLoader().getResource("game/object/sound/chicken.wav");
    }
    
    public void soundShoot()
    {
        play(shoot);
    }
    public void soundCollide()
    {
        play(collide);
    }
    public void soundDestroy()
    {
        play(destroy);
    }
    public void soundCuckoo()
    {
        play(cuckoo);
    }
    private void play(URL url)
    {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP)
                    {
                        clip.close();
                    }
                }
            });
            audioIn.close();
            clip.start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}