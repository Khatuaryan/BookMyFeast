package com.example.bookmyfeast;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurants.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESTAURANTS = "restaurants";
    public static final String COLUMN_RES_ID = "res_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_LOCALITY = "locality";
    public static final String COLUMN_LOCALITY_VERBOSE = "locality_verbose";
    public static final String COLUMN_ZIPCODE = "zipcode";
    public static final String COLUMN_COST_FOR_TWO = "cost_for_two";
    public static final String COLUMN_ESTABLISHMENT = "establishment";
    public static final String COLUMN_CUISINES = "cuisines";
    public static final String COLUMN_MEAL_OPTIONS = "meal_options";
    public static final String COLUMN_HIGHLIGHTS = "highlights";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_RATING_TEXT = "rating_text";
    public static final String COLUMN_PICTURES = "pictures";

    public RestDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY NOT NULL, " +
                        "%s TEXT NOT NULL, %s REAL NOT NULL, %s REAL NOT NULL, %s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, " +
                        "%s INTEGER NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL);",
                TABLE_RESTAURANTS, COLUMN_RES_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_ADDRESS, COLUMN_CITY, COLUMN_LOCALITY, COLUMN_LOCALITY_VERBOSE, COLUMN_ZIPCODE, COLUMN_COST_FOR_TWO, COLUMN_ESTABLISHMENT, COLUMN_CUISINES, COLUMN_MEAL_OPTIONS, COLUMN_HIGHLIGHTS, COLUMN_RATING, COLUMN_RATING_TEXT, COLUMN_PICTURES);
        db.execSQL(createTable);
        populateDatabase(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        insertRestaurant(db, 1, "Cin Cin", 19.073, 72.8639, "Ground Floor, Raheja Towers, Bandra Kurla Complex, Bandra East, Mumbai", "Bandra Kurla Complex", "Bandra East, Mumbai", "400051", 3500, "Fine Dining", "Italian, European", "Dinner, Breakfast, Lunch, Extensive wine and cocktail menu", "Upscale Italian dining, Extensive wine selection (38 wines available), Seasonal, ingredient-driven menu, Cicchettis (Italian small plates)", "4.4", "Good", "r1");
        insertRestaurant(db, 2, "Demy Cafe & Bar", 19.0011, 72.8346, "Ground Floor, Trade Centre, Kamala Mills Compound, A Wing, Lower Parel, Mumbai", "Lower Parel", "Lower Parel, South Mumbai", "400013", 2000, "Fine Dining", "Italian, Continental, North Indian, Asian", "Lunch Dinner", "Air Conditioned, DJ, Wifi, Valet Available, Home Delivery, Parking, Cards Accepted, Full Bar Available, Private Dining Available", "4.4", "Very Good", "r2");
        insertRestaurant(db, 3, "The Nines", 19.1066, 72.8261, "Devle Road, near PVR, JVPD Scheme, Juhu", "Juhu", "JVPD Scheme, Juhu", "400049", 3000, "Fine Dining", "Italian, Mediterranean, Chinese, North Indian, Pan-Asian", "Dinner, Snacks", "Outdoor seating, live music, special events", "4.0", "Good", "r3");
        insertRestaurant(db, 4, "All Saints", 19.0527, 2.8407, "15 S V Road, Near Khar Gymkhana, Khar West, Mumbai", "Khar West",	"Khar West, Mumbai", "400052",	2000, "Fine Dining", "Italian, Continental", "Lunch, Dinner", "Outdoor seating, vibrant atmosphere", "4.0", "Good",	"r4");
        insertRestaurant(db, 5, "Masala Library", 19.066, 72.8356, "Ground Floor, First International Financial Centre, Citi Bank Building, G Block BKC, Bandra Kurla Complex, Mumbai", "Bandra Kurla Complex", "Bandra East, Mumbai", "400051", 4000, "Fine Dining", "North Indian", "Lunch, Dinner", "Upscale Indian dining, Molecular gastronomy, Seasonal tasting menu, Extensive wine selection", "4.5", "Excellent", "r5");
        insertRestaurant(db, 6, "SpiceKlub", 19.079, 72.8298, "Opposite Phoenix Mills 8A, Janta Industrial Estate, Mumbai", "Lower Parel", "Lower Parel, South Mumbai", "400013", 1800, "Fine Dining", "North Indian", "Lunch, Dinner, Snacks", "Innovative Indian flavors, Vegetarian-only, Molecular techniques, Vibrant décor", "4.2", "Very Good", "r6");
        insertRestaurant(db, 7, "The Bombay Canteen", 19.0169, 72.8306, "Unit-1, Process House, S.B. Road, Kamala Mills, Near Radio Mirchi Office Lower, Parel, Mumbai", "Lower Parel", "Lower Parel, South Mumbai", "400013", 1700, "Fine Dining", "North Indian, Mediterranean", "Lunch, Dinner", "Eclectic ambiance, Cocktails inspired by Indian flavors, Seasonal ingredients, Air-conditioned", "4.4", "Very Good", "r7");
        insertRestaurant(db, 8, "Masque", 19.0073, 72.8291, "Masque, Unit G3, Shree Laxmi Woollen Mills, Shakti Mills Lane, Off, Dr Elijah Moses Rd, Mahalakshmi, Mumbai", "Mahalakshmi", "Mahalakshmi, Mumbai", "400011", 6000, "Fine Dining", "Pan-Asian, Mediterranean", "Dinner", "Farm-to-table concept, Seasonal tasting menu, Private dining, Contemporary décor", "4.6", "Exceptional", "r8");
        insertRestaurant(db, 9, "Ekaa", 19.0161, 72.8284, "1st Floor, Kitab Mahal, D Sukhadwala Rd, Azad Maidan, Fort, Mumbai", "Fort", "Fort, Mumbai", "400001", 4000, "Fine Dining", "Mediterranean, Pan-Asian", "Lunch, Dinner", "Modern, locally-sourced ingredients, Experimental dishes, Chef's tasting menu", "4.3", "Very Good", "r9");
        insertRestaurant(db, 10, "Aaswad", 19.0471, 72.8372, "Sanskruti Building Gadkari Chauk, 4.0 Lady Jamshedji Rd, opp. Shiv Sena Bhavan, Dadar West, Mumbai", "Dadar West", "Dadar West, Mumbai", "400028", 300, "Fine Dining", "North Indian, South Indian", "Breakfast, Lunch, Dinner", "Traditional Indian snacks, Family-friendly, Quick service, Known for Vada Pav", "4.1", "Good", "r10");
        insertRestaurant(db, 11, "Peshawri", 19.1125, 72.8372, "Chhatrapati Shivaji Maharaj Int'l Airport Rd, near International Airport, Ashok Nagar, Andheri East, Mumbai", "Andheri East", "Andheri East, Mumbai", "400099", 8500, "Fine Dining", "North Indian", "Lunch, Dinner", "Authentic Mughlai cuisine, Open kitchen, Signature tandoor dishes, Air-conditioned", "4.5", "Excellent", "r11");
        insertRestaurant(db, 12, "Epitome", 19.076, 72.8369, "Mathuradas Mill Compound, Plot 242, NM Joshi Marg, Lower Parel West, Lower Parel, Mumbai", "Lower Parel", "Lower Parel, South Mumbai", "400013", 2500, "Fine Dining", "North Indian, Pan-Asian", "Dinner, Snacks", "Rooftop seating, Vegetarian menu, DJ nights, Special cocktails", "4.3", "Very Good", "r12");
        insertRestaurant(db, 13, "The Bombay Cartel", 19.077, 72.829, "Kamala Mills Compound, Shop 4-5, 1st Floor Bombay Hub, Z - Wing, Senapati Bapat Marg, Lower Parel, Mumbai", "Lower Parel", "Lower Parel, South Mumbai", "400013", 1800, "Fine Dining", "Mediterranean, North Indian", "Lunch, Dinner", "Cocktail bar, Lively ambiance, Vibrant décor, Air-conditioned", "4.1", "Good", "r13");
        insertRestaurant(db, 14, "Global Fusion", 19.1212, 72.8393, "Linking Rd, opp. KFC, Bandra West, Mumbai", "Bandra West", "Bandra West, Mumbai", "400050", 3000, "Buffet", "Pan-Asian, Chinese", "Lunch, Dinner", "Buffet-style dining, Sushi bar, Open seating, Private dining rooms", "4.4", "Very Good", "r14");
        insertRestaurant(db, 15, "Barbeque Nation", 19.0768, 72.8274, "2nd Floor, Parle Square Mall, Shyam Kamal Rd, Agarwal Market, Vile Parle East, Mumbai", "Vile Parle East", "Vile Parle East, Mumbai", "400057", 2000, "Buffet", "North Indian, Chinese", "Lunch, Dinner", "Live grill on the table, Unlimited servings, Family-friendly, Birthday celebrations", "4.3", "Very Good", "r15");
        insertRestaurant(db, 16, "O22", 19.1113, 72.8362, "1st floor, Trident, C-56, G Block, Bandra Kurla Complex, Bandra East, Mumbai", "Bandra East", "Bandra East, Mumbai", "400098", 6000, "Buffet", "North Indian, Mediterranean", "Breakfast, Lunch, Dinner", "Luxury setting, Private dining, All-day dining, Extensive wine selection", "4.5", "Excellent", "r16");
        insertRestaurant(db, 17, "Fiona", 19.0898, 72.8422, "Radisson Blu Mumbai International Airport, Marol Maroshi Rd, near Marol, Andheri East, Mumbai", "Andheri East", "Andheri East, Mumbai", "400059", 3500, "Buffet", "Italian, Mediterranean", "Dinner", "Outdoor seating, Extensive wine list, Romantic ambiance, Small plates (Cicchetti)", "4.2", "Very Good", "r17");
        insertRestaurant(db, 18, "The Brasserie", 19.1145, 72.8309, "Hilton Mumbai International Airport, Chhatrapati Shivaji Maharaj Int'l Airport Rd, Ashok Nagar, Andheri East, Mumbai", "Andheri East", "Andheri East, Mumbai", "400059", 3500, "Buffet", "Italian, Pan-Asian", "Lunch, Dinner", "Chic and modern interior, Fine wines, Open kitchen, Group dining options", "4.3", "Very Good", "r18");
        insertRestaurant(db, 19, "Flamboyante", 19.0156, 72.8282, "Shop No:7, Ground Floor, The Arcade Building, Gate 2 World Trade Centre Colaba, Cuffe Parade, Mumbai", "Cuffe Parade", "Cuffe Parade, Mumbai", "400005", 2000, "Buffet", "Pan-Asian, North Indian", "Lunch, Dinner", "Outdoor seating, Family-friendly, Large portions, Kid’s menu available", "4.2", "Very Good", "r19");
        insertRestaurant(db, 20, "Peshwa Pavilion", 19.1041, 72.8696, "Itc Maratha, Chhatrapati Shivaji Maharaj Int'l Airport Rd, Ashok Nagar, Andheri East, Mumbai", "Andheri East", "Andheri East, Mumbai", "400059", 5000, "Buffet", "North Indian, South Indian", "Breakfast, Lunch, Dinner", "Luxury ambiance, Signature thalis, Private dining, Valet parking available", "4.5", "Excellent", "r20");
    }

    private void insertRestaurant(SQLiteDatabase db, int resId, String name, double latitude,
                                  double longitude, String address, String locality,
                                  String localityVerbose, String zipcode, int costForTwo,
                                  String establishment, String cuisines, String mealOptions,
                                  String highlights, String rating, String ratingText, String pictures) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RES_ID, resId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_CITY, "Mumbai");
        values.put(COLUMN_LOCALITY, locality);
        values.put(COLUMN_LOCALITY_VERBOSE, localityVerbose);
        values.put(COLUMN_ZIPCODE, zipcode);
        values.put(COLUMN_COST_FOR_TWO, costForTwo);
        values.put(COLUMN_ESTABLISHMENT, establishment);
        values.put(COLUMN_CUISINES, cuisines);
        values.put(COLUMN_MEAL_OPTIONS, mealOptions);
        values.put(COLUMN_HIGHLIGHTS, highlights);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_RATING_TEXT, ratingText);
        values.put(COLUMN_PICTURES, pictures);

        db.insert(TABLE_RESTAURANTS, null, values);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        onCreate(db);
    }
}
