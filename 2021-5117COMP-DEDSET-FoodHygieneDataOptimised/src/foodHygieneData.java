import java.io.File;
import java.lang.Comparable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.Reader;
import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.Set;

import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Date;

import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVRecord;

public class foodHygieneData implements Comparable<foodHygieneData> {

	public final String SEP = ",";

	public String fhrsid;

	public String localAuthorityBusinessID;

	public String businessName;

	public String businessType;

	public String businessTypeID;

	public String addressLine1;

	public String addressLine2;

	public String addressLine3;

	public String addressLine4;

	public String postcode;

	public String ratingValue;

	public String ratingKey;

	public boolean rating_Dates;

	public String localAuthorityCode;

	public String localAuthorityName;

	public String localAuthorityWebSite;

	public String localAuthorityEmailAddress;

	public String schemeType;

	public boolean newRatingPending;

	public double geocodeLongitude;

	public double geocodeLatitude;

	public String ratingDate;

	public String scoreHygiene;

	public String scoresStructural;

	public String scoreConfidenceInManagement;

	private Boolean ratingDates;

	public enum foodHygieneSort {
		RATING_VALUE, RATING_DATE, BUSINESS_NAME, LocalAuthorityName, ScoreHygiene, fhrsid

	}

	public enum SortAlgo {
		COLLECTIONS, INSERT, HEAP, BST
	}

	public foodHygieneData() {

	}

	public static ArrayList<String> recordedData = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		BSTDupNode<foodHygieneData> data = new BSTDupNode<foodHygieneData>(null);
		ArrayList<foodHygieneData> foodHygiene = new ArrayList<foodHygieneData>();
		File folder = new File("files.csv");

		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			if (file.isFile()) {

				ArrayList<foodHygieneData> fileData = readFile(file.getAbsolutePath());

				foodHygiene.addAll(fileData);

			}

		}

		boolean quit = false;

		while (!quit) {

			System.out.println("--MENU--");

			System.out.println("A. Local Authorities");

			System.out.println("B. Business Name");

			System.out.println("C. Search for a Rating/Score for a specified Business");

			System.out.println("D. Select Rating");

			System.out.println("F. Search for an organisation with i. least or ii. most satisfactory ratings");

			System.out.println(
					"G. Select Local Authority and Organisation with Most Rating scores or Most Rating 0 or 1 scores");

			System.out.println("H. Create a Rating for a business");

			System.out.println("Q. Quit");

			Scanner console = new Scanner(System.in);

			String choice = console.next();

			switch (choice) {

			case "A":

// Prints out unique Local Authorities

				outputLocalAuthority(foodHygiene);

				break;

			case "B":

// Prints out unique Businesses

				outputBusiness(foodHygiene);

				break;

			case "C":

// prints out Ratings for a user specified Business in chronological order
				outputRating(foodHygiene);

				break;

			case "D":

// Prints out selected Ratings

				SelectRating(foodHygiene);

			case "E":

// Prints out selected Ratings

				Filter(foodHygiene);

			case "F":
// Finds least or most satisfactory ratings for an organisation and outputs
// details
				Organisation(foodHygiene);

			case "G":
// Finds organisation with most rating 5 scores or most rating 0 or 1 scores and
// outputs details

				SelectRatingScore(foodHygiene);

			case "H":
// SHOWCASE FEATURE - user enters a business and their details output in a text
// file
				CreateRating(foodHygiene);

			}

		}

	}

	public static ArrayList<foodHygieneData> readFile(String file) throws IOException {

		ArrayList<foodHygieneData> foodHygiene = new ArrayList<foodHygieneData>();

		Reader in = new FileReader(file);

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in);

		File csvFile = new File(file);

		Scanner scan = new Scanner(csvFile);

		scan.nextLine();

		for (CSVRecord record : records) {

			foodHygieneData foodHyg = new foodHygieneData();

			foodHyg.fhrsid = record.get("FHRSID");

			foodHyg.localAuthorityBusinessID = record.get("LocalAuthorityBusinessID");

			foodHyg.businessName = record.get("BusinessName");

			foodHyg.businessType = record.get("BusinessType");

			foodHyg.businessTypeID = record.get("BusinessTypeID");

			foodHyg.addressLine1 = record.get("AddressLine1");

			foodHyg.addressLine2 = record.get("AddressLine2");

			foodHyg.addressLine3 = record.get("AddressLine3");

			foodHyg.addressLine4 = record.get("AddressLine4");

			foodHyg.postcode = record.get("PostCode");

			foodHyg.ratingValue = record.get("RatingValue");

			foodHyg.ratingKey = record.get("RatingKey");

			foodHyg.ratingDates = Boolean.valueOf(record.get("RatingDate"));

			foodHyg.localAuthorityCode = record.get("LocalAuthorityCode");

			foodHyg.localAuthorityName = record.get("LocalAuthorityName");

			foodHyg.localAuthorityWebSite = record.get("LocalAuthorityWebSite");

			foodHyg.localAuthorityEmailAddress = record.get("LocalAuthorityEmailAddress");

			foodHyg.schemeType = record.get("SchemeType");

			foodHyg.newRatingPending = Boolean.valueOf(record.get("NewRatingPending"));

			if (record.get("Geocode/Longitude").length() > 0) {

				foodHyg.geocodeLongitude = Double.parseDouble(record.get("Geocode/Longitude"));

				foodHyg.geocodeLatitude = Double.parseDouble(record.get("Geocode/Latitude"));

			}

			foodHyg.ratingDate = record.get("RatingDate");

			foodHyg.scoreHygiene = record.get("Scores/Hygiene");

			foodHyg.scoresStructural = record.get("Scores/Structural");

			foodHyg.scoreConfidenceInManagement = record.get("Scores/ConfidenceInManagement");

// adds these variables into arrayList

			foodHygiene.add(foodHyg);

// TODO: handle exception

		}

		return foodHygiene;

	}

	/**
	 * Insert sorts the specified list on the order specified by the comparator
	 * 
	 */
	private static void insertSortData(ArrayList<foodHygieneData> foodHygiene, Comparator<foodHygieneData> sort) {
		for (int i = 1; i < foodHygiene.size(); i++) {
			for (int j = i; j > 0; j--) {
				foodHygieneData lower = foodHygiene.get(j - 1);
				foodHygieneData higher = foodHygiene.get(j);

				boolean swap = (sort.compare(higher, lower) < 0);

				if (swap) {
					foodHygiene.set(j, lower);
					foodHygiene.set(j - 1, higher);
				} else
					break;
			}
		}
	}

	public static void sortFoodHygieneData(List<foodHygieneData> foodHygiene) {
		Comparator<foodHygieneData> sort = null;
		HeapSort<foodHygieneData> heap = new HeapSort<>(true, sort);

		heap.breadthFirst();
		foodHygiene.clear();
		while (heap.isEmpty()) {

			foodHygiene.add(heap.remove());

		}
	}

	private static void outputLocalAuthority(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<String> LA = GetLocalAuthority(foodHygiene);

		System.out.println("List of Local Authorities: ");

		for (String la : LA) {

			System.out.println(la);

		}

	}

	private static void outputBusiness(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<String> BU = GetBusiness(foodHygiene);

		System.out.println("List of Businesses");

		for (String bu : BU) {

			System.out.println(bu);

		}

	}

	private static void outputRating(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		TreeSet<String> RA = GetRating(foodHygiene);

		System.out.println("List of Ratings in chronological order (Most recent first)");

		String search = console.nextLine();

		for (String ra : RA.descendingSet()) {

			if (ra.contains(search)) {

				System.out.println(ra);

			}

		}

	}

// Gets unique local authorities

	private static ArrayList<String> GetLocalAuthority(ArrayList<foodHygieneData> foodHygiene) {
		Map<foodHygieneData, String> FoodData = new TreeMap();
		ArrayList<String> localAuthority = new ArrayList<String>();

		for (foodHygieneData f : foodHygiene) {

			if (!localAuthority.contains(f.localAuthorityName)) {

				localAuthority.add(f.localAuthorityName);

			}

		}

		return localAuthority;

	}

// Gets distinct unique businesses

	private static ArrayList<String> GetBusiness(ArrayList<foodHygieneData> foodHygiene) {
		Map<foodHygieneData, String> FoodData = new TreeMap();
		ArrayList<String> Business = new ArrayList<String>();

		for (foodHygieneData b : foodHygiene) {

			if (!Business.contains(b.businessName)) {

				Business.add(b.businessName);

			}

		}

		return Business;

	}

// gets ratings and organises rating dates into chronological order (most recent
// first)

	private static TreeSet<String> GetRating(ArrayList<foodHygieneData> foodhygiene) {
		TreeSet<String> Rating = new TreeSet<String>();

		for (foodHygieneData r : foodhygiene) {

			if (!Rating.contains(r.ratingDate) && !Rating.contains(r.businessName) && (!Rating.contains(r.ratingValue))

					&& (!Rating.contains(r.scoreHygiene)) && (!Rating.contains(r.addressLine1))) {

				Rating.add(r.ratingDate + "   " + r.businessName + "   " + r.ratingValue + "  " + r.scoreHygiene + " "
						+ r.addressLine1);

			}

		}

		return Rating;

	}

// method to display a menu to select what type of rating

	private static void SelectRating(ArrayList<foodHygieneData> foodHygiene) {

		boolean quit = false;

		while (!quit) {

			System.out.println("-- Select Rating --");

			System.out.println("1. Above a specified value");

			System.out.println("2. Below a specified value");

			System.out.println("3. Within a specified range");

			System.out.println("4. A special value");

			System.out.println("5. Quit");

			Scanner console = new Scanner(System.in);

			String choice = console.next();

			ArrayList<foodHygieneData> filtered = null;

			switch (choice) {

			case "1":

				filtered = aboveSpecifiedValue(foodHygiene);

				break;

			case "2":

				filtered = sortBelowValueData(foodHygiene);

				break;

			case "3":

				filtered = sortWithinSpecifiedRangeData(foodHygiene);

				break;

			case "4":

				filtered = sortSpecialvalueData(foodHygiene);

				break;

			}

			System.out.println("(Feature E) Do you want to further filter this on LA/Business Type");

			System.out.println("1. LA");

			System.out.println("2. Business Type");

			System.out.println("3. None");

			String input = console.next();

			if (input.equals("1")) {

				filtered = LocalAuthority(filtered);

				sortAboveValueData(filtered);

			} else if (input.equals("2")) {

				filtered = BusinessType(filtered);

				sortAboveValueData(filtered);

			}

			else if (input.equals("3")) {

				sortAboveValueData(filtered);

			}

		}

	}

// Feature E

	private static void Filter(ArrayList<foodHygieneData> foodHygiene) {

		boolean quit = false;

		while (!quit) {

			System.out.println("Choose an option to be filtered for a specified: ");

			System.out.println("1. Local Authority");

			System.out.println("2. Type of Business");

			Scanner console = new Scanner(System.in);

			String choice = console.next();

			switch (choice) {

			case "1":

				LocalAuthority(foodHygiene);

				break;

			case "2":

				BusinessType(foodHygiene);

				break;

			}

		}

	}

// gets above specified rating based on user input

	private static ArrayList<foodHygieneData> aboveSpecifiedValue(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a rating ");

		int search = console.nextInt();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value >= search) {

					if (currentrate.ratingValue.startsWith("ratingValue"))
						ratingV++;
					else if (currentrate.ratingValue.startsWith("ratingValue"))
						ratingV++;
					else
						other++;

					System.out.println(currentrate.ratingDate + "  " + currentrate.ratingValue + "  "
							+ currentrate.businessName + "  score:" + currentrate.scoreHygiene + "  "
							+ currentrate.localAuthorityName + "  " + currentrate.addressLine1);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");
		return foodHygiene;
	}

	private static ArrayList<foodHygieneData> sortAboveValueData(ArrayList<foodHygieneData> foodHygiene) {

		int option = 3;
		if (option == 3) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			aboveSpecifiedValue(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

	public static void sortData(ArrayList<foodHygieneData> foodHygiene, foodHygieneSort type, SortAlgo method) {

// Sort on other criteria
// functionality using Comparator)
		Comparator<foodHygieneData> sort = null;

// if sort remains null, it will sort on its default order
		if (type == foodHygieneSort.LocalAuthorityName) {
			sort = new Comparator<foodHygieneData>() {
				public int compare(foodHygieneData o1, foodHygieneData o2) {
					return o1.localAuthorityName.compareTo(o2.localAuthorityName);
				}
			};
		} else if (type == foodHygieneSort.RATING_VALUE) {
			sort = new Comparator<foodHygieneData>() {

				public int compare(foodHygieneData o1, foodHygieneData o2) {
					return o1.ratingValue.compareTo(o2.ratingValue);
				}

			};
		} else if (type == foodHygieneSort.RATING_DATE) {
			sort = new Comparator<foodHygieneData>() {
				public int compare(foodHygieneData o1, foodHygieneData o2) {
					return o1.ratingDate.compareTo(o2.ratingDate);
				}
			};
		}

// "0. Collections.sort\n1. Insert Sort\n2. Heap Sort\n3.BST Sort");
		switch (method)

		{
		case COLLECTIONS:
// sorting for natural and overridden sort
			Collections.sort(foodHygiene, sort);
			break;
		case INSERT:
// an insert sort for comparison purposes
			insertSortData(foodHygiene, sort);
			break;
		case HEAP:
// heapsort
			HeapSort<foodHygieneData> heap = new HeapSort<>(true, sort);
			for (foodHygieneData c : foodHygiene) {

				heap.insert(c);
			}
			System.out.println("heap" + heap.size() + ", height: " + heap.getHeight());
			foodHygiene.clear();
			while (!heap.isEmpty()) {
				foodHygiene.add(heap.remove());
			}
			break;
		case BST:
// BST sort
			BSTDupNode<foodHygieneData> nodes = new BSTDupNode<foodHygieneData>(foodHygiene.get(0), sort);
			for (int i = 1; i < foodHygiene.size(); i++) {
				nodes.insertdups(foodHygiene.get(i));
			}
			foodHygiene.clear();
			foodHygiene.addAll(nodes.inOrderTraverse());
			System.out.println("BST " + nodes.size() + ", height: " + nodes.height());
			break;
		}

	}

	private static void beloweSpecifiedValue(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a rating ");

		int search = console.nextInt();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value <= search) {

					if (currentrate.ratingValue.startsWith("ratingValue"))
						ratingV++;
					else if (currentrate.ratingValue.startsWith("ratingValue"))
						ratingV++;
					else
						other++;

					System.out.println(
							currentrate.ratingDate + "  " + currentrate.ratingValue + "  " + currentrate.businessName
									+ "  score:" + currentrate.scoreHygiene + " " + currentrate.addressLine1);
				}

			} catch (Exception e) {

			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");
	}

	private static ArrayList<foodHygieneData> sortBelowValueData(ArrayList<foodHygieneData> foodHygiene) {

		int option = 3;
		if (option == 3) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			beloweSpecifiedValue(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

// outputs rating scores that user specified based on highest and lowest within
// range

	private static void WithinSpecifiedRange(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a max ");

		int search = console.nextInt();

		System.out.println("Please enter a min ");
		int min = console.nextInt();
		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (min >= 1 && search <= 5) {

					if (currentrate.ratingValue.startsWith("ratingValue"))
						ratingV++;
					else if (currentrate.ratingValue.startsWith("ratingValue"))
						ratingV++;
					else
						other++;

					System.out.println(
							currentrate.ratingDate + "  " + currentrate.ratingValue + "  " + currentrate.businessName
									+ "  score:" + currentrate.scoreHygiene + " " + currentrate.addressLine1);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");

	}

// gets ratings and scores within a specified range based on user input

	private static ArrayList<foodHygieneData> sortWithinSpecifiedRangeData(ArrayList<foodHygieneData> foodHygiene) {

		int option = 3;
		if (option == 3) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			WithinSpecifiedRange(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

	private static void specialvalue(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a AwaitingInspection or Exempt");

		String search = console.nextLine();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			try {

				if (currentrate.ratingValue.equalsIgnoreCase(search)) {

					System.out.println(currentrate.ratingDate + "  " + currentrate.ratingValue + "  "
							+ currentrate.businessName + "  score:   " + currentrate.scoreHygiene + "  "
							+ currentrate.localAuthorityName + " " + currentrate.addressLine1);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");
		System.out.println("There are " + foodHygiene.size() + " rates; ");

	}

	private static ArrayList<foodHygieneData> sortSpecialvalueData(ArrayList<foodHygieneData> foodHygiene) {

		int option = 3;
		if (option == 3) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			specialvalue(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

// Feature E method for filtering for a local authority

	private static ArrayList<foodHygieneData> LocalAuthority(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Local Authority");

		String search = console.nextLine();

//		System.out.println("Feature H) Would you like ratings to be sorted in chronological order? ");
//
//		String answer = console.nextLine();

		for (foodHygieneData data : foodHygiene) {

// call ratingValue

			if (data.localAuthorityName.equalsIgnoreCase(search))

				Data.add(data);

		}

		return Data;
	}

// Feature E method for filtering for a type of business

	private static ArrayList<foodHygieneData> BusinessType(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Business");

		String search = console.nextLine();

		for (foodHygieneData data : foodHygiene) {

			if (data.businessName.equalsIgnoreCase(search))

				Data.add(data);

		}
		return Data;

	}

	private static void SelectRatingScore(ArrayList<foodHygieneData> foodHygiene) {

		boolean quit = false;

		while (!quit) {

			System.out.println("Choose an option to be filtered for a specified rating score: ");

			System.out.println("1. Most Rating 5 scores");

			System.out.println("2. Most Rating 0 or 1 scores");

			System.out.println("3. none");

			Scanner input = new Scanner(System.in);

			String choice = input.next();

			switch (choice) {

			case "1":

				sortRatingScoresFive(foodHygiene);

				break;

			case "2":

				sortRatingScoresZeroOrOne(foodHygiene);

				break;

			}

		}

	}

// Feature G method for finding an organisation with most Rating 5 scores or
// most Rating 0 or 1 scores

	private static void outputlocalhigh(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Local Name");

		String search = console.nextLine();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value == 5) {

					if (currentrate.localAuthorityName.equalsIgnoreCase(search))

						System.out.println(currentrate.ratingDate + "  " + currentrate.ratingValue + "  "
								+ currentrate.businessName + "  score:   " + currentrate.scoreHygiene + "  "
								+ currentrate.localAuthorityName);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");
		System.out.println("There are " + foodHygiene.size() + " rates; ");

	}

	private static ArrayList<foodHygieneData> sortRatingScoresFive(ArrayList<foodHygieneData> foodHygiene) {

		int option = 1;
		if (option == 1) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			outputlocalhigh(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

	private static void outputlocallow(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Local Name");

		String search = console.nextLine();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value < 2) {

					if (currentrate.localAuthorityName.equalsIgnoreCase(search))

						System.out.println(currentrate.ratingDate + "  " + currentrate.ratingValue + "  "
								+ currentrate.businessName + "  score:   " + currentrate.scoreHygiene + "  "
								+ currentrate.localAuthorityName + " " + currentrate.addressLine1);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");
		System.out.println("There are " + foodHygiene.size() + " rates; ");

	}

	private static ArrayList<foodHygieneData> sortRatingScoresZeroOrOne(ArrayList<foodHygieneData> foodHygiene) {

		int option = 1;
		if (option == 1) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			outputlocallow(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

	private static void Organisation(ArrayList<foodHygieneData> foodHygiene) {

		boolean quit = false;
		while (!quit) {
			System.out.println("-- Select --");
			System.out.println("1. Max");
			System.out.println("2. Min");
			;

			ArrayList<foodHygieneData> orate = null;

			Scanner console = new Scanner(System.in);
			String choice = console.next();

			switch (choice) {
			case "1":
				orate = high(foodHygiene);

				break;

			case "2":
				orate = low(foodHygiene);

				break;

			}
		}
	}

	private static void outputhigh(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Business Name");

		String search = console.nextLine();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value >= 3) {

					if (currentrate.businessName.equalsIgnoreCase(search))

						System.out.println(currentrate.ratingDate + "  " + currentrate.ratingValue + "  "
								+ currentrate.businessName + "  score:   " + currentrate.scoreHygiene + " "
								+ currentrate.addressLine1);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");

		System.out.println("There are " + foodHygiene.size() + " rates; ");

	}

	private static ArrayList<foodHygieneData> high(ArrayList<foodHygieneData> foodHygiene) {

		int option = 3;
		if (option == 3) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			outputhigh(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

	private static ArrayList<foodHygieneData> low(ArrayList<foodHygieneData> foodHygiene) {

		int option = 3;
		if (option == 3) {

			int sort = 1;
			long time = System.currentTimeMillis();
			sortData(foodHygiene, foodHygieneSort.values()[option], SortAlgo.values()[sort]);
			long elapsedTime = System.currentTimeMillis() - time;

			outputlow(foodHygiene);
			System.out.println("Sorting took " + elapsedTime + "ms");

			System.out.println("------");
		} else {
			long time = System.currentTimeMillis();
			long elapsedTime = System.currentTimeMillis() - time;
			System.out.println("Counting took " + elapsedTime + "ms");
		}
		return foodHygiene;

	}

	private static void outputlow(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Business Name");

		String search = console.nextLine();

		int ratingV = 0, other = 0;
		for (int i = 0; i < foodHygiene.size(); i++) {
			foodHygieneData currentrate = foodHygiene.get(i);

			String str = currentrate.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value <= 2) {

					if (currentrate.businessName.equalsIgnoreCase(search))

						System.out.println(currentrate.ratingDate + "  " + currentrate.ratingValue + "  "
								+ currentrate.businessName + "  score:   " + currentrate.scoreHygiene + " "
								+ currentrate.addressLine1);
				}

			} catch (Exception e) {
// TODO: handle exception
			}
		}

		System.out.println("There are " + foodHygiene.size() + " rates; ");
		System.out.println(ratingV + " Type exempt rate - ");
		System.out.println(other + " other -");
		ArrayList<foodHygieneData> Data = new ArrayList();
		System.out.println("There are " + foodHygiene.size() + " rates; ");

	}

// Feature H
	private static void CreateRating(ArrayList<foodHygieneData> foodHygiene) throws IOException {
		StringBuilder sb = new StringBuilder();
		Scanner console = new Scanner(System.in);
		System.out.println("Please enter a Business Name");
		String name = console.nextLine();

		System.out.println("Your rating has been submitted.");

		for (foodHygieneData data : foodHygiene) {

			if (data.businessName.equalsIgnoreCase(name)) {

				recordedData.add(data.businessName + "  " + data.ratingDate + "   " + data.ratingValue + "  "
						+ data.localAuthorityName + " " + data.addressLine1);
			}
			File file = new File("RecordFHD.txt");
			FileWriter pw;
			try {

				pw = new FileWriter(file);
				for (String s : recordedData) {
					pw.write(s + "\n");
				}
				pw.write(" businessName  " + data.businessName + "  \n  ");
				pw.write("Date---" + data.ratingDate + "\n ");
				pw.write("ratingValue-----" + data.ratingValue + "\n ");
				pw.write("ratingValue-----" + data.localAuthorityName + "\n ");
				pw.write("Business address-----" + data.addressLine1 + "\n");

				pw.close();
			} catch (IOException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("\n" + "your programme has recorded successfully");

	}

	@Override
	public int compareTo(foodHygieneData arg0) {
		return this.fhrsid.compareTo(arg0.fhrsid);

	}
}