package com.example.testaidlservice

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import java.util.*

class BookService : Service() {
    private val TAG = BookService::class.java.simpleName
    private var list: MutableList<Book>? = null
    override fun onCreate() {
        super.onCreate()
        list = ArrayList<Book>()
        for (i in 0..4) {
            val book = Book("第" + i + "本书")
            list!!.add(book)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return bookManager
    }

    private val bookManager: BookManager.Stub = object : BookManager.Stub() {
        override fun getBookList(): List<Book> {
            Log.d(TAG, "getBookList")
            return list!!
        }

        override fun addBook(book: Book?) {
            Log.d(TAG, "addBook")
            if (book != null) {
                list!!.add(book)
            }
            Log.d(TAG, book.toString())
        }
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}