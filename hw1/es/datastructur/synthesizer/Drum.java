package es.datastructur.synthesizer;

public class Drum extends GuitarString {

    private static final double DECAY = 1.0;

    public Drum(int frequency) {
        super(frequency);
    }

    @Override
    public void tic() {
        double frontSample = buffer.dequeue();
        double secondSample = buffer.peek();
        frontSample = DECAY * (frontSample + secondSample) / 2;
        if (Math.random() < 0.5) {
            buffer.enqueue(-frontSample);
        } else {
            buffer.enqueue(frontSample);
        }
    }
}
