package com.iontrading.com.apacheopnnlppoc.models.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerFactory;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.BaseModel;

public class CustomModels {
	public static Tokenizer getTokenizer(){
		final File customFile = new File("InstrumentTrainingSet_Tokenizer.txt");
		trainAndSerializeTokenizer(customFile, "en-custom-token.bin", 70, 1);
		Tokenizer tokenizer = null;
		try {
			final InputStream is = new FileInputStream("en-token.bin");
			try {
				final TokenizerModel model = new TokenizerModel(is);
				tokenizer = new TokenizerME(model);
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tokenizer;
	}
	
	// Internal utility methods
	private static void trainAndSerializeTokenizer(final File customInput, 
			final String outputFileName,
			final int trainingIterations,
			final int cutOffParam) {

		InputStreamFactory in = null;
		try {
			in = new MarkableFileInputStreamFactory(customInput);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		ObjectStream<TokenSample> sampleStream = null;
		try {
			sampleStream = new TokenSampleStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TrainingParameters params = getTrainingParams(trainingIterations, cutOffParam);

		TokenizerModel tokenizerModel = null;
		try {
			Map<String,Object>m=new HashMap<String, Object>();
			tokenizerModel = TokenizerME.train(sampleStream,
				 TokenizerFactory.create(null, null, null,
						 true, // use alphanumeric optimisation
						 null
						 ),
				 params);

		} catch (IOException e) {
			e.printStackTrace();
		}
		serializeToSystemRecognizableTrainingSet(outputFileName, tokenizerModel);
	}

	private static void serializeToSystemRecognizableTrainingSet(final String outputFileName,
			BaseModel model) {
		try{
			File output = new File(outputFileName);
			Files.deleteIfExists(output.toPath());
			FileOutputStream outputStream = new FileOutputStream(output);
			model.serialize(outputStream);
		}catch(Exception e){

		}
	}

	private static TrainingParameters getTrainingParams(final int trainingIterations, final int cutOffParam) {
		TrainingParameters params = new TrainingParameters();
		params.put(TrainingParameters.ITERATIONS_PARAM, trainingIterations);
		params.put(TrainingParameters.CUTOFF_PARAM, cutOffParam);
		return params;
	}
}
