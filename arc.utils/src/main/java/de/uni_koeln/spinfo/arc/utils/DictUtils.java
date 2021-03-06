package de.uni_koeln.spinfo.arc.utils;

import com.mongodb.*;
import org.springframework.util.*;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictUtils {

	public static String outputPath = "../arc.data/output/";
	public static String inputPath = "../arc.data/input/";

	private DictUtils() {
		throw new AssertionError();
	}

	public static List<String> handleEntriesWithParenthesis(String filePath)
			throws IOException {

		List<String> list = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {

			String[] cA = currentLine.split("((?<=\\$)|(?=\\$))");

			for (String s : cA) {

				if (s.contains("(")) {

					String[] parA = s.split("\\(");

				}

			}

		}
		reader.close();

		return list;
	}

	public static List<String> getLinesWithFinalDots(String filePath)
			throws IOException {

		List<String> list = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {

			String[] cA = currentLine.split(" ");

			if (cA.length > 1) {

				if (cA[cA.length - 1].endsWith(".")
						&& !Character.isUpperCase(cA[0].codePointAt(0))) {

					list.add(currentLine);

					String next = reader.readLine();

					if (next != null
							&& !Character.isUpperCase(next.codePointAt(0))) {
						list.add(next);
					}

				}

			}

		}

		reader.close();
		return list;

	}

	public static List<String> addTags(String filePath) throws IOException {

		List<String> list = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {

			StringBuilder builder = new StringBuilder();
			builder.append("<E>");
			builder.append(currentLine);
			builder.append("</E>");

			list.add(builder.toString());

		}
		System.out.println("Kandidaten gesamt: " +list.size());
		reader.close();
		return list;

	}

	public static List<String> addTags(List<String> list) throws IOException {

		List<String> taggedList = new ArrayList<String>();

		for (String s : list) {

			StringBuilder builder = new StringBuilder();
			builder.append("<E>");
			builder.append(s);
			builder.append("</E>");

			taggedList.add(builder.toString());
		}

		return taggedList;

	}



	/**
	 * deprecated, use FileUtils fileToList instead!
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<String> txtToList(String filePath) throws IOException {

		List<String> list = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {
			list.add(currentLine);
		}

		reader.close();
		return list;

	}

	public static List<String> cleanTextFile(String filePath)
			throws IOException {

		List<String> list = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);

		Set<String> toAvoid = new HashSet<String>();
		toAvoid.add("m/f");
		toAvoid.add("rn/f");
		toAvoid.add("m/f,");
		toAvoid.add("rn/f,");
		toAvoid.add("m/f;");
		toAvoid.add("rn/f;");
		toAvoid.add("m/f-");
		toAvoid.add("rn/f-");

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {

			String[] clAsA = currentLine.split(" ");

			StringBuilder builder = new StringBuilder();

			for (String s : clAsA) {

				// change false tokens

				s = s.replace("rn", "m");
				s = s.replace("ü.", "v.");
				s = s.replace("u.", "v.");
				s = s.replace("o.", "v.");

				// remove slashes
				if (!toAvoid.contains(s)) {

					String[] sAsA = s.split("((?<=\\/)|(?=\\/))");

					for (String t : sAsA) {

						if (t.equals("/")) {

							t = t.replace("/", "f");

						}

						builder.append(t);
						builder.append(" ");

					}

				} else {

					s = s.replace(s, "m/f");

					builder.append(s);
					builder.append(" ");

				}

			}

			list.add(builder.toString());

			// list.add(builder.toString());

		}

		reader.close();
		return list;

	}

	//
	public static File removeUnclosedParentheses(String filePath,
			String destPath, String newFileName) throws IOException {

		File inFile = new File(filePath);
		File outFile = new File(destPath + newFileName + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "UTF-8"));

		BufferedReader br = null;

		String sCurrentLine;

		br = new BufferedReader(new FileReader(inFile));

		while ((sCurrentLine = br.readLine()) != null) {

			StringBuffer b = new StringBuffer(sCurrentLine);

			Pattern lp = Pattern.compile("\\(");
			Matcher lm = lp.matcher(b);
			int lpcount = 0;
			while (lm.find()) {
				lpcount += 1;
			}

			Pattern rp = Pattern.compile("\\)");
			Matcher rm = rp.matcher(b);
			int rpcount = 0;
			while (rm.find()) {
				rpcount += 1;
			}

			if (lpcount > rpcount) {

				int llp = b.lastIndexOf("(");
				b.replace(llp, llp + 1, "");

			}

			if (b.length() > 3) {
				if (b.subSequence(0, 2).equals("<E>")) {

					writer.append(b + "</E>" + "\n");

				} else {

					writer.append("<E>" + b + "</E>" + "\n");

				}
			}

		}
		br.close();
		writer.flush();
		writer.close();

		return outFile;
	}

	public static File addClosingTag(String filePath, String destPath,
			String newFileName) throws IOException {

		File inFile = new File(filePath);
		File outFile = new File(destPath + newFileName + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "UTF-8"));

		BufferedReader br = null;

		String sCurrentLine;

		br = new BufferedReader(new FileReader(inFile));

		while ((sCurrentLine = br.readLine()) != null) {

			if (sCurrentLine.endsWith("")) {

				String nl = sCurrentLine + "</E>\n";
				writer.append(nl);

			} else

			{
				writer.append(sCurrentLine);

			}

			;

		}
		br.close();
		writer.flush();
		writer.close();

		return outFile;

	}

	public static File sortText(String filePath, String destPath,
			String newFileName) throws IOException {

		File outFile = new File(destPath + newFileName + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "UTF-8"));

		Path f = Paths.get(filePath);

		BufferedReader br = Files.newBufferedReader(f, StandardCharsets.UTF_8);

		String line = br.readLine();

		List<String> lines = new ArrayList<String>();

		while (line != null) {
			lines.add(line);
			line = br.readLine();

		}

		String first = null;

		for (int i = 0; i < lines.size(); i++) {

			first = lines.get(i);

			String fft = first.substring(0, 1);

			if (first.endsWith("")) {

				if (i + 1 >= lines.size()) {
					break;
				}

				String second = lines.get(i + 1);

				fft = first.substring(0, 1);
				String sst = second.substring(0, 1);

				if (fft.equals(sst)) {

					writer.append(first + "</E>\n");

					continue;

				} else {

					writer.append(first);
					continue;
				}

			} else {

				writer.append(first);

			}

		}
		writer.close();
		return outFile;

	}

	public static List lemmasfromNVS(String file) throws IOException {

		List<String> lemmas = new ArrayList<String>();

		BufferedReader br = null;

		String sCurrentLine;

		br = new BufferedReader(new FileReader(file));

		while ((sCurrentLine = br.readLine()) != null) {

			String[] line = sCurrentLine.split("\\s");

			String lemma = line[0].replace("*", "").replace("<E>", "");
			lemmas.add(lemma);

		}
		br.close();
		return lemmas;

	}

	public static List lemmasfromAntlrList(String antlrFilePath)
			throws IOException {

		List<String> antlrLemmas = new ArrayList<String>();

		BufferedReader br = null;

		String sCurrentLine;

		br = new BufferedReader(new FileReader(antlrFilePath));

		while ((sCurrentLine = br.readLine()) != null) {

			String[] line = sCurrentLine.split("\\$");

			String lemma = line[0].replace("*", "");
			antlrLemmas.add(lemma);

		}
		br.close();

		return antlrLemmas;

	}

	public static List<String> diffLemmasNVS(List<String> nvsList,
			List<String> antlrList) {

		List<String> cleaned_nvsList = new ArrayList<String>();

		for (String s : nvsList) {

			s = s.replace("*", "").replace(",", "");
			cleaned_nvsList.add(s);
		}

		cleaned_nvsList.removeAll(antlrList);

		return cleaned_nvsList;

	}

	public static List<String> diffLemmasAntlr(List<String> nvsList,
			List<String> antlrList) {

		List<String> cleaned_nvsList = new ArrayList<String>();

		for (String s : nvsList) {

			s = s.replace("*", "").replace(",", "");
			cleaned_nvsList.add(s);
		}

		antlrList.removeAll(cleaned_nvsList);

		return antlrList;

	}

	public static Map<String, Integer> countPOS(DBCollection collection,
			String dict_pos) {

		Map<String, Integer> occurrences = new HashMap<String, Integer>();

		DBCursor cursor = collection.find();

		List<String> posList = new ArrayList<String>();

		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			DBObject pos = (DBObject) doc.get("pos");

			String nvs_pos = (String) pos.get(dict_pos);
			posList.add(nvs_pos);

		}

		for (String s : posList) {

			Integer count = occurrences.get(s);
			if (count == null) {
				occurrences.put(s, 1);
			} else {
				occurrences.put(s, count + 1);
			}
		}

		return occurrences;

	}

	public static Map<String, Integer> countEaglesPOSFromMongo(
			String collection_name) throws UnknownHostException {
		Map<String, Integer> occurrences = new HashMap<String, Integer>();
		Set<String> posSet = new HashSet<String>(
				Arrays.asList(IPosTags.POS_TAGS_FINAL));

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("arc");
		DBCollection collection = db.getCollection(collection_name);

		DBCursor cursor = collection.find();

		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			DBObject pos = (BasicDBObject) o.get("pos");
			String eagles_pos = (String) pos.get("eagles_pos");

			if (eagles_pos != null) {

				for (String s : posSet) {

					if (eagles_pos.equals(s)) {

						Integer count = occurrences.get(s);
						if (count == null) {
							occurrences.put(s, 1);
						} else {
							occurrences.put(s, count + 1);
						}

					}

				}

			}

		}
		return occurrences;

	}

	// Visit all TXT files in Path
	public static <T> List joinLines(String foldersPath, final List<T> list)
			throws IOException {

		Path start = FileSystems.getDefault().getPath(foldersPath);
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (file.toString().endsWith(".txt")) {
					System.out.println(file.toString());

					try {
						getLines(list, file);
					} catch (IOException e) {

						e.printStackTrace();
					}

				}

				return FileVisitResult.CONTINUE;
			}

		});

		return list;
	}

	public static <T> List getLines(List<T> list, Path file) throws IOException {

		BufferedReader br = Files.newBufferedReader(file,
				StandardCharsets.UTF_8);

		String line;

		while ((line = br.readLine()) != null) {

			list.add((T) line);

		}

		return list;
	}

	public static <K, V> File printMap(Map<K, V> map, String destPath,
			String fileName) throws IOException {

		File file = new File(destPath + fileName + ".txt");
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Map.Entry<K, V> entry : map.entrySet()) {
			out.append(entry.getKey() + " : " + entry.getValue());
			out.append("\n");
		}

		out.flush();
		out.close();

		return file;
	}

	public static <T> File printSet(Set<T> set, String destPath, String filename)
			throws IOException {

		File file = new File(destPath + filename + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Object o : set) {
			writer.append(o + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public static <T> File printList(List<T> list, String destPath,
			String filename) throws IOException {

		File file = new File(destPath + filename + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Object o : list) {
			writer.append(o + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void removeUnclosedParenthesis(String nvs_0_200,
			String output_path, String nvs_0_200_pp) {
	}

	public static List<String> removeCapLetterEntries(List<String> list) {

		List<String> capLetterEntriesRemoved = new ArrayList<String>();

		Pattern capLetterStart = Pattern.compile("^[A-Z]");

		for (String s : list) {

			Matcher matcher = capLetterStart.matcher(s);

			if (!matcher.lookingAt()) {
				capLetterEntriesRemoved.add(s);
			}
		}
		return capLetterEntriesRemoved;
	}

	public static List<String> bracketCorrection(String inputFilePath, String outputPath, String fileName) throws IOException {
		List<String> inputList = FileUtils.fileToList(inputFilePath);
		return bracketCorrection(inputList, outputPath, fileName);
	}

	public static List<String> bracketCorrection(List<String> inputList, String output_data_path, String fileName) throws IOException {

		//List<String> entries = DictUtils.txtToList(inputFilePath);
		List<String> correctedEntries = new ArrayList<String>();

		// for Debug and statistics only
		List<String> corrected = new ArrayList<String>();


		for(String entry : inputList){
			int openingbrackets = StringUtils.countOccurrencesOf(entry, "(");
			int closingbrackets = StringUtils.countOccurrencesOf(entry, ")");

			int dif = openingbrackets - closingbrackets;

			if (dif > 0) {
				//System.out.println(entry);
				corrected.add(entry);
				for(int i = 0; i < dif; i++) {
					entry = entry + ")";
				}
				corrected.add(entry);
			}
			correctedEntries.add(entry);

		}
		DictUtils.printList(corrected,output_data_path,"infoOutputBrackets");
		System.out.println("Es wurden in " + corrected.size()/2 + " Zeilen fehlende schließende Klammern ergänzt.");
		DictUtils.printList(correctedEntries,output_data_path,fileName);

		return correctedEntries;
	}

	public static List<String> removeIndentedLines (String inFilePath, String outputPath, String fileName) throws IOException {

		List<String> entryList = FileUtils.fileToList(inFilePath);
		return removeIndentedLines(entryList, outputPath, fileName);
	}

	public static List<String> removeIndentedLines (List<String> entryList, String outputPath, String fileName) throws IOException {

		List<String> resultList = new ArrayList<String>();
		List<String> dumpedList = new ArrayList<String>();

		String currentLine;

		// Pattern for lines starting with 3 or more blanks
		Pattern pattern = Pattern.compile("^(<E>)?  [ ]+");

		for (String entry : entryList) {

			// Create matcher object
			Matcher matcher = pattern.matcher(entry);

			// Check if the matcher's prefix match with the matcher's pattern
			if (!matcher.lookingAt()) {
				// add line to list
				resultList.add(entry);
			} else {
				dumpedList.add(entry);
			}
		}

		DictUtils.printList(dumpedList,outputPath,fileName);
		return resultList;
	}

	public static List<String> splitTwoColumnLines (String inFilePath, String outputPath, String fileName) throws IOException {

		List<String> entryList = FileUtils.fileToList(inFilePath);
		return splitTwoColumnLines(entryList, outputPath, fileName);
	}

	public static List<String> splitTwoColumnLines (List<String> entryList, String outputPath, String fileName) throws IOException {

		List<String> resultList = new ArrayList<String>();

		for (String entry : entryList) {

			String[] splits = entry.split("[ ]{4,}");
			for (int i = 0; i < splits.length; i++) {
				resultList.add(splits[i]);
			}
		}

		DictUtils.printList(resultList, outputPath, fileName);
		return resultList;
	}

	public static void separateLemmasShorterThan(String filePath,String outPath, int lemmalength) throws IOException {
		List<String> shortLemmas = new ArrayList<String>();
		List<String> longerLemmas = new ArrayList<String>();

		List<String> extraction = FileUtils.fileToList(filePath);
		for (String entry : extraction) {
			String[] entryComps = entry.split("\\$");

			if (entryComps[0].length() < lemmalength) {
				shortLemmas.add(entry);
			} else {
				longerLemmas.add(entry);
			}
		}

		printList(longerLemmas, outPath, "lemmasLongerThan"+lemmalength);
		printList(shortLemmas, outPath, "lemmasShorterThan"+lemmalength);
	}

	public static void separateLemmasWithLengthOf(String filePath,String outPath, int lemmalength) throws IOException {
		List<String> shortLemmas = new ArrayList<String>();
		List<String> longerLemmas = new ArrayList<String>();

		List<String> extraction = FileUtils.fileToList(filePath);
		for (String entry : extraction) {
			String[] entryComps = entry.split("\\$");

			if (entryComps[0].length() == lemmalength) {
				shortLemmas.add(entry);
			} else {
				longerLemmas.add(entry);
			}
		}

		printList(longerLemmas, outPath, "lemmasWithLengthOf"+lemmalength);
		printList(shortLemmas, outPath, "lemmasWithOtherLengthsThan"+lemmalength);
	}

	public static void findGermanLemmas(String germanwordlistPath, String rumantschDictPath, String outPath) throws IOException {
		List<String> germanWords = FileUtils.fileToList(germanwordlistPath);
		System.out.println(germanWords.size());
		List<String> rumantschWords= new ArrayList<String>();


		System.out.println(germanWords.get(25)+ germanWords.get(25).length());

		List<String> rumantschDict = FileUtils.fileToList(rumantschDictPath);
		for (String entry : rumantschDict) {
			String[] entryComps = entry.split("\\$");

			rumantschWords.add(entryComps[0]);
		}
		System.out.println("RumantschWordsList erzeugt");

		DictUtils.printList(rumantschWords, outPath, "RumantschWordList");

		List<String> removedWords = new ArrayList<String>();
		removedWords.addAll(rumantschWords);

		System.out.println("Start removeAll");
		System.out.println("rumantschWords before:" +rumantschWords.size());
		rumantschWords.removeAll(germanWords);
		System.out.println("rumantschWords after:" +rumantschWords.size());
		DictUtils.printList(rumantschWords, outPath, "retainedRumantschWords");
		System.out.println("removeAll fertig");

		System.out.println("Start retainAll");
		System.out.println("removedWords before:" +removedWords.size());
		removedWords.removeAll(rumantschWords);
		System.out.println("removedWords after:" +removedWords.size());
		DictUtils.printList(removedWords, outPath, "removedGermanWords");
		System.out.println("retainAll fertig");

	}

	public static void removeGermanLemmas(String foundGermanWordsListPath, String rumantschDictPath, String outPath) throws IOException {
		List<String> foundGermanWords = FileUtils.fileToList(foundGermanWordsListPath);

		List<String> rumantschDict = FileUtils.fileToList(rumantschDictPath);
		System.out.println("rumantschDict has " + rumantschDict.size() + " enties.");
		List<String> resultDict = new ArrayList<String>();

		for (String entry : rumantschDict) {
			String[] entryComps = entry.split("\\$");
			if (!foundGermanWords.contains(entryComps[0])) {
				resultDict.add(entry);
			} else {
				System.out.println(entry + "wird entfernt.");
			}
		}
		System.out.println("resultDict has " + resultDict.size() + " enties. These are " + (rumantschDict.size()-resultDict.size()) + "less entries.");
		DictUtils.printList(resultDict, outPath, "GermanLemmasRemoved");
	}

	public static void replaceDots(String inFilePath, String outPath, String fileName) throws IOException {

		List<String> dict = FileUtils.fileToList(inFilePath);
		List<String> newDict = new ArrayList<String>();

		for (String fullentry : dict) {
			String[] entry = fullentry.split("\\$");

			entry[0] = entry[0].replaceAll("ạ", "a");
			entry[0] = entry[0].replaceAll("ḅ", "b");
			//entry[0].replaceAll("ḍ", "c");
			entry[0] = entry[0].replaceAll("ḍ", "d");
			entry[0] = entry[0].replaceAll("ẹ", "e");
			//entry[0].replaceAll("ḅ", "f");
			//entry[0].replaceAll("ḅ", "g");

			entry[0] = entry[0].replaceAll("ḥ", "h");
			entry[0] = entry[0].replaceAll("ị", "i");
			//entry[0].replaceAll("ḅ", "j");
			entry[0] = entry[0].replaceAll("ḳ", "k");
			entry[0] = entry[0].replaceAll("ḷ", "l");
			entry[0] = entry[0].replaceAll("ṃ", "m");
			entry[0] = entry[0].replaceAll("ṇ", "n");
			entry[0] = entry[0].replaceAll("ọ", "o");
			//entry[0].replaceAll("ḅ", "p");
			//entry[0].replaceAll("ḅ", "q");
			entry[0] = entry[0].replaceAll("ṛ", "r");
			entry[0] = entry[0].replaceAll("ṣ", "s");
			entry[0] = entry[0].replaceAll("ṭ", "t");
			entry[0] = entry[0].replaceAll("ụ", "u");
			entry[0] = entry[0].replaceAll("ṿ", "v");
			entry[0] = entry[0].replaceAll("ẉ", "w");
			//entry[0].replaceAll("ḅ", "x");
			//entry[0].replaceAll("ḅ", "y");
			entry[0] = entry[0].replaceAll("ẓ", "z");

			// was ist mit dem kombinierenden Zeichen . (U+0323) kommt es überhaupt vor?
			newDict.add(entry[0]+"$"+entry[1]);
		}

		DictUtils.printList(newDict, outPath, fileName);
	}

}
