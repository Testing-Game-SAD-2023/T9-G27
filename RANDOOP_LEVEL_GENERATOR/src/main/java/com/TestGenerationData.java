package com;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.lang.System;



public class TestGenerationData {
	
	String AUTpath = "/AUTName";
	String JarPath = "/eseguibili";
	String classpath = AUTpath+"/AUTSourceCode";
	String outputpath = AUTpath+"/RobotTest/RandoopTest/temp";
	
	@JsonProperty("Livello")
	private int livello;
	
	public String get_classpath() {
		return classpath;
	}
	
	public String get_JarPath() {
		return JarPath;
	}
	
	public int get_level() {
		return livello;
	}
	
	public String get_output() {
		return outputpath;
	}
	
	public void set_level(int level) {
		this.livello=level;
	}
	

	
}
	

class Copertura {
    private String test;
    private int coverage;
	
	 public Copertura() {
        this.test = "a";
        this.coverage = 1;
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public int getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }
}
