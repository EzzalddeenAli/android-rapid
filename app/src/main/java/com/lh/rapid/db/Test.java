package com.lh.rapid.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TEST.
 */
public class Test {

    private Long id;
    /** Not-null value. */
    private String test;

    public Test() {
    }

    public Test(Long id) {
        this.id = id;
    }

    public Test(Long id, String test) {
        this.id = id;
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTest() {
        return test;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTest(String test) {
        this.test = test;
    }

}
