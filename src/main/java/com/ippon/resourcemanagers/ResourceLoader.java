package com.ippon.resourcemanagers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class ResourceLoader {

	public static String sterilizeText(String text) {
		if (text == null)
			return "";
		return " " + text.toLowerCase().replaceAll("\\b((https?:|www.)[a-zA-Z0-9./?=]+)\\b", " ").replaceAll("\n", " ")
				.replaceAll("[^a-zA-Z0-9_]", " ").replaceAll("  +", " ") + " ";
	}

	public static Pattern loadWordsToRegex(String filename) {
		List<String> lines = loadWords(filename);
		LinkedList<String> linesWithWordWrappers = new LinkedList<>();
		String regex = String.join("|", lines ).toLowerCase();
		// this makes sure it matches a whole word, not some sub-set of a word.
		regex = "\\b("+regex + ")+\\b";
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	public static List<String> loadWords(String filename) {
		List<String> lines;
		Path p = Paths.get(filename);
		try {
			lines = Files.readAllLines(p);
		} catch (IOException e) {
			lines = new ArrayList<String>();
			try {
				Files.createFile(Paths.get(filename));
				if (filename.equals("userAuthInfo.txt"))
					Files.write(p, "Consumer Key\nSecret\nAccess Token\nAccess Token Secret".getBytes());

			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
		System.out.println("Loaded " + lines.size() + " words from " + filename);
		return lines;
	}

	protected static boolean deleteDirectory(File dir) {
		if (!dir.isDirectory())
			return false;

		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			boolean success = deleteDirectory(children[i]);
			if (!success)
				return false;
		}
		// either file or an empty directory
		System.out.println("removing file or directory : " + dir.getName());
		return dir.delete();
	}

	protected static <S, T> void saveToCSV(File f, Map<S, T> map, String... cols) {
		try {
			Files.write(f.toPath(), (String.join(",", cols) + "\n").getBytes());
			for (Entry<S, T> e : map.entrySet()) {
				Files.write(f.toPath(), (e.getKey().toString() + "," + e.getValue().toString() + "\n").getBytes(),
						StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
