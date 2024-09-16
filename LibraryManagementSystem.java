import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Book {
    private String title;
    private String author;
    private boolean checkedOut;
    private String isbn;
    private LocalDate publicationDate;
    private String genre;

    public Book(String title, String author, String isbn, LocalDate publicationDate, String genre) {
        this.title = title;
        this.author = author;
        this.checkedOut = false;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ", Published: " + publicationDate + ", Genre: " + genre + ")";
    }
}

class Library {
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(String title, String author, String isbn, LocalDate publicationDate, String genre) {
        Book book = new Book(title, author, isbn, publicationDate, genre);
        books.add(book);
    }

    public void searchBook(String keyword, String genre) {
        List<Book> searchResults = new ArrayList<>();

        for (Book book : books) {
            if ((book.getTitle().contains(keyword) || book.getAuthor().contains(keyword)) &&
                (genre == null || book.getGenre().equalsIgnoreCase(genre))) {
                searchResults.add(book);
            }
        }

        if (searchResults.isEmpty()) {
            System.out.println("No books found matching the search criteria.");
        } else {
            System.out.println("Search results:");
            for (Book book : searchResults) {
                System.out.println(book);
            }
        }
    }

    public void checkoutBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                if (!book.isCheckedOut()) {
                    book.setCheckedOut(true);
                    System.out.println("Book '" + title + "' checked out successfully.");
                } else {
                    System.out.println("Book '" + title + "' is already checked out.");
                }
                return;
            }
        }

        System.out.println("Book '" + title + "' not found in the library.");
    }

    public void returnBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                if (book.isCheckedOut()) {
                    book.setCheckedOut(false);
                    System.out.println("Book '" + title + "' returned successfully.");
                } else {
                    System.out.println("Book '" + title + "' is not checked out.");
                }
                return;
            }
        }

        System.out.println("Book '" + title + "' not found in the library.");
    }

    public void saveBooksToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Book book : books) {
                writer.println(String.join(",",
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPublicationDate().format(formatter),
                    book.getGenre(),
                    Boolean.toString(book.isCheckedOut())
                ));
            }
            System.out.println("Books saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving books to file: " + e.getMessage());
        }
    }

    public void loadBooksFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            books.clear();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length < 6) continue; // Skip invalid lines
                String title = parts[0];
                String author = parts[1];
                String isbn = parts[2];
                LocalDate publicationDate = LocalDate.parse(parts[3], formatter);
                String genre = parts[4];
                boolean checkedOut = Boolean.parseBoolean(parts[5]);
                Book book = new Book(title, author, isbn, publicationDate, genre);
                book.setCheckedOut(checkedOut);
                books.add(book);
            }
            System.out.println("Books loaded from file: " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (Exception e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        // Loading books from a file (if available)
        library.loadBooksFromFile("library.txt");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("----- Library Management System -----");
            System.out.println("1. Add a book");
            System.out.println("2. Search for books");
            System.out.println("3. Check out a book");
            System.out.println("4. Return a book");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                System.out.println();

                switch (choice) {
                    case 1:
                        System.out.print("Enter the title of the book: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter the author of the book: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter the ISBN of the book: "); //ISBN-10: 0-306-40615-2
                                                                            //ISBN-13: 978-0-306-40615-7
                        String isbn = scanner.nextLine();
                        System.out.print("Enter the publication date (yyyy-MM-dd): ");
                        LocalDate publicationDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Enter the genre of the book: ");
                        String genre = scanner.nextLine();
                        library.addBook(title, author, isbn, publicationDate, genre);
                        System.out.println("Book added successfully.\n");
                        break;
                    case 2:
                        System.out.print("Enter the search keyword: ");
                        String keyword = scanner.nextLine();
                        System.out.print("Enter the genre to filter by (or press Enter to skip): ");
                        String filterGenre = scanner.nextLine();
                        if (filterGenre.isEmpty()) filterGenre = null;
                        library.searchBook(keyword, filterGenre);
                        System.out.println();
                        break;
                    case 3:
                        System.out.print("Enter the title of the book to check out: ");
                        String checkoutTitle = scanner.nextLine();
                        library.checkoutBook(checkoutTitle);
                        System.out.println();
                        break;
                    case 4:
                        System.out.print("Enter the title of the book to return: ");
                        String returnTitle = scanner.nextLine();
                        library.returnBook(returnTitle);
                        System.out.println();
                        break;
                    case 0:
                        // Saving books to a file
                        library.saveBooksToFile("library.txt");
                        System.out.println("Exiting and saving changes.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.\n");
                }
            } else {
                System.out.println("Invalid input. Please try again.\n");
                scanner.nextLine(); // Consume invalid input
            }
        }

        scanner.close();
    }
}
