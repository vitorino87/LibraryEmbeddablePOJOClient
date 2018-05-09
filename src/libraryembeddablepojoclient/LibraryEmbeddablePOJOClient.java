/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryembeddablepojoclient;

import com.tutorialspoint.entity.Books;
import com.tutorialspoint.entity.Publisher;
import com.tutorialspoint.stateless.LibraryEmbeddablePOJOBeanRemote;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author tiago.lucas
 */
public class LibraryEmbeddablePOJOClient {

    BufferedReader brConsoleReader = null;
    Properties props;
    InitialContext ctx;

    {
        props = new Properties();
        try {
            props.load(new FileInputStream("jndi.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ctx = new InitialContext(props);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        brConsoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LibraryEmbeddablePOJOClient ejbTester = new LibraryEmbeddablePOJOClient();
        ejbTester.testEmbeddedObjects();
    }

    private void showGUI() {
        System.out.println("**********************");
        System.out.println("Welcome to Book Store");
        System.out.println("**********************");
        System.out.print("Options \n1. Add Book\n2. Exit \nEnter Choice: ");
    }

    private void testEmbeddedObjects() {

        try {
            int choice = 1;

            LibraryEmbeddablePOJOBeanRemote libraryBean
                    = (LibraryEmbeddablePOJOBeanRemote) ctx.lookup("LibraryEmbeddablePOJOBean/remote");

            while (choice != 2) {
                String bookName;
                String publisherName;
                String publisherAddress;
                showGUI();
                String strChoice = brConsoleReader.readLine();
                choice = Integer.parseInt(strChoice);
                if (choice == 1) {
                    System.out.print("Enter book name: ");
                    bookName = brConsoleReader.readLine();
                    System.out.print("Enter publisher name: ");
                    publisherName = brConsoleReader.readLine();
                    System.out.print("Enter publisher address: ");
                    publisherAddress = brConsoleReader.readLine();
                    Books book = new Books();
                    book.setName(bookName);
                    Publisher publisher = new Publisher(publisherName, publisherAddress);
                    book.setPublisher(publisher);

                    libraryBean.addBook(book);
                } else if (choice == 2) {
                    break;
                }
            }

            List<Books> booksList = libraryBean.getBooks();

            System.out.println("Book(s) entered so far: " + booksList.size());
            int i = 0;
            for (Books book : booksList) {
                System.out.println((i + 1) + ". " + book.getName());
                System.out.println("Publication: " + book.getPublisher());
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (brConsoleReader != null) {
                    brConsoleReader.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
