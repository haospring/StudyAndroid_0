package com.hcs.testaidl;

import com.hcs.testaidl.dao.Book;

interface IOnNewBookListener {
    void onNewBookArrived(in Book newBook);
}