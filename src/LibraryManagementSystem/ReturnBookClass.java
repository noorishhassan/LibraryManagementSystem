/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LibraryManagementSystem;

/**
 *
 * @author noorishhassan
 */
public class ReturnBookClass {
    String bookID;
    String memberID;
    String dateDue;
    String dateReturned;
    String fine;
    
    public ReturnBookClass(){
        
    }
    
    public ReturnBookClass(String bookID, String memberID){
        this.bookID = bookID;
        this.memberID = memberID;
    }
    
    public String returnBook(String dueDate, String returnDate, String fineCalculated){
        db obj = new db();
        if (obj.returnBook(this.bookID, this.memberID, dueDate, returnDate, fineCalculated))
            return "Book returned";
        else
            return "Error returning book";
    }
    
    public String findBook(String [] information){
        db obj = new db();
        
        information[0] = null;
        information[1] = null;
        information[2] = null;
        information[3] = null;
        
        if (!obj.memberExists(this.memberID)){
            information[0] = "";
            information[1] = "";
            information[2] = "";
            information[3] = "";
            return "Member does not exist"; 
        }
        
        else if (!obj.bookExistsAndIsIssued(this.bookID)){
            information[0] = "";
            information[1] = "";
            information[2] = "";
            information[3] = "";
            return "Book not Available/Issued"; 
        }
        
        else if (obj.findInIssuedBooks(this.bookID, this.memberID, information))
        {   
            information[3] = obj.findBookCover(this.bookID);
            return "";
        }
        else{
            information[0] = "";
            information[1] = "";
            information[2] = "";
            information[3] = "";
            return "Record not found";
        }
    }
}
