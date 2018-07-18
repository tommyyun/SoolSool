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
    private String region = null;
    private String title;
    private String description;
    private int targetHold = -1;
    private int currentHold = -1;
    private String leader = null;
    private String[] participants = null;
    private String roomid = null;
    private String date = null;
    private String time = null;

    public String getDate() throws InstantiationException{
        if (date ==null){
            throw new InstantiationException("Date field not instantiated!");
        }
        return date;
    }

    public String getTime() throws InstantiationException{
        if (time ==null){
            throw new InstantiationException("Time field not instantiated!");
        }
        return time;
    }




    public String getRoomid() throws InstantiationException{
        if (roomid ==null){
            throw new InstantiationException("RoomId field not instantiated!");
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

    public String getRegion() throws InstantiationException {
        if(region == null) {
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

    public int getTargetHold() throws InstantiationException {
        if(targetHold == -1) {
            throw new InstantiationException("TargetHold field not instantiated!");
        }
        return targetHold;
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

    public Room setRegion(String _region) {
        region = _region;
        return this;
    }

    public Room setDate(String _date) {
        date = _date;
        return this;
    }
    public Room setTime(String _time) {
        time = _time;
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

    public Room setTargetHold(int _targetHold) {
        targetHold = _targetHold;
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
        output+="\nTarget hold : " +targetHold;
        output+="\nCurrent hold : "+currentHold;

        return output;
    }

}
