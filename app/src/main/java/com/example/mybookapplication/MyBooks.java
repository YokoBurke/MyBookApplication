package com.example.mybookapplication;

public class MyBooks {

    String myBookImage;
    String myBookTitle;
    String myBookAuthor;
    String myBookDate;

    public MyBooks(String bookImage, String bookTitle, String bookAuthor, String bookDate){
        myBookImage = bookImage;
        myBookTitle = bookTitle;
        myBookAuthor = bookAuthor;
        myBookDate = bookDate;
    }

    public String getMyBookImage(){
        return myBookImage; }

    public String getMyBookTitle(){
        return myBookTitle; }

    public String getMyBookAuthor(){
        return myBookAuthor; }

    public String getMyBookDate(){
        return myBookDate; }

}
