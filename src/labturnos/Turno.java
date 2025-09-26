
package labturnos;

import java.time.Instant;

public class Turno {
    public enum Prioridad { 
        ALTA(3), MEDIA(2), BAJA(1);
        
        private final int peso;
        Prioridad(int peso) { this.peso = peso;}
        public int getPeso () {return peso;}
    }
    

    private final String id;
    private final Prioridad prioridad;
    private final long seq;
    private final Instant timestamp;

    public Turno(String id, Prioridad prioridad, long seq) {
        this.id = id;
        this.prioridad = prioridad;
        this.seq = seq;
        this.timestamp = Instant.now();
    }

    public String getId() { return id; }
    public Prioridad getPrioridad() { return prioridad; }
    public long getSeq() { return seq; }
    public Instant getTimestamp() { return timestamp; }

    @Override
    public String toString() { return id; }
}
