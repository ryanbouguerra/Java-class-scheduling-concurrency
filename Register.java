// Candidate number: 38388


public class Register<Type> { // linked data structure for storing an arbitrary number of values of type Type

    private Item first = null; // first item of the data structure
    private Item iterator = null; // item to be returned when next() is called
    private Item parent = null; // item that will store the last value returned by next() method
    private Item parent_parent = null; // item that will store the item just before parent

    // Auxiliary class storing item of the list
    private class Item {
        Type type;
        Item next;
    }

    // Method that prepares the data structure for starting a new iteration through the values
    public void startIteration() {
        // reinitialize iterating variables
        iterator = first;
        parent = null;
        parent_parent = null;
    }

    // Method that returns false if the current iteration reached the end, otherwise return true
    public boolean hasNext() {
        if (iterator != null) {
            return true;
        } else {
            return false;
        }
    }

    // Method that returns the next value stored in the data structure
    public Type next() {
        if (iterator == first) { // if first call of next()
            if (iterator == null) { // if dats structure empty return null
                return null;
            } else {
                // initialize tracking variables
                parent = iterator;
                iterator = iterator.next;
                return parent.type;
            }
        } else if (iterator == first.next) { // if second call of next()
            if (iterator == null) {
                parent = null;
                return null;
            } else {
                // initialize tracking variables
                parent = iterator;
                iterator = iterator.next;
                return parent.type;
            }
        } else { // if third or later call of next()
            if (iterator == null) {
                parent = null;
                return null;
            } else {
                parent_parent = parent;
                parent = iterator;
                iterator = iterator.next;
                return parent.type;
            }
        }

    }

    /* This implementation will make our greedy algorithm store the list of courses
    and slots in the same order as given in the txt file. Indeed, the first course
    in the txt file will be the first course in Register<Course>, and similarly, the first
    slot will be the first slot in Register<Slot>. For the given txt files, I believe that
    this implementation will make our algorithm chances to succeed greater, compared, for
    example, to an implementation where the added item will go to the first position
    (txt file reversed order). Indeed, we can see that the three first courses are among those
    who need the greatest number of seminars, and recall that our greedy algorithm will first
    assign overlapping slots to those course if they need more than one seminar. Hence, I believe
    that starting to assign overlapping slots to courses with large number of needed seminars, will
    increase the probability of success of our algorithm. Moreover, to maximise our chances of success,
    one way would be to order the Register<Slot> according to the longest chain of overlapping slots,
    and order Register<Course> according to courses with the highest number of needed seminars. */
    public void add(Type value) {
        // Add an item at the end of the generic class and returns nothing.
        Item add = new Item();
        add.type = value;
        if (first == null) { // if list empty
            first = add;
            return;
        }
        Item iteration = first;
        while (iteration.next != null) { // find last element
            iteration = iteration.next;
        }
        iteration.next = add; // link last element with the new element to be added

    }

    // Method that deletes the current item in this iteration.
    public void deleteCurrent() {
        if (first == null) { // if list empty
            return;
        } else if (parent == first) { // if we want to delete first item
            first = first.next;
        } else if (parent == first.next) { // if we want to delete second item
            parent = first;
            first.next = first.next.next;
        } else if (parent == null) {
            return;
        } else { // if we want to delete third or later item
            parent_parent.next = iterator;
            parent = parent_parent;
        }
    }


}

