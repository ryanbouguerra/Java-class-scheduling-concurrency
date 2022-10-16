// part of the assessed course work of MA407, 2021/22
//
// a class to read in data for scheduling from a file
// an example of the format of the content is given at the end, below

// This class uses many things that were not taught in the course,
// so you do not need to understand this code.
// DO NOT CHANGE THIS FILE.

// import the libraries needed to read the file
// YOU ARE NOT ALLOWED TO IMPORT LIBRARIES IN THE OTHER FILES

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Input {

    // read in data for scheduling
    public static Schedule read(String file) {
        Register<Slot> slots = new Register<Slot>();
        Register<Course> courses = new Register<Course>();
        // open the given file
        Scanner scanner;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File " + file + " not found.");
            return null;
        }
        // read the file, line by line
        while( scanner.hasNextLine() ) {
            String line = scanner.nextLine().trim();
            int firstSpace = line.indexOf(' ');
            // if this line contains no space, process next line
            if (firstSpace==-1) {
                continue;
            }
            // split line into words (omitting first word)
            String[] words = line.substring(firstSpace+1).trim().split("\\s+");   
            // each (non-empty) line starts with "Room", "Course", or "Student",
            if (line.startsWith("Room") ){
                // create all slots for this room and add them to slots
                int room;
                try {
                    room = Integer.parseInt(words[0]);
                } catch (NumberFormatException e) {
                    System.out.println( "ERROR: Invalid format in line: " + line);
                    return null;
                }
                for (int i=1; i<words.length; i++) {
                    Slot s;
                    try {
                        s = new Slot(room,words[i]);
                    } catch (Exception e) {
                        System.out.println( "ERROR: Invalid format in line: " + line);
                        e.printStackTrace();
                        return null;
                    }
                    slots.add(s);
                }
            } else if (line.startsWith("Course")){
                // create a course and add it to courses
                int id;
                int seminars;
                try {
                    id = Integer.parseInt (words[0]);
                    seminars = Integer.parseInt (words[words.length-1]);
                } catch (NumberFormatException e) {
                    System.out.println( "ERROR: Invalid format in line: " + line);
                    return null;
                }
                String name = "";
                for (int i=1; i<words.length-1; i++) {
                    name += words[i] + " ";
                }
                Course c = new Course(id, name, seminars);
                courses.add(c);
            } else if (line.startsWith("Student")){
                // for each pair c1,c2 of courses found in this line add c1
                // to the conflicts of c2
                Course[] conflictingCourses = new Course[words.length];
                for (int i=0; i<words.length; i++) {
                    int courseID; 
                    try {
                        courseID = Integer.parseInt( words[i] );
                    } catch (NumberFormatException e) {
                        System.out.println( "ERROR: Invalid format in line: " + line);
                        return null;
                    }
                    courses.startIteration();
                    // find the course with courseID
                    // and add it to conflictingCourses
                    while( courses.hasNext() ) {
                        Course c = courses.next();
                        if( c.getID()==courseID ) {
                            conflictingCourses[i]=c;
                            break;
                        }
                    }
                    if (conflictingCourses[i]==null) { 
                        // courseID was not found
                        System.out.print( "ERROR: Course " + courseID);
                        System.out.println(" not known; in line: " + line);
                        return null;
                    }
                }
                for (int i=0; i<conflictingCourses.length-1; i++) {
                    for (int j=i+1; j<conflictingCourses.length; j++) {
                        conflictingCourses[i].addConflict(conflictingCourses[j]);
                        conflictingCourses[j].addConflict(conflictingCourses[i]);
                    }
                }
            }
        }
        return new Schedule(slots,courses);
    }

}

// The format of the input is as follows:
// each (non-empty) line starts with "Room", "Course", or "Student",
//
// "Room" lines are of the form
// Room x t1 t2 ...
// where x is the room number
// and ti is the start time of a time slot in format h:mm or hh:mm
//
// "Course" lines are of the form
// Course id "title" x
// where x is the number of seminar slots needed
//
// "Student" lines are of the form
// Student c1 c2 c3 ...
// where ci is the id of a course the student whishes to take

// Example file:
//
// Room 1 9:00 11:30 12:30 14:00 15:00 16:30
// Room 4 09:15 14:15 16:45
//
// Course 1 "A brief history of times" 3
// Course 2 "Algorithms" 4
// Course 3 "Principles of scheduling" 2
// Course 4 "A last course" 2
//
// Student 1 2 4
// Student 2 3
