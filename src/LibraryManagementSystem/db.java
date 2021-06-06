/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LibraryManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import static java.util.concurrent.TimeUnit.DAYS;

/**
 *
 * @author noorishhassan
 */
public class db {
    
    Connection con;
    Statement stmt;
   
    db() 
    {
        try
        {
            
            String connection = "jdbc:mysql://localhost:3306/library";
            
            con= (Connection) DriverManager.getConnection(connection,"root","12345678");
            stmt = con.createStatement();
            
            System.out.println("Connection Successful.");
           
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    
    public Connection getConnString(){
        return con;
    }
    
    
    public Statement getStmt(){
        return stmt;
    }
    

    
    public boolean issueBook(String bookID, String memberID, String dateIssued){
        
        try {
            stmt.executeUpdate("UPDATE `library`.`book` SET `availableToIssue` = '0' WHERE (`bookID` = '" + bookID + "');");
            try{
                stmt.executeUpdate("INSERT INTO `library`.`issuedBooks` (`recordID`, `bookID`, `memberID`, `dateIssued`) VALUES (NULL, '" + bookID +"', '" + memberID + "', '" + dateIssued + "');");
                return true;
            }
            catch(Exception e){
                System.out.println(e);
                return false;
            }
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        
    }
    
    public boolean returnBook(String bookID, String memberID, String dateDue, String dateReturned, String fine){
        
        try {
            stmt.executeUpdate("UPDATE `library`.`book` SET `availableToIssue` = '1' WHERE (`bookID` = '" + bookID + "');");
            
            try{
                stmt.executeUpdate("INSERT INTO `library`.`returnedBooks` (`recordID`, `bookID`, `memberID`, `dateDue`, `dateReturned`, `fine`) VALUES (NULL, '" + bookID + "', '" + memberID + "', '" + dateDue + "', '" + dateReturned + "', '" + fine + "');");
                return true;
            }
            catch(Exception e){
                System.out.println(e);
                return false;
            }
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public static Date addDays(Date d, int days)
    {
        d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
        return d;
    }
    
    public boolean findInIssuedBooks(String bookID, String memberID, String [] information){
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM library.issuedBooks WHERE library.issuedBooks.bookID = '" + bookID + "' AND library.issuedBooks.memberID='" + memberID + "' ORDER BY library.issuedBooks.dateIssued DESC LIMIT 1;");
            rs.next();
            
            information[0] = rs.getString(4);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 
            Calendar c = Calendar.getInstance();
            information[1] = sdf.format(c.getTime());
            
            try{
                c.setTime(sdf.parse(information[0]));
            }catch(ParseException e){
                e.printStackTrace();
            }
            
            //Incrementing the date by 30 days
            c.add(Calendar.DAY_OF_MONTH, 30);  
            String newDate = sdf.format(c.getTime());
                       
            information [0] = newDate;
            
            //Parsing the date
            LocalDate dateBefore = LocalDate.parse(information[0]);
            LocalDate dateAfter = LocalDate.parse(information[1]);
		
            //calculating number of days in between
            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
             System.out.println("days " + noOfDaysBetween);
            if (noOfDaysBetween > 0)
                information[2] = String.valueOf(noOfDaysBetween * 50);
            else
                information[2] = String.valueOf(0);
            return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public String findBookCover(String bookID){
        try{
            ResultSet rs = stmt.executeQuery("SELECT library.book.bookCover FROM library.book where library.book.bookID=" + bookID);
            rs.next();
            return rs.getString(1);
        }
        catch(Exception e){
            System.out.println(e);
            return "";
        }
    }
    
    public boolean memberExists(String memberID){
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM library.member where library.member.memberID = " + memberID + ";");
            rs.next();
            if (rs.getString(2) == null)
                return false;
            else
                return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean bookExists(String bookID){
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM library.book where library.book.bookID = " + bookID + " and library.book.availableToIssue = 1;");
            rs.next();
            if (rs.getString(2) == null)
                return false;
            else
                return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean bookExistsAndIsIssued(String bookID){
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM library.book where library.book.bookID = " + bookID + " and library.book.availableToIssue = 0;");
            rs.next();
            if (rs.getString(2) == null)
                return false;
            else
                return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}
