package com.iontrading.com.apacheopnnlppoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.SystemInputStreamFactory;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import com.iontrading.com.apacheopnnlppoc.models.standard.StandardModels;


public class BasicModelsPOC 
{
	public static void main( String[] args ) throws Exception
	{
		System.out.println("Parser");
		parser();
		System.out.println();
		System.out.println("sentence detection");
		sentenceDetector();
		System.out.println();
		System.out.println("tokenizer");
		tokenizer();
		System.out.println();
		System.out.println("namefinder");
		namefinder();
		System.out.println();
		System.out.println("pos tagger - part of speech");
		postagger();
		System.out.println();
		System.out.println("chunker");
		chunker();
	}

	static void parser(){
		Parser parser = ParserFactory.create(StandardModels.getParserModel());
		String sentence = "Hi, how are you?";
		Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
		Arrays.stream(topParses).forEach(parse -> parse.show());
	}

	static void sentenceDetector(){
		SentenceModel model = StandardModels.getSentenceModel();
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		String paragraph = "Hi. How are you? This is Mike.";
		String sentences[] = sdetector.sentDetect(paragraph);
		Arrays.stream(sentences).forEach(sentence -> System.out.println(sentence));
	}


	static void tokenizer() throws Exception{
		Tokenizer tokenizer = StandardModels.getTokenizer();
		String tokens[] = tokenizer.tokenize("Hi. How are you? This is Mike.");
		Arrays.stream(tokens).forEach(token -> System.out.println(token));
	}

	static void namefinder() throws IOException {

		NameFinderME nameFinder =StandardModels.getNameFinderModel();
		Tokenizer tokenizer = StandardModels.getTokenizer();
		String str = "Mike Smith is a good person";
		Span nameSpans[] = nameFinder.find(tokenizer.tokenize(str));
		Arrays.stream(nameSpans).forEach(span -> System.out.println(span));
	}

	// TODO: EXPLORE more on USAGE
	// POS labels: https://stackoverflow.com/questions/1833252/java-stanford-nlp-part-of-speech-labels
	static void postagger() throws IOException{
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = StandardModels.getPOSTaggerME();

		//		String input = "Hi. How are you? This is Mike.";
		ObjectStream<String> lineStream = new PlainTextByLineStream(new SystemInputStreamFactory(), 
				SystemInputStreamFactory.encoding());

		perfMon.start();
		String line;
		for(int i = 0;i<1;i++){
			line = lineStream.read();
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);

			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());

			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
	}

	// TODO: EXPLORE more on USAGE
	static void chunker() throws IOException{
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = StandardModels.getPOSTaggerME();

		//		String input = "Hi. How are you? This is Mike.";
		ObjectStream<String> lineStream = new PlainTextByLineStream(new SystemInputStreamFactory(), SystemInputStreamFactory.encoding());

		perfMon.start();
		String line;
		String whitespaceTokenizerLine[] = null;

		String[] tags = null;
		//		while ((line = lineStream.read()) != null) {
		for(int i = 0;i<1;i++){
			line = lineStream.read();
			whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			tags = tagger.tag(whitespaceTokenizerLine);

			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());
			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();

		// chunker
		ChunkerME chunkerME = StandardModels.getChunkerME();
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);

		Arrays.stream(result).forEach(str -> System.out.println(str));

		Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		Arrays.stream(span).forEach(s -> System.out.println(s));
	}
}
