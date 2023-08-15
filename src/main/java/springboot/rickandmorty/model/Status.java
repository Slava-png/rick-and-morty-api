package springboot.rickandmorty.model;

public enum Status {
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("unknown");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
