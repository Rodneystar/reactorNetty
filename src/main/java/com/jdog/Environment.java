package com.jdog;

public class Environment {

    final static String CDR_PROFILE = System.getenv("CDR_PROFILE");


	public CdrProfile getCdrProfile() {
        if( CDR_PROFILE != null) {
            return CdrProfile.valueOf(CDR_PROFILE);
        }
        return CdrProfile.NONE;
	}

    public enum CdrProfile {
        KAFKA,
        CONSOLE,
        NONE
    }
    

}
