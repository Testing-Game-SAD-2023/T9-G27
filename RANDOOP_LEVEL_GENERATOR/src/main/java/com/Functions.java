package com;


import java.io.File;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Functions {

		
	//METODO PER ESTRARRE DA UN REPORT IL VALORE DI COPERTURA
	public static int valore_copertura(String hPath) throws IOException, NoSuchFileException {
		File htmlFile = new File(hPath);
		Document doc = Jsoup.parse(htmlFile, "UTF-8");
		Element coverageElement = doc.getElementById("c0");
		String coverageValueString = coverageElement.text().replace("%", "");
		int coverageValue = Integer.parseInt(coverageValueString);
		return coverageValue;
	}
		
		
	//METODO PER PORTABILITA' TRA OS
	public static void Identify_OS(String os) {
		  os = System.getProperty("os.name").toLowerCase();
	}
		
		
	//METODO PER GENERARE REPORT JACOCO
	public static void Generate_report(String classname,String inPath,String testName, String outPath, String jarPath,String ap, String classpath) throws IOException, NoSuchFileException{
		String os = System.getProperty("os.name").toLowerCase();
		String command6="c";
		try {
			
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (os.contains("win")){
			command6 = "java -javaagent:"+jarPath+"/jacocoagent.jar -cp "+jarPath+"/hamcrest.jar;"+jarPath+"/junit.jar;"
					+outPath+"/compilati;"+classpath+" org.junit.runner.JUnitCore "+testName.substring(0, testName.length() - 5);
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			command6 = "java -javaagent:"+jarPath+"/jacocoagent.jar -cp "+jarPath+"/hamcrest.jar:"+jarPath+"/junit.jar:"
					+outPath+"/compilati:"+classpath+": org.junit.runner.JUnitCore "+testName.substring(0, testName.length() - 5);
		}
		try {
			Process process6 = Runtime.getRuntime().exec(command6);
			int exitCode6 = process6.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String ReportName=testName.substring(0, testName.length() - 5)+"_report.html";
		String command7 = "java -jar "+jarPath+"/jacococli.jar report jacoco.exec --classfiles "+inPath+" --sourcefiles "
				+inPath+" --html "+outPath+"/report"+testName.substring(0, testName.length() - 5)+" --name "+ReportName;
		try {
			Process process7 = Runtime.getRuntime().exec(command7);
			int exitCode7 = process7.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ap=="false") {	
			File filew = new File("/jacoco.exec");
			filew.delete();
			try {
				TimeUnit.SECONDS.sleep(5); 
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	//METODO PER ORDINARE I TEST IN BASE AL VALORE CRESCENTE DI COPERTURA
	 public static Copertura[] sortByCoverage(Copertura[] array) {
        Arrays.sort(array, new Comparator<Copertura>() {
            public int compare(Copertura instance1, Copertura instance2) {
                int result = Integer.compare(instance1.getCoverage(), instance2.getCoverage());
                if (result == 0) {
                    return instance1.getTest().compareTo(instance2.getTest());
                }
                return result;
            }
        });
        return array;
    }
	
	//METODO PER COPIARE IL CONTENUTO DI UNA CARTELLA
	public static void copyFolderContents(String sourceFolder, String destinationFolder) throws IOException {
        Path sourcePath = Paths.get(sourceFolder);
        Path destinationPath = Paths.get(destinationFolder);
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path destination = destinationPath.resolve(sourcePath.relativize(file));
                Files.createDirectories(destination.getParent());
                Files.copy(file, destination, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path destination = destinationPath.resolve(sourcePath.relativize(dir));
                Files.createDirectories(destination);
                return FileVisitResult.CONTINUE;
            }
        });
    }
	
	
	//METODO PER ELIMINARE UNA CARTELLA
    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
	
	//METODO PER COMPILARE TUTTI I FILE IN UNA CARTELLA
	public static void compila_cartella(String classpath,  String testSuitePath, String compiledPath) throws IOException, NoSuchFileException  {
		File compiledDirectory = new File(compiledPath);
        compiledDirectory.mkdirs();
        File testSuiteDirectory = new File(testSuitePath);
        File[] javaFiles = testSuiteDirectory.listFiles((dir, name) -> name.endsWith(".java")); 
        ProcessBuilder compilerProcessBuilder = new ProcessBuilder();
		compilerProcessBuilder.command("javac", "-cp", classpath, "-d", compiledPath);
		for (File javaFile : javaFiles) {
            compilerProcessBuilder.command().add(javaFile.getAbsolutePath());
        }
			
			try {
				Process compilerProcess = compilerProcessBuilder.start();
				int exitCodecom = compilerProcess.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		

	//SELEZIONE DEI TEST PER I LIVELLI
     public static void selezione_test(String folderPath, int n) {
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		Arrays.sort(files, Comparator.comparingLong(File::lastModified));
			int totalFiles = files.length;
			int soglia=totalFiles/n;
			File [] savedFiles=new File[n];
			
			for(int i=0;i<n;i++) {
				savedFiles[i]=files[i*soglia];
			}
			
			for(int j=0;j<totalFiles;j++) {
				int canc=0;
				for(int k=0;k<n;k++) {
					if(savedFiles[k]==files[j]) canc++;
				}
				if(canc==0) files[j].delete();
			}
				
	}


	
	//METODO PER UNIRE I TEST SUITE
    public static void copy_suite_temp(String sourceFolder, String destinationFolder, String excludedFileName) {
        File sourceDir = new File(sourceFolder);
        File destinationDir = new File(destinationFolder);
        File[] files = sourceDir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (!fileName.startsWith("RegressionTest") || fileName.equals(excludedFileName)) {
                    continue;
                }
                File destinationFile = new File(destinationDir, fileName);
                try {
                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                }
            }
        }
    }


   
}

	
		


	


