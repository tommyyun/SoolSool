package com.example.q.soolsool;


import android.telephony.mbms.MbmsErrors;

public class MatchedRoom extends Room {
    private boolean[] accepted = null;

    public boolean isAllAccepted() {
        if(accepted == null)
            return false;

        for(int i=0;i<accepted.length;i++) {
            if(accepted[i] == false)
                return false;
        }

        return true;
    }

    public boolean[] getAccepted() throws InstantiationException {
        if(accepted ==  null) {
            throw new InstantiationException("Accepted field not instantiated");
        }
        return accepted;
    }

    public MatchedRoom setAccepted(boolean[] _accepted) {
        accepted = _accepted;
        return this;
    }
}