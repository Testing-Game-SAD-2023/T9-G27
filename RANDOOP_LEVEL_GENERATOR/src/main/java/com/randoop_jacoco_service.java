package com;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;




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
			
		if(Data.get_level()<1){
			System.out.println("ATTENZIONE! Il numero di livelli richiesti ("+Data.get_level()+") è minore di 1!");
			return "ATTENZIONE! Il numero di livelli richiesti ("+Data.get_level()+") è minore di 1!";
		}
		
		
        File foldero = new File(Data.get_classpath());
        boolean isFolderEmpty = foldero.list().length == 0;
        if (isFolderEmpty) {
			System.out.println("ATTENZIONE! "+Data.get_classpath()+" risulta vuota! Inserire il .java da testare nella cartella");
			return "ATTENZIONE! "+Data.get_classpath()+" risulta vuota! Inserire il .java da testare nella cartella";
		}
			
		 
    	File filj = new File(Data.get_output().substring(0, Data.get_output().length() - 5));
    	Functions.deleteDirectory(filj);
    	
    	Files.createDirectory(Path.of(Data.get_output().substring(0, Data.get_output().length() - 5)));
    		
    	String os = System.getProperty("os.name").toLowerCase();
		 
		File cartella = new File(Data.get_classpath());
		File[] fileArray = cartella.listFiles();
		File primoFile = fileArray[0];
		String nomeFile = primoFile.getName();
		String classname = nomeFile.replace(".java", "");

		int coperture [] = new int [100];

		Random random = new Random();
        int seed;
		
		 System.out.println("GENERAZIONE DI "+Data.get_level()+" LIVELLI SULLA CLASSE "+classname);
		 String comm = "javac "+Data.get_classpath()+"/"+classname+".java";
		 try {
			Process proce = Runtime.getRuntime().exec(comm);
			int exitCo = proce.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 boolean keepAlive=true; 
		 int execCount=1;
		 int vectorCount=0;
		 
		 System.out.println("GENERAZIONE TEST RANDOOP");
         while(keepAlive) {
			seed = random.nextInt();
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String command = "a";
			System.out.println("Esecuzione Randoop n. "+execCount);
			if (os.contains("win")){
				command = "java -classpath "+Data.get_classpath()+";"+Data.get_JarPath()+"/randoop.jar randoop.main.Main gentests --testclass="+classname+" --junit-output-dir="+Data.get_output()+"/testsuites/testsuite"+execCount+" --randomseed="+seed+" --no-error-revealing-tests=true --regression-test-basename=RegressionTest"+execCount+" --time-limit="+120;
			} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
				command = "java -classpath "+Data.get_classpath()+":"+Data.get_JarPath()+"/randoop.jar randoop.main.Main gentests --testclass="+classname+" --junit-output-dir="+Data.get_output()+"/testsuites/testsuite"+execCount+" --randomseed="+seed+" --no-error-revealing-tests=true --regression-test-basename=RegressionTest"+execCount+" --time-limit="+120;
			}
			try {
			Process process = Runtime.getRuntime().exec(command);
			int exitCode = process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Functions.compila_cartella(Data.get_JarPath()+"/hamcrest.jar:"+Data.get_JarPath()+"/junit.jar:"+Data.get_classpath(),  Data.get_output()+"/testsuites/testsuite"+execCount, Data.get_output()+"/testsuites/testsuite"+execCount+"/compilati");
			    	
			Functions.Generate_report(classname, Data.get_classpath(),"RegressionTest"+execCount+".java",Data.get_output()+"/testsuites/testsuite"+execCount,Data.get_JarPath(),"true", Data.get_classpath());	
			coperture[vectorCount]=Functions.valore_copertura(Data.get_output()+"/testsuites/testsuite"+execCount+"/reportRegressionTest"+execCount+"/index.html");
			System.out.println("Copertura esecuzione n. "+execCount+" = "+coperture[vectorCount]+"%");
					
			execCount=execCount+1;	
			if((vectorCount)>0){        
				if(coperture[vectorCount]<=coperture[vectorCount-1]){ 
					keepAlive=false;
				}
			}
			vectorCount = vectorCount+1;
		}
		int g;
		for(g=1; g<=execCount-1; g++)
			Functions.copy_suite_temp(Data.get_output()+"/testsuites/testsuite"+g, Data.get_output(), "RegressionTest"+g+".java");
		
		File filesui = new File(Data.get_output()+"/testsuites");
		Functions.deleteDirectory(filesui);		 
		 
        Functions.selezione_test(Data.get_output(), Data.get_level());
        
		Path sourcePath = Path.of(Data.get_classpath()+"/"+classname+".java");
		Path destinationPath = Path.of(Data.get_output()+"/"+classname+".java");
		Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		
			String os1 = System.getProperty("os.name").toLowerCase();
			String command3="b";
			String command6="c";
			
			
			File folder11=new File(Data.get_output());
			File[] files11=folder11.listFiles();
			if (files11 != null) {
	            for (File file11 : files11) {
	            	String Nametest=file11.getName();
	                if (file11.isFile()&& Nametest.startsWith("RegressionTest")) {
			
			
			if (os.contains("win")){
			    command3 = "javac -cp "+Data.get_JarPath()+"/hamcrest.jar;"+Data.get_JarPath()+"/junit.jar;"+Data.get_output()+"; -d "+Data.get_output()+"/compilati "
			       +Data.get_output()+"/"+Nametest;
			} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			    command3 = "javac -cp "+Data.get_JarPath()+"/hamcrest.jar:"+Data.get_JarPath()+"/junit.jar:"+Data.get_output()+": -d "+Data.get_output()+"/compilati "
			       +Data.get_output()+"/"+Nametest;
			}
			
			    
				try {
					Process process3 = Runtime.getRuntime().exec(command3);
					int exitCode3 = process3.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
		}
	                }
	            }
			}
	            
			
				
				
				
			File folder=new File(Data.get_output());
			String FilePrefix="RegressionTest";
			File[] files=folder.listFiles();
			String NameTest="";
			System.out.println("GENERAZIONE DI "+Data.get_level()+" LIVELLI");
			int q=2;
			if (files != null) {
	            for (File file2 : files) {
	            	NameTest=file2.getName();
	            	if (file2.isFile()&& NameTest.startsWith(FilePrefix)) {

						try {				
							TimeUnit.SECONDS.sleep(5);
						} catch (InterruptedException e) {
						e.printStackTrace();
						}
	                	Functions.Generate_report(classname, Data.get_classpath(),NameTest,Data.get_output(),Data.get_JarPath(),"false", Data.get_classpath());
	                	
	                }
	            }
			}
			
			String filePath = Data.get_classpath()+"/"+classname+".class";
			File file = new File(filePath);
			file.delete();
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
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String filePathh = Data.get_output()+"/"+classname+".class";
			File fileh = new File(filePath);
			fileh.delete();
			String filePathx = Data.get_output()+"/"+classname+".java";
			File filex = new File(filePath);
			filex.delete();
			
			Copertura[] Coverage_class = new Copertura[Data.get_level()];
			
			int o=0;
			String liv [] = new String[Data.get_level()];
			File folder9=new File(Data.get_output());
			File[] files9=folder9.listFiles();
			if (files9 != null) {
	            for (File file9 : files9) {
	            	NameTest=file9.getName();
	                if (file9.isFile()&& NameTest.startsWith("RegressionTest")) {
						String temp = NameTest.replace("RegressionTest", "");
	                	liv[o]=temp.replace(".java","");
						o=o+1;	                	
	                }
	            }
			}
			
			
			for(int i=0; i<Data.get_level(); i++){
				Coverage_class[i] = new Copertura();
				Coverage_class[i].setCoverage(Functions.valore_copertura(Data.get_output()+"/reportRegressionTest"+liv[i]+"/index.html"));
				Coverage_class[i].setTest("RegressionTest"+liv[i]);
			}
			
			Coverage_class = Functions.sortByCoverage(Coverage_class);
			
			int y=1;
			for (int i = 0; i < Data.get_level(); i++) {
				String filej = Coverage_class[i].getTest();
				int coverage = Coverage_class[i].getCoverage();
				System.out.println("Generato livello " + y +" con copertura del " + coverage+"%");
				y=y+1;
			}
			
			Path livda;
			Path liva;
			String repda;
			String repa;
			int l=1;
			
			String output2 = Data.get_output().replace("/temp", "");
			
			for (int gu=0; gu < Data.get_level(); gu++){
				Files.createDirectory(Path.of(output2+"/"+l+"Level"));
				Files.createDirectory(Path.of(output2+"/"+l+"Level/report"));
				livda=Path.of(Data.get_output()+"/"+Coverage_class[gu].getTest()+".java");
				liva=Path.of(output2+"/"+l+"Level/livello"+l+".java");
				repda=Data.get_output()+"/report"+Coverage_class[gu].getTest();
				repa=output2+"/"+l+"Level/report";
				Files.copy(livda, liva, StandardCopyOption.REPLACE_EXISTING);
				Functions.copyFolderContents(repda,repa);
				l=l+1;
			}
			
			try { 
					TimeUnit.SECONDS.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			System.out.println("ELIMINAZIONE FILE TEMPORANEI");
			
			File filej = new File(Data.get_output());
			Functions.deleteDirectory(filej);
			
	
		
			System.out.println("GENERAZIONE DI "+Data.get_level()+" LIVELLI TERMINATA CON SUCCESSO");
			return "GENERAZIONE DI "+Data.get_level()+" LIVELLI TERMINATA CON SUCCESSO";
    	   }
    }
}

    
    
    	
    

    	
    
			
		   

	