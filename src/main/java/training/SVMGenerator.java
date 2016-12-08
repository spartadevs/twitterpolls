package training;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import models.Vocabulary;
import util.PropertyReader;
import util.TwitterPollsUtils;

import com.opencsv.CSVReader;

public class SVMGenerator {

	/*
	 * args[0] - Training File Path args[1] - Output SVM File Path
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("In sufficient input arguements");
			System.exit(0);
		}
		Vocabulary vocab = new Vocabulary();
		String soruce = args[0];
		String target = args[1];
		String[] nextLine;
		
		// reading the CSV file

		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(target, false), "utf-8"));
			 CSVReader reader = new CSVReader(new FileReader(soruce))) {
			while ((nextLine = reader.readNext()) != null){
				//System.out.println(nextLine.length);
				try{
					StringBuffer svmSb = new StringBuffer(nextLine[0]);
					String tweet = nextLine[5];
					String vectorString = TwitterPollsUtils.generateVectorString(tweet, vocab);
					svmSb.append(vectorString).append("\n"); 
					writer.append(svmSb.toString());
					writer.flush();
				}catch(Exception e){
					System.err.println("Ignoring Error while processing a input line");
				}
				
				
			}
			//Serialize Vocabulary
			//PropertyReader.loadPropertiesFromFile("resources/props/main.props");
			
	
			TwitterPollsUtils.dumpVocabulary(vocab, PropertyReader.get().getProperty("vocabulary.persistence.location"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}