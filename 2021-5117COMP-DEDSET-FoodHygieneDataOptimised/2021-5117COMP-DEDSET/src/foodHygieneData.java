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

import java.util.Scanner;

import java.util.Set;

import java.util.StringTokenizer;

import java.util.Date;

import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVRecord;

public class foodHygieneData {

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

	public foodHygieneData() {

	}

	public static ArrayList<String> recodrdedData = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

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
				Organisation(foodHygiene);

			case "G":

				SelectRatingScore(foodHygiene);

			case "H":

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

			try {
				File files = new File("RecordFHD.txt");
				scan = new Scanner(files);

				while (scan.hasNextLine()) {
					recodrdedData.add(scan.nextLine());

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return foodHygiene;

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

		ArrayList<String> RA = GetRating(foodHygiene);

		System.out.println("List of Ratings in chronological order (Most recent first)");

		String search = console.nextLine();

		for (String ra : RA) {

			if (ra.contains(search)) {

				System.out.println(ra);

			}

		}

	}

	// Gets unique local authorities

	private static ArrayList<String> GetLocalAuthority(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<String> localAuthority = new ArrayList<String>();

		// traverses through first arraylist

		for (foodHygieneData f : foodHygiene) {

			if (!localAuthority.contains(f.localAuthorityName)) {

				localAuthority.add(f.localAuthorityName);

			}

		}

		return localAuthority;

	}

	// Gets distinct unique businesses

	private static ArrayList<String> GetBusiness(ArrayList<foodHygieneData> foodHygiene) {

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

	private static ArrayList<String> GetRating(ArrayList<foodHygieneData> foodhygiene) {

		ArrayList<String> Rating = new ArrayList<String>();

		for (foodHygieneData r : foodhygiene) {

			Collections.sort(Rating);

			Collections.reverse(Rating);

			if (!Rating.contains(r.ratingDate) && !Rating.contains(r.businessName) && (!Rating.contains(r.ratingValue))

					&& (!Rating.contains(r.scoreHygiene))) {

				Rating.add(r.ratingDate + "   " + r.businessName + "   " + r.ratingValue + "  " + r.scoreHygiene);

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

				filtered = belowSpecifiedValue(foodHygiene);

				break;

			case "3":

				filtered = withinSpecifiedRange(foodHygiene);

				break;

			case "4":

				filtered = specialvalue(foodHygiene);

				break;

			}

			System.out.println("(Feature E) Do you want to further filter this on LA/Business Type");

			System.out.println("1. LA");

			System.out.println("2. Business Type");

			System.out.println("3. None");

			String input = console.next();

			if (input.equals("1")) {

				filtered = LocalAuthority(filtered);

				outputList(filtered);

			} else if (input.equals("2")) {

				filtered = BusinessType(filtered);

				outputList(filtered);

			}

			else if (input.equals("3")) {

				outputList(filtered);

			}

		}

	}

	private static void outputList(ArrayList<foodHygieneData> list) {

		for (foodHygieneData data : list)

			System.out.println(data.ratingDate + "      " + data.ratingValue + "    " + data.businessName + " "

					+ data.localAuthorityName);

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

		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a rating");

		int search = console.nextInt();

		for (foodHygieneData data : foodHygiene) {

			// call ratingValue

			String str = data.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value >= search) {

					Data.add(data);

				}

			}

			catch (Exception e) {

			}

		}

		return Data;

	}

	private static ArrayList<foodHygieneData> belowSpecifiedValue(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList<foodHygieneData>();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a rating");

		int search = console.nextInt();

		for (foodHygieneData data : foodHygiene) {

			// call ratingValue

			String str = data.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value <= search) {

					Data.add(data);

				}

			}

			catch (Exception e) {

			}

		}

		return Data;

	}

	// outputs rating scores that user specified based on highest and lowest within
	// range

	private static ArrayList<String> outputWithinSpecifiedRange(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<String> withinSpecified = new ArrayList<String>();

		for (foodHygieneData ws : foodHygiene) {

			Collections.sort(withinSpecified);

			Collections.reverse(withinSpecified);

			withinSpecified.add(ws.ratingDate + "  / " + ws.businessName + " /  " + ws.businessType + " /  "

					+ ws.addressLine1 + "  / " + ws.ratingValue + " / " + ws.scoreHygiene);

		}

		return withinSpecified;

	}

	// gets ratings and scores within a specified range based on user input

	private static ArrayList<foodHygieneData> withinSpecifiedRange(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> data = new ArrayList<foodHygieneData>();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter the minimum rating");

		int min = console.nextInt();

		System.out.println("Please enter the maximum rating");

		int max = console.nextInt();

		ArrayList<String> WS = outputWithinSpecifiedRange(foodHygiene);

		ArrayList<Integer> w = new ArrayList<Integer>();

		String search = console.nextLine();

		for (String ws : WS) {

			for (int i = min; i < max; i++) {

				System.out.print(i);

				if (min >= 1 && max <= 5) {

				}

				if (ws.contains(search)) {

					if (ws.equals(search)) {

					}

					System.out.println(ws);

				}

			}

		}

		return data;

	}

	private static ArrayList<foodHygieneData> specialvalue(ArrayList<foodHygieneData> foodHygiene) {

		Scanner console = new Scanner(System.in);

		System.out.println("What type of value " + "\n Choose (AwaitingInspection or Exempt)");

		ArrayList<String> SR = outputspecialvalue(foodHygiene);

		String search = console.nextLine();

		ArrayList<foodHygieneData> dataList = new ArrayList<foodHygieneData>();

		for (foodHygieneData data : foodHygiene) {

			if (data.ratingValue.contains(search)) {

				dataList.add(data);

				System.out.println(data.ratingDate + " / " + data.businessName + "/" + data.ratingValue);

			}

		}

		return dataList;

	}

	private static ArrayList<String> outputspecialvalue(ArrayList<foodHygieneData> foodhygiene) {

		ArrayList<String> special = new ArrayList<String>();

		try {

			for (foodHygieneData s : foodhygiene) {

				Collections.sort(special);

				Collections.reverse(special);

				String sv = s.ratingValue;

				if (!special.contains(s.ratingDate) && !special.contains(s.businessName)

						&& (!special.contains(s.ratingValue)) && (!special.contains(s.businessType))

						&& (!special.contains(s.addressLine1)) && (!special.contains(s.scoreHygiene))) {

					special.add(s.ratingDate + "  / " + s.businessName + " /  " + s.businessType + " /  "

							+ s.addressLine1 + "  / " + s.ratingValue + " / " + s.scoreHygiene);

				}

			}

		} catch (Exception e) {

		}

		return special;

	}

	// Feature E method for filtering for a local authority

	private static ArrayList<foodHygieneData> LocalAuthority(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a Local Authority");

		String search = console.nextLine();

		System.out.println("Feature H) Would you like ratings to be sorted in chronological order? ");

		String answer = console.nextLine();

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

		System.out.println("Feature H) Would you like ratings to be sorted in chronological order? ");

		String answer = console.nextLine();

		for (foodHygieneData data : foodHygiene) {

			if (data.businessName.equalsIgnoreCase(search))

				Data.add(data);

			if (answer.equalsIgnoreCase("Yes")) {

			} else {

			}

		}
		return Data;
	}

	private static void SelectRatingScore(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> filtered = new ArrayList();

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

				filtered = RatingScoresFive(foodHygiene);

				outputLi(filtered);

				break;

			case "2":

				filtered = RatingScoresZeroOrOne(foodHygiene);

				outputLi(filtered);
				break;

			}

		}

	}

	private static void outputLi(ArrayList<foodHygieneData> list) {

		for (foodHygieneData data : list)

			System.out.println(data.ratingDate + "   " + data.ratingValue + "  " + data.localAuthorityName + "   "
					+ data.businessName);

	}

	// Feature G method for finding an organisation with most Rating 5 scores or

	// most Rating 0 or 1 scores

	private static ArrayList<foodHygieneData> RatingScoresFive(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a local authority");

		String search = console.nextLine();

		for (foodHygieneData data : foodHygiene) {

			String str = data.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value == 5) {

					if (data.localAuthorityName.equalsIgnoreCase(search))

						Data.add(data);

				}
			}

			catch (Exception e) {

			}

		}

		return Data;

	}

	private static ArrayList<foodHygieneData> RatingScoresZeroOrOne(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList<foodHygieneData>();

		Scanner console = new Scanner(System.in);

		System.out.println("Please enter a local authority");

		String search = console.nextLine();

		for (foodHygieneData data : foodHygiene) {

			String str = data.ratingValue;

			try {

				int value = Integer.parseInt(str);

				if (value < 2) {

					if (data.localAuthorityName.equalsIgnoreCase(search)) {

						Data.add(data);

					}

				}

			}

			catch (Exception e) {

			}

		}

		return Data;

	}

	private static void Organisation(ArrayList<foodHygieneData> foodHygiene) {

		boolean quit = false;
		while (!quit) {
			System.out.println("-- Select --");
			System.out.println("1. Max");
			System.out.println("2. Min");
			;

			Scanner console = new Scanner(System.in);
			String choice = console.next();

			ArrayList<foodHygieneData> orate = null;

			switch (choice) {
			case "1":
				orate = high(foodHygiene);
				outputorg(orate);
				break;

			case "2":
				orate = low(foodHygiene);
				outputorg(orate);
				break;

			}
		}
	}

	private static void outputorg(ArrayList<foodHygieneData> org) {
		for (foodHygieneData data : org)
			System.out.println(data.ratingDate + "      " + data.ratingValue + "    " + data.businessName);
	}

	private static ArrayList<foodHygieneData> high(ArrayList<foodHygieneData> foodHygiene) {

		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);
		System.out.println("Please enter a Business Name");
		String name = console.nextLine();

		for (foodHygieneData data : foodHygiene) {
			String str = data.ratingValue;

			try {
				int value = Integer.parseInt(str);

				if (value >= 3) {
					if (data.businessName.equalsIgnoreCase(name))
						Data.add(data);

				}
			}

			catch (Exception e) {

			}

		}

		return Data;

	}

	private static ArrayList<foodHygieneData> low(ArrayList<foodHygieneData> foodHygiene) {
		ArrayList<foodHygieneData> Data = new ArrayList();

		Scanner console = new Scanner(System.in);
		System.out.println("Please enter a Business Name");
		String name = console.nextLine();

		for (foodHygieneData data : foodHygiene) {
			String str = data.ratingValue;

			try {
				int value = Integer.parseInt(str);

				if (value <= 2) {
					if (data.businessName.equalsIgnoreCase(name))
						Data.add(data);

				}
			}

			catch (Exception e) {

			}

		}

		return Data;
	}

	// Feature H
	private static void CreateRating(ArrayList<foodHygieneData> foodHygiene) throws IOException {

		StringBuilder sb = new StringBuilder();
		Scanner console = new Scanner(System.in);
		System.out.println("Please enter a Business Name");
		String name = console.nextLine();
		System.out.println("Please enter a rating value (0-5)");
		String ratingValue = console.nextLine();
		System.out.println("Your rating has been submitted.");

		for (foodHygieneData data : foodHygiene) {

			if (data.businessName.equalsIgnoreCase(name)) {

				recodrdedData.add(data.businessName + "  " + data.ratingDate + "   " + data.ratingValue + "  "
						+ data.localAuthorityName);
			}
			File file = new File("RecordFHD.txt");
			FileWriter pw;
			try {

				pw = new FileWriter(file);
				for (String s : recodrdedData) {
					pw.write(s + "\n");
				}
				pw.write(" businessName  " + data.businessName + "  \n  ");
				pw.write("Date---" + data.ratingDate + "\n ");
				pw.write("ratingValue-----" + data.ratingValue + "\n ");
				pw.write("ratingValue-----" + data.localAuthorityName + "\n ");

				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("\n" + "your programme has recorded succefully");

	}
}
