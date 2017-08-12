package com.iontrading.com.apacheopnnlppoc.trainingsetgenerators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class InstrumentTrainingDataGenerator {
	public static void main(String[] args) {
		new InstrumentTrainingDataGenerator().generateTrainingSet();
	}

	public void generateTrainingSet() {
		List<String> sentences = getListOfTrainingData(getInstrumentsFromTSV());

		File output = new File("InstrumentTrainingSet.txt");
		try {
			if(Files.deleteIfExists(output.toPath())) {
				System.out.println("Old output was deleted");
			}
			PrintWriter printWriter = new PrintWriter(output);
			sentences.forEach(sentence -> {
				printWriter.println(sentence);
			});
			System.out.println("Training set prepared.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> getListOfTrainingData(Map<String, Instrument> instruments){
		List<String> list = new LinkedList<>();
		final String START_INSTRUMENTID = "<START:INSTRUMENTID>";
		final String START_ISIN = "<START:ISIN>";
		final String START_DESC = "<START:DESC>";
		final String START_SQUARE_BRACE = "[";
		final String END_SQUARE_BRACE = "]";
		final String END = "<END>";
		final String WHITESPACE = " ";

		instruments.values().forEach(instrument ->{
			StringBuilder builder = new StringBuilder();
			builder.append("BID - 37,000,000  ");
			
			// append instrument id
//			if(instrument.getInstrumentID() != null && instrument.getInstrumentID().length() > 0) {
//				builder.append(START_INSTRUMENTID)
//				.append(WHITESPACE)
//				.append(instrument.getInstrumentID())
//				.append(WHITESPACE)
//				.append(END)
//				.append(WHITESPACE);
//			}
			
			//append desc
			if(instrument.getDescription() != null && instrument.getInstrumentID().length() > 0) {
				builder.append(START_DESC)
				.append(WHITESPACE)
				.append(instrument.getDescription())
				.append(WHITESPACE)
				.append(END);
			}

			// append isin
			if(instrument.getIsin() != null && instrument.getIsin().length() > 0) {
				builder.append(START_ISIN)
				.append(START_SQUARE_BRACE)
				.append(WHITESPACE)
				.append(instrument.getIsin())
				.append(WHITESPACE)
				.append(END_SQUARE_BRACE)
				.append(END)
				.append(WHITESPACE);
			}

			

			if(builder.length() > 0) {
				list.add(builder.toString());
			}
		});
		return list;
	}

	public Map<String, Instrument> getInstrumentsFromTSV(){
		Map<String, Instrument >instruments = new HashMap<>();
		int count = 0;

		String csvFile = "RefData_Data_TSV.txt";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(csvFile));
			String line = "";
			while((line = reader.readLine()) != null){
				if(count > 0) {
					// skipping header
					String[] values = line.split("\t");
					/*
					 * InstrumentId: 1
					 * Isin: 4
					 * Desc: 6
					 * IssueId: 7
					 * CurrencyStr: 33
					 * Date: 35
					 */
					String instrumentId = values[1];
					String isin = values[4];
					String desc = values[6];
					String currencyStr = values[33];
					String date = values[35];

					instruments.put(instrumentId, new Instrument(instrumentId, isin, desc, currencyStr, date));
				}
				count++;
			}
		} catch (Exception e) {
		}
		return instruments;
	}

	public Map<String, Instrument> getInstrumentsFromCSV(){
		Map<String, Instrument >instruments = new HashMap<>();
		int count = 0;
		int totalCount = 0;

		String csvFile = "RefData_Data_CSV.csv";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(csvFile));
			String line = "";
			while((line = reader.readLine()) != null){
				totalCount++;
				if(line.split(",").length != 39){
					System.out.println(line);
					count++;
				}
			}
		} catch (Exception e) {
		}

		System.out.println("###################################");
		System.out.println(count+" number of lines are inconsistent, out of "+totalCount+" number of lines");
		System.out.println("###################################");
		return instruments;
	}



}
