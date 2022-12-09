// IBookManager.aidl
package com.hcs.testaidl;

import com.hcs.testaidl.dao.Book;
import com.hcs.testaidl.IOnNewBookListener;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBooks();

    void addBook(in Book book);

    void register(in IOnNewBookListener listener);

    void unregister(in IOnNewBookListener listener);
}