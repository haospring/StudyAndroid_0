// IBookManager.aidl
package com.hcs.testaidl;

import com.hcs.testaidl.dao.Book;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBooks();

    void addBook(in Book book, in Book test, int abc);
}