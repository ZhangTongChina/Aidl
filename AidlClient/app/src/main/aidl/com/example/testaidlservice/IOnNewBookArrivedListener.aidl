// IOnNewBookArrivedListener.aidl
package com.example.testaidlservice;
import com.example.testaidlservice.Book;

interface IOnNewBookArrivedListener {
        void onNewBookArrived(in Book newBook);
}
