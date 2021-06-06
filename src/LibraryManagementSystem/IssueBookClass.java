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
public class IssueBookClass {
    String bookID;
    String memberID;
    String issueDate;
    
    public IssueBookClass(){
        
    }
    
    public IssueBookClass(String bookID, String memberID, String issueDate){
        this.bookID = bookID;
        this.memberID = memberID;
        this.issueDate = issueDate;
    }
    
    public String issueBook(){
        db obj = new db();
        
        if (!obj.bookExists(this.bookID))
            return "Book unavailable";
        else if (!obj.memberExists(this.memberID))
            return "Member does not exist";
        else
        {
            if (obj.issueBook(this.bookID, this.memberID, this.issueDate))
                return "Book issued";
            else
                return "Error issuing book";
        }
    }
    
    public String findBookCover(){
        db obj = new db();
        return obj.findBookCover(this.bookID);
    }
}
