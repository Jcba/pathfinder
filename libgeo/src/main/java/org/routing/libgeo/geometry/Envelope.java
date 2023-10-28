package org.routing.libgeo.geometry;

public class Envelope {

    private final double[][] envelope;

    private Envelope() {
        this.envelope = new double[2][];
    }

    public double[][] getEnvelope() {
        return envelope;
    }

    public static Builder builder() {
        return new Envelope.Builder();
    }

    public static class Builder {

        private final Envelope envelope;

        Builder() {
            envelope = new Envelope();
        }

        public Builder min(double lat, double lon) {
            envelope.envelope[0] = new double[]{lat, lon};
            return this;
        }

        public Builder max(double lat, double lon) {
            envelope.envelope[1] = new double[]{lat, lon};
            return this;
        }

        public Envelope build() {
            return envelope;
        }
    }
}
