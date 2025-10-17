package com.project.api.Class;

public class TripSearchRequest {
    // traveller id
    private String id;
    // last name (frontend "lastName" maps to this, thats how they call it on the pdf even though it should be name)
    private String name;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
