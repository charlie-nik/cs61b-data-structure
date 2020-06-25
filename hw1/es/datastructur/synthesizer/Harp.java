package es.datastructur.synthesizer;

public class Harp extends GuitarString {

    public Harp(int frequency) {
        super(frequency * 2);

    }

    @Override
    public void tic() {
        double frontSample = buffer.dequeue();
        double secondSample = buffer.peek();
        frontSample = DECAY * (frontSample + secondSample) / 2;
        buffer.enqueue(-frontSample);
    }
}
