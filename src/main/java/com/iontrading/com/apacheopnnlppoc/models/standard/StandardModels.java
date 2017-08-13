package com.iontrading.com.apacheopnnlppoc.models.standard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class StandardModels {

	public static TokenNameFinderModel getTokenNameFinderModel(){
		TokenNameFinderModel tokenNameFinderModel = null;
		try {
			InputStream is = new FileInputStream("ner-custom-model.bin");
			try {
				tokenNameFinderModel = new TokenNameFinderModel(is);
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tokenNameFinderModel;
	}
	
	public static SentenceModel getSentenceModel(){
		SentenceModel sentenceModel = null;
		InputStream is;
		try {
			is = new FileInputStream("en-sent.bin");
			try {
				sentenceModel = new SentenceModel(is);
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return sentenceModel;
	}
	
	public static ParserModel getParserModel(){
		ParserModel parserModel = null;
		try {
			InputStream is = new FileInputStream("en-parser-chunking.bin");
			try {
				parserModel = new ParserModel(is);				
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parserModel;
	}
	
	public static ChunkerME getChunkerME(){
		return new ChunkerME(getChunkerModel());
	}
	
	public static ChunkerModel getChunkerModel(){
		ChunkerModel chunkerModel = null;
		try {
			InputStream is = new FileInputStream("en-chunker.bin");
			try {
				chunkerModel = new ChunkerModel(is);
				is.close();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chunkerModel;
	}
	
	public static POSModel getPOSModel(){
		POSModel posModel = null;
		posModel = new POSModelLoader().load(new File("en-pos-maxent.bin"));
		return posModel;
	}
	
	public static POSTaggerME getPOSTaggerME(){
		return new POSTaggerME(getPOSModel());
	}
	
	public static Tokenizer getTokenizer(){
		Tokenizer tokenizer = null;
		try {
			InputStream is = new FileInputStream("en-token.bin");
			try {
				TokenizerModel model = new TokenizerModel(is);
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

	public static NameFinderME getNameFinderModel(){
		NameFinderME nameFinder = null;
		TokenNameFinderModel tokenNameFinderModel = null;
		InputStream is;
		try {
			is = new FileInputStream("en-ner-person.bin");
//			is = new FileInputStream("en-custom-model.bin");
			try {
				tokenNameFinderModel = new TokenNameFinderModel(is);
				nameFinder = new NameFinderME(tokenNameFinderModel);
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nameFinder;
	}
}
