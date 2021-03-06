package com.example.ezmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.ezmanager.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "APPDATABASE"
        private const val TABLE_TRANSACTION = "TransactionTable"
        private const val KEY_TRANSACTION_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DATE = "date"
        private const val KEY_TYPE="type"
        private const val KEY_AMOUNT="amount"
        private const val WORKER_TABLE = "WorkerTable"
        private const val WORKER_ID = "WorkerId"
        private const val WORKER_NAME = "WorkerName"
        private const val WORKER_PAYMENT = "WorkerPayment"
        private const val WORKER_PHONE = "WorkerPhone"
        private const val WORKER_DOJ = "WorkerDoj"
        private const val WORKER_TYPE = "WorkerType"
        private const val CUSTOMER_TABLE = "CustomerTable"
        private const val CUSTOMER_ID = "CustomerId"
        private const val CUSTOMER_NAME = "CustomerName"
        private const val CUSTOMER_PHONE = "CustomerPhone"
        private const val CUSTOMER_ADDRESS = "CustomerAddress"
        private const val CUSTOMER_AREA = "CustomerArea"
        private const val CUSTOMER_DATE = "CustomerDate"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TRANSACTION_TABLE = ("CREATE TABLE $TABLE_TRANSACTION($KEY_TRANSACTION_ID TEXT PRIMARY KEY,$KEY_TITLE TEXT,$KEY_AMOUNT INTEGER,$KEY_DATE TEXT,$KEY_TYPE TEXT)")
        val CREATE_WORKER_TABLE = ("CREATE TABLE $WORKER_TABLE($WORKER_ID TEXT PRIMARY KEY,$WORKER_NAME TEXT, $WORKER_PAYMENT INTEGER, $WORKER_PHONE TEXT, $WORKER_DOJ TEXT, $WORKER_TYPE TEXT)")
        val CREATE_CUSTOMER_TABLE = ("CREATE TABLE $CUSTOMER_TABLE($CUSTOMER_ID TEXT PRIMARY KEY,$CUSTOMER_NAME TEXT, $CUSTOMER_PHONE TEXT, $CUSTOMER_ADDRESS TEXT, $CUSTOMER_AREA INT,$CUSTOMER_DATE TEXT)")
        db.execSQL(CREATE_TRANSACTION_TABLE)
        db.execSQL(CREATE_WORKER_TABLE)
        db.execSQL(CREATE_CUSTOMER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTION")
        db!!.execSQL("DROP TABLE IF EXISTS $WORKER_TABLE")
        db!!.execSQL("DROP TABLE IF EXISTS $CUSTOMER_TABLE")
        onCreate(db)
    }

    //method to insert data
    fun addTransaction(t:Transaction):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TRANSACTION_ID, t.id)
        contentValues.put(KEY_TITLE, t.title) // EmpModelClass Name
        contentValues.put(KEY_AMOUNT,t.amount )
        contentValues.put(KEY_DATE,t.date )
        contentValues.put(KEY_TYPE,t.type)
        // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_TRANSACTION, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    fun addCustomer(c:Customer):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CUSTOMER_ID, c.id)
        contentValues.put(CUSTOMER_NAME, c.name) // EmpModelClass Name
        contentValues.put(CUSTOMER_PHONE,c.phone )
        contentValues.put(CUSTOMER_ADDRESS,c.address )
        contentValues.put(CUSTOMER_AREA,c.area)
        contentValues.put(CUSTOMER_DATE,c.date)
        // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(CUSTOMER_TABLE, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun viewTransactions():List<Transaction>{
        val tList:ArrayList<Transaction> = ArrayList<Transaction>()
        val selectQuery = "SELECT  * FROM $TABLE_TRANSACTION "
        val db = this.readableDatabase
        //var kr de bsdk
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var tId: String
        var tTitle: String
        var tDate: String
        var tAmount:Int
        var tType:String
        if (cursor.moveToFirst()) {
            do {
                tId = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                tTitle= cursor.getString(cursor.getColumnIndexOrThrow("title"))
                tAmount=cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                tDate = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                tType=cursor.getString(cursor.getColumnIndexOrThrow("type"))
                val emp= Transaction(tId,tTitle,tAmount,tDate,tType)
                tList.add(emp)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return tList
    }
    fun viewCustomer():List<Customer>{
        val tList:ArrayList<Customer> = ArrayList<Customer>()
        val selectQuery = "SELECT  * FROM $CUSTOMER_TABLE "
        val db = this.readableDatabase
        //var kr de bsdk
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var cId: String
        var cName: String
        var cPhone: String
        var cAddress:String
        var cArea:String
        var cDate:String
        if (cursor.moveToFirst()) {
            do {
                cId = cursor.getString(cursor.getColumnIndexOrThrow("CustomerId"))
                cName= cursor.getString(cursor.getColumnIndexOrThrow("CustomerName"))
                cPhone=cursor.getString(cursor.getColumnIndexOrThrow("CustomerPhone"))
                cAddress = cursor.getString(cursor.getColumnIndexOrThrow("CustomerAddress"))
                cArea=cursor.getString(cursor.getColumnIndexOrThrow("CustomerArea"))
                cDate=cursor.getString(cursor.getColumnIndexOrThrow("CustomerDate"))
                val customer=Customer(cId,cName,cPhone,cAddress,cArea,cDate)
                tList.add(customer)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return tList
    }
    fun deleteTransaction(t: Transaction){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + TABLE_TRANSACTION+ " WHERE "+ KEY_TRANSACTION_ID+"='"+t.id+"'")
    }
    fun deleteCustomer(c: Customer){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + CUSTOMER_TABLE+ " WHERE "+ CUSTOMER_ID+"='"+c.id+"'")
    }

    fun sumTransaction():Int{
        var totalSum=0
        val selectQuery = "SELECT  * FROM $TABLE_TRANSACTION "
        val db = this.readableDatabase
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var tAmount:Int
        var tType:String
        if (cursor.moveToFirst()) {
            do {
                tAmount=cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                tType=cursor.getString(cursor.getColumnIndexOrThrow("type"))
                if (tType.equals("C"))
                totalSum+=tAmount
                else if (tType.equals("D"))
                    totalSum-=tAmount
            } while (cursor.moveToNext())
            cursor.close()
        }
        return totalSum
    }
    fun sumTransactionToday():Int{
        var totalSum=0
        val selectQuery = "SELECT  * FROM $TABLE_TRANSACTION "
        val db = this.readableDatabase
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var tAmount:Int
        var tType:String
        var tDate:String
        if (cursor.moveToFirst()) {
            do {
                tAmount=cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                tType=cursor.getString(cursor.getColumnIndexOrThrow("type"))
                tDate=cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val myFormat = "dd-MM-yyyy"
                val calendar:Calendar= Calendar.getInstance()
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val todayDate:String=sdf.format(calendar.time)
                if (todayDate.compareTo(tDate)==0){
                    if (tType == "C")
                        totalSum+=tAmount
                    else if (tType == "D")
                        totalSum-=tAmount
                }

            } while (cursor.moveToNext())
            cursor.close()
        }
        return totalSum
    }
    fun sumTransactionDate(date:String):TransactionType{
        var totalSum=0
        var totalCredit=0
        var totalDebit=0
        val selectQuery = "SELECT  * FROM $TABLE_TRANSACTION "
        val db = this.readableDatabase
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return TransactionType(-1,-1,-1)
        }
        var tAmount:Int
        var tType:String
        var tDate:String
        if (cursor.moveToFirst()) {
            do {
                tAmount=cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                tType=cursor.getString(cursor.getColumnIndexOrThrow("type"))
                tDate=cursor.getString(cursor.getColumnIndexOrThrow("date"))
                if (date.compareTo(tDate)==0){
                    if (tType == "C"){
                        totalSum+=tAmount
                        totalCredit+=tAmount
                    }

                    else if (tType == "D"){
                        totalSum-=tAmount
                        totalDebit+=tAmount
                    }

                }

            } while (cursor.moveToNext())
            cursor.close()
        }
        return TransactionType(totalSum,totalCredit,totalDebit)
    }
    fun sumTransactionDateRange(date:String,date2:String):TransactionType{
        var totalSum=0
        var totalCredit=0
        var totalDebit=0
        val selectQuery = "SELECT  * FROM $TABLE_TRANSACTION "
        val db = this.readableDatabase
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return TransactionType(-1,-1,-1)
        }
        var tAmount:Int
        var tType:String
        var tDate:String
        if (cursor.moveToFirst()) {
            do {
                tAmount=cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                tType=cursor.getString(cursor.getColumnIndexOrThrow("type"))
                tDate=cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val dateobject=dateToNum(date)
                val date2Object=dateToNum(date2)
                val tdateObject=dateToNum(tDate)
                if ((tdateObject.dd>=dateobject.dd && tdateObject.dd<=date2Object.dd)&&(tdateObject.mm==dateobject.mm)&&(tdateObject.yy==date2Object.yy) ){
                    if (tType == "C"){
                        totalSum+=tAmount
                        totalCredit+=tAmount
                    }

                    else if (tType == "D"){
                        totalSum-=tAmount
                        totalDebit+=tAmount
                    }

                }

            } while (cursor.moveToNext())
            cursor.close()
        }
        return TransactionType(totalSum,totalCredit,totalDebit)
    }
    fun dateToNum(date: String): CustomDate {

        val d1 = date[0].toString().toInt()
        val d2 = date[1].toString().toInt()
        val day = d1 * 10 + d2
        val m1 = date[3].toString().toInt()
        val m2 = date[4].toString().toInt()
        val month = m1 * 10 + m2
        val y1 = date[6].toString().toInt()
        val y2 = date[7].toString().toInt()
        val y3=  date[8].toString().toInt()
        val y4=  date[9].toString().toInt()
        val year = y1*1000+y2*100+y3*10+y4


        return CustomDate(day, month, year)
    }

    // *************************************************************WORKER DATABASE STARTING ***************************************************************************

    //method to insert data
    fun addWorker(w:Worker):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(WORKER_ID, w.id)
        contentValues.put(WORKER_NAME, w.name) // EmpModelClass Name
        contentValues.put(WORKER_PAYMENT,w.payment )
        contentValues.put(WORKER_PHONE,w.phone )
        contentValues.put(WORKER_DOJ,w.doj )
        contentValues.put(WORKER_TYPE,w.type)
        // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(WORKER_TABLE, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun viewWorker():List<Worker>{
        val wList:ArrayList<Worker> = ArrayList<Worker>()
        val selectQuery = "SELECT  * FROM $WORKER_TABLE "
        val db = this.readableDatabase
        //var kr de bsdk
        val cursor: Cursor?
        try{

            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var wId: String
        var wName: String
        var wDoj: String
        var wPayment:Int
        var wPhone: String
        var wType:String
        if (cursor.moveToFirst()) {
            do {
                wId = cursor.getString(cursor.getColumnIndexOrThrow("WorkerId"))
                wName= cursor.getString(cursor.getColumnIndexOrThrow("WorkerName"))
                wPayment=cursor.getInt(cursor.getColumnIndexOrThrow("WorkerPayment"))
                wPhone= cursor.getInt(cursor.getColumnIndexOrThrow("WorkerPhone")).toString()
                wDoj = cursor.getString(cursor.getColumnIndexOrThrow("WorkerDoj"))
                wType=cursor.getString(cursor.getColumnIndexOrThrow("WorkerType"))
                val emp= Worker(wId,wName,wPayment,wPhone,wDoj,wType)
                wList.add(emp)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return wList
    }

    fun deleteWorker(w: Worker){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + WORKER_TABLE+ " WHERE "+ WORKER_ID +"='"+w.id+"'")
    }

}
