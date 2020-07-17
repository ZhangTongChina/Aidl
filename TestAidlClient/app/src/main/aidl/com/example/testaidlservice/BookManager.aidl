// BookManager.aidl
package com.example.testaidlservice;

// Declare any non-default types here with import statements
import com.example.testaidlservice.Book;
interface BookManager {
    List<Book> getBookList();

    void addBook(inout Book book);
}
