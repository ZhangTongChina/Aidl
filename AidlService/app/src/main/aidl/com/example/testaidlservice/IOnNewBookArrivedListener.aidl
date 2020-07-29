// IOnNewBookArrivedListener.aidl
package com.example.testaidlservice;
import com.example.testaidlservice.Book;

interface IOnNewBookArrivedListener {
        //用于接收service端回调 当有新书到货通知客户端
        void onNewBookArrived(in Book newBook);
}
