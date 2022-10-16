// Candidate number: 38388

public class Schedule {

    private Register<Slot> slots;
    private Register<Course> courses;

    public Schedule(Register<Slot> s, Register<Course> c) {
        slots = s;
        courses = c;
    }

    // return a string representation of the courses
    // and their assigned timeslots
    public String toString() {
        String s = "";
        courses.startIteration();
        while (courses.hasNext()) {
            Course c = courses.next();
            s += c + "\n\n";
        }
        return s;
    }

    public void greedySchedule() {
        /* The algorithm starts by first appending the first slot to the first course. Then, the algorithm will look
        for courses that needs more than one seminar to be scheduled. For each of those courses, the algorithm will
        try to schedule seminars at only overlapping slots. Indeed, by doing so we minimise the chances of running out of
        possible slots for conflicting courses. At the last round, the algorithm will look for any courses that still
        need seminars to be scheduled, and will add the possible slot from the remaining slots available. */

        // FIRST ROUND: initialize first course with first seminar
        courses.startIteration();
        slots.startIteration();
        Slot first = slots.next();
        Course i_course = courses.next();
        if (i_course != null && first != null) { // if no empty course list and seminar list
            i_course.addSlot(first); // assign 1st slot to 1st course
            slots.deleteCurrent(); // delete slot from available
        } else {
            return;
        }

        // SECOND ROUND: add overlapping seminars to each course with more than one needed seminars
        while (i_course != null) {
            // outer while loop iterates through each course
            slots.startIteration(); // restart iteration
            Slot i_slot = slots.next();
            while (i_slot != null && i_course.seminars > 1) { // modify and use .next
                // while we didn't reach a slot further than the last slot and the course needs more than one seminar
                if (i_course.slotPossible(i_slot) && i_course.slotNeeded() && i_slot.overlaps(first)) {
                    // if slot can be assigned to the course AND the course needs more seminar AND
                    // the course overlaps with the previous assigned slot to the course
                    i_course.addSlot(i_slot);
                    slots.deleteCurrent(); // delete added slot
                    i_slot = slots.next();
                } else {
                    i_slot = slots.next(); // otherwise get next slot from the list
                }
            }
            // When we added all the possible overlapping slots to the first course
            // we move to the next course
            i_course = courses.next();
            while (i_course != null && i_course.seminars <= 1) {
                // skipping courses that only needs one seminar
                i_course = courses.next();
            }
            // Then we initialize the new course
            slots.startIteration();
            i_slot = slots.next();
            boolean initialized = false; // variables that takes true if the course slots list is initialized

            while (!initialized && i_slot != null) {
                // while we haven't initialized the list course, we iterate through the possible slots
                if (i_course == null) {
                    break;
                }
                if (i_course.slotPossible(i_slot)) {
                    // if the slot can be added, initialize the course slot list
                    i_course.addSlot(i_slot); // add slot to the list of slot of the course
                    slots.deleteCurrent();
                    first = i_slot; // initialize first
                    initialized = true;
                } else {
                    // if the slot cannot be added, try the next slot
                    i_slot = slots.next();
                }
            }
        }
        // THIRD ROUND: fill the remaining courses with only one seminar needed one by one and also courses
        // with more than one seminar needed that couldn't be filled earlier with only overlapping slots.
        courses.startIteration();
        i_course = courses.next();
        while (i_course != null) { // starting iteration through the courses
            if (!i_course.slotNeeded()) {
                // if the course is filled with the maximum number of slots, pass to the other course
                i_course = courses.next();
            } else {
                // if course is not totally filled with the maximum number of slots, try to fill it
                // by iterating other the list of remaining slots
                slots.startIteration();
                Slot i_slot = slots.next();
                while (i_slot != null && i_course.slotNeeded()) {
                    // while we didn't exceed the last slot and the course still needs more slots
                    if (i_course.slotPossible(i_slot)) {
                        // if slot can be assigned to the course, then add it to the slot list of the course
                        i_course.addSlot(i_slot);
                        slots.deleteCurrent();
                        i_slot = slots.next();
                    } else {
                        // if the slot cannot be added, try the next slot
                        i_slot = slots.next();
                    }
                }
                if (i_course.slotNeeded()) {
                    // After having iterated through all the possible remaining slots, if
                    // the course is not totally filled, then our algorithm failed for this course
                    System.out.println("GREEDY FAILED: could not assign further slot to course");
                    System.out.println(i_course.getID() + "\n");
                    i_course = courses.next(); // try to fill the other remaining course

                }
            }
        }
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        Schedule schedule = Input.read(args[0]);
        if (schedule != null) {
            schedule.greedySchedule();
            System.out.println(schedule);
        }
    }
}
