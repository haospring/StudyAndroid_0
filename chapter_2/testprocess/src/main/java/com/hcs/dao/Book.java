package com.hcs.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String isbn;
    private double price;
    private double author;

    public Book() {
    }

    public Book(String isbn, double price, double author) {
        this.isbn = isbn;
        this.price = price;
        this.author = author;
    }

    protected Book(Parcel in) {
        isbn = in.readString();
        price = in.readDouble();
        author = in.readDouble();
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAuthor() {
        return author;
    }

    public void setAuthor(double author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", price=" + price +
                ", author=" + author +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isbn);
        dest.writeDouble(price);
        dest.writeDouble(author);
    }
}
