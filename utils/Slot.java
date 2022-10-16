// Candidate number: 38388

public class Slot {
    // Class that stores the information about any slot in any room available for scheduling

    // store 4 variables
    private int room;
    private int hour;
    private int minute;
    private int total; // equal to (hour*60 + minute)

    // accept "hh:mm" and "h:mm" as startTime
    public Slot(int room, String time) {
        this.room = room;
        hour = Integer.parseInt(time.split(":")[0]);
        minute = Integer.parseInt(time.split(":")[1]);
        total = (hour * 60 + minute);
    }

    // return true if slot overlaps
    public boolean overlaps(Slot s) {
        return Math.abs(this.total - s.total) < 60;
    }

    // return as Day hh:mm
    public String toString() {
        return "Room " + this.room + " " + hour + ":" + minute + "-" + (hour + 1) + ":" + minute;
    }

}





