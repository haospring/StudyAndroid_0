package com.hcs.testmessenger.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String isbn;
    private String bookName;

    public Book() {
    }

    public Book(String isbn, String bookName) {
        this.isbn = isbn;
        this.bookName = bookName;
    }

    protected Book(Parcel in) {
        isbn = in.readString();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getBookId() {
        return isbn;
    }

    public void setBookId(String bookId) {
        this.isbn = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn +
                ", bookName='" + bookName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isbn);
        dest.writeString(bookName);
    }
}
