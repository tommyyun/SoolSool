package com.example.q.soolsool;

public class Room {

    public static int INTEREST_LOVE = 0;
    public static int INTEREST_WORK = 1;
    public static int INTEREST_LIFE = 2;
    public static int INTEREST_POLITICS = 3;

    public static int REGION_SEOUL_YONGSAN = 90;
    public static int REGION_SEOUL_DONDAEMUN = 91;
    public static int REGION_SEOUL_HANNAM = 92;

    private String interest = null;
    private int region = -1;
    private String title;
    private String description;
    private int minHold = -1;
    private int maxHold = -1;
    private int currentHold = -1;
    private String leader = null;
    private String[] participants = null;
    private String roomid = null;


    public String getRoomid() throws InstantiationException{
        if (roomid ==null){
            throw new InstantiationException("Leader field not instantiated!");
        }
        return roomid;
    }

    public String getLeader() throws InstantiationException{
        if (leader == null){
            throw new InstantiationException("Leader field not instantiated!") ;
        }
        return leader;
    }

    public String[] getParticipants() throws InstantiationException{
        if (participants == null){
            throw new InstantiationException("Participants field not instantiated!") ;
        }
        return participants;
    }

    public String getInterest() throws InstantiationException {
        if(interest == null) {
            throw new InstantiationException("Interest field not instantiated!");
        }
        return interest;
    }

    public int getRegion() throws InstantiationException {
        if(region == -1) {
            throw new InstantiationException("Region field not instantiated!");
        }
        return region;
    }

    public String getTitle() throws InstantiationException {
        if(title == null) {
            throw new InstantiationException("Title field not instantiated!");
        }
        return title;
    }

    public String getDescription() throws InstantiationException {
        if(description == null) {
            throw new InstantiationException("Description field not instantiated!");
        }
        return description;
    }

    public int getMinHold() throws InstantiationException {
        if(minHold==-1) {
            throw new InstantiationException("MinHold field not instantiated!");
        }
        return minHold;
    }

    public int getMaxHold() throws InstantiationException {
        if(maxHold == -1) {
            throw new InstantiationException("MaxHold field not instantiated!");
        }
        return maxHold;
    }

    public int getCurrentHold() throws InstantiationException {
        if (currentHold == -1) {
            throw new InstantiationException("CurrentHold field not instantiated!");
        }
        return currentHold;
    }

    public Room setRoomid(String _roomid){
        roomid = _roomid;
        return this;
    }


    public Room setLeader(String _leader){
        leader = _leader;
        return this;
    }

    public Room setParticipants(String[] _participants){
        participants = _participants;
        return this;
    }


    public Room setInterest(String _interest) {
        interest = _interest;
        return this;
    }

    public Room setRegion(int _region) {
        region = _region;
        return this;
    }

    public Room setTitle(String _title) {
        title = _title;
        return this;
    }

    public Room setDescription(String _description) {
        description = _description;
        return this;
    }

    public Room setMinHold(int _minHold) {
        minHold= _minHold;
        return this;
    }

    public Room setMaxHold(int _maxHold) {
        maxHold = _maxHold;
        return this;
    }

    public Room setCurrentHold(int _currentHold) {
        currentHold = _currentHold;
        return this;
    }

    public String toString() {
        String output =  "";
        output+="Interest : "+interest;
        output+="\nRegion : "+region;
        output+="\nTitle : "+title;
        output+="\nDescription : "+description;
        output+="\nMin hold : "+minHold;
        output+="\nMax hold : " +maxHold;
        output+="\nCurrent hold : "+currentHold;

        return output;
    }

}
