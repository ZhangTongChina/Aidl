package com.example.testaidlclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.testaidlservice.Book
import com.example.testaidlservice.BookManager
import com.example.testaidlservice.IOnNewBookArrivedListener


class MainActivity : AppCompatActivity() {

    private var textView3: TextView? = null
    private var textView4: TextView? = null
    private var textView5: TextView? = null
    private val ACTION1 = "android.intent.action.ConnectService"
    private val ACTION2 = "android.intent.action.BookService"
    private var manager: BookManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);
        textView5 = findViewById(R.id.text5);


        textView3!!.setOnClickListener {
            val intent = Intent(ACTION2)
            // 注意在 Android 5.0以后，不能通过隐式 Intent 启动 service，必须制定包名
            intent.setPackage("com.example.testaidlservice")
            bindService(intent, connection2, Context.BIND_AUTO_CREATE)
        }

        textView4!!.setOnClickListener {
            try {
                if (manager != null) {
                    val list: List<Book> = manager!!.getBookList()
                    Log.d("testService", "getBookList:$list")
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        textView5!!.setOnClickListener {
            val book = Book("添加的书")
            try {
                if (manager != null) {
                    manager!!.addBook(book)
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }


    }

    private val mOnNewBookArrivedListener = object : IOnNewBookArrivedListener.Stub(){
        override fun onNewBookArrived(newBook: Book?) {
            Log.d("testService", "mOnNewBookArrivedListener")
        }

    }

    val connection2: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d("testService", "onServiceConnected")
            manager = BookManager.Stub.asInterface(service)
            manager?.registerCallback(mOnNewBookArrivedListener)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            manager = null
        }
    }

    override fun onDestroy() {
        manager?.let {
            if(it.asBinder().isBinderAlive){
                try {
                    manager?.unregisterCallback(mOnNewBookArrivedListener)
                } catch (e:RemoteException){
                    e.printStackTrace()
                }

            }
        }
        unbindService(connection2)
        super.onDestroy()
    }
}
