package com.example.waitinglistmanagement;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class StudentSorter {

    public static void sortByPriority(List<Student> studentList) {
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                // First, compare by priority
                int priorityComparison = s2.getPriority().compareTo(s1.getPriority());

                // If priorities are the same, compare by student IDs
                if (priorityComparison == 0) {
                    // Compare by student IDs
                    return Long.compare(s1.getId(), s2.getId());
                } else {
                    return priorityComparison; // Return priority comparison result
                }
            }
        });
    }
}

