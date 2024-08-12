package ru.marat.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marat.springcourse.models.Book;
import ru.marat.springcourse.models.Person;
import ru.marat.springcourse.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear)
            return bookRepository.findAll(Sort.by("year"));
        else
            return bookRepository.findAll();
    }

    public Book findOne(int book_id) {
        Optional<Book> bookToFind = bookRepository.findById(book_id);
        return bookToFind.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int book_id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(book_id).get();

        updatedBook.setBook_id(book_id);
        updatedBook.setPerson(bookToBeUpdated.getPerson());

        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int book_id) {
        bookRepository.deleteById(book_id);
    }

    public Person getBookOwner(int bookId) {
        return bookRepository.findById(bookId).map(Book::getPerson).orElse(null);
    }

    public List<Book> findWithPagination(int page, int booksPerPage, boolean sortByYear) {
        if(sortByYear)
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    @Transactional
    public void release(int bookId) {

        bookRepository.findById(bookId).ifPresent(
                book -> {
                    book.setPerson(null);
                    book.setTakenAt(null);
                });
    }

    @Transactional
    public void assign(int bookId, Person selectedPerson) {

        bookRepository.findById(bookId).ifPresent(
                book -> {
                    book.setPerson(selectedPerson);
                    book.setTakenAt(new Date());
                });
    }

    public List<Book> searchByName(String query) {
        return bookRepository.findByNameContaining(query);
    }
}
