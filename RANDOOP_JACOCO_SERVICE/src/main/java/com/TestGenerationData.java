package com.example.task9;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestGenerationData {
	@JsonProperty("classpath")
	private String classpath;
	
	@JsonProperty("JarPath")
	private String JarPath;
	
	@JsonProperty("classname")
	private String classname;
	
	@JsonProperty("outputpath")
	private String outputpath;
	
	@JsonProperty("time_limit")
	private int time_limit;
	
	public String get_classpath() {
		return classpath;
	}
	
	public String get_classname() {
		return classname;
	}
	
	public String get_JarPath() {
		return JarPath;
	}
	
	public int get_time() {
		return time_limit;
	}
	
	public String get_output() {
		return outputpath;
	}
	
	public void set_classpath(String path) {
		this.classpath=path;
	}
	
	public void set_classname(String name) {
		this.classname=name;
	}
	
	public void set_JarPath(String path) {
		this.JarPath=path;
	}
	
	public void set_outputpath(String path) {
		this.outputpath=path;
	}
	
	public void set_time(int time) {
		this.time_limit=time;
	}
	
	


	
	
	
}
	
	

