// Candidate number: 38388

public class Course { // class that stores information on courses
    // Store 6 variables
    private int id;
    private String name;
    final public int seminars; // number of seminars needed to be scheduled, no matter how many slots have already been added
    private int needed_seminars; // number of remaining seminars waiting to be scheduled
    public Register<Course> conflict_list = new Register<Course>(); // dynamic data structure storing object of type Course
    public Register<Slot> slots_list = new Register<Slot>(); // dynamic data structure storing object of type Slot
    // I used the Register generic class to store the list of conflicting courses and the list of assigned slots,
    // because we can easily iterate through both lists using the previous implemented method such as,
    // startIteration() and next().

    // Constructor that needs three argument of type int, String and int
    public Course(int id, String name, int seminars) {
        this.id = id;
        this.name = name;
        this.seminars = seminars;
        needed_seminars = seminars; // update needed_seminars
    }

    // Method that returns true if adding the argument "s", of type Slot, to the course doesn't contradict any
    // of the timetabling rules, otherwise returns false
    public boolean slotPossible(Slot s) {

        if (conflict_list != null) { // first check if conflict list not empty
            conflict_list.startIteration(); // start iterating through list of conflicting courses
            Course i_conflict = conflict_list.next(); // first course of the conflicting list

            while (i_conflict != null) {
                // while that will iterate through all the assigned slots to i_conflict
                i_conflict.slots_list.startIteration(); // start iterating through slots of i_conflict courses
                Slot i_slot = i_conflict.slots_list.next(); // i_slot gets assigned the first slot of i_conflict
                while (i_slot != null) {
                    // while loop that will iterate through all assigned slots of i_conflict
                    if (i_slot.overlaps(s)) {
                        // if the slot overlaps with one the slot already attributed
                        // to one of conflicting list return false
                        return false;
                    } else {
                        i_slot = i_conflict.slots_list.next(); // if not return next of i_conflict
                    }
                }
                i_conflict = conflict_list.next(); // check the same, for the second conflict course
            }
        }
        return true; // if conflict list null, then return true
    }

    // Method that takes as argument an object of type Course and adds the course to the list of
    // conflicting courses.
    public void addConflict(Course c) {
        conflict_list.add(c);
    }

    // Method that returns the course id
    public int getID() {
        return id;
    }

    // Method that returns a string that includes the course id, name, and the assigned seminars
    public String toString() {
        String returnedValue = "Course " + id + " '" + name + "' \n" + "Seminars: ";
        if (slots_list != null) {
            slots_list.startIteration();
            Slot iterator = slots_list.next();
            while (iterator != null) {
                // iterating through the assigned slots of the course
                returnedValue += iterator.toString() + " "; // concatenate the strings
                iterator = slots_list.next();
            }
        }
        for (int i = 0; i < needed_seminars; i++) {
            // for not yet assigned seminars append "null" to the string
            returnedValue += " null";
        }
        return returnedValue;
    }

    // Method that returns true if one or more seminars are still not scheduled
    public boolean slotNeeded() {
        if (needed_seminars > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Method that assigns a slot "s", of type Slot, to the course (schedule a seminar at slot s)
    public void addSlot(Slot s) {
        slots_list.add(s);
        needed_seminars -= 1;
    }

}
