package io.codelex.flightplanner.flights;

public class Flight {

    private String from;
    private String to;

    private int id;

    public Flight(String from, String to, int id) {
        this.from = from;
        this.to = to;
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", id=" + id +
                '}';
    }
}
