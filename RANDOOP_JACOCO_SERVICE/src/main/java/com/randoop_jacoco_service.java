package com.example.task9;

import java.io.File;
import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;



@SpringBootApplication
@EnableWebMvc

public class randoop_jacoco_service{
    public static void main(String[] args) {
      SpringApplication.run(randoop_jacoco_service.class, args);
    }

    @RestController
    @RequestMapping("/randoop")
    
    public class TestGenerationController{  
    	 
    	@PostMapping("/generate")
   
    	 public  String generateTests1(@RequestBody TestGenerationData Data ) throws IOException, NoSuchFileException  {
	     String comm = "javac "+Data.get_classpath()+"/"+Data.get_classname()+".java";
         Process proce = Runtime.getRuntime().exec(comm);
			try {
				// Attendere 5 secondi
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	    String command = "java -classpath "+Data.get_classpath()+";"+Data.get_JarPath()+"/randoop.jar randoop.main.Main gentests --testclass="+Data.get_classname()+" --junit-output-dir="+Data.get_output()+" --time-limit="+Data.get_time();
    	
		  Process process = Runtime.getRuntime().exec(command);
			try {
				// Attendere 5 secondi + il tempo di Randoop
				TimeUnit.SECONDS.sleep(Data.get_time()+5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Path sourcePath = Path.of(Data.get_classpath()+"/"+Data.get_classname()+".java");
			Path destinationPath = Path.of(Data.get_output()+"/"+Data.get_classname()+".java");
			Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			try {
				// Attendere 5 secondi
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String command3 = "javac -cp "+Data.get_JarPath()+"/hamcrest.jar;"+Data.get_JarPath()+"/junit.jar;"+Data.get_output()+"; -d "+Data.get_output()+"/temp "+Data.get_output()+"/RegressionTest.java";
			Process process3 = Runtime.getRuntime().exec(command3);
			try {
				// Attendere 5 secondi
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Path sourcePath1 = Path.of(Data.get_output()+"/temp/"+Data.get_classname()+".class");
			Path destinationPath1 = Path.of(Data.get_classpath()+"/"+Data.get_classname()+".class");
			Files.copy(sourcePath1, destinationPath1, StandardCopyOption.REPLACE_EXISTING);
			try {
				// Attendere 5 secondi
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String command6 = "java -javaagent:"+Data.get_JarPath()+"/jacocoagent.jar -cp "+Data.get_JarPath()+"/hamcrest.jar;"+Data.get_JarPath()+"/junit.jar;"+Data.get_output()+"/temp; org.junit.runner.JUnitCore RegressionTest";
			Process process6 = Runtime.getRuntime().exec(command6);
			try {
				// Attendere 5 secondi
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String command7 = "java -jar "+Data.get_JarPath()+"/jacococli.jar report jacoco.exec --classfiles "+Data.get_classpath()+" --sourcefiles "+Data.get_classpath()+" --html "+Data.get_output()+"/report";
			Process process7 = Runtime.getRuntime().exec(command7);
			try {
				TimeUnit.SECONDS.sleep(5); 
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		
			Path sourcePath2=Path.of(Data.get_output()+"/temp");
			
			if(Files.exists(sourcePath2)&& Files.isDirectory(sourcePath2)) {
				try {
					Files.walk(sourcePath2)
						.sorted(java.util.Comparator.reverseOrder())
						.map(Path::toFile)
						.forEach(File::delete);
						
				} catch(IOException e ) {
					e.printStackTrace();
				}
				
			}
			String filePath = Data.get_classpath()+"/"+Data.get_classname()+".class";
			File file = new File(filePath);
			file.delete();
			return "GENERAZIONE TEST RANDOOP E REPORT JACOCO TERMINATA";
    	    }
    	}
    
}
    

	