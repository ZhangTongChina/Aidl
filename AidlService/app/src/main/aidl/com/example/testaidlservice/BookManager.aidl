// BookManager.aidl
package com.example.testaidlservice;

// Declare any non-default types here with import statements
import com.example.testaidlservice.Book;
import com.example.testaidlservice.IOnNewBookArrivedListener;
interface BookManager {
    List<Book> getBookList();

    void addBook(inout Book book);


    void registerCallback(IOnNewBookArrivedListener  listener);

    void unregisterCallback(IOnNewBookArrivedListener  listener);
}
