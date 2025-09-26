
package labturnos;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TurnoManager {
    private final AtomicLong seq = new AtomicLong(0);

    private final PriorityQueue<Turno> cola;
    private final List<Turno> atendidos = new ArrayList<>();
    private final Cabina[] cabinas;

    // Contadores por prioridad para generar IDs A-###, M-###, B-###
    private final Map<Turno.Prioridad, Integer> contadores = new EnumMap<>(Turno.Prioridad.class);

    public TurnoManager(int numCabinas) {
        this.cabinas = new Cabina[numCabinas];
        for (int i = 0; i < numCabinas; i++) {
            cabinas[i] = new Cabina(i + 1);
        }

        // Inicializar contadores
        for (Turno.Prioridad p : Turno.Prioridad.values()) {
            contadores.put(p, 0);
        }

        // Comparator integrado (sin clase externa)
        this.cola = new PriorityQueue<>(
            Comparator.comparingInt((Turno t) -> t.getPrioridad().getPeso()).reversed()
                      .thenComparingLong(Turno::getSeq)
        );
    }

    // Genera ID del tipo A-001 / M-002 / B-003
    private String nextId(Turno.Prioridad prioridad) {
        int next = contadores.merge(prioridad, 1, Integer::sum);
        String pref = prioridad == Turno.Prioridad.ALTA ? "A"
                    : prioridad == Turno.Prioridad.MEDIA ? "M" : "B";
        return pref + "-" + String.format("%03d", next);
    }

    public Turno tomarTurno(Turno.Prioridad prioridad) {
        String id = nextId(prioridad);
        Turno t = new Turno(id, prioridad, seq.getAndIncrement());
        cola.offer(t);
        return t;
    }

    public List<Turno> snapshotColaOrdenada() {
        return new ArrayList<>(cola); // PriorityQueue ya ordena por nuestro comparador
    }

    public List<Turno> getAtendidos() {
        return Collections.unmodifiableList(atendidos);
    }

    public Cabina[] getCabinas() {
        return cabinas;
    }

    public boolean hayCabinaLibre() {
        for (Cabina c : cabinas) {
            if (c.getTurnoActual() == null) return true;
        }
        return false;
    }

    public boolean hayTurnosEnCola() {
        return !cola.isEmpty();
    }

    // Terminar: libera cabina, mueve turno a atendidos
    public void terminar(int cabinaIdx) {
        Cabina c = cabinas[cabinaIdx];
        Turno actual = c.getTurnoActual();
        if (actual != null) {
            atendidos.add(actual);
            c.liberar();
        }
    }

    // Siguiente: asigna turno más prioritario a la mejor cabina libre
    public void siguiente() {
        if (cola.isEmpty()) return;
        Cabina mejor = elegirCabinaLibre();
        if (mejor == null) return;

        Turno t = cola.poll();
        mejor.ocupar(t);
    }

    private Cabina elegirCabinaLibre() {
        return Arrays.stream(cabinas)
            .filter(c -> c.getTurnoActual() == null)
            .min(Comparator
                    .comparingInt(Cabina::getAtendidosCount)
                    .thenComparing(c -> Optional.ofNullable(c.getLastFinishedAt()).orElse(new Date(0)))
                    .thenComparingInt(Cabina::getId)
            )
            .orElse(null);
    }

    public List<Cabina> snapshotCabinasLibresOrdenadas() {
        List<Cabina> libres = new ArrayList<>();
        for (Cabina c : cabinas) {
            if (c.getTurnoActual() == null) libres.add(c);
        }
        libres.sort(
            Comparator.comparingInt(Cabina::getAtendidosCount)
                      .thenComparing(c -> Optional.ofNullable(c.getLastFinishedAt()).orElse(new Date(0)))
                      .thenComparingInt(Cabina::getId)
        );
        return libres;
    }
}


