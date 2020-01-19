package com.example.jobhunteradmin.models;

public class GovtJobs {
    String jobname , companyname , location , experience , salary , applyby;

    public GovtJobs(String jobname, String companyname, String location, String experience, String salary, String applyby) {
        this.jobname = jobname;
        this.companyname = companyname;
        this.location = location;
        this.experience = experience;
        this.salary = salary;
        this.applyby = applyby;
    }

    public GovtJobs() {
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getApplyby() {
        return applyby;
    }

    public void setApplyby(String applyby) {
        this.applyby = applyby;
    }
}
