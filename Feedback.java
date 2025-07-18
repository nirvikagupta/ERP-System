package com.example.ErpManagement;

public class Feedback<T> extends ERPdesign{
    private Student student;
    private ShowCourses course;
    private T feedback;

    public Feedback(Student student, ShowCourses course, T feedback) {
        this.student = student;
        this.course = course;
        this.feedback = feedback;
    }

    public Student FetchStudent() {
        return student;
    }

    public ShowCourses FetchCourse() {
        return course;
    }

    public T FetchFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "Feedback from " + student.NameFetch() + " for " + course.CoursenameFetch() + ": " + feedback.toString();
    }
}