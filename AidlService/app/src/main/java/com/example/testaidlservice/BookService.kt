package com.example.testaidlservice

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import java.util.*

class BookService : Service() {
    private val TAG = BookService::class.java.simpleName
    var list: MutableList<Book>? = null

    val mListenerList: RemoteCallbackList<IOnNewBookArrivedListener> = RemoteCallbackList()
    override fun onCreate() {
        super.onCreate()
        list = ArrayList<Book>()
        for (i in 0..4) {
            val book = Book("第" + i + "本书")
            list!!.add(book)
        }

        Thread(ServerceWorker()).start()
    }

    override fun onBind(intent: Intent): IBinder? {
        return bookManager
    }

    private val bookManager: BookManager.Stub = object : BookManager.Stub() {
        override fun getBookList(): List<Book> {
            Log.d(TAG, "getBookList")
            return list!!
        }

        override fun unregisterCallback(listener: IOnNewBookArrivedListener?) {
            mListenerList.unregister(listener)
        }

        override fun registerCallback(listener: IOnNewBookArrivedListener?) {
            mListenerList.register(listener)
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

    // 回调，通知每个注册的对象
    fun onNewBookArrived(book: Book) {
        list?.add(book)
        val N = mListenerList.beginBroadcast()
        for (index in 0 until N) {
            val listener = mListenerList.getBroadcastItem(index)
            listener.onNewBookArrived(book)
        }
        // 结束发送通知
        mListenerList.finishBroadcast();
    }

     inner class ServerceWorker : Runnable {
        override fun run() {
            while (true) {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val book = Book("刚刚上架的新书")
                onNewBookArrived(book)
            }
        }

    }
}

