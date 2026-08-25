package com.example.commande_pc.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.commande_pc.Utils;
import com.example.commande_pc.entity.Administrator;
import com.example.commande_pc.entity.Assembler;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.MaterielItem;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.OrderItem;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.Role;
import com.example.commande_pc.entity.SofwareItem;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.entity.User;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;


public class SqliteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "commandedepcfinal.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_USERS_TABLE = "users";
    private static final String DATABASE_ROLES_TABLE = "roles";
    private static final String DATABASE_CREATED_AT_COLUMN = "created_at";
    private static final String DATABASE_UPDATED_AT_COLUMN = "updated_at";
    private static Context context;
    private static final String DATABASE_STOCK_TABLE = "components";
    private static final String DATABASE_ORDERS_TABLE= "orders";
    private static final String DATABASE_MY_ORDERS_ITEMS_TABLE= "my_orders_items";


    public SqliteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SqliteDatabaseHelper.context = context;
    }

    public SqliteDatabaseHelper() {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRolesTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_ROLES_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name VARCHAR(30) NOT NULL UNIQUE"
                + ")";
        db.execSQL(createRolesTable);
        String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS " + DATABASE_USERS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "last_name VARCHAR(30) NOT NULL," +
                "first_name VARCHAR(30) NOT NULL," +
                "email VARCHAR(30) UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role_id INT NOT NULL REFERENCES " + DATABASE_ROLES_TABLE + "(id) ON DELETE CASCADE ON UPDATE CASCADE ," +
                DATABASE_CREATED_AT_COLUMN + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                DATABASE_UPDATED_AT_COLUMN + " DATETIME NOT NULL" +
                ") ";
        db.execSQL(createUsersTableQuery);
        String createComponentsTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_STOCK_TABLE
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type VARCHAR(50) CHECK( type IN ('materiel','logiciel') ) NOT NULL," +
                "subtype VARCHAR(50) NOT NULL," +
                "description VARCHAR(100) NOT NULL UNIQUE," +
                "quantity INTEGER NOT NULL," +
                "comment VARCHAR(200) DEFAULT NULL," +
                "image TEXT DEFAULT NULL," +
                DATABASE_CREATED_AT_COLUMN + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                DATABASE_UPDATED_AT_COLUMN + " DATETIME NOT NULL" +
                ")";
        db.execSQL(createComponentsTable);
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_ORDERS_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "requester_id INT NOT NULL REFERENCES " + DATABASE_ROLES_TABLE + "(id) ON DELETE CASCADE ON UPDATE CASCADE ,"+
                DATABASE_CREATED_AT_COLUMN + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "state TEXT NOT NULL DEFAULT 'En attente acceptation' "+
                ")";
        db.execSQL(createOrdersTable);
        String createMyOrderItemsTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_MY_ORDERS_ITEMS_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INT NOT NULL REFERENCES " + DATABASE_ORDERS_TABLE + "(id) ON DELETE CASCADE ON UPDATE CASCADE ,"+
                "item_id INT NOT NULL REFERENCES " + DATABASE_STOCK_TABLE + "(id) ON DELETE CASCADE ON UPDATE CASCADE ,"+
                "quantity INT NOT NULL REFERENCES " + DATABASE_STOCK_TABLE +
                ")";
        db.execSQL(createMyOrderItemsTable);

        this.insertRoleToDatabase(db, "administrator");
        this.insertRoleToDatabase(db, "requester");
        this.insertRoleToDatabase(db, "storekeeper");
        this.insertRoleToDatabase(db, "assembler");
        // Default demo credentials — change before any real use
        this.insertUserToDatabase(db, "StoreKeeper", "John", "storekeeper@gmail.com", "changeme123", 3);
        this.insertUserToDatabase(db, "Assembleur", "Doe", "assembleur@gmail.com", "changeme123", 4);
        this.insertUserToDatabase(db, "Admin", "Commande", "admin@gmail.com", "changeme123", 1);







    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROLES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_USERS_TABLE);
        onCreate(db);
    }

    private void insertRoleToDatabase(SQLiteDatabase db, String roleName) {
        ContentValues values = new ContentValues();
        values.put("name", roleName);
        db.insert(DATABASE_ROLES_TABLE, null, values);
    }

    private long insertUserToDatabase(SQLiteDatabase db, String lastName, String firstName, String email, String password, long role_id) {
        ContentValues values = new ContentValues();
        values.put("last_name", lastName);
        values.put("first_name", firstName);
        values.put("email", email);
        values.put("password", Utils.hashPassword(password));
        values.put("role_id", role_id);
        values.put("updated_at", Utils.newDateToSQLString());
        return db.insert(DATABASE_USERS_TABLE, null, values);
    }

    public long insertRequester(Requester requester) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return insertUserToDatabase(db, requester.getLastName(), requester.getFirstName(), requester.getEmail(), requester.getPassword(), requester.getRoleId());
        } catch (Exception exception) {
            return -1;
        }
    }

    public void  insertComponentToDatabase(String type, String subtype, String description, int quantity, String comment, String image) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("type", type);
            values.put("subtype", subtype);
            values.put("description", description);
            values.put("quantity", quantity);
            values.put("comment", comment);
            values.put("image", image);
            values.put("updated_at", Utils.newDateToSQLString());
            db.insert(DATABASE_STOCK_TABLE, null, values);
        } catch (Exception exception) {
        }
    }

    public long insertComponentToDatabase(Item item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("type", item.getType());
            values.put("subtype", item.getSubtype());
            values.put("description", item.getDescription());
            values.put("quantity", item.getQuantity());
            values.put("comment", item.getComment());
            values.put("image", item.getImage());
            values.put("updated_at", Utils.newDateToSQLString());
            return db.insert(DATABASE_STOCK_TABLE, null, values);
        } catch (Exception exception) {
            return -1;
        }
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_STOCK_TABLE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            items.add(new Item(
                            cursor.getLong(Math.max(0, cursor.getColumnIndex("id"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("type"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("subtype"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("description"))),
                            cursor.getInt(Math.max(0, cursor.getColumnIndex("quantity"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("comment"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("image"))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("created_at")))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("updated_at"))))
                    )
            );
            cursor.moveToNext();
        }
        db.close();
        return items;
    }
    
    public ArrayList<SofwareItem> getSoftwareItems(String type){
        ArrayList<SofwareItem> softItems= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_STOCK_TABLE + " where type = ?", new String[]{type});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            softItems.add(new SofwareItem(
                            cursor.getLong(Math.max(0, cursor.getColumnIndex("id"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("type"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("subtype"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("description"))),
                            cursor.getInt(Math.max(0, cursor.getColumnIndex("quantity"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("comment"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("image"))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("created_at")))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("updated_at"))))
                    )
            );
            cursor.moveToNext();

        }
        db.close();
        return softItems;

    }
    public ArrayList<MaterielItem> getMaterielItems(String type){
        ArrayList<MaterielItem> matItems= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_STOCK_TABLE + " where type = ?", new String[]{type});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            matItems.add(new MaterielItem(
                            cursor.getLong(Math.max(0, cursor.getColumnIndex("id"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("type"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("subtype"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("description"))),
                            cursor.getInt(Math.max(0, cursor.getColumnIndex("quantity"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("comment"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("image"))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("created_at")))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("updated_at"))))
                    )
            );
            cursor.moveToNext();

        }
        db.close();
        return matItems;

    }

    public ArrayList<Requester> getRequesters() {
        ArrayList<Requester> requesters = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_USERS_TABLE + " where role_id = 2", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            requesters.add(new Requester(
                            cursor.getString(Math.max(0, cursor.getColumnIndex("last_name"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("first_name"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("email"))),
                            cursor.getLong(Math.max(0, cursor.getColumnIndex("id"))),
                            cursor.getString(Math.max(0, cursor.getColumnIndex("password"))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("created_at")))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("updated_at"))))
                    )
            );
            cursor.moveToNext();
        }
        db.close();
        return requesters;
    }
    public ArrayList<Order> getAllOrder(){
        ArrayList<Order> orders_save = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=null;
        try{
            cursor = db.rawQuery("SELECT * FROM " + DATABASE_ORDERS_TABLE, null);
            if(cursor !=null && cursor.moveToFirst()){
                do{
                    long id = cursor.getLong(Math.max(0, cursor.getColumnIndex("id")));
                    long requester_id = cursor.getLong(Math.max(0, cursor.getColumnIndex("requester_id")));
                    Date date = Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("created_at"))));
                    orders_save.add(new Order(
                            id,
                            requester_id,
                            date
                    ));


                }while(cursor.moveToNext());
            }
        } finally {
            if(cursor !=null) cursor.close();
            db.close();;
        }
        return orders_save;
    }

    public int deleteItem(long item_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(DATABASE_STOCK_TABLE, "id = ?", new String[]{String.valueOf(item_id)});
        } catch (Exception e) {
            return 0;
        }
    }

    public int deleteRequester(long requester_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(DATABASE_USERS_TABLE, "id = ?", new String[]{String.valueOf(requester_id)});
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateItem(Item item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("quantity", item.getQuantity());
            values.put("comment", item.getComment());
            values.put("updated_at", Utils.newDateToSQLString());
            return db.update(DATABASE_STOCK_TABLE, values, "id = ?", new String[]{String.valueOf(item.getId())});
        } catch (Exception e) {
            return 0;
        }
    }

    @SuppressLint("Range")
    public User findUserByCredentials(String email, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_USERS_TABLE + " where email = ?", new String[]{email});
            boolean bool = cursor.getCount() == 0;
            if (bool || !cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            String hashPassword = cursor.getString(cursor.getColumnIndex("password"));
            if (!Utils.checkPassword(hashPassword, password)) {
                cursor.close();
                return null;
            }
            User user = getUser(cursor);
            cursor.close();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("Range")
    public Role findRoleById(long role_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_ROLES_TABLE + " where id = ?", new String[]{String.valueOf(role_id)});
            boolean bool = cursor.getCount() == 0;
            if (bool || !cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return new Role(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name"))
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    @SuppressLint("Range")
    public Role findRoleByRoleName(String roleName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_ROLES_TABLE + " where name = ?", new String[]{roleName});
            boolean bool = cursor.getCount() == 0;
            if (bool || !cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return new Role(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name"))
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    public User findUserById(long id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_USERS_TABLE + " where id = ?", new String[]{String.valueOf(id)});
            boolean bool = cursor.getCount() == 0;
            if (bool || !cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            System.out.println("Cursor position");
            User user = getUser(cursor);
            cursor.close();
            return user;
        } catch (Exception ignored) {
            return null;
        }
    }

    public Item findItemById(long id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_STOCK_TABLE + " where id = ?", new String[]{String.valueOf(id)});
            boolean bool = cursor.getCount() == 0;
            if (bool || !cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            Item item = getItem(cursor);
            cursor.close();
            return item;
        } catch (Exception ignored) {
            return null;
        }
    }


    @SuppressLint("Range")
    private User getUser(Cursor cursor) {
        switch (cursor.getInt(cursor.getColumnIndex("role_id"))) {
            case 1:
                return new Administrator(
                        cursor.getString(cursor.getColumnIndex("last_name")), cursor.getString(cursor.getColumnIndex("first_name")), cursor.getString(cursor.getColumnIndex("email")), cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("password")), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("created_at"))), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("updated_at")))
                );
            case 2:
                return new Requester(
                        cursor.getString(cursor.getColumnIndex("last_name")), cursor.getString(cursor.getColumnIndex("first_name")), cursor.getString(cursor.getColumnIndex("email")), cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("password")), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("created_at"))), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("updated_at")))
                );
            case 3:
                return new StoreKeeper(
                        cursor.getString(cursor.getColumnIndex("last_name")), cursor.getString(cursor.getColumnIndex("first_name")), cursor.getString(cursor.getColumnIndex("email")), cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("password")), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("created_at"))), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("updated_at")))
                );
            case 4:
                return new Assembler(
                        cursor.getString(cursor.getColumnIndex("last_name")), cursor.getString(cursor.getColumnIndex("first_name")), cursor.getString(cursor.getColumnIndex("email")), cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("password")), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("created_at"))), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("updated_at")))
                );
        }
        return null;
    }

    @SuppressLint("Range")
    private Item getItem(Cursor cursor) {
        switch (cursor.getString(cursor.getColumnIndex("type"))) {
            case "materiel":
                return new MaterielItem(
                        cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("subtype")), cursor.getString(cursor.getColumnIndex("description")), cursor.getString(Math.max(0, cursor.getColumnIndex("description"))), cursor.getInt(cursor.getColumnIndex("quantity")), cursor.getString(cursor.getColumnIndex("comment")), cursor.getString(cursor.getColumnIndex("image")), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("created_at"))), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("updated_at")))
                );
            case "logiciel":
                return new SofwareItem(
                        cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("subtype")), cursor.getString(cursor.getColumnIndex("description")), cursor.getString(Math.max(0, cursor.getColumnIndex("description"))), cursor.getInt(cursor.getColumnIndex("quantity")), cursor.getString(cursor.getColumnIndex("comment")), cursor.getString(cursor.getColumnIndex("image")), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("created_at"))), Utils.stringToDate(cursor.getString(cursor.getColumnIndex("updated_at")))
                );
        }
        return null;
    }

    public void emptyDatabase() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DATABASE_USERS_TABLE, "id > 3", null);
            db.delete(DATABASE_STOCK_TABLE, null, null);
        } catch (Exception ignored) {
        }
    }
    public void reinitializeStock(String file){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DATABASE_STOCK_TABLE, null, null);
            chargingStockDataBase(db,file);
        } catch (Exception ignored) {}
    }
    private void chargingStockDataBase(SQLiteDatabase db, String file){
        BufferedReader reader=new BufferedReader(new StringReader(file));
        String line;
        try{
            while((line=reader.readLine())!=null){
                String [] tab= line.split(";");
                insertComponentToDatabase(tab[0],tab[1],tab[2],Integer.parseInt(tab[3]),null,null);

            }
        }catch(Exception e){}

    }
    public void reinitilizeRequesterAndStock(String file){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DATABASE_USERS_TABLE, "id > 3", null);
            db.delete(DATABASE_STOCK_TABLE, null, null);
            chargingRequesterAndStock(db,file);

        }catch (Exception ignored){

        }
    }
    private void chargingRequesterAndStock(SQLiteDatabase db, String file){
        BufferedReader reader=new BufferedReader(new StringReader(file));
        String line;
        try{
            while((line=reader.readLine())!=null){
                String [] tab= line.split(";");
                for (String s : tab) {
                    System.out.println(s);
                }
                insertComponentToDatabase(tab[0],tab[1],tab[2],Integer.parseInt(tab[3]),null,null);
                Requester requester = new Requester(tab[4],tab[5],tab[6],tab[7]);
                insertRequester(requester);

            }
        }catch(Exception ignored){}
    }
    public void addNewOrder(long requesterId,ArrayList<ArrayList<OrderItem>> orderItems){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("requester_id", requesterId);
        long order_id = db.insert(DATABASE_ORDERS_TABLE, null, values);

        for(ArrayList<OrderItem> list: orderItems){
            for(OrderItem item: list){
                ContentValues values1 = new ContentValues();
                values1.put("order_id", order_id);
                values1.put("item_id", item.getItemId());
                values1.put("quantity", item.getQuantity());
                db.insert(DATABASE_MY_ORDERS_ITEMS_TABLE, null, values1);
            }
        }
    }
    public ArrayList<Order> getOrders(long requester_id){
        ArrayList<Order> orders = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_ORDERS_TABLE + " where requester_id = ?", new String[]{String.valueOf(requester_id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            orders.add(new Order(
                            cursor.getLong(Math.max(0, cursor.getColumnIndex("id"))),
                            cursor.getLong(Math.max(0, cursor.getColumnIndex("requester_id"))),
                            Utils.stringToDate(cursor.getString(Math.max(0, cursor.getColumnIndex("created_at"))))
                    )
            );
            cursor.moveToNext();
        }
        db.close();
        return orders;
    }

    public int deleteOrder(long orderId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(DATABASE_ORDERS_TABLE, "id = ?", new String[]{String.valueOf(orderId)}) + db.delete(DATABASE_MY_ORDERS_ITEMS_TABLE, "order_id = ?", new String[]{String.valueOf(orderId)});
        } catch (Exception e) {
            return 0;
        }
    }
    public ArrayList<Item> getItemOfOrder(long order_id){
        ArrayList<Item> items= new ArrayList();
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + DATABASE_MY_ORDERS_ITEMS_TABLE + " where order_id = ?" , new String[]{String.valueOf(order_id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            items.add(findItemById(cursor.getLong(Math.max(0, cursor.getColumnIndex("item_id")))));
            cursor.moveToNext();
        }
        db.close();
        return items;


    }
    public void updateState(long order_id,String newState){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("state",newState);
        int therow = db.update(
                DATABASE_ORDERS_TABLE,
                values,
                "id = ?",
                new String[]{String.valueOf(order_id)}
        );
        db.close();

    }
    public String getState(long order_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String state = null;
        String query = "SELECT state FROM " + DATABASE_ORDERS_TABLE + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(order_id)});
        if (cursor!=null){
            if(cursor.moveToFirst()){
                state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
            }
            cursor.close();
        }
        db.close();
        return state;

    }
}