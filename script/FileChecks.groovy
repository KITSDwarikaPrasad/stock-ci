//import java.util.Date
import java.nio.file.Files
import java.nio.file.Paths

def rootDir = pwd()
println("rootDir: $rootDir")

def splitCsvFile() {
	// def stdout = sh(script: 'git config --get remote.origin.url', returnStdout: true)
	//println stdout
	// stdout = sh(script: 'sh script/pre-run.sh', returnStdout: true)
	//echo "rename the existing file SAPR3toStockAPI.csv --> SAPR3toStockAPI.csv_bkp"
	// replace cp --> mv
	//sh 'cp /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv_bkp'
	echo "invoking BODS Job -- dummy"
//	sh 'sed  \'1,1000p\' /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv > ~/SAPR3toStockAPI_1000.csv'
//	sh'sed \'1,1010p\' /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv > ~/SAPR3toStockAPI_1010.csv'
//	def lines = sh(script: 'script/pre-run.sh', returnStdout: true).split("\r?\n")

}

//check if csv file exists
def checkFileExists(String filePath) {
	def csvExists = fileExists(filePath)
	if(csvExists) {
		println("$filePath file found")
	} else {
		println("$filePath file not found")
	}
	return csvExists
}
//create the backup of old csv file before invoking the BODS job
def createBackupFile(directory, fileName) {
	
	//Files.copy(Paths.get(filePath), Paths.get("$directory$fileName$extention"+"_bkp"))

	new File(directory, fileName).renameTo(new File(directory,fileName + "_bkp"))
	println("Backup file created :$fileName/_bkp")
}

//To be invoked from jenkins file before invoking BODS job
def preBODSProcess(String directory, String fileName, String extention) {
	println("executing preBODSProcess.. ")
	String filePath = "$directory$fileName$extention"
	println("filePath : $filePath")
	//process csv file
	//chek if any existing csv file exists already
	checkFileExists(filePath)
	//hold the zip file - find out the latest zip file
	File oldLastModFile = new File( directory ).listFiles()?.sort { -it.lastModified() }?.head()
	println("oldLastModFile :"+ oldLastModFile.getName())
	createBackupFile(directory,"$fileName$extention")
	
	return oldLastModFile.getName()
	
}


//To be invoked from jenkins file afetr invoking BODS job
def postBODSProcess(String directory, String fileName, String extention) {
	String filePath = "$directory$fileName$extention"
	//check if the new csv file is present now
	if(checkFileExists(filePath)) {
		//check if new file has some bytes
		File file = new File(filePath)
		if(${file.length()} != 0) {
			
		} else {
			pritln("New csv file is empty")
			//check if zip file was created
		}
	}
	
	
}

def fileChecksNSort(String directory, String fileName, String extention) {
	//check if csv file exists
	//def csvExists = fileExists('/BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv')
	def csvExists = fileExists(directory + fileName + "." + extention)
	if(csvExists) {
		echo "csv file found"
	} else {
		echo "csv file not found"
	}
	
	//Check if zip file exists
	
	
	
	def  today = new Date().format('yyyyMMdd').toString()
	println("today : "+today)
	def zipFile = new FileNameFinder().getFileNames(directory , fileName +"_" + today + "*.zip")
	//def zipExists = {!zipFile.isEmpty()}
//	 if(!zipFile.isEmpty()) {
//		 println("zip file found :$zipFile");
//	 }
	println("zipFile/s: "+zipFile)
	 
	
	//return csvExists
}


def otherExampleMethod() {
	println("otherExampleMethod");
}
return this