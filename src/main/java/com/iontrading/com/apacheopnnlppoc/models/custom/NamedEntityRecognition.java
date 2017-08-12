package com.iontrading.com.apacheopnnlppoc.models.custom;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.iontrading.com.apacheopnnlppoc.models.standard.StandardModels;
import com.iontrading.com.apacheopnnlppoc.trainingsetgenerators.InstrumentTrainingDataGenerator;

import opennlp.tools.namefind.BioCodec;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;


public class NamedEntityRecognition {
	public static void main(String[] args) {
		System.out.println("Preparing training set");
		new InstrumentTrainingDataGenerator().generateTrainingSet();
		System.out.println("Custom training");
		train();
		System.out.println("Custom name finder");
		customNameFinder();
	}

	static void customNameFinder() {
		TokenNameFinderModel model = StandardModels.getTokenNameFinderModel();
		NameFinderME nameFinder = new NameFinderME(StandardModels.getTokenNameFinderModel());
		// prev DE0001143303
		// refdata DE0001142644
		String str = "BID - 37,000,000  US CONV GILT  RegS 1.5 22-Mar-2001 [ DE0001142644 ] S/D 21/";
		String[] strArray = StandardModels.getTokenizer().tokenize(str);
		Span nameSpans[] = nameFinder.find(strArray);

		Arrays.stream(nameSpans).forEach(s -> {
			int start = s.getStart();
			int end = s.getEnd();
			for(int i = start;i<end;i++){
				String isin = strArray[i];
				System.out.println(isin);
			}
		});
	}

	static void train(){
		InputStreamFactory in = null;
		try {
			in = new MarkableFileInputStreamFactory(new File("InstrumentTrainingSet.txt"));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		ObjectStream<NameSample> sampleStream = null;
		try {
			sampleStream = new NameSampleDataStream(
					new PlainTextByLineStream(in, StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TrainingParameters params = new TrainingParameters();
		params.put(TrainingParameters.ITERATIONS_PARAM, 70);
		params.put(TrainingParameters.CUTOFF_PARAM, 1);

		TokenNameFinderModel nameFinderModel = null;
		try {
			Map<String,Object>m=new HashMap<String, Object>();
			nameFinderModel = NameFinderME.train("en", null, sampleStream,
					params, TokenNameFinderFactory.create(null, null, m, new BioCodec()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
			File output = new File("ner-custom-model.bin");
			Files.deleteIfExists(output.toPath());
			FileOutputStream outputStream = new FileOutputStream(output);
			nameFinderModel.serialize(outputStream);
		}catch(Exception e){

		}

	}



	private static void poc1() throws IOException {
		//        Logger log = LoggerFactory.getLogger(BasicNameFinder.class);

		String[] sentences = {
				"Bernard Shaw is a fine artist.",
				"If President John F. Kennedy, after visiting France in 1961 with his immensely popular wife,"
						+ " famously described himself as 'the man who had accompanied Jacqueline Kennedy to Paris,'"
						+ " Mr. Hollande has been most conspicuous on this state visit for traveling alone.",
						"Mr. Draghi spoke on the first day of an economic policy conference here organized by"
								+ " the E.C.B. as a sort of counterpart to the annual symposium held in Jackson"
								+ " Hole, Wyo., by the Federal Reserve Bank of Kansas City. " };

		// Load the model file downloaded from OpenNLP
		// http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin
		TokenNameFinderModel model = new TokenNameFinderModel(new File(
				"en-ner-person.bin"));

		// Create a NameFinder using the model
		NameFinderME finder = new NameFinderME(model);

		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

		for (String sentence : sentences) {

			// Split the sentence into tokens
			String[] tokens = tokenizer.tokenize(sentence);

			// Find the names in the tokens and return Span objects
			Span[] nameSpans = finder.find(tokens);

			// Print the names extracted from the tokens using the Span data
			//            log.info(Arrays.toString(Span.spansToStrings(nameSpans, tokens)));
			System.out.println(Arrays.toString(Span.spansToStrings(nameSpans, tokens)));
		}
	}



}




