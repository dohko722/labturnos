
package labturnos;
import java.util.Date;

public class Cabina {
    private final int id;
    private Turno turnoActual;
    private int atendidosCount = 0;
    private Date lastFinishedAt;

    public Cabina(int id) { 
        this.id = id; 
    }

    public int getId() { return id; }
    public Turno getTurnoActual() { return turnoActual; }

    // ---- Nueva lógica ----
    public void ocupar(Turno t) {
        this.turnoActual = t;
    }

    public void liberar() {
        if (this.turnoActual != null) {
            atendidosCount++;
            lastFinishedAt = new Date();
            this.turnoActual = null;
        }
    }

    public int getAtendidosCount() { return atendidosCount; }
    public Date getLastFinishedAt() { return lastFinishedAt; }
}
